/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.PrefixFacture;
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
public class PrefixFactureFacade extends AbstractFacade<PrefixFacture> implements PrefixFactureFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrefixFactureFacade() {
        super(PrefixFacture.class);
    }
    
     @Override
    public List<PrefixFacture> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_PrefixFacture as o " + clause + " ", PrefixFacture.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    public boolean verifierUnique(String libellePrefixe) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from PrefixFacture as o where o.libellePrefixe like :libellePrefixe ").setParameter("libellePrefixe", libellePrefixe).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<PrefixFacture> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifierUnique(String libellePrefixe, Object id) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from PrefixFacture as o where o.id <> " + id + " and o.libellePrefixe like :libellePrefixe ").setParameter("libellePrefixe", libellePrefixe).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<PrefixFacture> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
}
