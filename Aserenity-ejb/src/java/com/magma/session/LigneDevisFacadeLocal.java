/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneDevis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface LigneDevisFacadeLocal {

    void create(LigneDevis ligneDevis);

    void edit(LigneDevis ligneDevis);

    void remove(LigneDevis ligneDevis);

    LigneDevis find(Object id);

    List<LigneDevis> findAll();

    List<LigneDevis> findRange(int[] range);

    int count();

    void create(List<LigneDevis> entitys);

    void edit(List<LigneDevis> entitys);

    void remove(List<LigneDevis> entitys);

    List<LigneDevis> findAll(String clause);

}
