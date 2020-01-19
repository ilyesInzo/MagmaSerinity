package com.magma.controller;

import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.AvoirVente;
import com.magma.entity.Facture;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.Encaissement;
import com.magma.entity.TypeEncaissementVente;
import com.magma.entity.Utilisateur;
import com.magma.session.AvoirVenteFacadeLocal;
import com.magma.session.FactureFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.EncaissementFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean(name= "encaissementController")
@SessionScoped
public class EncaissementController implements Serializable {

    private Encaissement selected;
    private Encaissement selectedSingle;
    private List<Encaissement> items = null;
    @EJB
    private EncaissementFacadeLocal ejbFacade;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    @EJB
    private FactureFacadeLocal ejbFacadeFacture;
    @EJB
    private AvoirVenteFacadeLocal ejbFacadeAvoirVente;
    private boolean errorMsg;
    private Long idTemp;
    private Encaissement encaissement;
    private long idEntreprise = 0;
    private BigDecimal resteaPayer = BigDecimal.ZERO;
    private CategorieClient categorieClient;
    private List<Client> listClient = null;
    private Client client;
    private List<Facture> facturesNonPaye = new ArrayList();
    private Date dateJour = new Date();
    private Utilisateur utilisateur;
    private List<AvoirVente> listAvoirFactures = null;

    private TypeEncaissementVente typeEncaissementVente;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();

    public EncaissementController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (encaissement.getIdEntrepriseSuivi() != null && encaissement.getIdEntrepriseSuivi() != 0) {
                idEntreprise = encaissement.getIdEntrepriseSuivi();
            } else {
                idEntreprise = encaissement.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GEncaissement", "MVente", null,utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GEncaissement", utilisateur);
            /*if (encaissement.getIdEntrepriseSuivi() != null && encaissement.getIdEntrepriseSuivi() != 0) {
                idEntreprise = encaissement.getIdEntrepriseSuivi();
            } else {
                idEntreprise = encaissement.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../encaissement/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Encaissement> getItems() {
        try {
            if (items == null) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.Enc_DateEncaissement between '" + debut + "' and '" + fin + "' and o.Enc_Supprimer= 0 ";
                items = getFacade().findAllNative(clause + " order by o.Enc_DateEncaissement desc");

            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- EncaissementController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Encaissement getSelected() {
        return selected;
    }

    public void setSelected(Encaissement selected) {
        this.selected = selected;
    }

    public Encaissement getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Encaissement selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private EncaissementFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        client = new Client();
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            client = new Client();
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new Encaissement();
        errorMsg = false;
        facturesNonPaye = new ArrayList<>();
        client = new Client();
        categorieClient = new CategorieClient();
        dateJour = new Date();
        return "Create";
    }

    public String create() {

        try {
            if (selected.getMontant().compareTo(BigDecimal.ZERO) == 1 && selected.getMontant().compareTo(selected.getFacture().getReste()) != 1) {

                if ((!selected.getTypeEncaissementVente().isAvoir() || (selected.getTypeEncaissementVente().isAvoir() && selected.getMontant().compareTo(selected.getAvoir().getReste()) != 1))) {
                    selected.setDateEncaissement(new Date());
                    selected.setNumero(selected.getFacture().getNumero());
                    if (client != null) {
                        selected.setIdClient(client.getId());
                        selected.setLibelleClient(client.getLibelle());
                        //selected.setCodeClient(client.getCode());
                    }

                    if (selected.getBanque() != null) {
                        selected.setIdBanque(selected.getBanque().getId());
                        selected.setLibelleBanque(selected.getBanque().getLibelle());
                    }

                    if (selected.getTicket() != null) {
                        selected.setIdTicket(selected.getTicket().getId());
                        selected.setLibelleTicket(selected.getTicket().getLibelle());
                    }

                    /*if (vendeur != null) {
                    selected.setIdVendeur(vendeur.getId());
                    selected.setCodeVendeur(vendeur.getCode());
                    selected.setCodeCommercialVendeur(vendeur.getCodeCommercial());
                    selected.setVendeur(vendeur.getNomPrenom());
                } else {
                    selected.setIdVendeur(utilisateur.getId());
                    selected.setCodeVendeur(utilisateur.getCode());
                    selected.setVendeur(utilisateur.getNomPrenom());
                }*/
                    selected.setSupprimer(false);

