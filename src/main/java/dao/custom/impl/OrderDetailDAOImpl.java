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
            pstm.setInt(1, dto.getItemId());
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


        PreparedStatement itemPstm = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM item WHERE id=?");

        while (resultSet.next()) {
            // Retrieve the order detail attributes
            String orderId = resultSet.getString(2);
            int itemId = resultSet.getInt(1);
            int qty = resultSet.getInt(3);

            // Set the item ID as a parameter in the item prepared statement
            itemPstm.setInt(1, itemId);

            // Execute the query to retrieve the item details
            ResultSet itemResultSet = itemPstm.executeQuery();
            if (itemResultSet.next()) {
                // Retrieve the item details
                double unitPrice = itemResultSet.getDouble("price");

                // Create the OrderDetailDTO object and add it to the list
                orderDetailDTOList.add(new OrderDetailDTO(orderId, itemId, qty, unitPrice));
            }

            // Close the item result set
            itemResultSet.close();
        }

        // Close the prepared statement and result sets
        itemPstm.close();
        resultSet.close();

        return orderDetailDTOList;
    }



}
