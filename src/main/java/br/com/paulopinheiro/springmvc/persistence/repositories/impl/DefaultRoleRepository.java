package br.com.paulopinheiro.springmvc.persistence.repositories.impl;

import br.com.paulopinheiro.springmvc.persistence.entities.Role;
import br.com.paulopinheiro.springmvc.persistence.repositories.RoleRepository;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultRoleRepository implements RoleRepository {
    @Override
    public Role findByName(String roleName) {
        try (
                var emf = Persistence.createEntityManagerFactory("spring-mvc-pu");
                var em = emf.createEntityManager();
            )
        {
            em.getTransaction().begin();

            TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.name = :roleName", Role.class);
            query.setParameter("roleName", roleName);
            Role role = query.getResultList().stream().findFirst().orElse(null);

            em.getTransaction().commit();

            return role;
        }
    }

    @Override
    public void save(Role role) {
        try (
                var emf = Persistence.createEntityManagerFactory("spring-mvc-pu");
                var em = emf.createEntityManager();
            )
        {
            em.getTransaction().begin();

            em.persist(role);

            em.getTransaction().commit();
        }
    }
}