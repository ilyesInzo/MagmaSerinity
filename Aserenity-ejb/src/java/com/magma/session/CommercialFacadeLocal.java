/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Commercial;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface CommercialFacadeLocal {

    void create(Commercial commercial);

    void edit(Commercial commercial);

    void remove(Commercial commercial);

    Commercial find(Object id);

    List<Commercial> findAll();
    
    List<Commercial> findAll(String clause);

    List<Commercial> findRange(int[] range);

    int count();
    
    List<Commercial> findAllNative(String clause);
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Long id);
    
}
