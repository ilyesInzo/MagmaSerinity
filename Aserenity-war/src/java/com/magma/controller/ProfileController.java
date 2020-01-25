package com.magma.controller;

import com.magma.entity.Privileges;
import com.magma.entity.Profile;
import com.magma.entity.Utilisateur;
import com.magma.session.PrivilegesFacadeLocal;
import com.magma.session.util.JsfUtil;
import com.magma.session.ProfileFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
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

@ManagedBean(name= "profileController")
@SessionScoped
public class ProfileController implements Serializable {

    private Profile selected;
    private Profile selectedSingle;
    private List<Profile> items = null;
    @EJB
    private ProfileFacadeLocal ejbFacade;
    @EJB
    private PrivilegesFacadeLocal ejbFacadePrivileges;
    private boolean errorMsg;
    private Long idTemp;
    private Profile profile;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    private boolean lectureTous;
    private boolean creationTous;
    private boolean modificationTous;
    private boolean supressionTous;
    private boolean activationTous;
    private boolean reinitialisationTous;
    private boolean journalisationTous;
    public ProfileController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (profile.getIdEntrepriseSuivi() != null && profile.getIdEntrepriseSuivi() != 0) {
                idEntreprise = profile.getIdEntrepriseSuivi();
            } else {
                idEntreprise = profile.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GProfile", "MParametrage", null,utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GProfile", utilisateur);
            /*if (profile.getIdEntrepriseSuivi() != null && profile.getIdEntrepriseSuivi() != 0) {
                idEntreprise = profile.getIdEntrepriseSuivi();
            } else {
                idEntreprise = profile.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../profile/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Profile> getItems() {
        try {
            if (items == null) {

                // uniquement le superAdmin peut gerer son profile de super admin
                if (utilisateur.getProfile().isSuperAdmin()) {
                    items = getFacade().findAll("order by o.libelle asc ");
                } else {

                    items = getFacade().findAll("where o.superAdmin = 0 order by o.libelle asc ");

                }

            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- ProfileController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Profile getSelected() {
        return selected;
    }

    public void setSelected(Profile selected) {
        this.selected = selected;
    }

    public Profile getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Profile selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private ProfileFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new Profile();
        errorMsg = false;
        lectureTous = false;
        creationTous = false;
        modificationTous = false;
        supressionTous = false;
        activationTous = false;
        selected.setListPrivileges(new ArrayList<>());
        preparerPrivileges();
        return "Create";
    }

    public List<Privileges> initPrivileges() {

        List<Privileges> listPrivileges = new ArrayList<>();
        Privileges privileges = null;
        //GCommande

        privileges = new Privileges("GCommande","bonCommandeCommercial" ,false, false, false, false, false, 123400700, 10020, "MCommande", 1, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GEtatCommande","etatCommande" ,false, false, false, false, false, 123400700, 10030, "MCommande", 1, false, false, false, true);
        listPrivileges.add(privileges);

        //MVente
        /* Privileges privileges = new Privileges("GDashboard", false, false, false, false, false, 100000000, 20010, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GDevis","devis", false, false, false, false, false, 123400700, 20020, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GBonCommandeVente", false, false, false, false, false, 103000000, 20030, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GBonLivraison","bonLivraison", false, false, false, false, false, 123400700, 20040, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GFacture","facture", false, false, false, false, false, 123400700, 20057, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GEncaissement","encaissement", false, false, false, false, false, 123400700, 20060, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GBonSortie", false, false, false, false, false, 123406000, 20070, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPaquet", false, false, false, false, false, 123400000, 20080, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);*/

