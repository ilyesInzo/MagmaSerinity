package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.PrefixBonLivraison;
import com.magma.entity.Utilisateur;
import com.magma.session.PrefixBonLivraisonFacadeLocal;
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

@Named("prefixBonLivraisonController")
@SessionScoped
public class PrefixBonLivraisonController implements Serializable {

    private PrefixBonLivraison selected;
    private PrefixBonLivraison selectedSingle;
    private List<PrefixBonLivraison> items = null;
    @EJB
    private PrefixBonLivraisonFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private PrefixBonLivraison prefixBonLivraison;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    public PrefixBonLivraisonController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (prefixBonLivraison.getIdEntrepriseSuivi() != null && prefixBonLivraison.getIdEntrepriseSuivi() != 0) {
                idEntreprise = prefixBonLivraison.getIdEntrepriseSuivi();
            } else {
                idEntreprise = prefixBonLivraison.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GPrefixBonLivraison", "MVente", "MVente", utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GPrefixBonLivraison", utilisateur);
            /*if (prefixBonLivraison.getIdEntrepriseSuivi() != null && prefixBonLivraison.getIdEntrepriseSuivi() != 0) {
                idEntreprise = prefixBonLivraison.getIdEntrepriseSuivi();
            } else {
                idEntreprise = prefixBonLivraison.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../prefixBonLivraison/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<PrefixBonLivraison> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("where o.supprimer = 0 order by o.libellePrefixe asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- PrefixBonLivraisonController - getItems: " + e.getMessage());
            return null;
        }
    }

    public PrefixBonLivraison getSelected() {
        return selected;
    }

    public void setSelected(PrefixBonLivraison selected) {
        this.selected = selected;
    }

    public PrefixBonLivraison getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(PrefixBonLivraison selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private PrefixBonLivraisonFacadeLocal getFacade() {
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
        selected = new PrefixBonLivraison();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibellePrefixe().trim());

            if (errorMsg == false) {

                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibellePrefixe() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- PrefixBonLivraisonController - create: " + e.getMessage());
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
            errorMsg = getFacade().verifierUnique(selected.getLibellePrefixe().trim(), selected.getId());

            if (errorMsg == false) {
                /* if (selected.getMontant().compareTo(BigDecimal.ZERO) == -1) {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("ValeurIncorrecte")));
                    return null;
                } else {*/
                //selected.setIdEntreprise(idEntreprise);
                getFacade().edit(selected);
                return prepareList();
                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibellePrefixe() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- PrefixBonLivraisonController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<PrefixBonLivraisonPVT> temps = ejbFacadePrefixBonLivraisonPVT.findAll("where o.idPrefixBonLivraison = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- PrefixBonLivraisonController - performDestroy: " + e.getMessage());
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

    public PrefixBonLivraison getPrefixBonLivraison(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = PrefixBonLivraison.class)
    public static class PrefixBonLivraisonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PrefixBonLivraisonController controller = (PrefixBonLivraisonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "prefixBonLivraisonController");
            return controller.getPrefixBonLivraison(getKey(value));
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
            if (object instanceof PrefixBonLivraison) {
                PrefixBonLivraison o = (PrefixBonLivraison) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PrefixBonLivraison.class.getName());
            }
        }

    }

}
