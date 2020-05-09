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
@Table(name = "T_Prospection")
public class Prospection implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "Prp_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "Prp_Libelle")
    private String libelle;

    @Column(name = "Prp_Description")
    private String description;
    
    @Column(name = "Prp_Adresse")
    private String adresse;

    @Column(name = "Prp_CodePostale")
    private String codePostale;
    
    @Column(name = "Prp_Longitude")
    private String longitude;

    @Column(name = "Prp_Latitude")
    private String latitude;
    
    @Column(name = "Gov_Id")
    private Long idGouvernorat;

    @Column(name = "Gov_Libelle")
    private String libelleGouvernorat;
    
    @Column(name = "Del_Id")
    private Long idDelegation;

    @Column(name = "Del_Libelle")
    private String libelleDelegation;
    
    @Column(name = "Prp_DateSynch")
    private Long dateSynch;
    
    @Column(name = "Tab_DateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "Com_id")
    private Long idCommercial;
    
    @Column(name = "Com_Nom")
    private String nomCommercial;

    @Column(name = "Com_Prenom")
    private String prenomCommercial;
    
    @Column(name = "Com_Type")
    private int type;
    // 0:En cours  1:validée  2:annulée
    @Column(name = "Prp_EtatProspection")
    private int etatProspection;
    
    @Column(name = "Prp_Gsm")
    private String gsm;
    
    @Column(name = "CCli_Id")
    private Long idCategorieClient;

    @Column(name = "CCli_Libelle")
    private String libelleCategorieClient;
    
    @Transient
    private Gouvernorat gouvernorat;
        
    @Transient
    private Delegation delegation;

    @Transient
    private Commercial commercial;
    
    @Transient
    private Client client;

    @Transient
    private CategorieClient categorieClient;
    
    @PreUpdate
    void preupdate() {
        this.dateSynch = System.currentTimeMillis();
    }

    @PrePersist
    void prepersist() {
        this.dateCreation = new Date();
        this.dateSynch = System.currentTimeMillis();
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Long getIdGouvernorat() {
        return idGouvernorat;
    }

    public void setIdGouvernorat(Long idGouvernorat) {
        this.idGouvernorat = idGouvernorat;
    }

    public String getLibelleGouvernorat() {
        return libelleGouvernorat;
    }

    public void setLibelleGouvernorat(String libelleGouvernorat) {
        this.libelleGouvernorat = libelleGouvernorat;
    }

    public Long getIdDelegation() {
        return idDelegation;
    }

    public void setIdDelegation(Long idDelegation) {
        this.idDelegation = idDelegation;
    }

    public String getLibelleDelegation() {
        return libelleDelegation;
    }

    public void setLibelleDelegation(String libelleDelegation) {
        this.libelleDelegation = libelleDelegation;
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

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Gouvernorat getGouvernorat() {
        return gouvernorat;
    }

    public void setGouvernorat(Gouvernorat gouvernorat) {
        this.gouvernorat = gouvernorat;
    }

    public Delegation getDelegation() {
        return delegation;
    }

    public void setDelegation(Delegation delegation) {
        this.delegation = delegation;
    }

    public Commercial getCommercial() {
        return commercial;
    }

    public void setCommercial(Commercial commercial) {
        this.commercial = commercial;
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

    public int getEtatProspection() {
        return etatProspection;
    }

    public void setEtatProspection(int etatProspection) {
        this.etatProspection = etatProspection;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public void setNomCommercial(String nomCommercial) {
        this.nomCommercial = nomCommercial;
    }

    public String getPrenomCommercial() {
        return prenomCommercial;
    }

    public void setPrenomCommercial(String prenomCommercial) {
        this.prenomCommercial = prenomCommercial;
    }
    
    public String getNomPrenom() {
        return prenomCommercial + " " + nomCommercial;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }
    
    public boolean isNePeutModifierProspection() {
        switch (etatProspection) {
            case 0:
                return  false;

            case 1:
                return true;

            case 2:
                return true;


        }
        return false;
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
    
    public String getEtatProspectionString() {

            if (etatProspection == 0 ) {
                return "En cours";
            }else if (etatProspection == 1 ) {
                return "Validée";
            } else if (etatProspection == 2 ) {
                return "Refusée";
            }else{
            return "---";
            }

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
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prospection)) {
            return false;
        }
        Prospection other = (Prospection) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.magma.entity.Prospection[ id=" + id + " ]";
    }
    
}
