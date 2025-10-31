package br.com.paulopinheiro.springmvc.security;

import br.com.paulopinheiro.springmvc.persistence.entities.Privilege;
import br.com.paulopinheiro.springmvc.persistence.entities.Role;
import br.com.paulopinheiro.springmvc.persistence.entities.User;
import br.com.paulopinheiro.springmvc.persistence.repositories.RoleRepository;
import br.com.paulopinheiro.springmvc.persistence.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Transactional
public class DefaultUserDetailsService implements UserDetailsService {
    @Autowired private UserRepository userRepository;
    @Autowired RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (Optional.ofNullable(user).isEmpty()) {
            return new org.springframework.security.core.userdetails.User(
                    "", "",
                    true, true, true, true,
                 getAuthorities(Arrays.asList(roleRepository.findByName(SetupDataLoader.ROLE_USER))));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.isEnabled(), true, true, true,
                getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();

        for (Role role: roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }

        for (Privilege item: collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String privilege: privileges) authorities.add(new SimpleGrantedAuthority(privilege));

        return authorities;
    }
}