 /* privileges = new Privileges("GStockBS", false, false, false, false, false, 100000000, 20100, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GStockPaquet", false, false, false, false, false, 100000000, 20110, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPaquetAnnules", false, false, false, false, false, 100000000, 20120, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GEncaissementBonLivraison","encaissementBonLivraison", false, false, false, false, false, 123400700, 20130, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GRapportVente", false, false, false, false, false, 123400000, 20140, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GTypeVente", false, false, false, false, false, 123400000, 20150, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GNatureVente", false, false, false, false, false, 123400000, 20160, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GTypeEncaissementVente","typeEncaissementVente", false, false, false, false, false, 123400700, 20170, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GRetenuSourceVente", false, false, false, false, false, 1234000, 20180, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GParametrageTaxe","parametrageTaxe", false, false, false, false, false, 123400700, 20190, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPrefixDevis","prefixDevis", false, false, false, false, false, 123400700, 20200, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPrefixFacture","prefixFacture", false, false, false, false, false, 123400700, 20210, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPrefixBonLivraison","prefixBonLivraison",false, false, false, false, false, 123400700, 20220, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GMotifRejetDevisVente","motifRejetDevisVente", false, false, false, false, false, 123400700, 20230, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GMotifsAnnulationBCVente", false, false, false, false, false, 123400000, 20240, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GMotifRejetBonLivraisonVente", false, false, false, false, false, 123400000, 20250, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GAvoirVente","avoirVente", false, false, false, false, false, 120000000, 20260, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPrefixAvoirVente","prefixAvoirVente", false, false, false, false, false, 123400700, 20270, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GTicket", false, false, false, false, false, 123400000, 20280, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GRetour","retour", false, false, false, false, false, 103400700, 20280, "MVente", 2, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GMotifAvoir","motifAvoir", false, false, false, false, false, 123400700, 20290, "MVente", 2, false, false, true, true);
        listPrivileges.add(privileges);

       
        //Articles
        privileges = new Privileges("GArticle","article", false, false, false, false, false, 123400700, 600010, "MProduit", 60, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GArticleConcurrent", false, false, false, false, false, 123400000, 600020, "MCatalogueArticles", 60, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GCategorie","categorie", false, false, false, false, false, 123400700, 600030, "MProduit", 60, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GMeteoArticle", false, false, false, false, false, 123400000, 600040, "MCatalogueArticles", 60, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GTypeMesure", false, false, false, false, false, 123400000, 600050, "MCatalogueArticles", 60, false, false, true, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GTva","tva", false, false, false, false, false, 123400700, 600060, "MProduit", 60, false, false, true, true);
        listPrivileges.add(privileges);
        /* privileges = new Privileges("TPlageQuantite", false, false, false, false, false, 123400000, 600070, "MCatalogueArticles", 60, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GCategorieSpecialeClients", false, false, false, false, false, 123400000, 600080, "MCatalogueArticles", 60, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("TConditionnement", false, false, false, false, false, 123450000, 600090, "MCatalogueArticles", 60, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("TConcentration", false, false, false, false, false, 123450000, 600100, "MCatalogueArticles", 60, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("TGranulometrie", false, false, false, false, false, 123450000, 600110, "MCatalogueArticles", 60, false, false, true, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("TForme", false, false, false, false, false, 123450000, 600120, "MCatalogueArticles", 60, false, false, true, true);
        listPrivileges.add(privileges);*/

        //Stock
        privileges = new Privileges("GStockArticle","stock", false, false, false, false, false, 103000700, 130090, "MStock", 13, false, false, false, true);
        listPrivileges.add(privileges);

        //Clients
        /* privileges = new Privileges("GEnsigne", false, false, false, false, false, 123400000, 620000, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GClient","client", false, false, false, false, false, 123456700, 620010, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);
        /* privileges = new Privileges("GNouveauClient", false, false, false, false, false, 123400000, 620020, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GCategorieClient","categorieClient", false, false, false, false, false, 123400700, 620030, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GClassificationClient","classificationClient", false, false, false, false, false, 123400700, 620040, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GLigneLivraison", false, false, false, false, false, 123400000, 620050, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GGroupeClient", false, false, false, false, false, 123400000, 620060, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPointDeVente", false, false, false, false, false, 123450700, 620070, "MClient", 62, false, false, false, true );
        listPrivileges.add(privileges);
        privileges = new Privileges("GTypePVT", false, false, false, false, false, 123400000, 620080, "MClient", 62, false, false, false, true );
        listPrivileges.add(privileges);
        privileges = new Privileges("GClassPVT", false, false, false, false, false, 123400000, 620090, "MClient", 62, false, false, false, true );
        listPrivileges.add(privileges);
        privileges = new Privileges("GPatrimoinePVT", false, false, false, false, false, 123400700, 620100, "MClient", 62, false, false, false, true );
        listPrivileges.add(privileges);
        privileges = new Privileges("GMotifBlocagePVT", false, false, false, false, false, 123400000, 620110, "MClient", 62, false, false, false, true );
        listPrivileges.add(privileges);
        privileges = new Privileges("GGroupePVT", false, false, false, false, false, 123400000, 620120, "MClient", 62, false, false, false, true );
        listPrivileges.add(privileges);
        privileges = new Privileges("GConsommateurFinal", false, false, false, false, false, 123400000, 620130, "MClient", 62, false, false, false, true);
        listPrivileges.add(privileges);*/

        
        //MCommerciale
        
