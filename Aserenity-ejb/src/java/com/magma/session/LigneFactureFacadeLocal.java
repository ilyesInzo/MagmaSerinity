/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneFacture;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface LigneFactureFacadeLocal {

    void create(LigneFacture ligneFacture);

    void edit(LigneFacture ligneFacture);

    void remove(LigneFacture ligneFacture);

    LigneFacture find(Object id);

    List<LigneFacture> findAll();
    
    List<LigneFacture> findAll(String clause);

    List<LigneFacture> findRange(int[] range);
    
    void create(List<LigneFacture> ligneFacture);

    void edit(List<LigneFacture> ligneFacture);

    void remove(List<LigneFacture> ligneFacture);
    
    List<LigneFacture> findAllNative(String clause);

    int count();
    
}
