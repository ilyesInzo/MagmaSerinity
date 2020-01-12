package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.PrefixDevis;
import com.magma.entity.Utilisateur;
import com.magma.session.PrefixDevisFacadeLocal;
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

@Named("prefixDevisController")
@SessionScoped
public class PrefixDevisController implements Serializable {

    private PrefixDevis selected;
    private PrefixDevis selectedSingle;
    private List<PrefixDevis> items = null;
    @EJB
    private PrefixDevisFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private PrefixDevis prefixDevis;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    public PrefixDevisController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (prefixDevis.getIdEntrepriseSuivi() != null && prefixDevis.getIdEntrepriseSuivi() != 0) {
                idEntreprise = prefixDevis.getIdEntrepriseSuivi();
            } else {
                idEntreprise = prefixDevis.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GPrefixDevis", "MVente", "MVente", utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GPrefixDevis", utilisateur);
            /*if (prefixDevis.getIdEntrepriseSuivi() != null && prefixDevis.getIdEntrepriseSuivi() != 0) {
                idEntreprise = prefixDevis.getIdEntrepriseSuivi();
            } else {
                idEntreprise = prefixDevis.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../prefixDevis/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<PrefixDevis> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("where o.supprimer = 0 order by o.libellePrefixe asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- PrefixDevisController - getItems: " + e.getMessage());
            return null;
        }
    }

    public PrefixDevis getSelected() {
        return selected;
    }

    public void setSelected(PrefixDevis selected) {
        this.selected = selected;
    }

    public PrefixDevis getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(PrefixDevis selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private PrefixDevisFacadeLocal getFacade() {
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
        selected = new PrefixDevis();
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
            System.out.println("Erreur- PrefixDevisController - create: " + e.getMessage());
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
            System.out.println("Erreur- PrefixDevisController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<PrefixDevisPVT> temps = ejbFacadePrefixDevisPVT.findAll("where o.idPrefixDevis = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- PrefixDevisController - performDestroy: " + e.getMessage());
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

    public PrefixDevis getPrefixDevis(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = PrefixDevis.class)
    public static class PrefixDevisControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PrefixDevisController controller = (PrefixDevisController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "prefixDevisController");
            return controller.getPrefixDevis(getKey(value));
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
            if (object instanceof PrefixDevis) {
                PrefixDevis o = (PrefixDevis) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PrefixDevis.class.getName());
            }
        }

    }

}
