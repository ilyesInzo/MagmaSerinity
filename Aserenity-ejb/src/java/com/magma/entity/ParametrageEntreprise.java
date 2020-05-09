/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_ParametrageEntreprise")
public class ParametrageEntreprise implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PEnt_Id")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "Ent_Id", referencedColumnName = "Ent_Id")
    private Entreprise entreprise;
    
    @Column(name = "PEnt_VisibiliteMCommande")
    private boolean visibiliteMCommande;
            
    @Column(name = "PEnt_VisibiliteMVentes")
    private boolean visibiliteMVentes;
    
    @Column(name = "PEnt_VisibiliteMStockArticle")
    private boolean visibiliteMStockArticle;

    @Column(name = "PEnt_VisibiliteMClient")
    private boolean visibiliteMClient;

    @Column(name = "PEnt_VisibiliteMProduit")
    private boolean visibiliteMProduit;
    
    @Column(name = "PEnt_VisibiliteMCommercial")
    private boolean visibiliteMCommercial;   
            
    @Column(name = "PEnt_VisibiliteMParametrage")
    private boolean visibiliteMParametrage;
    
    @Column(name = "PEnt_VisibiliteMJourneaux")
    private boolean visibiliteMJourneaux;
    
    @Column(name = "PEnt_VisibiliteMVeille")
    private boolean visibiliteMVeille;
    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public boolean isVisibiliteMCommande() {
        return visibiliteMCommande;
    }

    public void setVisibiliteMCommande(boolean visibiliteMCommande) {
        this.visibiliteMCommande = visibiliteMCommande;
    }

    public boolean isVisibiliteMVentes() {
        return visibiliteMVentes;
    }

    public void setVisibiliteMVentes(boolean visibiliteMVentes) {
        this.visibiliteMVentes = visibiliteMVentes;
    }

    public boolean isVisibiliteMStockArticle() {
        return visibiliteMStockArticle;
    }

    public void setVisibiliteMStockArticle(boolean visibiliteMStockArticle) {
        this.visibiliteMStockArticle = visibiliteMStockArticle;
    }

    public boolean isVisibiliteMClient() {
        return visibiliteMClient;
    }

    public void setVisibiliteMClient(boolean visibiliteMClient) {
        this.visibiliteMClient = visibiliteMClient;
    }

    public boolean isVisibiliteMProduit() {
        return visibiliteMProduit;
    }

    public void setVisibiliteMProduit(boolean visibiliteMProduit) {
        this.visibiliteMProduit = visibiliteMProduit;
    }

    public boolean isVisibiliteMCommercial() {
        return visibiliteMCommercial;
    }

    public void setVisibiliteMCommercial(boolean visibiliteMCommercial) {
        this.visibiliteMCommercial = visibiliteMCommercial;
    }

    public boolean isVisibiliteMParametrage() {
        return visibiliteMParametrage;
    }

    public void setVisibiliteMParametrage(boolean visibiliteMParametrage) {
        this.visibiliteMParametrage = visibiliteMParametrage;
    }

    public boolean isVisibiliteMJourneaux() {
        return visibiliteMJourneaux;
    }

    public void setVisibiliteMJourneaux(boolean visibiliteMJourneaux) {
        this.visibiliteMJourneaux = visibiliteMJourneaux;
    }

    public boolean isVisibiliteMVeille() {
        return visibiliteMVeille;
    }

    public void setVisibiliteMVeille(boolean visibiliteMVeille) {
        this.visibiliteMVeille = visibiliteMVeille;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametrageEntreprise)) {
            return false;
        }
        ParametrageEntreprise other = (ParametrageEntreprise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "";
    }
    
}
