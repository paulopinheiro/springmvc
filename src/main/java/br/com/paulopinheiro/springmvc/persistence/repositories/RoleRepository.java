package br.com.paulopinheiro.springmvc.persistence.repositories;

import br.com.paulopinheiro.springmvc.persistence.entities.Role;

public interface RoleRepository {
    Role findByName(String roleName);
    void save(Role role);
}
