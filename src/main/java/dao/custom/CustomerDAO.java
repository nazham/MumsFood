package dao.custom;

import dao.CrudDAO;
import dto.CustomerDTO;
import entity.Customer;

import java.sql.SQLException;


public interface CustomerDAO extends CrudDAO<Customer> {
   CustomerDTO searchCustomer(String phnNum) throws SQLException, ClassNotFoundException;

}
