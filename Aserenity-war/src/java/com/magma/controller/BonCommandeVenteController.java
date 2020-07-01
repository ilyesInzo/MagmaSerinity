package com.magma.controller;

import com.itextpdf.text.DocumentException;
import com.magma.bibliotheque.FonctionsMathematiques;
import com.magma.bibliotheque.FonctionsString;
import com.magma.bibliotheque.GenerationPdf;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Article;
import com.magma.entity.BonLivraison;
import com.magma.entity.Categorie;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.BonCommandeVente;
import com.magma.entity.Devis;
import com.magma.entity.Facture;
import com.magma.entity.LigneBonLivraison;
import com.magma.entity.LigneBonCommandeVente;
import com.magma.entity.LigneDevis;
import com.magma.entity.LigneFacture;
import com.magma.entity.ParametrageEntreprise;
import com.magma.entity.ParametrageTaxe;
import com.magma.entity.PrefixBonLivraison;
import com.magma.entity.PrefixBonCommandeVente;
import com.magma.entity.PrefixFacture;
import com.magma.entity.TaxesBonCommandeVente;
import com.magma.entity.TaxesDevis;
import com.magma.entity.TaxesFacture;
import com.magma.entity.Utilisateur;
import com.magma.session.BonLivraisonFacadeLocal;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.BonCommandeVenteFacadeLocal;
import com.magma.session.DevisFacadeLocal;
import com.magma.session.FactureFacadeLocal;
import com.magma.session.LigneBonLivraisonFacadeLocal;
import com.magma.session.LigneBonCommandeVenteFacadeLocal;
import com.magma.session.LigneDevisFacadeLocal;
import com.magma.session.LigneFactureFacadeLocal;
import com.magma.session.ParametrageTaxeFacadeLocal;
import com.magma.session.PrefixBonCommandeVenteFacadeLocal;
import com.magma.session.PrefixBonLivraisonFacadeLocal;
import com.magma.session.PrefixFactureFacadeLocal;
import com.magma.session.TaxesBonCommandeVenteFacadeLocal;
import com.magma.session.TaxesFactureFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "bonCommandeVenteController")
@SessionScoped
public class BonCommandeVenteController implements Serializable {

    private BonCommandeVente selected;
    private BonCommandeVente selectedSingle;
    private List<BonCommandeVente> items = null;
    private Utilisateur utilisateur;
    @EJB
    private BonCommandeVenteFacadeLocal ejbFacade;
    @EJB
    private PrefixBonCommandeVenteFacadeLocal ejbFacadePrefixBonCommandeVente;
    @EJB
    private LigneBonCommandeVenteFacadeLocal ejbFacadeLigneBonCommandeVente;
    @EJB
    private CategorieClientFacadeLocal ejbFacadeCatgorieClient;
    private CategorieClient categorieClient = null;

    @EJB
    private ClientFacadeLocal ejbFacadeClient;

    @EJB
    private ParametrageTaxeFacadeLocal ejbFacadeParametrageTaxe;

    @EJB
    private TaxesBonCommandeVenteFacadeLocal ejbFacadeTaxeBonCommandeVente;

    @EJB
    private BonLivraisonFacadeLocal ejbFacadeBonLivraison;
    @EJB
    private PrefixBonLivraisonFacadeLocal ejbFacadePrefixBonLivraison;
    @EJB
    private LigneFactureFacadeLocal ejbFacadeLigneFacture;

    @EJB
    private FactureFacadeLocal ejbFacadeFacture;
    @EJB
    private PrefixFactureFacadeLocal ejbFacadePrefixFacture;
    @EJB
    private LigneBonLivraisonFacadeLocal ejbFacadeLigneBonLivraison;
    @EJB
    private TaxesFactureFacadeLocal ejbFacadeTaxeFacture;

    @EJB
    private LigneDevisFacadeLocal ejbFacadeLigneDevis;
    @EJB
    private DevisFacadeLocal ejbFacadeDevis;

    private List<Client> listClient = null;

    private Categorie categorie;
    private List<Devis> listDeviss = new ArrayList();
    private boolean errorMsg;
    private Long idTemp;
    private BonCommandeVente bonCommandeVente;
    private long idEntreprise = 0;
    private boolean annulation = true;
    private BigDecimal oldPrix;
    private BigDecimal oldQuantity;
    private double oldRemise;
    //private BigDecimal ancienMontantHT;
    private LigneBonCommandeVente selectedLigneBonCommandeVente;
    private LigneBonCommandeVente selectedLigneBonCommandeVenteSingle;
    private LigneBonCommandeVente selectedLigneBonCommandeVenteModif;
    private List<LigneBonCommandeVente> LigneBonCommandeVenteTemps;
    private List<LigneBonCommandeVente> AncienLigneBonCommandeVente;
    private List<CategorieClient> listCategorieClient = null;
    private List<Article> listArticles = null;
    private List<ParametrageTaxe> listParametrageTaxeEntreprise = null;
    private List<ParametrageTaxe> selectedListParametrageTaxe = null;
    private TaxesBonCommandeVente selectedSingleTaxe;
    private TaxesBonCommandeVente selectedTaxe;
    private List<TaxesBonCommandeVente> listTaxeBonCommandeVente = null;
    private List<TaxesBonCommandeVente> listTaxeSuppresssion = null;

    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Integer etatBonCommandeVente;
    private Client client;
    private ParametrageEntreprise parametrageEntreprise = null;

