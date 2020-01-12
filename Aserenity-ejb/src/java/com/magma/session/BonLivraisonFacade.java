/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.BonLivraison;
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
public class BonLivraisonFacade extends AbstractFacade<BonLivraison> implements BonLivraisonFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BonLivraisonFacade() {
        super(BonLivraison.class);
    }

    @Override
    public List<BonLivraison> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_BonLivraison as o " + clause + " ", BonLivraison.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    @Override
    public boolean verifierUniqueNumero(String numero) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from BonLivraison as o where o.numero like '" + numero + "'").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<BonLivraison> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void updateAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Update T_BonLivraison " + clause + " ");
        q.executeUpdate();
    }

    @Override
    public List<BonLivraison> searchAllNative(String dateDebut, String dateFin, Integer etatFacture, Integer origineFacture, Long idClient, boolean retour) {

        String clause = "Select * from T_BonLivraison as o where o.BLiv_DateBonLivraison Between ?1 and ?2 ";

        if (etatFacture != null) {
            clause = clause + "and BLiv_Etat = ?3 ";
        }

        if (origineFacture != null) {
            clause = clause + "and BLiv_OrigineBonLivraison = ?4 ";
        }

        if (idClient != null) {
            clause = clause + "and Cli_Id = ?5 ";
        }

        if (retour) {
            clause = clause + "and Ret_Id not null ";
        }
        clause = clause + " order by BLiv_DateBonLivraison desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, BonLivraison.class).setParameter("1", dateDebut)
                .setParameter("2", dateFin)
                .setParameter("3", etatFacture)
                .setParameter("4", origineFacture)
                .setParameter("5", idClient);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }

}
