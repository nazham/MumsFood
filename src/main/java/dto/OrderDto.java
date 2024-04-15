package dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderDto {
    private String orderId;
    private String date;
    private String cusId;
    private List<OrderDetailsDto> list;

}