/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.MotifRejetDevisVente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface MotifRejetDevisVenteFacadeLocal {

    void create(MotifRejetDevisVente motifRejetDevisVente);

    void edit(MotifRejetDevisVente motifRejetDevisVente);

    void remove(MotifRejetDevisVente motifRejetDevisVente);

    MotifRejetDevisVente find(Object id);

    List<MotifRejetDevisVente> findAll();

    List<MotifRejetDevisVente> findRange(int[] range);

    int count();
    
    List<MotifRejetDevisVente> findAll(String clause);
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);
    
}
