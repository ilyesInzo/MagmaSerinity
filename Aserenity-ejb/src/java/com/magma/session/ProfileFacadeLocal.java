/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Profile;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface ProfileFacadeLocal {

    void create(Profile profile);

    void edit(Profile profile);

    void remove(Profile profile);

    Profile find(Object id);

    List<Profile> findAll();
    
    List<Profile> findAll(String clause);

    List<Profile> findRange(int[] range);
    
    public boolean verifierUnique(String libelle);
            
    public boolean verifierUnique(String libelle, Object id);

    int count();
    
}
