package dao.custom.impl;

import controller.TextFieldUtils;
import dao.custom.CategoryDAO;
import dao.util.HibernateUtil;
import dto.CategoryDTO;
import entity.Category;
import javafx.scene.control.Alert;
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
    public boolean delete(String categoryId) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            Category category = session.find(Category.class, categoryId);
            if (category != null) {
                // Check if there are any associated items
                if (!isCategoryEmpty(categoryId)) {
                    // If there are associated items, do not delete the category
                    TextFieldUtils.showAlert(Alert.AlertType.ERROR,"Error", "Cannot delete category. Associated items exist.");
                    return false;
                }
                session.delete(category);
                transaction.commit();
                return true;
            } else {
                System.out.println("Category not found.");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
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

    // Method to check if a category has associated items
    private boolean isCategoryEmpty(String categoryId) {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Item WHERE category.categoryId = :categoryId", Long.class);
            query.setParameter("categoryId", categoryId);
            Long count = query.uniqueResult();
            return count == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
