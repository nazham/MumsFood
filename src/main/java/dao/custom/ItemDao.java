package dao.custom;

import dao.CrudDao;
import dto.ItemDto;
import entity.Item;

import java.sql.SQLException;


public interface ItemDao extends CrudDao<Item> {

    ItemDto getItem(String code) throws SQLException, ClassNotFoundException;

}
