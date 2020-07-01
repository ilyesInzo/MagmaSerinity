/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Article;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author inzo
 */
@Stateless
public class ArticleFacade extends AbstractFacade<Article> implements ArticleFacadeLocal {

    @PersistenceContext(unitName = "Aserenity-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArticleFacade() {
        super(Article.class);
    }
    
        @Override
    public void editStockArticle(Long idArticle, BigDecimal quantite, String Operateur) {
        Query q = (Query) getEntityManager().createNativeQuery("Update T_Article SET Art_QuantiteStock = Art_QuantiteStock " + Operateur + " " + quantite + " where Art_Id = " + idArticle);
        q.executeUpdate();
    }
    
}
