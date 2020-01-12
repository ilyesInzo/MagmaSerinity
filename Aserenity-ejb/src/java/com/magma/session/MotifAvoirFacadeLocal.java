/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.MotifAvoir;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface MotifAvoirFacadeLocal {

    void create(MotifAvoir motifAvoir);

    void edit(MotifAvoir motifAvoir);

    void remove(MotifAvoir motifAvoir);

    MotifAvoir find(Object id);

    List<MotifAvoir> findAll();
    
    List<MotifAvoir> findAll(String clause);

    List<MotifAvoir> findRange(int[] range);

    int count();
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);
    
}
