package dao.custom.impl;

import dao.custom.ItemDao;
import dao.util.CrudUtil;
import dao.util.HibernateUtil;
import db.DBConnection;
import dto.ItemDto;
import entity.Category;
import entity.Customer;
import entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

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
                    resultSet.getString(4)

            );
        }
        return null;
    }

    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            // Retrieve the associated Category entity using its ID
            Category category = session.find(Category.class, entity.getCategory().getCategoryId());

            // Create a new Item entity instance
            Item item = new Item();
            item.setCode(entity.getCode());
            item.setDescription(entity.getDescription());
            item.setPrice(entity.getPrice());
            item.setCategory(category);

            // Save the Item entity
            session.save(item);

            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
//        String sql = "UPDATE Item SET Description=?, Price=?, qtyOnHand=? WHERE code=?";
//        return CrudUtil.execute(sql, entity.getDescription(),entity.getPrice(),entity.getQtyOnHand(),entity.getCode());
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        String sql = "DELETE from item WHERE code=?";
        return CrudUtil.execute(sql,value);
    }

    @Override
    public List<Item> getAll() throws SQLException, ClassNotFoundException {
//        List<Item> list = new ArrayList<>();
//        String sql = "SELECT * FROM item";
//        ResultSet resultSet = CrudUtil.execute(sql);
//        while (resultSet.next()){
//            list.add(new Item(
//                    resultSet.getInt(1),
//                    resultSet.getString(2),
//                    resultSet.getString(4), // Changed 3->4 due to Swap in UnitPrice & QTYOnHand in ItemTable
//                    resultSet.getInt(3)
//
//            ));
//        }
//        return list;
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Item");
        List<Item> list = query.list();
        session.close();
        return list;
    }
}
