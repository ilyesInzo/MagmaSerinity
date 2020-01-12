/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Devis;
import com.magma.entity.Devis;
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
public class DevisFacade extends AbstractFacade<Devis> implements DevisFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DevisFacade() {
        super(Devis.class);
    }
    
        @Override
    public List<Devis> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Devis as o " + clause + " ", Devis.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    @Override
    public boolean verifierUniqueNumero(String numero) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Devis as o where o.numero like '" + numero + "'").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Devis> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Devis> searchAllNative(String dateDebut, String dateFin, Integer etatDevis, Long idClient) {


        String clause = "Select * from T_Devis as o where o.Dev_DateDevis Between ?1 and ?2 ";

        if (etatDevis != null) {
clause=clause+ "and Dev_Etat = ?3 ";
        }

        if (idClient != null) {
clause=clause+ "and Cli_Id = ?4 ";
        }
        
clause = clause + " order by Dev_DateDevis desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, Devis.class).setParameter("1", dateDebut)
        .setParameter("2", dateFin)
        .setParameter("3", etatDevis)
        .setParameter("4", idClient);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }
    
}
