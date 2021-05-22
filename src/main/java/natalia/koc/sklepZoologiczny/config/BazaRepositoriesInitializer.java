package natalia.koc.sklepZoologiczny.config;

import natalia.koc.sklepZoologiczny.baza.DatabaseDumps;
import natalia.koc.sklepZoologiczny.domain.Kategoria;
import natalia.koc.sklepZoologiczny.domain.Zwierzeta;
import natalia.koc.sklepZoologiczny.domain.Role;
import natalia.koc.sklepZoologiczny.domain.User;
import natalia.koc.sklepZoologiczny.repositories.*;
import natalia.koc.sklepZoologiczny.services.PhotoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.awt.event.KeyAdapter;
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
    private ZwierzetaRepozytorium zwierzetaRepozytorium;
    @Autowired
    private KategoriaRepozytorium kategoriaRepozytorium;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KoszykRepozytorium koszykRepozytorium;
    @Autowired
    private HistoriaRepozytorium historiaRepozytorium;

    @Bean
    InitializingBean init() {
        return () -> {

            if(dostawaRepozytorium.findAll().isEmpty()) {
                for (var dostaw: DatabaseDumps.dostawy) {
                    dostaw.setDostawaId(null);
                    dostawaRepozytorium.save(dostaw);
                }
                zwierzetaRepozytorium.save(new Zwierzeta("Pies"));
                zwierzetaRepozytorium.save(new Zwierzeta("Kot"));
                zwierzetaRepozytorium.save(new Zwierzeta("Gryzonie"));
                zwierzetaRepozytorium.save(new Zwierzeta("Rybki"));
                zwierzetaRepozytorium.save(new Zwierzeta("Inne"));

                kategoriaRepozytorium.save(new Kategoria("Zwierzeta"));
                kategoriaRepozytorium.save(new Kategoria("Pokarm"));
                kategoriaRepozytorium.save(new Kategoria("Akcesoria"));
                kategoriaRepozytorium.save(new Kategoria("Zdrowie"));
                kategoriaRepozytorium.save(new Kategoria("Inne"));


                var zwierzeta = zwierzetaRepozytorium.findAll();
                var generator = new Random();
                for (var produkt: DatabaseDumps.produkty) {
                    produkt.setId(null);
                    produkt.setZwierzeta(new HashSet<>());
                    for (int j=0; j < (generator.nextInt(2)+1); j++) {
                        var idx = generator.nextInt(4);
                        produkt.getZwierzeta().add(zwierzeta.get(idx));
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
