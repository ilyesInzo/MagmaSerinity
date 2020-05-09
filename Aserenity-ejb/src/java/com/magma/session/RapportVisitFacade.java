/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.RapportVisit;
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
public class RapportVisitFacade extends AbstractFacade<RapportVisit> implements RapportVisitFacadeLocal {
    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RapportVisitFacade() {
        super(RapportVisit.class);
    }
    
    @Override
    public List<RapportVisit> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_RapportVisitas o " + clause + " ", RapportVisit.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    @Override
    public List<RapportVisit> findAllNativeLimit(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select TOP(5) * from T_RapportVisit as o " + clause + " ", RapportVisit.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
}
