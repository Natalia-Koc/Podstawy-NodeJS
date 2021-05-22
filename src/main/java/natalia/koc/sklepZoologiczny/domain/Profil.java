package natalia.koc.sklepZoologiczny.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Setter @Getter
public class Profil {
    @NotNull
    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Size(min = 3, message = "Imię nie może mieć mniej niż 3 znaki.")
    private String imie;
    @NotNull
    @Size(min = 3, message = "Nazwisko musi mieć więcej niż 3 znaki")
    private String nazwisko;
    @NotNull
    @Size(min = 9, max = 9,message = "Numer telefonu musi zawierać 9 cyfr")
    private String telefon;
    @NotNull
    private String adres;
    @OneToOne
    private User user;

    public Profil() {
        this.user = new User();
    }

    public Profil(
            @Min(0) Integer id,
            @Size(min = 3, message = "Imię nie może mieć mniej niż 3 znaki.") String imie,
            @Size(min = 3, message = "Nazwisko musi mieć więcej niż 3 znaki") String nazwisko,
            @Size(min = 9, max = 9, message = "Numer telefonu musi zawierać 9 cyfr") String telefon,
            String adres) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.telefon = telefon;
        this.adres = adres;
        this.user = new User();
    }
}
