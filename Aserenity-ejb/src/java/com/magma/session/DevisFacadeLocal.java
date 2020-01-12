/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Devis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface DevisFacadeLocal {

    void create(Devis devis);

    void edit(Devis devis);

    void remove(Devis devis);

    Devis find(Object id);

    List<Devis> findAll();

    List<Devis> findRange(int[] range);

    int count();
    
    List<Devis> findAllNative(String clause);
    
    List<Devis> findAll(String clause);
    
    boolean verifierUniqueNumero(String numero);
    
    
    List<Devis> searchAllNative(String dateDebut, String dateFin, Integer etatFacture, Long idClient);

}
