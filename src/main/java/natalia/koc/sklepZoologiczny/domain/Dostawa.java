package natalia.koc.sklepZoologiczny.domain;

import io.micrometer.core.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dostawy")
@Getter @Setter
public class Dostawa {
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dostawaId;
    @NonNull
    @Size(min=3, max=50, message = "Nazwa musi zawierac od {min} do {max} znakow.")
    @Column(name = "nazwa_firm", nullable = false)
    private String nazwaFirmy;
    @NonNull
    @Size(min=3, max=50, message = "Nazwa musi zawierac od {min} do {max} znakow.")
    @Column(name = "czas_dostawy", nullable = false)
    private String czasDostawy;
    @NonNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @Column(name = "cena", nullable = false)
    private float cena;

    public Dostawa(Integer dostawaId, String nazwaFirmy, String czasDostawy, float cena) {
        this.dostawaId = dostawaId;
        this.nazwaFirmy = nazwaFirmy;
        this.czasDostawy = czasDostawy;
        this.cena = cena;
    }

    public Dostawa() {}
}
