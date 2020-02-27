package com.magma.controller;

import com.itextpdf.text.DocumentException;
import com.magma.bibliotheque.FonctionsMathematiques;
import com.magma.bibliotheque.FonctionsString;
import com.magma.bibliotheque.GenerationPdf;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Article;
import com.magma.entity.AvoirVente;
import com.magma.entity.BonCommandeVente;
import com.magma.entity.BonLivraison;
import com.magma.entity.Facture;
import com.magma.entity.Categorie;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.Devis;
import com.magma.entity.Encaissement;
import com.magma.entity.EncaissementBonLivraison;
import com.magma.entity.LigneAvoirVente;
import com.magma.entity.LigneBonCommandeVente;
import com.magma.entity.LigneBonLivraison;
import com.magma.entity.LigneDevis;
import com.magma.entity.LigneFacture;
import com.magma.entity.LigneRetour;
import com.magma.entity.ParametrageTaxe;
import com.magma.entity.PrefixAvoirVente;
import com.magma.entity.PrefixFacture;
import com.magma.entity.Retour;
import com.magma.entity.TaxesBonCommandeVente;
import com.magma.entity.TaxesDevis;
import com.magma.entity.TaxesFacture;
import com.magma.entity.Utilisateur;
import com.magma.schedule.TraitementRetour;
import com.magma.session.ArticleFacadeLocal;
import com.magma.session.AvoirVenteFacadeLocal;
import com.magma.session.BonCommandeVenteFacadeLocal;
import com.magma.session.BonLivraisonFacadeLocal;
import com.magma.session.FactureFacadeLocal;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.DevisFacadeLocal;
import com.magma.session.EncaissementFacadeLocal;
import com.magma.session.LigneAvoirVenteFacadeLocal;
import com.magma.session.LigneBonCommandeVenteFacadeLocal;
import com.magma.session.LigneBonLivraisonFacadeLocal;
import com.magma.session.LigneDevisFacadeLocal;
import com.magma.session.LigneFactureFacadeLocal;
import com.magma.session.LigneRetourFacadeLocal;
import com.magma.session.ParametrageTaxeFacadeLocal;
import com.magma.session.PrefixAvoirVenteFacadeLocal;
import com.magma.session.PrefixBonLivraisonFacadeLocal;
import com.magma.session.PrefixFactureFacadeLocal;
import com.magma.session.RetourFacadeLocal;
import com.magma.session.TaxesFactureFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name= "factureController")
@SessionScoped
public class FactureController implements Serializable {

    @EJB
    private TraitementRetour traitementRetour;

    private Facture selected;
    private Facture selectedSingle;
    private List<Facture> items = null;

    @EJB
    private FactureFacadeLocal ejbFacade;
    @EJB
    private PrefixFactureFacadeLocal ejbFacadePrefixFacture;
    @EJB
    private LigneFactureFacadeLocal ejbFacadeLigneFacture;
    @EJB
    private CategorieClientFacadeLocal ejbFacadeCatgorieClient;
    @EJB
    private DevisFacadeLocal ejbFacadeDevis;
    @EJB
    private BonLivraisonFacadeLocal ejbFacadeBonLivraison;
    @EJB
    private LigneBonLivraisonFacadeLocal ejbFacadeLigneBonLivraison;
    @EJB
    private PrefixBonLivraisonFacadeLocal ejbFacadePrefixBonLivraison;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    @EJB
    private ArticleFacadeLocal ejbFacadeArticle;
    @EJB
    private ParametrageTaxeFacadeLocal ejbFacadeParametrageTaxe;
    @EJB
    private TaxesFactureFacadeLocal ejbFacadeTaxeFacture;
    @EJB
    private EncaissementFacadeLocal ejbFacadeEncaissement;
    @EJB
    private LigneDevisFacadeLocal ejbFacadeLigneDevis;
    @EJB
    private AvoirVenteFacadeLocal ejbFacadeAvoirVente;
    @EJB
    private PrefixAvoirVenteFacadeLocal ejbFacadePrefixAvoirVente;
    @EJB
    private LigneAvoirVenteFacadeLocal ejbFacadeLigneAvoirVente;
    @EJB
    private RetourFacadeLocal ejbFacadeRetour;
    @EJB
    private LigneRetourFacadeLocal ejbFacadeLigneRetour;
    @EJB
    private LigneBonCommandeVenteFacadeLocal ejbFacadeLigneBonCommande;
    @EJB
    private BonCommandeVenteFacadeLocal ejbFacadeBonCommande;
    private List<BonLivraison> listSelectedBonLivraison = null;
    private List<BonLivraison> listBonLivraison = null;
    private List<Client> listClient = null;

    private Categorie categorie;
    private CategorieClient categorieClient = null;
    private boolean errorMsg;
    private Long idTemp;
    private Facture facture;
    private long idEntreprise = 0;
    int nombreDeArticleRetournee = 0;
    private boolean annulation = true;
    private BigDecimal oldPrix;
    private BigDecimal oldQuantity;
    private double oldRemise;
    //private BigDecimal ancienMontantHT;
    private LigneFacture selectedLigneFacture;
    private LigneFacture selectedLigneFactureSingle;
    private LigneFacture selectedLigneFactureModif;
    private AvoirVente selectedAvoirVente;
    private List<LigneFacture> LigneFactureTemps;
    private List<LigneFacture> AncienLigneFacture;
    private List<CategorieClient> listCategorieClient = null;
    private List<Article> listArticles = null;
    private List<Article> listArticleTemps;
    private List<ParametrageTaxe> listParametrageTaxeEntreprise = null;
    private List<ParametrageTaxe> selectedListParametrageTaxe = null;
    private TaxesFacture selectedSingleTaxe;
    private TaxesFacture selectedTaxe;
    private List<TaxesFacture> listTaxeFacture = null;
    private List<TaxesFacture> listTaxeSuppresssion = null;
    private PrefixFacture prefixeFacture;
    private List<PrefixFacture> listPrefixFacture = new ArrayList<>();
    private List<Devis> listDeviss = new ArrayList();
    private List<BonCommandeVente> listCommandes = new ArrayList();
    private Utilisateur utilisateur;

    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Integer etatFacture;
    private Integer origineFacture;
    private Client client;
    private boolean avoir;
    private boolean retour;

