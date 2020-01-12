/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.PrefixFacture;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface PrefixFactureFacadeLocal {

    void create(PrefixFacture prefixFacture);

    void edit(PrefixFacture prefixFacture);

    void remove(PrefixFacture prefixFacture);

    PrefixFacture find(Object id);

    List<PrefixFacture> findAll();
    
    List<PrefixFacture> findAll(String clause);

    List<PrefixFacture> findRange(int[] range);

    int count();
    
List<PrefixFacture> findAllNative(String clause);

    public boolean verifierUnique(String libelle);
            
    public boolean verifierUnique(String libelle, Object id);
}
