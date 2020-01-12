/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.TypeEncaissementVente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface TypeEncaissementVenteFacadeLocal {

    void create(TypeEncaissementVente typeEncaissementVente);

    void edit(TypeEncaissementVente typeEncaissementVente);

    void remove(TypeEncaissementVente typeEncaissementVente);

    TypeEncaissementVente find(Object id);

    List<TypeEncaissementVente> findAll();

    List<TypeEncaissementVente> findRange(int[] range);

    int count();
    
    List<TypeEncaissementVente> findAll(String clause);
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);

    List<TypeEncaissementVente> findAllNative(String clause);
    
}
