/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.PrefixAvoirVente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface PrefixAvoirVenteFacadeLocal {

    void create(PrefixAvoirVente prefixAvoirVente);

    void edit(PrefixAvoirVente prefixAvoirVente);

    void remove(PrefixAvoirVente prefixAvoirVente);

    PrefixAvoirVente find(Object id);

    List<PrefixAvoirVente> findAll();

    List<PrefixAvoirVente> findRange(int[] range);
    
    List<PrefixAvoirVente> findAll(String clause);

    int count();
    
    List<PrefixAvoirVente> findAllNative(String clause);

    public boolean verifierUnique(String libelle);

    public boolean verifierUnique(String libelle, Object id);
    
}
