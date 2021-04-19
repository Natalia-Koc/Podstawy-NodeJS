package natalia.koc.sklepZoologiczny.domain;

import com.sun.istack.NotNull;
import io.micrometer.core.lang.NonNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produkty")
@Getter @Setter
@NamedQuery(name = "Produkt.findProduktUsingNameQuery",
query =
        "SELECT b FROM Produkt b WHERE" +
                "(" +
                ":phrase is null OR :phrase = '' OR " +
                "upper(b.nazwa) LIKE upper(:phrase) OR " +
                "upper(b.opis) LIKE upper(:phrase)" +
                " ) " +
                "AND (:minCena is null OR :minCena <= b.cena)" +
                "AND (:maxCena is null OR :maxCena >= b.cena)" +
                "AND (:minOcena is null OR :minOcena <= b.ocena)" +
                "AND (:maxOcena is null OR :maxOcena >= b.ocena)" +
                "AND (COALESCE(:kategoria) is null OR EXISTS (SELECT g FROM b.kategoria g WHERE g in :kategoria))" +
                ""
)
public class Produkt {
    @NotNull
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min=5, max=100)
    @Column(name = "nazwa", nullable = false)
    private String nazwa;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min=10, max=1000)
    @Column(name = "opis", nullable = false)
    private String opis;

    @NumberFormat(style = NumberFormat.Style.PERCENT)
    @NotNull
    @Column(name = "ocena", nullable = false)
    private Float ocena;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotNull
    @Min(0)
    @Column(name = "cena", nullable = false)
    private Float cena;

    @Column(name = "dostepnosc_na_magazynie", nullable = false)
    private Boolean dostepnoscNaMagazynie;

    @Valid
    @ManyToOne
    private Dostawa dostawa;

    @ManyToMany
    private Set<Kategoria> kategoria;


    public Produkt() {
        dostawa = new Dostawa();
        kategoria = new HashSet<>();
    }

    public Produkt(Integer id, String nazwa, String opis, float ocena, float cena, Boolean dostepnoscNaMagazynie) {
        this.id = id;
        this.nazwa = nazwa;
        this.opis = opis;
        this.ocena = ocena;
        this.cena = cena;
        this.dostepnoscNaMagazynie = dostepnoscNaMagazynie;
        kategoria = new HashSet<>();
    }
}
