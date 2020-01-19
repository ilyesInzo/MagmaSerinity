/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.EtatCommande;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface EtatCommandeFacadeLocal {

    void create(EtatCommande etatCommande);

    void edit(EtatCommande etatCommande);

    void remove(EtatCommande etatCommande);

    EtatCommande find(Object id);

    List<EtatCommande> findAll();

    List<EtatCommande> findRange(int[] range);

    int count();
    
    List<EtatCommande> findAllNative(String clause);
    
    List<EtatCommande> findAll(String clause);
    
    boolean verifierUnique(String clause);
    
}
