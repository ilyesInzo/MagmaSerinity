package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.Categorie;
import com.magma.entity.Utilisateur;
import com.magma.session.CategorieFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@Named("categorieController")
@SessionScoped
public class CategorieController implements Serializable {

    private Categorie selected;
    private Categorie selectedSingle;
    private List<Categorie> items = null;
    @EJB
    private CategorieFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private Categorie categorie;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    public CategorieController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (categorie.getIdEntrepriseSuivi() != null && categorie.getIdEntrepriseSuivi() != 0) {
                idEntreprise = categorie.getIdEntrepriseSuivi();
            } else {
                idEntreprise = categorie.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GCategorie", "MProduit", null,utilisateur);

           // MenuTemplate.menuFonctionnalitesModules("GCategorie", utilisateur);
            /*if (categorie.getIdEntrepriseSuivi() != null && categorie.getIdEntrepriseSuivi() != 0) {
                idEntreprise = categorie.getIdEntrepriseSuivi();
            } else {
                idEntreprise = categorie.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../categorie/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }


    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Categorie> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll(" where o.supprimer = 0 order by o.libelle asc");

            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- CategorieController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Categorie getSelected() {
        return selected;
    }

    public void setSelected(Categorie selected) {
        this.selected = selected;
    }

    public Categorie getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Categorie selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private CategorieFacadeLocal getFacade() {
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
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new Categorie();
        selected.setActiver(true);
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {

            String clause = " where o.Cat_Supprimer = 0 and o.Cat_Libelle like '" + selected.getLibelle().trim().toUpperCase() + "' ";

            if (selected.getParent() != null) {
                clause = clause + " and o.Cat_IdParent = " + selected.getParent().getId() + "";
            } else {
                clause = clause + " and o.Cat_Rang = " + selected.getRang() + "";
            }

            //errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());
            errorMsg = getFacade().verifierUnique(clause);

            if (errorMsg == false) {

                if (selected.getParent() != null) {
                    //  selected.setIdParent(selected.getParent().getId());
                    selected.setLibelleParent(selected.getParent().getLibelle());
                    selected.setRang(selected.getParent().getRang() + 1);
                    if (selected.getParent().getRang() > 0) {
                        selected.setIdPremierParent(selected.getParent().getIdPremierParent());
                        selected.setIdSuiteParent(selected.getParent().getIdSuiteParent() + " > " + selected.getParent().getId());
                        selected.setLibelleSuiteParent(selected.getParent().getLibelleSuiteParent() + " > " + selected.getParent().getLibelle());
                    } else {
                        selected.setIdPremierParent(selected.getParent().getId());
                        selected.setIdSuiteParent(selected.getParent().getId() + "");
                        selected.setLibelleSuiteParent(selected.getParent().getLibelle());
                    }
                } else {
                    selected.setLibelleSuiteParent("---");
                    selected.setIdSuiteParent("---");
                    selected.setRang(0);
                    selected.setIdPremierParent(null);
                    // selected.setIdParent(null);
                }

                selected.setSupprimer(false);
                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- CategorieController - create: " + e.getMessage());
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

            String clause = " where o.Cat_Supprimer = 0 and o.Cat_Libelle like '" + selected.getLibelle().trim().toUpperCase() + "' and o.Cat_Id <> " + selected.getId();

            if (selected.getParent() != null) {
                clause = clause + " and o.Cat_IdParent = " + selected.getParent().getId() + "";
            } else {
                clause = clause + " and o.Cat_Rang = " + selected.getRang() + "";
            }

            //errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());
            errorMsg = getFacade().verifierUnique(clause);

            if (errorMsg == false) {

                if (selected.getParent() != null) {
                    //  selected.setIdParent(selected.getParent().getId());
                    selected.setLibelleParent(selected.getParent().getLibelle());
                    selected.setRang(selected.getParent().getRang() + 1);
                    if (selected.getParent().getRang() > 0) {
                        selected.setIdPremierParent(selected.getParent().getIdPremierParent());
                        selected.setIdSuiteParent(selected.getParent().getIdSuiteParent() + " > " + selected.getParent().getId());
                        selected.setLibelleSuiteParent(selected.getParent().getLibelleSuiteParent() + " > " + selected.getParent().getLibelle());
                    } else {
                        selected.setIdPremierParent(selected.getParent().getId());
                        selected.setIdSuiteParent(selected.getParent().getId() + "");
                        selected.setLibelleSuiteParent(selected.getParent().getLibelle());
                    }
                } else {
                    selected.setLibelleSuiteParent("---");
                    selected.setIdSuiteParent("---");
                    selected.setRang(0);
                    selected.setIdPremierParent(null);
                    // selected.setIdParent(null);
                }
                selected.setSupprimer(false);
                getFacade().edit(selected);
                return prepareList();
                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- CategorieController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<CategoriePVT> temps = ejbFacadeCategoriePVT.findAll("where o.idCategorie = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- CategorieController - performDestroy: " + e.getMessage());
        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public SelectItem[] getItemsAvailableSelectOneProduits() {
        return JsfUtil.getSelectItems(getFacade().findAll(" where o.supprimer = 0 and o.dernierRang = 1 order by o.libelle asc"), true);
    }

    public SelectItem[] getItemsAvailableSelectOneParents() {
        return JsfUtil.getSelectItems(getFacade().findAll(" where o.supprimer = 0 and  o.dernierRang = 0 order by o.libelle asc"), true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Categorie getCategorie(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Categorie.class)
    public static class CategorieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategorieController controller = (CategorieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categorieController");
            return controller.getCategorie(getKey(value));
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
            if (object instanceof Categorie) {
                Categorie o = (Categorie) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Categorie.class.getName());
            }
        }

    }

}
