/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Facture;
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
public class FactureFacade extends AbstractFacade<Facture> implements FactureFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FactureFacade() {
        super(Facture.class);
    }

    @Override
    public List<Facture> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Facture as o " + clause + " ", Facture.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    @Override
    public boolean verifierUniqueNumero(String numero) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Facture as o where o.numero like '" + numero + "'").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Facture> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Update T_Facture " + clause + " ");
        q.executeUpdate();
    }

    @Override
    public List<Facture> searchAllNative(String dateDebut, String dateFin, Integer etatFacture, Integer origineFacture, Long idClient, boolean avoir, boolean retour) {


        String clause = "Select * from T_Facture as o where o.Fct_DateFacture Between ?1 and ?2 ";

        if (etatFacture != null) {
clause=clause+ "and Fct_Etat = ?3 ";
        }

        if (origineFacture != null) {
clause=clause+ "and Fct_OrigineFacture = ?4 ";
        }

        if (idClient != null) {
clause=clause+ "and Cli_Id = ?5 ";
        }
        
         if (avoir) {
clause=clause+ "and Fct_Avoir = ?6 ";
        }
                
        if (retour) {
clause=clause+ "and Ret_Id not null ";
        }
clause = clause + " order by Fct_DateFacture desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, Facture.class).setParameter("1", dateDebut)
        .setParameter("2", dateFin)
        .setParameter("3", etatFacture)
        .setParameter("4", origineFacture)
        .setParameter("5", idClient)
        .setParameter("6", avoir);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }

}
