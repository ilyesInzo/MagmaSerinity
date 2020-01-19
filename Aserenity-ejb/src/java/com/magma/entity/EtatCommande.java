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
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_EtatCommande")
public class EtatCommande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ECm_Id")
    private Long id;

    @Column(name = "ECm_Libelle")
    private String libelle;

    @Column(name = "ECm_Supprimer")
    private boolean supprimer;

    @Column(name = "ECm_LibelleArb")
    private String libelleArb;

    @Column(name = "ECm_Description")
    private String description;

    @Column(name = "ECm_Rang")
    private int rang;

    @Column(name = "ECm_DernierRang")
    private boolean dernierRang;

    @Column(name = "ECm_Acceptation")
    private boolean acceptation;

    @Column(name = "ECm_LibelleParent")
    private String libelleParent;

    @Column(name = "ECm_LibelleSuiteParent")
    private String libelleSuiteParent;

    @Column(name = "ECm_Couleur")
    private String couleur;

    @Column(name = "ECm_IdSuiteParent")
    private String idSuiteParent;

    @Column(name = "ECm_IdPremierParent")
    private Long idPremierParent;
/*
    @OneToMany(mappedBy = "etatCommande", fetch = FetchType.EAGER)
    private List<BonCommandeCommercial> listBonCommandeCommercial;
*/
    @ManyToOne
    @JoinColumn(name = "ECm_IdParent")
    private EtatCommande parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<EtatCommande> listEtatCommandesFils;

    @Column(name = "Pst_Id")
    private Long idPoste;

    @Column(name = "Pst_Libelle")
    private String libellePoste;

    @Column(name = "Dep_Id")
    private Long idDepartement;

    @Column(name = "Dep_Libelle")
    private String libelleDepartement;

    @Column(name = "Tab_dateSynch")
    private Long dateSynch;

    @Column(name = "Tab_DateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(name = "Tab_IdUserCreate")
    private Long idUserCreate;

    @Column(name = "Tab_LibelleUserCreate")
    private String libelleUserCreate;

    @Column(name = "Tab_IdUserModif")
    private Long idUserModif;

    @Column(name = "Tab_LibelleUserModif")
    private String libelleUserModif;

    @Transient
    private Poste poste;

    @Transient
    private Departement departement;

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public String getLibelleArb() {
        return libelleArb;
    }

    public void setLibelleArb(String libelleArb) {
        this.libelleArb = libelleArb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public boolean isDernierRang() {
        return dernierRang;
    }

    public void setDernierRang(boolean dernierRang) {
        this.dernierRang = dernierRang;
    }

    public String getLibelleParent() {
        return libelleParent;
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

    public EtatCommande getParent() {
        return parent;
    }

    public void setParent(EtatCommande parent) {
        this.parent = parent;
    }

    public List<EtatCommande> getListEtatCommandesFils() {
        return listEtatCommandesFils;
    }

    public void setListEtatCommandesFils(List<EtatCommande> listEtatCommandesFils) {
        this.listEtatCommandesFils = listEtatCommandesFils;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLibelleSuiteParentString() {
        if (rang == 0) {
            return libelle;
        }
        return libelleSuiteParent + " > " + libelle;
    }

    public String getPosteDepartement() {

        if (libelleDepartement != null) {

            if (libellePoste != null) {

                return libellePoste + " - " + libelleDepartement;

            } else {
                return libelleDepartement;
            }

        } else {
            return "---";
        }

    }

    public String getCategorieRootParent() {
        if (getParent() != null) {
            return getParent().getCategorieRootParent();
        } else {
            return libelle;
        }
    }
/*
    public List<BonCommandeCommercial> getListBonCommandeCommercial() {
        return listBonCommandeCommercial;
    }

    public void setListBonCommandeCommercial(List<BonCommandeCommercial> listBonCommandeCommercial) {
        this.listBonCommandeCommercial = listBonCommandeCommercial;
    }*/

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public boolean isAcceptation() {
        return acceptation;
    }

    public void setAcceptation(boolean acceptation) {
        this.acceptation = acceptation;
    }

    public String getIdSuiteParent() {
        return idSuiteParent;
    }

    public void setIdSuiteParent(String idSuiteParent) {
        this.idSuiteParent = idSuiteParent;
    }

    public Long getIdPremierParent() {
        return idPremierParent;
    }

    public void setIdPremierParent(Long idPremierParent) {
        this.idPremierParent = idPremierParent;
    }

    public Long getIdPoste() {
        return idPoste;
    }

    public void setIdPoste(Long idPoste) {
        this.idPoste = idPoste;
    }

    public String getLibellePoste() {
        return libellePoste;
    }

    public void setLibellePoste(String libellePoste) {
        this.libellePoste = libellePoste;
    }

    public Long getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(Long idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getLibelleDepartement() {
        return libelleDepartement;
    }

    public void setLibelleDepartement(String libelleDepartement) {
        this.libelleDepartement = libelleDepartement;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

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
        if (!(object instanceof EtatCommande)) {
            return false;
        }
        EtatCommande other = (EtatCommande) object;
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
