/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.TaxesFacture;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface TaxesFactureFacadeLocal {

    void create(TaxesFacture taxesFacture);

    void edit(TaxesFacture taxesFacture);

    void remove(TaxesFacture taxesFacture);

    TaxesFacture find(Object id);

    List<TaxesFacture> findAll();

    List<TaxesFacture> findRange(int[] range);

    int count();
    
    void create(List<TaxesFacture> entitys);

    void edit(List<TaxesFacture> entitys);

    void remove(List<TaxesFacture> entitys);

    List<TaxesFacture> findAll(String clause);

    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);
    
}
