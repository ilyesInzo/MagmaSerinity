/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.ParametrageTaxe;
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
public class ParametrageTaxeFacade extends AbstractFacade<ParametrageTaxe> implements ParametrageTaxeFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParametrageTaxeFacade() {
        super(ParametrageTaxe.class);
    }
    
    @Override
    public List<ParametrageTaxe> findAllNative(String clause) {
           Query q = (Query) getEntityManager().createNativeQuery("Select * from T_ParametrageTaxe as o " + clause + " ", ParametrageTaxe.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
}
