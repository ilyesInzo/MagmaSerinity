package com.magma.controller;

import com.itextpdf.text.DocumentException;
import com.magma.bibliotheque.FonctionsMathematiques;
import com.magma.bibliotheque.FonctionsString;
import com.magma.bibliotheque.GenerationPdf;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Article;
import com.magma.entity.BonCommandeVente;
import com.magma.entity.BonLivraison;
import com.magma.entity.Devis;
import com.magma.entity.Categorie;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.Facture;
import com.magma.entity.LigneBonCommandeVente;
import com.magma.entity.LigneBonLivraison;
import com.magma.entity.LigneDevis;
import com.magma.entity.LigneFacture;
import com.magma.entity.LigneRetour;
import com.magma.entity.ParametrageTaxe;
import com.magma.entity.PrefixBonCommandeVente;
import com.magma.entity.PrefixBonLivraison;
import com.magma.entity.PrefixDevis;
import com.magma.entity.PrefixFacture;
import com.magma.entity.Retour;
import com.magma.entity.TaxesBonCommandeVente;
import com.magma.entity.TaxesDevis;
import com.magma.entity.TaxesFacture;
import com.magma.entity.Utilisateur;
import com.magma.session.BonCommandeVenteFacadeLocal;
import com.magma.session.BonLivraisonFacadeLocal;
import com.magma.session.DevisFacadeLocal;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.FactureFacadeLocal;
import com.magma.session.LigneBonCommandeVenteFacadeLocal;
import com.magma.session.LigneBonLivraisonFacadeLocal;
import com.magma.session.LigneDevisFacadeLocal;
import com.magma.session.LigneFactureFacadeLocal;
import com.magma.session.LigneRetourFacadeLocal;
import com.magma.session.ParametrageTaxeFacadeLocal;
import com.magma.session.PrefixBonCommandeVenteFacadeLocal;
import com.magma.session.PrefixBonLivraisonFacadeLocal;
import com.magma.session.PrefixDevisFacadeLocal;
import com.magma.session.PrefixFactureFacadeLocal;
import com.magma.session.RetourFacadeLocal;
import com.magma.session.TaxesBonCommandeVenteFacadeLocal;
import com.magma.session.TaxesDevisFacadeLocal;
import com.magma.session.TaxesFactureFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.File;
import java.io.FileInputStream;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@ManagedBean(name= "devisController")
@SessionScoped
public class DevisController implements Serializable {

    private Devis selected;
    private Devis selectedSingle;
    private List<Devis> items = null;
    private Utilisateur utilisateur;
    @EJB
    private DevisFacadeLocal ejbFacade;
    @EJB
    private PrefixDevisFacadeLocal ejbFacadePrefixDevis;
    @EJB
    private LigneDevisFacadeLocal ejbFacadeLigneDevis;
    @EJB
    private CategorieClientFacadeLocal ejbFacadeCatgorieClient;
    private CategorieClient categorieClient = null;

    @EJB
    private ClientFacadeLocal ejbFacadeClient;

    @EJB
    private ParametrageTaxeFacadeLocal ejbFacadeParametrageTaxe;

    @EJB
    private TaxesDevisFacadeLocal ejbFacadeTaxeDevis;

    @EJB
    private BonLivraisonFacadeLocal ejbFacadeBonLivraison;
    
    @EJB
    private LigneBonLivraisonFacadeLocal ejbFacadeLigneBonLivraison;
    
    @EJB
    private PrefixBonLivraisonFacadeLocal ejbFacadePrefixBonLivraison;
    
    @EJB
    private LigneFactureFacadeLocal ejbFacadeLigneFacture;

    @EJB
    private FactureFacadeLocal ejbFacadeFacture;
    @EJB
    private PrefixFactureFacadeLocal ejbFacadePrefixFacture;
    @EJB
    private TaxesFactureFacadeLocal ejbFacadeTaxeFacture;
    @EJB
    private RetourFacadeLocal ejbFacadeRetour;
    @EJB
    private LigneRetourFacadeLocal ejbFacadeLigneRetour;
    
    @EJB
    private BonCommandeVenteFacadeLocal ejbFacadeBonCommandeVente;
    
    @EJB
    private LigneBonCommandeVenteFacadeLocal ejbFacadeLigneBonCommandeVente;
    
    @EJB
    private PrefixBonCommandeVenteFacadeLocal ejbFacadePrefixBonCommandeVente;
    @EJB
    private TaxesBonCommandeVenteFacadeLocal ejbFacadeTaxeBonCommandeVente;
    
    private List<Client> listClient = null;

    private Categorie categorie;

    private boolean errorMsg;
    private Long idTemp;
    private Devis devis;
    private long idEntreprise = 0;
    private boolean annulation = true;
    private BigDecimal oldPrix;
    private BigDecimal oldQuantity;
    private double oldRemise;
    //private BigDecimal ancienMontantHT;
    private LigneDevis selectedLigneDevis;
    private LigneDevis selectedLigneDevisSingle;
    private LigneDevis selectedLigneDevisModif;
    private List<LigneDevis> LigneDevisTemps;
    private List<LigneDevis> AncienLigneDevis;
    private List<CategorieClient> listCategorieClient = null;
    private List<Article> listArticles = null;
    private List<ParametrageTaxe> listParametrageTaxeEntreprise = null;
    private List<ParametrageTaxe> selectedListParametrageTaxe = null;
    private TaxesDevis selectedSingleTaxe;
    private TaxesDevis selectedTaxe;
    private List<TaxesDevis> listTaxeDevis = null;
    private List<TaxesDevis> listTaxeSuppresssion = null;

    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Integer etatDevis;
    private Client client;

