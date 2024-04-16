package bo;

import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.ItemBOImpl;
import bo.custom.impl.OrderBOImpl;
import dao.util.BOType;

public class BOFactory {
    private static BOFactory boFactory;
    private BOFactory(){
    }
    public static BOFactory getInstance(){
        return boFactory!=null? boFactory:(boFactory = new BOFactory());
    }

    public <T extends SuperBO>T getBo(BOType type){
        switch (type){
            case CUSTOMER: return (T) new CustomerBOImpl();
            case ITEM: return (T) new ItemBOImpl();
            case ORDER: return (T) new OrderBOImpl();
//            case ORDER_DETAIL: return (T) new OrderDetailsBoImpl();
        }
        return null;
    }
}
