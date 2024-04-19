package dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter


public class CustomerDTO {
    private int id;
    private String name;
    private String phoneNumber;
    private String address;

    public CustomerDTO(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}