/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.TraitementDate;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_Categorie")
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Cat_Id")
    private Long id;

    @Column(name = "Cat_Libelle")
    private String libelle;

    @Column(name = "Cat_Description")
    private String description;

    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(name = "Cat_Rang")
    private int rang;

    @Column(name = "Cat_LibelleParent")
    private String libelleParent;

    @Column(name = "Cat_DernierRang")
    private boolean dernierRang;

    @Column(name = "Cat_Supprimer")
    private boolean supprimer;

    @Column(name = "Cat_LibelleSuiteParent")
    private String libelleSuiteParent;

    @Column(name = "Cat_IdSuiteParent")
    private String idSuiteParent;

    @Column(name = "Cat_Activer")
    private boolean activer;

    @OneToMany(mappedBy = "categorie", fetch = FetchType.EAGER)
    private List<Article> articles;

    @Column(name = "Cat_IdPremierParent")
    private Long idPremierParent;

    @ManyToOne
    @JoinColumn(name = "Cat_IdParent")
    private Categorie parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Categorie> listCategorieFils;
    
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    
    @Column(name = "Tab_IdUserCreate")
    private Long idUserCreate;

    @Column(name = "Tab_LibelleUserCreate")
    private String libelleUserCreate;
    
    @Column(name = "Tab_IdUserModif")
    private Long idUserModif;

    @Column(name = "Tab_LibelleUserModif")
    private String libelleUserModif;

    public Long getIdUserCreate() {
        return idUserCreate;
    }

    public void setIdUserCreate(Long idUserCreate) {
        this.idUserCreate = idUserCreate;
    }

    public String getLibelleUserCreate() {
        return libelleUserCreate;
    }

    public void setLibelleUserCreate(String libelleUserCreate) {
        this.libelleUserCreate = libelleUserCreate;
    }

    public Long getIdUserModif() {
        return idUserModif;
    }

    public void setIdUserModif(Long idUserModif) {
        this.idUserModif = idUserModif;
    }

    public String getLibelleUserModif() {
        return libelleUserModif;
    }

    public void setLibelleUserModif(String libelleUserModif) {
        this.libelleUserModif = libelleUserModif;
    }
    
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

    public String getDescriptionString() {
        if (description != null && !description.equals("")) {
            return description;
        } else {
            return "---";
        }
    }

    public String getDateCreationStringFr() {
        try {
            if (dateCreation != null) {
                return TraitementDate.returnDateHeure(dateCreation);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateCreationStringEn() {
        try {
            if (dateCreation != null) {
                return TraitementDate.returnDateHeureEn(dateCreation);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }
    
    public String getDateSyncStringFr() {
        try {
            if (dateSynch != null) {
                return TraitementDate.returnDateHeure(dateSynch);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateSyncStringEn() {
        try {
            if (dateSynch != null) {
                return TraitementDate.returnDateHeureEn(dateSynch);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public String getLibelleParent() {
        return libelleParent;
    }

    public void setLibelleParent(String libelleParent) {
        this.libelleParent = libelleParent;
    }

    public boolean isDernierRang() {
        return dernierRang;
    }

    public void setDernierRang(boolean dernierRang) {
        this.dernierRang = dernierRang;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public String getLibelleSuiteParent() {
        return libelleSuiteParent;
    }

    public void setLibelleSuiteParent(String libelleSuiteParent) {
        this.libelleSuiteParent = libelleSuiteParent;
    }

    public String getIdSuiteParent() {
        return idSuiteParent;
    }

    public void setIdSuiteParent(String idSuiteParent) {
        this.idSuiteParent = idSuiteParent;
    }

    public boolean isActiver() {
        return activer;
    }

    public void setActiver(boolean activer) {
        this.activer = activer;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Long getIdPremierParent() {
        return idPremierParent;
    }

    public void setIdPremierParent(Long idPremierParent) {
        this.idPremierParent = idPremierParent;
    }

    public Categorie getParent() {
        return parent;
    }

    public void setParent(Categorie parent) {
        this.parent = parent;
    }

    public List<Categorie> getListCategorieFils() {
        return listCategorieFils;
    }

    public void setListCategorieFils(List<Categorie> listCategorieFils) {
        this.listCategorieFils = listCategorieFils;
    }

    public String getLibelleSuiteParentString() {
        if (rang == 0) {
            return libelle;
        }
        return libelleSuiteParent + " > " + libelle;
    }

    public String getCategorieRootParent() {
        if (getParent() != null) {
            return getParent().getCategorieRootParent();
        } else {
            return libelle;
        }
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }
        @PreUpdate
    void preupdate() {
        this.dateSynch = System.currentTimeMillis();

    }
    @PrePersist
    void prepersist() {
        this.dateCreation = new Date();
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
        if (!(object instanceof Categorie)) {
            return false;
        }
        Categorie other = (Categorie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getLibelleSuiteParentString();
    }

}
