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
    private final CategoryDAO categoryDAO = DAOFactory.getInstance().getDao(DAOType.CATEGORY);

    @Override
    public boolean saveCategory(CategoryDTO dto) throws SQLException, ClassNotFoundException {
        return categoryDAO.save(new Category(
                dto.getId(),
                dto.getCategoryName()
        ));
    }

    @Override
    public boolean updateCategory(CategoryDTO dto) throws SQLException, ClassNotFoundException {
        return categoryDAO.update(new Category(
                dto.getId(),
                dto.getCategoryName()
        ));
    }

    @Override
    public boolean deleteCategory(String id) throws SQLException, ClassNotFoundException {
        return categoryDAO.delete(id);
    }

    @Override
    public List<CategoryDTO> allCategory() throws SQLException, ClassNotFoundException {
        List<Category> entityList = categoryDAO.getAll();
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
