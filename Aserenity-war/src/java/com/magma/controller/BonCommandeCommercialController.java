package com.magma.controller;

import com.itextpdf.text.DocumentException;
import com.magma.bibliotheque.FonctionsMathematiques;
import com.magma.bibliotheque.GenerationPdf;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.TraitementDate;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Article;
import com.magma.entity.BonCommandeCommercial;
import com.magma.entity.Categorie;
import com.magma.entity.CategorieClient;
import com.magma.entity.Client;
import com.magma.entity.Commercial;
import com.magma.entity.EtatCommande;
import com.magma.entity.LigneBonCommandeCommercial;
import com.magma.entity.ParametrageEntreprise;
import com.magma.entity.ParametrageTaxe;
import com.magma.entity.Utilisateur;
import com.magma.session.BonLivraisonFacadeLocal;
import com.magma.session.BonCommandeCommercialFacadeLocal;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.EtatCommandeFacadeLocal;
import com.magma.session.FactureFacadeLocal;
import com.magma.session.LigneBonLivraisonFacadeLocal;
import com.magma.session.LigneBonCommandeCommercialFacadeLocal;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
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

@ManagedBean(name = "bonCommandeCommercialController")
@SessionScoped
public class BonCommandeCommercialController implements Serializable {

    private BonCommandeCommercial selected;
    private BonCommandeCommercial selectedSingle;
    private List<BonCommandeCommercial> items = null;
    private Utilisateur utilisateur;
    @EJB
    private BonCommandeCommercialFacadeLocal ejbFacade;

    @EJB
    private LigneBonCommandeCommercialFacadeLocal ejbFacadeLigneBonCommandeCommercial;

    @EJB
    private CategorieClientFacadeLocal ejbFacadeCatgorieClient;
    private CategorieClient categorieClient = null;

    @EJB
    private ClientFacadeLocal ejbFacadeClient;

    @EJB
    private ParametrageTaxeFacadeLocal ejbFacadeParametrageTaxe;

    @EJB
    private EtatCommandeFacadeLocal ejbFacadeEtatCommande;

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
    private RetourFacadeLocal ejbFacadeRetour;
    @EJB
    private LigneRetourFacadeLocal ejbFacadeLigneRetour;
    private List<Client> listClient = null;
    private List<Client> listClientLivraison = null;

    private Categorie categorie;

    private boolean errorMsg;
    private Long idTemp;
    private BonCommandeCommercial bonCommandeCommercial;
    private long idEntreprise = 0;
    private boolean annulation = true;
    private BigDecimal oldPrix;
    private BigDecimal oldQuantity;
    private double oldRemise;
    //private BigDecimal ancienMontantHT;
    private LigneBonCommandeCommercial selectedLigneBonCommandeCommercial;
    private LigneBonCommandeCommercial selectedLigneBonCommandeCommercialSingle;
    private LigneBonCommandeCommercial selectedLigneBonCommandeCommercialModif;
    private List<LigneBonCommandeCommercial> LigneBonCommandeCommercialTemps;
    private List<LigneBonCommandeCommercial> AncienLigneBonCommandeCommercial;
    private List<CategorieClient> listCategorieClient = null;
    private List<Article> listArticles = null;
    private List<ParametrageTaxe> listParametrageTaxeEntreprise = null;
    private List<ParametrageTaxe> selectedListParametrageTaxe = null;

    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Integer etatBonCommandeCommercial;
    private Client client;
    private ParametrageEntreprise parametrageEntreprise = null;

