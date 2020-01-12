/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.FonctionsMathematiques;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_TaxesFacture")
public class TaxesFacture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TFr_Id")
    private Long id;

    @Column(name = "TFr_Valeur", precision = 28, scale = 3)
    private BigDecimal valeur;

    @Column(name = "TFr_Operation")
    private String operation;

    @Column(name = "TFr_TypeTaxe")
    private String typeTaxe;

    @Column(name = "TFr_ApresTva")
    private boolean apresTva;

    @Column(name = "TFr_Montant", precision = 28, scale = 3)
    private BigDecimal montant;


    @ManyToOne
    @JoinColumn(name = "Fct_Id", referencedColumnName = "Fct_Id", nullable = false)
    private Facture facture;

    @ManyToOne
    @JoinColumn(name = "PTa_Id", referencedColumnName = "PTa_Id", nullable = false)
    private ParametrageTaxe parametrageTaxe;
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTypeTaxe() {
        return typeTaxe;
    }

    public void setTypeTaxe(String typeTaxe) {
        this.typeTaxe = typeTaxe;
    }

    public String getTypeTaxeString() {

        if (typeTaxe.equals("0")) {
            return "Pourcentage";
        } else if (typeTaxe.equals("1")) {
            return "Fixe";
        } else {
            return "---";
        }

    }

    public boolean isApresTva() {
        return apresTva;
    }

    public void setApresTva(boolean apresTva) {
        this.apresTva = apresTva;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    public ParametrageTaxe getParametrageTaxe() {
        return parametrageTaxe;
    }

    public void setParametrageTaxe(ParametrageTaxe parametrageTaxe) {
        this.parametrageTaxe = parametrageTaxe;
    }

    public BigDecimal getMontant() {
        return FonctionsMathematiques.arrondiBigDecimal(montant, 3);
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }
        @PrePersist
    void prepersist() {
        this.dateSynch = System.currentTimeMillis();
    }
        @PreUpdate
    void preupdate() {
        this.dateSynch = System.currentTimeMillis();

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
        if (!(object instanceof TaxesFacture)) {
            return false;
        }
        TaxesFacture other = (TaxesFacture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.symatique.smartSales.entity.TaxesFacture[ id=" + id + " ]";
    }

}
