package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderDetailDTO {
    private String orderId;
    private int itemId;
    private int qty;
    private double unitPrice;
}
