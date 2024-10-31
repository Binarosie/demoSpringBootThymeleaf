package nhuquynh.demothymeleaf.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "category")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryID")
    private Long ID;

    @NotEmpty(message = "Khong duoc bo trong")
    @Column(name = "categoryName", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "images", columnDefinition = "NVARCHAR(500)")
    private String images;

    @Column(name = "Status")
    private int status;


}