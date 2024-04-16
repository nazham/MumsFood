package bo.custom.impl;

import bo.custom.CustomerBO;
import dao.custom.CustomerDAO;
import dao.util.DAOFactory;
import dao.util.DAOType;
import dto.CustomerDTO;
import entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {
    private CustomerDAO customerDao = DAOFactory.getInstance().getDao(DAOType.CUSTOMER);
    @Override
    public boolean saveCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDao.save(new Customer(
                dto.getPhoneNumber(),
                dto.getName(),
                dto.getAddress()
        ));
    }

    @Override
    public boolean updateCustomer(CustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDao.update(new Customer(
                Integer.parseInt(dto.getId()),
                dto.getPhoneNumber(),
                dto.getName(),
                dto.getAddress()
        ));
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDao.delete(id);
    }

    @Override
    public List<CustomerDTO> allCustomers() throws SQLException, ClassNotFoundException {
        List<Customer> entityList = customerDao.getAll();
        List<CustomerDTO> list = new ArrayList<>();
        for (Customer customer:entityList) {
            list.add(new CustomerDTO(
                    Integer.toString(customer.getCustomerId()),
                    customer.getName(),
                    customer.getPhoneNumber(),
                    customer.getAddress()
            ));
        }
        return list;
    }
}
