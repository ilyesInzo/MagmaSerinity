package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.Article;
import com.magma.entity.Categorie;
import com.magma.entity.TemplateArticleVisite;
import com.magma.entity.Utilisateur;
import com.magma.session.CategorieFacadeLocal;
import com.magma.session.TemplateArticleVisiteFacadeLocal;
import com.magma.util.Document;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "templateArticleVisiteController")
@SessionScoped
public class TemplateArticleVisiteController implements Serializable {

    private TemplateArticleVisite selected;
    private TemplateArticleVisite selectedSingle;
    private List<TemplateArticleVisite> items = null;
    @EJB
    private TemplateArticleVisiteFacadeLocal ejbFacade;
    @EJB
    private CategorieFacadeLocal ejbCategorie;
    private boolean errorMsg;
    private Long idTemp;
    private TemplateArticleVisite templateArticleVisite;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    private TreeNode[] selectedNodes;
    private TreeNode root;
    private List<String> listeLibelleArticles = null;
    private List<Article> listArticlesPhare = null;
    private List<TemplateArticleVisite> listeTemplateArticleVistes = null;
    private List<Categorie> listeCategories = null;

    public TemplateArticleVisiteController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (templateArticleVisite.getIdEntrepriseSuivi() != null && templateArticleVisite.getIdEntrepriseSuivi() != 0) {
         idEntreprise = templateArticleVisite.getIdEntrepriseSuivi();
         } else {
         idEntreprise = templateArticleVisite.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GTemplateArticleVisite", "MCommercial", "MCommercial", utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GTemplateArticleVisite", utilisateur);
            /*if (templateArticleVisite.getIdEntrepriseSuivi() != null && templateArticleVisite.getIdEntrepriseSuivi() != 0) {
             idEntreprise = templateArticleVisite.getIdEntrepriseSuivi();
             } else {
             idEntreprise = templateArticleVisite.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../templateArticleVisite/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
        selectedNodes = null;
    }

    public List<TemplateArticleVisite> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- TemplateArticleVisiteController - getItems: " + e.getMessage());
            return null;
        }
    }

    public TemplateArticleVisite getSelected() {
        return selected;
    }

    public void setSelected(TemplateArticleVisite selected) {
        this.selected = selected;
    }

    public TemplateArticleVisite getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(TemplateArticleVisite selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private TemplateArticleVisiteFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            listeLibelleArticles = new ArrayList<>();
            viewCheckboxArborescence();
            return "View";
        }
        return "List";
    }

    public TreeNode viewCheckboxArborescence() {
        listeCategories = ejbCategorie.findAll();
        List<String> listId = new ArrayList<>();
        List<TreeNode> listTreeNode = new ArrayList<>();
        listArticlesPhare = new ArrayList<>();
        root = new CheckboxTreeNode(new Document("Files", Long.parseLong("0"), "Folder", ""), null);
        if (listeCategories != null && !listeCategories.isEmpty()) {
            int i = 0;
            for (Categorie categorie : listeCategories) {
                if (categorie.getParent() != null) {
                    if (listId.contains(categorie.getParent().getId() + "")) {
                        TreeNode documentParent = listTreeNode.get(listId.indexOf(categorie.getParent().getId() + ""));
                        TreeNode document = new CheckboxTreeNode(new Document(categorie.getLibelle(), categorie.getId(), "Categorie", ""), documentParent);
                        if (categorie.isDernierRang() == true) {
                            if (categorie.getArticles() != null && !categorie.getArticles().isEmpty()) {
                                for (Article article : categorie.getArticles()) {
                                    TreeNode articleTree = new CheckboxTreeNode(new Document(article.getLibelle(), article.getId(), "Article", article.getCode()), document);
                                    if (selected.getSelection().contains(article.getId() + "")) {
                                        articleTree.setSelected(true);
                                        listeLibelleArticles.add(article.getCode() + " - " + article.getLibelle());
                                        listArticlesPhare.add(article);
                                    }
                                }
                            }
                        }
                        listId.add(categorie.getId() + "");
                        listTreeNode.add(document);
                    }
                } else {
                    TreeNode document = new CheckboxTreeNode(new Document(categorie.getLibelle(), categorie.getId(), "Categorie", ""), root);
                    if (categorie.isDernierRang() == true) {
                        if (categorie.getArticles() != null && !categorie.getArticles().isEmpty()) {
                            for (Article article : categorie.getArticles()) {
                                TreeNode articleTree = new CheckboxTreeNode(new Document(article.getLibelle(), article.getId(), "Article", article.getCode()), document);
                                if (selected.getSelection().contains(article.getId() + "")) {
                                    articleTree.setSelected(true);
                                    listeLibelleArticles.add(article.getCode() + " - " + article.getLibelle());
                                    listArticlesPhare.add(article);
                                }
                            }
                        }
                    }
                    listId.add(categorie.getId() + "");
                    listTreeNode.add(document);
                }
            }
        }
        return root;
    }

    public String prepareCreate() {
        createCheckboxArborescence();
        selected = new TemplateArticleVisite();
        errorMsg = false;
        return "Create";
    }

    public TreeNode createCheckboxArborescence() {
        listeCategories = ejbCategorie.findAll();
        List<String> listId = new ArrayList<>();
        List<TreeNode> listTreeNode = new ArrayList<>();
        root = new CheckboxTreeNode(new Document("Files", Long.parseLong("0"), "Folder", ""), null);
        if (listeCategories != null && !listeCategories.isEmpty()) {
            for (Categorie categorie : listeCategories) {
                if (categorie.getParent() != null) {
                    if (listId.contains(categorie.getParent().getId() + "")) {
                        TreeNode documentParent = listTreeNode.get(listId.indexOf(categorie.getParent().getId() + ""));
                        TreeNode document = new CheckboxTreeNode(new Document(categorie.getLibelle(), categorie.getId(), "Categorie", ""), documentParent);
                        if (categorie.isDernierRang() == true) {
                            if (categorie.getArticles() != null && !categorie.getArticles().isEmpty()) {
                                for (Article article : categorie.getArticles()) {
                                    TreeNode articleTree = new CheckboxTreeNode(new Document(article.getLibelle(), article.getId(), "Article", article.getCode()), document);
                                }
                            }
                        }
                        listId.add(categorie.getId() + "");
                        listTreeNode.add(document);
                    }
                } else {
                    TreeNode document = new CheckboxTreeNode(new Document(categorie.getLibelle(), categorie.getId(), "Categorie", ""), root);
                    if (categorie.isDernierRang() == true) {
                        if (categorie.getArticles() != null && !categorie.getArticles().isEmpty()) {
                            for (Article article : categorie.getArticles()) {
                                TreeNode articleTree = new CheckboxTreeNode(new Document(article.getLibelle(), article.getId(), "Article", article.getCode()), document);
                            }
                        }
                    }
                    listId.add(categorie.getId() + "");
                    listTreeNode.add(document);
                }
            }
        }
        return root;
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {
                creationInfo();

                if (selectedNodes.length >= 1) {
                    String selection = "", selectionLibelleArticle = "";
                    for (TreeNode selectedNode : selectedNodes) {
                        if (((Document) selectedNode.getData()).getType().equals("Article")) {
                            selection = selection + ((Document) selectedNode.getData()).getId() + ";";
                            selectionLibelleArticle = selectionLibelleArticle + ((Document) selectedNode.getData()).getCode() + " - " + ((Document) selectedNode.getData()).getName() + ";";
                        }
                    }
                    selected.setSelection(selection);
                    selected.setLibelleArticle(selectionLibelleArticle);
                    selected.setSupprimer(false);
                    selected.setDateSynchro(new Date().getTime());

                    getFacade().create(selected);
                    return prepareList();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurListeArticle")));
                    return null;
                }

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- TemplateArticleVisiteController - create: " + e.getMessage());
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
        if (selected != null) {
            errorMsg = false;
            idTemp = selected.getId();
            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());

            if (errorMsg == false) {
                editionInfo();
                if (selectedNodes.length >= 1) {
                    String selection = "", selectionLibelleArticle = "";
                    for (TreeNode selectedNode : selectedNodes) {
                        if (((Document) selectedNode.getData()).getType().equals("Article")) {
                            selection = selection + ((Document) selectedNode.getData()).getId() + ";";
                            selectionLibelleArticle = selectionLibelleArticle + ((Document) selectedNode.getData()).getCode() + " - " + ((Document) selectedNode.getData()).getName() + ";";
                        }
                    }
                    selected.setSelection(selection);
                    selected.setLibelleArticle(selectionLibelleArticle);
                    selected.setSupprimer(false);
                    selected.setDateSynchro(new Date().getTime());
                    getFacade().edit(selected);
                    return prepareList();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurListeArticle")));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- TemplateArticleVisiteController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            performDestroy();
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
            System.out.println("Erreur- TemplateArticleVisiteController - performDestroy: " + e.getMessage());
        }
    }

    private void creationInfo() {
        selected.setIdUserCreate(utilisateur.getId());
        selected.setLibelleUserCreate(utilisateur.getNomPrenom());
    }

    private void editionInfo() {
        selected.setIdUserModif(utilisateur.getId());
        selected.setLibelleUserModif(utilisateur.getNomPrenom());
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<String> getListeLibelleArticles() {
        return listeLibelleArticles;
    }

    public void setListeLibelleArticles(List<String> listeLibelleArticles) {
        this.listeLibelleArticles = listeLibelleArticles;
    }

    public List<Article> getListArticlesPhare() {
        return listArticlesPhare;
    }

    public void setListArticlesPhare(List<Article> listArticlesPhare) {
        this.listArticlesPhare = listArticlesPhare;
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

    public TemplateArticleVisite getTemplateArticleVisite(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = TemplateArticleVisite.class)
    public static class TemplateArticleVisiteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TemplateArticleVisiteController controller = (TemplateArticleVisiteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "templateArticleVisiteController");
            return controller.getTemplateArticleVisite(getKey(value));
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
            if (object instanceof TemplateArticleVisite) {
                TemplateArticleVisite o = (TemplateArticleVisite) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TemplateArticleVisite.class.getName());
            }
        }

    }

}
