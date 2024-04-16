package bo.custom;

import bo.SuperBO;
import dto.CategoryDTO;
import dto.CustomerDTO;

import java.sql.SQLException;
import java.util.List;

public interface CategoryBO extends SuperBO {
    boolean saveCategory(CategoryDTO dto) throws SQLException, ClassNotFoundException;
    boolean updateCategory(CategoryDTO dto) throws SQLException, ClassNotFoundException;
    boolean deleteCategory(String id) throws SQLException, ClassNotFoundException;
    List<CategoryDTO> allCategory() throws SQLException, ClassNotFoundException;

}
