/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Categorie;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface CategorieFacadeLocal {

    void create(Categorie categorie);

    void edit(Categorie categorie);

    void remove(Categorie categorie);

    Categorie find(Object id);

    List<Categorie> findAll();
    
    List<Categorie> findAll(String clause);

    List<Categorie> findRange(int[] range);
    
    boolean verifierUnique(String libelle, Long idParent);

    boolean verifierUnique(String libelle, Long idParent, Long id);

    boolean verifierUnique(String libelle, int rang);

    boolean verifierUnique(String libelle, int rang, Long id);

    int count();
    
    boolean verifierUnique(String clause);
    
}
