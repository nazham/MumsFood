package dto;

import entity.Customer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrdersDTO {
    private String orderId;
    private LocalDateTime dateTime;
    private Customer customer;
    private Double totalAmount;
    private String orderType;
    private String userId;

    private List<OrderDetailDTO> list;

}