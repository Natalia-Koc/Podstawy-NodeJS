package natalia.koc.sklepZoologiczny.services.impl;


import natalia.koc.sklepZoologiczny.config.ProfileName;
import natalia.koc.sklepZoologiczny.domain.Role;
import natalia.koc.sklepZoologiczny.repositories.RoleRepository;
import natalia.koc.sklepZoologiczny.repositories.UserRepository;
import natalia.koc.sklepZoologiczny.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
@Profile(ProfileName.USERS_IN_DATABASE)
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return convertToUserDetails(user);
    }

    private UserDetails convertToUserDetails(natalia.koc.sklepZoologiczny.domain.User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getType().toString()));
        }
        return new User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, grantedAuthorities);
    }

    @Override
    public void saveUser(natalia.koc.sklepZoologiczny.domain.User user) {
        var userRole = roleRepository.findRoleByType(Role.Types.ROLE_USER);
        var roles = new HashSet<Role>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordConfirm(user.getPassword());
        userRepository.save(user);
    }
}
