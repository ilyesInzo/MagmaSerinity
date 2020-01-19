/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.BonCommandeVente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface BonCommandeVenteFacadeLocal {

    void create(BonCommandeVente bonCommandeVente);

    void edit(BonCommandeVente bonCommandeVente);

    void remove(BonCommandeVente bonCommandeVente);

    BonCommandeVente find(Object id);

    List<BonCommandeVente> findAll();

    List<BonCommandeVente> findRange(int[] range);

    int count();
    
    boolean verifierUniqueNumero(String numero);
    
    List<BonCommandeVente> findAllNative(String clause);
    
    List<BonCommandeVente> findAll(String clause);
    
}
