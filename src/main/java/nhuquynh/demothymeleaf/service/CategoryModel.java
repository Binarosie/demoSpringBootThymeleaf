package nhuquynh.demothymeleaf.service;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int ID;
    @NotEmpty(message = "Khong duoc bo trong")
    private String name;
    private String images;
    private int status;

    private Boolean isEdit=false;
}
