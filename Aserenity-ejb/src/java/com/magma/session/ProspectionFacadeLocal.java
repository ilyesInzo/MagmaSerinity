/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Prospection;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface ProspectionFacadeLocal {

    void create(Prospection prospection);

    void edit(Prospection prospection);

    void remove(Prospection prospection);

    Prospection find(Object id);

    List<Prospection> findAll();

    List<Prospection> findRange(int[] range);

    int count();
    
    List<Prospection> findAllNative(String clause);
    
    List<Prospection> findAll(String clause);
    
}
