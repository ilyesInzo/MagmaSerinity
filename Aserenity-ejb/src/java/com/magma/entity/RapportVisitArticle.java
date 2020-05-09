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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_RapportVisitArticle")
public class RapportVisitArticle implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RVArt_Id")
    private Long id;
    
    @Column(name = "RVArt_Note")
    private int note;
    
    @Column(name = "RVArt_Commentaire")
    private String commentaire;
    
    @Column(name = "Art_Id")
    private Long idArticle;

    @Column(name = "Art_Code")
    private String codeArticle;

    @Column(name = "Art_Libelle")
    private String libelleArticle;
    
    @ManyToOne
    @JoinColumn(name = "RVst_Id", referencedColumnName = "RVst_Id", nullable = false)
    private RapportVisit rapportVisit;

    public RapportVisit getRapportVisit() {
        return rapportVisit;
    }

    public void setRapportVisit(RapportVisit rapportVisit) {
        this.rapportVisit = rapportVisit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }


    
    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Long idArticle) {
        this.idArticle = idArticle;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getLibelleArticle() {
        return libelleArticle;
    }

    public void setLibelleArticle(String libelleArticle) {
        this.libelleArticle = libelleArticle;
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
        if (!(object instanceof RapportVisitArticle)) {
            return false;
        }
        RapportVisitArticle other = (RapportVisitArticle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.magma.entity.RapportVisitArticle[ id=" + id + " ]";
    } 
    
    
}
