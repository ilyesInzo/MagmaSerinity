/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Privileges;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface PrivilegesFacadeLocal {

    void create(Privileges privileges);

    void edit(Privileges privileges);

    void remove(Privileges privileges);
    
    void create(List<Privileges> privileges);

    void edit(List<Privileges> privileges);

    void remove(List<Privileges> privileges);

    Privileges find(Object id);

    List<Privileges> findAll();

    List<Privileges> findAll(String clause); 
    
    List<Privileges> findRange(int[] range);

    int count();
    
}
