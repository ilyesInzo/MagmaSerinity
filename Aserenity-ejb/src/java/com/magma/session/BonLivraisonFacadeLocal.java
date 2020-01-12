/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.BonLivraison;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface BonLivraisonFacadeLocal {

    void create(BonLivraison bonLivraison);

    void edit(BonLivraison bonLivraison);

    void remove(BonLivraison bonLivraison);

    BonLivraison find(Object id);

    List<BonLivraison> findAll();

    List<BonLivraison> findRange(int[] range);

    int count();
    
    List<BonLivraison> findAllNative(String clause);
    
    List<BonLivraison> findAll(String clause);
    
    boolean verifierUniqueNumero(String numero);
    
    void updateAllNative(String clause);
    
    List<BonLivraison> searchAllNative(String dateDebut, String dateFin, Integer etatFacture, Integer origineFacture, Long idClient, boolean retour);

    
}
