/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.CategorieClient;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface CategorieClientFacadeLocal {

    void create(CategorieClient categorieClient);

    void edit(CategorieClient categorieClient);

    void remove(CategorieClient categorieClient);

    CategorieClient find(Object id);

    List<CategorieClient> findAll();
    
    List<CategorieClient> findAll(String clause);

    List<CategorieClient> findRange(int[] range);
    
    public boolean verifierUnique(String libelle);
            
    public boolean verifierUnique(String libelle, Object id);

    int count();
    
}
