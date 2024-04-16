package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class OrderTM extends RecursiveTreeObject<OrderTM> {
    private String code;
    private String desc;
    private int qty;
    private double amount;
    private JFXButton btn;
}
