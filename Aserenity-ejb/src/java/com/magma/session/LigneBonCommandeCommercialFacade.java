/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneBonCommandeCommercial;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author inzo
 */
@Stateless
public class LigneBonCommandeCommercialFacade extends AbstractFacade<LigneBonCommandeCommercial> implements LigneBonCommandeCommercialFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LigneBonCommandeCommercialFacade() {
        super(LigneBonCommandeCommercial.class);
    }
    
}
