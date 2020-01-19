/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneBonCommandeVente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface LigneBonCommandeVenteFacadeLocal {

    void create(LigneBonCommandeVente ligneBonCommandeVente);

    void edit(LigneBonCommandeVente ligneBonCommandeVente);

    void remove(LigneBonCommandeVente ligneBonCommandeVente);

    LigneBonCommandeVente find(Object id);

    List<LigneBonCommandeVente> findAll();

    List<LigneBonCommandeVente> findRange(int[] range);

    int count();
    
    void create(List<LigneBonCommandeVente> entitys);

    void edit(List<LigneBonCommandeVente> entitys);

    void remove(List<LigneBonCommandeVente> entitys);

    List<LigneBonCommandeVente> findAll(String clause);
    
}
