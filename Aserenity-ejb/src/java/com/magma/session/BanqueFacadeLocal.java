/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Banque;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface BanqueFacadeLocal {

    void create(Banque banque);

    void edit(Banque banque);

    void remove(Banque banque);

    Banque find(Object id);

    List<Banque> findAll();

    List<Banque> findRange(int[] range);

    int count();
    
    void create(List<Banque> entitys);

    void edit(List<Banque> entitys);

    void remove(List<Banque> entitys);

    List<Banque> findAll(String clause);

    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);
    
}
