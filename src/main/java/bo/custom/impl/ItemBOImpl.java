package bo.custom.impl;


import bo.custom.ItemBO;
import dao.custom.ItemDAO;
import dao.util.DAOFactory;
import dao.util.DAOType;
import dto.ItemDTO;
import entity.Category;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
    private ItemDAO itemDao = DAOFactory.getInstance().getDao(DAOType.ITEM);

    @Override
    public boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        Item item =new Item(
                dto.getCode(),
                dto.getDesc(),
                dto.getUnitPrice()
        );
        item.setCategory(new Category(dto.getCategoryId(),"dummy"));
        return itemDao.save(item);

    }

    @Override
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException {
        Item item =new Item(
                dto.getId(),
                dto.getCode(),
                dto.getDesc(),
                dto.getUnitPrice()
        );
        item.setCategory(new Category(dto.getCategoryId(),"dummy"));
        return itemDao.update(item);
    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        return itemDao.delete(id);
    }

    @Override
    public List<ItemDTO> allItems() throws SQLException, ClassNotFoundException {
        List<Item> entityList = itemDao.getAll();
        List<ItemDTO> list = new ArrayList<>();
        for (Item item :entityList) {
            list.add(new ItemDTO(
                    item.getItemId(),
                    item.getCode(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getCategory().getName()
            ));
        }
        return list;
    }
}
