package bo.custom.impl;

import bo.custom.OrderBO;
import dao.custom.OrderDAO;
import dao.util.DAOFactory;
import dao.util.DAOType;
import dto.OrderDTO;

import java.sql.SQLException;

public class OrderBOImpl implements OrderBO {

    private OrderDAO orderDao = DAOFactory.getInstance().getDao(DAOType.ORDER);

    @Override
    public boolean saveOrder(OrderDTO dto) throws SQLException, ClassNotFoundException {
        return orderDao.save(dto);
    }

    @Override
    public String generateId() {
        try {
            OrderDTO dto = orderDao.getLastOrder();
            if (dto!=null){
                String id = dto.getOrderId();
                int num = Integer.parseInt(id.split("[D]")[1]);
                num++;
                return String.format("D%03d",num);
            }else{
                return "D001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
