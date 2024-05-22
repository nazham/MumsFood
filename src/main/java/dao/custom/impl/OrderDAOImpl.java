package dao.custom.impl;

import controller.TextFieldUtils;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dao.util.CrudUtil;
import dao.util.HibernateUtil;
import db.DBConnection;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.OrdersDTO;
import entity.*;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    OrderDetailDAO orderDetailDao = new OrderDetailDAOImpl();

    @Override
    public OrderDTO getLastOrder() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM orders ORDER BY id DESC LIMIT 1";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()){
            Timestamp timestamp = resultSet.getTimestamp(2);
            LocalDateTime dateTime = timestamp.toLocalDateTime();
            return  new OrderDTO(
                    resultSet.getString(1),
                    dateTime,
                    Integer.parseInt(resultSet.getString(3)),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    null

            );
        }
        return null;
    }

    @Override
    public String getLastOrderId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM orders ORDER BY id DESC LIMIT 1");
        if (!resultSet.next()) return null;
        return resultSet.getString("id");
    }

    @Override
    public boolean save(OrderDTO dto) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Orders orders = new Orders(
                    dto.getOrderId(),
                    dto.getDateTime(),
                    dto.getOrderType(),
                    dto.getTotalAmount()
            );
            orders.setCustomer(session.find(Customer.class,dto.getCusId()));
            session.save(orders);

            List<OrderDetailDTO> list = dto.getList(); //dto type

            for (OrderDetailDTO detailDto:list) {
                OrderDetail orderDetail = new OrderDetail(
                        new OrderDetailKey(detailDto.getOrderId(), detailDto.getItemId()),
                        session.find(Item.class, detailDto.getItemId()),
                        orders,
                        detailDto.getQty()
                );
                session.save(orderDetail);
            }

            transaction.commit();
            session.close();
            return true;
        } catch (ConstraintViolationException ex) {
            // Log specific message for ConstraintViolationException
            TextFieldUtils.showAlert(Alert.AlertType.ERROR, "Error", "Constraint violation error: " + ex.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
            return false;
        } catch (PersistenceException ex) {
            // Log specific message for PersistenceException
            TextFieldUtils.showAlert(Alert.AlertType.ERROR, "Error", "Persistence error: " + ex.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
            return false;
        } catch (Exception ex) {
            // Log generic message for any other exception
            TextFieldUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error: " + ex.getMessage());
            if (transaction.isActive()) {
                transaction.rollback();
            }
            session.close();
            return false;
        }
    }

    @Override
    public boolean update(OrderDTO entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<OrdersDTO> getAllOrders() throws SQLException, ClassNotFoundException{
        List<OrdersDTO> orderDTOList = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            Query<Orders> query = session.createQuery("FROM Orders", Orders.class);
            List<Orders> ordersList = query.getResultList();

            for (Orders orders : ordersList) {
                OrdersDTO ordersDTO = new OrdersDTO(
                        orders.getOrderId(),
                        orders.getDateTime(),
                        orders.getCustomer(),
                        orders.getTotalAmount(),
                        orders.getOrderType(),
                        orders.getUser() != null ? String.valueOf(orders.getUser().getUserId()) : null,
                        new ArrayList<>()
                );

                for (OrderDetail orderDetail : orders.getOrderDetails()) {
                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO(
                            orderDetail.getId().getOrderId(),
                            orderDetail.getItem().getItemId(),
                            orderDetail.getQty(),
                            orderDetail.getItem().getPrice()
                    );
                    ordersDTO.getList().add(orderDetailDTO);
                }

                orderDTOList.add(ordersDTO);
            }
        }
        return orderDTOList;
    }

    public List<OrderDTO> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }


    @Override
    public double getTotalSalesOfCurrentDay() {
        try (Session session = HibernateUtil.getSession()) {
            LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(00, 0));
            LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59));

            Query<Double> query = session.createQuery("SELECT SUM(o.totalAmount) FROM Orders o WHERE o.dateTime BETWEEN :startDateTime AND :endDateTime", Double.class);
            query.setParameter("startDateTime", startDateTime);
            query.setParameter("endDateTime", endDateTime);

            Double totalSales = query.uniqueResult();

            return totalSales != null ? totalSales : 0.0;
        }
    }
    @Override
    public double getTotalSalesOfCurrentWeek() throws SQLException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        double totalSales = 0.0;

        try {
            // Calculate the start and end of the current week
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfWeek = now.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
            LocalDateTime endOfWeek = startOfWeek.plusDays(7).minusNanos(1);

            // Query to get total sales for the current week
            Query<Double> query = session.createQuery("SELECT COALESCE(SUM(o.totalAmount), 0) " +
                    "FROM Orders o " +
                    "WHERE o.dateTime BETWEEN :startOfWeek AND :endOfWeek", Double.class);
            query.setParameter("startOfWeek", startOfWeek);
            query.setParameter("endOfWeek", endOfWeek);
            totalSales = query.getSingleResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        return totalSales;
    }

    @Override
    public double getTotalSalesOfCurrentMonth() throws SQLException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        double totalSales = 0.0;

        try {
            // Calculate the start and end of the current month
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

            // Query to get total sales for the current month
            Query<Double> query = session.createQuery("SELECT COALESCE(SUM(o.totalAmount), 0) " +
                    "FROM Orders o " +
                    "WHERE o.dateTime BETWEEN :startOfMonth AND :endOfMonth", Double.class);
            query.setParameter("startOfMonth", startOfMonth);
            query.setParameter("endOfMonth", endOfMonth);
            totalSales = query.getSingleResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        return totalSales;
    }


}
