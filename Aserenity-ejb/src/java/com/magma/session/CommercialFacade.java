/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Commercial;
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
public class CommercialFacade extends AbstractFacade<Commercial> implements CommercialFacadeLocal {
    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommercialFacade() {
        super(Commercial.class);
    }
    
        @Override
    public List<Commercial> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Commercial as o " + clause + " ", Commercial.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    @Override
    public boolean verifierUnique(String email) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Commercial as o where o.email like '" + email + "' ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Commercial> Ts = q.getResultList();
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
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Commercial as o where o.id <> " + id + " and o.email like '" + email + "' ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Commercial> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
}
