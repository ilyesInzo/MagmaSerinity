/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.ParametrageTaxe;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface ParametrageTaxeFacadeLocal {

    void create(ParametrageTaxe parametrageTaxe);

    void edit(ParametrageTaxe parametrageTaxe);

    void remove(ParametrageTaxe parametrageTaxe);

    ParametrageTaxe find(Object id);

    List<ParametrageTaxe> findAll();

    List<ParametrageTaxe> findRange(int[] range);

    int count();
    
    void create(List<ParametrageTaxe> entitys);

    void edit(List<ParametrageTaxe> entitys);

    void remove(List<ParametrageTaxe> entitys);

    List<ParametrageTaxe> findAll(String clause);

    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);

    List<ParametrageTaxe> findAllNative(String clause);
    
}
