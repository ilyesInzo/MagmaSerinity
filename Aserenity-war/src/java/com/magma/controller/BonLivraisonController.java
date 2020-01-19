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
import com.magma.entity.Devis;
import com.magma.entity.Encaissement;
import com.magma.entity.EncaissementBonLivraison;
import com.magma.entity.Facture;
import com.magma.entity.LigneBonLivraison;
import com.magma.entity.LigneDevis;
import com.magma.entity.LigneFacture;
import com.magma.entity.LigneRetour;
import com.magma.entity.ParametrageTaxe;
import com.magma.entity.PrefixBonLivraison;
import com.magma.entity.PrefixFacture;
import com.magma.entity.Retour;
import com.magma.entity.TaxesFacture;
import com.magma.entity.Utilisateur;
import com.magma.session.BonLivraisonFacadeLocal;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.DevisFacadeLocal;
import com.magma.session.EncaissementFacadeLocal;
import com.magma.session.FactureFacadeLocal;
import com.magma.session.LigneBonLivraisonFacadeLocal;
import com.magma.session.LigneDevisFacadeLocal;
import com.magma.session.LigneFactureFacadeLocal;
import com.magma.session.LigneRetourFacadeLocal;
import com.magma.session.ParametrageTaxeFacadeLocal;
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

@ManagedBean(name= "bonLivraisonController")
@SessionScoped
public class BonLivraisonController implements Serializable {

    private BonLivraison selected;
    private BonLivraison selectedSingle;
    private List<BonLivraison> items = null;

    @EJB
    private BonLivraisonFacadeLocal ejbFacade;
    @EJB
    private PrefixBonLivraisonFacadeLocal ejbFacadePrefixBonLivraison;
    @EJB
    private LigneBonLivraisonFacadeLocal ejbFacadeLigneBonLivraison;
    @EJB
    private CategorieClientFacadeLocal ejbFacadeCatgorieClient;
    @EJB
    private LigneDevisFacadeLocal ejbFacadeLigneDevis;
    private CategorieClient categorieClient = null;
    @EJB
    private DevisFacadeLocal ejbFacadeDevis;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    @EJB
    private LigneFactureFacadeLocal ejbFacadeLigneFacture;

    @EJB
    private FactureFacadeLocal ejbFacadeFacture;
    @EJB
    private PrefixFactureFacadeLocal ejbFacadePrefixFacture;
    @EJB
    private EncaissementFacadeLocal ejbFacadeEncaissement;
    @EJB
    private ParametrageTaxeFacadeLocal ejbFacadeParametrageTaxe;
    @EJB
    private TaxesFactureFacadeLocal ejbFacadeTaxeFacture;
    @EJB
    private RetourFacadeLocal ejbFacadeRetour;
    @EJB
    private LigneRetourFacadeLocal ejbFacadeLigneRetour;

    private List<Client> listClient = null;

    private Categorie categorie;

