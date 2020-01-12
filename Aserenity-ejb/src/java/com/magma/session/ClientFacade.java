/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Client;
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
public class ClientFacade extends AbstractFacade<Client> implements ClientFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientFacade() {
        super(Client.class);
    }
    
    @Override
    public List<Client> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_Client as o " + clause + " ", Client.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }

    @Override
    public boolean verifierUnique(String libelle, String gsm) {

        Query q = (Query) getEntityManager().createQuery("Select object(o) from Client as o where o.supprimer = 0 and o.libelle like '" + libelle + "' and o.gsm like '" + gsm + "'").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Client> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean verifierUnique(String libelle, String gsm, Long id) {

        Query q = (Query) getEntityManager().createQuery("Select object(o) from Client as o where o.supprimer = 0 and o.id <> " + id + " and o.libelle like '" + libelle + "' and o.gsm like '" + gsm + "' ").setHint(QueryHints.REFRESH, HintValues.TRUE);
        try {
            List<Client> Ts = q.getResultList();
            if (Ts.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }

    }

}
