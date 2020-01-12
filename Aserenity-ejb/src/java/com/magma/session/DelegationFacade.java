/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Delegation;
import com.magma.entity.Gouvernorat;
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
public class DelegationFacade extends AbstractFacade<Delegation> implements DelegationFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DelegationFacade() {
        super(Delegation.class);
    }
    
        @Override
    public List<Delegation> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Delegation as o " + clause + " ", Delegation.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
}
