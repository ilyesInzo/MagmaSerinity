package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.MotifAvoir;
import com.magma.entity.Utilisateur;
import com.magma.session.MotifAvoirFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "motifAvoirController")
@SessionScoped
public class MotifAvoirController implements Serializable {

    private MotifAvoir selected;
    private MotifAvoir selectedSingle;
    private List<MotifAvoir> items = null;
    @EJB
    private MotifAvoirFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private MotifAvoir motifAvoir;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    public MotifAvoirController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (motifAvoir.getIdEntrepriseSuivi() != null && motifAvoir.getIdEntrepriseSuivi() != 0) {
         idEntreprise = motifAvoir.getIdEntrepriseSuivi();
         } else {
         idEntreprise = motifAvoir.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GMotifAvoir", "MVente", "MVente", utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GMotifAvoir", utilisateur);
            /*if (motifAvoir.getIdEntrepriseSuivi() != null && motifAvoir.getIdEntrepriseSuivi() != 0) {
             idEntreprise = motifAvoir.getIdEntrepriseSuivi();
             } else {
             idEntreprise = motifAvoir.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../motifAvoir/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<MotifAvoir> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- MotifAvoirController - getItems: " + e.getMessage());
            return null;
        }
    }

    public MotifAvoir getSelected() {
        return selected;
    }

    public void setSelected(MotifAvoir selected) {
        this.selected = selected;
    }

    public MotifAvoir getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(MotifAvoir selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private MotifAvoirFacadeLocal getFacade() {
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
        selected = new MotifAvoir();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {
                creationInfo();
                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- MotifAvoirController - create: " + e.getMessage());
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
            System.out.println("Erreur- MotifAvoirController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<MotifAvoirPVT> temps = ejbFacadeMotifAvoirPVT.findAll("where o.idMotifAvoir = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- MotifAvoirController - performDestroy: " + e.getMessage());
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

    public MotifAvoir getMotifAvoir(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = MotifAvoir.class)
    public static class MotifAvoirControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MotifAvoirController controller = (MotifAvoirController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "motifAvoirController");
            return controller.getMotifAvoir(getKey(value));
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
            if (object instanceof MotifAvoir) {
                MotifAvoir o = (MotifAvoir) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + MotifAvoir.class.getName());
            }
        }

    }

}
