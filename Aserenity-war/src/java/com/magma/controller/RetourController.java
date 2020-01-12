package com.magma.controller;

import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Client;
import com.magma.entity.LigneRetour;
import com.magma.entity.Retour;
import com.magma.entity.Utilisateur;
import com.magma.session.ArticleFacadeLocal;
import com.magma.session.RetourFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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

@Named("retourController")
@SessionScoped
public class RetourController implements Serializable {

    private Retour selected;
    private Retour selectedSingle;
    private List<Retour> items = null;
    @EJB
    private RetourFacadeLocal ejbFacade;
    @EJB
    private ArticleFacadeLocal ejbFacadeArticle;
    private boolean errorMsg;
    private Long idTemp;
    private Retour retour;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    
    
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Integer etatRetour;
    private Client client;

    public RetourController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (retour.getIdEntrepriseSuivi() != null && retour.getIdEntrepriseSuivi() != 0) {
                idEntreprise = retour.getIdEntrepriseSuivi();
            } else {
                idEntreprise = retour.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GRetour", "MVente", null,utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GRetour", utilisateur);
            /*if (retour.getIdEntrepriseSuivi() != null && retour.getIdEntrepriseSuivi() != 0) {
                idEntreprise = retour.getIdEntrepriseSuivi();
            } else {
                idEntreprise = retour.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../retour/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Retour> getItems() {
        try {
            if (items == null) {
                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.Tab_DateCreation between '" + debut + "' and '" + fin + "' ";
                items = getFacade().findAllNative(clause + " order by o.Tab_DateCreation desc");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- RetourController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Retour getSelected() {
        return selected;
    }

    public void setSelected(Retour selected) {
        this.selected = selected;
    }

    public Retour getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Retour selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private RetourFacadeLocal getFacade() {
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
        selected = new Retour();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            //errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {

                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- RetourController - create: " + e.getMessage());
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
//            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());

            if (errorMsg == false) {

                /* if (selected.getMontant().compareTo(BigDecimal.ZERO) == -1) {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("ValeurIncorrecte")));
                    return null;
                } else {*/
                //selected.setIdEntreprise(idEntreprise);
                boolean erreur = false;
                for (LigneRetour ligneRetour : selected.getListeLigneRetours()) {

                    if (ligneRetour.getQuantiteCasse().compareTo(ligneRetour.getQuantite()) == 1 || ligneRetour.getQuantiteCasse().compareTo(BigDecimal.ZERO) == -1) {
                        erreur = true;
                        break;
                    }

                }

                if (erreur) {

                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), ": " + ResourceBundle.getBundle("/Bundle").getString("QuantiteError")));
                    return null;

                } else {

                    for (LigneRetour ligneRetour : selected.getListeLigneRetours()) {

                        BigDecimal quantite = ligneRetour.getQuantite().subtract(ligneRetour.getQuantiteCasse());
                        System.out.println(quantite);
                        System.out.println(ligneRetour.getIdArticle());
                        if (quantite.compareTo(BigDecimal.ZERO) == 1) {
                            ejbFacadeArticle.editStockArticle(ligneRetour.getIdArticle(), quantite);
                        }

                    }

                    selected.setEtat(1);
                    getFacade().edit(selected);
                    return prepareList();

                }

                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- RetourController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<RetourPVT> temps = ejbFacadeRetourPVT.findAll("where o.idRetour = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- RetourController - performDestroy: " + e.getMessage());
        }
    }
    
    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";


                items = getFacade().searchAllNative(debut, fin, etatRetour, client != null ? client.getId() : null);

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

    public Integer getEtatRetour() {
        return etatRetour;
    }

    public void setEtatRetour(Integer etatRetour) {
        this.etatRetour = etatRetour;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void onCellEdit() {
        selected.setTotalTTC(BigDecimal.ZERO);
        for (LigneRetour ligneRetour : selected.getListeLigneRetours()) {

            selected.setTotalTTC(selected.getTotalTTC().add(ligneRetour.getTotalTTC()));

        }

    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Retour getRetour(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Retour.class)
    public static class RetourControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RetourController controller = (RetourController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "retourController");
            return controller.getRetour(getKey(value));
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
            if (object instanceof Retour) {
                Retour o = (Retour) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Retour.class.getName());
            }
        }

    }

}
