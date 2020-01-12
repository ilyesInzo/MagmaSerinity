/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Gouvernorat;
import com.magma.entity.Profile;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface GouvernoratFacadeLocal {

    void create(Gouvernorat gouvernorat);

    void edit(Gouvernorat gouvernorat);

    void remove(Gouvernorat gouvernorat);

    Gouvernorat find(Object id);

    List<Gouvernorat> findAll();

    List<Gouvernorat> findRange(int[] range);
    
    List<Gouvernorat> findAll(String clause);
    
    public List<Gouvernorat> findAllNative(String clause);
        
    public boolean verifierUnique(String libelle);
            
    public boolean verifierUnique(String libelle, Object id);

    int count();
    
}
