package com.magma.controller;

import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.ParametrageEntreprise;
import com.magma.entity.PlanificationVisite;
import com.magma.entity.RapportVisit;
import com.magma.entity.Utilisateur;
import com.magma.session.PlanificationVisiteFacadeLocal;
import com.magma.session.RapportVisitFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.time.LocalDate;
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
import org.primefaces.event.FlowEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

@ManagedBean(name = "rapportVisitController")
@SessionScoped
public class RapportVisitController implements Serializable {

    private RapportVisit selected;
    private RapportVisit selectedSingle;
    private List<RapportVisit> items = null;
    private List<PlanificationVisite> itemsPlanification = null;
    @EJB
    private RapportVisitFacadeLocal ejbFacade;

    @EJB
    private PlanificationVisiteFacadeLocal ejbFacadePlanification;
    private boolean errorMsg;
    private Long idTemp;
    private RapportVisit rapportVisit;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    private boolean skip;
    private PlanificationVisite selectedPlanification;
    private ParametrageEntreprise parametrageEntreprise;
    private TimelineModel timeLineModel;
    private Date dateStartTimeline;

    public RapportVisitController() {
        items = null;
        itemsPlanification = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (rapportVisit.getIdEntrepriseSuivi() != null && rapportVisit.getIdEntrepriseSuivi() != 0) {
         idEntreprise = rapportVisit.getIdEntrepriseSuivi();
         } else {
         idEntreprise = rapportVisit.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GRapportVisite", "MCommercial", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GRapportVisit", utilisateur);
            /*if (rapportVisit.getIdEntrepriseSuivi() != null && rapportVisit.getIdEntrepriseSuivi() != 0) {
             idEntreprise = rapportVisit.getIdEntrepriseSuivi();
             } else {
             idEntreprise = rapportVisit.getEntreprise().getId();
             }*/
            recreateModel();

            parametrageEntreprise = utilisateur.getEntreprise().getParametrageEntreprise();
            if (parametrageEntreprise.getTypePlanificationVisite() == 1) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../rapportVisit/ListVisiteSecteur.xhtml");
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../rapportVisit/List.xhtml");
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        itemsPlanification = null;
        errorMsg = false;
    }

    public List<PlanificationVisite> getItemsPlanification() {
        try {
            if (itemsPlanification == null) {
                itemsPlanification = ejbFacadePlanification.findAll("");
            }
            return itemsPlanification;
        } catch (Exception e) {
            System.out.println("Erreur- RapportVisitController - getItemsPlanification: " + e.getMessage());
            return null;
        }
    }

    public List<RapportVisit> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- RapportVisitController - getItems: " + e.getMessage());
            return null;
        }
    }

    public RapportVisit getSelected() {
        return selected;
    }

    public void setSelected(RapportVisit selected) {
        this.selected = selected;
    }

    public RapportVisit getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(RapportVisit selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private RapportVisitFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selectedPlanification = null;
        selected = null;
        if (parametrageEntreprise.getTypePlanificationVisite() == 1) {
            return "ListVisiteSecteur";
        } else {
            return "List";
        }

    }

    public String prepareView() {
        if (selected != null) {
            return "View";
        }
        if (parametrageEntreprise.getTypePlanificationVisite() == 1) {
            return "ListVisiteSecteur";
        } else {
            return "List";
        }
    }

    public String prepareViewVisiteSecteur() {
        if (selectedPlanification != null) {

            timeLineModel = new TimelineModel();
            dateStartTimeline = new Date();
            if (selectedPlanification.getListRapportVisits() != null && !selectedPlanification.getListRapportVisits().isEmpty()) {
                selectedPlanification.getListRapportVisits().forEach((rapportEvent) -> {

                    timeLineModel.add(new TimelineEvent(rapportEvent.getLibelleClient(), rapportEvent.getDateCreation()));
                    if(rapportEvent.getDateCreation().before(dateStartTimeline))
                    dateStartTimeline = TraitementDate.debutJournee( TraitementDate.moinsJour(rapportEvent.getDateCreation()));
                });
            }
            return "ViewVisiteSecteur";
        }
        return "ListVisiteSecteur";
    }

    public String prepareCreate() {
        selected = new RapportVisit();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            // errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {
                // selected.setSupprimer(false);
                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- RapportVisitController - create: " + e.getMessage());
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
        if (parametrageEntreprise.getTypePlanificationVisite() == 1) {
            return "ListVisiteSecteur";
        } else {
            return "List";
        }
    }

    public String update() {
        try {
            // errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());

            if (errorMsg == false) {

                //  selected.setSupprimer(false);
                getFacade().edit(selected);
                return prepareList();
                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                //  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- RapportVisitController - update: " + e.getMessage());
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
            System.out.println("Erreur- RapportVisitController - performDestroy: " + e.getMessage());
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
//            getArticleVisite();
//            getPatrimoineByPvt();
            return event.getNewStep();

        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public PlanificationVisite getSelectedPlanification() {
        return selectedPlanification;
    }

    public void setSelectedPlanification(PlanificationVisite selectedPlanification) {
        this.selectedPlanification = selectedPlanification;
    }

    public TimelineModel getTimeLineModel() {
        return timeLineModel;
    }

    public void setTimeLineModel(TimelineModel timeLineModel) {
        this.timeLineModel = timeLineModel;
    }

    public Date getDateStartTimeline() {
        return dateStartTimeline;
    }

    public void setDateStartTimeline(Date dateStartTimeline) {
        this.dateStartTimeline = dateStartTimeline;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public RapportVisit getRapportVisit(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = RapportVisit.class)
    public static class RapportVisitControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RapportVisitController controller = (RapportVisitController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "rapportVisitController");
            return controller.getRapportVisit(getKey(value));
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
            if (object instanceof RapportVisit) {
                RapportVisit o = (RapportVisit) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + RapportVisit.class.getName());
            }
        }

    }

}
