/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.util;

import com.magma.entity.Privileges;
import com.magma.entity.Utilisateur;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.context.FacesContext;

/**
 *
 * @author inzo
 */
public class MenuTemplate {

    private static List<String> ListFonctionalite = new ArrayList<String>(Arrays.asList(
            "GCommande",
            "GEtatCommande",
            "GDevis",
            "GBonCommandeVente",
            "GBonLivraison",
            "GFacture",
            "GEncaissement",
            "GEncaissementBonLivraison",
            "GTypeEncaissementVente",
            "GParametrageTaxe",
            "GPrefixDevis",
            "GPrefixBonCommandeVente",
            "GPrefixFacture",
            "GPrefixBonLivraison",
            "GMotifRejetDevisVente",
            "GAvoirVente",
            "GPrefixAvoirVente",
            "GRetour",
            "GMotifAvoir",
            "GArticle",
            "GCategorie",
            "GTva",
            "GStockArticle",
            "GClient",
            "GCategorieClient",
            "GUtilisateur",
            "GProfile",
            "GEntreprise",
            "GDepartement",
            "GPoste",
            "GGouvernorat",
            "GDelegation",
            "GBanque",
            "GTicket",
            "GClassificationClient",
            "GTemplateArticleVisite"
    ));

    private static List<String> ListFonctionaliteModule = new ArrayList<String>(Arrays.asList(
            "MParametrage",
            "MProduit",
            "MClient",
            "MVente",
            "MStock",
            "MCommande",
            "Mcommercial"));

    public static void menuFonctionnalitesModules(String fonctionnalite, String fonctionnaliteModule, String fonctionnaliteSousModule, Utilisateur utilisateur) {
        FacesContext context = FacesContext.getCurrentInstance();
        //String Module1 = "";
        /*   for (String privilege : ListFonctionalite) {
         if (privilege.equals(fonctionnalite)) {
         context.getExternalContext().getSessionMap().put(privilege, "active-menuitem");
         } else {
         context.getExternalContext().getSessionMap().put(privilege, "");
         }

         }

         for (String module : ListFonctionaliteModule) {

         context.getExternalContext().getSessionMap().put(module, "");
         }
         context.getExternalContext().getSessionMap().put(fonctionnaliteModule, "display: block");*/

        // i will retrieve the old selected privilege , model and sous module
        MenuItemProperties s = (MenuItemProperties) context.getExternalContext().getSessionMap().get("OldMenuItemProperties");
        if (s != null) {
            context.getExternalContext().getSessionMap().put(s.getSelectedPrivilegeLibelle(), "");
            context.getExternalContext().getSessionMap().put(s.getSelectedActivationPrivilegeModule(), "");
            context.getExternalContext().getSessionMap().put(s.getSelectedPrivilegeModule(), "");

            if (s.getSelectedActivationPrivilegSousMdule() != null && !s.getSelectedActivationPrivilegSousMdule().equals("")) {
                context.getExternalContext().getSessionMap().put(s.getSelectedActivationPrivilegSousMdule(), "");
                context.getExternalContext().getSessionMap().put(s.getSelectedPrivilegSousMdule(), "");
            }

        } else {
            s = new MenuItemProperties();
        }

        s.setSelectedPrivilegeLibelle(fonctionnalite);
        s.setSelectedPrivilegeModule(fonctionnaliteModule);
        s.setSelectedActivationPrivilegeModule("A" + fonctionnaliteModule);

        context.getExternalContext().getSessionMap().put(s.getSelectedPrivilegeLibelle(), "active-menuitem");
        context.getExternalContext().getSessionMap().put(s.getSelectedActivationPrivilegeModule(), "active-menuitem");
        context.getExternalContext().getSessionMap().put(s.getSelectedPrivilegeModule(), "display: block;");

        if (fonctionnaliteSousModule != null && !fonctionnaliteSousModule.equals("")) {
            s.setSelectedPrivilegSousMdule("S" + fonctionnaliteSousModule);
            s.setSelectedActivationPrivilegSousMdule("SA" + fonctionnaliteSousModule);
            context.getExternalContext().getSessionMap().put(s.getSelectedActivationPrivilegSousMdule(), "active-menuitem");
            context.getExternalContext().getSessionMap().put(s.getSelectedPrivilegSousMdule(), "display: block;");
        }

        context.getExternalContext().getSessionMap().put("OldMenuItemProperties", s);

    }

