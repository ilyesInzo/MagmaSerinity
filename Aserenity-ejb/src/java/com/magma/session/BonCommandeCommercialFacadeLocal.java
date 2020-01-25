/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.BonCommandeCommercial;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface BonCommandeCommercialFacadeLocal {

    void create(BonCommandeCommercial bonCommandeCommercial);

    void edit(BonCommandeCommercial bonCommandeCommercial);

    void remove(BonCommandeCommercial bonCommandeCommercial);

    BonCommandeCommercial find(Object id);

    List<BonCommandeCommercial> findAll();

    List<BonCommandeCommercial> findRange(int[] range);

    int count();
    
    List<BonCommandeCommercial> findAllNative(String clause);
    
    List<BonCommandeCommercial> findAll(String clause);
    
    boolean verifierUniqueNumero(String numero);
    
    
    List<BonCommandeCommercial> searchAllNative(String dateDebut, String dateFin, Integer etatFacture, Long idClient);

}
