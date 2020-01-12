/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.FonctionsString;
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
import javax.persistence.Transient;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_Poste")
public class Poste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Pst_Id")
    private Long id;

    @Column(name = "Pst_Libelle")
    private String libelle;

    @Column(name = "Pst_Description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "Dep_Id", referencedColumnName = "Dep_Id", nullable = false)
    private Departement departement;

    @Column(name = "Pst_Rang")
    private int rang;

    @Column(name = "Pst_LibelleParent")
    private String libelleParent;

    @Column(name = "Pst_LibelleSuiteParent")
    private String libelleSuiteParent;

    @Column(name = "Pst_IdSuiteParent")
    private String idSuiteParent;

    @Column(name = "Pst_DernierRang")
    private boolean dernierRang;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Poste> listPostes;

    @ManyToOne
    @JoinColumn(name = "Pst_IdParent")
    private Poste parent;

    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    
    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    
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
        try {
            this.libelle = FonctionsString.PremiereLetteMajuscule(libelle);
        } catch (Exception e) {
            this.libelle = libelle;
        }
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionString() {
        if (description != null && !description.equals("")) {
            return description;
        } else {
            return "---";
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
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

    public String getLibelleParentString() {
        if (parent != null) {
            return parent.getLibelle();
        } else if (libelleParent != null && !libelleParent.equals("")) {
            return libelleParent;
        } else {
            return "---";
        }
    }

    public void setLibelleParent(String libelleParent) {
        this.libelleParent = libelleParent;
    }

    public String getLibelleSuiteParent() {
        return libelleSuiteParent;
    }

    public void setLibelleSuiteParent(String libelleSuiteParent) {
        this.libelleSuiteParent = libelleSuiteParent;
    }

    public boolean isDernierRang() {
        return dernierRang;
    }

    public void setDernierRang(boolean dernierRang) {
        this.dernierRang = dernierRang;
    }

    public List<Poste> getListPostes() {
        return listPostes;
    }

    public void setListPostes(List<Poste> listPostes) {
        this.listPostes = listPostes;
    }

    public Poste getParent() {
        return parent;
    }

    public void setParent(Poste parent) {
        this.parent = parent;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
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

    public String getLibelleSuiteParentString() {
        if (rang == 0) {
            return libelle;
        }
        return libelleSuiteParent + " > " + libelle;
    }

    public String getPosteRootParent() {
        if (getParent() != null) {
            return getParent().getPosteRootParent();
        } else {
            return libelle;
        }
    }

    public String getIdSuiteParent() {
        return idSuiteParent;
    }

    public void setIdSuiteParent(String idSuiteParent) {
        this.idSuiteParent = idSuiteParent;
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
        if (!(object instanceof Poste)) {
            return false;
        }
        Poste other = (Poste) object;
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
