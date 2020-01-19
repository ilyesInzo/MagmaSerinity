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
import com.magma.entity.EtatCommande;
import com.magma.entity.LigneBonCommandeCommercial;
import com.magma.entity.Utilisateur;
import com.magma.session.BonCommandeCommercialFacadeLocal;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.EtatCommandeFacadeLocal;
import com.magma.session.LigneBonCommandeCommercialFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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

@ManagedBean(name = "bonCommandeCommercialController")
@SessionScoped
public class BonCommandeCommercialController implements Serializable {

    private BonCommandeCommercial selected;
    private BonCommandeCommercial selectedSingle;
    private List<BonCommandeCommercial> items = null;

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
    private EtatCommandeFacadeLocal ejbFacadeEtatCommande;

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
    private Utilisateur utilisateur;

    private Date dateDebut = new Date();
    private Date dateFin = new Date();
    private Integer etatBonCommandeCommercial;
    private Integer origineBonCommandeCommercial;
    private Client client;
    private boolean retour;

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
            MenuTemplate.menuFonctionnalitesModules("GCommande", "MCommande", null, utilisateur);

            // MenuTemplate.menuFonctionnalitesModules("GBonCommandeCommercial", utilisateur);
            /*if (bonCommandeCommercial.getIdEntrepriseSuivi() != null && bonCommandeCommercial.getIdEntrepriseSuivi() != 0) {
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

                String clause = " where o.Cmd_DateCommande between '" + debut + "' and '" + fin + "' ";
                System.out.println("" + clause);
                items = getFacade().findAllNative(clause + " order by o.Cmd_DateCommande desc");
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

            return "View";
        }
        return "List";
    }

    public String prepareCreate() {

        categorieClient = new CategorieClient();
        selected = new BonCommandeCommercial();
        errorMsg = false;
        selected.setDateCommande(new Date());
        EtatCommande etatCommande = null;
        /*try {
            etatCommande = ejbFacadeEtatCommande.findAll(" where o.rang = 0 and o.supprimer = 0 ").get(0);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EtatCmdNull")));
            return null;
        }
        selected.setEtatCommande(etatCommande);*/

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

        selected.setListeLigneBonCommandeCommercials(new ArrayList<LigneBonCommandeCommercial>());

