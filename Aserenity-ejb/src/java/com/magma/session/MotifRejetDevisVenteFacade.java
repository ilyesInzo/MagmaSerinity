/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.MotifRejetDevisVente;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author inzo
 */
@Stateless
public class MotifRejetDevisVenteFacade extends AbstractFacade<MotifRejetDevisVente> implements MotifRejetDevisVenteFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MotifRejetDevisVenteFacade() {
        super(MotifRejetDevisVente.class);
    }
    
}
