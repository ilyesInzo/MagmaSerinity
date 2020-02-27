/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.webService;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author inzo
 */
public class ArticleWS {
    
    private Long id;
    private String code;
    private String libelle;
    private String description;
    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private String photo5;
    
    private BigDecimal prixRevendeur;
    
    private String typeMesure;
    
    private BigDecimal tva;
    
    private boolean catalogue;
    
    private boolean supprimer;
    
    private Long dateSynch;
    
    private Long idCategorie;
    
    private String libelleCategorie;

    private Long idPremierParent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }

    public BigDecimal getPrixRevendeur() {
        return prixRevendeur;
    }

    public void setPrixRevendeur(BigDecimal prixRevendeur) {
        this.prixRevendeur = prixRevendeur;
    }

    public String getTypeMesure() {
        return typeMesure;
    }

    public void setTypeMesure(String typeMesure) {
        this.typeMesure = typeMesure;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public boolean isCatalogue() {
        return catalogue;
    }

    public void setCatalogue(boolean catalogue) {
        this.catalogue = catalogue;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public Long getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(Long idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getLibelleCategorie() {
        return libelleCategorie;
    }

    public void setLibelleCategorie(String libelleCategorie) {
        this.libelleCategorie = libelleCategorie;
    }

    public Long getIdPremierParent() {
        return idPremierParent;
    }

    public void setIdPremierParent(Long idPremierParent) {
        this.idPremierParent = idPremierParent;
    }
    
    
    
    
}
