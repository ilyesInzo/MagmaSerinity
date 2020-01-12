/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Tva;
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
public class TvaFacade extends AbstractFacade<Tva> implements TvaFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TvaFacade() {
        super(Tva.class);
    }
    
    @Override
    public boolean verifierUnique(Float valeur) {
//        Query q = (Query) getEntityManager().createQuery("Select object(o) from Tva as o where o.valeur = '" + valeur + "' ").setHint(QueryHints.REFRESH, HintValues.TRUE);
         Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Tva as o where o.Tva_Valeur ='"+valeur+"'", Tva.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Tva> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean verifierUnique(Float valeur, Long id) {
//        Query q = (Query) getEntityManager().createQuery("Select object(o) from Tva as o where o.id <> " + id + " and o.valeur = '" + valeur + "' ").setHint(QueryHints.REFRESH, HintValues.TRUE);
       
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Tva as o where o.Tva_Id <> " + id + " and  o.Tva_Valeur ='"+valeur+"'", Tva.class).setHint(QueryHints.REFRESH, HintValues.TRUE);try {
            List<Tva> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
}
