package com.magma.controller;

import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.BonLivraison;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.EncaissementBonLivraison;
import com.magma.entity.TypeEncaissementVente;
import com.magma.entity.Utilisateur;
import com.magma.session.BonLivraisonFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.EncaissementBonLivraisonFacadeLocal;
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

@ManagedBean(name= "encaissementBonLivraisonController")
@SessionScoped
public class EncaissementBonLivraisonController implements Serializable {

    private EncaissementBonLivraison selected;
    private EncaissementBonLivraison selectedSingle;
    private List<EncaissementBonLivraison> items = null;
    @EJB
    private EncaissementBonLivraisonFacadeLocal ejbFacade;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    @EJB
    private BonLivraisonFacadeLocal ejbFacadeBonLivraison;
    private boolean errorMsg;
    private Long idTemp;
    private EncaissementBonLivraison encaissementBonLivraison;
    private long idEntreprise = 0;
    private BigDecimal resteaPayer = BigDecimal.ZERO;
    private CategorieClient categorieClient;
    private List<Client> listClient = null;
    private Client client;
    private List<BonLivraison> bonLivraisonsNonPaye = new ArrayList();
    private Date dateJour = new Date();
    private Utilisateur utilisateur;
    
    
    private TypeEncaissementVente typeEncaissementVente;
    private Date dateDebut = new Date();
    private Date dateFin = new Date();

