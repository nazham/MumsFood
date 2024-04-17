package dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter



public class ItemDTO {
    private int id;
    private String code;
    private String desc;
    private double unitPrice;
    private String categoryId;

    public ItemDTO(String code, String desc, double unitPrice, String categoryId) {
        this.code = code;
        this.desc = desc;
        this.unitPrice = unitPrice;
        this.categoryId = categoryId;
    }
}
