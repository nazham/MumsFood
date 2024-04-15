package dao.custom.impl;

import dao.custom.ItemDao;
import dao.util.CrudUtil;
import db.DBConnection;
import dto.ItemDto;
import entity.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {
    @Override
    public ItemDto getItem(String code) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM item WHERE code=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, code);
        ResultSet resultSet=pstm.executeQuery();
        if(resultSet.next()){
            return  new ItemDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)

            );
        }
        return null;
    }

//    @Override
//    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {
//        List<ItemDto> list = new ArrayList<>();
//        String sql = "SELECT * FROM item";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        ResultSet resultSet = pstm.executeQuery();
//        while (resultSet.next()){
//            list.add(new ItemDto(
//                    resultSet.getString(1),
//                    resultSet.getString(2),
//                    resultSet.getDouble(3),
//                    resultSet.getInt(4)
//
//            ));
//        }
//        return list;
//    }

    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO item VALUES(?,?,?,?)";

        return CrudUtil.execute(sql,entity.getCode(),entity.getDescription(),entity.getQtyOnHand(),entity.getUnitPrice());
        //changed due to swap in Qty & UnitPrice
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Item SET Description=?, UnitPrice=?, qtyOnHand=? WHERE code=?";
        return CrudUtil.execute(sql, entity.getDescription(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getCode());
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        String sql = "DELETE from item WHERE code=?";
        return CrudUtil.execute(sql,value);
    }

    @Override
    public List<Item> getAll() throws SQLException, ClassNotFoundException {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT * FROM item";
        ResultSet resultSet = CrudUtil.execute(sql);
        while (resultSet.next()){
            list.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(4), // Changed 3->4 due to Swap in UnitPrice & QTYOnHand in ItemTable
                    resultSet.getInt(3)

            ));
        }
        return list;
    }
}
