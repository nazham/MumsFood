package dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderDTO {
    private String orderId;
    private LocalDateTime dateTime;
    private int cusId;
    private Double totalAmount;
    private String orderType;
    private String userId;

    private List<OrderDetailDTO> list;

}