        privileges = new Privileges("GCommercial","commercial", false, false, false, false, false, 123400700, 60120, "MCommercial", 6, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPlanificationVisite","planificationVisite", false, false, false, false, false, 123400000, 60130, "MCommercial", 6, false, false, false, true);
        listPrivileges.add(privileges);
        
        
        //Parametrage
        privileges = new Privileges("GUtilisateur","utilisateur", false, false, false, false, false, 123456700, 900010, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GProfile","profile", false, false, false, false, false, 123400700, 900020, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GDomaineActivite", false, false, false, false, false, 123400000, 900030, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GEntreprise","entreprise", false, false, false, false, false, 123400000, 900040, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GDomaineEntreprise", false, false, false, false, false, 123400000, 900050, "MParametrage", 90, false, false, false,true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GDepartement","departement", false, false, false, false, false, 123400700, 900060, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPoste","poste", false, false, false, false, false, 123400700, 900070, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GConcurrents", false, false, false, false, false, 123450000, 900080, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GPays", false, false, false, false, false, 100000000, 900085, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GGouvernorat","gouvernorat", false, false, false, false, false, 100000000, 900090, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GDelegation","delegation", false, false, false, false, false, 100000000, 900100, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GBanque","banque", false, false, false, false, false, 123400700, 900110, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        /*privileges = new Privileges("GPatrimoine", false, false, false, false, false, 123400000, 900130, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GCalendrierFeries", false, false, false, false, false, 123400000, 900140, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);
        privileges = new Privileges("GMotifAvoir", false, false, false, false, false, 123400000, 900150, "MParametrage", 90, false, false, false, true);
        listPrivileges.add(privileges);*/
        privileges = new Privileges("GTicket","ticket", false, false, false, false, false, 123400700, 900160, "MParametrage", 90, false, false, true, true);
        listPrivileges.add(privileges);

        return listPrivileges;
    }

    public void preparerPrivileges() {

        //selected.setListPrivileges(new ArrayList<>());
        Set<Privileges> set = new TreeSet<>(selected.getListPrivileges());
        System.out.println("set.size"+set.size());
        set.addAll(initPrivileges());
        System.out.println("set.size"+set.size());
        selected.setListPrivileges(new ArrayList<>(set));

    }

    public boolean nonModifiable(int order) {

        // la pluspart du temps c'est modifiable
        boolean nonModifiable = false;

        // si je suis superAdmin je dois toujours avoir la liste des profiles et des utilisateurs
        if (selected.isSuperAdmin()) {

            if (order == 900010 || order == 900020) {
                // non modifiable
                return true;
            }

        } else {
            return false;
        }

        return nonModifiable;

    }

