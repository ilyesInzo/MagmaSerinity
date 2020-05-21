/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Categorie;
import com.magma.entity.CategorieClient;
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
public class CategorieClientFacade extends AbstractFacade<CategorieClient> implements CategorieClientFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategorieClientFacade() {
        super(CategorieClient.class);
    }
    
    @Override
    public boolean verifierUnique(String libelle, Long idParent) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_CategorieClient as o  where o.CCl_Libelle like '" + libelle + "' and o.CCl_IdParent = '" + idParent + "' ", CategorieClient.class).setHint(QueryHints.REFRESH, HintValues.TRUE);

        try {
            List<CategorieClient> Ts = q.getResultList();
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
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_CategorieClient as o  where o.CCl_Id <> " + id + " and  o.CCl_Libelle like '" + libelle + "' and o.CCl_IdParent = '" + idParent + "'", CategorieClient.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<CategorieClient> Ts = q.getResultList();
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
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_CategorieClient as o  where o.CCl_Libelle like '" + libelle + "' and o.CCl_Rang = '" + rang + "'", CategorieClient.class).setHint(QueryHints.REFRESH, HintValues.TRUE);

        try {
            List<CategorieClient> Ts = q.getResultList();
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
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_CategorieClient as o  where o.CCl_Id <> " + id + " and  ( o.CCl_Libelle like '" + libelle + "' and o.CCl_Rang = '" + rang + "' )", CategorieClient.class).setHint(QueryHints.REFRESH, HintValues.TRUE);

        try {
            List<CategorieClient> Ts = q.getResultList();
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
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_CategorieClient as o "+clause+" ", CategorieClient.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<CategorieClient> Ts = q.getResultList();
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