    public DevisController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (devis.getIdEntrepriseSuivi() != null && devis.getIdEntrepriseSuivi() != 0) {
                idEntreprise = devis.getIdEntrepriseSuivi();
            } else {
                idEntreprise = devis.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GDevis", "MVente",null, utilisateur);
            //MenuTemplate.menuFonctionnalitesModules("GDevis", utilisateur);
            /* if (devis.getIdEntrepriseSuivi() != null && devis.getIdEntrepriseSuivi() != 0) {
                idEntreprise = devis.getIdEntrepriseSuivi();
            } else {
                idEntreprise = devis.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../devis/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Devis> getItems() {
        try {
            if (items == null) {
                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.Dev_DateDevis between '" + debut + "' and '" + fin + "' ";
                items = getFacade().findAllNative(clause + " order by o.Dev_DateDevis desc");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- DevisController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Devis getSelected() {
        return selected;
    }

    public void setSelected(Devis selected) {
        this.selected = selected;
    }

    public Devis getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Devis selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private DevisFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        selectedLigneDevis = null;
        selectedLigneDevisSingle = null;
        selectedLigneDevisModif = null;
        categorie = null;
        //client = null;
        categorieClient = null;

        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            /*annulation = false;
            if (selected.getIdDevis() == null && selected.getListEncaissementDeviss().isEmpty()) {
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
        selected = new Devis();
        selected.setDateDevis(new Date());
        errorMsg = false;
        selectedLigneDevis = new LigneDevis();
        selectedLigneDevis.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneDevis.setTvaArticle(BigDecimal.ZERO);
        selectedLigneDevis.setQuantite(BigDecimal.ZERO);
        selectedLigneDevis.setRemise(BigDecimal.ZERO);
        selectedLigneDevis.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneDevis.setTotalHT(BigDecimal.ZERO);
        selectedLigneDevis.setTotalTTC(BigDecimal.ZERO);

        selectedLigneDevisModif = new LigneDevis();
        selectedLigneDevisModif.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneDevisModif.setTvaArticle(BigDecimal.ZERO);
        selectedLigneDevisModif.setQuantite(BigDecimal.ZERO);
        selectedLigneDevisModif.setRemise(BigDecimal.ZERO);
        selectedLigneDevisModif.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneDevisModif.setTotalHT(BigDecimal.ZERO);
        selectedLigneDevisModif.setTotalTTC(BigDecimal.ZERO);

        selectedLigneDevisSingle = new LigneDevis();
        selectedLigneDevisSingle.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneDevisSingle.setTvaArticle(BigDecimal.ZERO);
        selectedLigneDevisSingle.setQuantite(BigDecimal.ZERO);
        selectedLigneDevisSingle.setRemise(BigDecimal.ZERO);
        selectedLigneDevisSingle.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneDevisSingle.setTotalHT(BigDecimal.ZERO);
        selectedLigneDevisSingle.setTotalTTC(BigDecimal.ZERO);

        selectedListParametrageTaxe = new ArrayList<>();
        listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");
        selected.setTotalTaxe(BigDecimal.ZERO);
        selected.setTypeVente(0);
        selected.setListeLigneDeviss(new ArrayList<LigneDevis>());
        selected.setListsTaxe(new ArrayList<TaxesDevis>());
        //selected.setEtat(-1);
        categorie = new Categorie();
        initTaxeDevis(listParametrageTaxeEntreprise);
        return "Create";
    }

    public String create() {

        /*try {*/
        List<PrefixDevis> listPrefixDeviss = null;
        PrefixDevis prefixDevis = null;
        listPrefixDeviss = ejbFacadePrefixDevis.findAll(" where o.supprimer = 0 ");
        String compteur = "";
        if (listPrefixDeviss != null && !listPrefixDeviss.isEmpty()) {
            prefixDevis = listPrefixDeviss.get(0);
            compteur = prefixDevis.getLibellePrefixe() + FonctionsString.retourMotSelonLongeur(prefixDevis.getCompteur() + "", 6);

        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CreerUnPrefixe")));
            return null;
        }

        errorMsg = getFacade().verifierUniqueNumero(compteur);

        if (errorMsg == false) {

            if (selected.getListeLigneDeviss() != null && !selected.getListeLigneDeviss().isEmpty()) {
                List<TaxesDevis> listTaxesTemps = new ArrayList<>();
                LigneDevisTemps = selected.getListeLigneDeviss();

//                    for (LigneDevis ligneDevis : LigneDevisTemps) {
//                        if (ligneDevis.getQuantite().compareTo(BigDecimal.ZERO) == 0) {
//                            LigneDevisTemps.remove(ligneDevis);
//                        }
//                    }
                for (int i = LigneDevisTemps.size() - 1; i >= 0; i--) {
                    if (LigneDevisTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 0) {
                        LigneDevisTemps.remove(LigneDevisTemps.get(i));
                    }
                }

                if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                    listTaxesTemps = selected.getListsTaxe();
                    selected.setListsTaxe(null);

                }

                selected.setNumero(compteur);
                selected.setListeLigneDeviss(null);
                /*                selected.setDateSynch(System.currentTimeMillis());
                selected.setSupprimer(false);*/
                //selected.setDateCreation(new Date());

                if (selected.getClient() != null) {
                    selected.setIdClient(selected.getClient().getId());
                    selected.setAssujettiTVA(selected.getClient().isAssujettiTVA());
                    selected.setLibelleClient(selected.getClient().getLibelle());

                }
                selected.setReste(selected.getMontantHT());
                selected.setTransFormTo(-1);
                getFacade().create(selected);

                for (LigneDevis ligneDevis : LigneDevisTemps) {
                    ligneDevis.setDevis(selected);
                }
                ejbFacadeLigneDevis.create(LigneDevisTemps);

                if (!listTaxesTemps.isEmpty()) {
                    for (TaxesDevis listTaxesTemp : listTaxesTemps) {
                        listTaxesTemp.setDevis(selected);
                        ejbFacadeTaxeDevis.create(listTaxesTemp);
                    }
                }

                prefixDevis.setCompteur(prefixDevis.getCompteur() + 1);
                ejbFacadePrefixDevis.edit(prefixDevis);

                return prepareList();

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
            System.out.println("Erreur- DevisController - create: " + e.getMessage());
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

            selectedLigneDevis = new LigneDevis();
            selectedLigneDevisModif = new LigneDevis();
            selectedLigneDevisSingle = new LigneDevis();

            selectedListParametrageTaxe = new ArrayList<>();
            listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");
            selectedTaxe = new TaxesDevis();

            listTaxeDevis = new ArrayList<>();
            listTaxeDevis = selected.getListsTaxe();
            listTaxeSuppresssion = new ArrayList<>();
            categorie = new Categorie();

            Client client = new Client();
            client.setId(selected.getIdClient());
            client.setAssujettiTVA(selected.isAssujettiTVA());
            client.setLibelle(selected.getLibelleClient());
            selected.setClient(client);

            //ancienMontantHT = selected.getMontantHT();
            for (LigneDevis ligneDevis : selected.getListeLigneDeviss()) {

                Article article = new Article();
                article.setId(ligneDevis.getIdArticle());
                article.setCode(ligneDevis.getCodeArticle());
                article.setLibelle(ligneDevis.getLibelleArticle());
                ligneDevis.setArticle(article);

            }

            AncienLigneDevis = new ArrayList<LigneDevis>(selected.getListeLigneDeviss());

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {

            if (selected.getListeLigneDeviss() != null && !selected.getListeLigneDeviss().isEmpty()) {

                List<TaxesDevis> listTaxesTemps = new ArrayList<>();
                if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {

                    listTaxesTemps = selected.getListsTaxe();
                    selected.setListsTaxe(null);
                }

                LigneDevisTemps = new ArrayList<LigneDevis>(selected.getListeLigneDeviss());
                selected.setListeLigneDeviss(null);
                getFacade().edit(selected);

                //LigneDevisTemps = selected.getListeLigneDeviss();
                for (LigneDevis ligneDevis : AncienLigneDevis) {

                    int index = LigneDevisTemps.indexOf(ligneDevis);

                    if (index == -1) {

                        ejbFacadeLigneDevis.remove(ligneDevis);
                    } else {
                        //ceci pour remédier au cas ou le stock est supprimer puis réajouter sans passer par la modif
                        LigneDevisTemps.get(index).setId(ligneDevis.getId());
                        LigneDevisTemps.get(index).setDevis(selected);
                        ejbFacadeLigneDevis.edit(LigneDevisTemps.get(index));

                    }

                }
                sauvegardeNouvelleLigneDevis();

                //les taxes a suuprimer
                if (listTaxeSuppresssion != null && !listTaxeSuppresssion.isEmpty()) {
                    ejbFacadeTaxeDevis.remove(listTaxeSuppresssion);
                }

                if (!listTaxesTemps.isEmpty()) {
                    for (TaxesDevis taxeDevis : listTaxesTemps) {
                        taxeDevis.setDevis(selected);
                        if (taxeDevis.getId() != null) {
                            ejbFacadeTaxeDevis.edit(taxeDevis);
                        } else {
                            ejbFacadeTaxeDevis.create(taxeDevis);
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
            System.out.println("Erreur- DevisController - update: " + e.getMessage());
            return null;
        }
    }

    private void sauvegardeNouvelleLigneDevis() {

        int j = 0;
        //boolean trouve = false;

        for (int i = 0; i < LigneDevisTemps.size(); i++) {

            if (!AncienLigneDevis.contains(LigneDevisTemps.get(i)) && LigneDevisTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 1) {
                LigneDevisTemps.get(i).setDevis(selected);
                ejbFacadeLigneDevis.create(LigneDevisTemps.get(i));
            }

        }

    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<DevisPVT> temps = ejbFacadeDevisPVT.findAll("where o.idDevis = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- DevisController - performDestroy: " + e.getMessage());
        }
    }

    public void deleteFromListLigneDevis() {

        if (selectedLigneDevisSingle != null) {
            selected.getListeLigneDeviss().remove(selectedLigneDevisSingle);
            recalculerTotal();
            updateListTaxe(selected.getListsTaxe());
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }

    public void validerDetailArticle() {
        if (selectedLigneDevis.getArticle() != null) {
            selectedLigneDevis.setIdArticle(selectedLigneDevis.getArticle().getId());
            int index = selected.getListeLigneDeviss().indexOf(selectedLigneDevis);
            if (index > -1) {
                selected.getListeLigneDeviss().get(index).setQuantite(selectedLigneDevis.getQuantite());
                selected.getListeLigneDeviss().get(index).setQuantiteMax(selectedLigneDevis.getQuantite());
                selected.getListeLigneDeviss().get(index).setPrixUnitaireHT(selectedLigneDevis.getPrixUnitaireHT());
                selected.getListeLigneDeviss().get(index).setPrixUnitaireApresRemise(selectedLigneDevis.getPrixUnitaireApresRemise());
                selected.getListeLigneDeviss().get(index).setRemise(selectedLigneDevis.getRemise());
                selected.getListeLigneDeviss().get(index).setTvaArticle(new BigDecimal(selectedLigneDevis.getTvaArticle() + ""));

                selected.getListeLigneDeviss().get(index).setTotalHT(selectedLigneDevis.getTotalHT());
                selected.getListeLigneDeviss().get(index).setTotalTVA(selectedLigneDevis.getTotalTVA());
                selected.getListeLigneDeviss().get(index).setTotalTTC(selectedLigneDevis.getTotalTTC());
                recalculerTotal();
                selectedLigneDevis = new LigneDevis();
                categorie = new Categorie();
            } else {
                selected.getListeLigneDeviss().add(selectedLigneDevis);
                recalculerTotal();
                selectedLigneDevis = new LigneDevis();
                categorie = new Categorie();
            }

            if (selected.getListsTaxe() != null && !selected.getListsTaxe().isEmpty()) {
                updateListTaxe(selected.getListsTaxe());
            }

        }
    }

    public void validerDetailArticleModif() {

        if (selectedLigneDevis.getQuantite().compareTo(selectedLigneDevis.getQuantiteMax()) <= 0) {
            recalculerTotal();
            selectedLigneDevis = new LigneDevis();

        } else {
            selectedLigneDevis.setPrixUnitaireHT(oldPrix);
            selectedLigneDevis.setQuantite(oldQuantity);
            changedTotalHtTotalTtc();
        }

    }

    public String rejeterDevis() {
        if (selected.getMotifRejetDevisVente() != null && selected.getMotifRejetDevisVente().getId() != 0) {
            selected.setEtat(3);
            getFacade().edit(selected);
            return prepareList();
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("MsgErreuAnnulation")));
            return null;
        }
    }

    public String approuverDevis() {

        
        if (selected.getTransFormTo() == 0) {

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

            if (!ejbFacadeBonCommandeVente.verifierUniqueNumero(compteur)) {
                BonCommandeVente BonCommandeVente = createCommande(compteur);
                selected.setEtat(4);

                selected.setIdDocumentTransform(BonCommandeVente.getId());
                selected.setNumeroDocumentTransform(BonCommandeVente.getNumero());
                getFacade().edit(selected);
                prefixBonCommandeVente.setCompteur(prefixBonCommandeVente.getCompteur() + 1);
                ejbFacadePrefixBonCommandeVente.edit(prefixBonCommandeVente);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }

        }//BL
        else if (selected.getTransFormTo() == 1) {

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
        else if (selected.getTransFormTo() == 2) {

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
    
    public BonCommandeVente createCommande(String numero) {

        BonCommandeVente bonCommandeVente = new BonCommandeVente();
        bonCommandeVente.setNumero(numero);
        bonCommandeVente.setCodeClient(selected.getCodeClient());
        bonCommandeVente.setLibelleClient(selected.getLibelleClient());
        bonCommandeVente.setIdClient(selected.getIdClient());
        bonCommandeVente.setDateBonCommandeVente(TraitementDate.debutJournee(new Date()));
        bonCommandeVente.setEtat(0);
        bonCommandeVente.setDateCreation(new Date());
        bonCommandeVente.setOrigine(1);
        bonCommandeVente.setIdDocumentOrigine(selected.getId());
        bonCommandeVente.setNumeroDocumentOrigine(selected.getNumero());
        bonCommandeVente.setTypeVente(0);
        bonCommandeVente.setMontantHT(selected.getMontantHT());
        bonCommandeVente.setMontantTVA(selected.getMontantTVA());
        bonCommandeVente.setMontantTTC(selected.getMontantTTC());
        bonCommandeVente.setTotalHT(selected.getTotalHT());
        bonCommandeVente.setTotalTVA(selected.getTotalTVA());
        bonCommandeVente.setTotalTTC(selected.getTotalTTC());
        bonCommandeVente.setTotalTaxe(selected.getTotalTaxe());
        bonCommandeVente.setReste(selected.getTotalTTC());
        ejbFacadeBonCommandeVente.create(bonCommandeVente);

        List<LigneBonCommandeVente> listLignebonCommandeVentes = new ArrayList<>();

        for (LigneDevis ligneDevis : selected.getListeLigneDeviss()) {
            LigneBonCommandeVente lignebonCommandeVente = new LigneBonCommandeVente();
            lignebonCommandeVente.setCodeArticle(ligneDevis.getCodeArticle());
            lignebonCommandeVente.setLibelleArticle(ligneDevis.getLibelleArticle());
            lignebonCommandeVente.setIdArticle(ligneDevis.getIdArticle());

            lignebonCommandeVente.setTvaArticle(ligneDevis.getTvaArticle());
            lignebonCommandeVente.setPrixUnitaireHT(ligneDevis.getPrixUnitaireHT());
            lignebonCommandeVente.setPrixUnitaireApresRemise(ligneDevis.getPrixUnitaireApresRemise());
            lignebonCommandeVente.setRemise(ligneDevis.getRemise());
            lignebonCommandeVente.setQuantite(ligneDevis.getQuantite());
            lignebonCommandeVente.setQuantiteMax(ligneDevis.getQuantiteMax());
            lignebonCommandeVente.setTotalTVA(ligneDevis.getTotalTVA());
            lignebonCommandeVente.setTotalHT(ligneDevis.getTotalHT());
            lignebonCommandeVente.setTotalTTC(ligneDevis.getTotalTTC());
            lignebonCommandeVente.setBonCommandeVente(bonCommandeVente);
            listLignebonCommandeVentes.add(lignebonCommandeVente);
        }
        ejbFacadeLigneBonCommandeVente.create(listLignebonCommandeVentes);

        List<TaxesBonCommandeVente> listTaxesbonCommandeVentes = new ArrayList<>();
        for (TaxesDevis taxesDevis : selected.getListsTaxe()) {
            TaxesBonCommandeVente taxesbonCommandeVente = new TaxesBonCommandeVente();
            taxesbonCommandeVente.setBonCommandeVente(bonCommandeVente);
            taxesbonCommandeVente.setApresTva(taxesDevis.isApresTva());
            taxesbonCommandeVente.setValeur(taxesDevis.getValeur());
            taxesbonCommandeVente.setMontant(taxesDevis.getMontant());
            taxesbonCommandeVente.setTypeTaxe(taxesDevis.getTypeTaxe());
            taxesbonCommandeVente.setOperation(taxesDevis.getOperation());
            taxesbonCommandeVente.setParametrageTaxe(taxesDevis.getParametrageTaxe());
            listTaxesbonCommandeVentes.add(taxesbonCommandeVente);
        }

        ejbFacadeTaxeBonCommandeVente.create(listTaxesbonCommandeVentes);

        //createRetour(listLignebonCommandeVentes,bonCommandeVente);
        return bonCommandeVente;
    }

    public BonLivraison createBonLivraison(String numero) {

        BonLivraison bonLivraison = new BonLivraison();
        bonLivraison.setNumero(numero);
        //bonLivraison.setCodeClient(selected.getCodeClient());
        bonLivraison.setLibelleClient(selected.getLibelleClient());
        bonLivraison.setIdClient(selected.getIdClient());
        bonLivraison.setOrigine(1);
        bonLivraison.setIdDocumentOrigine(selected.getId());
        bonLivraison.setNumeroDocumentOrigine(selected.getNumero());
        bonLivraison.setEtat(0);
        bonLivraison.setDateCreation(new Date());
        bonLivraison.setTypeVente(0);
        bonLivraison.setMontantHT(selected.getMontantHT());
        bonLivraison.setMontantTVA(selected.getMontantTVA());
        bonLivraison.setMontantTTC(selected.getMontantTTC());
        bonLivraison.setDateSynch(System.currentTimeMillis());
        bonLivraison.setReste(selected.getMontantHT());
        ejbFacadeBonLivraison.create(bonLivraison);

        List<LigneBonLivraison> listLigneBonLivraisons = new ArrayList<>();

        for (LigneDevis ligneDevis : selected.getListeLigneDeviss()) {
            LigneBonLivraison ligneBonLivraison = new LigneBonLivraison();
            ligneBonLivraison.setCodeArticle(ligneDevis.getCodeArticle());
            ligneBonLivraison.setLibelleArticle(ligneDevis.getLibelleArticle());
            ligneBonLivraison.setIdArticle(ligneDevis.getIdArticle());

            ligneBonLivraison.setTvaArticle(ligneDevis.getTvaArticle());
            ligneBonLivraison.setPrixUnitaireHT(ligneDevis.getPrixUnitaireHT());
            ligneBonLivraison.setPrixUnitaireApresRemise(ligneDevis.getPrixUnitaireApresRemise());
            ligneBonLivraison.setRemise(ligneDevis.getRemise());
            ligneBonLivraison.setQuantite(ligneDevis.getQuantite());
            ligneBonLivraison.setQuantiteMax(ligneDevis.getQuantiteMax());

            ligneBonLivraison.setTotalHT(ligneDevis.getTotalHT());
            ligneBonLivraison.setTotalTVA(ligneDevis.getTotalTVA());
            ligneBonLivraison.setTotalTTC(ligneDevis.getTotalTTC());
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
        facture.setOrigine(1);
        facture.setIdDocumentOrigine(selected.getId());
        facture.setNumeroDocumentOrigine(selected.getNumero());
        facture.setTypeVente(0);
        facture.setMontantHT(selected.getMontantHT());
        facture.setMontantTVA(selected.getMontantTVA());
        facture.setMontantTTC(selected.getMontantTTC());
        facture.setTotalHT(selected.getTotalHT());
        facture.setTotalTVA(selected.getTotalTVA());
        facture.setTotalTTC(selected.getTotalTTC());
        facture.setTotalTaxe(selected.getTotalTaxe());
        facture.setReste(selected.getTotalTTC());
        ejbFacadeFacture.create(facture);

        List<LigneFacture> listLigneFactures = new ArrayList<>();

        for (LigneDevis ligneDevis : selected.getListeLigneDeviss()) {
            LigneFacture ligneFacture = new LigneFacture();
            ligneFacture.setCodeArticle(ligneDevis.getCodeArticle());
            ligneFacture.setLibelleArticle(ligneDevis.getLibelleArticle());
            ligneFacture.setIdArticle(ligneDevis.getIdArticle());

            ligneFacture.setTvaArticle(ligneDevis.getTvaArticle());
            ligneFacture.setPrixUnitaireHT(ligneDevis.getPrixUnitaireHT());
            ligneFacture.setPrixUnitaireApresRemise(ligneDevis.getPrixUnitaireApresRemise());
            ligneFacture.setRemise(ligneDevis.getRemise());
            ligneFacture.setQuantite(ligneDevis.getQuantite());
            ligneFacture.setQuantiteMax(ligneDevis.getQuantiteMax());
            ligneFacture.setTotalTVA(ligneDevis.getTotalTVA());
            ligneFacture.setTotalHT(ligneDevis.getTotalHT());
            ligneFacture.setTotalTTC(ligneDevis.getTotalTTC());
            ligneFacture.setFacture(facture);
            listLigneFactures.add(ligneFacture);
        }
        ejbFacadeLigneFacture.create(listLigneFactures);

        List<TaxesFacture> listTaxesFactures = new ArrayList<>();
        for (TaxesDevis taxesDevis : selected.getListsTaxe()) {
            TaxesFacture taxesFacture = new TaxesFacture();
            taxesFacture.setFacture(facture);
            taxesFacture.setApresTva(taxesDevis.isApresTva());
            taxesFacture.setValeur(taxesDevis.getValeur());
            taxesFacture.setMontant(taxesDevis.getMontant());
            taxesFacture.setTypeTaxe(taxesDevis.getTypeTaxe());
            taxesFacture.setOperation(taxesDevis.getOperation());
            taxesFacture.setParametrageTaxe(taxesDevis.getParametrageTaxe());
            listTaxesFactures.add(taxesFacture);
        }

        ejbFacadeTaxeFacture.create(listTaxesFactures);

        //createRetour(listLigneFactures,facture);
        return facture;
    }

    private void createRetour(List<LigneFacture> listeLigneFactures, Facture facture) {

        Retour retour = new Retour();

        retour.setCodeClient(facture.getCodeClient());
        retour.setLibelleClient(facture.getLibelleClient());
        retour.setIdClient(facture.getIdClient());
        retour.setOrigineRetour(0);
        retour.setEtat(0);
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

        ejbFacadeLigneRetour.create(listLigneRetourTemps);

    }

    public void recalculerTotal() {

        selected.setMontantHT(BigDecimal.ZERO);
        selected.setMontantTVA(BigDecimal.ZERO);
        selected.setMontantTTC(BigDecimal.ZERO);
        for (LigneDevis ligneDevis : selected.getListeLigneDeviss()) {
            System.out.println("da" + selected.getMontantHT().add(ligneDevis.getTotalHT()));

            System.out.println("da" + selected.getMontantTVA().add(ligneDevis.getTotalTVA()));

            selected.setMontantHT(selected.getMontantHT().add(ligneDevis.getTotalHT()));
            selected.setMontantTVA(selected.getMontantTVA().add(ligneDevis.getTotalTVA()));
            selected.setMontantTTC(selected.getMontantTTC().add(ligneDevis.getTotalTTC()));
        }
        selected.setTotalHT(selected.getMontantHT());
        selected.setTotalTVA(selected.getMontantTVA());
        selected.setTotalTTC(selected.getMontantTTC());
    }

    public void listnerPrixUnitaire() {
        if (selectedLigneDevis.getArticle() != null) {
            selectedLigneDevis.setIdArticle(selectedLigneDevis.getArticle().getId());
            selectedLigneDevis.setCodeArticle(selectedLigneDevis.getArticle().getCode());
            selectedLigneDevis.setLibelleArticle(selectedLigneDevis.getArticle().getLibelle());
            selectedLigneDevis.setPrixUnitaireHT(selectedLigneDevis.getArticle().getPrixRevendeur());
            selectedLigneDevis.setTvaArticle(new BigDecimal(selectedLigneDevis.getArticle().getTva().getValeur()));
            selectedLigneDevis.setQuantite(BigDecimal.ZERO);
            selectedLigneDevis.setRemise(BigDecimal.ZERO);
            selectedLigneDevis.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneDevis.setTotalHT(BigDecimal.ZERO);
            selectedLigneDevis.setTotalTVA(BigDecimal.ZERO);
            selectedLigneDevis.setTotalTTC(BigDecimal.ZERO);
        }
    }

    public void changedTotalHtTotalTtc() {
        if (selected.getClient() == null || selectedLigneDevis.getLibelleArticle() == null) {
            selectedLigneDevis.setPrixUnitaireHT(BigDecimal.ZERO);
            selectedLigneDevis.setTvaArticle(BigDecimal.ZERO);
            selectedLigneDevis.setQuantite(BigDecimal.ZERO);
            selectedLigneDevis.setRemise(BigDecimal.ZERO);
            selectedLigneDevis.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneDevis.setTotalHT(BigDecimal.ZERO);
            selectedLigneDevis.setTotalTVA(BigDecimal.ZERO);
            selectedLigneDevis.setTotalTTC(BigDecimal.ZERO);
        } else {
            selectedLigneDevis.setTotalHT(BigDecimal.ZERO);
            selectedLigneDevis.setTotalTVA(BigDecimal.ZERO);
            selectedLigneDevis.setTotalTTC(BigDecimal.ZERO);
            selectedLigneDevis.setQuantiteMax(selectedLigneDevis.getQuantite());
            selectedLigneDevis.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            BigDecimal prixRevendeur = selectedLigneDevis.getPrixUnitaireHT();
            if (selectedLigneDevis.getRemise().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal valRemise = FonctionsMathematiques.arrondiBigDecimal(prixRevendeur.multiply(selectedLigneDevis.getRemise()), 3);
                valRemise = FonctionsMathematiques.arrondiBigDecimal(valRemise.divide(new BigDecimal("100")), 3);
                prixRevendeur = prixRevendeur.subtract(valRemise);
            }
            selectedLigneDevis.setPrixUnitaireApresRemise(prixRevendeur);

            BigDecimal TotalHT = FonctionsMathematiques.arrondiBigDecimal((selectedLigneDevis.getPrixUnitaireApresRemise()).multiply(selectedLigneDevis.getQuantite()), 3);
            selectedLigneDevis.setTotalHT(TotalHT);

            if (selected.getClient().isAssujettiTVA()) {
                selectedLigneDevis.setTotalTVA(((selectedLigneDevis.getTotalHT().multiply(selectedLigneDevis.getTvaArticle()))).divide(BigDecimal.valueOf(100)));
                selectedLigneDevis.setTotalTTC(selectedLigneDevis.getTotalHT().add(selectedLigneDevis.getTotalTVA()));

            } else {
                selectedLigneDevis.setTotalTVA(BigDecimal.ZERO);
                selectedLigneDevis.setTotalTTC(TotalHT);
            }

            /*            BigDecimal valTemp = FonctionsMathematiques.arrondiBigDecimal(selectedLigneDevis.getMontantHT().multiply(selectedLigneDevis.getTvaArticle()), 3);
            selectedLigneDevis.setMontantTVA(FonctionsMathematiques.arrondiBigDecimal(valTemp.divide(BigDecimal.valueOf(100)), 3));
            selectedLigneDevis.setMontantTTC(selectedLigneDevis.getMontantHT().add(selectedLigneDevis.getMontantTVA()));*/
        }
    }

    //taxe devis
    public boolean verifierExistanceTaxe(TaxesDevis taxe, List<TaxesDevis> listTaxe) {
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

    public void calculerTotalTaxe(List<TaxesDevis> list) {
        selected.setTotalTaxe(BigDecimal.ZERO);
        for (int i = 0; i < list.size(); i++) {
            if (selected.getListsTaxe().get(i).getOperation().trim().equals("+")) {
                selected.setTotalTaxe((selected.getTotalTaxe()).add(list.get(i).getMontant()));
            } else {

                selected.setTotalTaxe((selected.getTotalTaxe()).subtract(list.get(i).getMontant()));
            }
        }
    }

    public void validerTaxeDevis() {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantHT());
            selected.setTotalTTC(selected.getMontantTTC());
        }
        System.err.println("selectedListParametrageTaxe.size() : " + selectedListParametrageTaxe.size());
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
                        TaxesDevis taxe = new TaxesDevis();
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
                        TaxesDevis taxe = new TaxesDevis();
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
                        TaxesDevis taxe = new TaxesDevis();
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
                        TaxesDevis taxe = new TaxesDevis();
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

    public void validerTaxeDevisEdit() {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantHT());
            selected.setTotalTTC(selected.getMontantTTC());
        }
        if (selectedListParametrageTaxe != null && !selectedListParametrageTaxe.isEmpty()) {

            for (ParametrageTaxe selectTaxe : selectedListParametrageTaxe) {
                int index = -1;
                for (int j = 0; j < listTaxeDevis.size(); j++) {
                    if (listTaxeDevis.get(j).getParametrageTaxe().getLibelle().trim().equals(selectTaxe.getLibelle())) {
                        index = j;
                    }
                }
                if (index > -1) {
                    System.out.println("taxe existe");
                } else {
                    if ((selectTaxe.getTypeTaxe().trim().equals("0")) && (!selectTaxe.isApresTva())) {
                        TaxesDevis taxe = new TaxesDevis();
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
                        if (!verifierExistanceTaxe(taxe, listTaxeDevis)) {
                            listTaxeDevis.add(taxe);
                        }
                    } //apresTva
                    else if ((selectTaxe.getTypeTaxe().trim().equals("0")) && (selectTaxe.isApresTva())) {
                        TaxesDevis taxe = new TaxesDevis();
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
                        if (!verifierExistanceTaxe(taxe, listTaxeDevis)) {
                            listTaxeDevis.add(taxe);
                        }
                        //valeurs fixe avant tva
                    } else if ((selectTaxe.getTypeTaxe().trim().equals("1")) && (!selectTaxe.isApresTva())) {
                        TaxesDevis taxe = new TaxesDevis();
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
                        if (!verifierExistanceTaxe(taxe, listTaxeDevis)) {
                            listTaxeDevis.add(taxe);
                        }
                    } //valeur fixe apres tva
                    else if ((selectTaxe.getTypeTaxe().trim().equals("1")) && (selectTaxe.isApresTva())) {
                        TaxesDevis taxe = new TaxesDevis();
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
                        if (!verifierExistanceTaxe(taxe, listTaxeDevis)) {
                            listTaxeDevis.add(taxe);
                        }
                    }
                }
            }
            calculerTotalTaxe(listTaxeDevis);
            updateListTaxe(listTaxeDevis);
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
            listTaxeDevis.remove(selectedSingleTaxe);
            listTaxeSuppresssion.add(selectedSingleTaxe);
            updateListTaxe(listTaxeDevis);

        }
    }

    public void updateListTaxe(List<TaxesDevis> list) {
        selected.setTotalTTC(selected.getMontantTTC());
        if (!list.isEmpty()) {
            for (TaxesDevis item : list) {

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

    public void initTaxeDevis(List<ParametrageTaxe> listParametrageTaxeEntreprise) {

        if ((selected.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (selected.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            selected.setTotalHT(selected.getMontantHT());
            selected.setTotalTTC(selected.getMontantTTC());
        }
        if (listParametrageTaxeEntreprise != null && !listParametrageTaxeEntreprise.isEmpty()) {

            for (ParametrageTaxe selectedTaxe : listParametrageTaxeEntreprise) {
                if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (!selectedTaxe.isApresTva())) {
                    TaxesDevis taxe = new TaxesDevis();
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
                    TaxesDevis taxe = new TaxesDevis();
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
                    TaxesDevis taxe = new TaxesDevis();
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
                    TaxesDevis taxe = new TaxesDevis();
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
            String dateDevis = TraitementDate.returnDate(selectedSingle.getDateCreation());
            String numeroDevis = selectedSingle.getNumero();

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
            ArrayList<String> ligneDevisEntete = new ArrayList();

            ligneDevisEntete.add("Reference");
            ligneDevisEntete.add("Produit");
            ligneDevisEntete.add("TVA");
            ligneDevisEntete.add("Quantite");
            ligneDevisEntete.add("Prix UT");
            ligneDevisEntete.add("Total HT");

            ArrayList<ArrayList<String>> ligneFactures = new ArrayList<ArrayList<String>>();

            ArrayList<String> ligneDevisInfo;

            for (int i = 0; i < selectedSingle.getListeLigneDeviss().size(); i++) {

                ligneDevisInfo = new ArrayList();
                //0:left 1: linea left 2:center 3:linea right 4:right
                ligneDevisInfo.add("1:" + selectedSingle.getListeLigneDeviss().get(i).getCodeArticle());
                ligneDevisInfo.add("1:" + selectedSingle.getListeLigneDeviss().get(i).getLibelleArticle());
                ligneDevisInfo.add("3:" + selectedSingle.getListeLigneDeviss().get(i).getTvaArticle() + "%");
                ligneDevisInfo.add("3:" + selectedSingle.getListeLigneDeviss().get(i).getQuantite());
                ligneDevisInfo.add("3:" + selectedSingle.getListeLigneDeviss().get(i).getPrixUnitaireApresRemise());
                ligneDevisInfo.add("3:" + selectedSingle.getListeLigneDeviss().get(i).getTotalHT());
                ligneFactures.add(i, ligneDevisInfo);
            }

            //addInvoiceSummarize
            ArrayList<String> devisSummarizeInfos = new ArrayList();

            devisSummarizeInfos.add("Total HT" + " : " + selectedSingle.getTotalHT());
            devisSummarizeInfos.add("Total TVA" + " : " + selectedSingle.getTotalTVA());
            devisSummarizeInfos.add("Total Taxe" + " : " + selectedSingle.getTotalTaxe());
            devisSummarizeInfos.add("Total TTC" + " : " + selectedSingle.getTotalTTC());

            GenerationPdf.generationPdf(image, path, "Devis", numeroDevis, dateDevis, entrepriseInfos, clientInfos, ligneDevisEntete, ligneFactures, devisSummarizeInfos);

            File file = new File(path);
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=" + "Devis_" + numeroDevis + ".pdf");
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

                items = getFacade().searchAllNative(debut, fin, etatDevis, client != null ? client.getId() : null);

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreuPeriode")));
            }

        } catch (Exception e) {

        }
    }

    public SelectItem[] getItemsAvailableSelectOneArticle() {
        if (listArticles == null) {
            listArticles = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listArticles, true);
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

    public LigneDevis getSelectedLigneDevis() {
        return selectedLigneDevis;
    }

    public void setSelectedLigneDevis(LigneDevis selectedLigneDevis) {
        this.selectedLigneDevis = selectedLigneDevis;
    }

    public LigneDevis getSelectedLigneDevisSingle() {
        return selectedLigneDevisSingle;
    }

    public void setSelectedLigneDevisSingle(LigneDevis selectedLigneDevisSingle) {
        this.selectedLigneDevisSingle = selectedLigneDevisSingle;
    }

    public LigneDevis getSelectedLigneDevisModif() {
        return selectedLigneDevisModif;
    }

    public void setSelectedLigneDevisModif(LigneDevis selectedLigneDevisModif) {
        this.selectedLigneDevisModif = selectedLigneDevisModif;
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

    public TaxesDevis getSelectedSingleTaxe() {
        return selectedSingleTaxe;
    }

    public void setSelectedSingleTaxe(TaxesDevis selectedSingleTaxe) {
        this.selectedSingleTaxe = selectedSingleTaxe;
    }

    public TaxesDevis getSelectedTaxe() {
        return selectedTaxe;
    }

    public void setSelectedTaxe(TaxesDevis selectedTaxe) {
        this.selectedTaxe = selectedTaxe;
    }

    public List<TaxesDevis> getListTaxeDevis() {
        return listTaxeDevis;
    }

    public void setListTaxeDevis(List<TaxesDevis> listTaxeDevis) {
        this.listTaxeDevis = listTaxeDevis;
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

    public Integer getEtatDevis() {
        return etatDevis;
    }

    public void setEtatDevis(Integer etatDevis) {
        this.etatDevis = etatDevis;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public Devis getDevis(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Devis.class)
    public static class DevisControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DevisController controller = (DevisController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "devisController");
            return controller.getDevis(getKey(value));
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
            if (object instanceof Devis) {
                Devis o = (Devis) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Devis.class.getName());
            }
        }

    }

}
