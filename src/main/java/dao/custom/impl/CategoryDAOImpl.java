package dao.custom.impl;

import dao.custom.CategoryDAO;
import dao.custom.CustomerDAO;
import dao.util.HibernateUtil;
import dto.CategoryDTO;
import dto.CustomerDTO;
import entity.Category;
import entity.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean save(Category entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Category entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();

        Transaction transaction = session.beginTransaction();
        try {
            Category category = session.find(Category.class, entity.getCategoryId());
            category.setName(entity.getName());
            session.update(category);
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
    public List<Category> getAll() throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Category");
        List<Category> list = query.list();
        session.close();
        return list;
    }

    @Override
    public CategoryDTO searchCategory() {
        return null;
    }
}
