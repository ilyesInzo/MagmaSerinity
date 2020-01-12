/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Tva;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface TvaFacadeLocal {

    void create(Tva tva);

    void edit(Tva tva);

    void remove(Tva tva);

    Tva find(Object id);

    List<Tva> findAll();
    
    List<Tva> findAll(String clause);

    List<Tva> findRange(int[] range);
    
    public boolean verifierUnique(Float valeur );
            
    public boolean verifierUnique(Float valeur, Long id);

    int count();
    
}
