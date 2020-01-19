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
@Table(name = "T_LigneBonCommandeVente")
public class LigneBonCommandeVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Art_Id")
    private Long idArticle;

    @Column(name = "Art_Libelle")
    private String libelleArticle;

    @Column(name = "Art_Code")
    private String codeArticle;

    @Column(name = "Art_Tva", precision = 28, scale = 3)
    private BigDecimal tvaArticle;

    @Column(name = "LCom_Quantite", precision = 28, scale = 3)
    private BigDecimal quantite;

    @Column(name = "LCom_PrixUnitaireHT", scale = 3, precision = 28)
    private BigDecimal prixUnitaireHT;
    
    @Column(name = "LCom_Remise", precision = 28, scale = 3)
    private BigDecimal remise;

    @Column(name = "LCom_PrixUnitaireApresRemise", precision = 28, scale = 3)
    private BigDecimal prixUnitaireApresRemise;

    @Column(name = "LCom_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;
    
    @Column(name = "LCom_TotalTVA", scale = 3, precision = 28)
    private BigDecimal totalTVA;

    @Column(name = "LCom_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    @ManyToOne
    @JoinColumn(name = "BCVnt_Id", referencedColumnName = "BCVnt_Id", nullable = false)
    private BonCommandeVente bonCommandeVente;
    
    
    @Column(name = "LCom_QuantiteMax", precision = 28, scale = 3)
    private BigDecimal quantiteMax;
    
    @Transient
    private Article article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BonCommandeVente getBonCommandeVente() {
        return bonCommandeVente;
    }

    public void setBonCommandeVente(BonCommandeVente bonCommandeVente) {
        this.bonCommandeVente = bonCommandeVente;
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
