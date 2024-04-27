package dao.custom.impl;

import dao.custom.CustomerDAO;
import dao.util.HibernateUtil;
import db.DBConnection;
import dto.CustomerDTO;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public CustomerDTO searchCustomer(String phnNum) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM customer WHERE phone_number=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1, phnNum);
        ResultSet resultSet=pstm.executeQuery();
        if(resultSet.next()){
            return  new CustomerDTO(
                    resultSet.getInt(1),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(2)

            );
        }
        return null;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Customer customer = session.find(Customer.class, entity.getCustomerId());
            customer.setName(entity.getName());
            customer.setAddress(entity.getAddress());
            customer.setPhoneNumber(entity.getPhoneNumber());
            session.update(customer);
            transaction.commit(); // Commit the transaction
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback the transaction if an exception occurs
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.find(Customer.class,Integer.parseInt(value)));
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Customer");
        List<Customer> list = query.list();
        session.close();
        return list;
    }
}
