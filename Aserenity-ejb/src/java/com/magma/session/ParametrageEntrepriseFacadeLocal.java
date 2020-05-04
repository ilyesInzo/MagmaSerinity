/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.ParametrageEntreprise;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface ParametrageEntrepriseFacadeLocal {

    void create(ParametrageEntreprise parametrageEntreprise);

    void edit(ParametrageEntreprise parametrageEntreprise);

    void remove(ParametrageEntreprise parametrageEntreprise);

    ParametrageEntreprise find(Object id);

    List<ParametrageEntreprise> findAll();

    List<ParametrageEntreprise> findRange(int[] range);

    int count();
    
}
