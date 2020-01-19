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
@Table(name = "T_TaxesBonCommandeVente")
public class TaxesBonCommandeVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TBCVnt_Id")
    private Long id;

    @Column(name = "TBCVnt_Valeur", precision = 28, scale = 3)
    private BigDecimal valeur;

    @Column(name = "TBCVnt_Operation")
    private String operation;

    @Column(name = "TBCVnt_TypeTaxe")
    private String typeTaxe;

    @Column(name = "TBCVnt_ApresTva")
    private boolean apresTva;

    @Column(name = "TBCVnt_Montant", precision = 28, scale = 3)
    private BigDecimal montant;

    @ManyToOne
    @JoinColumn(name = "BCVnt_Id", referencedColumnName = "BCVnt_Id", nullable = false)
    private BonCommandeVente bonCommandeVente;

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

    public boolean isApresTva() {
        return apresTva;
    }

    public void setApresTva(boolean apresTva) {
        this.apresTva = apresTva;
    }

    public BigDecimal getMontant() {
        return FonctionsMathematiques.arrondiBigDecimal(montant, 3);
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BonCommandeVente getBonCommandeVente() {
        return bonCommandeVente;
    }

    public void setBonCommandeVente(BonCommandeVente bonCommandeVente) {
        this.bonCommandeVente = bonCommandeVente;
    }

    public ParametrageTaxe getParametrageTaxe() {
        return parametrageTaxe;
    }

    public void setParametrageTaxe(ParametrageTaxe parametrageTaxe) {
        this.parametrageTaxe = parametrageTaxe;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
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
        if (!(object instanceof TaxesBonCommandeVente)) {
            return false;
        }
        TaxesBonCommandeVente other = (TaxesBonCommandeVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.magma.entity.TaxesBonCommandeVente[ id=" + id + " ]";
    }

}
