package dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailTM {
    private String timeStamp;
    private String orderId;
    private String customer;
    private String orderType;
    private String user;
    private double total;
    private JFXButton print;
    private JFXButton delete;

}
