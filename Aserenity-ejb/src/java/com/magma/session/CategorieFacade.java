/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Categorie;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author inzo
 */
@Stateless
public class CategorieFacade extends AbstractFacade<Categorie> implements CategorieFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategorieFacade() {
        super(Categorie.class);
    }
    
    @Override
    public boolean verifierUnique(String libelle, Long idParent) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Categorie as o  where o.Cat_Libelle like '" + libelle + "' and o.Cat_IdParent = '" + idParent + "' ", Categorie.class).setHint(QueryHints.REFRESH, HintValues.TRUE);

        try {
            List<Categorie> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifierUnique(String libelle, Long idParent, Long id) {
//        Query q = (Query) getEntityManager().createQuery("Select object(o) from Categorie as o where o.id <> " + id + " and ( o.libelle like '" + libelle + "'   and o.parent.id = " + idParent + " ) ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Categorie as o  where o.Cat_Id <> " + id + " and  o.Cat_Libelle like '" + libelle + "' and o.Cat_IdParent = '" + idParent + "'", Categorie.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Categorie> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifierUnique(String libelle, int rang) {
//         Query q = (Query) getEntityManager().createQuery("Select object(o) from Categorie as o where o.libelle like '" + libelle + "'  and o.rang = " + rang + " ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Categorie as o  where o.Cat_Libelle like '" + libelle + "' and o.Cat_Rang = '" + rang + "'", Categorie.class).setHint(QueryHints.REFRESH, HintValues.TRUE);

        try {
            List<Categorie> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifierUnique(String libelle, int rang, Long id) {
//        Query q = (Query) getEntityManager().createQuery("Select object(o) from Categorie as o where o.id <> " + id + " and ( o.libelle like '" + libelle + "'   and o.rang = " + rang + " ) ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Categorie as o  where o.Cat_Id <> " + id + " and  ( o.Cat_Libelle like '" + libelle + "' and o.Cat_Rang = '" + rang + "' )", Categorie.class).setHint(QueryHints.REFRESH, HintValues.TRUE);

        try {
            List<Categorie> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
     public boolean verifierUnique(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Categorie as o "+clause+" ", Categorie.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Categorie> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
    
}
