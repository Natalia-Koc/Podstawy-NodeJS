package natalia.koc.sklepZoologiczny.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter @Getter
public class Historia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer nrZamowienia;
    @NotNull
    private String stanZamowienia;
    @NotNull
    private LocalDate dataZamowienia;
    @NotNull
    private Float cena;
    @NotNull
    private LocalDate przewidywanyCzasDostawy;
    @ManyToOne
    private User user;
    private Boolean czyUserUsunal;
    private Boolean czyAdminUsunal;

    public Historia(Integer nrZamowienia, String stanZamowienia, LocalDate dataZamowienia, Float cena, LocalDate przewidywanyCzasDostawy, User user, Boolean czyUserUsunal, Boolean czyAdminUsunal) {
        this.nrZamowienia = nrZamowienia;
        this.stanZamowienia = stanZamowienia;
        this.dataZamowienia = dataZamowienia;
        this.cena = cena;
        this.przewidywanyCzasDostawy = przewidywanyCzasDostawy;
        this.user = user;
        this.czyUserUsunal = czyUserUsunal;
        this.czyAdminUsunal = czyAdminUsunal;
    }

    public Historia() {

    }
}
