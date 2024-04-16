package dao.custom;

import dao.CrudDAO;
import dto.ItemDTO;
import entity.Item;

import java.sql.SQLException;


public interface ItemDAO extends CrudDAO<Item> {

    ItemDTO getItem(String code) throws SQLException, ClassNotFoundException;

}
