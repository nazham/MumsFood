package bo.custom.impl;

import bo.custom.CategoryBO;
import dao.custom.CategoryDAO;
import dao.util.DAOFactory;
import dao.util.DAOType;
import dto.CategoryDTO;
import entity.Category;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryBOImpl implements CategoryBO {
    private CategoryDAO customerDao = DAOFactory.getInstance().getDao(DAOType.CATEGORY);

    @Override
    public boolean saveCategory(CategoryDTO dto) throws SQLException, ClassNotFoundException {
        return customerDao.save(new Category(
                dto.getId(),
                dto.getCategoryName()
        ));
    }

    @Override
    public boolean updateCategory(CategoryDTO dto) throws SQLException, ClassNotFoundException {
        return customerDao.update(new Category(
                dto.getId(),
                dto.getCategoryName()
        ));
    }

    @Override
    public boolean deleteCategory(String id) throws SQLException, ClassNotFoundException {
        return customerDao.delete(id);
    }

    @Override
    public List<CategoryDTO> allCategory() throws SQLException, ClassNotFoundException {
        List<Category> entityList = customerDao.getAll();
        List<CategoryDTO> list = new ArrayList<>();
        for (Category category:entityList) {
            list.add(new CategoryDTO(
                    category.getCategoryId(),
                    category.getName()
            ));
        }
        return list;
    }
}