    public EncaissementBonLivraisonController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (encaissementBonLivraison.getIdEntrepriseSuivi() != null && encaissementBonLivraison.getIdEntrepriseSuivi() != 0) {
                idEntreprise = encaissementBonLivraison.getIdEntrepriseSuivi();
            } else {
                idEntreprise = encaissementBonLivraison.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GEncaissementBonLivraison", "MVente", null,utilisateur);

           // MenuTemplate.menuFonctionnalitesModules("GEncaissementBonLivraison", utilisateur);
            /*if (encaissementBonLivraison.getIdEntrepriseSuivi() != null && encaissementBonLivraison.getIdEntrepriseSuivi() != 0) {
                idEntreprise = encaissementBonLivraison.getIdEntrepriseSuivi();
            } else {
                idEntreprise = encaissementBonLivraison.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../encaissementBonLivraison/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void menuModules() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("MParametrage", "");
        context.getExternalContext().getSessionMap().put("MProduit", "");
        context.getExternalContext().getSessionMap().put("MClient", "");
        context.getExternalContext().getSessionMap().put("MVente", "display: block");
    }

    private void menuFonctionnalites() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("GProfil", "");
        context.getExternalContext().getSessionMap().put("GUtilisateur", "");
        context.getExternalContext().getSessionMap().put("GGouvernorat", "");
        context.getExternalContext().getSessionMap().put("GDelegation", "");
        context.getExternalContext().getSessionMap().put("GBanque", "");
        context.getExternalContext().getSessionMap().put("GTicket", "");

        context.getExternalContext().getSessionMap().put("GTva", "");
        context.getExternalContext().getSessionMap().put("GCategorie", "");
        context.getExternalContext().getSessionMap().put("GArticle", "");

        context.getExternalContext().getSessionMap().put("GClient", "");
        context.getExternalContext().getSessionMap().put("GCategorieClient", "");

        context.getExternalContext().getSessionMap().put("GBonLivraison", "");
        context.getExternalContext().getSessionMap().put("GFacture", "");
        context.getExternalContext().getSessionMap().put("GPrefixFacture", "");
        context.getExternalContext().getSessionMap().put("GPrefixBonLivraison", "");
        context.getExternalContext().getSessionMap().put("GEncaissementBonLivraison", "active-menuitem");
        context.getExternalContext().getSessionMap().put("GEncaissement", "");
        context.getExternalContext().getSessionMap().put("GTypeEncaissementVente", "");
        context.getExternalContext().getSessionMap().put("GParametrageTaxe", "");
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<EncaissementBonLivraison> getItems() {
        try {
            if (items == null) {
                
                
                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.EBLiv_DateEncaissement between '" + debut + "' and '" + fin + "' and o.EBLiv_Supprimer= 0 ";
                items = getFacade().findAllNative(clause + " order by o.EBLiv_DateEncaissement desc");
                
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- EncaissementBonLivraisonController - getItems: " + e.getMessage());
            return null;
        }
    }

    public EncaissementBonLivraison getSelected() {
        return selected;
    }

    public void setSelected(EncaissementBonLivraison selected) {
        this.selected = selected;
    }

    public EncaissementBonLivraison getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(EncaissementBonLivraison selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private EncaissementBonLivraisonFacadeLocal getFacade() {
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
        selected = new EncaissementBonLivraison();
        errorMsg = false;
        bonLivraisonsNonPaye = new ArrayList<>();
        client = new Client();
        categorieClient = new CategorieClient();
        dateJour = new Date();
        return "Create";
    }

    public String create() {

        try {
            if (selected.getMontant().compareTo(BigDecimal.ZERO) == 1 && selected.getMontant().compareTo(selected.getBonLivraison().getReste()) != 1) {
                selected.setDateEncaissement(new Date());
                selected.setNumero(selected.getBonLivraison().getNumero());
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
                    retenuSourceVente.setNumeroBonLivraison(selected.getBonLivraison().getNumero());
                    retenuSourceVente.setOrigine(0);
                    ejbFacadeRetenuSourceVente.create(retenuSourceVente);

                }*/

 /*selected.setIdDomaine(selected.getDomaine().getId());
                selected.setLibelleDomaine(selected.getDomaine().getLibelle());
                selected.setCodeDomaine(selected.getDomaine().getCode());
                selected.setIdEntreprise(idEntreprise);*/
                getFacade().create(selected);

                if (selected.getBonLivraison() != null) {

                    /* for (EncaissementBonLivraison item : selected.getBonLivraison().getListeEncaissementBonLivraisons()) {
                     if (item.isSupprimer() == false) {
                     montantPaye = montantPaye.add(item.getMontant());
                     }
                     }*/
                    resteaPayer = selected.getBonLivraison().getReste().subtract(selected.getMontant());
                    selected.getBonLivraison().setReste(resteaPayer);
                    if (resteaPayer.compareTo(BigDecimal.ZERO) == 0) {
                        selected.getBonLivraison().setEtat(1);
                    } else {
                        selected.getBonLivraison().setEtat(3);
                    }
                    ejbFacadeBonLivraison.edit(selected.getBonLivraison());
                    System.out.println("succes de soustraction du reste");
                }
                return prepareList();
            } else {

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("MontantSuperieurReste")));
                return null;
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EncaissementBonLivraisonController - create: " + e.getMessage());
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
            bonLivraisonsNonPaye = new ArrayList<>();
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

            if (selected.getBonLivraison() != null) {

                for (EncaissementBonLivraison item : selected.getBonLivraison().getListEncaissementBonLivraisons()) {
                    if (item.isSupprimer() == false) {
                        montantPaye = montantPaye.add(item.getMontant());
                    }
                }
                resteaPayer = selected.getBonLivraison().getMontantHT().subtract(montantPaye);
                selected.getBonLivraison().setReste(resteaPayer);

                /*selected.setIdDomaine(selected.getDomaine().getId());
                selected.setLibelleDomaine(selected.getDomaine().getLibelle());
                selected.setCodeDomaine(selected.getDomaine().getCode());
                selected.setIdEntreprise(idEntreprise);
                selected.setLibelleEntreprise(selected.getDomaine().getEntreprise().getLibelle());
                selected.setCodeEntreprise(selected.getDomaine().getEntreprise().getCode());*/
                selected.getBonLivraison().setEtat(3);
                ejbFacadeBonLivraison.edit(selected.getBonLivraison());
            }
            return prepareList();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EncaissementBonLivraisonController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<EncaissementBonLivraisonPVT> temps = ejbFacadeEncaissementBonLivraisonPVT.findAll("where o.idEncaissementBonLivraison = " + selectedSingle.getId() + " ");
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

        resteaPayer = selectedSingle.getBonLivraison().getReste().add(selectedSingle.getMontant());

        if (resteaPayer.compareTo(selectedSingle.getBonLivraison().getMontantHT()) == 0) {

            selectedSingle.getBonLivraison().setEtat(0);

        }
        selectedSingle.getBonLivraison().setReste(resteaPayer);
        ejbFacadeBonLivraison.edit(selectedSingle.getBonLivraison());

        getFacade().edit(selectedSingle);

    }

    public void initPaiement() {
        client = new Client();
        bonLivraisonsNonPaye = new ArrayList<>();
        selected.setBonLivraison(null);
        selected.setTypeEncaissementVente(null);
        selected.setMontant(BigDecimal.ZERO);
    }

    public void changeBonLivraison() {
        System.out.println("changeBonLivraison");
        bonLivraisonsNonPaye = new ArrayList<>();
        BigDecimal montantPaye = BigDecimal.ZERO;
        if (client != null) {
            bonLivraisonsNonPaye = ejbFacadeBonLivraison.findAllNative("where o.BLiv_idFacture is null and o.BLiv_Etat not in (1,2) and o.Cli_Id= " + client.getId() + " ");

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
    
    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";


                items = getFacade().searchAllNative(debut, fin,  typeEncaissementVente != null ? typeEncaissementVente.getId() : null, client != null ? client.getId() : null);

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

        return JsfUtil.getSelectItems(bonLivraisonsNonPaye, true);

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

    public EncaissementBonLivraison getEncaissementBonLivraison(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = EncaissementBonLivraison.class)
    public static class EncaissementBonLivraisonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EncaissementBonLivraisonController controller = (EncaissementBonLivraisonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "encaissementBonLivraisonController");
            return controller.getEncaissementBonLivraison(getKey(value));
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
            if (object instanceof EncaissementBonLivraison) {
                EncaissementBonLivraison o = (EncaissementBonLivraison) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EncaissementBonLivraison.class.getName());
            }
        }

    }

}
