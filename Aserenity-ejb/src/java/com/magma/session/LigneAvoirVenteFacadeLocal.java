/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneAvoirVente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface LigneAvoirVenteFacadeLocal {

    void create(LigneAvoirVente ligneAvoirVente);

    void edit(LigneAvoirVente ligneAvoirVente);

    void remove(LigneAvoirVente ligneAvoirVente);

    LigneAvoirVente find(Object id);

    List<LigneAvoirVente> findAll();

    List<LigneAvoirVente> findRange(int[] range);

    int count();
    
    void create( List<LigneAvoirVente> detailAvoirVente);

    void edit( List<LigneAvoirVente> detailAvoirVente);

    void remove( List<LigneAvoirVente> detailAvoirVente);
    
    List<LigneAvoirVente> findAll(String clause);
    
}
