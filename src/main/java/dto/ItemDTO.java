package dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter



public class ItemDTO {
    private String code;
    private String desc;
    private double unitPrice;
    private String categoryId;

}
