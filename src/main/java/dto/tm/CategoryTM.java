package dto.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class CategoryTM extends RecursiveTreeObject<CategoryTM> {
    private String id;
    private String categoryName;
    private JFXButton btn;
}
