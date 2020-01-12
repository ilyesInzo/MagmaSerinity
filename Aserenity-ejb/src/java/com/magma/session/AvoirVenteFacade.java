/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.AvoirVente;
import java.math.BigDecimal;
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
public class AvoirVenteFacade extends AbstractFacade<AvoirVente> implements AvoirVenteFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AvoirVenteFacade() {
        super(AvoirVente.class);
    }

    @Override
    public boolean verifierUniqueNumero(String libelle) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_AvoirVente as o where o.AVnt_Numero like '" + libelle + "'", AvoirVente.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<AvoirVente> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<AvoirVente> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_AvoirVente as o " + clause + " ", AvoirVente.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    @Override
    public List<AvoirVente> searchAllNative(String dateDebut, String dateFin, Long idClient) {

        String clause = "Select * from T_AvoirVente as o where o.AVnt_DateCreation Between ?1 and ?2 ";

        if (idClient != null) {
            clause = clause + "and Cli_Id = ?3 ";
        }

        clause = clause + " order by AVnt_DateCreation desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, AvoirVente.class).setParameter("1", dateDebut)
                .setParameter("2", dateFin)
                .setParameter("3", idClient);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }

    @Override
    public void editRestAvoir(Long idAvoir, BigDecimal montant) {
        Query q = (Query) getEntityManager().createNativeQuery("Update T_AvoirVente SET AVnt_Reste = AVnt_Reste + " + montant + " where AVnt_Id = " + idAvoir);
        q.executeUpdate();
    }

}