    public FactureController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (facture.getIdEntrepriseSuivi() != null && facture.getIdEntrepriseSuivi() != 0) {
                idEntreprise = facture.getIdEntrepriseSuivi();
            } else {
                idEntreprise = facture.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GFacture", "MVente", null,utilisateur);
            //MenuTemplate.menuFonctionnalitesModules("GFacture", utilisateur);
            /*if (facture.getIdEntrepriseSuivi() != null && facture.getIdEntrepriseSuivi() != 0) {
                idEntreprise = facture.getIdEntrepriseSuivi();
            } else {
                idEntreprise = facture.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../facture/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void generateRetour() {

        traitementRetour.runCreationRetour();
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
        selectedAvoirVente = null;
    }

    public List<Facture> getItems() {
        try {
            if (items == null) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.Fct_DateFacture between '" + debut + "' and '" + fin + "' ";
                items = getFacade().findAllNative(clause + " order by o.Fct_DateFacture desc");

            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- FactureController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Facture getSelected() {
        return selected;
    }

    public void setSelected(Facture selected) {
        this.selected = selected;
    }

    public Facture getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Facture selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private FactureFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        selectedLigneFacture = null;
        selectedLigneFactureSingle = null;
        selectedLigneFactureModif = null;
        categorie = null;
        //client = null;
        categorieClient = null;

        return "List";
    }

    public String prepareView() {
        if (selected != null) {

            selectedAvoirVente = new AvoirVente();
            selectedAvoirVente.setRemise(BigDecimal.ZERO);
            selectedAvoirVente.setMontantHT(selected.getTotalHT());
            selectedAvoirVente.setMontantTTC(selected.getMontantTTC());
            selectedAvoirVente.setReste(BigDecimal.ZERO);
            selectedAvoirVente.setListLigneAvoirVentes(new ArrayList<LigneAvoirVente>());
            nombreDeArticleRetournee = 0;
            for (LigneFacture ligneFacture : selected.getListeLigneFactures()) {
                LigneAvoirVente ligneAvoirVente = new LigneAvoirVente();
                ligneAvoirVente.setIdArticle(ligneFacture.getIdArticle());
                ligneAvoirVente.setCodeArticle(ligneFacture.getCodeArticle());
                ligneAvoirVente.setLibelleArticle(ligneFacture.getLibelleArticle());
                ligneAvoirVente.setNumero(selected.getNumero());
                ligneAvoirVente.setTvaArticle(ligneFacture.getTvaArticle());
                ligneAvoirVente.setQuantite(ligneFacture.getQuantite());
                ligneAvoirVente.setQuantiteFacture(ligneFacture.getQuantite());
                ligneAvoirVente.setPrixUnitaireHT(ligneFacture.getPrixUnitaireHT());
                ligneAvoirVente.setPrixUnitaireApresRemise(ligneFacture.getPrixUnitaireApresRemise());
                ligneAvoirVente.setRemise(ligneFacture.getRemise());
                ligneAvoirVente.setTotalHT(ligneFacture.getTotalHT());
                ligneAvoirVente.setTotalTVA(ligneFacture.getTotalTVA());
                ligneAvoirVente.setTotalTTC(ligneFacture.getTotalTTC());
                selectedAvoirVente.getListLigneAvoirVentes().add(ligneAvoirVente);
            }

            if (selected.getOrigine() == 2) {
                listSelectedBonLivraison = ejbFacadeBonLivraison.findAllNative(" where o.BLiv_IdDocumentTransform = " + selected.getId());

                for (BonLivraison bonLivraison : listSelectedBonLivraison) {

                    for (LigneBonLivraison ligneBonLivraison : bonLivraison.getListeLigneBonLivraisons()) {

                        LigneAvoirVente ligneAvoirVente = new LigneAvoirVente();

                        ligneAvoirVente.setIdArticle(ligneBonLivraison.getIdArticle());
                        ligneAvoirVente.setCodeArticle(ligneBonLivraison.getCodeArticle());
                        ligneAvoirVente.setLibelleArticle(ligneBonLivraison.getLibelleArticle());
                        ligneAvoirVente.setNumero(bonLivraison.getNumero());
                        ligneAvoirVente.setTvaArticle(ligneBonLivraison.getTvaArticle());

                        ligneAvoirVente.setQuantite(ligneBonLivraison.getQuantite());
                        ligneAvoirVente.setQuantiteFacture(ligneBonLivraison.getQuantite());

                        ligneAvoirVente.setPrixUnitaireHT(ligneBonLivraison.getPrixUnitaireHT());

                        ligneAvoirVente.setRemise(ligneBonLivraison.getRemise());
                        ligneAvoirVente.setPrixUnitaireApresRemise(ligneBonLivraison.getPrixUnitaireApresRemise());

                        ligneAvoirVente.setTotalHT(ligneBonLivraison.getTotalHT());
                        ligneAvoirVente.setTotalTVA(ligneBonLivraison.getTotalTVA());
                        ligneAvoirVente.setTotalTTC(ligneBonLivraison.getTotalTTC());

                        selectedAvoirVente.getListLigneAvoirVentes().add(ligneAvoirVente);
                    }

                }

            }

            return "View";
        }
        return "List";
    }

    public String prepareCreate() {

        categorieClient = new CategorieClient();
        selected = new Facture();
        errorMsg = false;
        selectedLigneFacture = new LigneFacture();
        selectedLigneFacture.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneFacture.setTvaArticle(BigDecimal.ZERO);
        selectedLigneFacture.setQuantite(BigDecimal.ZERO);
        selectedLigneFacture.setRemise(BigDecimal.ZERO);
        selectedLigneFacture.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneFacture.setTotalHT(BigDecimal.ZERO);
        selectedLigneFacture.setTotalTVA(BigDecimal.ZERO);
        selectedLigneFacture.setTotalTTC(BigDecimal.ZERO);

        selectedLigneFactureModif = new LigneFacture();
        selectedLigneFactureModif.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneFactureModif.setTvaArticle(BigDecimal.ZERO);
        selectedLigneFactureModif.setQuantite(BigDecimal.ZERO);
        selectedLigneFactureModif.setRemise(BigDecimal.ZERO);
        selectedLigneFactureModif.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneFactureModif.setTotalHT(BigDecimal.ZERO);
        selectedLigneFactureModif.setTotalTVA(BigDecimal.ZERO);
        selectedLigneFactureModif.setTotalTTC(BigDecimal.ZERO);

        selectedLigneFactureSingle = new LigneFacture();
        selectedLigneFactureSingle.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneFactureSingle.setTvaArticle(BigDecimal.ZERO);
        selectedLigneFactureSingle.setQuantite(BigDecimal.ZERO);
        selectedLigneFactureSingle.setRemise(BigDecimal.ZERO);
        selectedLigneFactureSingle.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneFactureSingle.setTotalHT(BigDecimal.ZERO);
        selectedLigneFactureSingle.setTotalTVA(BigDecimal.ZERO);
        selectedLigneFactureSingle.setTotalTTC(BigDecimal.ZERO);

        listSelectedBonLivraison = new ArrayList<BonLivraison>();
        listBonLivraison = new ArrayList<BonLivraison>();

        categorieClient = new CategorieClient();
        listClient = new ArrayList<>();
        listArticleTemps = ejbFacadeArticle.findAll();
        listPrefixFacture = new ArrayList<>();
        selectedListParametrageTaxe = new ArrayList<>();
        listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");
        selected.setTotalTaxe(BigDecimal.ZERO);
        selected.setTypeVente(0);
        selected.setDateFacture(new Date());
        selected.setListeLigneFactures(new ArrayList<LigneFacture>());
        selected.setListsTaxe(new ArrayList<TaxesFacture>());
        categorie = new Categorie();

        initTaxeFacture(listParametrageTaxeEntreprise);

        return "Create";
    }

    public String create() {

        /*try {*/
        if (selected.getOrigine() == 3) {
            if (listSelectedBonLivraison != null && !listSelectedBonLivraison.isEmpty()) {
                //recalculer les taxes
                changedFactureBL();
                List<TaxesFacture> listTaxesTemps = new ArrayList<>();
                List<LigneFacture> tempsList = new ArrayList<>();
                tempsList = selected.getListeLigneFactures();
                selected.setListeLigneFactures(null);

                selected.setDateCreation(new Date());
                if (selected.getClient() != null) {
                    selected.setIdClient(selected.getClient().getId());
                    //selected.setCodeClient(selected.getClient().getCode());
                    selected.setAssujettiTVA(selected.getClient().isAssujettiTVA());
                    selected.setLibelleClient(selected.getClient().getLibelle());
                }
                selected.setAvoir(false);

                selected.setSynchroniser(false);

                selected.setListeEncaissements(new ArrayList<Encaissement>());
                //selected.setListePaquetVendus(new ArrayList<PaquetVendu>());
                selected.setListeLigneFactures(new ArrayList<LigneFacture>());


                if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                    listTaxesTemps = selected.getListsTaxe();
                    selected.setListsTaxe(null);

                }

                listPrefixFacture = ejbFacadePrefixFacture.findAll("where o.supprimer = 0");
                if (listPrefixFacture != null && !listPrefixFacture.isEmpty()) {
                    prefixeFacture = listPrefixFacture.get(0);
                }

                if (prefixeFacture != null) {
                    String compteur = FonctionsString.retourMotSelonLongeur(prefixeFacture.getCompteur() + "", 6);
                    selected.setNumero(prefixeFacture.getLibellePrefixe() + compteur);

                    if (ejbFacade.verifierUniqueNumero(selected.getNumero()) == false) {
                        selected.setReste(selected.getTotalTTC());
                        selected.setEtat(0);
                        getFacade().create(selected);
                        prefixeFacture.setCompteur(prefixeFacture.getCompteur() + 1);
                        ejbFacadePrefixFacture.edit(prefixeFacture);

                    } else {
                        selected.setListeLigneFactures(tempsList);
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getNumero() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                        return null;
                    }

                    if (!listTaxesTemps.isEmpty()) {
                        for (TaxesFacture listTaxesTemp : listTaxesTemps) {
                            listTaxesTemp.setFacture(selected);
                            ejbFacadeTaxeFacture.create(listTaxesTemp);
                        }
                    }
                    //boolean creationBL = false;
                    for (LigneFacture tempsList1 : tempsList) {
                        if (tempsList1.getQuantite().compareTo(BigDecimal.ZERO) == 1) {
                            tempsList1.setFacture(selected);
                            ejbFacadeLigneFacture.create(tempsList1);
                            // JE NE VAIS PAS CREER LA BL
                            //creationBL = true;
                        }
                    }

                    for (BonLivraison bonLivraison : listSelectedBonLivraison) {

                        // Si c'est une vente basé directement sur le client alors l'encaissement du BL = Encaissement facture
                        if (selected.getTypeVente() == 0) {
                            List<Encaissement> listEncaissements = new ArrayList<>();
                            for (EncaissementBonLivraison encaissementBonLivraison : bonLivraison.getListEncaissementBonLivraisons()) {

                                Encaissement encaissement = new Encaissement();

                                encaissement.setDateCheque(encaissementBonLivraison.getDateCheque());
                                encaissement.setDateEncaissement(encaissementBonLivraison.getDateEncaissement());
                                encaissement.setDeductionPourcentageTicket(encaissementBonLivraison.getDeductionPourcentageTicket());
                                encaissement.setFacture(selected);
                                encaissement.setIdBanque(encaissementBonLivraison.getIdBanque());
                                encaissement.setIdClient(encaissementBonLivraison.getIdClient());

                                encaissement.setLibelleBanque(encaissementBonLivraison.getLibelleBanque());
                                encaissement.setLibelleClient(encaissementBonLivraison.getLibelleClient());
                                encaissement.setVille(encaissementBonLivraison.getVille());
                                encaissement.setValeurUnitaireTicket(encaissementBonLivraison.getValeurUnitaireTicket());
                                encaissement.setTypeEncaissementVente(encaissementBonLivraison.getTypeEncaissementVente());
                                encaissement.setTireur(encaissementBonLivraison.getTireur());
                                encaissement.setTauxRetenu(encaissementBonLivraison.getTauxRetenu());
                                encaissement.setSupprimer(encaissementBonLivraison.isSupprimer());
                                encaissement.setNumero(encaissementBonLivraison.getNumero());
                                encaissement.setNumCheque(encaissementBonLivraison.getNumCheque());
                                encaissement.setNbrTicket(encaissementBonLivraison.getNbrTicket());
                                encaissement.setMontant(encaissementBonLivraison.getMontant());
                                encaissement.setLibelleTicket(encaissementBonLivraison.getLibelleTicket());
                                /*encaissement.setLibelleEntreprise(encaissementBonLivraison.getLibelleEntreprise());
                                encaissement.setVendeur(encaissementBonLivraison.getVendeur());*/
                                selected.setReste(selected.getReste().subtract(encaissement.getMontant()));


                                listEncaissements.add(encaissement);
                            }

                            ejbFacadeEncaissement.create(listEncaissements);
                        }

                        if (selected.getReste().compareTo(BigDecimal.ZERO) == 0) {

                            selected.setEtat(1);
                        } else if (selected.getReste().compareTo(selected.getTotalTTC()) == -1) {

                            selected.setEtat(3);
                        }

                        bonLivraison.setIdDocumentTransform(selected.getId());
                        ejbFacadeBonLivraison.edit(bonLivraison);
                    }

                    ejbFacade.edit(selected);
                    //createRetour(tempsList, selected);
                    return prepareList();

                } else {
                    selected.setListeLigneFactures(tempsList);
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
                    return null;
                }

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getNumero() + " " + ResourceBundle.getBundle("/Bundle").getString("BonLivraisonsInexistantes")));
                return null;
            }

            // origine vente directe
        } else if (selected.getOrigine() == 0) {
            if (selected.getListeLigneFactures() != null && !selected.getListeLigneFactures().isEmpty()) {
                List<TaxesFacture> listTaxesTemps = new ArrayList<>();

                if (selected.getClient() != null) {
                    selected.setIdClient(selected.getClient().getId());
                    //selected.setCodeClient(selected.getClient().getCode());
                    selected.setLibelleClient(selected.getClient().getLibelle());
                    selected.setAssujettiTVA(selected.getClient().isAssujettiTVA());
                }
                selected.setAvoir(false);

                selected.setReste(selected.getTotalTTC());

                List<LigneFacture> tempsList = new ArrayList<>();
                tempsList = selected.getListeLigneFactures();
                selected.setListeLigneFactures(null);
                selected.setSynchroniser(false);
                selected.setDateCreation(new Date());

                if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                    listTaxesTemps = selected.getListsTaxe();
                    selected.setListsTaxe(null);

                }

                listPrefixFacture = ejbFacadePrefixFacture.findAll("");
                if (listPrefixFacture != null && !listPrefixFacture.isEmpty()) {
                    prefixeFacture = listPrefixFacture.get(0);
                }
                //je vérifie prefix facture
                if (prefixeFacture != null) {
                    String compteur = FonctionsString.retourMotSelonLongeur(prefixeFacture.getCompteur() + "", 6);
                    selected.setNumero(prefixeFacture.getLibellePrefixe() + compteur);


                    selected.setEtat(0);
                    getFacade().create(selected);
                    prefixeFacture.setCompteur(prefixeFacture.getCompteur() + 1);
                    ejbFacadePrefixFacture.edit(prefixeFacture);

                    for (LigneFacture tempsList1 : tempsList) {
                        tempsList1.setFacture(selected);
                        ejbFacadeLigneFacture.create(tempsList1);
                    }
                    if (!listTaxesTemps.isEmpty()) {
                        for (TaxesFacture listTaxesTemp : listTaxesTemps) {
                            listTaxesTemp.setFacture(selected);
                            ejbFacadeTaxeFacture.create(listTaxesTemp);
                        }
                    }
                    //createRetour(tempsList, selected);
                    return prepareList();
                    /*} else {
                        selected.setListeLigneFactures(tempsList);
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
                        return null;
                    }*/

                } else {
                    selected.setListeLigneFactures(tempsList);
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("DetailNull")));
                return null;
            }

        } else if (selected.getOrigine() == 1) {

            List<TaxesFacture> listTaxesTemps = new ArrayList<>();

            selected.setAvoir(false);
            selected.setSynchroniser(false);
            //selected.setListeLigneFactures(new ArrayList<LigneFacture>());

            if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                listTaxesTemps = selected.getListsTaxe();
                selected.setListsTaxe(null);

            }

            listPrefixFacture = ejbFacadePrefixFacture.findAll("where o.supprimer = 0");
            if (listPrefixFacture != null && !listPrefixFacture.isEmpty()) {
                prefixeFacture = listPrefixFacture.get(0);
            }

            if (prefixeFacture != null) {
                String compteur = FonctionsString.retourMotSelonLongeur(prefixeFacture.getCompteur() + "", 6);
                selected.setNumero(prefixeFacture.getLibellePrefixe() + compteur);

                if (ejbFacade.verifierUniqueNumero(selected.getNumero()) == false) {

                    List<LigneFacture> tempsList = new ArrayList<>();
                    tempsList = selected.getListeLigneFactures();
                    selected.setListeLigneFactures(null);

                    createFactureFromDevis(tempsList);

                    if (!listTaxesTemps.isEmpty()) {
                        for (TaxesFacture taxesTemp : listTaxesTemps) {
                            taxesTemp.setFacture(selected);
                            ejbFacadeTaxeFacture.create(taxesTemp);
                        }
                    }
                    selected.getDevis().setIdDocumentTransform(selected.getId());
                    selected.getDevis().setNumeroDocumentTransform(selected.getNumero());
                    selected.getDevis().setTransFormTo(1);
                    selected.getDevis().setEtat(4);
                    ejbFacadeDevis.edit(selected.getDevis());

                    /*selected.setReste(selected.getTotalTTC());
                    selected.setEtat(0);
                    getFacade().create(selected);*/
                    prefixeFacture.setCompteur(prefixeFacture.getCompteur() + 1);
                    ejbFacadePrefixFacture.edit(prefixeFacture);
                    //createRetour(tempsList, selected);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getNumero() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                    return null;
                }

                return prepareList();

            } else {

                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
                return null;
            }

        } else if (selected.getOrigine() == 2) {

            List<TaxesFacture> listTaxesTemps = new ArrayList<>();

            selected.setAvoir(false);
            selected.setSynchroniser(false);
            //selected.setListeLigneFactures(new ArrayList<LigneFacture>());

            if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                listTaxesTemps = selected.getListsTaxe();
                selected.setListsTaxe(null);

            }

            listPrefixFacture = ejbFacadePrefixFacture.findAll("where o.supprimer = 0");
            if (listPrefixFacture != null && !listPrefixFacture.isEmpty()) {
                prefixeFacture = listPrefixFacture.get(0);
            }

            if (prefixeFacture != null) {
                String compteur = FonctionsString.retourMotSelonLongeur(prefixeFacture.getCompteur() + "", 6);
                selected.setNumero(prefixeFacture.getLibellePrefixe() + compteur);

                if (ejbFacade.verifierUniqueNumero(selected.getNumero()) == false) {

                    List<LigneFacture> tempsList = new ArrayList<>();
                    tempsList = selected.getListeLigneFactures();
                    selected.setListeLigneFactures(null);

                    createFactureFromCommandes(tempsList);

                    if (!listTaxesTemps.isEmpty()) {
                        for (TaxesFacture taxesTemp : listTaxesTemps) {
                            taxesTemp.setFacture(selected);
                            ejbFacadeTaxeFacture.create(taxesTemp);
                        }
                    }
                    selected.getBonCommande().setIdDocumentTransform(selected.getId());
                    selected.getBonCommande().setNumeroDocumentTransform(selected.getNumero());
                    selected.getBonCommande().setTransFormTo(1);
                    selected.getBonCommande().setEtat(4);
                    ejbFacadeBonCommande.edit(selected.getBonCommande());

                    /*selected.setReste(selected.getTotalTTC());
                    selected.setEtat(0);
                    getFacade().create(selected);*/
                    prefixeFacture.setCompteur(prefixeFacture.getCompteur() + 1);
                    ejbFacadePrefixFacture.edit(prefixeFacture);
                    //createRetour(tempsList, selected);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getNumero() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                    return null;
                }

                return prepareList();

            } else {

                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
                return null;
            }

        } else {

            return null;
        }

        /*} catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- FactureController - create: " + e.getMessage());
            return null;
        }*/
    }

    public void createFactureFromDevis(List<LigneFacture> tempsList) {

        //selected.setNumero(numero);
        selected.setCodeClient(selected.getDevis().getCodeClient());
        selected.setLibelleClient(selected.getDevis().getLibelleClient());
        selected.setAssujettiTVA(selected.getDevis().isAssujettiTVA());
        selected.setIdClient(selected.getDevis().getIdClient());

        selected.setEtat(0);
        selected.setDateCreation(new Date());
        selected.setOrigine(1);
        selected.setIdDocumentOrigine(selected.getDevis().getId());
        selected.setNumeroDocumentOrigine(selected.getDevis().getNumero());
        selected.setTypeVente(0);
        selected.setMontantHT(selected.getDevis().getMontantHT());
        selected.setMontantTVA(selected.getDevis().getMontantTVA());
        selected.setMontantTTC(selected.getDevis().getMontantTTC());
        selected.setTotalHT(selected.getDevis().getTotalHT());
        selected.setTotalTVA(selected.getDevis().getTotalTVA());
        selected.setTotalTTC(selected.getDevis().getTotalTTC());
        selected.setTotalTaxe(selected.getDevis().getTotalTaxe());
        selected.setReste(selected.getDevis().getTotalTTC());

        ejbFacade.create(selected);

        //List<LigneFacture> listLigneFactures = new ArrayList<>();
        for (LigneFacture tempsList1 : tempsList) {
            tempsList1.setFacture(selected);

        }
        ejbFacadeLigneFacture.create(tempsList);

    }
    
    public void createFactureFromCommandes(List<LigneFacture> tempsList) {

        //selected.setNumero(numero);
        selected.setCodeClient(selected.getBonCommande().getCodeClient());
        selected.setLibelleClient(selected.getBonCommande().getLibelleClient());
        selected.setAssujettiTVA(selected.getBonCommande().isAssujettiTVA());
        selected.setIdClient(selected.getBonCommande().getIdClient());

        selected.setEtat(0);
        selected.setDateCreation(new Date());
        selected.setOrigine(2);
        selected.setIdDocumentOrigine(selected.getBonCommande().getId());
        selected.setNumeroDocumentOrigine(selected.getBonCommande().getNumero());
        selected.setTypeVente(0);
        selected.setMontantHT(selected.getBonCommande().getMontantHT());
        selected.setMontantTVA(selected.getBonCommande().getMontantTVA());
        selected.setMontantTTC(selected.getBonCommande().getMontantTTC());
        selected.setTotalHT(selected.getBonCommande().getTotalHT());
        selected.setTotalTVA(selected.getBonCommande().getTotalTVA());
        selected.setTotalTTC(selected.getBonCommande().getTotalTTC());
        selected.setTotalTaxe(selected.getBonCommande().getTotalTaxe());
        selected.setReste(selected.getBonCommande().getTotalTTC());

        ejbFacade.create(selected);

        //List<LigneFacture> listLigneFactures = new ArrayList<>();
        for (LigneFacture tempsList1 : tempsList) {
            tempsList1.setFacture(selected);

        }
        ejbFacadeLigneFacture.create(tempsList);

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

            selectedLigneFacture = new LigneFacture();
            selectedLigneFactureModif = new LigneFacture();
            selectedLigneFactureSingle = new LigneFacture();

            selectedListParametrageTaxe = new ArrayList<>();
            listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");
            selectedTaxe = new TaxesFacture();

            //listDeviss = new ArrayList();
            listTaxeFacture = new ArrayList<>();
            listTaxeFacture = selected.getListsTaxe();
            listTaxeSuppresssion = new ArrayList<>();
            categorie = new Categorie();

            Client client = new Client();
            client.setId(selected.getIdClient());
            client.setAssujettiTVA(selected.isAssujettiTVA());
            client.setLibelle(selected.getLibelleClient());
            selected.setClient(client);

            //ancienMontantHT = selected.getMontantHT();
            for (LigneFacture ligneFacture : selected.getListeLigneFactures()) {

                Article article = new Article();
                article.setId(ligneFacture.getIdArticle());
                article.setCode(ligneFacture.getCodeArticle());
                article.setLibelle(ligneFacture.getLibelleArticle());
                ligneFacture.setArticle(article);

            }

            AncienLigneFacture = new ArrayList<LigneFacture>(selected.getListeLigneFactures());

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {

            if (selected.getListeLigneFactures() != null && !selected.getListeLigneFactures().isEmpty()) {

                LigneFactureTemps = new ArrayList<LigneFacture>(selected.getListeLigneFactures());
                selected.setListeLigneFactures(null);
                selected.setListsTaxe(null);

                BigDecimal montantPaye = BigDecimal.ZERO;
                for (Encaissement item : selected.getListeEncaissements()) {
                    if (item.isSupprimer() == false) {
                        montantPaye = montantPaye.add(item.getMontant());
                    }
                }
                selected.setReste(selected.getTotalTTC().subtract(montantPaye));

                getFacade().edit(selected);

                //LigneFactureTemps = selected.getListeLigneFactures();
                for (LigneFacture ligneFacture : AncienLigneFacture) {

                    int index = LigneFactureTemps.indexOf(ligneFacture);

                    if (index == -1) {

                        ejbFacadeLigneFacture.remove(ligneFacture);
                    } else {
                        //ceci pour remédier au cas ou le stock est supprimer puis réajouter sans passer par la modif
                        LigneFactureTemps.get(index).setId(ligneFacture.getId());
                        LigneFactureTemps.get(index).setFacture(selected);
                        ejbFacadeLigneFacture.edit(LigneFactureTemps.get(index));

                    }

                }
                sauvegardeNouvelleLigneFacture();
                //les taxes a suuprimer
                if (listTaxeSuppresssion != null && !listTaxeSuppresssion.isEmpty()) {
                    ejbFacadeTaxeFacture.remove(listTaxeSuppresssion);
                }

                for (TaxesFacture taxesFacture : listTaxeFacture) {
                    taxesFacture.setFacture(selected);
                    if (taxesFacture.getId() != null) {
                        ejbFacadeTaxeFacture.edit(taxesFacture);
                    } else {
                        ejbFacadeTaxeFacture.create(taxesFacture);
                    }

                }

                //errorMsg = getFacade().verifierUnique(selected.getLibellePrefixe().trim(), selected.getId());
                //if (errorMsg == false) {
                /* if (selected.getMontant().compareTo(BigDecimal.ZERO) == -1) {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("ValeurIncorrecte")));
                    return null;
                } else {*/
                //selected.setIdEntreprise(idEntreprise);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("DetailCommandeNull")));
                return null;
            }

            //}
            /* } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getNumero() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }*/
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("EchecOperation"), ""));
            System.out.println("Erreur- FactureController - update: " + e.getMessage());
            return null;
        }
    }

    private void sauvegardeNouvelleLigneFacture() {

        int j = 0;
        //boolean trouve = false;

        for (int i = 0; i < LigneFactureTemps.size(); i++) {

            if (!AncienLigneFacture.contains(LigneFactureTemps.get(i)) && LigneFactureTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 1) {
                LigneFactureTemps.get(i).setFacture(selected);
                ejbFacadeLigneFacture.create(LigneFactureTemps.get(i));
            }

        }

    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<FacturePVT> temps = ejbFacadeFacturePVT.findAll("where o.idFacture = " + selectedSingle.getId() + " ");
            //if (temps == null || temps.isEmpty()) {
            performDestroy();
            /*} else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SuppressionNonAutorisé")));
            }*/
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }
        return prepareList();
    }

    private void performDestroy() {
        try {
            getFacade().remove(selectedSingle);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("EchecOperation"), ""));
            System.out.println("Erreur- FactureController - performDestroy: " + e.getMessage());
        }
    }

    /*public void deleteFromListLigneFacture() {

        if (selectedLigneFactureSingle != null) {
            selected.getListeLigneFactures().remove(selectedLigneFactureSingle);
            recalculerTotal();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }*/
    public void deleteFromListLigneFacture() {


        if (selectedLigneFactureSingle != null) {
            selected.getListeLigneFactures().remove(selectedLigneFactureSingle);
            changedFactureBL();
            /*recalculerTotal();
            updateListTaxe(selected.getListsTaxe());*/
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }

    public void validerDetailArticle() {

        if (selected.getOrigine() != null && selectedLigneFacture.getArticle() != null) {

            selectedLigneFacture.setIdArticle(selectedLigneFacture.getArticle().getId());
            int index = selected.getListeLigneFactures().indexOf(selectedLigneFacture);

            if (index > -1) {
                //a ajouter
/*                selected.getListeLigneFactures().get(index).setArticle(selectedLigneFacture.getArticle());
                selected.getListeLigneFactures().get(index).setIdArticle(selectedLigneFacture.getIdArticle());
                selected.getListeLigneFactures().get(index).setCodeArticle(selectedLigneFacture.getCodeArticle());*/
                selected.getListeLigneFactures().get(index).setLibelleArticle(selectedLigneFacture.getLibelleArticle());
                selected.getListeLigneFactures().get(index).setTvaArticle(selectedLigneFacture.getTvaArticle());
                selected.getListeLigneFactures().get(index).setPrixUnitaireHT(selectedLigneFacture.getPrixUnitaireHT());
                selected.getListeLigneFactures().get(index).setTotalHT(selectedLigneFacture.getTotalHT());
                selected.getListeLigneFactures().get(index).setTotalTVA(selectedLigneFacture.getTotalTVA());
                selected.getListeLigneFactures().get(index).setTotalTTC(selectedLigneFacture.getTotalTTC());
                selected.getListeLigneFactures().get(index).setQuantite(selectedLigneFacture.getQuantite());
                //recalculerTotal();
                // l'undate des taxe est fait ici
                changedFactureBL();
                selectedLigneFacture = new LigneFacture();

            } else {

                selected.getListeLigneFactures().add(selectedLigneFacture);
                changedFactureBL();
                selectedLigneFacture = new LigneFacture();
            }

            categorie = new Categorie();

        }

    }

    public void validerDetailArticleModif() {

        if (selectedLigneFacture.getQuantite().compareTo(selectedLigneFacture.getQuantiteMax()) <= 0) {
            recalculerTotal();
            selectedLigneFacture = new LigneFacture();

        } else {
            selectedLigneFacture.setPrixUnitaireHT(oldPrix);
            selectedLigneFacture.setQuantite(oldQuantity);
            changedTotalHtTotalTtc();
        }

    }

    public void recalculerTotal() {
        selected.setMontantHT(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTVA(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTTC(new BigDecimal(BigInteger.ZERO));
        for (LigneFacture ligneFacture : selected.getListeLigneFactures()) {
            selected.setMontantHT(selected.getMontantHT().add(ligneFacture.getTotalHT()));
            selected.setMontantTVA(selected.getMontantTVA().add(ligneFacture.getTotalTVA()));
            selected.setMontantTTC(selected.getMontantTTC().add(ligneFacture.getTotalTTC()));
        }
        selected.setTotalHT(selected.getMontantHT());
        selected.setTotalTVA(selected.getMontantTVA());
        selected.setTotalTTC(selected.getMontantTTC());
    }

    public void listnerPrixUnitaire() {
        if (selectedLigneFacture.getArticle() != null) {
            selectedLigneFacture.setIdArticle(selectedLigneFacture.getArticle().getId());
            selectedLigneFacture.setCodeArticle(selectedLigneFacture.getArticle().getCode());
            selectedLigneFacture.setLibelleArticle(selectedLigneFacture.getArticle().getLibelle());
            selectedLigneFacture.setPrixUnitaireHT(selectedLigneFacture.getArticle().getPrixRevendeur());
            selectedLigneFacture.setTvaArticle(new BigDecimal(selectedLigneFacture.getArticle().getTva().getValeur()));
            selectedLigneFacture.setQuantite(BigDecimal.ZERO);
            selectedLigneFacture.setRemise(BigDecimal.ZERO);
            selectedLigneFacture.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneFacture.setTotalHT(BigDecimal.ZERO);
            selectedLigneFacture.setTotalTVA(BigDecimal.ZERO);
            selectedLigneFacture.setTotalTTC(BigDecimal.ZERO);
        }
    }

    public void changedTotalHtTotalTtc() {
        if (selected.getClient() == null || selectedLigneFacture.getLibelleArticle() == null) {
            selectedLigneFacture.setPrixUnitaireHT(BigDecimal.ZERO);
            selectedLigneFacture.setTvaArticle(BigDecimal.ZERO);
            selectedLigneFacture.setQuantite(BigDecimal.ZERO);
            selectedLigneFacture.setRemise(BigDecimal.ZERO);
            selectedLigneFacture.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneFacture.setTotalHT(BigDecimal.ZERO);
            selectedLigneFacture.setTotalTVA(BigDecimal.ZERO);
            selectedLigneFacture.setTotalTTC(BigDecimal.ZERO);
        } else {
            selectedLigneFacture.setTotalHT(BigDecimal.ZERO);
            selectedLigneFacture.setTotalTVA(BigDecimal.ZERO);
            selectedLigneFacture.setTotalTTC(BigDecimal.ZERO);
            selectedLigneFacture.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            BigDecimal prixRevendeur = selectedLigneFacture.getPrixUnitaireHT();
            if (selectedLigneFacture.getRemise().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal valRemise = FonctionsMathematiques.arrondiBigDecimal(prixRevendeur.multiply(selectedLigneFacture.getRemise()), 3);
                valRemise = FonctionsMathematiques.arrondiBigDecimal(valRemise.divide(new BigDecimal("100")), 3);
                prixRevendeur = prixRevendeur.subtract(valRemise);
            }
            selectedLigneFacture.setPrixUnitaireApresRemise(prixRevendeur);

            BigDecimal TotalHT = FonctionsMathematiques.arrondiBigDecimal((selectedLigneFacture.getPrixUnitaireApresRemise()).multiply(selectedLigneFacture.getQuantite()), 3);
            selectedLigneFacture.setTotalHT(TotalHT);

            // si le client doit payer TVA pour l'article
            if (selected.getClient().isAssujettiTVA()) {
                selectedLigneFacture.setTotalTVA(((selectedLigneFacture.getTotalHT().multiply(selectedLigneFacture.getTvaArticle()))).divide(BigDecimal.valueOf(100)));
                selectedLigneFacture.setTotalTTC(selectedLigneFacture.getTotalHT().add(selectedLigneFacture.getTotalTVA()));
            } else {
                selectedLigneFacture.setTotalTVA(BigDecimal.ZERO);
                selectedLigneFacture.setTotalTTC(TotalHT);
            }

//            BigDecimal valTemp = FonctionsMathematiques.arrondiBigDecimal(selectedLigneFacture.getMontantHT().multiply(selectedLigneFacture.getTvaArticle()), 3);
//            selectedLigneFacture.setMontantTVA(FonctionsMathematiques.arrondiBigDecimal(valTemp.divide(BigDecimal.valueOf(100)), 3));
//            selectedLigneFacture.setMontantTTC(selectedLigneFacture.getMontantHT().add(selectedLigneFacture.getMontantTVA()));
        }
    }

    public void changedBonLivraison() {

        //Cas bon de livraison
        if (selected.getOrigine() == 3) {
            /*if (selected.getLivreur() != null && selected.getTypeVente() == 1 && selected.getDomaine() != null) {

                System.err.println("getLivreur " + selected.getLivreur().getId());
                listBonLivraison = ejbFacadeBonLivraison.findAllNative(" where o.Ent_Id = " + idEntreprise + " and o.Liv_Id = " + selected.getLivreur().getId() + " and o.BLiv_IdDocumentTransform is null and o.BLiv_OrigineBonLivraison = 2 and o.Dom_Id = '" + selected.getDomaine().getId() + "'");

            } else*/ if (selected.getClient() != null) {
                listBonLivraison = ejbFacadeBonLivraison.findAllNative(" where o.Cli_Id = " + selected.getClient().getId() + " and o.BLiv_IdDocumentTransform is null and o.BLiv_OrigineBonLivraison = 3 ");

            } else {
                listBonLivraison = new ArrayList<>();
            }
        } else if (selected.getOrigine() == 1) {

            if (selected.getClient() != null) {
                listDeviss = ejbFacadeDevis.findAllNative(" where o.Cli_Id = " + selected.getClient().getId() + " and o.Dev_Etat = 1 ");

            } else {
                listDeviss = new ArrayList<>();
            }

        }else if (selected.getOrigine() == 2) {

            if (selected.getClient() != null) {
                listCommandes = ejbFacadeBonCommande.findAllNative(" where o.Cli_Id = " + selected.getClient().getId() + " and o.BCVnt_Etat = 1 ");

            } else {
                listCommandes = new ArrayList<>();
            }

        }

    }

    public void changeDevis() {

        selected.setListeLigneFactures(new ArrayList<LigneFacture>());

        if (selected.getDevis().getListeLigneDeviss().isEmpty()) {
            selected.getDevis().setListeLigneDeviss(ejbFacadeLigneDevis.findAll(" where o.devis.id = " + selected.getDevis().getId()));

        }
        selected.setTotalTaxe(selected.getDevis().getTotalTaxe());
        selected.setMontantHT(selected.getDevis().getTotalHT());
        selected.setMontantTVA(selected.getDevis().getTotalTVA());
        selected.setMontantTTC(selected.getDevis().getTotalTTC());

        selected.setTotalHT(selected.getMontantHT());
        selected.setTotalTVA(selected.getMontantTVA());
        selected.setTotalTTC(selected.getMontantTTC());

        // nous allons calculer la taxe
        for (LigneDevis detailDevis : selected.getDevis().getListeLigneDeviss()) {

            LigneFacture ligneFacture = new LigneFacture();
            ligneFacture.setIdArticle(detailDevis.getIdArticle());
            ligneFacture.setCodeArticle(detailDevis.getCodeArticle());
            ligneFacture.setLibelleArticle(detailDevis.getLibelleArticle());
            ligneFacture.setPrixUnitaireApresRemise(detailDevis.getPrixUnitaireApresRemise());
            ligneFacture.setRemise(detailDevis.getRemise());
            ligneFacture.setQuantite(detailDevis.getQuantite());
            ligneFacture.setQuantiteMax(detailDevis.getQuantite());
            ligneFacture.setPrixUnitaireHT(detailDevis.getPrixUnitaireHT());
            ligneFacture.setTvaArticle(detailDevis.getTvaArticle());
            ligneFacture.setTotalHT(detailDevis.getTotalHT());
            ligneFacture.setTotalTVA(detailDevis.getTotalTVA());
            ligneFacture.setTotalTTC(detailDevis.getTotalTTC());
            // if recalculer taxe


            selected.getListeLigneFactures().add(ligneFacture);

            //List<TaxesFacture> listTaxesFactures = new ArrayList<>();
            for (TaxesDevis taxesDevis : selected.getDevis().getListsTaxe()) {
                TaxesFacture taxesFacture = new TaxesFacture();
                //taxesFacture.setFacture(facture);
                taxesFacture.setApresTva(taxesDevis.isApresTva());
                taxesFacture.setValeur(taxesDevis.getValeur());
                taxesFacture.setMontant(taxesDevis.getMontant());
                taxesFacture.setTypeTaxe(taxesDevis.getTypeTaxe());
                taxesFacture.setOperation(taxesDevis.getOperation());
                taxesFacture.setParametrageTaxe(taxesDevis.getParametrageTaxe());
                selected.getListsTaxe().add(taxesFacture);
            }

            //ejbFacadeTaxeFacture.create(listTaxesFactures);
        }

    }
    
    public void changeCommande() {

        selected.setListeLigneFactures(new ArrayList<LigneFacture>());

        if (selected.getBonCommande().getListeLigneBonCommandeVentes().isEmpty()) {
            selected.getBonCommande().setListeLigneBonCommandeVentes(ejbFacadeLigneBonCommande.findAll(" where o.bonCommandeVente.id = " + selected.getBonCommande().getId()));

        }
        selected.setTotalTaxe(selected.getBonCommande().getTotalTaxe());
        selected.setMontantHT(selected.getBonCommande().getTotalHT());
        selected.setMontantTVA(selected.getBonCommande().getTotalTVA());
        selected.setMontantTTC(selected.getBonCommande().getTotalTTC());

        selected.setTotalHT(selected.getMontantHT());
        selected.setTotalTVA(selected.getMontantTVA());
        selected.setTotalTTC(selected.getMontantTTC());

        // nous allons calculer la taxe
        for (LigneBonCommandeVente ligneBonCommandeVente : selected.getBonCommande().getListeLigneBonCommandeVentes()) {

            LigneFacture ligneFacture = new LigneFacture();
            ligneFacture.setIdArticle(ligneBonCommandeVente.getIdArticle());
            ligneFacture.setCodeArticle(ligneBonCommandeVente.getCodeArticle());
            ligneFacture.setLibelleArticle(ligneBonCommandeVente.getLibelleArticle());
            ligneFacture.setPrixUnitaireApresRemise(ligneBonCommandeVente.getPrixUnitaireApresRemise());
            ligneFacture.setRemise(ligneBonCommandeVente.getRemise());
            ligneFacture.setQuantite(ligneBonCommandeVente.getQuantite());
            ligneFacture.setQuantiteMax(ligneBonCommandeVente.getQuantite());
            ligneFacture.setPrixUnitaireHT(ligneBonCommandeVente.getPrixUnitaireHT());
            ligneFacture.setTvaArticle(ligneBonCommandeVente.getTvaArticle());
            ligneFacture.setTotalHT(ligneBonCommandeVente.getTotalHT());
            ligneFacture.setTotalTVA(ligneBonCommandeVente.getTotalTVA());
            ligneFacture.setTotalTTC(ligneBonCommandeVente.getTotalTTC());
            // if recalculer taxe


            selected.getListeLigneFactures().add(ligneFacture);

            //List<TaxesFacture> listTaxesFactures = new ArrayList<>();
            for (TaxesBonCommandeVente taxesBonCommandeVente : selected.getBonCommande().getListsTaxe()) {
                TaxesFacture taxesFacture = new TaxesFacture();
                //taxesFacture.setFacture(facture);
                taxesFacture.setApresTva(taxesBonCommandeVente.isApresTva());
                taxesFacture.setValeur(taxesBonCommandeVente.getValeur());
                taxesFacture.setMontant(taxesBonCommandeVente.getMontant());
                taxesFacture.setTypeTaxe(taxesBonCommandeVente.getTypeTaxe());
                taxesFacture.setOperation(taxesBonCommandeVente.getOperation());
                taxesFacture.setParametrageTaxe(taxesBonCommandeVente.getParametrageTaxe());
                selected.getListsTaxe().add(taxesFacture);
            }

            //ejbFacadeTaxeFacture.create(listTaxesFactures);
        }

    }

    public void changedFactureBL() {

        recalculerTotal();
        //selected.setListeLigneFactures(new ArrayList<LigneFacture>());
        if (listSelectedBonLivraison != null && !listSelectedBonLivraison.isEmpty()) {

            for (BonLivraison bonLivraison : listSelectedBonLivraison) {

                /*selected.setMontantHT(selected.getMontantHT().add(bonLivraison.getMontantHT()));
                 selected.setMontantTTC(selected.getMontantTTC().add(bonLivraison.getMontantTTC()));

                 selected.setTotalHT(selected.getMontantHT());
                 selected.setTotalTTC(selected.getMontantTTC());*/
                for (LigneBonLivraison ligneBonLivraison : bonLivraison.getListeLigneBonLivraisons()) {


                    LigneFacture ligneFacture = new LigneFacture();
                    //exprés pour différencier les lignes provenons d'un BL et les lignes qui peuvent étre ajouter récament
                    //ligneFacture.setId(ligneBonLivraison.getId());
                    ligneFacture.setIdArticle(ligneBonLivraison.getIdArticle());

// si la tva s'applique a ce client on va appliqer les nouvelle TVA
// ce ci peut changer => demander si on applique les nouvelles tva
                    if (selected.getClient().isAssujettiTVA()) {
                        Article articleTemp = new Article();
                        articleTemp.setId(ligneBonLivraison.getIdArticle());
                        int i = listArticleTemps.indexOf(articleTemp);

                        if (i > -1) {
                            // to customer
                            if (selected.getTypeVente() == 0) {
                                ligneFacture.setTotalHT(ligneBonLivraison.getTotalHT());
                            } else {
                                ligneFacture.setTotalHT(ligneBonLivraison.getQuantite().multiply(listArticleTemps.get(i).getPrixRevendeur()));
                            }
                            // the new TVA will be applied
                            ligneFacture.setTotalTVA(((ligneFacture.getTotalHT().multiply(BigDecimal.valueOf(listArticleTemps.get(i).getTva().getValeur())))).divide(BigDecimal.valueOf(100)));
                            ligneFacture.setTotalTTC(ligneFacture.getTotalHT().add(ligneFacture.getTotalTVA()));

                        } else {
                            ligneFacture.setTotalHT(ligneBonLivraison.getTotalHT());
                            ligneFacture.setTotalTVA(ligneBonLivraison.getTotalTVA());
                            ligneFacture.setTotalTTC(ligneBonLivraison.getTotalTTC());
                        }
                    } else {
                        ligneFacture.setTotalHT(ligneBonLivraison.getTotalHT());
                        ligneFacture.setTotalTVA(ligneBonLivraison.getTotalTVA());
                        ligneFacture.setTotalTTC(ligneBonLivraison.getTotalTTC());
                    }

                    selected.setMontantHT(selected.getMontantHT().add(ligneFacture.getTotalHT()));
                    selected.setTotalHT(selected.getMontantHT());

                    selected.setMontantTVA(selected.getMontantTVA().add(ligneFacture.getTotalTVA()));
                    selected.setTotalTVA(selected.getMontantTVA());

                    selected.setMontantTTC(selected.getMontantTTC().add(ligneFacture.getTotalTTC()));
                    selected.setTotalTTC(selected.getMontantTTC());
                    //ligneFacture.setFacture(selected);
                    // selected.getListeLigneFactures().add(ligneFacture);
                }

                /* }*/
            }

        }

        updateListTaxe(selected.getListsTaxe());
    }

    //taxe facture
    public boolean verifierExistanceTaxe(TaxesFacture taxe, List<TaxesFacture> listTaxe) {
        boolean result = false;
        int index = -1;
        for (int i = 0; i < listTaxe.size(); i++) {
            if (listTaxe.get(i).getParametrageTaxe().getLibelle().trim().equals(taxe.getParametrageTaxe().getLibelle().trim())) {
                index = i;
            }
        }
        if (index > -1) {
            result = true;
        }
        return result;

    }

    public void calculerTotalTaxe(List<TaxesFacture> list) {
        selected.setTotalTaxe(BigDecimal.ZERO);
        for (int i = 0; i < list.size(); i++) {
            if (selected.getListsTaxe().get(i).getOperation().trim().equals("+")) {
                selected.setTotalTaxe((selected.getTotalTaxe()).add(list.get(i).getMontant()));
            } else {

                selected.setTotalTaxe((selected.getTotalTaxe()).subtract(list.get(i).getMontant()));
            }
        }
    }

    public void validerTaxeFacture() {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantHT());
            selected.setTotalTTC(selected.getMontantTTC());
        }

        if (selectedListParametrageTaxe != null && !selectedListParametrageTaxe.isEmpty()) {

            for (ParametrageTaxe selectedTaxe : selectedListParametrageTaxe) {
                int index = -1;
                for (int j = 0; j < selected.getListsTaxe().size(); j++) {
                    if (selected.getListsTaxe().get(j).getParametrageTaxe().getLibelle().trim().equals(selectedTaxe.getLibelle())) {
                        index = j;
                    }
                }
                if (index > -1) {

                } else {
                    if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (!selectedTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectedTaxe);
                        taxe.setValeur(selectedTaxe.getValeur());
                        taxe.setOperation(selectedTaxe.getOperation());
                        taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                        taxe.setApresTva(selectedTaxe.isApresTva());
                        taxe.setMontant((selected.getMontantHT().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, selected.getListsTaxe())) {
                            selected.getListsTaxe().add(taxe);
                        }
                    } //apresTva
                    else if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (selectedTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectedTaxe);
                        taxe.setValeur(selectedTaxe.getValeur());
                        taxe.setOperation(selectedTaxe.getOperation());
                        taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                        taxe.setApresTva(selectedTaxe.isApresTva());
                        taxe.setMontant((selected.getMontantTTC().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, selected.getListsTaxe())) {
                            selected.getListsTaxe().add(taxe);
                        }
                        //valeurs fixe avant tva
                    } else if ((selectedTaxe.getTypeTaxe().trim().equals("1")) && (!selectedTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectedTaxe);
                        taxe.setValeur(selectedTaxe.getValeur());
                        taxe.setOperation(selectedTaxe.getOperation());
                        taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                        taxe.setApresTva(selectedTaxe.isApresTva());
                        taxe.setMontant(taxe.getValeur());
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, selected.getListsTaxe())) {
                            selected.getListsTaxe().add(taxe);
                        }
                    } //valeur fixe apres tva
                    else if ((selectedTaxe.getTypeTaxe().trim().equals("1")) && (selectedTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectedTaxe);
                        taxe.setValeur(selectedTaxe.getValeur());
                        taxe.setOperation(selectedTaxe.getOperation());
                        taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                        taxe.setApresTva(selectedTaxe.isApresTva());
                        taxe.setMontant(taxe.getValeur());
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, selected.getListsTaxe())) {
                            selected.getListsTaxe().add(taxe);
                        }
                    }
                }
            }
            calculerTotalTaxe(selected.getListsTaxe());
            selectedListParametrageTaxe = new ArrayList<>();
        }

    }

    public void validerTaxeFactureEdit() {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantHT());
            selected.setTotalTTC(selected.getMontantTTC());
        }
        if (selectedListParametrageTaxe != null && !selectedListParametrageTaxe.isEmpty()) {

            for (ParametrageTaxe selectTaxe : selectedListParametrageTaxe) {
                int index = -1;
                for (int j = 0; j < listTaxeFacture.size(); j++) {
                    if (listTaxeFacture.get(j).getParametrageTaxe().getLibelle().trim().equals(selectTaxe.getLibelle())) {
                        index = j;
                    }
                }
                if (index > -1) {

                } else {
                    if ((selectTaxe.getTypeTaxe().trim().equals("0")) && (!selectTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectTaxe);
                        taxe.setValeur(selectTaxe.getValeur());
                        taxe.setOperation(selectTaxe.getOperation());
                        taxe.setTypeTaxe(selectTaxe.getTypeTaxe());
                        taxe.setApresTva(selectTaxe.isApresTva());
                        taxe.setMontant((selected.getMontantHT().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, listTaxeFacture)) {
                            listTaxeFacture.add(taxe);
                        }
                    } //apresTva
                    else if ((selectTaxe.getTypeTaxe().trim().equals("0")) && (selectTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectTaxe);
                        taxe.setValeur(selectTaxe.getValeur());
                        taxe.setOperation(selectTaxe.getOperation());
                        taxe.setTypeTaxe(selectTaxe.getTypeTaxe());
                        taxe.setApresTva(selectTaxe.isApresTva());
                        taxe.setMontant((selected.getMontantTTC().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, listTaxeFacture)) {
                            listTaxeFacture.add(taxe);
                        }
                        //valeurs fixe avant tva
                    } else if ((selectTaxe.getTypeTaxe().trim().equals("1")) && (!selectTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectTaxe);
                        taxe.setValeur(selectTaxe.getValeur());
                        taxe.setOperation(selectTaxe.getOperation());
                        taxe.setTypeTaxe(selectTaxe.getTypeTaxe());
                        taxe.setApresTva(selectTaxe.isApresTva());
                        taxe.setMontant(taxe.getValeur());
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, listTaxeFacture)) {
                            listTaxeFacture.add(taxe);
                        }
                    } //valeur fixe apres tva
                    else if ((selectTaxe.getTypeTaxe().trim().equals("1")) && (selectTaxe.isApresTva())) {
                        TaxesFacture taxe = new TaxesFacture();
                        taxe.setParametrageTaxe(selectTaxe);
                        taxe.setValeur(selectTaxe.getValeur());
                        taxe.setOperation(selectTaxe.getOperation());
                        taxe.setTypeTaxe(selectTaxe.getTypeTaxe());
                        taxe.setApresTva(selectTaxe.isApresTva());
                        taxe.setMontant(taxe.getValeur());
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, listTaxeFacture)) {
                            listTaxeFacture.add(taxe);
                        }
                    }
                }
            }
            calculerTotalTaxe(listTaxeFacture);
            updateListTaxe(listTaxeFacture);
            selectedListParametrageTaxe = new ArrayList<>();
        }

    }

    public void deleteTaxe() {
        if (selectedSingleTaxe != null) {
            if ((selectedSingleTaxe.getTypeTaxe().trim().equals("0")) && (!selectedSingleTaxe.isApresTva())) {
                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }
            } //apresTva
            else if ((selectedSingleTaxe.getTypeTaxe().trim().equals("0")) && (selectedSingleTaxe.isApresTva())) {

                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }
                //valeurs fixe avant tva
            } else if ((selectedSingleTaxe.getTypeTaxe().trim().equals("1")) && (!selectedSingleTaxe.isApresTva())) {

                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }

            } //valeur fixe apres tva
            else if ((selectedSingleTaxe.getTypeTaxe().trim().equals("1")) && (selectedSingleTaxe.isApresTva())) {

                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }
            }
            selected.getListsTaxe().remove(selectedSingleTaxe);
            calculerTotalTaxe(selected.getListsTaxe());
        }
    }

    public void deleteTaxeEdit() {
        if (selectedSingleTaxe != null) {
            if ((selectedSingleTaxe.getTypeTaxe().trim().equals("0")) && (!selectedSingleTaxe.isApresTva())) {
                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }
            } //apresTva
            else if ((selectedSingleTaxe.getTypeTaxe().trim().equals("0")) && (selectedSingleTaxe.isApresTva())) {

                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }
                //valeurs fixe avant tva
            } else if ((selectedSingleTaxe.getTypeTaxe().trim().equals("1")) && (!selectedSingleTaxe.isApresTva())) {

                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }

            } //valeur fixe apres tva
            else if ((selectedSingleTaxe.getTypeTaxe().trim().equals("1")) && (selectedSingleTaxe.isApresTva())) {

                if (selectedSingleTaxe.getOperation().equals("+")) {
                    selected.setTotalTTC((selected.getTotalTTC()).subtract(selectedSingleTaxe.getMontant()));
                } else {
                    selected.setTotalTTC((selected.getTotalTTC()).add(selectedSingleTaxe.getMontant()));
                }
            }
            listTaxeFacture.remove(selectedSingleTaxe);
            listTaxeSuppresssion.add(selectedSingleTaxe);
            updateListTaxe(listTaxeFacture);

        }
    }

    public void updateListTaxe(List<TaxesFacture> list) {
        selected.setTotalTTC(selected.getMontantTTC());


        if (!list.isEmpty()) {
            for (TaxesFacture item : list) {


                if ((item.getTypeTaxe().trim().equals("0")) && (!item.isApresTva())) {
                    if (item.getOperation().trim().equals("+")) {
                        item.setMontant((selected.getMontantHT().multiply(item.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (item.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(item.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(item.getMontant()));
                        }
                    }
                } //apresTva
                else if ((item.getTypeTaxe().trim().equals("0")) && (item.isApresTva())) {
                    item.setMontant((selected.getMontantTTC().multiply(item.getValeur())).divide(BigDecimal.valueOf(100)));
                    if (item.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(item.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(item.getMontant()));
                    }
                    //valeurs fixe avant tva
                } else if ((item.getTypeTaxe().trim().equals("1")) && (!item.isApresTva())) {
                    if (item.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(item.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(item.getMontant()));
                    }
                } //valeur fixe apres tva
                else if ((item.getTypeTaxe().trim().equals("1")) && (item.isApresTva())) {
                    if (item.getOperation().equals("+")) {
                        System.err.println("getMontant : " + item.getMontant());
                        selected.setTotalTTC((selected.getTotalTTC()).add(item.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(item.getMontant()));
                    }
                }
            }

        }
        calculerTotalTaxe(list);

    }

    public void initTaxeFacture(List<ParametrageTaxe> listParametrageTaxeEntreprise) {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantHT());
            selected.setTotalTTC(selected.getMontantTTC());
        }
        if (listParametrageTaxeEntreprise != null && !listParametrageTaxeEntreprise.isEmpty()) {

            for (ParametrageTaxe selectedTaxe : listParametrageTaxeEntreprise) {
                if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (!selectedTaxe.isApresTva())) {
                    TaxesFacture taxe = new TaxesFacture();
                    taxe.setParametrageTaxe(selectedTaxe);
                    taxe.setValeur(selectedTaxe.getValeur());
                    taxe.setOperation(selectedTaxe.getOperation());
                    taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                    taxe.setApresTva(selectedTaxe.isApresTva());
                    taxe.setMontant((selected.getMontantHT().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                    if (taxe.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                    }

                    selected.getListsTaxe().add(taxe);

                } //apresTva
                else if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (selectedTaxe.isApresTva())) {
                    TaxesFacture taxe = new TaxesFacture();
                    taxe.setParametrageTaxe(selectedTaxe);
                    taxe.setValeur(selectedTaxe.getValeur());
                    taxe.setOperation(selectedTaxe.getOperation());
                    taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                    taxe.setApresTva(selectedTaxe.isApresTva());
                    taxe.setMontant((selected.getMontantTTC().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                    if (taxe.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                    }
                    selected.getListsTaxe().add(taxe);

                    //valeurs fixe avant tva
                } else if ((selectedTaxe.getTypeTaxe().trim().equals("1")) && (!selectedTaxe.isApresTva())) {
                    TaxesFacture taxe = new TaxesFacture();
                    taxe.setParametrageTaxe(selectedTaxe);
                    taxe.setValeur(selectedTaxe.getValeur());
                    taxe.setOperation(selectedTaxe.getOperation());
                    taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                    taxe.setApresTva(selectedTaxe.isApresTva());
                    taxe.setMontant(taxe.getValeur());
                    if (taxe.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                    }
                    selected.getListsTaxe().add(taxe);

                } //valeur fixe apres tva
                else if ((selectedTaxe.getTypeTaxe().trim().equals("1")) && (selectedTaxe.isApresTva())) {
                    TaxesFacture taxe = new TaxesFacture();
                    taxe.setParametrageTaxe(selectedTaxe);
                    taxe.setValeur(selectedTaxe.getValeur());
                    taxe.setOperation(selectedTaxe.getOperation());
                    taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                    taxe.setApresTva(selectedTaxe.isApresTva());
                    taxe.setMontant(taxe.getValeur());
                    if (taxe.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                    }
                    selected.getListsTaxe().add(taxe);
                }

            }
            calculerTotalTaxe(selected.getListsTaxe());
            listParametrageTaxeEntreprise = new ArrayList<>();
        }

    }

    public String creerAvoir() {
        if (selectedAvoirVente.getMotifAvoir() != null && selectedAvoirVente.getMotifAvoir().getId() != 0) {
// on ne depasse pas le montant de la facture
            if (selectedAvoirVente.getMontantTTC().compareTo(selected.getMontantTTC()) != 1) {

                selectedAvoirVente.setCodeClient(selected.getCodeClient());
                selectedAvoirVente.setIdClient(selected.getIdClient());
                selectedAvoirVente.setLibelleClient(selected.getLibelleClient());

                selectedAvoirVente.setDateCreation(new Date());
                selectedAvoirVente.setDateAvoir(new Date());
                selectedAvoirVente.setIdFacture(selected.getId());
                selectedAvoirVente.setNumeroFacture(selected.getNumero());
                selectedAvoirVente.setSupprimer(false);

                List<PrefixAvoirVente> listPrefixAvoirVente = new ArrayList<>();
                listPrefixAvoirVente = ejbFacadePrefixAvoirVente.findAll();
                PrefixAvoirVente prefixAvoirVente = null;
                if (listPrefixAvoirVente != null && !listPrefixAvoirVente.isEmpty()) {
                    prefixAvoirVente = listPrefixAvoirVente.get(0);
                }

                if (prefixAvoirVente != null) {
                    String compteur = FonctionsString.retourMotSelonLongeur(prefixAvoirVente.getCompteur() + "", 6);
                    selectedAvoirVente.setNumero(prefixAvoirVente.getLibellePrefixe() + compteur);

                    if (ejbFacadeAvoirVente.verifierUniqueNumero(selectedAvoirVente.getNumero()) == false) {

                        // retour de marchandises
                        if (selectedAvoirVente.getTypeAvoir() == 1) {
                            // en cas retourn de marchandise il faut qu'il y ait au moins un article retournée

                            if (verifQuantiteAvoir() && nombreDeArticleRetournee < selected.getListeLigneFactures().size()) {
                                List<LigneAvoirVente> listLigneAvoirVenteTemps = new ArrayList<>();
                                listLigneAvoirVenteTemps = selectedAvoirVente.getListLigneAvoirVentes();
                                selectedAvoirVente.setListLigneAvoirVentes(null);
                                //on creer l'avoir
                                ejbFacadeAvoirVente.create(selectedAvoirVente);

                                selectedAvoirVente.setMontantHT(BigDecimal.ZERO);
                                selectedAvoirVente.setMontantTVA(BigDecimal.ZERO);
                                selectedAvoirVente.setMontantTTC(BigDecimal.ZERO);

                                for (LigneAvoirVente detailAvoirVente : listLigneAvoirVenteTemps) {
                                    if (detailAvoirVente.getQuantite().compareTo(BigDecimal.ZERO) > 0) {

                                        detailAvoirVente.setTotalHT(detailAvoirVente.getPrixUnitaireApresRemise().multiply(detailAvoirVente.getQuantite()));

                                        if (selected.isAssujettiTVA()) {
                                            detailAvoirVente.setTotalTVA(((detailAvoirVente.getTotalHT().multiply(detailAvoirVente.getTvaArticle()))).divide(BigDecimal.valueOf(100)));
                                            detailAvoirVente.setTotalTTC(detailAvoirVente.getTotalHT().add(detailAvoirVente.getTotalTVA()));

                                        } else {
                                            detailAvoirVente.setTotalTVA(BigDecimal.ZERO);
                                            detailAvoirVente.setTotalTTC(detailAvoirVente.getTotalHT());

                                        }
                                        selectedAvoirVente.setMontantHT(selectedAvoirVente.getMontantHT().add(detailAvoirVente.getTotalHT()));
                                        selectedAvoirVente.setMontantTTC(selectedAvoirVente.getMontantTTC().add(detailAvoirVente.getTotalTTC()));

                                        detailAvoirVente.setAvoirVente(selectedAvoirVente);
                                    }

                                }

                                updateFactureAvoir();

                                //selectedAvoirVente.setReste(selectedAvoirVente.getMontantTTC());
                                // on met a jour les montants
                                ejbFacadeLigneAvoirVente.create(listLigneAvoirVenteTemps);
                                ejbFacadeAvoirVente.edit(selectedAvoirVente);

                            } else {
                                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("QuantiteError")));
                                return null;
                            }

                        } // remise sur produit/facture
                        else if (selectedAvoirVente.getTypeAvoir() == 0) {

                            ejbFacadeAvoirVente.create(selectedAvoirVente);

                        } // erreur facture
                        else if (selectedAvoirVente.getTypeAvoir() == 2) {

                            ejbFacadeAvoirVente.create(selectedAvoirVente);
                            updateFactureAvoir();

                        }

                        prefixAvoirVente.setCompteur(prefixAvoirVente.getCompteur() + 1);
                        ejbFacadePrefixAvoirVente.edit(prefixAvoirVente);

                    } else {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getNumero() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                        return null;
                    }

                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe") + " " + ResourceBundle.getBundle("/Bundle").getString("Avoir")));
                    return null;
                }

                selected.setAvoir(true);
                selected.setIdAvoir(selectedAvoirVente.getId());
                selected.setTypeAvoir(selectedAvoirVente.getTypeAvoir());
                selected.setNumeroAvoir(selectedAvoirVente.getNumero());
                getFacade().edit(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("MontantAvoirSuperieur")));
                return null;
            }
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("MsgErreuMotifAvoir")));
            return null;
        }
    }

    private void updateFactureAvoir() {

        // pas de paiement
        if (selected.getEtat() == 0) {
            // simple avoir
            // le paiement de la facture n'est plus permis

            selectedAvoirVente.setReste(BigDecimal.ZERO);
            // facture annulée
            selected.setEtat(2);
            //selected.setReste(BigDecimal.ZERO);
        } // paiement total
        else if (selected.getEtat() == 1) {

            selectedAvoirVente.setReste(selectedAvoirVente.getMontantTTC());

        } // paiement partiel
        else if (selected.getEtat() == 3) {

            // si avoir total
            if (selectedAvoirVente.getMontantTTC().compareTo(selected.getMontantTTC()) == 0) {

                // si paiement > au montant TTC je rembourse tout sans la taxe (exp timbre fiscale)
                if (selected.getTotalPaiement().compareTo(selectedAvoirVente.getMontantTTC()) == 1) {
                    selectedAvoirVente.setReste(selected.getMontantTTC());

                } else {
                    // je rembourse ce qui a été payé
                    selectedAvoirVente.setReste(selected.getTotalPaiement());
                }

                // la facture est annulé // plus de paiement possible
                selected.setEtat(2);

            } // avoir partiel => des payements peuvent etres fait et la taxe (exp timbre fiscale) doit etre payé
            else {

                // deux cas soit je dois rembourser de l'argent / soit il dois payée ce qui reste aprés déduction de l'avoir
                BigDecimal totalNormalementApayer = selected.getTotalTTC().subtract(selectedAvoirVente.getMontantTTC());

                // il me dois toujours de l'argent méme aprés avoir (le timbre fiscale dois etre payée)
                if (totalNormalementApayer.compareTo(selected.getTotalPaiement()) == 1) {
                    selected.setReste(totalNormalementApayer.subtract(selected.getTotalPaiement()));
                    selectedAvoirVente.setReste(BigDecimal.ZERO);
                } else {

                    selected.setEtat(2);
                    // je dois le rembourser
                    selectedAvoirVente.setReste(selected.getTotalPaiement().subtract(totalNormalementApayer));
                }

            }

        }

    }

    private boolean verifQuantiteAvoir() {
        boolean correct = true;
        nombreDeArticleRetournee = 0;
        for (LigneAvoirVente detailAvoirVente : selectedAvoirVente.getListLigneAvoirVentes()) {

            // nous allons compter le nombre d'articles qui ne sont pas prit en compte dans l'avoir
            // si ce nombre est egale aux nombre total de ligne facture cela veut dire qu'aucun article ne fera le sujet de retour
            // donc cela ne sra pas permis en cas d'avoir de type retour de marchandise
            // il faut au moins un article retourné
            if (detailAvoirVente.getQuantite().compareTo(BigDecimal.ZERO) == 0) {
                nombreDeArticleRetournee = nombreDeArticleRetournee + 1;
            }
            if (detailAvoirVente.getQuantite().compareTo(BigDecimal.ZERO) == -1 || detailAvoirVente.getQuantite().compareTo(detailAvoirVente.getQuantiteFacture()) == 1) {
                correct = false;
                break;
            }
        }

        return correct;

    }

    public void initChamps() {
        listSelectedBonLivraison = new ArrayList<>();
        changedFactureBL();
    }

    public void changeMontant() {
        if (selectedAvoirVente.getTypeAvoir() != null && selectedAvoirVente.getTypeAvoir() == 0 && selectedAvoirVente.getRemise() != null && selectedAvoirVente.getRemise().compareTo(BigDecimal.ZERO) != -1) {
            BigDecimal deductionHT = (selected.getTotalHT().multiply(selectedAvoirVente.getRemise())).divide(BigDecimal.valueOf(100));
            // les taxes n'entre pas
            BigDecimal deductionTTC = (selected.getMontantTTC().multiply(selectedAvoirVente.getRemise())).divide(BigDecimal.valueOf(100));
//            BigDecimal newValeurHT = (selected.getTotalTTC()).subtract(deductionHT);
//            BigDecimal newValeurTTC = (selected.getTotalTTC()).subtract(deductionTTC);

            selectedAvoirVente.setMontantHT(deductionHT);
            selectedAvoirVente.setMontantTTC(deductionTTC);
            selectedAvoirVente.setReste(deductionTTC);
            //calculReste();
        }

    }

    public void initParametre() {
        selectedAvoirVente.setMontantTTC(selected.getMontantTTC());
        selectedAvoirVente.setRemise(BigDecimal.ZERO);
        selectedAvoirVente.setReste(BigDecimal.ZERO);
    }

    private void createRetour(List<LigneFacture> listeLigneFactures, Facture facture) {

        Retour retour = new Retour();

        retour.setCodeClient(facture.getCodeClient());
        retour.setLibelleClient(facture.getLibelleClient());
        retour.setIdClient(facture.getIdClient());

        retour.setEtat(0);
        retour.setOrigineRetour(0);
        retour.setAssujettiTVA(facture.isAssujettiTVA());
        retour.setNumeroFactureBL(facture.getNumero());
        retour.setIdFactureBL(facture.getId());
        retour.setDateCreation(new Date());
        retour.setTotalHT(BigDecimal.ZERO);
        retour.setTotalTTC(BigDecimal.ZERO);

        ejbFacadeRetour.create(retour);
        List<LigneRetour> listLigneRetourTemps = new ArrayList<>();

        for (LigneFacture ligneFacture : listeLigneFactures) {
            LigneRetour ligneRetour = new LigneRetour();

            ligneRetour.setCodeArticle(ligneFacture.getCodeArticle());
            ligneRetour.setIdArticle(ligneFacture.getIdArticle());
            ligneRetour.setLibelleArticle(ligneFacture.getLibelleArticle());

            ligneRetour.setQuantite(ligneFacture.getQuantite());
            ligneRetour.setQuantiteCasse(BigDecimal.ZERO);
            ligneRetour.setTotalHT(BigDecimal.ZERO);
            ligneRetour.setTotalTTC(BigDecimal.ZERO);

            ligneRetour.setPrixUnitaireHT(BigDecimal.ZERO);
            ligneRetour.setRetour(retour);

            listLigneRetourTemps.add(ligneRetour);
        }

        if (facture.getOrigine() == 2) {

            for (BonLivraison bonLivraison : listSelectedBonLivraison) {

                // les bls qui ont fait sujet de retour ne seront pas pris en compte
                // ceux validé normalement
                for (LigneBonLivraison ligneBonLivraison : bonLivraison.getListeLigneBonLivraisons()) {

                    LigneRetour ligneRetour = new LigneRetour();
                    ligneRetour.setIdArticle(ligneBonLivraison.getIdArticle());

                    int index = listLigneRetourTemps.indexOf(ligneRetour);

                    if (index > -1) {
                        listLigneRetourTemps.get(index).setQuantite(listLigneRetourTemps.get(index).getQuantite().add(ligneBonLivraison.getQuantite()));
                    } else {

                        //   ligneFacture.setId(ligneBonLivraison.getId());
                        ligneRetour.setCodeArticle(ligneBonLivraison.getCodeArticle());
                        ligneRetour.setLibelleArticle(ligneBonLivraison.getLibelleArticle());
                        ligneRetour.setQuantite(ligneBonLivraison.getQuantite());

                        ligneRetour.setPrixUnitaireHT(ligneBonLivraison.getPrixUnitaireHT());
                        ligneRetour.setRemise(ligneBonLivraison.getRemise());
                        ligneRetour.setPrixUnitaireApresRemise(ligneBonLivraison.getPrixUnitaireApresRemise());
                        ligneRetour.setQuantiteCasse(BigDecimal.ZERO);
                        ligneRetour.setTotalHT(BigDecimal.ZERO);
                        ligneRetour.setTotalTTC(BigDecimal.ZERO);

                        ligneRetour.setPrixUnitaireHT(BigDecimal.ZERO);
                        ligneRetour.setRetour(retour);
                        listLigneRetourTemps.add(ligneRetour);
                    }

                }
            }

        }// end BL

        ejbFacadeLigneRetour.create(listLigneRetourTemps);

    }

    public void generationSelectedPDF() throws IOException, DocumentException {

        if (selectedSingle != null) {

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String path = LireParametrage.returnValeurParametrage("urlDocumentNonPersistant") + "Facture_"+selectedSingle.getNumero()+".pdf";
            String image = LireParametrage.returnValeurParametrage("urlUmploadPhoto") + utilisateur.getEntreprise().getLogo();

            if (utilisateur.getEntreprise().getLogo().contains("/resources/images/") == true) {

                File fileImage = new File(image);
                try {
                    if (fileImage.exists() == false) {
                        image = ec.getRealPath("resources/images") + "/company.png";
                    }
                } catch (Exception e) {
                    image = ec.getRealPath("resources/images") + "/company.png";
                }
            }

            // addHeaderInfo
            String dateFacture = TraitementDate.returnDate(selectedSingle.getDateCreation());
            String numeroFacture = selectedSingle.getNumero();

            //addCompanyCustomerInfo
            ArrayList<String> entrepriseInfos = new ArrayList();
            entrepriseInfos.add(utilisateur.getEntreprise().getLibelle());
            entrepriseInfos.add(utilisateur.getEntreprise().getLibelleGouvernorat() + ", " + utilisateur.getEntreprise().getLibelleDelegation());
            entrepriseInfos.add(utilisateur.getEntreprise().getAdresse());
            entrepriseInfos.add(utilisateur.getEntreprise().getTelephone());
            entrepriseInfos.add(utilisateur.getEntreprise().getEmail());

            Client client = ejbFacadeClient.find(selectedSingle.getIdClient());
            ArrayList<String> clientInfos = new ArrayList();
            clientInfos.add(client.getLibelle());
            clientInfos.add(client.getLibelleGouvernorat() + ", " + client.getLibelleDelegation());
            clientInfos.add(client.getAdresse());
            clientInfos.add(client.getGsm());
            clientInfos.add(client.getEmail());

            //addInvoiceInfo
            ArrayList<String> ligneFactureEntete = new ArrayList();

            ligneFactureEntete.add("Reference");
            ligneFactureEntete.add("Produit");
            ligneFactureEntete.add("TVA");
            ligneFactureEntete.add("Quantite");
            ligneFactureEntete.add("Prix UT");
            ligneFactureEntete.add("Total HT");

            ArrayList<ArrayList<String>> ligneFactures = new ArrayList<ArrayList<String>>();

            ArrayList<String> ligneFactureInfo;

            for (int i = 0; i < selectedSingle.getListeLigneFactures().size(); i++) {

                ligneFactureInfo = new ArrayList();
                //0:left 1: linea left 2:center 3:linea right 4:right
                ligneFactureInfo.add("1:" + selectedSingle.getListeLigneFactures().get(i).getCodeArticle());
                ligneFactureInfo.add("1:" + selectedSingle.getListeLigneFactures().get(i).getLibelleArticle());
                ligneFactureInfo.add("3:" + selectedSingle.getListeLigneFactures().get(i).getTvaArticle()+"%");
                ligneFactureInfo.add("3:" + selectedSingle.getListeLigneFactures().get(i).getQuantite());
                ligneFactureInfo.add("3:" + selectedSingle.getListeLigneFactures().get(i).getPrixUnitaireApresRemise());
                ligneFactureInfo.add("3:" + selectedSingle.getListeLigneFactures().get(i).getTotalHT());
                ligneFactures.add(i, ligneFactureInfo);
            }

            //bonLivraison
            if (selectedSingle.getOrigine() == 2) {

                listSelectedBonLivraison = ejbFacadeBonLivraison.findAllNative(" where o.BLiv_IdDocumentTransform = " + selectedSingle.getId());
                int i = selectedSingle.getListeLigneFactures().size();
                for (BonLivraison bonLivraison : listSelectedBonLivraison) {

                    for (LigneBonLivraison ligneBonLivraison : bonLivraison.getListeLigneBonLivraisons()) {

                        ligneFactureInfo = new ArrayList();
                        ligneFactureInfo.add("1:" + ligneBonLivraison.getCodeArticle());
                        ligneFactureInfo.add("1:" + ligneBonLivraison.getLibelleArticle());
                        ligneFactureInfo.add("3:" + ligneBonLivraison.getTvaArticle()+"%");
                        ligneFactureInfo.add("3:" + ligneBonLivraison.getQuantite());
                        ligneFactureInfo.add("3:" + ligneBonLivraison.getPrixUnitaireApresRemise());
                        ligneFactureInfo.add("3:" + ligneBonLivraison.getTotalHT());
                        ligneFactures.add(i, ligneFactureInfo);
                        i++;

                    }

                }

            }

            //addInvoiceSummarize
            ArrayList<String> factureSummarizeInfos = new ArrayList();

            factureSummarizeInfos.add("Total HT" + " : " + selectedSingle.getTotalHT());
            factureSummarizeInfos.add("Total TVA" + " : " + selectedSingle.getTotalTVA());
            factureSummarizeInfos.add("Total Taxe" + " : " + selectedSingle.getTotalTaxe());
            factureSummarizeInfos.add("Total TTC" + " : " + selectedSingle.getTotalTTC());

            GenerationPdf.generationPdf(image, path ,"Facture",numeroFacture, dateFacture,entrepriseInfos,clientInfos,ligneFactureEntete,ligneFactures,factureSummarizeInfos);

            File file = new File(path);
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename="+"Facture_"+numeroFacture+".pdf");
            response.setContentLength((int) file.length());
            ServletOutputStream out = null;
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                out = response.getOutputStream();
                int i = 0;
                while ((i = input.read(buffer)) != -1) {
                    out.write(buffer);
                    out.flush();
                }
                FacesContext.getCurrentInstance().responseComplete();
                FacesContext.getCurrentInstance().renderResponse();
            } catch (IOException err) {
                System.out.println("/Generate PDF Error : -> " + err.getMessage());
            }finally{
                        selectedSingle = null;
            listSelectedBonLivraison=null;
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }

    public StreamedContent generationSelectedPDF2() {
        StreamedContent file = null;
        try {
            String path = LireParametrage.returnValeurParametrage("urlDocumentNonPersistant") + "FirstPdf.pdf";
            File file2 = new File(path);
            if (file2.exists() == true) {

                String extenstion = file2.getName().substring(file2.getName().lastIndexOf(".") + 1);
                if (extenstion.equals("pdf") || extenstion.equals("jpeg") || extenstion.equals("jpg") || extenstion.equals("gif")) {
                    file = new DefaultStreamedContent(new FileInputStream(file2), "application/pdf", "FirstPdf" + "." + extenstion);
                    System.out.println("com.magma.controller.FactureController.generationSelectedPDF2()" + file);
                }

            }
            // String[] extention = selected.getFile().split(".");
            //selected.getLibelle() + "." + extention[1]

        } catch (Exception e) {
            System.err.println("PubliciteController : erreur downoald getMedia" + e.getMessage());
        }
        return file;

    }

    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                items = getFacade().searchAllNative(debut, fin, etatFacture, origineFacture, client != null ? client.getId() : null, avoir, retour);

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreuPeriode")));
            }

        } catch (Exception e) {

        }
    }

    public void changedArticle() {
        listArticles = new ArrayList<>();
        listArticles = categorie.getArticles();

    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BigDecimal getOldPrix() {
        return oldPrix;
    }

    public void setOldPrix(BigDecimal oldPrix) {
        this.oldPrix = oldPrix;
    }

    public BigDecimal getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(BigDecimal oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    public double getOldRemise() {
        return oldRemise;
    }

    public void setOldRemise(double oldRemise) {
        this.oldRemise = oldRemise;
    }

    public Integer getOrigineFacture() {
        return origineFacture;
    }

    public void setOrigineFacture(Integer origineFacture) {
        this.origineFacture = origineFacture;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public CategorieClient getCategorieClient() {
        return categorieClient;
    }

    public void setCategorieClient(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
    }

    public List<CategorieClient> getListCategorieClient() {
        return listCategorieClient;
    }

    public void setListCategorieClient(List<CategorieClient> listCategorieClient) {
        this.listCategorieClient = listCategorieClient;
    }

    public LigneFacture getSelectedLigneFacture() {
        return selectedLigneFacture;
    }

    public void setSelectedLigneFacture(LigneFacture selectedLigneFacture) {
        this.selectedLigneFacture = selectedLigneFacture;
    }

    public LigneFacture getSelectedLigneFactureSingle() {
        return selectedLigneFactureSingle;
    }

    public void setSelectedLigneFactureSingle(LigneFacture selectedLigneFactureSingle) {
        this.selectedLigneFactureSingle = selectedLigneFactureSingle;
    }

    public LigneFacture getSelectedLigneFactureModif() {
        return selectedLigneFactureModif;
    }

    public void setSelectedLigneFactureModif(LigneFacture selectedLigneFactureModif) {
        this.selectedLigneFactureModif = selectedLigneFactureModif;
    }

    public boolean isAnnulation() {
        return annulation;
    }

    public void setAnnulation(boolean annulation) {
        this.annulation = annulation;
    }

    public List<BonLivraison> getListSelectedBonLivraison() {
        return listSelectedBonLivraison;
    }

    public void setListSelectedBonLivraison(List<BonLivraison> listSelectedBonLivraison) {
        this.listSelectedBonLivraison = listSelectedBonLivraison;
    }

    public TaxesFacture getSelectedTaxe() {
        return selectedTaxe;
    }

    public void setSelectedTaxe(TaxesFacture selectedTaxe) {
        this.selectedTaxe = selectedTaxe;
    }

    public List<ParametrageTaxe> getSelectedListParametrageTaxe() {
        return selectedListParametrageTaxe;
    }

    public void setSelectedListParametrageTaxe(List<ParametrageTaxe> selectedListParametrageTaxe) {
        this.selectedListParametrageTaxe = selectedListParametrageTaxe;
    }

    public List<ParametrageTaxe> getListParametrageTaxeEntreprise() {
        return listParametrageTaxeEntreprise;
    }

    public void setListParametrageTaxeEntreprise(List<ParametrageTaxe> listParametrageTaxeEntreprise) {
        this.listParametrageTaxeEntreprise = listParametrageTaxeEntreprise;
    }

    public TaxesFacture getSelectedSingleTaxe() {
        return selectedSingleTaxe;
    }

    public void setSelectedSingleTaxe(TaxesFacture selectedSingleTaxe) {
        this.selectedSingleTaxe = selectedSingleTaxe;
    }

    public List<TaxesFacture> getListTaxeFacture() {
        return listTaxeFacture;
    }

    public void setListTaxeFacture(List<TaxesFacture> listTaxeFacture) {
        this.listTaxeFacture = listTaxeFacture;
    }

    public AvoirVente getSelectedAvoirVente() {
        return selectedAvoirVente;
    }

    public void setSelectedAvoirVente(AvoirVente selectedAvoirVente) {
        this.selectedAvoirVente = selectedAvoirVente;
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

    public Integer getEtatFacture() {
        return etatFacture;
    }

    public void setEtatFacture(Integer etatFacture) {
        this.etatFacture = etatFacture;
    }

    public SelectItem[] getItemsAvailableSelectManyBonLivraison() {
        return JsfUtil.getSelectItems(listBonLivraison, false);

    }

    public SelectItem[] getItemsAvailableSelectOneDevis() {

        return JsfUtil.getSelectItems(listDeviss, true);

    }
    
        public SelectItem[] getItemsAvailableSelectOneCommande() {

        return JsfUtil.getSelectItems(listCommandes, true);
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

    public SelectItem[] getItemsAvailableSelectOneArticle() {
        if (listArticles == null) {
            listArticles = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listArticles, true);
    }

    public Facture getFacture(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isAvoir() {
        return avoir;
    }

    public void setAvoir(boolean avoir) {
        this.avoir = avoir;
    }

    public boolean isRetour() {
        return retour;
    }

    public void setRetour(boolean retour) {
        this.retour = retour;
    }

    @FacesConverter(forClass = Facture.class)
    public static class FactureControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FactureController controller = (FactureController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "factureController");
            return controller.getFacture(getKey(value));
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
            if (object instanceof Facture) {
                Facture o = (Facture) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Facture.class.getName());
            }
        }

    }

}
