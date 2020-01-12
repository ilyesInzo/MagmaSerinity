/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Article;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface ArticleFacadeLocal {

    void create(Article article);

    void edit(Article article);

    void remove(Article article);

    void create(List<Article> article);

    void edit(List<Article> article);

    void remove(List<Article> article);

    Article find(Object id);

    List<Article> findAll();

    List<Article> findAll(String clause);

    List<Article> findRange(int[] range);

    public boolean verifierUnique(String libelle);

    public boolean verifierUnique(String libelle, Object id);

    int count();

    void editStockArticle(Long idArticle, BigDecimal quantite);

}
