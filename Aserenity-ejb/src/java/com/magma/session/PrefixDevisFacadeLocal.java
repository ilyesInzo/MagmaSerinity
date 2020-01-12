/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.PrefixDevis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface PrefixDevisFacadeLocal {

    void create(PrefixDevis prefixDevis);

    void edit(PrefixDevis prefixDevis);

    void remove(PrefixDevis prefixDevis);

    PrefixDevis find(Object id);

    List<PrefixDevis> findAll();

    List<PrefixDevis> findAll(String clause);

    List<PrefixDevis> findRange(int[] range);

    List<PrefixDevis> findAllNative(String clause);

    public boolean verifierUnique(String libelle);

    public boolean verifierUnique(String libelle, Object id);

    int count();

}