                    /*if (selected.getTypeEncaissementVente().isTauxRetenu()) {
                    RetenuSourceVente retenuSourceVente = new RetenuSourceVente();
                    retenuSourceVente.setDateCreation(new Date());
                    retenuSourceVente.setCodeClient(selected.getCodeClient());
                    retenuSourceVente.setIdClient(selected.getIdClient());
                    retenuSourceVente.setLibelleClient(selected.getLibelleClient());
                    retenuSourceVente.setDateFacture(new Date());
                    retenuSourceVente.setIdEntreprise(selected.getIdEntreprise());
                    retenuSourceVente.setMontant(selected.getMontant());
                    retenuSourceVente.setNumeroFacture(selected.getFacture().getNumero());
                    retenuSourceVente.setOrigine(0);
                    ejbFacadeRetenuSourceVente.create(retenuSourceVente);

                }*/

 /*selected.setIdDomaine(selected.getDomaine().getId());
                selected.setLibelleDomaine(selected.getDomaine().getLibelle());
                selected.setCodeDomaine(selected.getDomaine().getCode());
                selected.setIdEntreprise(idEntreprise);*/
                    getFacade().create(selected);

                    if (selected.getFacture() != null) {

                        /* for (Encaissement item : selected.getFacture().getListeEncaissements()) {
                     if (item.isSupprimer() == false) {
                     montantPaye = montantPaye.add(item.getMontant());
                     }
                     }*/
                        if (selected.getTypeEncaissementVente().isAvoir()) {

                            selected.getAvoir().setReste(selected.getAvoir().getReste().subtract(selected.getMontant()));
                            ejbFacadeAvoirVente.edit(selected.getAvoir());
                        }

                        resteaPayer = selected.getFacture().getReste().subtract(selected.getMontant());
                        selected.getFacture().setReste(resteaPayer);
                        if (resteaPayer.compareTo(BigDecimal.ZERO) == 0) {
                            selected.getFacture().setEtat(1);
                        } else {
                            selected.getFacture().setEtat(3);
                        }
                        ejbFacadeFacture.edit(selected.getFacture());
                        System.out.println("succes de soustraction du reste");
                    }
                    return prepareList();

                } else {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("MontantAvoirDepasser")));
                    return null;
                }

            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("MontantSuperieurReste")));
                return null;
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EncaissementController - create: " + e.getMessage());
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
            facturesNonPaye = new ArrayList<>();
            idTemp = selected.getId();
            return "Edit";
        }
        return "List";
    }

    public String update() {
        BigDecimal montantPaye = BigDecimal.ZERO;
        try {
            selected.setDateEncaissement(new Date());
            getFacade().edit(selected);

            if (selected.getFacture() != null) {

                for (Encaissement item : selected.getFacture().getListeEncaissements()) {
                    if (item.isSupprimer() == false) {
                        montantPaye = montantPaye.add(item.getMontant());
                    }
                }
                resteaPayer = selected.getFacture().getMontantHT().subtract(montantPaye);
                selected.getFacture().setReste(resteaPayer);

                /*selected.setIdDomaine(selected.getDomaine().getId());
                selected.setLibelleDomaine(selected.getDomaine().getLibelle());
                selected.setCodeDomaine(selected.getDomaine().getCode());
                selected.setIdEntreprise(idEntreprise);
                selected.setLibelleEntreprise(selected.getDomaine().getEntreprise().getLibelle());
                selected.setCodeEntreprise(selected.getDomaine().getEntreprise().getCode());*/
                selected.getFacture().setEtat(3);
                ejbFacadeFacture.edit(selected.getFacture());
            }
            return prepareList();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EncaissementController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<EncaissementPVT> temps = ejbFacadeEncaissementPVT.findAll("where o.idEncaissement = " + selectedSingle.getId() + " ");
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

        selectedSingle.setSupprimer(true);

        resteaPayer = selectedSingle.getFacture().getReste().add(selectedSingle.getMontant());

        if (resteaPayer.compareTo(selectedSingle.getFacture().getMontantHT()) == 0) {

            selectedSingle.getFacture().setEtat(0);

        }
        selectedSingle.getFacture().setReste(resteaPayer);
        ejbFacadeFacture.edit(selectedSingle.getFacture());

        if (selectedSingle.getTypeEncaissementVente().isAvoir()) {
            //selectedSingle.getAvoir().setReste(selectedSingle.getAvoir().getReste().add(selectedSingle.getMontant()));
            ejbFacadeAvoirVente.editRestAvoir(selectedSingle.getIdAvoir(),selectedSingle.getMontant());
        }
        getFacade().edit(selectedSingle);

    }

    public void initPaiement() {
        client = new Client();
        facturesNonPaye = new ArrayList<>();
        selected.setFacture(null);
        selected.setTypeEncaissementVente(null);
        selected.setMontant(BigDecimal.ZERO);
    }

    public void changeFacture() {
        facturesNonPaye = new ArrayList<>();
        listAvoirFactures = new ArrayList<>();
        if (client != null) {
            facturesNonPaye = ejbFacadeFacture.findAllNative("where  o.Fct_Etat not in (1,2) and o.Cli_Id= " + client.getId() + " ");
            listAvoirFactures = ejbFacadeAvoirVente.findAllNative("where  o.AVnt_Reste > 0 and o.Cli_Id= " + client.getId() + " ");
        }

    }

    public void changeTicket() {
        if (selected.getTicket() != null) {
            selected.setDeductionPourcentageTicket(selected.getTicket().getDeductionPourcentage());
        } else {
            selected.setDeductionPourcentageTicket(BigDecimal.ZERO);
        }
    }

    public void changeMontantTicketResto() {
        if (selected.getDeductionPourcentageTicket() == null) {
            selected.setDeductionPourcentageTicket(BigDecimal.ZERO);
        }

        if (selected.getValeurUnitaireTicket() != null && selected.getNbrTicket() != 0 && selected.getDeductionPourcentageTicket() != null) {
            BigDecimal deduction = (selected.getValeurUnitaireTicket().multiply(selected.getDeductionPourcentageTicket())).divide(BigDecimal.valueOf(100));
            BigDecimal newValeur = (selected.getValeurUnitaireTicket()).subtract(deduction);
            selected.setMontant(newValeur.multiply(BigDecimal.valueOf(selected.getNbrTicket())));
            //calculReste();
        }

    }

    public void changeMontantAvoir() {

        if (selected.getAvoir() != null && selected.getFacture() != null) {

            if (selected.getFacture().getReste().compareTo(selected.getAvoir().getReste()) == 1) {
                selected.setMontant(selected.getAvoir().getReste());
            } else {
                selected.setMontant(selected.getFacture().getReste());
            }

        }

    }

    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                items = getFacade().searchAllNative(debut, fin, typeEncaissementVente != null ? typeEncaissementVente.getId() : null, client != null ? client.getId() : null);

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

    public CategorieClient getCategorieClient() {
        return categorieClient;
    }

    public void setCategorieClient(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDateJour() {
        return dateJour;
    }

    public void setDateJour(Date dateJour) {
        this.dateJour = dateJour;
    }

    public TypeEncaissementVente getTypeEncaissementVente() {
        return typeEncaissementVente;
    }

    public void setTypeEncaissementVente(TypeEncaissementVente typeEncaissementVente) {
        this.typeEncaissementVente = typeEncaissementVente;
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

    public SelectItem[] getItemsAvailableSelectOneNotPaid() {

        return JsfUtil.getSelectItems(facturesNonPaye, true);

    }

    public SelectItem[] getItemsAvailableSelectOneAvoir() {

        return JsfUtil.getSelectItems(listAvoirFactures, true);

    }

    public SelectItem[] getItemsAvailableSelectOneClient() {
        listClient = new ArrayList<>();
        if (categorieClient != null) {
            listClient = ejbFacadeClient.findAllNative(" where o.CCl_Id = " + categorieClient.getId());
        }
        return JsfUtil.getSelectItems(listClient, true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Encaissement getEncaissement(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Encaissement.class)
    public static class EncaissementControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EncaissementController controller = (EncaissementController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "encaissementController");
            return controller.getEncaissement(getKey(value));
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
            if (object instanceof Encaissement) {
                Encaissement o = (Encaissement) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Encaissement.class.getName());
            }
        }

    }

}
