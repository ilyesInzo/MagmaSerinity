/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.TaxesBonCommandeVente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface TaxesBonCommandeVenteFacadeLocal {

    void create(TaxesBonCommandeVente taxesBonCommandeVente);

    void edit(TaxesBonCommandeVente taxesBonCommandeVente);

    void remove(TaxesBonCommandeVente taxesBonCommandeVente);
    
    void create(List<TaxesBonCommandeVente> entitys);

    void edit(List<TaxesBonCommandeVente> entitys);

    void remove(List<TaxesBonCommandeVente> entitys);

    TaxesBonCommandeVente find(Object id);

    List<TaxesBonCommandeVente> findAll();

    List<TaxesBonCommandeVente> findRange(int[] range);
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);

    int count();
    
}
