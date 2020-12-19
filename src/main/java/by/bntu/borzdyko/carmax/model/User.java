package by.bntu.borzdyko.carmax.model;

import lombok.*;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@Builder
@Proxy(lazy = false)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    @Email(message = "E-mail should be valid")
    @NotEmpty(message = "E-mail cannot be empty")
    @Size(min = 8, max = 64, message = "E-mail should be between 6 and 64")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 4, max = 255, message = "Password should be between 4 and 255")
    private String password;

    @Column(name = "first_name")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 50, message = "Name should be between 2 and 50")
    private String firstName;

    @Column(name = "second_name")
    @NotEmpty(message = "Sure name cannot be empty")
    @Size(min = 2, max = 50, message = "Sure name should be between 2 and 50")
    private String secondName;

    @Column(name = "status")
    private Boolean status;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders;
}
