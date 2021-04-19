package natalia.koc.sklepZoologiczny.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Types type;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
    public Role(Types type){
        this.type = type;
    }
    public static enum Types{
        ROLE_ADMIN,
        ROLE_USER
    }
}

