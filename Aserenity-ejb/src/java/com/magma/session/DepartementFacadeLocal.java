/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Departement;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface DepartementFacadeLocal {

    void create(Departement departement);

    void edit(Departement departement);

    void remove(Departement departement);

    Departement find(Object id);

    List<Departement> findAll();

    List<Departement> findRange(int[] range);

    int count();
    
    List<Departement> findAllNative(String clause);
    
    List<Departement> findAll(String clause);
    
    boolean verifierUnique(String libelle);
    
    boolean verifierUnique(String libelle, Object id);
    
}
