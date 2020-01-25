/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.BonCommandeCommercial;
import com.magma.entity.BonCommandeCommercial;
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
public class BonCommandeCommercialFacade extends AbstractFacade<BonCommandeCommercial> implements BonCommandeCommercialFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BonCommandeCommercialFacade() {
        super(BonCommandeCommercial.class);
    }
    
        @Override
    public List<BonCommandeCommercial> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_BonCommandeCommercial as o " + clause + " ", BonCommandeCommercial.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    @Override
    public boolean verifierUniqueNumero(String numero) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from BonCommandeCommercial as o where o.numero like '" + numero + "'").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<BonCommandeCommercial> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<BonCommandeCommercial> searchAllNative(String dateDebut, String dateFin, Integer etatBonCommandeCommercial, Long idClient) {


        String clause = "Select * from T_BonCommandeCommercial as o where o.Dev_DateBonCommandeCommercial Between ?1 and ?2 ";

        if (etatBonCommandeCommercial != null) {
clause=clause+ "and Dev_Etat = ?3 ";
        }

        if (idClient != null) {
clause=clause+ "and Cli_Id = ?4 ";
        }
        
clause = clause + " order by Dev_DateBonCommandeCommercial desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, BonCommandeCommercial.class).setParameter("1", dateDebut)
        .setParameter("2", dateFin)
        .setParameter("3", etatBonCommandeCommercial)
        .setParameter("4", idClient);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }
    
}