    public BonCommandeVenteController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (bonCommandeVente.getIdEntrepriseSuivi() != null && bonCommandeVente.getIdEntrepriseSuivi() != 0) {
         idEntreprise = bonCommandeVente.getIdEntrepriseSuivi();
         } else {
         idEntreprise = bonCommandeVente.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
            parametrageEntreprise = utilisateur.getEntreprise().getParametrageEntreprise();
            MenuTemplate.menuFonctionnalitesModules("GBonCommandeVente", "MVente", null, utilisateur);
            //MenuTemplate.menuFonctionnalitesModules("GBonCommandeVente", utilisateur);
            /* if (bonCommandeVente.getIdEntrepriseSuivi() != null && bonCommandeVente.getIdEntrepriseSuivi() != 0) {
             idEntreprise = bonCommandeVente.getIdEntrepriseSuivi();
             } else {
             idEntreprise = bonCommandeVente.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../bonCommandeVente/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<BonCommandeVente> getItems() {
        try {
            if (items == null) {
                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.BCVnt_DateBonCommandeVente between '" + debut + "' and '" + fin + "' ";
                items = getFacade().findAllNative(clause + " order by o.BCVnt_DateBonCommandeVente desc");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- BonCommandeVenteController - getItems: " + e.getMessage());
            return null;
        }
    }

    public BonCommandeVente getSelected() {
        return selected;
    }

    public void setSelected(BonCommandeVente selected) {
        this.selected = selected;
    }

    public BonCommandeVente getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(BonCommandeVente selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private BonCommandeVenteFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        selectedLigneBonCommandeVente = null;
        selectedLigneBonCommandeVenteSingle = null;
        selectedLigneBonCommandeVenteModif = null;
        categorie = null;
        //client = null;
        categorieClient = null;

        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            /*annulation = false;
             if (selected.getIdBonCommandeVente() == null && selected.getListEncaissementBonCommandeVentes().isEmpty()) {
             annulation = true;
             }*/
            /*System.err.println(System.getenv("SystemDrive"));
             System.err.println(System.getProperty("user.dir"));
             System.err.println(System.getProperty("user.home"));*/

            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        categorieClient = new CategorieClient();
        selected = new BonCommandeVente();
        selected.setDateBonCommandeVente(new Date());
        errorMsg = false;
        selectedLigneBonCommandeVente = new LigneBonCommandeVente();
        selectedLigneBonCommandeVente.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonCommandeVente.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonCommandeVente.setQuantite(BigDecimal.ZERO);
        selectedLigneBonCommandeVente.setRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeVente.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeVente.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonCommandeVente.setTotalTTC(BigDecimal.ZERO);

        selectedLigneBonCommandeVenteModif = new LigneBonCommandeVente();
        selectedLigneBonCommandeVenteModif.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteModif.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteModif.setQuantite(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteModif.setRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteModif.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteModif.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteModif.setTotalTTC(BigDecimal.ZERO);

        selectedLigneBonCommandeVenteSingle = new LigneBonCommandeVente();
        selectedLigneBonCommandeVenteSingle.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteSingle.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteSingle.setQuantite(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteSingle.setRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteSingle.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteSingle.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonCommandeVenteSingle.setTotalTTC(BigDecimal.ZERO);

        selectedListParametrageTaxe = new ArrayList<>();
        listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");
        selected.setTotalTaxe(BigDecimal.ZERO);
        selected.setTypeVente(0);
        //selected.setEtat(-1);
        categorie = new Categorie();
        initDocumentFields();
        return "Create";
    }

    public String create() {

        /*try {*/
        List<PrefixBonCommandeVente> listPrefixBonCommandeVentes = null;
        PrefixBonCommandeVente prefixBonCommandeVente = null;
        listPrefixBonCommandeVentes = ejbFacadePrefixBonCommandeVente.findAll(" where o.supprimer = 0 ");
        String compteur = "";
        if (listPrefixBonCommandeVentes != null && !listPrefixBonCommandeVentes.isEmpty()) {
            prefixBonCommandeVente = listPrefixBonCommandeVentes.get(0);
            compteur = prefixBonCommandeVente.getLibellePrefixe() + FonctionsString.retourMotSelonLongeur(prefixBonCommandeVente.getCompteur() + "", 6);

        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
            return null;
        }

        errorMsg = getFacade().verifierUniqueNumero(compteur);

        if (errorMsg == false) {
            creationInfo();
            if (selected.getListeLigneBonCommandeVentes() != null && !selected.getListeLigneBonCommandeVentes().isEmpty()) {

                if (selected.getOrigine() == 0) {

                    List<TaxesBonCommandeVente> listTaxesTemps = new ArrayList<>();
                    LigneBonCommandeVenteTemps = selected.getListeLigneBonCommandeVentes();

                    if (parametrageEntreprise.isAppliquerRemiseGlobale()) {
                        recalculerToutMontants();
                    } else {
                        selected.setMontantNet(selected.getTotalHT());
                    }

                    for (int i = LigneBonCommandeVenteTemps.size() - 1; i >= 0; i--) {
                        if (LigneBonCommandeVenteTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 0) {
                            LigneBonCommandeVenteTemps.remove(LigneBonCommandeVenteTemps.get(i));
                        }
                    }

                    if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                        listTaxesTemps = selected.getListsTaxe();
                        selected.setListsTaxe(null);

                    }

                    selected.setNumero(compteur);
                    selected.setListeLigneBonCommandeVentes(null);

                    if (selected.getClient() != null) {
                        selected.setIdClient(selected.getClient().getId());
                        selected.setAssujettiTVA(selected.getClient().isAssujettiTVA());
                        selected.setLibelleClient(selected.getClient().getLibelle());

                    }
                    selected.setReste(selected.getTotalTTC());
                    selected.setTransFormTo(-1);
                    getFacade().create(selected);

                    for (LigneBonCommandeVente ligneBonCommandeVente : LigneBonCommandeVenteTemps) {
                        ligneBonCommandeVente.setBonCommandeVente(selected);
                    }
                    ejbFacadeLigneBonCommandeVente.create(LigneBonCommandeVenteTemps);

                    if (!listTaxesTemps.isEmpty()) {
                        for (TaxesBonCommandeVente listTaxesTemp : listTaxesTemps) {
                            listTaxesTemp.setBonCommandeVente(selected);
                            ejbFacadeTaxeBonCommandeVente.create(listTaxesTemp);
                        }
                    }

                    prefixBonCommandeVente.setCompteur(prefixBonCommandeVente.getCompteur() + 1);
                    ejbFacadePrefixBonCommandeVente.edit(prefixBonCommandeVente);

                    return prepareList();

                } else if (selected.getOrigine() == 1) {

                    selected.setNumero(compteur);

                    createBonCommandeFromDevis();

                    selected.getDevis().setIdDocumentTransform(selected.getId());
                    selected.getDevis().setNumeroDocumentTransform(selected.getNumero());
                    selected.getDevis().setTransFormTo(0);
                    selected.getDevis().setEtat(4);
                    ejbFacadeDevis.edit(selected.getDevis());

                    prefixBonCommandeVente.setCompteur(prefixBonCommandeVente.getCompteur() + 1);
                    ejbFacadePrefixBonCommandeVente.edit(prefixBonCommandeVente);

                    return prepareList();

                } else {

                    return null;

                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("DetailCommandeNull")));
                return null;
            }

        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
            return null;
        }

        /*} catch (Exception e) {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
         System.out.println("Erreur- BonCommandeVenteController - create: " + e.getMessage());
         return null;
         }*/
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

            selectedLigneBonCommandeVente = new LigneBonCommandeVente();
            selectedLigneBonCommandeVenteModif = new LigneBonCommandeVente();
            selectedLigneBonCommandeVenteSingle = new LigneBonCommandeVente();

            selectedListParametrageTaxe = new ArrayList<>();
            listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");
            selectedTaxe = new TaxesBonCommandeVente();

            listTaxeBonCommandeVente = new ArrayList<>();
            listTaxeBonCommandeVente = selected.getListsTaxe();
            listTaxeSuppresssion = new ArrayList<>();
            categorie = new Categorie();

            Client client = new Client();
            client.setId(selected.getIdClient());
            client.setAssujettiTVA(selected.isAssujettiTVA());
            client.setLibelle(selected.getLibelleClient());
            selected.setClient(client);

            //ancienMontantHT = selected.getMontantHT();
            for (LigneBonCommandeVente ligneBonCommandeVente : selected.getListeLigneBonCommandeVentes()) {

                Article article = new Article();
                article.setId(ligneBonCommandeVente.getIdArticle());
                article.setCode(ligneBonCommandeVente.getCodeArticle());
                article.setLibelle(ligneBonCommandeVente.getLibelleArticle());
                ligneBonCommandeVente.setArticle(article);

            }

            AncienLigneBonCommandeVente = new ArrayList<LigneBonCommandeVente>(selected.getListeLigneBonCommandeVentes());

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {

            if (selected.getListeLigneBonCommandeVentes() != null && !selected.getListeLigneBonCommandeVentes().isEmpty()) {
                editionInfo();
                List<TaxesBonCommandeVente> listTaxesTemps = new ArrayList<>();

                if (parametrageEntreprise.isAppliquerRemiseGlobale()) {
                    recalculerToutMontants();
                } else {
                    selected.setMontantNet(selected.getTotalHT());
                }

                if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                    listTaxesTemps = selected.getListsTaxe();
                    selected.setListsTaxe(null);
                }

                LigneBonCommandeVenteTemps = new ArrayList<LigneBonCommandeVente>(selected.getListeLigneBonCommandeVentes());
                selected.setListeLigneBonCommandeVentes(null);
                getFacade().edit(selected);

                //LigneBonCommandeVenteTemps = selected.getListeLigneBonCommandeVentes();
                for (LigneBonCommandeVente ligneBonCommandeVente : AncienLigneBonCommandeVente) {

                    int index = LigneBonCommandeVenteTemps.indexOf(ligneBonCommandeVente);

                    if (index == -1) {

                        ejbFacadeLigneBonCommandeVente.remove(ligneBonCommandeVente);
                    } else {
                        //ceci pour remédier au cas ou le stock est supprimer puis réajouter sans passer par la modif
                        LigneBonCommandeVenteTemps.get(index).setId(ligneBonCommandeVente.getId());
                        LigneBonCommandeVenteTemps.get(index).setBonCommandeVente(selected);
                        ejbFacadeLigneBonCommandeVente.edit(LigneBonCommandeVenteTemps.get(index));

                    }

                }
                sauvegardeNouvelleLigneBonCommandeVente();

                //les taxes a suuprimer
                if (listTaxeSuppresssion != null && !listTaxeSuppresssion.isEmpty()) {
                    ejbFacadeTaxeBonCommandeVente.remove(listTaxeSuppresssion);
                }

                if (!listTaxesTemps.isEmpty()) {
                    for (TaxesBonCommandeVente taxeBonCommandeVente : listTaxesTemps) {
                        taxeBonCommandeVente.setBonCommandeVente(selected);
                        if (taxeBonCommandeVente.getId() != null) {
                            ejbFacadeTaxeBonCommandeVente.edit(taxeBonCommandeVente);
                        } else {
                            ejbFacadeTaxeBonCommandeVente.create(taxeBonCommandeVente);
                        }

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
            System.out.println("Erreur- BonCommandeVenteController - update: " + e.getMessage());
            return null;
        }
    }

    private void sauvegardeNouvelleLigneBonCommandeVente() {

        int j = 0;
        //boolean trouve = false;

        for (int i = 0; i < LigneBonCommandeVenteTemps.size(); i++) {

            if (!AncienLigneBonCommandeVente.contains(LigneBonCommandeVenteTemps.get(i)) && LigneBonCommandeVenteTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 1) {
                LigneBonCommandeVenteTemps.get(i).setBonCommandeVente(selected);
                ejbFacadeLigneBonCommandeVente.create(LigneBonCommandeVenteTemps.get(i));
            }

        }

    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<BonCommandeVentePVT> temps = ejbFacadeBonCommandeVentePVT.findAll("where o.idBonCommandeVente = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- BonCommandeVenteController - performDestroy: " + e.getMessage());
        }
    }

    public void deleteFromListLigneBonCommandeVente() {

        if (selectedLigneBonCommandeVenteSingle != null) {
            selected.getListeLigneBonCommandeVentes().remove(selectedLigneBonCommandeVenteSingle);
            recalculerTotal();
            updateListTaxe(selected.getListsTaxe());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }

    public void validerDetailArticle() {
        if (selectedLigneBonCommandeVente.getArticle() != null) {
            selectedLigneBonCommandeVente.setIdArticle(selectedLigneBonCommandeVente.getArticle().getId());
            int index = selected.getListeLigneBonCommandeVentes().indexOf(selectedLigneBonCommandeVente);
            if (index > -1) {
                selected.getListeLigneBonCommandeVentes().get(index).setQuantite(selectedLigneBonCommandeVente.getQuantite());
                selected.getListeLigneBonCommandeVentes().get(index).setQuantiteMax(selectedLigneBonCommandeVente.getQuantite());
                selected.getListeLigneBonCommandeVentes().get(index).setPrixUnitaireHT(selectedLigneBonCommandeVente.getPrixUnitaireHT());
                selected.getListeLigneBonCommandeVentes().get(index).setPrixUnitaireApresRemise(selectedLigneBonCommandeVente.getPrixUnitaireApresRemise());
                selected.getListeLigneBonCommandeVentes().get(index).setRemise(selectedLigneBonCommandeVente.getRemise());
                selected.getListeLigneBonCommandeVentes().get(index).setTvaArticle(new BigDecimal(selectedLigneBonCommandeVente.getTvaArticle() + ""));

                selected.getListeLigneBonCommandeVentes().get(index).setTotalHT(selectedLigneBonCommandeVente.getTotalHT());
                selected.getListeLigneBonCommandeVentes().get(index).setTotalTVA(selectedLigneBonCommandeVente.getTotalTVA());
                selected.getListeLigneBonCommandeVentes().get(index).setTotalTTC(selectedLigneBonCommandeVente.getTotalTTC());
                recalculerTotal();
                selectedLigneBonCommandeVente = new LigneBonCommandeVente();
                categorie = new Categorie();
            } else {
                selected.getListeLigneBonCommandeVentes().add(selectedLigneBonCommandeVente);
                recalculerTotal();
                selectedLigneBonCommandeVente = new LigneBonCommandeVente();
                categorie = new Categorie();
            }

            if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {
                updateListTaxe(selected.getListsTaxe());
            }

        }
    }

    public void validerDetailArticleModif() {

        if (selectedLigneBonCommandeVente.getQuantite().compareTo(selectedLigneBonCommandeVente.getQuantiteMax()) <= 0) {
            recalculerTotal();
            selectedLigneBonCommandeVente = new LigneBonCommandeVente();

        } else {
            selectedLigneBonCommandeVente.setPrixUnitaireHT(oldPrix);
            selectedLigneBonCommandeVente.setQuantite(oldQuantity);
            changedTotalHtTotalTtc();
        }

    }

    public String rejeterBonCommandeVente() {
        selected.setEtat(3);
        getFacade().edit(selected);
        return prepareList();

    }

    public String approuverBonCommandeVente() {

        //BL
        if (selected.getTransFormTo() == 0) {

            List<PrefixBonLivraison> listPrefixBonLivraisons = null;
            PrefixBonLivraison prefixBonLivraison = null;
            listPrefixBonLivraisons = ejbFacadePrefixBonLivraison.findAll(" where o.supprimer = 0 ");
            String compteur = "";
            if (listPrefixBonLivraisons != null && !listPrefixBonLivraisons.isEmpty()) {
                prefixBonLivraison = listPrefixBonLivraisons.get(0);
                compteur = prefixBonLivraison.getLibellePrefixe() + FonctionsString.retourMotSelonLongeur(prefixBonLivraison.getCompteur() + "", 6);

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
                return null;
            }

            if (!ejbFacadeBonLivraison.verifierUniqueNumero(compteur)) {
                BonLivraison bonLivraison = createBonLivraison(compteur);
                selected.setEtat(4);
                selected.setIdDocumentTransform(bonLivraison.getId());
                selected.setNumeroDocumentTransform(bonLivraison.getNumero());
                getFacade().edit(selected);
                prefixBonLivraison.setCompteur(prefixBonLivraison.getCompteur() + 1);
                ejbFacadePrefixBonLivraison.edit(prefixBonLivraison);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }

        }//Facture
        else if (selected.getTransFormTo() == 1) {

            List<PrefixFacture> listPrefixFactures = null;
            PrefixFacture prefixFacture = null;
            listPrefixFactures = ejbFacadePrefixFacture.findAll(" where o.supprimer = 0 ");
            String compteur = "";
            if (listPrefixFactures != null && !listPrefixFactures.isEmpty()) {
                prefixFacture = listPrefixFactures.get(0);
                compteur = prefixFacture.getLibellePrefixe() + FonctionsString.retourMotSelonLongeur(prefixFacture.getCompteur() + "", 6);

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
                return null;
            }

            if (!ejbFacadeFacture.verifierUniqueNumero(compteur)) {
                Facture facture = createFacture(compteur);
                selected.setEtat(4);
                selected.setIdDocumentTransform(facture.getId());
                selected.setNumeroDocumentTransform(facture.getNumero());
                getFacade().edit(selected);
                prefixFacture.setCompteur(prefixFacture.getCompteur() + 1);
                ejbFacadePrefixFacture.edit(prefixFacture);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }

        }

        return prepareList();
    }

    public void createBonCommandeFromDevis() {

        // selected.setNumero(numero);
        //selected.setCodeClient(selected.getDevis().getCodeClient());
        selected.setLibelleClient(selected.getDevis().getLibelleClient());
        selected.setAssujettiTVA(selected.getDevis().isAssujettiTVA());
        selected.setIdClient(selected.getDevis().getIdClient());
        selected.setDateSynch(System.currentTimeMillis());
        selected.setEtat(0);
        selected.setDateCreation(new Date());
        selected.setOrigine(1);
        selected.setTypeVente(0);
        selected.setMontantHT(selected.getDevis().getMontantHT());
        selected.setMontantNet(selected.getDevis().getMontantNet());
        selected.setAppliquerRemise(selected.getDevis().getAppliquerRemise());
        selected.setMontantRemiseGlobal(selected.getDevis().getMontantRemiseGlobal());
        selected.setTauxRemiseGlobal(selected.getDevis().getTauxRemiseGlobal());
        selected.setMontantTVA(selected.getDevis().getMontantTVA());
        selected.setMontantTTC(selected.getDevis().getMontantTTC());
        /*selected.setTotalHT(selected.getDevis().getTotalHT());
         selected.setTotalTTC(selected.getDevis().getTotalTTC());
         selected.setTotalTaxe(selected.getDevis().getTotalTaxe());*/

        selected.setIdDocumentOrigine(selected.getDevis().getId());
        selected.setNbJourVente(selected.getDevis().getNbJourVente());
        List<LigneBonCommandeVente> tempsList = new ArrayList<>();
        tempsList = selected.getListeLigneBonCommandeVentes();
        selected.setListeLigneBonCommandeVentes(null);

        ejbFacade.create(selected);

        //List<LigneFacture> listLigneFactures = new ArrayList<>();
        for (LigneBonCommandeVente tempsList1 : tempsList) {
            tempsList1.setBonCommandeVente(selected);

        }
        ejbFacadeLigneBonCommandeVente.create(tempsList);

    }

    public BonLivraison createBonLivraison(String numero) {

        BonLivraison bonLivraison = new BonLivraison();
        bonLivraison.setNumero(numero);
        //bonLivraison.setCodeClient(selected.getCodeClient());
        bonLivraison.setLibelleClient(selected.getLibelleClient());
        bonLivraison.setIdClient(selected.getIdClient());
        bonLivraison.setOrigine(2);
        bonLivraison.setIdDocumentOrigine(selected.getId());
        bonLivraison.setNumeroDocumentOrigine(selected.getNumero());
        bonLivraison.setEtat(0);
        bonLivraison.setDateCreation(new Date());
        bonLivraison.setTypeVente(0);
        bonLivraison.setMontantHT(selected.getMontantHT());
        bonLivraison.setTotalHT(selected.getTotalHT());
        bonLivraison.setMontantNet(selected.getMontantNet());
        bonLivraison.setAppliquerRemise(selected.getAppliquerRemise());
        bonLivraison.setMontantRemiseGlobal(selected.getMontantRemiseGlobal());
        bonLivraison.setTauxRemiseGlobal(selected.getTauxRemiseGlobal());
        bonLivraison.setMontantTVA(selected.getMontantTVA());
        bonLivraison.setMontantTTC(selected.getMontantTTC());
        bonLivraison.setDateSynch(System.currentTimeMillis());
        bonLivraison.setReste(selected.getMontantNet());
        bonLivraison.setDateBonLivraison(new Date());
        bonLivraison.setNbJourVente(selected.getNbJourVente());
        ejbFacadeBonLivraison.create(bonLivraison);

        List<LigneBonLivraison> listLigneBonLivraisons = new ArrayList<>();

        for (LigneBonCommandeVente ligneBonCommandeVente : selected.getListeLigneBonCommandeVentes()) {
            LigneBonLivraison ligneBonLivraison = new LigneBonLivraison();
            ligneBonLivraison.setCodeArticle(ligneBonCommandeVente.getCodeArticle());
            ligneBonLivraison.setLibelleArticle(ligneBonCommandeVente.getLibelleArticle());
            ligneBonLivraison.setIdArticle(ligneBonCommandeVente.getIdArticle());

            ligneBonLivraison.setTvaArticle(ligneBonCommandeVente.getTvaArticle());
            ligneBonLivraison.setPrixUnitaireHT(ligneBonCommandeVente.getPrixUnitaireHT());
            ligneBonLivraison.setPrixUnitaireApresRemise(ligneBonCommandeVente.getPrixUnitaireApresRemise());
            ligneBonLivraison.setRemise(ligneBonCommandeVente.getRemise());
            ligneBonLivraison.setQuantite(ligneBonCommandeVente.getQuantite());
            ligneBonLivraison.setQuantiteMax(ligneBonCommandeVente.getQuantiteMax());

            ligneBonLivraison.setTotalHT(ligneBonCommandeVente.getTotalHT());
            ligneBonLivraison.setTotalTVA(ligneBonCommandeVente.getTotalTVA());
            ligneBonLivraison.setTotalTTC(ligneBonCommandeVente.getTotalTTC());
            ligneBonLivraison.setBonLivraison(bonLivraison);
            listLigneBonLivraisons.add(ligneBonLivraison);
        }
        ejbFacadeLigneBonLivraison.create(listLigneBonLivraisons);

        return bonLivraison;
    }

    public Facture createFacture(String numero) {

        Facture facture = new Facture();
        facture.setNumero(numero);
        facture.setCodeClient(selected.getCodeClient());
        facture.setLibelleClient(selected.getLibelleClient());
        facture.setIdClient(selected.getIdClient());
        facture.setDateFacture(TraitementDate.debutJournee(new Date()));
        facture.setEtat(0);
        facture.setDateCreation(new Date());
        facture.setOrigine(2);
        facture.setIdDocumentOrigine(selected.getId());
        facture.setNumeroDocumentOrigine(selected.getNumero());
        facture.setTypeVente(0);
        facture.setMontantHT(selected.getMontantHT());
        facture.setMontantNet(selected.getMontantNet());
        facture.setAppliquerRemise(selected.getAppliquerRemise());
        facture.setMontantRemiseGlobal(selected.getMontantRemiseGlobal());
        facture.setTauxRemiseGlobal(selected.getTauxRemiseGlobal());
        facture.setMontantTVA(selected.getMontantTVA());
        facture.setMontantTTC(selected.getMontantTTC());
        facture.setTotalHT(selected.getTotalHT());
        facture.setTotalTVA(selected.getTotalTVA());
        facture.setTotalTTC(selected.getTotalTTC());
        facture.setTotalTaxe(selected.getTotalTaxe());
        facture.setReste(selected.getTotalTTC());
        facture.setNbJourVente(selected.getNbJourVente());
        ejbFacadeFacture.create(facture);

        List<LigneFacture> listLigneFactures = new ArrayList<>();

        for (LigneBonCommandeVente ligneBonCommandeVente : selected.getListeLigneBonCommandeVentes()) {
            LigneFacture ligneFacture = new LigneFacture();
            ligneFacture.setCodeArticle(ligneBonCommandeVente.getCodeArticle());
            ligneFacture.setLibelleArticle(ligneBonCommandeVente.getLibelleArticle());
            ligneFacture.setIdArticle(ligneBonCommandeVente.getIdArticle());

            ligneFacture.setTvaArticle(ligneBonCommandeVente.getTvaArticle());
            ligneFacture.setPrixUnitaireHT(ligneBonCommandeVente.getPrixUnitaireHT());
            ligneFacture.setPrixUnitaireApresRemise(ligneBonCommandeVente.getPrixUnitaireApresRemise());
            ligneFacture.setRemise(ligneBonCommandeVente.getRemise());
            ligneFacture.setQuantite(ligneBonCommandeVente.getQuantite());
            ligneFacture.setQuantiteMax(ligneBonCommandeVente.getQuantiteMax());
            ligneFacture.setTotalTVA(ligneBonCommandeVente.getTotalTVA());
            ligneFacture.setTotalHT(ligneBonCommandeVente.getTotalHT());
            ligneFacture.setTotalTTC(ligneBonCommandeVente.getTotalTTC());
            ligneFacture.setFacture(facture);
            listLigneFactures.add(ligneFacture);
        }
        ejbFacadeLigneFacture.create(listLigneFactures);

        List<TaxesFacture> listTaxesFactures = new ArrayList<>();
        for (TaxesBonCommandeVente taxesBonCommandeVente : selected.getListsTaxe()) {
            TaxesFacture taxesFacture = new TaxesFacture();
            taxesFacture.setFacture(facture);
            taxesFacture.setApresTva(taxesBonCommandeVente.isApresTva());
            taxesFacture.setValeur(taxesBonCommandeVente.getValeur());
            taxesFacture.setMontant(taxesBonCommandeVente.getMontant());
            taxesFacture.setTypeTaxe(taxesBonCommandeVente.getTypeTaxe());
            taxesFacture.setOperation(taxesBonCommandeVente.getOperation());
            taxesFacture.setParametrageTaxe(taxesBonCommandeVente.getParametrageTaxe());
            listTaxesFactures.add(taxesFacture);
        }

        ejbFacadeTaxeFacture.create(listTaxesFactures);

        //createRetour(listLigneFactures,facture);
        return facture;
    }

    public void recalculerTotal() {

        selected.setMontantHT(BigDecimal.ZERO);
        selected.setMontantTVA(BigDecimal.ZERO);
        selected.setMontantTTC(BigDecimal.ZERO);
        for (LigneBonCommandeVente ligneBonCommandeVente : selected.getListeLigneBonCommandeVentes()) {
            selected.setMontantHT(selected.getMontantHT().add(ligneBonCommandeVente.getTotalHT()));
            selected.setMontantTVA(selected.getMontantTVA().add(ligneBonCommandeVente.getTotalTVA()));
            selected.setMontantTTC(selected.getMontantTTC().add(ligneBonCommandeVente.getTotalTTC()));
        }

        selected.setTotalHT(selected.getMontantHT().multiply(BigDecimal.valueOf(selected.getNbJourVente())));
        selected.setTotalTVA(selected.getMontantTVA().multiply(BigDecimal.valueOf(selected.getNbJourVente())));
        selected.setMontantNet(selected.getTotalHT());
        selected.setTotalTTC(selected.getMontantTTC().multiply(BigDecimal.valueOf(selected.getNbJourVente())));

        if (parametrageEntreprise.isAppliquerRemiseGlobale()) {
            calculeMontantNet();
        }
    }

    public void listnerPrixUnitaire() {
        if (selectedLigneBonCommandeVente.getArticle() != null) {
            selectedLigneBonCommandeVente.setIdArticle(selectedLigneBonCommandeVente.getArticle().getId());
            selectedLigneBonCommandeVente.setCodeArticle(selectedLigneBonCommandeVente.getArticle().getCode());
            selectedLigneBonCommandeVente.setLibelleArticle(selectedLigneBonCommandeVente.getArticle().getLibelle());
            selectedLigneBonCommandeVente.setPrixUnitaireHT(selectedLigneBonCommandeVente.getArticle().getPrixRevendeur());
            selectedLigneBonCommandeVente.setTvaArticle(new BigDecimal(selectedLigneBonCommandeVente.getArticle().getTva().getValeur()));
            selectedLigneBonCommandeVente.setQuantiteStock(selectedLigneBonCommandeVente.getArticle().getQuantiteStock());
            selectedLigneBonCommandeVente.setQuantite(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalTVA(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalTTC(BigDecimal.ZERO);
        }
    }

    public void changedTotalHtTotalTtc() {
        if (selected.getClient() == null || selectedLigneBonCommandeVente.getLibelleArticle() == null) {
            selectedLigneBonCommandeVente.setPrixUnitaireHT(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTvaArticle(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setQuantite(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setQuantiteStock(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalTVA(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalTTC(BigDecimal.ZERO);
        } else {
            selectedLigneBonCommandeVente.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalTVA(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setTotalTTC(BigDecimal.ZERO);
            selectedLigneBonCommandeVente.setQuantiteMax(selectedLigneBonCommandeVente.getQuantite());
            selectedLigneBonCommandeVente.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            BigDecimal prixRevendeur = selectedLigneBonCommandeVente.getPrixUnitaireHT();
            if (selectedLigneBonCommandeVente.getRemise().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal valRemise = FonctionsMathematiques.arrondiBigDecimal(prixRevendeur.multiply(selectedLigneBonCommandeVente.getRemise()), 3);
                valRemise = FonctionsMathematiques.arrondiBigDecimal(valRemise.divide(new BigDecimal("100")), 3);
                prixRevendeur = prixRevendeur.subtract(valRemise);
            }
            selectedLigneBonCommandeVente.setPrixUnitaireApresRemise(prixRevendeur);

            BigDecimal TotalHT = FonctionsMathematiques.arrondiBigDecimal((selectedLigneBonCommandeVente.getPrixUnitaireApresRemise()).multiply(selectedLigneBonCommandeVente.getQuantite()), 3);
            selectedLigneBonCommandeVente.setTotalHT(TotalHT);

            if (selected.getClient().isAssujettiTVA()) {
                selectedLigneBonCommandeVente.setTotalTVA(((selectedLigneBonCommandeVente.getTotalHT().multiply(selectedLigneBonCommandeVente.getTvaArticle()))).divide(BigDecimal.valueOf(100)));
                selectedLigneBonCommandeVente.setTotalTTC(selectedLigneBonCommandeVente.getTotalHT().add(selectedLigneBonCommandeVente.getTotalTVA()));

            } else {
                selectedLigneBonCommandeVente.setTotalTVA(BigDecimal.ZERO);
                selectedLigneBonCommandeVente.setTotalTTC(TotalHT);
            }

            /*            BigDecimal valTemp = FonctionsMathematiques.arrondiBigDecimal(selectedLigneBonCommandeVente.getMontantHT().multiply(selectedLigneBonCommandeVente.getTvaArticle()), 3);
             selectedLigneBonCommandeVente.setMontantTVA(FonctionsMathematiques.arrondiBigDecimal(valTemp.divide(BigDecimal.valueOf(100)), 3));
             selectedLigneBonCommandeVente.setMontantTTC(selectedLigneBonCommandeVente.getMontantHT().add(selectedLigneBonCommandeVente.getMontantTVA()));*/
        }
    }

    public void loadDevis() {

        if (selected.getOrigine() == 1) {

            if (selected.getClient() != null) {
                listDeviss = ejbFacadeDevis.findAllNative(" where o.Cli_Id = " + selected.getClient().getId() + " and o.Dev_Etat = 1 ");

            } else {
                listDeviss = new ArrayList<>();
            }

        } else {
            listDeviss = new ArrayList<>();
        }

    }

    public void changeDevis() {

        selected.setListeLigneBonCommandeVentes(new ArrayList<LigneBonCommandeVente>());

        if (selected.getDevis().getListeLigneDeviss().isEmpty()) {
            selected.getDevis().setListeLigneDeviss(ejbFacadeLigneDevis.findAll(" where o.devis.id = " + selected.getDevis().getId()));

        }

        selected.setTotalTaxe(selected.getDevis().getTotalTaxe());
        selected.setMontantHT(selected.getDevis().getTotalHT());
        selected.setMontantTVA(selected.getDevis().getTotalTVA());
        selected.setMontantTTC(selected.getDevis().getTotalTTC());
        selected.setMontantNet(selected.getDevis().getMontantNet());
        selected.setMontantRemiseGlobal(selected.getDevis().getMontantRemiseGlobal());
        selected.setTauxRemiseGlobal(selected.getDevis().getTauxRemiseGlobal());
        selected.setAppliquerRemise(selected.getDevis().getAppliquerRemise());
        selected.setTotalHT(selected.getMontantHT());
        selected.setTotalTVA(selected.getMontantTVA());
        selected.setTotalTTC(selected.getMontantTTC());
        selected.setNbJourVente(selected.getDevis().getNbJourVente());
        selected.setListsTaxe(new ArrayList<>());
        // nous allons calculer la taxe
        for (LigneDevis detailDevis : selected.getDevis().getListeLigneDeviss()) {

            LigneBonCommandeVente ligneBonCommandeVente = new LigneBonCommandeVente();
            ligneBonCommandeVente.setIdArticle(detailDevis.getIdArticle());
            ligneBonCommandeVente.setCodeArticle(detailDevis.getCodeArticle());
            ligneBonCommandeVente.setLibelleArticle(detailDevis.getLibelleArticle());
            ligneBonCommandeVente.setPrixUnitaireApresRemise(detailDevis.getPrixUnitaireApresRemise());
            ligneBonCommandeVente.setRemise(detailDevis.getRemise());
            ligneBonCommandeVente.setQuantite(detailDevis.getQuantite());
            ligneBonCommandeVente.setQuantiteMax(detailDevis.getQuantite());
            ligneBonCommandeVente.setPrixUnitaireHT(detailDevis.getPrixUnitaireHT());
            ligneBonCommandeVente.setTvaArticle(detailDevis.getTvaArticle());
            ligneBonCommandeVente.setTotalHT(detailDevis.getTotalHT());
            ligneBonCommandeVente.setTotalTVA(detailDevis.getTotalTVA());
            ligneBonCommandeVente.setTotalTTC(detailDevis.getTotalTTC());

            selected.getListeLigneBonCommandeVentes().add(ligneBonCommandeVente);

        }
        if (selected.getDevis().getListsTaxe() != null) {
            for (TaxesDevis taxesDevis : selected.getDevis().getListsTaxe()) {
                TaxesBonCommandeVente taxesBonCommandeVente = new TaxesBonCommandeVente();
                //taxesFacture.setFacture(facture);
                taxesBonCommandeVente.setApresTva(taxesDevis.isApresTva());
                taxesBonCommandeVente.setValeur(taxesDevis.getValeur());
                taxesBonCommandeVente.setMontant(taxesDevis.getMontant());
                taxesBonCommandeVente.setTypeTaxe(taxesDevis.getTypeTaxe());
                taxesBonCommandeVente.setOperation(taxesDevis.getOperation());
                taxesBonCommandeVente.setParametrageTaxe(taxesDevis.getParametrageTaxe());
                selected.getListsTaxe().add(taxesBonCommandeVente);
            }
        }
    }

    //taxe bonCommandeVente
    public boolean verifierExistanceTaxe(TaxesBonCommandeVente taxe, List<TaxesBonCommandeVente> listTaxe) {
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

    public void calculerTotalTaxe(List<TaxesBonCommandeVente> list) {
        selected.setTotalTaxe(BigDecimal.ZERO);
        for (int i = 0; i < list.size(); i++) {
            if (selected.getListsTaxe().get(i).getOperation().trim().equals("+")) {
                selected.setTotalTaxe((selected.getTotalTaxe()).add(list.get(i).getMontant()));
            } else {

                selected.setTotalTaxe((selected.getTotalTaxe()).subtract(list.get(i).getMontant()));
            }
        }
    }

    public void validerTaxeBonCommandeVente() {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantNet());
            selected.setTotalTTC(selected.getMontantTTC().multiply(BigDecimal.valueOf(selected.getNbJourVente())));
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
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
                        taxe.setParametrageTaxe(selectedTaxe);
                        taxe.setValeur(selectedTaxe.getValeur());
                        taxe.setOperation(selectedTaxe.getOperation());
                        taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                        taxe.setApresTva(selectedTaxe.isApresTva());
                        taxe.setMontant((selected.getMontantNet().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
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
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
                        taxe.setParametrageTaxe(selectedTaxe);
                        taxe.setValeur(selectedTaxe.getValeur());
                        taxe.setOperation(selectedTaxe.getOperation());
                        taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                        taxe.setApresTva(selectedTaxe.isApresTva());
                        taxe.setMontant(((selected.getMontantTTC().multiply(BigDecimal.valueOf(selected.getNbJourVente()))).multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
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
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
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
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
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

    public void validerTaxeBonCommandeVenteEdit() {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantHT());
            selected.setTotalTTC(selected.getMontantTTC());
        }
        if (selectedListParametrageTaxe != null && !selectedListParametrageTaxe.isEmpty()) {

            for (ParametrageTaxe selectTaxe : selectedListParametrageTaxe) {
                int index = -1;
                for (int j = 0; j < listTaxeBonCommandeVente.size(); j++) {
                    if (listTaxeBonCommandeVente.get(j).getParametrageTaxe().getLibelle().trim().equals(selectTaxe.getLibelle())) {
                        index = j;
                    }
                }
                if (index > -1) {
                    System.out.println("taxe existe");
                } else {
                    if ((selectTaxe.getTypeTaxe().trim().equals("0")) && (!selectTaxe.isApresTva())) {
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
                        taxe.setParametrageTaxe(selectTaxe);
                        taxe.setValeur(selectTaxe.getValeur());
                        taxe.setOperation(selectTaxe.getOperation());
                        taxe.setTypeTaxe(selectTaxe.getTypeTaxe());
                        taxe.setApresTva(selectTaxe.isApresTva());
                        taxe.setMontant((selected.getMontantNet().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, listTaxeBonCommandeVente)) {
                            listTaxeBonCommandeVente.add(taxe);
                        }
                    } //apresTva
                    else if ((selectTaxe.getTypeTaxe().trim().equals("0")) && (selectTaxe.isApresTva())) {
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
                        taxe.setParametrageTaxe(selectTaxe);
                        taxe.setValeur(selectTaxe.getValeur());
                        taxe.setOperation(selectTaxe.getOperation());
                        taxe.setTypeTaxe(selectTaxe.getTypeTaxe());
                        taxe.setApresTva(selectTaxe.isApresTva());
                        taxe.setMontant(((selected.getMontantTTC().multiply(BigDecimal.valueOf(selected.getNbJourVente()))).multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (taxe.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                        }
                        if (!verifierExistanceTaxe(taxe, listTaxeBonCommandeVente)) {
                            listTaxeBonCommandeVente.add(taxe);
                        }
                        //valeurs fixe avant tva
                    } else if ((selectTaxe.getTypeTaxe().trim().equals("1")) && (!selectTaxe.isApresTva())) {
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
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
                        if (!verifierExistanceTaxe(taxe, listTaxeBonCommandeVente)) {
                            listTaxeBonCommandeVente.add(taxe);
                        }
                    } //valeur fixe apres tva
                    else if ((selectTaxe.getTypeTaxe().trim().equals("1")) && (selectTaxe.isApresTva())) {
                        TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
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
                        if (!verifierExistanceTaxe(taxe, listTaxeBonCommandeVente)) {
                            listTaxeBonCommandeVente.add(taxe);
                        }
                    }
                }
            }
            calculerTotalTaxe(listTaxeBonCommandeVente);
            updateListTaxe(listTaxeBonCommandeVente);
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
            listTaxeBonCommandeVente.remove(selectedSingleTaxe);
            listTaxeSuppresssion.add(selectedSingleTaxe);
            updateListTaxe(listTaxeBonCommandeVente);

        }
    }

    public void updateListTaxe(List<TaxesBonCommandeVente> list) {
        //selected.setTotalTTC(selected.getMontantTTC().multiply(BigDecimal.valueOf(selected.getNbJourVente())));
        if (!list.isEmpty()) {
            for (TaxesBonCommandeVente item : list) {

                if ((item.getTypeTaxe().trim().equals("0")) && (!item.isApresTva())) {
                    if (item.getOperation().trim().equals("+")) {
                        item.setMontant((selected.getMontantNet().multiply(item.getValeur())).divide(BigDecimal.valueOf(100)));
                        if (item.getOperation().equals("+")) {
                            selected.setTotalTTC((selected.getTotalTTC()).add(item.getMontant()));
                        } else {
                            selected.setTotalTTC((selected.getTotalTTC()).subtract(item.getMontant()));
                        }
                    }
                } //apresTva
                else if ((item.getTypeTaxe().trim().equals("0")) && (item.isApresTva())) {
                    item.setMontant(((selected.getMontantTTC().multiply(BigDecimal.valueOf(selected.getNbJourVente()))).multiply(item.getValeur())).divide(BigDecimal.valueOf(100)));
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
                        selected.setTotalTTC((selected.getTotalTTC()).add(item.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(item.getMontant()));
                    }
                }
            }

        }
        calculerTotalTaxe(list);

    }

    public void initChamps() {
        initDocumentFields();
        loadDevis();
        selected.setDevis(null);
    }

    public void initTaxeBonCommandeVente(List<ParametrageTaxe> listParametrageTaxeEntreprise) {

        if (listParametrageTaxeEntreprise != null && !listParametrageTaxeEntreprise.isEmpty()) {

            for (ParametrageTaxe selectedTaxe : listParametrageTaxeEntreprise) {
                if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (!selectedTaxe.isApresTva())) {
                    TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
                    taxe.setParametrageTaxe(selectedTaxe);
                    taxe.setValeur(selectedTaxe.getValeur());
                    taxe.setOperation(selectedTaxe.getOperation());
                    taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                    taxe.setApresTva(selectedTaxe.isApresTva());
                    taxe.setMontant((selected.getMontantNet().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                    if (taxe.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                    }

                    selected.getListsTaxe().add(taxe);

                } //apresTva
                else if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (selectedTaxe.isApresTva())) {
                    TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
                    taxe.setParametrageTaxe(selectedTaxe);
                    taxe.setValeur(selectedTaxe.getValeur());
                    taxe.setOperation(selectedTaxe.getOperation());
                    taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                    taxe.setApresTva(selectedTaxe.isApresTva());
                    taxe.setMontant(((selected.getMontantTTC().multiply(BigDecimal.valueOf(selected.getNbJourVente()))).multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                    if (taxe.getOperation().equals("+")) {
                        selected.setTotalTTC((selected.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        selected.setTotalTTC((selected.getTotalTTC()).subtract(taxe.getMontant()));
                    }
                    selected.getListsTaxe().add(taxe);

                    //valeurs fixe avant tva
                } else if ((selectedTaxe.getTypeTaxe().trim().equals("1")) && (!selectedTaxe.isApresTva())) {
                    TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
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
                    TaxesBonCommandeVente taxe = new TaxesBonCommandeVente();
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

    public void appliquerRemiseGlobale() {
        if (selected.getAppliquerRemise() == -1) {

            selected.setMontantRemiseGlobal(BigDecimal.ZERO);
            selected.setTauxRemiseGlobal(BigDecimal.ZERO);
            recalculerTotal();
            if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {
                updateListTaxe(selected.getListsTaxe());
            }
        }
    }

    private void calculeMontantNet() {

        if (selected.getAppliquerRemise() == 0 && selected.getTauxRemiseGlobal().compareTo(BigDecimal.ZERO) == 1) {

            selected.setMontantRemiseGlobal(selected.getTotalHT().multiply(selected.getTauxRemiseGlobal()).divide(BigDecimal.valueOf(100)));
            selected.setMontantNet(selected.getMontantNet().subtract(selected.getMontantRemiseGlobal()));
            selected.setTotalTTC(selected.getMontantNet().add(selected.getTotalTVA()));

        } else if (selected.getAppliquerRemise() == 1 && selected.getMontantRemiseGlobal().compareTo(BigDecimal.ZERO) == 1) {

            selected.setTauxRemiseGlobal((selected.getMontantRemiseGlobal().divide(selected.getTotalHT(), 4, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100))));
            if (selected.getMontantRemiseGlobal().compareTo(selected.getTotalHT()) >= 0) {
                selected.setTauxRemiseGlobal(BigDecimal.valueOf(100));
                selected.setMontantRemiseGlobal(selected.getTotalHT());
            }
            selected.setMontantNet(selected.getMontantNet().subtract(selected.getMontantRemiseGlobal()));
            selected.setTotalTTC(selected.getMontantNet().add(selected.getTotalTVA()));
        }
    }

    public void recalculerToutMontants() {
        recalculerTotal();
        if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {
            updateListTaxe(selected.getListsTaxe());
        }
    }

    private void initDocumentFields() {
        selected.setMontantHT(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTVA(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTTC(new BigDecimal(BigInteger.ZERO));
        selected.setTotalHT(BigDecimal.ZERO);
        selected.setTotalTVA(BigDecimal.ZERO);
        selected.setTotalTTC(BigDecimal.ZERO);
        selected.setMontantNet(BigDecimal.ZERO);
        selected.setMontantRemiseGlobal(BigDecimal.ZERO);
        selected.setTauxRemiseGlobal(BigDecimal.ZERO);
        selected.setAppliquerRemise(-1);
        selected.setNbJourVente(1);
        selected.setDevis(null);
        selected.setListsTaxe(new ArrayList<>());
        selected.setListeLigneBonCommandeVentes(new ArrayList<>());
        initTaxeBonCommandeVente(listParametrageTaxeEntreprise);
    }

    public void generationSelectedPDF() throws IOException, DocumentException {

        if (selectedSingle != null) {

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String path = LireParametrage.returnValeurParametrage("urlDocumentNonPersistant") + "Facture_" + selectedSingle.getNumero() + ".pdf";
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
            String dateBonCommandeVente = TraitementDate.returnDate(selectedSingle.getDateCreation());
            String numeroBonCommandeVente = selectedSingle.getNumero();

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
            ArrayList<String> ligneBonCommandeVenteEntete = new ArrayList();
            if (parametrageEntreprise.isGestionParCodeArticle()) {
                ligneBonCommandeVenteEntete.add("Reference");
            }
            ligneBonCommandeVenteEntete.add("Produit");
            ligneBonCommandeVenteEntete.add("TVA");
            ligneBonCommandeVenteEntete.add("Quantite");
            ligneBonCommandeVenteEntete.add("Prix UT");
            ligneBonCommandeVenteEntete.add("Total HT");

            ArrayList<ArrayList<String>> ligneFactures = new ArrayList<ArrayList<String>>();

            ArrayList<String> ligneBonCommandeVenteInfo;

            for (int i = 0; i < selectedSingle.getListeLigneBonCommandeVentes().size(); i++) {

                ligneBonCommandeVenteInfo = new ArrayList();
                //0:left 1: linea left 2:center 3:linea right 4:right
                if (parametrageEntreprise.isGestionParCodeArticle()) {
                    ligneBonCommandeVenteInfo.add("1:" + selectedSingle.getListeLigneBonCommandeVentes().get(i).getCodeArticle());
                }
                ligneBonCommandeVenteInfo.add("1:" + selectedSingle.getListeLigneBonCommandeVentes().get(i).getLibelleArticle());
                ligneBonCommandeVenteInfo.add("3:" + selectedSingle.getListeLigneBonCommandeVentes().get(i).getTvaArticle() + "%");
                ligneBonCommandeVenteInfo.add("3:" + selectedSingle.getListeLigneBonCommandeVentes().get(i).getQuantite());
                ligneBonCommandeVenteInfo.add("3:" + selectedSingle.getListeLigneBonCommandeVentes().get(i).getPrixUnitaireApresRemise());
                ligneBonCommandeVenteInfo.add("3:" + selectedSingle.getListeLigneBonCommandeVentes().get(i).getTotalHT());
                ligneFactures.add(i, ligneBonCommandeVenteInfo);
            }

            //addInvoiceSummarize
            ArrayList<String> bonCommandeVenteSummarizeInfos = new ArrayList();

            bonCommandeVenteSummarizeInfos.add("Total HT" + " : " + selectedSingle.getTotalHT());
            bonCommandeVenteSummarizeInfos.add("Remise" + " : " + selectedSingle.getMontantRemiseGlobal());
            bonCommandeVenteSummarizeInfos.add("Total Net" + " : " + selectedSingle.getMontantNet());
            bonCommandeVenteSummarizeInfos.add("Total TVA" + " : " + selectedSingle.getTotalTVA());
            bonCommandeVenteSummarizeInfos.add("Total Taxe" + " : " + selectedSingle.getTotalTaxe());
            bonCommandeVenteSummarizeInfos.add("Total TTC" + " : " + selectedSingle.getTotalTTC());

            GenerationPdf.generationPdf(image, path, "BonCommandeVente", numeroBonCommandeVente, dateBonCommandeVente, entrepriseInfos, clientInfos, ligneBonCommandeVenteEntete, ligneFactures, bonCommandeVenteSummarizeInfos, parametrageEntreprise.isGestionParCodeArticle(), utilisateur.getEntreprise().getHeader(), utilisateur.getEntreprise().getFooter());

            File file = new File(path);
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=" + "BonCommandeVente_" + numeroBonCommandeVente + ".pdf");
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
            } finally {
                selectedSingle = null;

            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }

    public void changedArticle() {
        listArticles = new ArrayList<>();
        listArticles = categorie.getArticles();

    }

    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

//                items = getFacade().searchAllNative(debut, fin, etatBonCommandeVente, client != null ? client.getId() : null);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreuPeriode")));
            }

        } catch (Exception e) {

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

    public SelectItem[] getItemsAvailableSelectOneArticle() {
        if (listArticles == null) {
            listArticles = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listArticles, true);
    }

    public SelectItem[] getItemsAvailableSelectOneDevis() {

        return JsfUtil.getSelectItems(listDeviss, true);

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

    public LigneBonCommandeVente getSelectedLigneBonCommandeVente() {
        return selectedLigneBonCommandeVente;
    }

    public void setSelectedLigneBonCommandeVente(LigneBonCommandeVente selectedLigneBonCommandeVente) {
        this.selectedLigneBonCommandeVente = selectedLigneBonCommandeVente;
    }

    public LigneBonCommandeVente getSelectedLigneBonCommandeVenteSingle() {
        return selectedLigneBonCommandeVenteSingle;
    }

    public void setSelectedLigneBonCommandeVenteSingle(LigneBonCommandeVente selectedLigneBonCommandeVenteSingle) {
        this.selectedLigneBonCommandeVenteSingle = selectedLigneBonCommandeVenteSingle;
    }

    public LigneBonCommandeVente getSelectedLigneBonCommandeVenteModif() {
        return selectedLigneBonCommandeVenteModif;
    }

    public void setSelectedLigneBonCommandeVenteModif(LigneBonCommandeVente selectedLigneBonCommandeVenteModif) {
        this.selectedLigneBonCommandeVenteModif = selectedLigneBonCommandeVenteModif;
    }

    public boolean isAnnulation() {
        return annulation;
    }

    public void setAnnulation(boolean annulation) {
        this.annulation = annulation;
    }

    public List<ParametrageTaxe> getListParametrageTaxeEntreprise() {
        return listParametrageTaxeEntreprise;
    }

    public void setListParametrageTaxeEntreprise(List<ParametrageTaxe> listParametrageTaxeEntreprise) {
        this.listParametrageTaxeEntreprise = listParametrageTaxeEntreprise;
    }

    public List<ParametrageTaxe> getSelectedListParametrageTaxe() {
        return selectedListParametrageTaxe;
    }

    public void setSelectedListParametrageTaxe(List<ParametrageTaxe> selectedListParametrageTaxe) {
        this.selectedListParametrageTaxe = selectedListParametrageTaxe;
    }

    public TaxesBonCommandeVente getSelectedSingleTaxe() {
        return selectedSingleTaxe;
    }

    public void setSelectedSingleTaxe(TaxesBonCommandeVente selectedSingleTaxe) {
        this.selectedSingleTaxe = selectedSingleTaxe;
    }

    public TaxesBonCommandeVente getSelectedTaxe() {
        return selectedTaxe;
    }

    public void setSelectedTaxe(TaxesBonCommandeVente selectedTaxe) {
        this.selectedTaxe = selectedTaxe;
    }

    public List<TaxesBonCommandeVente> getListTaxeBonCommandeVente() {
        return listTaxeBonCommandeVente;
    }

    public void setListTaxeBonCommandeVente(List<TaxesBonCommandeVente> listTaxeBonCommandeVente) {
        this.listTaxeBonCommandeVente = listTaxeBonCommandeVente;
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

    public Integer getEtatBonCommandeVente() {
        return etatBonCommandeVente;
    }

    public void setEtatBonCommandeVente(Integer etatBonCommandeVente) {
        this.etatBonCommandeVente = etatBonCommandeVente;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ParametrageEntreprise getParametrageEntreprise() {
        return parametrageEntreprise;
    }

    public void setParametrageEntreprise(ParametrageEntreprise parametrageEntreprise) {
        this.parametrageEntreprise = parametrageEntreprise;
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

    public BonCommandeVente getBonCommandeVente(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = BonCommandeVente.class)
    public static class BonCommandeVenteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BonCommandeVenteController controller = (BonCommandeVenteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bonCommandeVenteController");
            return controller.getBonCommandeVente(getKey(value));
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
            if (object instanceof BonCommandeVente) {
                BonCommandeVente o = (BonCommandeVente) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + BonCommandeVente.class.getName());
            }
        }

    }

}
