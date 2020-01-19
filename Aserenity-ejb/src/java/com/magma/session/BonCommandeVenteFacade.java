/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.BonCommandeVente;
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
public class BonCommandeVenteFacade extends AbstractFacade<BonCommandeVente> implements BonCommandeVenteFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BonCommandeVenteFacade() {
        super(BonCommandeVente.class);
    }
    
    @Override
    public List<BonCommandeVente> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_BonCommandeVente as o " + clause + " ", BonCommandeVente.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    @Override
    public boolean verifierUniqueNumero(String numero) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from BonCommandeVente as o where o.numero like '" + numero + "'").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<BonCommandeVente> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
}
