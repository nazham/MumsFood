package bo.custom;

import bo.SuperBO;
import dto.OrderDTO;

import java.sql.SQLException;

public interface OrderBO extends SuperBO {
    boolean saveOrder(OrderDTO dto) throws SQLException, ClassNotFoundException;
    String generateId();


}
