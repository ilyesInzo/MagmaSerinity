/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.RapportVisitArticle;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface RapportVisitArticleFacadeLocal {

    void create(RapportVisitArticle rapportVisitArticle);

    void edit(RapportVisitArticle rapportVisitArticle);

    void remove(RapportVisitArticle rapportVisitArticle);
    
    void create(List< RapportVisitArticle> rapportVisitArticle);

    void edit(List<RapportVisitArticle> rapportVisitArticle);

    void remove(List<RapportVisitArticle> rapportVisitArticle);

    RapportVisitArticle find(Object id);

    List<RapportVisitArticle> findAll();

    List<RapportVisitArticle> findRange(int[] range);

    int count();
    
}
