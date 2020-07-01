package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.PrefixBonCommandeVente;
import com.magma.entity.Utilisateur;
import com.magma.session.PrefixBonCommandeVenteFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
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

@ManagedBean(name = "prefixBonCommandeVenteController")
@SessionScoped
public class PrefixBonCommandeVenteController implements Serializable {

    private PrefixBonCommandeVente selected;
    private PrefixBonCommandeVente selectedSingle;
    private List<PrefixBonCommandeVente> items = null;
    @EJB
    private PrefixBonCommandeVenteFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private PrefixBonCommandeVente prefixBonCommandeVente;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    public PrefixBonCommandeVenteController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (prefixBonCommandeVente.getIdEntrepriseSuivi() != null && prefixBonCommandeVente.getIdEntrepriseSuivi() != 0) {
         idEntreprise = prefixBonCommandeVente.getIdEntrepriseSuivi();
         } else {
         idEntreprise = prefixBonCommandeVente.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GPrefixBonCommandeVente", "MVente", "MVente", utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GPrefixBonCommandeVente", utilisateur);
            /*if (prefixBonCommandeVente.getIdEntrepriseSuivi() != null && prefixBonCommandeVente.getIdEntrepriseSuivi() != 0) {
             idEntreprise = prefixBonCommandeVente.getIdEntrepriseSuivi();
             } else {
             idEntreprise = prefixBonCommandeVente.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../prefixBonCommandeVente/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<PrefixBonCommandeVente> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("where o.supprimer = 0 order by o.libellePrefixe asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- PrefixBonCommandeVenteController - getItems: " + e.getMessage());
            return null;
        }
    }

    public PrefixBonCommandeVente getSelected() {
        return selected;
    }

    public void setSelected(PrefixBonCommandeVente selected) {
        this.selected = selected;
    }

    public PrefixBonCommandeVente getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(PrefixBonCommandeVente selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private PrefixBonCommandeVenteFacadeLocal getFacade() {
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
        selected = new PrefixBonCommandeVente();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibellePrefixe().trim());

            if (errorMsg == false) {
                creationInfo();
                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibellePrefixe() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- PrefixBonCommandeVenteController - create: " + e.getMessage());
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
                editionInfo();
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
            System.out.println("Erreur- PrefixBonCommandeVenteController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<PrefixBonCommandeVentePVT> temps = ejbFacadePrefixBonCommandeVentePVT.findAll("where o.idPrefixBonCommandeVente = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- PrefixBonCommandeVenteController - performDestroy: " + e.getMessage());
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

    public PrefixBonCommandeVente getPrefixBonCommandeVente(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = PrefixBonCommandeVente.class)
    public static class PrefixBonCommandeVenteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PrefixBonCommandeVenteController controller = (PrefixBonCommandeVenteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "prefixBonCommandeVenteController");
            return controller.getPrefixBonCommandeVente(getKey(value));
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
            if (object instanceof PrefixBonCommandeVente) {
                PrefixBonCommandeVente o = (PrefixBonCommandeVente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PrefixBonCommandeVente.class.getName());
            }
        }

    }

}
