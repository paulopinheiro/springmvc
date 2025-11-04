package br.com.paulopinheiro.springmvc.service;

import br.com.paulopinheiro.springmvc.persistence.entities.User;
import br.com.paulopinheiro.springmvc.persistence.repositories.springdata.UserJpaRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private UserJpaRepository<User> userRepository;

    @Transactional
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional
    public List<User> findByFirstName(String name) {
        return userRepository.findByFirstName(name);
    }

    @Transactional
    public List<User> findByFirstNameCaseInsensitive(String firstName) {
        return userRepository.getByFirstNameCaseInsensitive(firstName);
    }

    @Transactional
    public User getById(Long id) {
        return userRepository.findById(id).orElseGet(null);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public boolean addUser(User user) {
        return userRepository.save(user) != null;
    }

    @Transactional
    public boolean updateUser(User user) {
        return userRepository.save(user) != null;
    }

    @Transactional
    public List<User> getAllUsersOrderedByFirstName() {
        return userRepository.getAllUsersOrderByFirstName();
    }
}
