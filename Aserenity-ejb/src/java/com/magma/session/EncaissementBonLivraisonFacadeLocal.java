/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Encaissement;
import com.magma.entity.EncaissementBonLivraison;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface EncaissementBonLivraisonFacadeLocal {

    void create(EncaissementBonLivraison encaissementBonLivraison);

    void edit(EncaissementBonLivraison encaissementBonLivraison);

    void remove(EncaissementBonLivraison encaissementBonLivraison);

    EncaissementBonLivraison find(Object id);

    List<EncaissementBonLivraison> findAll();

    List<EncaissementBonLivraison> findRange(int[] range);
    
    List<EncaissementBonLivraison> findAll(String clause);

    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id );

    List<EncaissementBonLivraison> findAllNative(String clause);

    int count();
    
    List<EncaissementBonLivraison> searchAllNative(String dateDebut, String dateFin, Long idTypeEncaissement, Long idClient);
    
}
