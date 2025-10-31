package br.com.paulopinheiro.springmvc.security;

import br.com.paulopinheiro.springmvc.persistence.entities.Privilege;
import br.com.paulopinheiro.springmvc.persistence.entities.Role;
import br.com.paulopinheiro.springmvc.persistence.entities.User;
import br.com.paulopinheiro.springmvc.persistence.repositories.PrivilegeRepository;
import br.com.paulopinheiro.springmvc.persistence.repositories.RoleRepository;
import br.com.paulopinheiro.springmvc.persistence.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    public static final String DELETE_PRIVILEGE = "DELETE_PRIVILEGE";
    public static final String WRITE_PRIVILEGE = "WRITE_PRIVILEGE";
    public static final String READ_PRIVILEGE = "READ_PRIVILEGE";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";

    private boolean isAlreadyConfigured;

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PrivilegeRepository privilegeRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (isAlreadyConfigured) return;

        Privilege readPrivilege = this.createPrivilegeIfNotFound(READ_PRIVILEGE);
        Privilege writePrivilege = this.createPrivilegeIfNotFound(WRITE_PRIVILEGE);
        Privilege deletePrivilege = this.createPrivilegeIfNotFound(DELETE_PRIVILEGE);

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, deletePrivilege);
        List<Privilege> managerPrivileges = Arrays.asList(readPrivilege, writePrivilege);

        createRoleIfNotFound(ROLE_ADMIN,adminPrivileges);
        createRoleIfNotFound(ROLE_MANAGER, managerPrivileges);
        createRoleIfNotFound(ROLE_USER, Arrays.asList(readPrivilege));

        Role adminRole = roleRepository.findByName(ROLE_ADMIN);
        Role managerRole = roleRepository.findByName(ROLE_MANAGER);

        System.out.println("Creating users:");
        createUserIfNotFound("admin@test.com", adminRole, "admin");
        createUserIfNotFound("manager@test.com",managerRole,"manager");

        isAlreadyConfigured = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (Optional.ofNullable(privilege).isEmpty()) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }

        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (Optional.ofNullable(role).isEmpty()) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    private void createUserIfNotFound(String email, Role role, String password) {
        User user = userRepository.findByEmail(email);

        if (Optional.ofNullable(user).isEmpty()) {
            user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setRoles(Arrays.asList(role));
            user.setEnabled(true);

            userRepository.save(user);
        }
    }
}
