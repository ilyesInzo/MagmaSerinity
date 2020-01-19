package com.magma.controller;

import com.magma.bibliotheque.TraitementDate;
import com.magma.entity.AvoirVente;
import com.magma.controller.util.JsfUtil;
import com.magma.controller.util.PaginationHelper;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.MotifAvoir;
import com.magma.entity.Utilisateur;
import com.magma.session.AvoirVenteFacade;
import com.magma.session.AvoirVenteFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean(name= "avoirVenteController")
@SessionScoped
public class AvoirVenteController implements Serializable {

    private AvoirVente selected;
    private AvoirVente selectedSingle;
    private List<AvoirVente> items = null;
    @EJB
    private AvoirVenteFacadeLocal ejbFacade;

    private boolean errorMsg;
    private Long idTemp;
    private Utilisateur utilisateur = null;
    private long idEntreprise = 0;

    private Client client;
    private List<Client> listClient;
    private CategorieClient categorieClient;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private MotifAvoir motifAvoir;

    private Integer typeAvoir;

    public AvoirVenteController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GAvoirVente", "MVente", null,utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GAvoirVente", utilisateur);
            /*if (prefixAvoirVente.getIdEntrepriseSuivi() != null && prefixAvoirVente.getIdEntrepriseSuivi() != 0) {
                idEntreprise = prefixAvoirVente.getIdEntrepriseSuivi();
            } else {
                idEntreprise = prefixAvoirVente.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../avoirVente/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<AvoirVente> getItems() {
        try {
            if (items == null) {
                /*String debut = TraitementDate.returnDateHeure(dateDebut);
                String fin = TraitementDate.returnDateHeure(dateFin);*/
                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.Tab_DateCreation between '" + debut + "' and '" + fin + "' ";
                items = getFacade().findAllNative(clause + " order by o.Tab_DateCreation desc");


            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- avoirVenteController - getItems: " + e.getMessage());
            return null;
        }
    }

    public AvoirVente getSelected() {
        return selected;
    }

    public void setSelected(AvoirVente selected) {
        this.selected = selected;
    }

    public AvoirVente getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(AvoirVente selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private AvoirVenteFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        categorieClient = null;
        client = null;
        listClient = null;

        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new AvoirVente();
        errorMsg = false;
        return "Create";
    }

    public String create() {
        try {
            //errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), idEntreprise);
            if (errorMsg == false) {
               
                //selected.setDateSynchro(new Date().getTime());
                selected.setSupprimer(false);
                getFacade().create(selected);
                return prepareList();
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- avoirVenteController - create: " + e.getMessage());
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
            // errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId(), idEntreprise);
            if (errorMsg == false) {
               
                //selected.setDateSynchro(new Date().getTime());
                selected.setSupprimer(false);
                getFacade().edit(selected);
                return prepareList();
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- avoirVenteController - update: " + e.getMessage());
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
            // selectedSingle.setDateSynchro(new Date().getTime());
            selectedSingle.setSupprimer(true);
            getFacade().edit(selectedSingle);
            //getFacade().remove(selectedSingle);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SuppressionNonAutorisé")));
            System.out.println("Erreur- avoirVenteController - performDestroy: " + e.getMessage());
        }
    }

    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";


                items = getFacade().searchAllNative(debut, fin, client != null ? client.getId() : null);

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreuPeriode")));
            }

        } catch (Exception e) {

        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CategorieClient getCategorieClient() {
        return categorieClient;
    }

    public void setCategorieClient(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public MotifAvoir getMotifAvoir() {
        return motifAvoir;
    }

    public void setMotifAvoir(MotifAvoir motifAvoir) {
        this.motifAvoir = motifAvoir;
    }

    public Integer getTypeAvoir() {
        return typeAvoir;
    }

    public void setTypeAvoir(Integer typeAvoir) {
        this.typeAvoir = typeAvoir;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public AvoirVente getAvoirVente(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = AvoirVente.class)
    public static class AvoirVenteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AvoirVenteController controller = (AvoirVenteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "avoirVenteController");
            return controller.getAvoirVente(getKey(value));
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
            if (object instanceof AvoirVente) {
                AvoirVente o = (AvoirVente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + AvoirVente.class.getName());
            }
        }

    }

}
