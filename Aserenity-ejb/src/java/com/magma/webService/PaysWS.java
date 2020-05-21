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
public class PaysWS {
    
    private Long id;

    private String libelle;

    private String description;

    private long dateSynch;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(long dateSynch) {
        this.dateSynch = dateSynch;
    }


    
    
}
