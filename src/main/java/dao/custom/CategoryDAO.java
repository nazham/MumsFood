package dao.custom;

import dao.CrudDAO;
import dto.CategoryDTO;
import dto.CustomerDTO;
import entity.Category;
import entity.Customer;


public interface CategoryDAO extends CrudDAO<Category> {
   CategoryDTO searchCategory();

}