        categorie = new Categorie();
        return "Create";
    }

    public String create() {

        errorMsg = false;

        if (errorMsg == false) {

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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
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
            categorie = new Categorie();

            Client client = new Client();
            client.setId(selected.getIdClient());
            client.setAssujettiTVA(selected.isAssujettiTVA());
            client.setLibelle(selected.getLibelleClient());
            selected.setClient(client);

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

                LigneBonCommandeCommercialTemps = new ArrayList<LigneBonCommandeCommercial>(selected.getListeLigneBonCommandeCommercials());
                selected.setListeLigneBonCommandeCommercials(null);

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
                sauvegardeNouvelleLigneBonCommande();

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

    private void sauvegardeNouvelleLigneBonCommande() {

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
        selected.setMontantHT(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTVA(new BigDecimal(BigInteger.ZERO));
        selected.setMontantTTC(new BigDecimal(BigInteger.ZERO));
        for (LigneBonCommandeCommercial ligneBonCommandeCommercial : selected.getListeLigneBonCommandeCommercials()) {
            selected.setMontantHT(selected.getMontantHT().add(ligneBonCommandeCommercial.getTotalHT()));
            selected.setMontantTVA(selected.getMontantTVA().add(ligneBonCommandeCommercial.getTotalTVA()));
            selected.setMontantTTC(selected.getMontantTTC().add(ligneBonCommandeCommercial.getTotalTTC()));
        }

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
            selectedLigneBonCommandeCommercial.setTotalTTC(BigDecimal.ZERO);
        } else {
            selectedLigneBonCommandeCommercial.setTotalHT(BigDecimal.ZERO);
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

//            BigDecimal valTemp = FonctionsMathematiques.arrondiBigDecimal(selectedLigneBonCommandeCommercial.getMontantHT().multiply(selectedLigneBonCommandeCommercial.getTvaArticle()), 3);
//            selectedLigneBonCommandeCommercial.setMontantTVA(FonctionsMathematiques.arrondiBigDecimal(valTemp.divide(BigDecimal.valueOf(100)), 3));
//            selectedLigneBonCommandeCommercial.setMontantTTC(selectedLigneBonCommandeCommercial.getMontantHT().add(selectedLigneBonCommandeCommercial.getMontantTVA()));
        }
    }

    public BigDecimal getTotaleHT() {
        BigDecimal totalHT = new BigDecimal(0);
        if (selectedLigneBonCommandeCommercial.getLibelleArticle() == null) {
            return new BigDecimal(0);
        }
        BigDecimal prixRevendeur = selectedLigneBonCommandeCommercial.getPrixUnitaireHT();
        totalHT = prixRevendeur.multiply(selectedLigneBonCommandeCommercial.getQuantite());
        if (totalHT != new BigDecimal(0)) {
            return FonctionsMathematiques.arrondiBigDecimal(totalHT, 3);
        } else {
            return FonctionsMathematiques.arrondiBigDecimal(new BigDecimal(0), 3);
        }

    }

    public BigDecimal getTotaleTTC() {
        BigDecimal totalTTC = new BigDecimal(0);
        if (selectedLigneBonCommandeCommercial.getLibelleArticle() == null) {
            return new BigDecimal(0);
        }
        BigDecimal prixRevendeur = selectedLigneBonCommandeCommercial.getPrixUnitaireHT();

        BigDecimal totaleHT = (prixRevendeur).multiply(selectedLigneBonCommandeCommercial.getQuantite());
        totalTTC = totaleHT.add(totaleHT.multiply(selectedLigneBonCommandeCommercial.getTvaArticle().divide(BigDecimal.valueOf(100))));

        if (totalTTC != new BigDecimal(0)) {
            return FonctionsMathematiques.arrondiBigDecimal(totalTTC, 3);
        } else {
            return FonctionsMathematiques.arrondiBigDecimal(new BigDecimal(0), 3);
        }
    }

    public void rechercher() {

        try {

            if (dateDebut.getTime() <= dateFin.getTime()) {

                String debut = TraitementDate.returnDate(dateDebut) + " 00:00:00";
                String fin = TraitementDate.returnDate(dateFin) + " 23:59:59";

                //items = getFacade().searchAllNative(debut, fin, etatBonCommandeCommercial, origineBonCommandeCommercial, client != null ? client.getId() : null, retour);
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

    public Integer getEtatBonCommandeCommercial() {
        return etatBonCommandeCommercial;
    }

    public void setEtatBonCommandeCommercial(Integer etatBonCommandeCommercial) {
        this.etatBonCommandeCommercial = etatBonCommandeCommercial;
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

    public Integer getOrigineBonCommandeCommercial() {
        return origineBonCommandeCommercial;
    }

    public void setOrigineBonCommandeCommercial(Integer origineBonCommandeCommercial) {
        this.origineBonCommandeCommercial = origineBonCommandeCommercial;
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
        if (selected.getCategorieClient() != null) {
            listClient = ejbFacadeClient.findAllNative(" where o.CCl_Id = " + selected.getCategorieClient().getId());
        }
        return JsfUtil.getSelectItems(listClient, true);

    }

    public SelectItem[] getItemsAvailableSelectOneClientLivraison() {
        listClientLivraison = new ArrayList<>();
        if (selected.getCategorieClientLivraison() != null) {
            listClientLivraison = ejbFacadeClient.findAllNative(" where o.CCl_Id = " + selected.getCategorieClientLivraison().getId());
        }
        return JsfUtil.getSelectItems(listClientLivraison, true);

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
