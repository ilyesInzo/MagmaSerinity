/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

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
public class GouvernoratFacade extends AbstractFacade<Gouvernorat> implements GouvernoratFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GouvernoratFacade() {
        super(Gouvernorat.class);
    }
    
        @Override
    public List<Gouvernorat> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Gouvernorat as o " + clause + " ", Gouvernorat.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
}
