package dao.custom.impl;

import dao.custom.OrderDetailDAO;
import dao.util.CrudUtil;
import db.DBConnection;
import dto.OrderDetailDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public OrderDetailDTO searchOrder(OrderDetailDTO orderDetailDTO) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean saveOrderDetails(List<OrderDetailDTO> list) throws SQLException, ClassNotFoundException {
        boolean isDetailsSaved = true;
        for (OrderDetailDTO dto:list) {
            String sql = "INSERT INTO order_detail VALUES(?,?,?,?)";
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(2, dto.getOrderId());
            pstm.setString(1, dto.getItemCode());
            pstm.setInt(3, dto.getQty());
            pstm.setDouble(4, dto.getUnitPrice());

            if (!(pstm.executeUpdate()>0)){
                isDetailsSaved = false;
            }
        }
        return isDetailsSaved;
    }

    @Override
    public List<OrderDetailDTO> getAll() throws SQLException, ClassNotFoundException {
        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM order_detail");

        while(resultSet.next()) {
            orderDetailDTOList.add(new OrderDetailDTO(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            ));
        }

        return orderDetailDTOList;
    }
}
