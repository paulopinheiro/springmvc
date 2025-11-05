package br.com.paulopinheiro.springmvc.persistence.repositories.springdata;

import br.com.paulopinheiro.springmvc.persistence.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository<U> extends CrudRepository<User, Long> {
    List<User> findByFirstName(String firstName);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) = LOWER(:firstName)")
    List<User> getByFirstNameCaseInsensitive(@Param("firstName") String firstName);

    @Query(value="SELECT * FROM users ORDER BY firstname", nativeQuery=true)
    List<User> getAllUsersOrderByFirstName();
}
