package br.com.paulopinheiro.springmvc.persistence.repositories;

import br.com.paulopinheiro.springmvc.persistence.entities.User;

public interface UserRepository {
    void save(User user);
    User findByEmail(String email);
}
