package dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderDto {
    private String orderId;
    private LocalDateTime dateTime;
    private String cusId;
    private Double totalAmount;
    private String orderType;
    private String userId;

    private List<OrderDetailsDto> list;

}