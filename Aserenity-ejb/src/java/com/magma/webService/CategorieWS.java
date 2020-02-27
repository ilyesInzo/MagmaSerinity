/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.webService;

/**
 *
 * @author inzo
 */
public class CategorieWS {
    
    private Long id;
    
    private String libelle;

    private Long dateSynch;
    
    private int rang;
    
    private Long idParent;
    
    private boolean supprimer;
    
    private String photo;

    private boolean dernierRang;

    private boolean activer;
    
    private Long idPremierParent;
    
    private String libelleSuiteParent;

    private String idSuiteParent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public Long getIdParent() {
        return idParent;
    }

    public void setIdParent(Long idParent) {
        this.idParent = idParent;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isDernierRang() {
        return dernierRang;
    }

    public void setDernierRang(boolean dernierRang) {
        this.dernierRang = dernierRang;
    }

    public boolean isActiver() {
        return activer;
    }

    public void setActiver(boolean activer) {
        this.activer = activer;
    }

    public Long getIdPremierParent() {
        return idPremierParent;
    }

    public void setIdPremierParent(Long idPremierParent) {
        this.idPremierParent = idPremierParent;
    }

    public String getLibelleSuiteParent() {
        return libelleSuiteParent;
    }

    public void setLibelleSuiteParent(String libelleSuiteParent) {
        this.libelleSuiteParent = libelleSuiteParent;
    }

    public String getIdSuiteParent() {
        return idSuiteParent;
    }

    public void setIdSuiteParent(String idSuiteParent) {
        this.idSuiteParent = idSuiteParent;
    }
    
    
    
    
}
