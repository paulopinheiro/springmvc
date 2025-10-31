package br.com.paulopinheiro.springmvc.persistence.repositories;

import br.com.paulopinheiro.springmvc.persistence.entities.Privilege;

public interface PrivilegeRepository {
    Privilege findByName(String privilegeName);
    void save(Privilege privilege);
}
