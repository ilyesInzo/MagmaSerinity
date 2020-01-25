package com.magma.controller;

import com.magma.bibliotheque.TraitementDate;
import com.magma.entity.PlanificationVisite;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.Commercial;
import com.magma.entity.Utilisateur;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.PlanificationVisiteFacade;
import com.magma.session.PlanificationVisiteFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ManagedBean(name = "planificationVisiteController")
@SessionScoped
public class PlanificationVisiteController implements Serializable {

    private PlanificationVisite selected;
    private PlanificationVisite selectedSingle;
    private List<PlanificationVisite> items = null;
    private boolean errorMsg;
    private Long idTemp;
    private PlanificationVisite planificationVisite;

    private Utilisateur utilisateur;
    
    @EJB
    private PlanificationVisiteFacadeLocal ejbFacade;
    
    
    @EJB
    private ClientFacadeLocal ejbFacadeClient;

    private ScheduleModel lazyEventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private Date startDate = new Date();
    private Date endDate = new Date();
    private Date startAujourdhui = new Date();
    private Date endAujourdhui = new Date();
    private Date dateCalender = new Date();
    private Date demain = null;
    private Commercial commercial = null;

    private List<PlanificationVisite> listePlanificationVisites = new ArrayList<PlanificationVisite>();
    private Date dateJour = new Date();

    private String langue = "fr_FR";
    private String patternString = "dd/MM/yyyy HH:mm";
    private Date minDate = new Date();
    private Date dateTempDebut = new Date();
    private Date dateTempFin = new Date();
    private String optionAfficheCalendrier = "agendaWeek";
    private String conditionAffichageEventDialog = "hidden";
    private int typeClient;
    private Client client = new Client();
    private CategorieClient categorieClient;
    private List<Client> listClient = null;

