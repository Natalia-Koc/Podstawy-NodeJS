package natalia.koc.sklepZoologiczny.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Getter @Setter
public class Kategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Valid
    public String name;

    public Kategoria(String name){
        this.name = name;
    }

    @ManyToMany(mappedBy = "kategoria", fetch= FetchType.EAGER)
    private Set<Produkt> produkt;

    public Kategoria(){
        this.produkt = new HashSet<>();//Inicjalizacja zbioru
    }
}
