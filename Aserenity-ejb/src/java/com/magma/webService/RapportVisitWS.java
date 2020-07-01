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
public class RapportVisitWS {
    
    private Long id;
    
    private Long idClient;

    private String libelleClient;
    
    private Long idCommercial;
    
    private String nomCommercial;

    private String prenomCommercial;
    
    private Long idPlanificationVisite;
    
    private Long dateVisite;
    
    private List<RapportVisitArticleWS> listRapportVisitArticles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getLibelleClient() {
        return libelleClient;
    }

    public void setLibelleClient(String libelleClient) {
        this.libelleClient = libelleClient;
    }

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
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

    public Long getIdPlanificationVisite() {
        return idPlanificationVisite;
    }

    public void setIdPlanificationVisite(Long idPlanificationVisite) {
        this.idPlanificationVisite = idPlanificationVisite;
    }

    public List<RapportVisitArticleWS> getListRapportVisitArticles() {
        return listRapportVisitArticles;
    }

    public void setListRapportVisitArticles(List<RapportVisitArticleWS> listRapportVisitArticles) {
        this.listRapportVisitArticles = listRapportVisitArticles;
    }

    public Long getDateVisite() {
        return dateVisite;
    }

    public void setDateVisite(Long dateVisite) {
        this.dateVisite = dateVisite;
    }
    
    
    
}
