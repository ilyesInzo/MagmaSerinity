/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Retour;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface RetourFacadeLocal {

    void create(Retour retour);

    void edit(Retour retour);

    void remove(Retour retour);

    Retour find(Object id);

    List<Retour> findAll();

    List<Retour> findRange(int[] range);

    int count();
    
    List<Retour> findAllNative(String clause);
    
    List<Retour> findAll(String clause);
    
    boolean verifierUniqueNumero(String numero);
    
    List<Retour> searchAllNative(String dateDebut, String dateFin, Integer etatRetour, Long idClient);
}
