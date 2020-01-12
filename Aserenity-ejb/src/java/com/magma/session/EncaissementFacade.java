/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Encaissement;
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
public class EncaissementFacade extends AbstractFacade<Encaissement> implements EncaissementFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EncaissementFacade() {
        super(Encaissement.class);
    }

    @Override
    public List<Encaissement> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Encaissement as o " + clause + " ", Encaissement.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    @Override
    public List<Encaissement> searchAllNative(String dateDebut, String dateFin, Long idEncaissement, Long idClient) {

        String clause = "Select * from T_Encaissement as o where o.Enc_DateEncaissement Between ?1 and ?2 ";

        if (idEncaissement != null) {
            clause = clause + "and TEVnt_Id = ?3 ";
        }

        if (idClient != null) {
            clause = clause + "and Cli_Id = ?4 ";
        }

        clause = clause + " order by Enc_DateEncaissement desc";
        Query q = (Query) getEntityManager().createNativeQuery(clause, Encaissement.class).setParameter("1", dateDebut)
                .setParameter("2", dateFin)
                .setParameter("3", idEncaissement)
                .setParameter("4", idClient);
        q.setHint(QueryHints.REFRESH, HintValues.TRUE);

        return q.getResultList();
    }

}
