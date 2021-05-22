package natalia.koc.sklepZoologiczny.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.websocket.OnMessage;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produkt")
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
                "AND (COALESCE(:zwierzeta) is null OR EXISTS (SELECT g FROM b.zwierzeta g WHERE g in :zwierzeta))" +
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
    @Size(min=5, max=300, message = "Nazwa musi zawierać min-5 i max-300 znaków")
    @Column(name = "nazwa", nullable = false)
    private String nazwa;

    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min=10, max=1500, message = "Opis musi zawierać min-10 i max-1500 znaków")
    @Column(name = "opis", nullable = false)
    private String opis;

    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    @NotNull
    @Min(0)
    @Column(name = "cena", nullable = false)
    private Float cena;

    @Column(name = "dostepnosc_na_magazynie", nullable = false)
    private Boolean dostepnoscNaMagazynie;

    //@Valid
    //@ManyToOne
    //private Dostawa dostawa;

    @ManyToMany
    private Set<Zwierzeta> zwierzeta;

    @ManyToMany
    private Set<Kategoria> kategoria;

    private PhotoDesc photo;


    public Produkt() {
        zwierzeta = new HashSet<>();
        kategoria = new HashSet<>();
        photo = new PhotoDesc();
    }

    public Produkt(Integer id, String nazwa, String opis, float cena, Boolean dostepnoscNaMagazynie) {
        this.id = id;
        this.nazwa = nazwa;
        this.opis = opis;
        this.cena = cena;
        this.dostepnoscNaMagazynie = dostepnoscNaMagazynie;
        this.zwierzeta = new HashSet<>();
        this.kategoria = new HashSet<>();
        this.photo = new PhotoDesc();
    }
}
