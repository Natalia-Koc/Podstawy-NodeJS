package natalia.koc.sklepZoologiczny.config;

import natalia.koc.sklepZoologiczny.baza.DatabaseDumps;
import natalia.koc.sklepZoologiczny.domain.Kategoria;
import natalia.koc.sklepZoologiczny.domain.Role;
import natalia.koc.sklepZoologiczny.domain.User;
import natalia.koc.sklepZoologiczny.repositories.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;


@Configuration
public class BazaRepositoriesInitializer {

    @Autowired
    private DostawaRepozytorium dostawaRepozytorium;
    @Autowired
    private ProduktRepozytorium produktRepozytorium;
    @Autowired
    private KategoriaRepozytorium kategoriaRepozytorium;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Bean
    InitializingBean init() {
        return () -> {

            if(dostawaRepozytorium.findAll().isEmpty()) {
                for (var dostaw: DatabaseDumps.dostawy) {
                    dostaw.setDostawaId(null);
                    dostawaRepozytorium.save(dostaw);
                }

                kategoriaRepozytorium.save(new Kategoria("Pies"));
                kategoriaRepozytorium.save(new Kategoria("Kot"));
                kategoriaRepozytorium.save(new Kategoria("Gryzonie"));
                kategoriaRepozytorium.save(new Kategoria("Rybki"));
                var kategoria = kategoriaRepozytorium.findAll();
                var generator = new Random();
                for (var produkt: DatabaseDumps.produkty) {
                    produkt.setId(null);
                    produkt.setKategoria(new HashSet<>());
                    for (int j=0; j < (generator.nextInt(2)+1); j++) {
                        var idx = generator.nextInt(4);
                        produkt.getKategoria().add(kategoria.get(idx));
                    }
                    produktRepozytorium.save(produkt);
                }

                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                Role roleUser = roleRepository.save(new Role(Role.Types.ROLE_USER));
                Role roleAdmin = roleRepository.save(new Role(Role.Types.ROLE_ADMIN));

                User user = new User("user", true);
                user.setRoles(new HashSet<>(Arrays.asList(roleUser)));
                user.setPassword(passwordEncoder.encode("user"));

                User admin = new User("admin", true);
                admin.setRoles(new HashSet<>(Arrays.asList(roleAdmin)));
                admin.setPassword(passwordEncoder.encode("admin"));

                User test = new User("test", true);
                test.setRoles(new HashSet<>(Arrays.asList(roleAdmin, roleUser)));
                test.setPassword(passwordEncoder.encode("test"));

                userRepository.save(user);
                userRepository.save(admin);
                userRepository.save(test);
            }
        };
    }
}
