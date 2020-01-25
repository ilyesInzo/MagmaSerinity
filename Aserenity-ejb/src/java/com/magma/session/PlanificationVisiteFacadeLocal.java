/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.PlanificationVisite;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface PlanificationVisiteFacadeLocal {

    void create(PlanificationVisite planificationVisite);

    void edit(PlanificationVisite planificationVisite);

    void remove(PlanificationVisite planificationVisite);

    PlanificationVisite find(Object id);

    List<PlanificationVisite> findAll();

    List<PlanificationVisite> findRange(int[] range);

    int count();
    
    void create(List<PlanificationVisite> entitys);

    void edit(List<PlanificationVisite> entitys);

    void remove(List<PlanificationVisite> entitys);
    
    void createOrUpdate(PlanificationVisite visite);
    
    void createOrUpdate(List<PlanificationVisite> visites);
    
    List<PlanificationVisite> findAll(String clause);

    List<PlanificationVisite> findAllNative(String clause);
    
    boolean chefDeZoneNonDisponible(Long idChefZon, Date dateDebut, Date dateFin);
    
}
