/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Utilisateur;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author inzo
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> implements UtilisateurFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }

    @Override
    public boolean verifierUnique(String email) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Utilisateur as o where o.email like '" + email + "' ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Utilisateur> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifierUnique(String email, Long id) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Utilisateur as o where o.id <> " + id + " and o.email like '" + email + "' ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Utilisateur> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifierUnique(String email, String code) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Utilisateur as o where o.email like '" + email + "' or o.code like '" + code + "'  ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Utilisateur> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            //System.out.println("verifierUnique1:: "+e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifierUnique(String email, String code, Long id) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Utilisateur as o where o.id <> " + id + " and ( o.email like '" + email + "' or o.code like '" + code + "' )  ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Utilisateur> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Utilisateur findUserByEmail(String email) {

        try {
            Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Utilisateur as o where o.Usr_Email = ?1", Utilisateur.class).setParameter(1, email).setHint(QueryHints.REFRESH, HintValues.TRUE);
            return (Utilisateur) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }
    
    @Override
    public List<Utilisateur> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Utilisateur as o " + clause + " ", Utilisateur.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

}