    public BonCommandeCommercialController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (bonCommandeCommercial.getIdEntrepriseSuivi() != null && bonCommandeCommercial.getIdEntrepriseSuivi() != 0) {
         idEntreprise = bonCommandeCommercial.getIdEntrepriseSuivi();
         } else {
         idEntreprise = bonCommandeCommercial.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
            parametrageEntreprise = utilisateur.getEntreprise().getParametrageEntreprise();
            MenuTemplate.menuFonctionnalitesModules("GCommande", "MCommande", null, utilisateur);
            //MenuTemplate.menuFonctionnalitesModules("GBonCommandeCommercial", utilisateur);
            /* if (bonCommandeCommercial.getIdEntrepriseSuivi() != null && bonCommandeCommercial.getIdEntrepriseSuivi() != 0) {
             idEntreprise = bonCommandeCommercial.getIdEntrepriseSuivi();
             } else {
             idEntreprise = bonCommandeCommercial.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../bonCommandeCommercial/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<BonCommandeCommercial> getItems() {
        try {
            if (items == null) {
                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                String clause = " where o.BCom_DateBonCommandeCommercial between '" + debut + "' and '" + fin + "' ";
                items = getFacade().findAllNative(clause + " order by o.BCom_DateBonCommandeCommercial desc");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- BonCommandeCommercialController - getItems: " + e.getMessage());
            return null;
        }
    }

    public BonCommandeCommercial getSelected() {
        return selected;
    }

    public void setSelected(BonCommandeCommercial selected) {
        this.selected = selected;
    }

    public BonCommandeCommercial getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(BonCommandeCommercial selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private BonCommandeCommercialFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        selectedLigneBonCommandeCommercial = null;
        selectedLigneBonCommandeCommercialSingle = null;
        selectedLigneBonCommandeCommercialModif = null;
        categorie = null;
        //client = null;
        categorieClient = null;

        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            /*annulation = false;
             if (selected.getIdBonCommandeCommercial() == null && selected.getListEncaissementBonCommandeCommercials().isEmpty()) {
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
        selected = new BonCommandeCommercial();
        selected.setDateBonCommandeCommercial(new Date());

        EtatCommande etatCommande = null;
        try {
            etatCommande = ejbFacadeEtatCommande.findAll(" where o.rang = 0 and o.supprimer = 0 ").get(0);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EtatCmdNull")));
            return null;
        }
        selected.setEtatCommande(etatCommande);

        errorMsg = false;
        selectedLigneBonCommandeCommercial = new LigneBonCommandeCommercial();
        selectedLigneBonCommandeCommercial.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercial.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercial.setQuantite(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercial.setRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercial.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercial.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercial.setTotalTTC(BigDecimal.ZERO);

        selectedLigneBonCommandeCommercialModif = new LigneBonCommandeCommercial();
        selectedLigneBonCommandeCommercialModif.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialModif.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialModif.setQuantite(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialModif.setRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialModif.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialModif.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialModif.setTotalTTC(BigDecimal.ZERO);

        selectedLigneBonCommandeCommercialSingle = new LigneBonCommandeCommercial();
        selectedLigneBonCommandeCommercialSingle.setPrixUnitaireHT(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialSingle.setTvaArticle(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialSingle.setQuantite(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialSingle.setRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialSingle.setPrixUnitaireApresRemise(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialSingle.setTotalHT(BigDecimal.ZERO);
        selectedLigneBonCommandeCommercialSingle.setTotalTTC(BigDecimal.ZERO);

        selectedListParametrageTaxe = new ArrayList<>();
        selected.setTotalTaxe(BigDecimal.ZERO);
        selected.setTypeVente(0);
        selected.setListeLigneBonCommandeCommercials(new ArrayList<LigneBonCommandeCommercial>());
        //selected.setEtat(-1);
        categorie = new Categorie();

        return "Create";
    }

    public String create() {

        /*try {*/
        if (errorMsg == false) {
            creationInfo();
            if (selected.getListeLigneBonCommandeCommercials() != null && !selected.getListeLigneBonCommandeCommercials().isEmpty()) {
                LigneBonCommandeCommercialTemps = selected.getListeLigneBonCommandeCommercials();

//                    for (LigneBonCommandeCommercial ligneBonCommandeCommercial : LigneBonCommandeCommercialTemps) {
//                        if (ligneBonCommandeCommercial.getQuantite().compareTo(BigDecimal.ZERO) == 0) {
//                            LigneBonCommandeCommercialTemps.remove(ligneBonCommandeCommercial);
//                        }
//                    }
                for (int i = LigneBonCommandeCommercialTemps.size() - 1; i >= 0; i--) {
                    if (LigneBonCommandeCommercialTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 0) {
                        LigneBonCommandeCommercialTemps.remove(LigneBonCommandeCommercialTemps.get(i));
                    }
                }

                selected.setListeLigneBonCommandeCommercials(null);
                /*                selected.setDateSynch(System.currentTimeMillis());
                 selected.setSupprimer(false);*/
                //selected.setDateCreation(new Date());

                selected.setDateSynch(System.currentTimeMillis());

                if (selected.getCategorieClient() != null) {
                    if (selected.getClient() != null) {

                        selected.setIdClient(selected.getClient().getId());
                        selected.setAssujettiTVA(selected.getClient().isAssujettiTVA());
                        selected.setLibelleClient(selected.getClient().getLibelle());

                        selected.setIdCategorieClient(selected.getCategorieClient().getId());
                        selected.setLibelleCategorieClient(selected.getCategorieClient().getLibelle());
                    }
                }

                if (selected.getCategorieClientLivraison() != null) {
                    if (selected.getClientLivraison() != null) {
                        selected.setIdClientLivraison(selected.getClientLivraison().getId());
                        selected.setAssujettiTVA(selected.getClientLivraison().isAssujettiTVA());
                        selected.setLibelleClientLivraison(selected.getClientLivraison().getLibelle());

                        selected.setIdCategorieClientLivraison(selected.getCategorieClientLivraison().getId());
                        selected.setLibelleCategorieClientLivraison(selected.getCategorieClientLivraison().getLibelle());
                    }
                }

                if (selected.getCommercial() != null) {

                    selected.setNomCommercial(selected.getCommercial().getNom());
                    selected.setPrenomCommercial(selected.getCommercial().getPrenom());
                    selected.setIdCommercial(selected.getCommercial().getId());
                    selected.setTypeCommercial(selected.getCommercial().getTypeCommercial());

                }
                selected.setNumero("BC" + System.currentTimeMillis());
                getFacade().create(selected);

                for (LigneBonCommandeCommercial ligneBonCommandeCommercial : LigneBonCommandeCommercialTemps) {
                    ligneBonCommandeCommercial.setBonCommandeCommercial(selected);
                }
                ejbFacadeLigneBonCommandeCommercial.create(LigneBonCommandeCommercialTemps);

                return prepareList();

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("DetailCommandeNull")));
                return null;
            }

        } else {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), "" + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
            return null;
        }

        /*} catch (Exception e) {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
         System.out.println("Erreur- BonCommandeCommercialController - create: " + e.getMessage());
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

            selectedLigneBonCommandeCommercial = new LigneBonCommandeCommercial();
            selectedLigneBonCommandeCommercialModif = new LigneBonCommandeCommercial();
            selectedLigneBonCommandeCommercialSingle = new LigneBonCommandeCommercial();

            selectedListParametrageTaxe = new ArrayList<>();
            listParametrageTaxeEntreprise = ejbFacadeParametrageTaxe.findAll("");
            categorie = new Categorie();

            if (selected.getIdCategorieClient() != null) {
                CategorieClient categorieClient = new CategorieClient();
                categorieClient.setId(selected.getIdCategorieClient());
                categorieClient.setLibelle(selected.getLibelleCategorieClient());
                selected.setCategorieClient(categorieClient);
                if (selected.getIdClient() != null) {
                    Client client = new Client();
                    client.setId(selected.getIdClient());
                    client.setAssujettiTVA(selected.isAssujettiTVA());
                    client.setLibelle(selected.getLibelleClient());
                    selected.setClient(client);
                }

            }

            if (selected.getIdCategorieClientLivraison() != null) {
                CategorieClient categorieClient = new CategorieClient();
                categorieClient.setId(selected.getIdCategorieClientLivraison());
                categorieClient.setLibelle(selected.getLibelleCategorieClientLivraison());
                selected.setCategorieClientLivraison(categorieClient);
                if (selected.getIdClientLivraison() != null) {
                    Client client = new Client();
                    client.setId(selected.getIdClientLivraison());
                    client.setAssujettiTVA(selected.isAssujettiTVA());
                    client.setLibelle(selected.getLibelleClientLivraison());
                    selected.setClientLivraison(client);
                }

            }

            if (selected.getIdCommercial() != null) {
                Commercial commercial = new Commercial();
                commercial.setId(selected.getIdCommercial());
                commercial.setNom(selected.getNomCommercial());
                commercial.setPrenom(selected.getPrenomCommercial());
                commercial.setTypeCommercial(0);
                selected.setCommercial(commercial);
            }

            //ancienMontantHT = selected.getMontantHT();
            for (LigneBonCommandeCommercial ligneBonCommandeCommercial : selected.getListeLigneBonCommandeCommercials()) {

                Article article = new Article();
                article.setId(ligneBonCommandeCommercial.getIdArticle());
                article.setCode(ligneBonCommandeCommercial.getCodeArticle());
                article.setLibelle(ligneBonCommandeCommercial.getLibelleArticle());
                ligneBonCommandeCommercial.setArticle(article);

            }

            AncienLigneBonCommandeCommercial = new ArrayList<LigneBonCommandeCommercial>(selected.getListeLigneBonCommandeCommercials());

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {

            if (selected.getListeLigneBonCommandeCommercials() != null && !selected.getListeLigneBonCommandeCommercials().isEmpty()) {
                editionInfo();
                LigneBonCommandeCommercialTemps = new ArrayList<LigneBonCommandeCommercial>(selected.getListeLigneBonCommandeCommercials());
                selected.setListeLigneBonCommandeCommercials(null);

                if (selected.getCategorieClient() != null) {
                    if (selected.getClient() != null) {

                        selected.setIdClient(selected.getClient().getId());
                        selected.setAssujettiTVA(selected.getClient().isAssujettiTVA());
                        selected.setLibelleClient(selected.getClient().getLibelle());

                        selected.setIdCategorieClient(selected.getCategorieClient().getId());
                        selected.setLibelleCategorieClient(selected.getCategorieClient().getLibelle());
                    }
                }

                if (selected.getCategorieClientLivraison() != null) {
                    if (selected.getClientLivraison() != null) {
                        selected.setIdClientLivraison(selected.getClientLivraison().getId());
                        selected.setAssujettiTVA(selected.getClientLivraison().isAssujettiTVA());
                        selected.setLibelleClientLivraison(selected.getClientLivraison().getLibelle());

                        selected.setIdCategorieClientLivraison(selected.getCategorieClientLivraison().getId());
                        selected.setLibelleCategorieClientLivraison(selected.getCategorieClientLivraison().getLibelle());
                    }
                }

                if (selected.getCommercial() != null) {

                    selected.setNomCommercial(selected.getCommercial().getNom());
                    selected.setPrenomCommercial(selected.getCommercial().getPrenom());
                    selected.setIdCommercial(selected.getCommercial().getId());
                    selected.setTypeCommercial(selected.getCommercial().getTypeCommercial());

                }

                getFacade().edit(selected);

                //LigneBonCommandeCommercialTemps = selected.getListeLigneBonCommandeCommercials();
                for (LigneBonCommandeCommercial ligneBonCommandeCommercial : AncienLigneBonCommandeCommercial) {

                    int index = LigneBonCommandeCommercialTemps.indexOf(ligneBonCommandeCommercial);

                    if (index == -1) {

                        ejbFacadeLigneBonCommandeCommercial.remove(ligneBonCommandeCommercial);
                    } else {
                        //ceci pour remédier au cas ou le stock est supprimer puis réajouter sans passer par la modif
                        LigneBonCommandeCommercialTemps.get(index).setId(ligneBonCommandeCommercial.getId());
                        LigneBonCommandeCommercialTemps.get(index).setBonCommandeCommercial(selected);
                        ejbFacadeLigneBonCommandeCommercial.edit(LigneBonCommandeCommercialTemps.get(index));

                    }

                }
                sauvegardeNouvelleLigneBonCommandeCommercial();

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
            System.out.println("Erreur- BonCommandeCommercialController - update: " + e.getMessage());
            return null;
        }
    }

    private void sauvegardeNouvelleLigneBonCommandeCommercial() {

        int j = 0;
        //boolean trouve = false;

        for (int i = 0; i < LigneBonCommandeCommercialTemps.size(); i++) {

            if (!AncienLigneBonCommandeCommercial.contains(LigneBonCommandeCommercialTemps.get(i)) && LigneBonCommandeCommercialTemps.get(i).getQuantite().compareTo(BigDecimal.ZERO) == 1) {
                LigneBonCommandeCommercialTemps.get(i).setBonCommandeCommercial(selected);
                ejbFacadeLigneBonCommandeCommercial.create(LigneBonCommandeCommercialTemps.get(i));
            }

        }

    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<BonCommandeCommercialPVT> temps = ejbFacadeBonCommandeCommercialPVT.findAll("where o.idBonCommandeCommercial = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- BonCommandeCommercialController - performDestroy: " + e.getMessage());
        }
    }

    public void deleteFromListLigneBonCommandeCommercial() {

        if (selectedLigneBonCommandeCommercialSingle != null) {
            selected.getListeLigneBonCommandeCommercials().remove(selectedLigneBonCommandeCommercialSingle);
            recalculerTotal();

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": " + ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet"), ""));
        }

    }

    public void validerDetailArticle() {
        if (selectedLigneBonCommandeCommercial.getArticle() != null) {
            selectedLigneBonCommandeCommercial.setIdArticle(selectedLigneBonCommandeCommercial.getArticle().getId());
            int index = selected.getListeLigneBonCommandeCommercials().indexOf(selectedLigneBonCommandeCommercial);
            if (index > -1) {
                selected.getListeLigneBonCommandeCommercials().get(index).setQuantite(selectedLigneBonCommandeCommercial.getQuantite());
                selected.getListeLigneBonCommandeCommercials().get(index).setQuantiteMax(selectedLigneBonCommandeCommercial.getQuantite());
                selected.getListeLigneBonCommandeCommercials().get(index).setPrixUnitaireHT(selectedLigneBonCommandeCommercial.getPrixUnitaireHT());
                selected.getListeLigneBonCommandeCommercials().get(index).setPrixUnitaireApresRemise(selectedLigneBonCommandeCommercial.getPrixUnitaireApresRemise());
                selected.getListeLigneBonCommandeCommercials().get(index).setRemise(selectedLigneBonCommandeCommercial.getRemise());
                selected.getListeLigneBonCommandeCommercials().get(index).setTvaArticle(new BigDecimal(selectedLigneBonCommandeCommercial.getTvaArticle() + ""));

                selected.getListeLigneBonCommandeCommercials().get(index).setTotalHT(selectedLigneBonCommandeCommercial.getTotalHT());
                selected.getListeLigneBonCommandeCommercials().get(index).setTotalTVA(selectedLigneBonCommandeCommercial.getTotalTVA());
                selected.getListeLigneBonCommandeCommercials().get(index).setTotalTTC(selectedLigneBonCommandeCommercial.getTotalTTC());
                recalculerTotal();
                selectedLigneBonCommandeCommercial = new LigneBonCommandeCommercial();
                categorie = new Categorie();
            } else {
                selected.getListeLigneBonCommandeCommercials().add(selectedLigneBonCommandeCommercial);
                recalculerTotal();
                selectedLigneBonCommandeCommercial = new LigneBonCommandeCommercial();
                categorie = new Categorie();
            }

        }
    }

    public void validerDetailArticleModif() {

        if (selectedLigneBonCommandeCommercial.getQuantite().compareTo(selectedLigneBonCommandeCommercial.getQuantiteMax()) <= 0) {
            recalculerTotal();
            selectedLigneBonCommandeCommercial = new LigneBonCommandeCommercial();

        } else {
            selectedLigneBonCommandeCommercial.setPrixUnitaireHT(oldPrix);
            selectedLigneBonCommandeCommercial.setQuantite(oldQuantity);
            changedTotalHtTotalTtc();
        }

    }

    public void recalculerTotal() {

        selected.setMontantHT(BigDecimal.ZERO);
        selected.setMontantTVA(BigDecimal.ZERO);
        selected.setMontantTTC(BigDecimal.ZERO);
        for (LigneBonCommandeCommercial ligneBonCommandeCommercial : selected.getListeLigneBonCommandeCommercials()) {
            System.out.println("da" + selected.getMontantHT().add(ligneBonCommandeCommercial.getTotalHT()));

            System.out.println("da" + selected.getMontantTVA().add(ligneBonCommandeCommercial.getTotalTVA()));

            selected.setMontantHT(selected.getMontantHT().add(ligneBonCommandeCommercial.getTotalHT()));
            selected.setMontantTVA(selected.getMontantTVA().add(ligneBonCommandeCommercial.getTotalTVA()));
            selected.setMontantTTC(selected.getMontantTTC().add(ligneBonCommandeCommercial.getTotalTTC()));
        }
        selected.setTotalHT(selected.getMontantHT());
        selected.setTotalTVA(selected.getMontantTVA());
        selected.setTotalTTC(selected.getMontantTTC());
    }

    public void listnerPrixUnitaire() {
        if (selectedLigneBonCommandeCommercial.getArticle() != null) {
            selectedLigneBonCommandeCommercial.setIdArticle(selectedLigneBonCommandeCommercial.getArticle().getId());
            selectedLigneBonCommandeCommercial.setCodeArticle(selectedLigneBonCommandeCommercial.getArticle().getCode());
            selectedLigneBonCommandeCommercial.setLibelleArticle(selectedLigneBonCommandeCommercial.getArticle().getLibelle());
            selectedLigneBonCommandeCommercial.setPrixUnitaireHT(selectedLigneBonCommandeCommercial.getArticle().getPrixRevendeur());
            selectedLigneBonCommandeCommercial.setTvaArticle(new BigDecimal(selectedLigneBonCommandeCommercial.getArticle().getTva().getValeur()));
            selectedLigneBonCommandeCommercial.setQuantite(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalTVA(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalTTC(BigDecimal.ZERO);
        }
    }

    public void changedTotalHtTotalTtc() {
        if (selected.getClient() == null || selectedLigneBonCommandeCommercial.getLibelleArticle() == null) {
            selectedLigneBonCommandeCommercial.setPrixUnitaireHT(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTvaArticle(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setQuantite(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalTVA(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalTTC(BigDecimal.ZERO);
        } else {
            selectedLigneBonCommandeCommercial.setTotalHT(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalTVA(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setTotalTTC(BigDecimal.ZERO);
            selectedLigneBonCommandeCommercial.setQuantiteMax(selectedLigneBonCommandeCommercial.getQuantite());
            selectedLigneBonCommandeCommercial.setPrixUnitaireApresRemise(BigDecimal.ZERO);
            BigDecimal prixRevendeur = selectedLigneBonCommandeCommercial.getPrixUnitaireHT();
            if (selectedLigneBonCommandeCommercial.getRemise().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal valRemise = FonctionsMathematiques.arrondiBigDecimal(prixRevendeur.multiply(selectedLigneBonCommandeCommercial.getRemise()), 3);
                valRemise = FonctionsMathematiques.arrondiBigDecimal(valRemise.divide(new BigDecimal("100")), 3);
                prixRevendeur = prixRevendeur.subtract(valRemise);
            }
            selectedLigneBonCommandeCommercial.setPrixUnitaireApresRemise(prixRevendeur);

            BigDecimal TotalHT = FonctionsMathematiques.arrondiBigDecimal((selectedLigneBonCommandeCommercial.getPrixUnitaireApresRemise()).multiply(selectedLigneBonCommandeCommercial.getQuantite()), 3);
            selectedLigneBonCommandeCommercial.setTotalHT(TotalHT);

            if (selected.getClient().isAssujettiTVA()) {
                selectedLigneBonCommandeCommercial.setTotalTVA(((selectedLigneBonCommandeCommercial.getTotalHT().multiply(selectedLigneBonCommandeCommercial.getTvaArticle()))).divide(BigDecimal.valueOf(100)));
                selectedLigneBonCommandeCommercial.setTotalTTC(selectedLigneBonCommandeCommercial.getTotalHT().add(selectedLigneBonCommandeCommercial.getTotalTVA()));

            } else {
                selectedLigneBonCommandeCommercial.setTotalTVA(BigDecimal.ZERO);
                selectedLigneBonCommandeCommercial.setTotalTTC(TotalHT);
            }

            /*            BigDecimal valTemp = FonctionsMathematiques.arrondiBigDecimal(selectedLigneBonCommandeCommercial.getMontantHT().multiply(selectedLigneBonCommandeCommercial.getTvaArticle()), 3);
             selectedLigneBonCommandeCommercial.setMontantTVA(FonctionsMathematiques.arrondiBigDecimal(valTemp.divide(BigDecimal.valueOf(100)), 3));
             selectedLigneBonCommandeCommercial.setMontantTTC(selectedLigneBonCommandeCommercial.getMontantHT().add(selectedLigneBonCommandeCommercial.getMontantTVA()));*/
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
            String dateBonCommandeCommercial = TraitementDate.returnDate(selectedSingle.getDateCreation());
            String numeroBonCommandeCommercial = selectedSingle.getNumero();

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
            ArrayList<String> ligneBonCommandeCommercialEntete = new ArrayList();

            ligneBonCommandeCommercialEntete.add("Reference");
            ligneBonCommandeCommercialEntete.add("Produit");
            ligneBonCommandeCommercialEntete.add("TVA");
            ligneBonCommandeCommercialEntete.add("Quantite");
            ligneBonCommandeCommercialEntete.add("Prix UT");
            ligneBonCommandeCommercialEntete.add("Total HT");

            ArrayList<ArrayList<String>> ligneFactures = new ArrayList<ArrayList<String>>();

            ArrayList<String> ligneBonCommandeCommercialInfo;

            for (int i = 0; i < selectedSingle.getListeLigneBonCommandeCommercials().size(); i++) {

                ligneBonCommandeCommercialInfo = new ArrayList();
                //0:left 1: linea left 2:center 3:linea right 4:right
                ligneBonCommandeCommercialInfo.add("1:" + selectedSingle.getListeLigneBonCommandeCommercials().get(i).getCodeArticle());
                ligneBonCommandeCommercialInfo.add("1:" + selectedSingle.getListeLigneBonCommandeCommercials().get(i).getLibelleArticle());
                ligneBonCommandeCommercialInfo.add("3:" + selectedSingle.getListeLigneBonCommandeCommercials().get(i).getTvaArticle() + "%");
                ligneBonCommandeCommercialInfo.add("3:" + selectedSingle.getListeLigneBonCommandeCommercials().get(i).getQuantite());
                ligneBonCommandeCommercialInfo.add("3:" + selectedSingle.getListeLigneBonCommandeCommercials().get(i).getPrixUnitaireApresRemise());
                ligneBonCommandeCommercialInfo.add("3:" + selectedSingle.getListeLigneBonCommandeCommercials().get(i).getTotalHT());
                ligneFactures.add(i, ligneBonCommandeCommercialInfo);
            }

            //addInvoiceSummarize
            ArrayList<String> bonCommandeCommercialSummarizeInfos = new ArrayList();

            bonCommandeCommercialSummarizeInfos.add("Total HT" + " : " + selectedSingle.getTotalHT());
            bonCommandeCommercialSummarizeInfos.add("Total TVA" + " : " + selectedSingle.getTotalTVA());
            bonCommandeCommercialSummarizeInfos.add("Total Taxe" + " : " + selectedSingle.getTotalTaxe());
            bonCommandeCommercialSummarizeInfos.add("Total TTC" + " : " + selectedSingle.getTotalTTC());

            GenerationPdf.generationPdf(image, path, "BonCommandeCommercial", numeroBonCommandeCommercial, dateBonCommandeCommercial, entrepriseInfos, clientInfos, ligneBonCommandeCommercialEntete, ligneFactures, bonCommandeCommercialSummarizeInfos, parametrageEntreprise.isGestionParCodeArticle(), utilisateur.getEntreprise().getHeader(), utilisateur.getEntreprise().getFooter());

            File file = new File(path);
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=" + "BonCommandeCommercial_" + numeroBonCommandeCommercial + ".pdf");
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

                items = getFacade().searchAllNative(debut, fin, etatBonCommandeCommercial, client != null ? client.getId() : null);

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

    public LigneBonCommandeCommercial getSelectedLigneBonCommandeCommercial() {
        return selectedLigneBonCommandeCommercial;
    }

    public void setSelectedLigneBonCommandeCommercial(LigneBonCommandeCommercial selectedLigneBonCommandeCommercial) {
        this.selectedLigneBonCommandeCommercial = selectedLigneBonCommandeCommercial;
    }

    public LigneBonCommandeCommercial getSelectedLigneBonCommandeCommercialSingle() {
        return selectedLigneBonCommandeCommercialSingle;
    }

    public void setSelectedLigneBonCommandeCommercialSingle(LigneBonCommandeCommercial selectedLigneBonCommandeCommercialSingle) {
        this.selectedLigneBonCommandeCommercialSingle = selectedLigneBonCommandeCommercialSingle;
    }

    public LigneBonCommandeCommercial getSelectedLigneBonCommandeCommercialModif() {
        return selectedLigneBonCommandeCommercialModif;
    }

    public void setSelectedLigneBonCommandeCommercialModif(LigneBonCommandeCommercial selectedLigneBonCommandeCommercialModif) {
        this.selectedLigneBonCommandeCommercialModif = selectedLigneBonCommandeCommercialModif;
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

    public Integer getEtatBonCommandeCommercial() {
        return etatBonCommandeCommercial;
    }

    public void setEtatBonCommandeCommercial(Integer etatBonCommandeCommercial) {
        this.etatBonCommandeCommercial = etatBonCommandeCommercial;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public SelectItem[] parentFils() {
        // TODO check poste for son
        if (selected.getEtatCommande().getRang() == 0) {

            // i will not use request => i will use the parent son instead
            List<EtatCommande> etatCommandes = new ArrayList<EtatCommande>();
            etatCommandes.addAll(selected.getEtatCommande().getListEtatCommandesFils());
            etatCommandes.add(selected.getEtatCommande());
            return JsfUtil.getSelectItems(etatCommandes, true);

            //return JsfUtil.getSelectItems(ejbFacadeEtatCommande.findAllNative(" where ( o.ECm_Rang = 1 OR o.ECm_Rang = 0 )" ), true);
        } else if (selected.getEtatCommande().isDernierRang() == true) {
            // int rangFils = selected.getEtatCommande().getRang() - 1;
            List<EtatCommande> etatCommandes = new ArrayList<EtatCommande>();

            etatCommandes.add(selected.getEtatCommande());
            etatCommandes.add(selected.getEtatCommande().getParent());

            //etatCommandes = ejbFacadeEtatCommande.findAllNative(" where o.ECm_Rang = " + rangFils + " OR o.ECm_Id = "+selected.getEtatCommande().getId());
            etatCommandes.add(selected.getEtatCommande());
            return JsfUtil.getSelectItems(etatCommandes, true);

        } else {
            //int rangParent = selected.getEtatCommande().getRang() + 1;
            //int rangFils = selected.getEtatCommande().getRang() - 1;
            List<EtatCommande> etatCommandes = new ArrayList<EtatCommande>();

            etatCommandes.add(selected.getEtatCommande());// RETURN TO PARENT
            etatCommandes.add(selected.getEtatCommande().getParent());// STAY
            etatCommandes.addAll(selected.getEtatCommande().getListEtatCommandesFils()); //GO TO SUN

            //etatCommandes = ejbFacadeEtatCommande.findAllNative(" where  ( o.ECm_Rang = " + rangParent + " OR o.ECm_Rang = " + rangFils + " OR o.ECm_Id = "+selected.getEtatCommande().getId()+" )");
            // etatCommandes.add(selected.getEtatCommande());
            return JsfUtil.getSelectItems(etatCommandes, true);

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

    public SelectItem[] getItemsAvailableSelectOneClientLivraison() {
        listClientLivraison = new ArrayList<>();
        if (selected.getCategorieClientLivraison() != null) {
            listClientLivraison = ejbFacadeClient.findAllNative(" where o.CCl_Id = " + selected.getCategorieClientLivraison().getId());
        }
        return JsfUtil.getSelectItems(listClientLivraison, true);

    }

    public SelectItem[] getItemsAvailableSelectOneClient() {
        listClient = new ArrayList<>();
        if (selected.getCategorieClient() != null) {
            listClient = ejbFacadeClient.findAllNative(" where o.CCl_Id = " + selected.getCategorieClient().getId());
        }
        return JsfUtil.getSelectItems(listClient, true);

    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public BonCommandeCommercial getBonCommandeCommercial(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = BonCommandeCommercial.class)
    public static class BonCommandeCommercialControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BonCommandeCommercialController controller = (BonCommandeCommercialController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "bonCommandeCommercialController");
            return controller.getBonCommandeCommercial(getKey(value));
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
            if (object instanceof BonCommandeCommercial) {
                BonCommandeCommercial o = (BonCommandeCommercial) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + BonCommandeCommercial.class.getName());
            }
        }

    }

}
