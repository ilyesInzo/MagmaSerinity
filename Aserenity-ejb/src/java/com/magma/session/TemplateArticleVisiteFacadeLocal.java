/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.TemplateArticleVisite;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface TemplateArticleVisiteFacadeLocal {

    void create(TemplateArticleVisite templateArticleVisite);

    void edit(TemplateArticleVisite templateArticleVisite);

    void remove(TemplateArticleVisite templateArticleVisite);

    TemplateArticleVisite find(Object id);

    List<TemplateArticleVisite> findAll();

    List<TemplateArticleVisite> findRange(int[] range);

    int count();
    
    void create(List<TemplateArticleVisite> entitys);

    void edit(List<TemplateArticleVisite> entitys);

    void remove(List<TemplateArticleVisite> entitys);

    List<TemplateArticleVisite> findAll(String clause);
    
    public boolean verifierUnique(String libelle);
            
    public boolean verifierUnique(String libelle, Object id);
    
}
