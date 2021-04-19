package natalia.koc.sklepZoologiczny.repositories;


import natalia.koc.sklepZoologiczny.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByType(Role.Types roleUser);
}
