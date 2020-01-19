/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneBonCommandeCommercial;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface LigneBonCommandeCommercialFacadeLocal {

    void create(LigneBonCommandeCommercial ligneBonCommandeCommercial);

    void edit(LigneBonCommandeCommercial ligneBonCommandeCommercial);

    void remove(LigneBonCommandeCommercial ligneBonCommandeCommercial);

    LigneBonCommandeCommercial find(Object id);

    List<LigneBonCommandeCommercial> findAll();

    List<LigneBonCommandeCommercial> findRange(int[] range);

    int count();
    
    void create(List<LigneBonCommandeCommercial> entitys);

    void edit(List<LigneBonCommandeCommercial> entitys);

    void remove(List<LigneBonCommandeCommercial> entitys);

    List<LigneBonCommandeCommercial> findAll(String clause);
    
}
