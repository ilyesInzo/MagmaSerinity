/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.RapportVisit;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface RapportVisitFacadeLocal {

    void create(RapportVisit rapportVisit);

    void edit(RapportVisit rapportVisit);

    void remove(RapportVisit rapportVisit);

    RapportVisit find(Object id);

    List<RapportVisit> findAll();

    List<RapportVisit> findRange(int[] range);

    int count();
    
    void create(List<RapportVisit> entitys);

    void edit(List<RapportVisit> entitys);

    void remove(List<RapportVisit> entitys);

    List<RapportVisit> findAll(String clause);

    List<RapportVisit> findAllNativeLimit(String clause);
    
    List<RapportVisit> findAllNative(String clause);
    
}
