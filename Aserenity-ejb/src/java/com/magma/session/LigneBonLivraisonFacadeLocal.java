/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneBonLivraison;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface LigneBonLivraisonFacadeLocal {

    void create(LigneBonLivraison ligneBonLivraison);

    void edit(LigneBonLivraison ligneBonLivraison);

    void remove(LigneBonLivraison ligneBonLivraison);

    LigneBonLivraison find(Object id);

    List<LigneBonLivraison> findAll();

    List<LigneBonLivraison> findRange(int[] range);

    int count();
    
    void create(List<LigneBonLivraison> entitys);

    void edit(List<LigneBonLivraison> entitys);

    void remove(List<LigneBonLivraison> entitys);

    List<LigneBonLivraison> findAll(String clause);
    
    List<LigneBonLivraison> findAllNative(String clause);
    
}