    public void onSelectLecture() {
        if (lectureTous == false) {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {

                if (!nonModifiable(selected.getListPrivileges().get(i).getOrder())) {
                    selected.getListPrivileges().get(i).setLecture(false);
                }

                selected.getListPrivileges().get(i).setCreation(false);
                selected.getListPrivileges().get(i).setModification(false);
                selected.getListPrivileges().get(i).setSupression(false);
                selected.getListPrivileges().get(i).setActivation(false);
                selected.getListPrivileges().get(i).setReinitialisation(false);
                selected.getListPrivileges().get(i).setJournalisation(false);
            }
            creationTous = false;
            modificationTous = false;
            supressionTous = false;
            activationTous = false;
            reinitialisationTous = false;
            journalisationTous = false;
        } else {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                selected.getListPrivileges().get(i).setLecture(true);
            }
        }

    }

    public void onSelectCreation() {
        if (creationTous == false) {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("2")) {
                    selected.getListPrivileges().get(i).setCreation(false);
                }
            }
        } else {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("2")) {
                    selected.getListPrivileges().get(i).setLecture(true);
                    selected.getListPrivileges().get(i).setCreation(true);
                }
            }
            lectureTous = creationTous;
        }
    }

    public void onSelectModification() {
        if (modificationTous == false) {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("3")) {
                    selected.getListPrivileges().get(i).setModification(false);
                }
            }
        } else {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("3")) {
                    selected.getListPrivileges().get(i).setLecture(true);
                    selected.getListPrivileges().get(i).setModification(true);
                }
            }
            lectureTous = true;
        }
    }

    public void onSelectSupression() {
        if (supressionTous == false) {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("4")) {
                    selected.getListPrivileges().get(i).setSupression(false);
                }
            }
        } else {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("4")) {
                    selected.getListPrivileges().get(i).setLecture(true);
                    selected.getListPrivileges().get(i).setSupression(true);
                }
            }
            lectureTous = true;
        }
    }

    public void onSelectActivation() {
        if (activationTous == false) {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("5")) {
                    selected.getListPrivileges().get(i).setActivation(false);
                }
            }
        } else {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("5")) {
                    selected.getListPrivileges().get(i).setLecture(true);
                    selected.getListPrivileges().get(i).setActivation(true);
                }
            }
            lectureTous = true;
        }
    }
    
    public void onSelectReinitialisation() {
        if (reinitialisationTous == false) {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("6")) {
                    selected.getListPrivileges().get(i).setReinitialisation(false);
                }
            }
        } else {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("6")) {
                    selected.getListPrivileges().get(i).setLecture(true);
                    selected.getListPrivileges().get(i).setReinitialisation(true);
                }
            }
            lectureTous = true;
        }
    }
    
    public void onSelectJournalisation() {
        if (journalisationTous == false) {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("7")) {
                    selected.getListPrivileges().get(i).setJournalisation(false);
                }
            }
        } else {
            for (int i = 0; i < selected.getListPrivileges().size(); i++) {
                if (selected.getListPrivileges().get(i).getNbPrivelegeString().contains("7")) {
                    selected.getListPrivileges().get(i).setLecture(true);
                    selected.getListPrivileges().get(i).setJournalisation(true);
                }
            }
            lectureTous = true;
        }
    }

    public void onRowSelectLecture(int indexTemp) {
        if (selected.getListPrivileges().get(indexTemp).isLecture() == true) {
            lectureTous = true;
        } else {
            selected.getListPrivileges().get(indexTemp).setCreation(false);
            selected.getListPrivileges().get(indexTemp).setModification(false);
            selected.getListPrivileges().get(indexTemp).setSupression(false);
            selected.getListPrivileges().get(indexTemp).setActivation(false);
            selected.getListPrivileges().get(indexTemp).setReinitialisation(false);
            selected.getListPrivileges().get(indexTemp).setJournalisation(false);
        }
    }

    public void onRowSelect(int indexTemp) {
        if (selected.getListPrivileges().get(indexTemp).isCreation() == true) {
            creationTous = true;
            selected.getListPrivileges().get(indexTemp).setLecture(true);
            lectureTous = true;
        }
        if (selected.getListPrivileges().get(indexTemp).isModification() == true) {
            modificationTous = true;
            selected.getListPrivileges().get(indexTemp).setLecture(true);
            lectureTous = true;
        }
        if (selected.getListPrivileges().get(indexTemp).isSupression() == true) {
            supressionTous = true;
            selected.getListPrivileges().get(indexTemp).setLecture(true);
            lectureTous = true;
        }
        if (selected.getListPrivileges().get(indexTemp).isActivation() == true) {
            activationTous = true;
            selected.getListPrivileges().get(indexTemp).setLecture(true);
            lectureTous = true;
        }
        if (selected.getListPrivileges().get(indexTemp).isReinitialisation() == true) {
            reinitialisationTous = true;
            selected.getListPrivileges().get(indexTemp).setLecture(true);
            lectureTous = true;
        }
        if (selected.getListPrivileges().get(indexTemp).isJournalisation() == true) {
            journalisationTous = true;
            selected.getListPrivileges().get(indexTemp).setLecture(true);
            lectureTous = true;
        }
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {

                boolean insert = false;
                List<Privileges> temps = selected.getListPrivileges();
                for (Privileges temp : temps) {
                    if (temp.isLecture() == true) {
                        insert = true;
                        break;
                    }
                }
                if (insert == true) {
                    selected.setListPrivileges(null);
                    getFacade().create(selected);
                    for (Privileges temp : temps) {
                        temp.setProfile(selected);
                    }
                    ejbFacadePrivileges.create(temps);
                    temps = null;

                    return prepareList();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurPrivileges")));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- ProfileController - create: " + e.getMessage());
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
            preparerPrivileges();
            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());
            boolean insert = false;
            if (errorMsg == false) {
                List<Privileges> temps = new ArrayList<Privileges>();
                temps = selected.getListPrivileges();
                for (int i = 0; i < temps.size(); i++) {
                    if (temps.get(i).isLecture() == true) {
                        insert = true;
                    }
                }
                if (insert == true) {
                    selected.setListPrivileges(null);
                    getFacade().edit(selected);
                    for (int i = 0; i < temps.size(); i++) {
                        temps.get(i).setProfile(selected);
                    }
                    ejbFacadePrivileges.edit(temps);
                    return prepareList();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurPrivileges")));
                    return null;
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- ProfileController - update: " + e.getMessage());
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
            if (!selectedSingle.isSuperAdmin()) {
                getFacade().remove(selectedSingle);
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- ProfileController - performDestroy: " + e.getMessage());
        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isLectureTous() {
        return lectureTous;
    }

    public void setLectureTous(boolean lectureTous) {
        this.lectureTous = lectureTous;
    }

    public boolean isCreationTous() {
        return creationTous;
    }

    public void setCreationTous(boolean creationTous) {
        this.creationTous = creationTous;
    }

    public boolean isModificationTous() {
        return modificationTous;
    }

    public void setModificationTous(boolean modificationTous) {
        this.modificationTous = modificationTous;
    }

    public boolean isSupressionTous() {
        return supressionTous;
    }

    public void setSupressionTous(boolean supressionTous) {
        this.supressionTous = supressionTous;
    }

    public boolean isActivationTous() {
        return activationTous;
    }

    public void setActivationTous(boolean activationTous) {
        this.activationTous = activationTous;
    }
    
    public boolean isReinitialisationTous() {
        return reinitialisationTous;
    }

    public void setReinitialisationTous(boolean reinitialisationTous) {
        this.reinitialisationTous = reinitialisationTous;
    }

    public boolean isJournalisationTous() {
        return journalisationTous;
    }

    public void setJournalisationTous(boolean journalisationTous) {
        this.journalisationTous = journalisationTous;
    }
    
    

    public SelectItem[] getItemsAvailableSelectMany() {

        /*if (utilisateur == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        }

        if (utilisateur.getProfile().isSuperAdmin()) {
            return JsfUtil.getSelectItems(ejbFacade.findAll("order by o.libelle asc "), false);
        } else {*/

            return JsfUtil.getSelectItems(ejbFacade.findAll("where o.superAdmin = 0 order by o.libelle asc "), false);
        //}

    }

    public SelectItem[] getItemsAvailableSelectOne() {

        /*if (utilisateur == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        }

        if (utilisateur.getProfile().isSuperAdmin()) {
            return JsfUtil.getSelectItems(ejbFacade.findAll("order by o.libelle asc "), true);
        } else {
*/
            return JsfUtil.getSelectItems(ejbFacade.findAll("where o.superAdmin = 0 order by o.libelle asc "), true);
        //}
    }

    public Profile getProfile(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Profile.class)
    public static class ProfileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProfileController controller = (ProfileController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "profileController");
            return controller.getProfile(getKey(value));
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
            if (object instanceof Profile) {
                Profile o = (Profile) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Profile.class.getName());
            }
        }

    }

}
