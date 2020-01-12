/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.PrefixBonLivraison;
import com.magma.entity.PrefixFacture;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface PrefixBonLivraisonFacadeLocal {

    void create(PrefixBonLivraison prefixBonLivraison);

    void edit(PrefixBonLivraison prefixBonLivraison);

    void remove(PrefixBonLivraison prefixBonLivraison);

    PrefixBonLivraison find(Object id);

    List<PrefixBonLivraison> findAll();

    List<PrefixBonLivraison> findRange(int[] range);

    List<PrefixBonLivraison> findAll(String clause);

    int count();

    List<PrefixBonLivraison> findAllNative(String clause);

    public boolean verifierUnique(String libelle);

    public boolean verifierUnique(String libelle, Object id);

}
