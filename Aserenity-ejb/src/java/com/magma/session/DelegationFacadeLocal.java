/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Delegation;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface DelegationFacadeLocal {

    void create(Delegation delegation);

    void edit(Delegation delegation);

    void remove(Delegation delegation);

    Delegation find(Object id);

    List<Delegation> findAll();

    List<Delegation> findRange(int[] range);
    
    List<Delegation> findAll(String clause);
    
    public List<Delegation> findAllNative(String clause);
        
    public boolean verifierUnique(String libelle);
            
    public boolean verifierUnique(String libelle, Object id);

    int count();
    
}
