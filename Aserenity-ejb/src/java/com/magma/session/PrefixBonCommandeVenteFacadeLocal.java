/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.PrefixBonCommandeVente;
import com.magma.entity.PrefixDevis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface PrefixBonCommandeVenteFacadeLocal {

    void create(PrefixBonCommandeVente prefixBonCommandeVente);

    void edit(PrefixBonCommandeVente prefixBonCommandeVente);

    void remove(PrefixBonCommandeVente prefixBonCommandeVente);

    PrefixBonCommandeVente find(Object id);

    List<PrefixBonCommandeVente> findAll();
    
    List<PrefixBonCommandeVente> findAll(String clause);

    List<PrefixBonCommandeVente> findRange(int[] range);
    
    List<PrefixBonCommandeVente> findAllNative(String clause);

    public boolean verifierUnique(String libelle);

    public boolean verifierUnique(String libelle, Object id);

    int count();
    
}
