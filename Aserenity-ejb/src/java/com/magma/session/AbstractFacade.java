/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceException;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author Symatique
 * @param <T>
 */
public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();
//

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public boolean createBoolean(T entity) {
        try {
            getEntityManager().persist(entity);
            return true;
        } catch (PersistenceException e) {
            System.out.println("PersistenceException: " + e.getMessage());
            return false;
        } catch (DatabaseException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void create(List<T> entitys) {
        for (T entity : entitys) {
            getEntityManager().persist(entity);
        }
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void edit(List<T> entitys) {
        for (T entity : entitys) {
            getEntityManager().merge(entity);
        }
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public void remove(List<T> entitys) {
        for (T entity : entitys) {
            getEntityManager().remove(getEntityManager().merge(entity));
        }
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);//.setHint(QueryHints.REFRESH, HintValues.TRUE);
    }

//    public List<T> findAll() {
//        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        cq.select(cq.from(entityClass));
//        return getEntityManager().createQuery(cq).getResultList();
//    }
    public List<T> findAll() {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from " + entityClass.getSimpleName() + " as o ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    public List<T> findAll(String clause) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from " + entityClass.getSimpleName() + " as o " + clause + " ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

//    public List<T> findRange(int[] range) {
//        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        cq.select(cq.from(entityClass));
//        javax.persistence.Query q = getEntityManager().createQuery(cq);
//        q.setMaxResults(range[1] - range[0] + 1);
//        q.setFirstResult(range[0]);
//        return q.getResultList();
//    }
    public List<T> findRange(int[] range) {
        Query q = getEntityManager().createQuery("Select object(o) from " + entityClass.getSimpleName() + " as o").setHint(QueryHints.REFRESH, HintValues.TRUE);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
//    public int count() {
//        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
//        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
//        javax.persistence.Query q = getEntityManager().createQuery(cq);
//        return ((Long) q.getSingleResult()).intValue();
//    }

    public int count() {
        Query q = getEntityManager().createQuery("Select count(o) from " + entityClass.getSimpleName() + " as o").setHint(QueryHints.REFRESH, HintValues.TRUE);
        return ((Long) q.getSingleResult()).intValue();
    }

    /* public boolean verifierUnique(String libelle) {
     Query q = (Query) getEntityManager().createQuery("Select object(o) from " + entityClass.getSimpleName() + " as o where o.libelle like :libelle ").setParameter("libelle", libelle).setHint(QueryHints.REFRESH, HintValues.TRUE);
     try {
     List<T> Ts = q.getResultList();
     return Ts.size() > 0;
     } catch (Exception e) {
     return false;
     }
     }

     public boolean verifierUnique(String libelle, Object id) {
     Query q = (Query) getEntityManager().createQuery("Select object(o) from " + entityClass.getSimpleName() + " as o where o.id <> " + id + " and o.libelle like :libelle ").setParameter("libelle", libelle).setHint(QueryHints.REFRESH, HintValues.TRUE);
     try {
     List<T> Ts = q.getResultList();
     return Ts.size() > 0;
     } catch (Exception e) {
     return false;
     }
     }*/
    public boolean verifierUnique(String libelle) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from " + entityClass.getSimpleName() + " as o where o.libelle like :libelle ").setParameter("libelle", libelle).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<T> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifierUnique(String libelle, Object id) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from " + entityClass.getSimpleName() + " as o where o.id <> " + id + " and o.libelle like :libelle ").setParameter("libelle", libelle).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<T> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

}
