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
@Table(name = "T_LigneRetour")
public class LigneRetour implements Serializable {

    @Id
    @Column(name = "LRet_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "Art_Id")
    private Long idArticle;

    @Column(name = "Art_Libelle")
    private String libelleArticle;

    @Column(name = "Art_Code")
    private String codeArticle;
    
    @Column(name = "LRet_Quantite")
    private BigDecimal quantite;

    @Column(name = "LRet_QuantiteCasse")
    private BigDecimal quantiteCasse;

    @Column(name = "LRet_Remise", precision = 28, scale = 3)
    private BigDecimal remise;

    @Column(name = "LRet_PrixUnitaireApresRemise", precision = 28, scale = 3)
    private BigDecimal prixUnitaireApresRemise;

    @Column(name = "LRet_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;

    @Column(name = "LRet_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    @Column(name = "LRet_PrixUnitaireHT", scale = 3, precision = 28)
    private BigDecimal prixUnitaireHT;

    @ManyToOne
    @JoinColumn(name = "Ret_Id", referencedColumnName = "Ret_Id", nullable = true)
    private Retour retour;

    @Transient
    private Article article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantite() {
        return FonctionsMathematiques.arrondiBigDecimal(quantite, 0);
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaireHT() {
        return FonctionsMathematiques.arrondiBigDecimal(prixUnitaireHT, 3);
    }

    public void setPrixUnitaireHT(BigDecimal prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public Retour getRetour() {
        return retour;
    }

    public void setRetour(Retour retour) {
        this.retour = retour;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
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

    public BigDecimal getTotalHT() {
        return FonctionsMathematiques.arrondiBigDecimal(totalHT, 3);
    }

    public void setTotalHT(BigDecimal totalHT) {
        this.totalHT = totalHT;
    }

    public BigDecimal getTotalTTC() {
        return FonctionsMathematiques.arrondiBigDecimal(getQuantiteCasse().multiply(getPrixUnitaireHT()), 3);
    }

    public void setTotalTTC(BigDecimal totalTTC) {
        this.totalTTC = totalTTC;
    }

    public BigDecimal getQuantiteCasse() {
        return FonctionsMathematiques.arrondiBigDecimal(quantiteCasse, 0);
    }

    public void setQuantiteCasse(BigDecimal quantiteCasse) {
        this.quantiteCasse = quantiteCasse;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LigneRetour)) {
            return false;
        }
        LigneRetour other = (LigneRetour) object;
        /*if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;*/
        
        return other.getIdArticle() != null && this.idArticle!= null && this.idArticle.equals(other.getIdArticle());
    }

    @Override
    public String toString() {
        return "com.magma.entity.LigneRetour[ id=" + id + " ]";
    }
    
}
