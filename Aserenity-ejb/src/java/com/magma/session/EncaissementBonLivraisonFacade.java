/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.EncaissementBonLivraison;
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
public class EncaissementBonLivraisonFacade extends AbstractFacade<EncaissementBonLivraison> implements EncaissementBonLivraisonFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EncaissementBonLivraisonFacade() {
        super(EncaissementBonLivraison.class);
    }
    
    @Override
    public List<EncaissementBonLivraison> findAllNative(String clause) {
//        System.out.println("Select * from T_Facture as o " + clause + " ");
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_EncaissementBonLivraison as o " + clause + " ", EncaissementBonLivraison.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    @Override
    public List<EncaissementBonLivraison> searchAllNative(String dateDebut, String dateFin, Long idEncaissement, Long idClient) {


        String clause = "Select * from T_EncaissementBonLivraison as o where o.EBLiv_DateEncaissement Between ?1 and ?2 ";

        if (idEncaissement != null) {
clause=clause+ "and TEVnt_Id = ?3 ";
        }

        if (idClient != null) {
clause=clause+ "and Cli_Id = ?4 ";
        }
        
clause = clause + " order by EBLiv_DateEncaissement desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, EncaissementBonLivraison.class).setParameter("1", dateDebut)
        .setParameter("2", dateFin)
        .setParameter("3", idEncaissement)
        .setParameter("4", idClient);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }
    
}
