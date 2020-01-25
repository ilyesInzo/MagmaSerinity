/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.bibliotheque.TraitementDate;
import com.magma.entity.PlanificationVisite;
import java.util.Date;
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
public class PlanificationVisiteFacade extends AbstractFacade<PlanificationVisite> implements PlanificationVisiteFacadeLocal {
    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanificationVisiteFacade() {
        super(PlanificationVisite.class);
    }
    
    @Override
    public void createOrUpdate(PlanificationVisite entity) {
        if (entity.getId()!=null && find(entity.getId()) == null) {
            getEntityManager().persist(entity);
        } else {
            getEntityManager().merge(entity);
        }
    }

    @Override
    public void createOrUpdate(List<PlanificationVisite> entitys) {
        for (PlanificationVisite entity : entitys) {
            if (entity.getId()!=null && find(entity.getId()) == null) {
                getEntityManager().persist(entity);
            } else {
                getEntityManager().merge(entity);
            }
        }
    }
    
    @Override
    public List<PlanificationVisite> findAllNative(String clause) {
        Query q = (Query) getEntityManager().createNativeQuery("Select * from T_PlanificationVisite as o " + clause + " ", PlanificationVisite.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        return q.getResultList();
    }
    
    @Override
    public boolean chefDeZoneNonDisponible(Long idChefZon, Date dateDebut, Date dateFin) {
     Query q = (Query) getEntityManager().createNativeQuery("Select * from T_PlanificationVisite as o where  o.Com_Id = " + idChefZon + " and  ( o.PVi_DateDebut between '" +  TraitementDate.returnDateHeure(TraitementDate.moinsPlusMinute(dateDebut, -5)) +"' and '"+ TraitementDate.returnDateHeure(TraitementDate.moinsPlusMinute(dateFin,5)) + "' )  or ( o.PVi_DateFin between '" + TraitementDate.returnDateHeure(TraitementDate.moinsPlusMinute(dateDebut, -5)) +"' and '"+ TraitementDate.returnDateHeure(TraitementDate.moinsPlusMinute(dateFin, 5)) + "')  or ( PVi_DateDebut= '" + TraitementDate.returnDateHeure(TraitementDate.moinsPlusMinute(dateDebut, -5)) +"' and PVi_DateFin=  '" + TraitementDate.returnDateHeure(TraitementDate.moinsPlusMinute(dateFin, 5)) +"')  ", PlanificationVisite.class).setHint(QueryHints.REFRESH, HintValues.TRUE);
        
        try {
            List<PlanificationVisite> Ts = q.getResultList();
            if (Ts.size() > 0) {
                System.out.println("sizeeeeee"+Ts.size());
                return true;
            } else{
                return false; 
            }
           
        } catch (Exception e) {
            return false;
        }
    }
    
}
