package br.com.paulopinheiro.springmvc.persistence.repositories.impl;

import br.com.paulopinheiro.springmvc.persistence.entities.Privilege;
import br.com.paulopinheiro.springmvc.persistence.repositories.PrivilegeRepository;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultPrivilegeRepository implements PrivilegeRepository {
    @Override
    public Privilege findByName(String privilegeName) {
        try (
                var emf = Persistence.createEntityManagerFactory("spring-mvc-pu");
                var em = emf.createEntityManager();
            )
        {
            em.getTransaction().begin();

            TypedQuery<Privilege> query = em.createQuery("SELECT p FROM Privilege p WHERE p.name = :privilegeName", Privilege.class);
            query.setParameter("privilegeName", privilegeName);
            Privilege privilege = query.getResultList().stream().findFirst().orElse(null);

            em.getTransaction().commit();

            return privilege;
        }
    }

    @Override
    public void save(Privilege privilege) {
        try (
                var emf = Persistence.createEntityManagerFactory("spring-mvc-pu");
                var em = emf.createEntityManager();
            )
        {
            em.getTransaction().begin();

            em.persist(privilege);

            em.getTransaction().commit();
        }
    }
}
