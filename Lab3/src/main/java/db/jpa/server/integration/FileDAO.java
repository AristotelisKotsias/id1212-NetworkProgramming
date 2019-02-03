package db.jpa.server.integration;

import javax.persistence.*;
import db.jpa.server.model.Client;
import db.jpa.server.model.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FileDAO {

    private final EntityManagerFactory emFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<>();

    public FileDAO() {
        emFactory = Persistence.createEntityManagerFactory("filePersistenceUnit");
    }

    public void storeFile(File file) {
        try {
            EntityManager entityManager = beginTransaction();
            entityManager.persist(file);
        } finally {
            commitTransaction();
        }
    }

    public void deleteFile(File file) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(entityManager.merge(file));
        commitTransaction();
    }

    public void updateFile(File file) {
        EntityManager entityManager = beginTransaction();
        entityManager.merge(file);
        commitTransaction();
    }

    public File findFileByName(String name) {
        try {
            EntityManager em = beginTransaction();
            try {
                Query query = em.createQuery("SELECT f FROM File f WHERE f.name=:name");
                query.setParameter("name", name);
                return (File) query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        } finally {
            commitTransaction();
        }
    }

    public List<File> listAllFiles(Client owner) {

        EntityManager em = beginTransaction();

        Query query = em.createQuery("SELECT f FROM File f WHERE (f.isPrivate=false OR f.owner=:owner)");
        query.setParameter("owner", owner);
        List<File> files;

        try {
            files = query.getResultList();
        } catch (NoResultException e) {
            files = new ArrayList<>();
        }

        commitTransaction();
        return files;
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
