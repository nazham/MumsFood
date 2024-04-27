package dao.custom;

import dao.CrudDAO;
import dto.CategoryDTO;
import entity.Category;


public interface CategoryDAO extends CrudDAO<Category> {
   CategoryDTO searchCategory();

}
