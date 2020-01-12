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
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_LigneDevis")
public class LigneDevis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LDev_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Art_Id")
    private Long idArticle;

    @Column(name = "Art_Libelle")
    private String libelleArticle;

    @Column(name = "Art_Code")
    private String codeArticle;

    @Column(name = "Art_Tva", precision = 28, scale = 3)
    private BigDecimal tvaArticle;

    @Column(name = "LDev_Quantite", precision = 28, scale = 3)
    private BigDecimal quantite;

    @Column(name = "LDev_PrixUnitaireHT", scale = 3, precision = 28)
    private BigDecimal prixUnitaireHT;
    
    @Column(name = "LDev_Remise", precision = 28, scale = 3)
    private BigDecimal remise;

    @Column(name = "LDev_PrixUnitaireApresRemise", precision = 28, scale = 3)
    private BigDecimal prixUnitaireApresRemise;

    @Column(name = "LDev_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;
    
    @Column(name = "LDev_TotalTVA", scale = 3, precision = 28)
    private BigDecimal totalTVA;

    @Column(name = "LDev_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    @ManyToOne
    @JoinColumn(name = "Dev_Id", referencedColumnName = "Dev_Id", nullable = false)
    private Devis devis;
    
    @Column(name = "LDev_QuantiteMax", precision = 28, scale = 3)
    private BigDecimal quantiteMax;
    
    @Transient
    private Article article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Long idArticle) {
        this.idArticle = idArticle;
    }

    public String getLibelleArticle() {
        return libelleArticle;
    }

    public void setLibelleArticle(String libelleArticle) {
        this.libelleArticle = libelleArticle;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    

    public BigDecimal getPrixUnitaireHT() {
        return FonctionsMathematiques.arrondiBigDecimal(prixUnitaireHT, 3);
    }

    public void setPrixUnitaireHT(BigDecimal prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public Devis getDevis() {
        return devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public BigDecimal getTotalHT() {
   
        return FonctionsMathematiques.arrondiBigDecimal(totalHT, 3);
    }

    public void setTotalHT(BigDecimal totalHT) {
        this.totalHT = totalHT;
    }

    public BigDecimal getTotalTVA() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTVA, 3);
    }

    public void setTotalTVA(BigDecimal totalTVA) {
        this.totalTVA = totalTVA;
    }

    public BigDecimal getTotalTTC() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTTC, 3);
    }

    public void setTotalTTC(BigDecimal totalTTC) {
        this.totalTTC = totalTTC;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public BigDecimal getTvaArticle() {
       return FonctionsMathematiques.arrondiBigDecimal(tvaArticle, 2);
    }

    public void setTvaArticle(BigDecimal tvaArticle) {
        this.tvaArticle = tvaArticle;
    }

    public BigDecimal getQuantite() {
        return FonctionsMathematiques.arrondiBigDecimal(quantite,0);
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getPrixUnitaireApresRemise() {
        return FonctionsMathematiques.arrondiBigDecimal(prixUnitaireApresRemise, 3);
    }

    public void setPrixUnitaireApresRemise(BigDecimal prixUnitaireApresRemise) {
        this.prixUnitaireApresRemise = prixUnitaireApresRemise;
    }
    
    public BigDecimal getQuantiteMax() {
        return quantiteMax;
    }

    public void setQuantiteMax(BigDecimal quantiteMax) {
        this.quantiteMax = quantiteMax;
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
        if (!(object instanceof LigneDevis)) {
            return false;
        }
        LigneDevis other = (LigneDevis) object;
        /*if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;*/
        
        return other.getIdArticle() != null && this.idArticle!= null && this.idArticle.equals(other.getIdArticle());
    }

    @Override
    public String toString() {
        return libelleArticle + "--" + prixUnitaireHT;
    }
    
}
