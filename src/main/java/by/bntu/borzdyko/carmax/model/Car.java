package by.bntu.borzdyko.carmax.model;

import by.bntu.borzdyko.carmax.model.description.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car")
@Proxy(lazy = false)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    @NotEmpty(message = "Price cannot be empty")
    @DecimalMin(value = "0.0", message = "Price should be greater than 0.0")
    @DecimalMax(value = "10000000.0", message = "Price is so hight")
    private BigDecimal price;

    @Column(name = "year_of_issue")
    @NotEmpty(message = "Year cannot be empty")
    @Min(value = 1990, message = "Year cannot be lower than 1990")
    @Max(value = 2020, message = "Year cannot be higher than current year")
    private Integer yearOfIssue;

    @Column(name = "mileage")
    // ??
    private Double mileage;

    @Column(name = "file_name")
    @Size(min = 1, max = 220, message = "Filename should be between 1 and 220")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id")
    @NotNull(message = "Color cannot be null")
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    @NotNull(message = "Country cannot be null")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transmission_id")
    @NotNull(message = "Transmission cannot be null")
    private Transmission transmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @NotNull(message = "Brand cannot be null")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @NotNull(message = "Model cannot be null")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuel_id")
    @NotNull(message = "Fuel cannot be null")
    private Fuel fuel;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private List<Order> orders;
}
