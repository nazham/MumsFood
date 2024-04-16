package dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter


public class CustomerDto {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;

    public CustomerDto(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}