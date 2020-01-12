/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Encaissement;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface EncaissementFacadeLocal {

    void create(Encaissement encaissement);

    void edit(Encaissement encaissement);

    void remove(Encaissement encaissement);

    Encaissement find(Object id);

    List<Encaissement> findAll();

    List<Encaissement> findRange(int[] range);

    int count();
    
    void create(List<Encaissement> entitys);
    
    void edit(List<Encaissement> entitys);
    
    void remove(List<Encaissement> entitys);
    
    List<Encaissement> findAll(String clause);
    
    List<Encaissement> findAllNative(String clause);
    
    List<Encaissement> searchAllNative(String dateDebut, String dateFin, Long idTypeEncaissement, Long idClient);
    
}