    public static void menuFonctionnalitesModules(String fonctionnalite, Utilisateur utilisateur) {
        FacesContext context = FacesContext.getCurrentInstance();
        String module = "";
        String sousModule = "";
        for (Privileges privilege : utilisateur.getProfile().getListPrivileges()) {

            //Vissibilité pour les fonctionnalitées
            if (privilege.isLecture() == true) {
                /* if (privilege.getOrder() == 900050) {
                 if (utilisateur.getEntrepriseEnCours().isMultiDomaine()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 910040) {
                 if (utilisateur.getEntrepriseEnCours().isSynchronisationMFG()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 910070) {
                 if (utilisateur.getEntrepriseEnCours().isModuleProduction()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 910080) {
                 if (utilisateur.getEntrepriseEnCours().isModuleProduction()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 910030) {
                 if (utilisateur.getEntrepriseEnCours().isModuleVente()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 600080) {
                 if (utilisateur.getEntrepriseEnCours().isModuleProduction()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 600090) {
                 if (utilisateur.getEntrepriseEnCours().isModuleProduction()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 600100) {
                 if (utilisateur.getEntrepriseEnCours().isModuleProduction()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 600110) {
                 if (utilisateur.getEntrepriseEnCours().isModuleProduction()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 600120) {
                 if (utilisateur.getEntrepriseEnCours().isModuleProduction()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 620130) {
                 if (utilisateur.getEntrepriseEnCours().isGestionConsomateur()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else if (privilege.getOrder() == 50040) {
                 if (utilisateur.getEntrepriseEnCours().isGestionConsomateur()) {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                 } else {
                 context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                 }
                 } else {*/
                context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                //}

            } else {
                context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
            }

            if (privilege.getLibelle().equals(fonctionnalite)) {
                context.getExternalContext().getSessionMap().put(privilege.getLibelle(), "active-menuitem");
                context.getExternalContext().getSessionMap().put("A" + privilege.getModule(), "active-menuitem");
                context.getExternalContext().getSessionMap().put(privilege.getModule(), "display: block;");
                //context.getExternalContext().getSessionMap().put(privilege.getLibelle() + "Style", "color: #1a8ea5;");
                //context.getExternalContext().getSessionMap().put(privilege.getModule() + "Style", "color: #1a8ea5;");
                module = privilege.getModule();
                if (privilege.isSousModule() == true) {
                    context.getExternalContext().getSessionMap().put("SA" + privilege.getModule(), "active-menuitem");
                    context.getExternalContext().getSessionMap().put("S" + privilege.getModule(), "display: block;");
                    sousModule = "SA" + privilege.getModule();
                }
            } else {
                context.getExternalContext().getSessionMap().put(privilege.getLibelle(), "");
                // context.getExternalContext().getSessionMap().put(privilege.getLibelle() + "Style", "");

                if (module.equals("")) {
                    context.getExternalContext().getSessionMap().put("A" + privilege.getModule(), "");
                    context.getExternalContext().getSessionMap().put(privilege.getModule(), "");
                    //context.getExternalContext().getSessionMap().put(privilege.getModule() + "Style", "");
                } else {
                    if (!module.equals(privilege.getModule())) {
                        context.getExternalContext().getSessionMap().put("A" + privilege.getModule(), "");
                        context.getExternalContext().getSessionMap().put(privilege.getModule(), "");
                        //context.getExternalContext().getSessionMap().put(privilege.getModule() + "Style", "");
                    }
                }
                if (privilege.isSousModule() == true) {
                    if (sousModule.equals("")) {
                        context.getExternalContext().getSessionMap().put("SA" + privilege.getModule(), "");
                        context.getExternalContext().getSessionMap().put("S" + privilege.getModule(), "");
                    } else {
                        if (!sousModule.equals("SA" + privilege.getModule())) {
                            context.getExternalContext().getSessionMap().put("SA" + privilege.getModule(), "");
                            context.getExternalContext().getSessionMap().put("S" + privilege.getModule(), "");
                        }
                    }
                }
            }
        }

    }

    public static void initMenu() {
        FacesContext context = FacesContext.getCurrentInstance();
        for (String privilege : ListFonctionalite) {

            context.getExternalContext().getSessionMap().put("FV" + privilege, "display: none;");

        }

    }

}
