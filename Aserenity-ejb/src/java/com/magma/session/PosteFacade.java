/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Facture;
import com.magma.entity.Poste;
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
public class PosteFacade extends AbstractFacade<Poste> implements PosteFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PosteFacade() {
        super(Poste.class);
    }
    
    @Override
    public List<Poste> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Poste as o " + clause + " ", Poste.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
   @Override
     public boolean verifierUnique(String libelle, Long idDepartement) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Poste as o where o.Pst_Libelle like '" + libelle + "' and o.Dep_Id =  "+idDepartement, Poste.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Poste> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean verifierUnique(String libelle, Long id, Long idDepartement) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Poste as o where o.Pst_Id <> " + id + " and o.Pst_Libelle like '" + libelle + "' and o.Dep_Id =  "+idDepartement, Poste.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Poste> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
}
