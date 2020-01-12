/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Entreprise;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface EntrepriseFacadeLocal {

    void create(Entreprise entreprise);

    void edit(Entreprise entreprise);

    void remove(Entreprise entreprise);

    Entreprise find(Object id);

    List<Entreprise> findAll();
    
    List<Entreprise> findAll(String clause);

    List<Entreprise> findRange(int[] range);

    List<Entreprise> findAllNative(String clause);
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);
     
    int count();
    
}
