/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Facture;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface FactureFacadeLocal {

    void create(Facture facture);

    void edit(Facture facture);

    void remove(Facture facture);

    Facture find(Object id);

    List<Facture> findAll();

    List<Facture> findRange(int[] range);

    int count();
    
    List<Facture> findAllNative(String clause);
    
    List<Facture> findAll(String clause);
    
    boolean verifierUniqueNumero(String numero);
    
    void updateAllNative(String clause);
    
    List<Facture> searchAllNative(String dateDebut, String dateFin, Integer etatFacture, Integer origineFacture, Long idClient, boolean avoir, boolean retour);
    
}
