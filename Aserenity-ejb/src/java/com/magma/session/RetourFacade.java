/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Retour;
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
public class RetourFacade extends AbstractFacade<Retour> implements RetourFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RetourFacade() {
        super(Retour.class);
    }
    
                @Override
    public List<Retour> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Retour as o " + clause + " ", Retour.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    @Override
    public boolean verifierUniqueNumero(String numero) {
        Query q = (Query) getEntityManager().createQuery("Select object(o) from Retour as o where o.numero like '" + numero + "'").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Retour> Ts = q.getResultList();
            return Ts.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }    
    
    
    @Override
    public List<Retour> searchAllNative(String dateDebut, String dateFin, Integer etatRetour, Long idClient) {


        String clause = "Select * from T_Retour as o where o.Ret_DateCreation Between ?1 and ?2 ";

        if (etatRetour != null) {
clause=clause+ "and Ret_Etat = ?3 ";
        }

        if (idClient != null) {
clause=clause+ "and Cli_Id = ?4 ";
        }
        

clause = clause + " order by Ret_DateCreation desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, Retour.class).setParameter("1", dateDebut)
        .setParameter("2", dateFin)
        .setParameter("3", etatRetour)
        .setParameter("4", idClient);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }
    
    
}
