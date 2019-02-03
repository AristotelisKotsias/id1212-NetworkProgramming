package db.jpa.server.integration;

import db.jpa.server.model.Client;
import javax.persistence.*;

public class ClientDAO {

    private final EntityManagerFactory emFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    public ClientDAO() {
        emFactory = Persistence.createEntityManagerFactory("filePersistenceUnit");
    }

    public Client findCliendByName(String username) {
        try {
            EntityManager em = beginTransaction();
            try {
                Query query = em.createQuery("SELECT u FROM Client u WHERE u.username=:username");
                query.setParameter("username", username);
                return (Client) query.getSingleResult();
            } catch (NoResultException noSuchName) {
                return null;
            }
        } finally {
            commitTransaction();
        }

    }

    public Client findCliendByPass(String password) {
        try {
            EntityManager em = beginTransaction();
            try {
                Query query = em.createQuery("SELECT u FROM Client u WHERE u.password=:password");
                query.setParameter("password", password);
                return (Client) query.getSingleResult();
            } catch (NoResultException noSuchPassword) {
                return null;
            }
        } finally {
            commitTransaction();
        }

    }

    public void registerClient(Client client) {
        try {
            EntityManager entityManager = beginTransaction();
            entityManager.persist(client);
        } finally {
            commitTransaction();
        }
    }

    public void deleteClient(Client client) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(entityManager.merge(client));
        commitTransaction();
    }

    private EntityManager beginTransaction() {
        EntityManager em = emFactory.createEntityManager();
        threadLocalEntityManager.set(em);
        EntityTransaction transaction = em.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        return em;
    }

    private void commitTransaction() {
        threadLocalEntityManager.get().getTransaction().commit();
    }
}
