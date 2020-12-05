package by.bntu.borzdyko.carmax.model.description;

import by.bntu.borzdyko.carmax.model.Car;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fuel")
@Proxy(lazy = false)
@Builder
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @NotEmpty(message = "Fuel type cannot be empty")
    @Size(min = 4, max = 25, message = "Fuel type should be between 4 and 25")
    private String type;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fuel")
    private List<Car> cars;

}