    private boolean errorMsg;
    private Long idTemp;
    private BonLivraison bonLivraison;
    private long idEntreprise = 0;
    private boolean annulation = true;
    private BigDecimal oldPrix;
    private BigDecimal oldQuantity;
    private double oldRemise;
    //private BigDecimal ancienMontantHT;
    private LigneBonLivraison selectedLigneBonLivraison;
    private LigneBonLivraison selectedLigneBonLivraisonSingle;
    private LigneBonLivraison selectedLigneBonLivraisonModif;
    private List<LigneBonLivraison> LigneBonLivraisonTemps;
    private List<LigneBonLivraison> AncienLigneBonLivraison;
    private List<CategorieClient> listCategorieClient = null;
    private List<Article> listArticles = null;
    private List<Devis> listDeviss = new ArrayList();
    private Utilisateur utilisateur;

    
    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Integer etatBonLivraison;
    private Integer origineBonLivraison;
    private Client client;
    private boolean retour;
    
    
    public BonLivraisonController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (bonLivraison.getIdEntrepriseSuivi() != null && bonLivraison.getIdEntrepriseSuivi() != 0) {
                idEntreprise = bonLivraison.getIdEntrepriseSuivi();
            } else {
                idEntreprise = bonLivraison.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
            MenuTemplate.menuFonctionnalitesModules("GBonLivraison", "MVente", null,utilisateur);
          
           // MenuTemplate.menuFonctionnalitesModules("GBonLivraison", utilisateur);
            /*if (bonLivraison.getIdEntrepriseSuivi() != null && bonLivraison.getIdEntrepriseSuivi() != 0) {
                idEntreprise = bonLivraison.getIdEntrepriseSuivi();
            } else {
                idEntreprise = bonLivraison.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../bonLivraison/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<BonLivraison> getItems() {
        try {
            if (items == null) {
                
                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.BLiv_DateBonLivraison between '" + debut + "' and '" + fin + "' ";
                System.out.println(""+clause);
                items = getFacade().findAllNative(clause + " order by o.BLiv_DateBonLivraison desc");
                
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- BonLivraisonController - getItems: " + e.getMessage());
            return null;
        }
    }

    public BonLivraison getSelected() {
        return selected;
    }

    public void setSelected(BonLivraison selected) {
        this.selected = selected;
    }

    public BonLivraison getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(BonLivraison selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private BonLivraisonFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        selectedLigneBonLivraison = null;
        selectedLigneBonLivraisonSingle = null;
        selectedLigneBonLivraisonModif = null;
        categorie = null;
        //client = null;
        categorieClient = null;

        return "List";
    }

    public String prepareView() {
        if (selected != null) {

            annulation = false;
            if (selected.getIdFacture() == null && selected.getListEncaissementBonLivraisons().isEmpty()) {
                annulation = true;
            }

            return "View";
        }
        return "List";
    }

    public String prepareCreate() {

        categorieClient = new CategorieClient();
        selected = new BonLivraison();
        selected.setDateBonLivraison(new Date());
        errorMsg = false;
        selectedLigneBonLivraison = new LigneBonLivraison();
        selectedLigneBonLivraison.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonLivraison.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonLivraison.setQuantite(BigDecimal.ZERO);
        selectedLigneBonLivraison.setRemise(BigDecimal.ZERO);
        selectedLigneBonLivraison.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonLivraison.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonLivraison.setTotalTTC(BigDecimal.ZERO);

        selectedLigneBonLivraisonModif = new LigneBonLivraison();
        selectedLigneBonLivraisonModif.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonLivraisonModif.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonLivraisonModif.setQuantite(BigDecimal.ZERO);
        selectedLigneBonLivraisonModif.setRemise(BigDecimal.ZERO);
        selectedLigneBonLivraisonModif.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonLivraisonModif.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonLivraisonModif.setTotalTTC(BigDecimal.ZERO);

        selectedLigneBonLivraisonSingle = new LigneBonLivraison();
        selectedLigneBonLivraisonSingle.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonLivraisonSingle.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonLivraisonSingle.setQuantite(BigDecimal.ZERO);
        selectedLigneBonLivraisonSingle.setRemise(BigDecimal.ZERO);
        selectedLigneBonLivraisonSingle.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonLivraisonSingle.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonLivraisonSingle.setTotalTTC(BigDecimal.ZERO);
        selected.setTypeVente(0);
        selected.setListeLigneBonLivraisons(new ArrayList<LigneBonLivraison>());

        categorie = new Categorie();
        return "Create";
    }

    public String create() {

        /*try {*/
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

        errorMsg = getFacade().verifierUniqueNumero(compteur);

        if (errorMsg == false) {

            if (selected.getListeLigneBonLivraisons() != null && !selected.getListeLigneBonLivraisons().isEmpty()) {

                if (selected.getOrigineBonLivraison() == 2) {

                    LigneBonLivraisonTemps = selected.getListeLigneBonLivraisons();

//                    for (LigneBonLivraison ligneBonLivraison : LigneBonLivraisonTemps) {
//                        if (ligneBonLivraison.getQuantite().compareTo(BigDecimal.ZERO) == 0) {
//                            LigneBonLivraisonTemps.remove(ligneBonLivraison);
//                        }
//                    }
                    for (int i = LigneBonLivraisonTemps.size() - 1; i >= 0; i--) {
                        if (LigneBonLivraisonTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 0) {
                            LigneBonLivraisonTemps.remove(LigneBonLivraisonTemps.get(i));
                        }
                    }

                    selected.setNumero(compteur);

                    selected.setListeLigneBonLivraisons(null);
                    selected.setDateSynch(System.currentTimeMillis());
                    selected.setSupprimer(false);
                    //selected.setDateCreation(new Date());

                    if (selected.getClient() != null) {
                        selected.setIdClient(selected.getClient().getId());
                        selected.setAssujettiTVA(selected.getClient().isAssujettiTVA());
                        selected.setLibelleClient(selected.getClient().getLibelle());

                    }
                    selected.setReste(selected.getMontantHT());
                    selected.setEtat(0);
                    selected.setOrigineBonLivraison(2);
                    getFacade().create(selected);

                    for (LigneBonLivraison ligneBonLivraison : LigneBonLivraisonTemps) {
                        ligneBonLivraison.setBonLivraison(selected);
                    }
                    ejbFacadeLigneBonLivraison.create(LigneBonLivraisonTemps);

                    prefixBonLivraison.setCompteur(prefixBonLivraison.getCompteur() + 1);
                    ejbFacadePrefixBonLivraison.edit(prefixBonLivraison);

                    return prepareList();

                } else if (selected.getOrigineBonLivraison() == 1) {

                    selected.setNumero(compteur);

                    createBonLivraisonFromDevis();

                    selected.getDevis().setIdBl(selected.getId());
                    selected.getDevis().setTransFormTo(0);
                    selected.getDevis().setEtat(4);
                    ejbFacadeDevis.edit(selected.getDevis());

                    prefixBonLivraison.setCompteur(prefixBonLivraison.getCompteur() + 1);
                    ejbFacadePrefixBonLivraison.edit(prefixBonLivraison);
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
            System.out.println("Erreur- BonLivraisonController - create: " + e.getMessage());
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

            selectedLigneBonLivraison = new LigneBonLivraison();
            selectedLigneBonLivraisonModif = new LigneBonLivraison();
            selectedLigneBonLivraisonSingle = new LigneBonLivraison();
            categorie = new Categorie();
            listDeviss = new ArrayList();
            Client client = new Client();
            client.setId(selected.getIdClient());
            client.setAssujettiTVA(selected.isAssujettiTVA());
            client.setLibelle(selected.getLibelleClient());
            selected.setClient(client);

            //ancienMontantHT = selected.getMontantHT();
            for (LigneBonLivraison ligneBonLivraison : selected.getListeLigneBonLivraisons()) {

                Article article = new Article();
                article.setId(ligneBonLivraison.getIdArticle());
                article.setCode(ligneBonLivraison.getCodeArticle());
                article.setLibelle(ligneBonLivraison.getLibelleArticle());
                ligneBonLivraison.setArticle(article);

            }

            AncienLigneBonLivraison = new ArrayList<LigneBonLivraison>(selected.getListeLigneBonLivraisons());

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {

            if (selected.getListeLigneBonLivraisons() != null && !selected.getListeLigneBonLivraisons().isEmpty()) {

                LigneBonLivraisonTemps = new ArrayList<LigneBonLivraison>(selected.getListeLigneBonLivraisons());
                selected.setListeLigneBonLivraisons(null);

                BigDecimal montantPaye = BigDecimal.ZERO;
                for (EncaissementBonLivraison item : selected.getListEncaissementBonLivraisons()) {
                    if (item.isSupprimer() == false) {
                        montantPaye = montantPaye.add(item.getMontant());
                    }
                }
                selected.setReste(selected.getMontantHT().subtract(montantPaye));

                getFacade().edit(selected);

                //LigneBonLivraisonTemps = selected.getListeLigneBonLivraisons();
                for (LigneBonLivraison ligneBonLivraison : AncienLigneBonLivraison) {

                    int index = LigneBonLivraisonTemps.indexOf(ligneBonLivraison);

                    if (index == -1) {

                        ejbFacadeLigneBonLivraison.remove(ligneBonLivraison);
                    } else {
                        //ceci pour remédier au cas ou le stock est supprimer puis réajouter sans passer par la modif
                        LigneBonLivraisonTemps.get(index).setId(ligneBonLivraison.getId());
                        LigneBonLivraisonTemps.get(index).setBonLivraison(selected);
                        ejbFacadeLigneBonLivraison.edit(LigneBonLivraisonTemps.get(index));

                    }

                }
                sauvegardeNouvelleLigneFacture();

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
            System.out.println("Erreur- BonLivraisonController - update: " + e.getMessage());
            return null;
        }
    }

    private void sauvegardeNouvelleLigneFacture() {

        int j = 0;
        //boolean trouve = false;

        for (int i = 0; i < LigneBonLivraisonTemps.size(); i++) {

            if (!AncienLigneBonLivraison.contains(LigneBonLivraisonTemps.get(i)) && LigneBonLivraisonTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 1) {
                LigneBonLivraisonTemps.get(i).setBonLivraison(selected);
                ejbFacadeLigneBonLivraison.create(LigneBonLivraisonTemps.get(i));
            }

        }

    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<BonLivraisonPVT> temps = ejbFacadeBonLivraisonPVT.findAll("where o.idBonLivraison = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- BonLivraisonController - performDestroy: " + e.getMessage());
        }
    }

    public void deleteFromListLigneBonLivraison() {

        if (selectedLigneBonLivraisonSingle != null) {
            selected.getListeLigneBonLivraisons().remove(selectedLigneBonLivraisonSingle);
            recalculerTotal();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }

    public void validerDetailArticle() {
        if (selectedLigneBonLivraison.getArticle() != null) {
            selectedLigneBonLivraison.setIdArticle(selectedLigneBonLivraison.getArticle().getId());
            int index = selected.getListeLigneBonLivraisons().indexOf(selectedLigneBonLivraison);
            if (index > -1) {
                selected.getListeLigneBonLivraisons().get(index).setQuantite(selectedLigneBonLivraison.getQuantite());
                selected.getListeLigneBonLivraisons().get(index).setQuantiteMax(selectedLigneBonLivraison.getQuantite());
                selected.getListeLigneBonLivraisons().get(index).setPrixUnitaireHT(selectedLigneBonLivraison.getPrixUnitaireHT());
                selected.getListeLigneBonLivraisons().get(index).setPrixUnitaireApresRemise(selectedLigneBonLivraison.getPrixUnitaireApresRemise());
                selected.getListeLigneBonLivraisons().get(index).setRemise(selectedLigneBonLivraison.getRemise());
                selected.getListeLigneBonLivraisons().get(index).setTvaArticle(new BigDecimal(selectedLigneBonLivraison.getTvaArticle() + ""));
                selected.getListeLigneBonLivraisons().get(index).setTotalHT(selectedLigneBonLivraison.getTotalHT());
                selected.getListeLigneBonLivraisons().get(index).setTotalTVA(selectedLigneBonLivraison.getTotalTVA());
                selected.getListeLigneBonLivraisons().get(index).setTotalTTC(selectedLigneBonLivraison.getTotalTTC());
                recalculerTotal();
                selectedLigneBonLivraison = new LigneBonLivraison();
                categorie = new Categorie();
            } else {
                selected.getListeLigneBonLivraisons().add(selectedLigneBonLivraison);
                recalculerTotal();
                selectedLigneBonLivraison = new LigneBonLivraison();
                categorie = new Categorie();
            }
        }
    }

    public void validerDetailArticleModif() {

        if (selectedLigneBonLivraison.getQuantite().compareTo(selectedLigneBonLivraison.getQuantiteMax()) <= 0) {
            recalculerTotal();
            selectedLigneBonLivraison = new LigneBonLivraison();

        } else {
            selectedLigneBonLivraison.setPrixUnitaireHT(oldPrix);
            selectedLigneBonLivraison.setQuantite(oldQuantity);
            changedTotalHtTotalTtc();
        }

    }

    public String approuverBonLivraison() {

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
            //selected.setEtat(4);
            selected.setIdFacture(facture.getId());
            getFacade().edit(selected);
            prefixFacture.setCompteur(prefixFacture.getCompteur() + 1);
            ejbFacadePrefixFacture.edit(prefixFacture);
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), compteur + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
            return null;
        }

        return prepareList();
    }

    public Facture createFacture(String numero) {

        Facture facture = new Facture();
        facture.setNumero(numero);
        //facture.setCodeClient(selected.getCodeClient());
        facture.setLibelleClient(selected.getLibelleClient());
        facture.setIdClient(selected.getIdClient());

        facture.setDateCreation(new Date());
        facture.setDateFacture(TraitementDate.debutJournee(new Date()));
        facture.setOrigineFacture(2);
        facture.setTypeVente(0);
        facture.setMontantHT(selected.getMontantHT());
        facture.setMontantTVA(selected.getMontantTVA());
        facture.setMontantTTC(selected.getMontantTTC());
        facture.setTotalHT(selected.getMontantHT());
        facture.setTotalTVA(selected.getMontantTVA());
        facture.setTotalTTC(selected.getMontantTTC());

        // depend des taxes factures  => par default on mettra ces valeurs comme si pas de taxe dans la table
        facture.setTotalTaxe(BigDecimal.ZERO);
        // reste facture = reste bl + tva + taxes
        facture.setReste(selected.getReste().add(selected.getMontantTVA()));

        facture.setEtat(selected.getEtat());
        //facture.setIdDevis(selected.getId());
        ejbFacadeFacture.create(facture);
        facture.setListsTaxe(new ArrayList<>());
        List<ParametrageTaxe> listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");

        initTaxeFacture(listParametrageTaxeEntreprise, facture);

       /* if (selected.isAssujettiTVA()) {

            BigDecimal montantTVA = BigDecimal.ZERO;
            for (LigneBonLivraison ligneBonLivraison : selected.getListeLigneBonLivraisons()) {

                montantTVA = montantTVA.add(((ligneBonLivraison.getTotalHT().multiply(ligneBonLivraison.getTvaArticle()))).divide(BigDecimal.valueOf(100)));

            }
            facture.setReste(facture.getReste().add(montantTVA));

        }*/

        // si le TTC a augmenter (on ne va pas prendre la diminution car probleme si paiement)
        // donc l'etat et le reste doivent changé
        // si la bon de livraison a été totalement payé et que il y'a taxe a ajouté => alors l'etat facure est partiellement payé et le reste augmente par le Total de taxt
        // sinon uniquement le reste augmentra
        if (facture.getTotalTaxe().compareTo(BigDecimal.ZERO) == 1) {
            facture.setReste(facture.getReste().add(facture.getTotalTaxe()));
            // paiement Total
            if (selected.getEtat() == 1) {
                facture.setEtat(3);
            }
            List<TaxesFacture> listTaxesTemps = facture.getListsTaxe();
            facture.setListsTaxe(null);

            ejbFacadeFacture.edit(facture);

            if (!listTaxesTemps.isEmpty()) {
                for (TaxesFacture taxesFacture : listTaxesTemps) {
                    taxesFacture.setFacture(facture);
                    ejbFacadeTaxeFacture.create(taxesFacture);
                }
            }
        }

        //Les payements
        List<Encaissement> listEncaissements = new ArrayList<>();
        for (EncaissementBonLivraison encaissementBonLivraison : selected.getListEncaissementBonLivraisons()) {

            Encaissement encaissement = new Encaissement();
            encaissement.setDateCheque(encaissementBonLivraison.getDateCheque());
            encaissement.setDateEncaissement(encaissementBonLivraison.getDateEncaissement());
            encaissement.setDeductionPourcentageTicket(encaissementBonLivraison.getDeductionPourcentageTicket());
            encaissement.setFacture(facture);
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
            encaissement.setNumero(facture.getNumero());
            encaissement.setNumCheque(encaissementBonLivraison.getNumCheque());
            encaissement.setNbrTicket(encaissementBonLivraison.getNbrTicket());
            encaissement.setMontant(encaissementBonLivraison.getMontant());
            encaissement.setLibelleTicket(encaissementBonLivraison.getLibelleTicket());
            listEncaissements.add(encaissement);
        }

        ejbFacadeEncaissement.create(listEncaissements);

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

    public void initTaxeFacture(List<ParametrageTaxe> listParametrageTaxeEntreprise, Facture facture) {

        if ((facture.getTotalHT().compareTo(BigDecimal.ZERO) == 0) && (facture.getTotalTTC().compareTo(BigDecimal.ZERO) == 0)) {
            facture.setTotalHT(facture.getMontantHT());
            facture.setTotalTTC(facture.getMontantTTC());
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
                    taxe.setMontant((facture.getMontantHT().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                    if (taxe.getOperation().equals("+")) {
                        facture.setTotalTTC((facture.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        facture.setTotalTTC((facture.getTotalTTC()).subtract(taxe.getMontant()));
                    }

                    facture.getListsTaxe().add(taxe);

                } //apresTva
                else if ((selectedTaxe.getTypeTaxe().trim().equals("0")) && (selectedTaxe.isApresTva())) {
                    TaxesFacture taxe = new TaxesFacture();
                    taxe.setParametrageTaxe(selectedTaxe);
                    taxe.setValeur(selectedTaxe.getValeur());
                    taxe.setOperation(selectedTaxe.getOperation());
                    taxe.setTypeTaxe(selectedTaxe.getTypeTaxe());
                    taxe.setApresTva(selectedTaxe.isApresTva());
                    taxe.setMontant((facture.getMontantTTC().multiply(taxe.getValeur())).divide(BigDecimal.valueOf(100)));
                    if (taxe.getOperation().equals("+")) {
                        facture.setTotalTTC((facture.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        facture.setTotalTTC((facture.getTotalTTC()).subtract(taxe.getMontant()));
                    }
                    facture.getListsTaxe().add(taxe);

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
                        facture.setTotalTTC((facture.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        facture.setTotalTTC((facture.getTotalTTC()).subtract(taxe.getMontant()));
                    }
                    facture.getListsTaxe().add(taxe);

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
                        facture.setTotalTTC((facture.getTotalTTC()).add(taxe.getMontant()));
                    } else {
                        facture.setTotalTTC((facture.getTotalTTC()).subtract(taxe.getMontant()));
                    }
                    facture.getListsTaxe().add(taxe);
                }

            }
            calculerTotalTaxe(facture);
            listParametrageTaxeEntreprise = new ArrayList<>();
        }

    }

    public void calculerTotalTaxe(Facture facture) {
        facture.setTotalTaxe(BigDecimal.ZERO);
        for (int i = 0; i < facture.getListsTaxe().size(); i++) {
            if (facture.getListsTaxe().get(i).getOperation().trim().equals("+")) {
                facture.setTotalTaxe((facture.getTotalTaxe()).add(facture.getListsTaxe().get(i).getMontant()));
            } else {

                facture.setTotalTaxe((facture.getTotalTaxe()).subtract(facture.getListsTaxe().get(i).getMontant()));
            }
        }
    }

    public void recalculerTotal() {
        selected.setMontantHT(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTVA(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTTC(new BigDecimal(BigInteger.ZERO));
        for (LigneBonLivraison ligneBonLivraison : selected.getListeLigneBonLivraisons()) {
            selected.setMontantHT(selected.getMontantHT().add(ligneBonLivraison.getTotalHT()));
            selected.setMontantTVA(selected.getMontantTVA().add(ligneBonLivraison.getTotalTVA()));
            selected.setMontantTTC(selected.getMontantTTC().add(ligneBonLivraison.getTotalTTC()));
        }

    }

    public void listnerPrixUnitaire() {
        if (selectedLigneBonLivraison.getArticle() != null) {
            selectedLigneBonLivraison.setIdArticle(selectedLigneBonLivraison.getArticle().getId());
            selectedLigneBonLivraison.setCodeArticle(selectedLigneBonLivraison.getArticle().getCode());
            selectedLigneBonLivraison.setLibelleArticle(selectedLigneBonLivraison.getArticle().getLibelle());
            selectedLigneBonLivraison.setPrixUnitaireHT(selectedLigneBonLivraison.getArticle().getPrixRevendeur());
            selectedLigneBonLivraison.setTvaArticle(new BigDecimal(selectedLigneBonLivraison.getArticle().getTva().getValeur()));
            selectedLigneBonLivraison.setQuantite(BigDecimal.ZERO);
            selectedLigneBonLivraison.setRemise(BigDecimal.ZERO);
            selectedLigneBonLivraison.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneBonLivraison.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonLivraison.setTotalTTC(BigDecimal.ZERO);
        }
    }

    public void changedTotalHtTotalTtc() {
        if (selected.getClient() == null || selectedLigneBonLivraison.getLibelleArticle() == null) {
            selectedLigneBonLivraison.setPrixUnitaireHT(BigDecimal.ZERO);
            selectedLigneBonLivraison.setTvaArticle(BigDecimal.ZERO);
            selectedLigneBonLivraison.setQuantite(BigDecimal.ZERO);
            selectedLigneBonLivraison.setRemise(BigDecimal.ZERO);
            selectedLigneBonLivraison.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneBonLivraison.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonLivraison.setTotalTTC(BigDecimal.ZERO);
        } else {
            selectedLigneBonLivraison.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonLivraison.setTotalTTC(BigDecimal.ZERO);
            selectedLigneBonLivraison.setQuantiteMax(selectedLigneBonLivraison.getQuantite());
            selectedLigneBonLivraison.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            BigDecimal prixRevendeur = selectedLigneBonLivraison.getPrixUnitaireHT();
            
            if (selectedLigneBonLivraison.getRemise().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal valRemise = FonctionsMathematiques.arrondiBigDecimal(prixRevendeur.multiply(selectedLigneBonLivraison.getRemise()), 3);
                valRemise = FonctionsMathematiques.arrondiBigDecimal(valRemise.divide(new BigDecimal("100")), 3);
                prixRevendeur = prixRevendeur.subtract(valRemise);
            }
            
            selectedLigneBonLivraison.setPrixUnitaireApresRemise(prixRevendeur);

            BigDecimal TotalHT = FonctionsMathematiques.arrondiBigDecimal((selectedLigneBonLivraison.getPrixUnitaireApresRemise()).multiply(selectedLigneBonLivraison.getQuantite()), 3);
            selectedLigneBonLivraison.setTotalHT(TotalHT);

            if (selected.getClient().isAssujettiTVA()) {
                selectedLigneBonLivraison.setTotalTVA(((selectedLigneBonLivraison.getTotalHT().multiply(selectedLigneBonLivraison.getTvaArticle()))).divide(BigDecimal.valueOf(100)));
                selectedLigneBonLivraison.setTotalTTC(selectedLigneBonLivraison.getTotalHT().add(selectedLigneBonLivraison.getTotalTVA()));
            
            } else {
                selectedLigneBonLivraison.setTotalTVA(BigDecimal.ZERO);
                selectedLigneBonLivraison.setTotalTTC(TotalHT);
                
            }

//            BigDecimal valTemp = FonctionsMathematiques.arrondiBigDecimal(selectedLigneBonLivraison.getMontantHT().multiply(selectedLigneBonLivraison.getTvaArticle()), 3);
//            selectedLigneBonLivraison.setMontantTVA(FonctionsMathematiques.arrondiBigDecimal(valTemp.divide(BigDecimal.valueOf(100)), 3));
//            selectedLigneBonLivraison.setMontantTTC(selectedLigneBonLivraison.getMontantHT().add(selectedLigneBonLivraison.getMontantTVA()));
        }
    }

    public BigDecimal getTotaleHT() {
        BigDecimal totalHT = new BigDecimal(0);
        if (selectedLigneBonLivraison.getLibelleArticle() == null) {
            return new BigDecimal(0);
        }
        BigDecimal prixRevendeur = selectedLigneBonLivraison.getPrixUnitaireHT();
        totalHT = prixRevendeur.multiply(selectedLigneBonLivraison.getQuantite());
        if (totalHT != new BigDecimal(0)) {
            return FonctionsMathematiques.arrondiBigDecimal(totalHT, 3);
        } else {
            return FonctionsMathematiques.arrondiBigDecimal(new BigDecimal(0), 3);
        }

    }

    public BigDecimal getTotaleTTC() {
        BigDecimal totalTTC = new BigDecimal(0);
        if (selectedLigneBonLivraison.getLibelleArticle() == null) {
            return new BigDecimal(0);
        }
        BigDecimal prixRevendeur = selectedLigneBonLivraison.getPrixUnitaireHT();

        BigDecimal totaleHT = (prixRevendeur).multiply(selectedLigneBonLivraison.getQuantite());
        totalTTC = totaleHT.add(totaleHT.multiply(selectedLigneBonLivraison.getTvaArticle().divide(BigDecimal.valueOf(100))));

        if (totalTTC != new BigDecimal(0)) {
            return FonctionsMathematiques.arrondiBigDecimal(totalTTC, 3);
        } else {
            return FonctionsMathematiques.arrondiBigDecimal(new BigDecimal(0), 3);
        }
    }

    public void createBonLivraisonFromDevis() {

        // selected.setNumero(numero);
        //selected.setCodeClient(selected.getDevis().getCodeClient());
        selected.setLibelleClient(selected.getDevis().getLibelleClient());
        selected.setAssujettiTVA(selected.getDevis().isAssujettiTVA());
        selected.setIdClient(selected.getDevis().getIdClient());
        selected.setDateSynch(System.currentTimeMillis());
        selected.setSupprimer(false);
        selected.setEtat(0);
        selected.setDateCreation(new Date());
        selected.setOrigineBonLivraison(1);
        selected.setTypeVente(0);
        selected.setMontantHT(selected.getDevis().getMontantHT());
        selected.setMontantTVA(selected.getDevis().getMontantTVA());
        selected.setMontantTTC(selected.getDevis().getMontantTTC());
        /*selected.setTotalHT(selected.getDevis().getTotalHT());
        selected.setTotalTTC(selected.getDevis().getTotalTTC());
        selected.setTotalTaxe(selected.getDevis().getTotalTaxe());*/
        selected.setIdDevis(selected.getDevis().getId());
        bonLivraison.setReste(selected.getMontantHT());

        List<LigneBonLivraison> tempsList = new ArrayList<>();
        tempsList = selected.getListeLigneBonLivraisons();
        selected.setListeLigneBonLivraisons(null);

        ejbFacade.create(selected);

        //List<LigneFacture> listLigneFactures = new ArrayList<>();
        for (LigneBonLivraison tempsList1 : tempsList) {
            tempsList1.setBonLivraison(selected);

        }
        ejbFacadeLigneBonLivraison.create(tempsList);

    }

    public void chargeListDevis() {

        if (selected.getOrigineBonLivraison() == 1) {

            if (selected.getClient() != null) {
                listDeviss = ejbFacadeDevis.findAllNative(" where o.Cli_Id = " + selected.getClient().getId() + " and o.Dev_Etat = 1 ");

            } else {
                listDeviss = new ArrayList<>();
            }

        }
    }

    public void chargeDevis() {

        selected.setListeLigneBonLivraisons(new ArrayList<LigneBonLivraison>());

        if (selected.getDevis().getListeLigneDeviss().isEmpty()) {
            selected.getDevis().setListeLigneDeviss(ejbFacadeLigneDevis.findAll(" where o.devis.id = " + selected.getDevis().getId()));

        }
        //selected.setTotalTaxe(selected.getDevis().getTotalTaxe());
        selected.setMontantHT(selected.getDevis().getTotalHT());
        selected.setMontantTVA(selected.getDevis().getTotalTVA());
        selected.setMontantTTC(selected.getDevis().getTotalTTC());

        /*selected.setTotalHT(selected.getMontantHT());
        selected.setTotalTTC(selected.getMontantTTC());*/
        // nous allons calculer la taxe
        for (LigneDevis detailDevis : selected.getDevis().getListeLigneDeviss()) {

            LigneBonLivraison LigneBonLivraison = new LigneBonLivraison();
            LigneBonLivraison.setIdArticle(detailDevis.getIdArticle());
            LigneBonLivraison.setCodeArticle(detailDevis.getCodeArticle());
            LigneBonLivraison.setLibelleArticle(detailDevis.getLibelleArticle());
            LigneBonLivraison.setPrixUnitaireApresRemise(detailDevis.getPrixUnitaireApresRemise());
            LigneBonLivraison.setRemise(detailDevis.getRemise());
            LigneBonLivraison.setQuantite(detailDevis.getQuantite());
            LigneBonLivraison.setQuantiteMax(detailDevis.getQuantite());
            LigneBonLivraison.setPrixUnitaireHT(detailDevis.getPrixUnitaireHT());
            LigneBonLivraison.setTvaArticle(detailDevis.getTvaArticle());
            LigneBonLivraison.setTotalHT(detailDevis.getTotalHT());
            LigneBonLivraison.setTotalTVA(detailDevis.getTotalTVA());
            LigneBonLivraison.setTotalTTC(detailDevis.getTotalTTC());

            // if recalculer taxe
            /*Article articleTemp = new Article();
            articleTemp.setId(ligneFacture.getIdArticle());
            int i = listArticleTemps.indexOf(articleTemp);

            if (i > -1) {
                ligneFacture.setTotalTTC(ligneFacture.getTotalHT().add(((ligneFacture.getTotalHT().multiply(BigDecimal.valueOf(listArticleTemps.get(i).getTva().getValeur())))).divide(BigDecimal.valueOf(100))));

            } else {
                ligneFacture.setTotalTTC(ligneFacture.getTotalTTC());
            }

            selected.setMontantTTC(selected.getMontantTTC().add(ligneFacture.getTotalTTC()));
            selected.setTotalTTC(selected.getMontantTTC());
             */
            selected.getListeLigneBonLivraisons().add(LigneBonLivraison);

        }

        //récupération des bons de livraison
        /*List<BonLivraison> listBonLivraisonTemp = ejbFacadeBonLivraison.findAllNative(" where o.BLiv_OrigineBonLivraison = 1 and o.BLiv_Etat = 0 and o.Dev_Id = " + selected.getDevis().getId());

        //soustraction des lignes de bon de livraison et la taxe pour chaque ligne
        if (listBonLivraisonTemp != null && !listBonLivraisonTemp.isEmpty()) {
            //Probléme les bons de livraisons sont importer sans leurs lignes
            //ejbFacadeLigneBonLivraison.findAll("where o.bonLivraison.id = " + listBonLivraison.get(0).getId());

            for (LigneFacture ligneFacture : selected.getListeLigneFactures()) {
                for (BonLivraison bonLivraison : listBonLivraisonTemp) {
                    bonLivraison.setListeLigneBonLivraisons(ejbFacadeLigneBonLivraison.findAll("where o.bonLivraison.id = " + bonLivraison.getId()));
                    LigneBonLivraison ligneBonLivraison = new LigneBonLivraison();
                    ligneBonLivraison.setIdArticle(ligneFacture.getIdArticle());

                    int index = bonLivraison.getListeLigneBonLivraisons().indexOf(ligneBonLivraison);

                    if (index > -1) {

                        ligneFacture.setQuantite(ligneFacture.getQuantite() - Double.parseDouble("" + bonLivraison.getListeLigneBonLivraisons().get(index).getQuantite()));
                        ligneFacture.setQuantiteMax(BigDecimal.valueOf(ligneFacture.getQuantite()));

                        BigDecimal TotalTVA = bonLivraison.getListeLigneBonLivraisons().get(index).getTotalHT().multiply(BigDecimal.valueOf(ligneFacture.getTvaArticle())).divide(BigDecimal.valueOf(100));

                        selected.setMontantHT(selected.getMontantHT().subtract(bonLivraison.getListeLigneBonLivraisons().get(index).getTotalHT()));
                        selected.setMontantTTC(selected.getMontantTTC().subtract(bonLivraison.getListeLigneBonLivraisons().get(index).getTotalHT().add(TotalTVA)));
                        selected.setTotalHT(selected.getMontantHT());
                        selected.setTotalTTC(selected.getMontantTTC());

                        ligneFacture.setTotalHT(ligneFacture.getTotalHT().subtract(bonLivraison.getListeLigneBonLivraisons().get(index).getTotalHT()));

                        // la tva est déjà actualiser en haut
                        ligneFacture.setTotalTTC(ligneFacture.getTotalTTC().subtract(bonLivraison.getListeLigneBonLivraisons().get(index).getTotalHT().add(TotalTVA)));

                    }

                }
            }

            // aprés avoir enlever les ligne déja saisites dans les anciennes bonDelivraison
            // je vais filter la liste de bon de livraison listBonLivraison de maniére à garder les
            // les bon de livraison non ajouter dans une ancienne facture liée a ce bon de commande
            listBonLivraison = new ArrayList<>();
            for (BonLivraison bonLivraison : listBonLivraisonTemp) {
                if (bonLivraison.getIdFacture() == null) {
                    listBonLivraison.add(bonLivraison);
                }
            }

        }*/
    }
    
    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";


                items = getFacade().searchAllNative(debut, fin, etatBonLivraison, origineBonLivraison, client != null ? client.getId() : null, retour);

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreuPeriode")));
            }

        } catch (Exception e) {

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
            String dateBonLivraison = TraitementDate.returnDate(selectedSingle.getDateCreation());
            String numeroBonLivraison = selectedSingle.getNumero();

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
            ArrayList<String> ligneBLEntete = new ArrayList();

            ligneBLEntete.add("Reference");
            ligneBLEntete.add("Produit");
            //ligneBLEntete.add("TVA");
            ligneBLEntete.add("Quantite");
            ligneBLEntete.add("Prix UT");
            ligneBLEntete.add("Total HT");

            ArrayList<ArrayList<String>> ligneBLs = new ArrayList<ArrayList<String>>();

            ArrayList<String> ligneBLInfo;

            for (int i = 0; i < selectedSingle.getListeLigneBonLivraisons().size(); i++) {

                ligneBLInfo = new ArrayList();
                //0:left 1: linea left 2:center 3:linea right 4:right
                ligneBLInfo.add("1:" + selectedSingle.getListeLigneBonLivraisons().get(i).getCodeArticle());
                ligneBLInfo.add("1:" + selectedSingle.getListeLigneBonLivraisons().get(i).getLibelleArticle());
                //ligneDevisInfo.add("3:" + selectedSingle.getListeLigneBonLivraisons().get(i).getTvaArticle() + "%");
                ligneBLInfo.add("3:" + selectedSingle.getListeLigneBonLivraisons().get(i).getQuantite());
                ligneBLInfo.add("3:" + selectedSingle.getListeLigneBonLivraisons().get(i).getPrixUnitaireApresRemise());
                ligneBLInfo.add("3:" + selectedSingle.getListeLigneBonLivraisons().get(i).getTotalHT());
                ligneBLs.add(i, ligneBLInfo);
            }

            //addInvoiceSummarize
            ArrayList<String> blSummarizeInfos = new ArrayList();

            blSummarizeInfos.add("Total HT" + " : " + selectedSingle.getMontantHT());
            /*devisSummarizeInfos.add("Total TVA" + " : " + selectedSingle.getTotalTVA());
            devisSummarizeInfos.add("Total Taxe" + " : " + selectedSingle.getTotalTaxe());
            devisSummarizeInfos.add("Total TTC" + " : " + selectedSingle.getTotalTTC());*/

            GenerationPdf.generationPdf(image, path, "Bon Livraison", numeroBonLivraison, dateBonLivraison, entrepriseInfos, clientInfos, ligneBLEntete, ligneBLs, blSummarizeInfos);

            File file = new File(path);
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=" + "BonLivraison_" + numeroBonLivraison + ".pdf");
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

    public LigneBonLivraison getSelectedLigneBonLivraison() {
        return selectedLigneBonLivraison;
    }

    public void setSelectedLigneBonLivraison(LigneBonLivraison selectedLigneBonLivraison) {
        this.selectedLigneBonLivraison = selectedLigneBonLivraison;
    }

    public LigneBonLivraison getSelectedLigneBonLivraisonSingle() {
        return selectedLigneBonLivraisonSingle;
    }

    public void setSelectedLigneBonLivraisonSingle(LigneBonLivraison selectedLigneBonLivraisonSingle) {
        this.selectedLigneBonLivraisonSingle = selectedLigneBonLivraisonSingle;
    }

    public LigneBonLivraison getSelectedLigneBonLivraisonModif() {
        return selectedLigneBonLivraisonModif;
    }

    public void setSelectedLigneBonLivraisonModif(LigneBonLivraison selectedLigneBonLivraisonModif) {
        this.selectedLigneBonLivraisonModif = selectedLigneBonLivraisonModif;
    }

    public boolean isAnnulation() {
        return annulation;
    }

    public void setAnnulation(boolean annulation) {
        this.annulation = annulation;
    }

    public Integer getEtatBonLivraison() {
        return etatBonLivraison;
    }

    public void setEtatBonLivraison(Integer etatBonLivraison) {
        this.etatBonLivraison = etatBonLivraison;
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

    public Integer getOrigineBonLivraison() {
        return origineBonLivraison;
    }

    public void setOrigineBonLivraison(Integer origineBonLivraison) {
        this.origineBonLivraison = origineBonLivraison;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isRetour() {
        return retour;
    }

    public void setRetour(boolean retour) {
        this.retour = retour;
    }

    public SelectItem[] getItemsAvailableSelectOneClient() {
        listClient = new ArrayList<>();
        if (categorieClient != null) {
            listClient = ejbFacadeClient.findAllNative(" where o.CCl_Id = " + categorieClient.getId());
        }
        return JsfUtil.getSelectItems(listClient, true);

    }

    public SelectItem[] getItemsAvailableSelectOneDevis() {

        return JsfUtil.getSelectItems(listDeviss, true);

    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public BonLivraison getBonLivraison(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = BonLivraison.class)
    public static class BonLivraisonControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BonLivraisonController controller = (BonLivraisonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bonLivraisonController");
            return controller.getBonLivraison(getKey(value));
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
            if (object instanceof BonLivraison) {
                BonLivraison o = (BonLivraison) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + BonLivraison.class.getName());
            }
        }

    }

}
