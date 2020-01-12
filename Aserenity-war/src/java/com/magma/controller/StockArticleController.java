/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.controller;

import com.magma.entity.Article;
import com.magma.entity.Utilisateur;
import com.magma.session.ArticleFacadeLocal;
import com.magma.session.util.JsfUtil;
import com.magma.util.MenuTemplate;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author inzo
 */
@Named("stockArticleController")
@SessionScoped
public class StockArticleController implements Serializable {

    private Article selected;
    private Article selectedSingle;
    private List<Article> items = null;
    @EJB
    private ArticleFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private Article article;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    private List<Article> listArticles;

    public StockArticleController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (stockArticle.getIdEntrepriseSuivi() != null && stockArticle.getIdEntrepriseSuivi() != 0) {
                idEntreprise = stockArticle.getIdEntrepriseSuivi();
            } else {
                idEntreprise = stockArticle.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GStockArticle", "MStock", null,utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GStockArticle", utilisateur);
            /*if (stockArticle.getIdEntrepriseSuivi() != null && stockArticle.getIdEntrepriseSuivi() != 0) {
                idEntreprise = stockArticle.getIdEntrepriseSuivi();
            } else {
                idEntreprise = stockArticle.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../stock/View.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Article> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- StockArticleController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Article getSelected() {
        return selected;
    }

    public void setSelected(Article selected) {
        this.selected = selected;
    }

    public Article getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Article selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private ArticleFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        listArticles = null;
        return "List";
    }

    public String prepareView() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        listArticles = null;

        return "View";

    }

    public String prepareCreate() {
        selected = new Article();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {

                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- StockArticleController - create: " + e.getMessage());
            return null;
        }

    }

    public Long getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(Long idTemp) {
        this.idTemp = idTemp;
    }

    public String prepareEdit() {
//        if (selected != null) {
        errorMsg = false;
        //idTemp = selected.getId();
        listArticles = new ArrayList<>();
        return "Edit";
//        }
//        return "List";
    }

    public String update() {
        try {

            if (listArticles != null && !listArticles.isEmpty()) {
                getFacade().edit(listArticles);
            }
            return prepareView();
            //}

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- StockArticleController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<StockArticlePVT> temps = ejbFacadeStockArticlePVT.findAll("where o.idStockArticle = " + selectedSingle.getId() + " ");
            //if (temps == null || temps.isEmpty()) {
            performDestroy();
            /*} else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SuppressionNonAutorisé")));
            }*/
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet")));
        }
        return prepareList();
    }

    private void performDestroy() {
        try {
            getFacade().remove(selectedSingle);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- StockArticleController - performDestroy: " + e.getMessage());
        }
    }

    public void onStockEdit(Article article) {

        int index = listArticles.indexOf(article);

        if (index == -1) {
            listArticles.add(article);
        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Article getStockArticle(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Article.class)
    public static class StockArticleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StockArticleController controller = (StockArticleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "stockArticleController");
            return controller.getStockArticle(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Article) {
                Article o = (Article) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Article.class.getName());
            }
        }

    }

}
