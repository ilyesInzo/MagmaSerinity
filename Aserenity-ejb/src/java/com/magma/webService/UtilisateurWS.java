/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.webService;

import java.util.List;

/**
 *
 * @author inzo
 */
public class UtilisateurWS {
    
    private Long id;
    private String login;
    private String passwd;
    private String code;
    private String statut;
    private String nom;
    private String prenom;
    private String gsm;
    private String email;
    private int type; //0:client, 1:commercial, 2:consomateur final;
    private int etatCompte; //0: ok, 1: bloquer, 2:mot de passe erronée, 3: login ou pwd non conforme, 4: erreur systeme, 5: entreprise desactiver

    private Long idEntreprise;
    private String LibelleEntreprise;
    
    private String sequenceClientID;

    private boolean moduleClient;

    private boolean moduleArticles;

    private boolean moduleProduction;

    private boolean moduleSuggestions;

    private boolean moduleVeilles;

    private boolean moduleCommerciale;

    private boolean moduleReclamations;

    private boolean moduleActualites;

    private boolean moduleCommande;

    private boolean moduleVente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getEtatCompte() {
        return etatCompte;
    }

    public void setEtatCompte(int etatCompte) {
        this.etatCompte = etatCompte;
    }

    public Long getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(Long idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public String getLibelleEntreprise() {
        return LibelleEntreprise;
    }

    public void setLibelleEntreprise(String LibelleEntreprise) {
        this.LibelleEntreprise = LibelleEntreprise;
    }

    public boolean isModuleClient() {
        return moduleClient;
    }

    public void setModuleClient(boolean moduleClient) {
        this.moduleClient = moduleClient;
    }

    public boolean isModuleArticles() {
        return moduleArticles;
    }

    public void setModuleArticles(boolean moduleArticles) {
        this.moduleArticles = moduleArticles;
    }

    public boolean isModuleProduction() {
        return moduleProduction;
    }

    public void setModuleProduction(boolean moduleProduction) {
        this.moduleProduction = moduleProduction;
    }

    public boolean isModuleSuggestions() {
        return moduleSuggestions;
    }

    public void setModuleSuggestions(boolean moduleSuggestions) {
        this.moduleSuggestions = moduleSuggestions;
    }

    public boolean isModuleVeilles() {
        return moduleVeilles;
    }

    public void setModuleVeilles(boolean moduleVeilles) {
        this.moduleVeilles = moduleVeilles;
    }

    public boolean isModuleCommerciale() {
        return moduleCommerciale;
    }

    public void setModuleCommerciale(boolean moduleCommerciale) {
        this.moduleCommerciale = moduleCommerciale;
    }

    public boolean isModuleReclamations() {
        return moduleReclamations;
    }

    public void setModuleReclamations(boolean moduleReclamations) {
        this.moduleReclamations = moduleReclamations;
    }

    public boolean isModuleActualites() {
        return moduleActualites;
    }

    public void setModuleActualites(boolean moduleActualites) {
        this.moduleActualites = moduleActualites;
    }

    public boolean isModuleCommande() {
        return moduleCommande;
    }

    public void setModuleCommande(boolean moduleCommande) {
        this.moduleCommande = moduleCommande;
    }

    public boolean isModuleVente() {
        return moduleVente;
    }

    public void setModuleVente(boolean moduleVente) {
        this.moduleVente = moduleVente;
    }

    public String getSequenceClientID() {
        return sequenceClientID;
    }

    public void setSequenceClientID(String sequenceClientID) {
        this.sequenceClientID = sequenceClientID;
    }




    
}
