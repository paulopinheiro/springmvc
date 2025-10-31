package br.com.paulopinheiro.springmvc.persistence.repositories.impl;

import br.com.paulopinheiro.springmvc.persistence.entities.User;
import br.com.paulopinheiro.springmvc.persistence.repositories.UserRepository;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultUserRepository implements UserRepository {
    @Override
    public void save(User user) {
        try (
                var emf = Persistence.createEntityManagerFactory("spring-mvc-pu");
                var em = emf.createEntityManager();
            )
        {
            em.getTransaction().begin();

            em.persist(user);

            em.getTransaction().commit();
        }
    }

    @Override
    public User findByEmail(String email) {
        try (
                var emf = Persistence.createEntityManagerFactory("spring-mvc-pu");
                var em = emf.createEntityManager();
            )
        {
            em.getTransaction().begin();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(root);
            criteriaQuery.where(criteriaBuilder.equal(root.get("email"), email));
            List<User> list = em.createQuery(criteriaQuery).getResultList();
            if (Optional.ofNullable(list).isEmpty() || list.isEmpty()) return null;

            User user = list.get(0);

            em.getTransaction().commit();

            return user;
        }
    }
}
