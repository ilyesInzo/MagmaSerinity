package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.Commercial;
import com.magma.entity.Utilisateur;
import com.magma.session.CommercialFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
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

@ManagedBean(name = "commercialController")
@SessionScoped
public class CommercialController implements Serializable {

    private Commercial selected;
    private Commercial selectedSingle;
    private List<Commercial> items = null;
    @EJB
    private CommercialFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private Utilisateur commercial;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    public CommercialController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (commercial.getIdEntrepriseSuivi() != null && commercial.getIdEntrepriseSuivi() != 0) {
         idEntreprise = commercial.getIdEntrepriseSuivi();
         } else {
         idEntreprise = commercial.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GCommercial", "MCommercial", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GCommercial", utilisateur);
            /*if (commercial.getIdEntrepriseSuivi() != null && commercial.getIdEntrepriseSuivi() != 0) {
             idEntreprise = commercial.getIdEntrepriseSuivi();
             } else {
             idEntreprise = commercial.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../commercial/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Commercial> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.nom asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- CommercialController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Commercial getSelected() {
        return selected;
    }

    public void setSelected(Commercial selected) {
        this.selected = selected;
    }

    public Commercial getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Commercial selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private CommercialFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        commercial= null;
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new Commercial();
        commercial = new Utilisateur();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(commercial.getEmail().trim());
            if (errorMsg == false) {
                selected.setId(commercial.getId());
                selected.setCode(commercial.getCode());
                selected.setDateSynch(new Date().getTime());
                selected.setNom(commercial.getNom());
                selected.setPrenom(commercial.getPrenom());
                selected.setEmail(commercial.getEmail());
                selected.setGsm(commercial.getGsm());
                selected.setPasswd(commercial.getPasswd());
                selected.setEtatUsr(commercial.isEtatUsr());
                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getEmail() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- CommercialController - create: " + e.getMessage());
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
            errorMsg = getFacade().verifierUnique(selected.getEmail().trim(), selected.getId());

            if (errorMsg == false) {

                getFacade().edit(selected);
                return prepareList();
                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getEmail() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- CommercialController - update: " + e.getMessage());
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
            System.out.println("Erreur- CommercialController - performDestroy: " + e.getMessage());
        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Utilisateur getCommercial() {
        return commercial;
    }

    public void setCommercial(Utilisateur commercial) {
        this.commercial = commercial;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAllNative(" where o.Usr_EtatUsr = 1"), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAllNative(" where o.Usr_EtatUsr = 1"), true);
    }

    @FacesConverter(forClass = Commercial.class)
    public static class CommercialControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommercialController controller = (CommercialController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "commercialController");
            return controller.ejbFacade.find(getKey(value));
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
            if (object instanceof Commercial) {
                Commercial o = (Commercial) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Commercial.class.getName());
            }
        }

    }

}
