package natalia.koc.sklepZoologiczny.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile(ProfileName.USERS_IN_MEMORY)
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        User.UserBuilder userBuilder = User.builder();

        UserDetails admin = userBuilder
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();
        UserDetails user = userBuilder
                .username("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();
        UserDetails superUser = userBuilder
                .username("superUser")
                .password(passwordEncoder.encode("superUser"))
                .roles("USER", "ADMIN")
                .build();
        manager.createUser(admin);
        manager.createUser(user);
        manager.createUser(superUser);
        return manager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/webjars/**", "/css/**", "/images/**", "/produkty/showList**").permitAll()
                .antMatchers("/produkty/szegolyProduktu/**").hasRole("USER")
                .antMatchers("/produkty/add**", "/produkty/**", "/produkty/usun/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated();//każde żądanie ma być uwierzytelnione
        http
                .formLogin()//uwierzytelnienie poprzez formularz logowania
                .loginPage("/login")//formularz dostępny jest pod URL
                .permitAll();//przyznaj dostęp wszystkim użytkownikom

        http.exceptionHandling().accessDeniedHandler(createDeniedHandler());
    }

    private AccessDeniedHandler createDeniedHandler() {
        AccessDeniedHandlerImpl impl = new AccessDeniedHandlerImpl();
        impl.setErrorPage("/error403");//nie jest to nazwa widoku tylko url!!!
        return impl;
    }
}
