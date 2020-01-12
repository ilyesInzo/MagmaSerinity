/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.TypeEncaissementVente;
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
public class TypeEncaissementVenteFacade extends AbstractFacade<TypeEncaissementVente> implements TypeEncaissementVenteFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypeEncaissementVenteFacade() {
        super(TypeEncaissementVente.class);
    }
    
    @Override
    public List<TypeEncaissementVente> findAllNative(String clause) {
//        System.out.println("Select * from T_Facture as o " + clause + " ");
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_TypeEncaissementVente as o " + clause + " ", TypeEncaissementVente.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
}
