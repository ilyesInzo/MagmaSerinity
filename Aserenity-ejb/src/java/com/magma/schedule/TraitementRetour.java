/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.schedule;

import com.magma.bibliotheque.TraitementDate;
import com.magma.entity.BonLivraison;
import com.magma.entity.Facture;
import com.magma.entity.LigneBonLivraison;
import com.magma.entity.LigneFacture;
import com.magma.entity.LigneRetour;
import com.magma.entity.Retour;
import com.magma.session.BonLivraisonFacadeLocal;
import com.magma.session.FactureFacadeLocal;
import com.magma.session.LigneBonLivraisonFacadeLocal;
import com.magma.session.LigneFactureFacadeLocal;
import com.magma.session.LigneRetourFacadeLocal;
import com.magma.session.RetourFacadeLocal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author inzo
 */
@Singleton
@Startup
public class TraitementRetour {

    @EJB
    private RetourFacadeLocal ejbFacadeRetour;
    @EJB
    private LigneRetourFacadeLocal ejbFacadeLigneRetour;

    @EJB
    private LigneFactureFacadeLocal ejbFacadeLigneFacture;
    @EJB
    private FactureFacadeLocal ejbFacadeFacture;

    @EJB
    private BonLivraisonFacadeLocal ejbFacadeBonLivraison;
    @EJB
    private LigneBonLivraisonFacadeLocal ejbFacadeLigneBonLivraison;

    private String dateJour;
    @Schedule(minute = "00", hour = "02", dayOfMonth = "*")
    public void runCreationRetour() {
        dateJour = TraitementDate.returnDate( TraitementDate.moinsPlusJours(  TraitementDate.debutJournee(new Date()) , 1)  );
        System.out.println("**************************** runCreationRetour : " + dateJour + "*****************************");

        createRetourBonLivraison();
        createRetourFacture();
    }

    private void createRetourFacture() {

        // toutes les factures non annulée , sans retours déja émis et date inférieur a date facture
        List<Facture> listFactures = ejbFacadeFacture.findAllNative(" where o.Fct_Etat <> 2 and o.Ret_Id is null and o.Fct_DateFacture < '" + dateJour + "'");
        List<BonLivraison> listSelectedBonLivraison;
        System.out.println("listFactures : " + listFactures.size());
        for (Facture facture : listFactures) {

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
            List<LigneFacture> listLigneFactures;

            // en cas ou les lignes ne sont pas loader avec les factures
            if (facture.getListeLigneFactures() != null) {
                listLigneFactures = facture.getListeLigneFactures();
            } else {
                listLigneFactures = ejbFacadeLigneFacture.findAllNative(" where o.Fct_Id = " + facture.getId());
            }
            System.out.println("listLigneFactures : " + listLigneFactures.size());

            for (LigneFacture ligneFacture : listLigneFactures) {
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

            if (facture.getOrigineFacture() == 2) {

                // si la BL
                listSelectedBonLivraison = ejbFacadeBonLivraison.findAllNative(" where o.BLiv_idFacture = " + facture.getId() + " and o.Ret_Id is null");
                System.out.println("listSelectedBonLivraison : " + listSelectedBonLivraison.size());
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
            ejbFacadeFacture.updateAllNative("SET Ret_Id = " + retour.getId() + " where Fct_Id = " + facture.getId());
        }

        listFactures = null;
        listSelectedBonLivraison = null;
    }

    private void createRetourBonLivraison() {

        // toutes les bls non annulée , sans retours  émies, non transformer en facture et date inférieur a date bl
        List<BonLivraison> listBonLivraisons = ejbFacadeBonLivraison.findAllNative(" where o.BLiv_Etat <> 2 and o.Ret_Id is null and o.BLiv_idFacture is null and o.BLiv_DateBonLivraison < '" + dateJour + "'");
        System.out.println("listBonLivraisons : " + listBonLivraisons.size());
        for (BonLivraison bonLivraison : listBonLivraisons) {

            Retour retour = new Retour();
            retour.setLibelleClient(bonLivraison.getLibelleClient());
            retour.setIdClient(bonLivraison.getIdClient());
            retour.setOrigineRetour(1);
            retour.setEtat(0);
            retour.setAssujettiTVA(bonLivraison.isAssujettiTVA());
            retour.setNumeroFactureBL(bonLivraison.getNumero());
            retour.setIdFactureBL(bonLivraison.getId());
            retour.setDateCreation(new Date());
            retour.setTotalHT(BigDecimal.ZERO);
            retour.setTotalTTC(BigDecimal.ZERO);

            ejbFacadeRetour.create(retour);
            List<LigneRetour> listLigneRetourTemps = new ArrayList<>();

            List<LigneBonLivraison> listLigneBonLivraisons;

            // en cas ou les lignes ne sont pas loader avec les factures
            if (bonLivraison.getListeLigneBonLivraisons() != null) {
                listLigneBonLivraisons = bonLivraison.getListeLigneBonLivraisons();
            } else {
                listLigneBonLivraisons = ejbFacadeLigneBonLivraison.findAllNative(" where o.BLiv_Id = " + bonLivraison.getId());
            }
            System.out.println("listLigneBonLivraisons : " + listLigneBonLivraisons.size());
            for (LigneBonLivraison ligneBonLivraison : listLigneBonLivraisons) {
                LigneRetour ligneRetour = new LigneRetour();

                ligneRetour.setCodeArticle(ligneBonLivraison.getCodeArticle());
                ligneRetour.setIdArticle(ligneBonLivraison.getIdArticle());
                ligneRetour.setLibelleArticle(ligneBonLivraison.getLibelleArticle());

                ligneRetour.setQuantite(ligneBonLivraison.getQuantite());
                ligneRetour.setQuantiteCasse(BigDecimal.ZERO);
                ligneRetour.setTotalHT(BigDecimal.ZERO);
                ligneRetour.setTotalTTC(BigDecimal.ZERO);

                ligneRetour.setPrixUnitaireHT(BigDecimal.ZERO);
                ligneRetour.setRetour(retour);

                listLigneRetourTemps.add(ligneRetour);
            }

            ejbFacadeLigneRetour.create(listLigneRetourTemps);
            ejbFacadeBonLivraison.updateAllNative("SET Ret_Id = " + retour.getId() + " where BLiv_Id = " + bonLivraison.getId());
        }

        listBonLivraisons = null;

    }

}
