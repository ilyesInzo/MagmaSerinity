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
public class ClientWS {
    
    private Long id;
    
    private String libelle;
    
    private String gsm;

    private String email;
    
    private Long idGouvernorat;

    private String libelleGouvernorat;

    private String adresse;

    private Long idDelegation;

    private String libelleDelegation;

    private Long idCommercial;
    private String nomCommercial;
    private String prenomCommercial;
    private int typeCommercial;
    
    private Long dateSynch;
    
    private String longitude;
    
    private String latitude;
    
    private int precision;
    
    private Long idCategorieClient;
    
    private String libelleCategorieClient;
    
    // 0:En cours  1:validée  2:annulée
    private int etatProspection;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getIdGouvernorat() {
        return idGouvernorat;
    }

    public void setIdGouvernorat(Long idGouvernorat) {
        this.idGouvernorat = idGouvernorat;
    }

    public String getLibelleGouvernorat() {
        return libelleGouvernorat;
    }

    public void setLibelleGouvernorat(String libelleGouvernorat) {
        this.libelleGouvernorat = libelleGouvernorat;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Long getIdDelegation() {
        return idDelegation;
    }

    public void setIdDelegation(Long idDelegation) {
        this.idDelegation = idDelegation;
    }

    public String getLibelleDelegation() {
        return libelleDelegation;
    }

    public void setLibelleDelegation(String libelleDelegation) {
        this.libelleDelegation = libelleDelegation;
    }

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
    }

    public int getTypeCommercial() {
        return typeCommercial;
    }

    public void setTypeCommercial(int typeCommercial) {
        this.typeCommercial = typeCommercial;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public Long getIdCategorieClient() {
        return idCategorieClient;
    }

    public void setIdCategorieClient(Long idCategorieClient) {
        this.idCategorieClient = idCategorieClient;
    }

    public String getLibelleCategorieClient() {
        return libelleCategorieClient;
    }

    public void setLibelleCategorieClient(String libelleCategorieClient) {
        this.libelleCategorieClient = libelleCategorieClient;
    }

    public int getEtatProspection() {
        return etatProspection;
    }

    public void setEtatProspection(int etatProspection) {
        this.etatProspection = etatProspection;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public void setNomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
    }

    public String getPrenomCommercial() {
        return prenomCommercial;
    }

    public void setPrenomCommercial(String prenomCommercial) {
        this.prenomCommercial = prenomCommercial;
    }
    
    
    
}
