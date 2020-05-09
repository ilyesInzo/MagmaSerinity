/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.TraitementDate;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "T_PlanificationVisite")
public class PlanificationVisite implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PVi_Id")
    private Long id;
    
    @Column(name = "PVi_Supprimer")
    private boolean supprimer;
    
    @Column(name = "PVi_Etat") //0:Visite planifié & 1:Visite réalisé & 2:Visite non réalisé
    private int etat;

    @Column(name = "PVi_DateDebut")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;

    @Column(name = "PVi_DateFin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    
    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Code")
    private String codeClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;
    
    @Column(name = "CCl_Id")
    private Long idCategorieClient;

    @Column(name = "CCl_Libelle")
    private String libelleCategorieClient;
    
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    
    @Column(name = "Tab_DateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "PVi_Description")
    private String description;

    @Column(name = "Com_Id")
    private Long idCommercial;

    @Column(name = "Usr_Nom")
    private String nom;

    @Column(name = "Usr_Prenom")
    private String prenom;
    
    @Transient
    private Client client;
    
    @Transient
    private CategorieClient categorieClient;
    
    @Transient
    private Commercial commercial;
    
    @OneToOne
    @JoinColumn(name = "RVst_Id", nullable = true)
    private RapportVisit rapportVisit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public String getLibelleClient() {
        return libelleClient;
    }

    public void setLibelleClient(String libelleClient) {
        this.libelleClient = libelleClient;
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

    public Long getIdCategorieClient() {
        return idCategorieClient;
    }

    public void setIdCategorieClient(Long idCategorieClient) {
        this.idCategorieClient = idCategorieClient;
    }

    public String getLibelleCategorieClient() {
        return libelleCategorieClient;
    }

    public void setLibelleCategorieClient(String libelleCategorieClient) {
        this.libelleCategorieClient = libelleCategorieClient;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CategorieClient getCategorieClient() {
        return categorieClient;
    }

    public void setCategorieClient(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
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
    
    public String getDateDebutStringFr() {
        try {
            if (dateDebut != null) {
                return TraitementDate.returnDateHeure(dateDebut);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateDebutStringEn() {
        try {
            if (dateDebut != null) {
                return TraitementDate.returnDateHeureEn(dateDebut);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateFinStringFr() {
        try {
            if (dateFin != null) {
                return TraitementDate.returnDateHeure(dateFin);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateFinStringEn() {
        try {
            if (dateFin != null) {
                return TraitementDate.returnDateHeureEn(dateFin);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getClientString() {

        try {

            return  libelleClient;

        } catch (Exception e) {
            return "---";
        }

    }

    public Commercial getCommercial() {
        return commercial;
    }

    public void setCommercial(Commercial commercial) {
        this.commercial = commercial;
    }

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public RapportVisit getRapportVisit() {
        return rapportVisit;
    }

    public void setRapportVisit(RapportVisit rapportVisit) {
        this.rapportVisit = rapportVisit;
    }

    @PrePersist
    void prepersist() {
        this.dateCreation = new Date();
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
        if (!(object instanceof PlanificationVisite)) {
            return false;
        }
        PlanificationVisite other = (PlanificationVisite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return libelleClient;
    }
    
}