    public PlanificationVisiteController() {
        items = null;
        errorMsg = false;
        demain = TraitementDate.dateDuString(TraitementDate.returnDate(TraitementDate.plusJour(new Date())) + " 00:00:00");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        if (langue.equals("en")) {
            patternString = "MM/dd/yyyy HH:mm";
        }
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GPlanificationVisite", "MCommercial", null,utilisateur);

            recreateModel();
            dateCalender = new Date();
            selected = new PlanificationVisite();
            lazyEventModel = new LazyScheduleModel() {
                @Override
                public void loadEvents(Date start, Date end) {
                    startDate = start;
                    endDate = end;
                    startAujourdhui = start;
                    endAujourdhui = end;
                    if (commercial != null && commercial.getId() != 0) {
                        items = ejbFacade.findAllNative(" where o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
                        event = new DefaultScheduleEvent();
                        if (items != null && !items.isEmpty()) {
                            for (PlanificationVisite item : items) {
                                DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                                if (item.getEtat() == 1) {
                                    evento.setStyleClass("visteVerte");
                                } else if (item.getEtat() == 2) {
                                    evento.setStyleClass("visteRouge");
                                } else {
                                    if (item.getDateDebut().getTime() < demain.getTime()) {
                                        evento.setStyleClass("visiteOrange");
                                    }
                                }
                                addEvent(evento);
                            }
                        }
                    }
                }
            };

            FacesContext.getCurrentInstance().getExternalContext().redirect("../planificationVisite/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }
    

    public void addEvent(ActionEvent actionEvent) {
        if (commercial != null && commercial.getId() != null) {
            if (getFacade().chefDeZoneNonDisponible(commercial.getId(), selected.getDateDebut(), selected.getDateFin()) == false) {
                    if (client != null && client.getId() != null) {
                    if (selected.getDateDebut() != null && selected.getDateFin() != null && selected.getDateDebut().getTime() < selected.getDateFin().getTime()) {
                       // selected.setUtilisateur(commercial);

                        selected.setIdCommercial(commercial.getId());
                        selected.setNom(commercial.getNom());
                        selected.setPrenom(commercial.getPrenom());
                        selected.setIdClient(client.getId());
                        selected.setLibelleClient(client.getLibelle());
                        selected.setEtat(0);
                        selected.setDateSynch(new Date().getTime());
                        selected.setSupprimer(false);
                        ejbFacade.create(selected);
                        lazyEventModel = new LazyScheduleModel() {

                            @Override
                            public void loadEvents(Date start, Date end) {
                                startDate = start;
                                endDate = end;
                                startAujourdhui = start;
                                endAujourdhui = end;
                                if (commercial != null && commercial.getId() != 0) {
                                    items = ejbFacade.findAllNative(" where o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
                                    event = new DefaultScheduleEvent();
                                    if (items != null && !items.isEmpty()) {
                                        for (PlanificationVisite item : items) {
                                            DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                                            if (item.getEtat() == 1) {
                                                evento.setStyleClass("visteVerte");
                                            } else if (item.getEtat() == 2) {
                                                evento.setStyleClass("visteRouge");
                                            } else {
                                                if (item.getDateDebut().getTime() < demain.getTime()) {
                                                    evento.setStyleClass("visiteOrange");
                                                }
                                            }
                                            addEvent(evento);
                                        }
                                    }
                                }
                            }
                        };

                        selected = new PlanificationVisite();
                        client = new Client();
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), ResourceBundle.getBundle("/Bundle").getString("ErreurDuree")));
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), ResourceBundle.getBundle("/Bundle").getString("SelectionnerClient")));
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                }

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), ResourceBundle.getBundle("/Bundle").getString("VousAvezUneAutrVisitePendantLaMemePeriode")));
//           
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), ResourceBundle.getBundle("/Bundle").getString("SelectionnerCommercial")));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        }

        client = null;
        typeClient = -1;
    }

    public void removeEvent(ActionEvent actionEvent) {
        selected.setSupprimer(true);
        ejbFacade.edit(selected);
        lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                startDate = start;
                endDate = end;
                startAujourdhui = start;
                endAujourdhui = end;
                if (commercial != null && commercial.getId() != 0) {
                    items = ejbFacade.findAllNative(" where o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
                    event = new DefaultScheduleEvent();
                    if (items != null && !items.isEmpty()) {
                        for (PlanificationVisite item : items) {
                            DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                            if (item.getEtat() == 1) {
                                evento.setStyleClass("visteVerte");
                            } else if (item.getEtat() == 2) {
                                evento.setStyleClass("visteRouge");
                            } else {
                                if (item.getDateDebut().getTime() < demain.getTime()) {
                                    evento.setStyleClass("visiteOrange");
                                }
                            }
                            addEvent(evento);
                        }
                    }
                }
            }
        };
        selected = new PlanificationVisite();
        client = new Client();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
        selected = (PlanificationVisite) event.getData();
        if (selected.getDateDebut().getTime() >= demain.getTime()) {
            conditionAffichageEventDialog = "visible";
        } else {
            conditionAffichageEventDialog = "hidden";
        }
        dateTempDebut = selected.getDateDebut();
        dateTempFin = selected.getDateFin();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        Date date = (Date) selectEvent.getObject();
        if (date.getTime() >= demain.getTime()) {
            if (optionAfficheCalendrier.equals("month")) {
                date = TraitementDate.dateDuString(TraitementDate.returnDate(date) + " 07:00:00");
            }
            selected = new PlanificationVisite();
            selected.setDateDebut(TraitementDate.moinsPlusHeurs(date, -1));
            selected.setDateFin(date);
            client = new Client();
            conditionAffichageEventDialog = "visible";
        } else {
            conditionAffichageEventDialog = "hidden";
        }
    }

    public Date getDemain() {
        return demain;
    }

    public void setDemain(Date demain) {
        this.demain = demain;
    }

    public void onEventMove(ScheduleEntryMoveEvent eventSelect) {
        //items = ejbFacade.findAllNative(" where o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
        final PlanificationVisite planificationVisite = (PlanificationVisite) eventSelect.getScheduleEvent().getData();
        lazyEventModel.clear();
        lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                startDate = start;
                endDate = end;
                startAujourdhui = start;
                endAujourdhui = end;
                if (commercial != null && commercial.getId() != 0) {
                    items = ejbFacade.findAllNative(" where  o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
                    event = new DefaultScheduleEvent();
                    if (items != null && !items.isEmpty()) {
                        for (PlanificationVisite item : items) {
                            if (Objects.equals(item.getId(), planificationVisite.getId())) {
                                if (item.getDateDebut().getTime() <= demain.getTime()) {
                                    DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                                    if (item.getEtat() == 1) {
                                        evento.setStyleClass("visteVerte");
                                    } else if (item.getEtat() == 2) {
                                        evento.setStyleClass("visteRouge");
                                    } else {
                                        if (item.getDateDebut().getTime() < demain.getTime()) {
                                            evento.setStyleClass("visiteOrange");
                                        }
                                    }
                                    addEvent(evento);
                                } else {
                                    if (planificationVisite.getDateDebut().getTime() < demain.getTime()) {
                                        DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                                        if (item.getEtat() == 1) {
                                            evento.setStyleClass("visteVerte");
                                        } else if (item.getEtat() == 2) {
                                            evento.setStyleClass("visteRouge");
                                        } else {
                                            if (item.getDateDebut().getTime() < demain.getTime()) {
                                                evento.setStyleClass("visiteOrange");
                                            }
                                        }
                                        addEvent(evento);
                                    } else {
                                        DefaultScheduleEvent evento = new DefaultScheduleEvent(planificationVisite.toString(), planificationVisite.getDateDebut(), planificationVisite.getDateFin(), planificationVisite);
                                        if (planificationVisite.getEtat() == 1) {
                                            evento.setStyleClass("visteVerte");
                                        } else if (planificationVisite.getEtat() == 2) {
                                            evento.setStyleClass("visteRouge");
                                        } else {
                                            if (planificationVisite.getDateDebut().getTime() < demain.getTime()) {
                                                evento.setStyleClass("visiteOrange");
                                            }
                                        }
                                        addEvent(evento);
                                        planificationVisite.setDateSynch(new Date().getTime());
                                        planificationVisite.setSupprimer(false);
                                        ejbFacade.edit(planificationVisite);
                                    }
                                }
                            } else {
                                DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                                if (item.getEtat() == 1) {
                                    evento.setStyleClass("visteVerte");
                                } else if (item.getEtat() == 2) {
                                    evento.setStyleClass("visteRouge");
                                } else {
                                    if (item.getDateDebut().getTime() < demain.getTime()) {
                                        evento.setStyleClass("visiteOrange");
                                    }
                                }
                                addEvent(evento);
                            }

                        }

                    }
                }
            }
        };
        selected = new PlanificationVisite();
        client = new Client();
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        addMessage(message);
        System.out.println("onEventResize...." + event.getDayDelta());
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public String gestionDateActuelle() {
        dateCalender = new Date();
        startDate = startAujourdhui;
        endDate = endAujourdhui;
        selected = new PlanificationVisite();
        client = new Client();
        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {
                System.out.println(TraitementDate.returnDateHeure(start) + " ## " + TraitementDate.returnDateHeure(end) + " ## " + TraitementDate.returnDateHeure(dateCalender));
                startDate = start;
                endDate = end;
                startAujourdhui = start;
                endAujourdhui = end;
                if (commercial != null && commercial.getId() != 0) {
                    items = ejbFacade.findAllNative(" where o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
                    event = new DefaultScheduleEvent();
                    if (items != null && !items.isEmpty()) {
                        for (PlanificationVisite item : items) {
                            DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                            if (item.getEtat() == 1) {
                                evento.setStyleClass("visteVerte");
                            } else if (item.getEtat() == 2) {
                                evento.setStyleClass("visteRouge");
                            } else {
                                if (item.getDateDebut().getTime() < demain.getTime()) {
                                    evento.setStyleClass("visiteOrange");
                                }
                            }
                            addEvent(evento);
                        }
                    }
                }
            }
        };
        return "List";
    }

    public String gestionDatePrecedent() {
        switch (optionAfficheCalendrier) {
            case "agendaDay":
                dateCalender = TraitementDate.moinsJour(dateCalender);
                startDate = TraitementDate.moinsJour(startDate);
                endDate = TraitementDate.moinsJour(endDate);
                break;
            case "agendaWeek":
                dateCalender = TraitementDate.moinsSemaine(dateCalender);
                startDate = TraitementDate.moinsSemaine(startDate);
                endDate = TraitementDate.moinsSemaine(endDate);
                break;
            case "month":
                dateCalender = TraitementDate.moinsMois(dateCalender);
                startDate = TraitementDate.moinsMois(startDate);
                endDate = TraitementDate.moinsMois(endDate);
                break;
            default:
                dateCalender = TraitementDate.moinsJour(dateCalender);
                startDate = TraitementDate.moinsJour(startDate);
                endDate = TraitementDate.moinsJour(endDate);
                break;
        }

        selected = new PlanificationVisite();
        client = new Client();
        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {
                startDate = start;
                endDate = end;
                startAujourdhui = start;
                endAujourdhui = end;
                if (commercial != null && commercial.getId() != 0) {
                    items = ejbFacade.findAllNative(" where o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
                    event = new DefaultScheduleEvent();
                    if (items != null && !items.isEmpty()) {
                        for (PlanificationVisite item : items) {
                            DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                            if (item.getEtat() == 1) {
                                evento.setStyleClass("visteVerte");
                            } else if (item.getEtat() == 2) {
                                evento.setStyleClass("visteRouge");
                            } else {
                                if (item.getDateDebut().getTime() < demain.getTime()) {
                                    evento.setStyleClass("visiteOrange");
                                }
                            }
                            addEvent(evento);
                        }
                    }
                }
            }
        };
        return "List";
    }

    public String gestionDateSuivant() {
        switch (optionAfficheCalendrier) {
            case "agendaDay":
                dateCalender = TraitementDate.plusJour(dateCalender);
                startDate = TraitementDate.plusJour(startDate);
                endDate = TraitementDate.plusJour(endDate);
                break;
            case "agendaWeek":
                dateCalender = TraitementDate.plusSemaine(dateCalender);
                startDate = TraitementDate.plusSemaine(startDate);
                endDate = TraitementDate.plusSemaine(endDate);
                break;
            case "month":
                dateCalender = TraitementDate.plusMois(dateCalender);
                startDate = TraitementDate.plusMois(startDate);
                endDate = TraitementDate.plusMois(endDate);
                break;
            default:
                dateCalender = TraitementDate.plusSemaine(dateCalender);
                startDate = TraitementDate.plusSemaine(startDate);
                endDate = TraitementDate.plusSemaine(endDate);
                break;
        }

        selected = new PlanificationVisite();
        client = new Client();
        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date start, Date end) {
                System.out.println(TraitementDate.returnDateHeure(start) + " ## " + TraitementDate.returnDateHeure(end) + " ## " + TraitementDate.returnDateHeure(dateCalender));
                startDate = start;
                endDate = end;
                startAujourdhui = start;
                endAujourdhui = end;
                if (commercial != null && commercial.getId() != 0) {
                    items = ejbFacade.findAllNative(" where o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
                    event = new DefaultScheduleEvent();
                    if (items != null && !items.isEmpty()) {
                        for (PlanificationVisite item : items) {
                            DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                            if (item.getEtat() == 1) {
                                evento.setStyleClass("visteVerte");
                            } else if (item.getEtat() == 2) {
                                evento.setStyleClass("visteRouge");
                            } else {
                                if (item.getDateDebut().getTime() < demain.getTime()) {
                                    evento.setStyleClass("visiteOrange");
                                }
                            }
                            addEvent(evento);
                        }
                    }
                }
            }
        };
        return "List";
    }




    public void changedCommercial() {
        listClient = null;
        lazyEventModel = new LazyScheduleModel() {
            @Override
            public void loadEvents(Date start, Date end) {
                startDate = start;
                endDate = end;
                startAujourdhui = start;
                endAujourdhui = end;
                if (commercial != null && commercial.getId() != 0) {
                    items = ejbFacade.findAllNative(" where  o.PVi_Supprimer = 0 and o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");

                    event = new DefaultScheduleEvent();
                    if (items != null && !items.isEmpty()) {
                        for (PlanificationVisite item : items) {
                            DefaultScheduleEvent evento = new DefaultScheduleEvent(item.toString(), item.getDateDebut(), item.getDateFin(), item);
                            if (item.getEtat() == 1) {
                                evento.setStyleClass("visteVerte");
                            } else if (item.getEtat() == 2) {
                                evento.setStyleClass("visteRouge");
                            } else {
                                if (item.getDateDebut().getTime() < demain.getTime()) {
                                    evento.setStyleClass("visiteOrange");
                                }
                            }
                            addEvent(evento);
                        }
                    }
                }
            }
        };
    }


    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    private PlanificationVisiteFacadeLocal getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        selectedSingle = null;
        recreateModel();
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            return "View";
        }
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectOneClientByCommercial() {

        if (listClient == null) {
            listClient = null;
            if (commercial != null && commercial.getId() != 0) {
//                if (categorieClient != null) {
//                    listClient = ejbFacadeClient.findAllNative(" where o.Com_Id = " + commercial.getId() + " and o.CCl_Id = " + categorieClient.getId());
//                } else {
                listClient = ejbFacadeClient.findAllNative("where o.Com_Id = " + commercial.getId());
//                }

            } else {
                listClient = new ArrayList<>();
            }
        }
        return JsfUtil.getSelectItems(listClient, true);

    }

    public List<PlanificationVisite> getItems() {
        if (items == null) {
            if (commercial != null && commercial.getId() != 0) {
                items = ejbFacade.findAllNative(" where  o.Com_Id = " + commercial.getId() + " and o.PVi_DateDebut between '" + TraitementDate.returnDateHeure(startDate) + "' and '" + TraitementDate.returnDateHeure(endDate) + "' ");
            }
        }
        return items;
    }

    
    public String getOptionAfficheCalendrier() {
        return optionAfficheCalendrier;
    }

    public void setOptionAfficheCalendrier(String optionAfficheCalendrier) {
        this.optionAfficheCalendrier = optionAfficheCalendrier;
    }

    public Date getDateJour() {
        return dateJour;
    }

    public void setDateJour(Date dateJour) {
        this.dateJour = dateJour;
    }

    public Date getDateTempDebut() {
        return dateTempDebut;
    }

    public int getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(int typeClient) {
        this.typeClient = typeClient;
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

    public void setDateTempDebut(Date dateTempDebut) {
        this.dateTempDebut = dateTempDebut;
    }

    public Date getDateTempFin() {
        return dateTempFin;
    }

    public void setDateTempFin(Date dateTempFin) {
        this.dateTempFin = dateTempFin;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getPatternString() {
        return patternString;
    }

    public void setPatternString(String patternString) {
        this.patternString = patternString;
    }

    public Date getEndAujourdhui() {
        return endAujourdhui;
    }

    public void setEndAujourdhui(Date endAujourdhui) {
        this.endAujourdhui = endAujourdhui;
    }

    public Date getStartAujourdhui() {
        return startAujourdhui;
    }

    public void setStartAujourdhui(Date startAujourdhui) {
        this.startAujourdhui = startAujourdhui;
    }

    public PlanificationVisite getSelected() {
        return selected;
    }

    public void setSelected(PlanificationVisite selected) {
        this.selected = selected;
    }

    public PlanificationVisite getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(PlanificationVisite selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    public Commercial getCommercial() {
        return commercial;
    }

    public void setCommercial(Commercial commercial) {
        this.commercial = commercial;
    }

    public Date getDateCalender() {
        return dateCalender;
    }

    public void setDateCalender(Date dateCalender) {
        this.dateCalender = dateCalender;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }


    public String getConditionAffichageEventDialog() {
        return conditionAffichageEventDialog;
    }

    public void setConditionAffichageEventDialog(String conditionAffichageEventDialog) {
        this.conditionAffichageEventDialog = conditionAffichageEventDialog;
    }
    
    private void recreateModel() {
        items = null;
        errorMsg = false;
        commercial = null;
        client = null;
        typeClient = -1;
    }
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = PlanificationVisite.class)
    public static class PlanificationVisiteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PlanificationVisiteController controller = (PlanificationVisiteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "planificationVisiteController");
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
            if (object instanceof PlanificationVisite) {
                PlanificationVisite o = (PlanificationVisite) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + PlanificationVisite.class.getName());
            }
        }

    }

}
