package natalia.koc.sklepZoologiczny.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    /*
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kategoria kategoria = (Kategoria) o;
        return Objects.equals(id, kategoria.id) &&
                Objects.equals(name, kategoria.name);
    }

    @Override
    public int hashCode() {return Objects.hash(id, name);}*/
}
