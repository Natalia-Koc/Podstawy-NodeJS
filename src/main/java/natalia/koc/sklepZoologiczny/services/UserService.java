package natalia.koc.sklepZoologiczny.services;

import natalia.koc.sklepZoologiczny.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void saveUser(User userForm);
}
