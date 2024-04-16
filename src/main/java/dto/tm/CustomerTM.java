package dto.tm;


import javafx.scene.control.Button;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class CustomerTM {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private Button btn;

    public CustomerTM(String id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}

