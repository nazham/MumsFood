package dao.custom;

import dao.CrudDAO;
import dto.OrderDTO;
import dto.OrdersDTO;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO extends CrudDAO<OrderDTO> {

    OrderDTO getLastOrder() throws SQLException, ClassNotFoundException;
    String getLastOrderId() throws SQLException, ClassNotFoundException;

    double getTotalSalesOfCurrentDay();

    double getTotalSalesOfCurrentWeek() throws SQLException;

    double getTotalSalesOfCurrentMonth() throws SQLException;
    List<OrdersDTO> getAllOrders() throws SQLException, ClassNotFoundException;
}
