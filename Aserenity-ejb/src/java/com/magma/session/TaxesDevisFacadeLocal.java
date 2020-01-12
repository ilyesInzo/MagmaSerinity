/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.TaxesDevis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface TaxesDevisFacadeLocal {

    void create(TaxesDevis taxesDevis);

    void edit(TaxesDevis taxesDevis);

    void remove(TaxesDevis taxesDevis);

    TaxesDevis find(Object id);

    List<TaxesDevis> findAll();

    List<TaxesDevis> findRange(int[] range);

    int count();
    
    void create(List<TaxesDevis> entitys);

    void edit(List<TaxesDevis> entitys);

    void remove(List<TaxesDevis> entitys);

    List<TaxesDevis> findAll(String clause);

    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);
    
}
