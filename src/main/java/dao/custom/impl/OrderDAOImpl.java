package dao.custom.impl;

import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dao.util.CrudUtil;
import dao.util.HibernateUtil;
import db.DBConnection;
import dto.OrderDetailDTO;
import dto.OrderDTO;
import entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
                    resultSet.getString(3),
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
        if (!resultSet.next()) return "D001";
        return resultSet.getString(1);
    }

    @Override
    public boolean save(OrderDTO dto) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
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
                    new OrderDetailKey(detailDto.getOrderId(), detailDto.getItemCode()),
                    session.find(Item.class, detailDto.getItemCode()),
                    orders,
                    detailDto.getQty()
            );
            session.save(orderDetail);
        }

        transaction.commit();
        session.close();
        return true;
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
    public List<OrderDTO> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}
