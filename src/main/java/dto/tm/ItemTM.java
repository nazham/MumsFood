package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class ItemTM extends RecursiveTreeObject<ItemTM> {
    private int id;
    private String code;
    private String desc;
    private Double unitPrice;
    private String categoryId;
    private JFXButton btn;

    public ItemTM(int id, String code, String desc, Double unitPrice, String categoryId) {
        this.id = id;
        this.code = code;
        this.desc = desc;
        this.unitPrice = unitPrice;
        this.categoryId = categoryId;
    }
}
