/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.ClassificationClient;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface ClassificationClientFacadeLocal {

    void create(ClassificationClient classificationClient);

    void edit(ClassificationClient classificationClient);

    void remove(ClassificationClient classificationClient);

    ClassificationClient find(Object id);

    List<ClassificationClient> findAll();

    List<ClassificationClient> findRange(int[] range);

    int count();
    
    List<ClassificationClient> findAll(String clause);
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Object id);
    
}
