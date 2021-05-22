package natalia.koc.sklepZoologiczny.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

@Entity
@Table(name = "koszykk")
@Setter @Getter
public class Koszyk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer ilosc;
    @NotNull
    private String nazwa;
    @NotNull
    @NumberFormat(style= NumberFormat.Style.CURRENCY)
    private Float cena;
    @NotNull
    @ManyToOne
    private User user;

    public Koszyk(Integer ilosc, String nazwa, Float cena, User user) {
        this.ilosc = ilosc;
        this.nazwa = nazwa;
        this.cena = cena;
        this.user = user;
    }

    public Koszyk() { }
}
