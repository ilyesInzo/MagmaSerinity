package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.Prospection;
import com.magma.entity.Utilisateur;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.ProspectionFacadeLocal;
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

@ManagedBean(name = "prospectionController")
@SessionScoped
public class ProspectionController implements Serializable {

    private Prospection selected;
    private Prospection selectedSingle;
    private List<Prospection> items = null;
    @EJB
    private ProspectionFacadeLocal ejbFacade;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    private boolean errorMsg;
    private Long idTemp;
    private Prospection prospection;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    public ProspectionController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (prospection.getIdEntrepriseSuivi() != null && prospection.getIdEntrepriseSuivi() != 0) {
         idEntreprise = prospection.getIdEntrepriseSuivi();
         } else {
         idEntreprise = prospection.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GProspection", "MVeille", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GProspection", utilisateur);
            /*if (prospection.getIdEntrepriseSuivi() != null && prospection.getIdEntrepriseSuivi() != 0) {
             idEntreprise = prospection.getIdEntrepriseSuivi();
             } else {
             idEntreprise = prospection.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../prospection/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Prospection> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- ProspectionController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Prospection getSelected() {
        return selected;
    }

    public void setSelected(Prospection selected) {
        this.selected = selected;
    }

    public Prospection getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Prospection selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private ProspectionFacadeLocal getFacade() {
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
        selected = new Prospection();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            // errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {
                //selected.setSupprimer(false);

                selected.setIdCommercial(selected.getCommercial().getId());
                selected.setNomCommercial(selected.getCommercial().getNom());
                selected.setPrenomCommercial(selected.getCommercial().getPrenom());

                selected.setIdGouvernorat(selected.getGouvernorat().getId());
                selected.setLibelleGouvernorat(selected.getGouvernorat().getLibelle());

                selected.setIdDelegation(selected.getDelegation().getId());
                selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                //selected.setSupression(false);
                selected.setDateSynch(System.currentTimeMillis());
                selected.setDateCreation(new Date());

                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- ProspectionController - create: " + e.getMessage());
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
            //errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());

            if (errorMsg == false) {

                //selected.setSupprimer(false);
                // Transdorm To
                if (selected.getEtatProspection() == 1) {
                    Client client = new Client();
                    client.setAdresse(selected.getAdresse());
                    client.setCodePostale(selected.getCodePostale());
                    client.setIdDelegation(selected.getIdDelegation());
                    client.setIdGouvernorat(selected.getIdGouvernorat());
                    client.setLibelle(selected.getLibelle());
                    //client.setEmail(selected.get);
                    client.setGsm(selected.getGsm());

                    CategorieClient categorieClient = new CategorieClient();
                    categorieClient.setId(selected.getIdCategorieClient());
                    client.setCategorieClient(categorieClient);

                    client.setDateCreation(new Date());
                    client.setAssujettiTVA(true);

                    ejbFacadeClient.create(client);

                }

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
            System.out.println("Erreur- ProspectionController - update: " + e.getMessage());
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
            System.out.println("Erreur- ProspectionController - performDestroy: " + e.getMessage());
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

    public Prospection getProspection(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Prospection.class)
    public static class ProspectionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProspectionController controller = (ProspectionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "prospectionController");
            return controller.getProspection(getKey(value));
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
            if (object instanceof Prospection) {
                Prospection o = (Prospection) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Prospection.class.getName());
            }
        }

    }

}
