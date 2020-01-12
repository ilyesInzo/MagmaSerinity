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
@Table(name = "T_Entreprise")
public class Entreprise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Ent_Id")
    private Long id;

    @Column(name = "Ent_Libelle")
    private String libelle;

    @Column(name = "Ent_Adresse")
    private String adresse;

    @Column(name = "Ent_SiteWeb")
    private String siteWeb;

    @Column(name = "Ent_Telephone")
    private String telephone;

    @Column(name = "Ent_Email")
    private String email;

    @Column(name = "Ent_Fax")
    private String fax;

    @Column(name = "Ent_CodePostal")
    private String codePostal;

    @Column(name = "Ent_Description")
    private String description;

    @Column(name = "Ent_Logo")
    private String logo;

    @Column(name = "Gov_Id")
    private Long idGouvernorat;

    @Column(name = "Gov_Libelle")
    private String libelleGouvernorat;

    @Column(name = "Del_Id")
    private Long idDelegation;

    @Column(name = "Del_Libelle")
    private String libelleDelegation;

    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    
    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Transient
    private Gouvernorat gouvernorat;

    @Transient
    private Delegation delegation;

    @OneToMany(mappedBy = "entreprise", fetch = FetchType.LAZY)
    private List<Utilisateur> utilisateurs;

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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(List<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getSiteWebString() {
        if (siteWeb == null || siteWeb.equals("")) {
            return "---";
        }
        return siteWeb;
    }

    public String getFaxString() {
        if (fax != null && !fax.equals("")) {
            return fax;
        }
        return "---";
    }

    public String getEmailString() {
        if (email != null && !email.equals("")) {
            return email;
        }
        return "---";
    }

    public String getTelephoneString() {
        if (telephone != null && !telephone.equals("")) {
            return telephone;
        }
        return "---";
    }

    public String getDescriptionString() {
        if (description != null && !description.equals("")) {
            return description;
        }
        return "---";
    }

    public String getAdresseString() {
        if (adresse != null && !adresse.equals("")) {
            return adresse;
        }
        return "---";
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
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
        if (!(object instanceof Entreprise)) {
            return false;
        }
        Entreprise other = (Entreprise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return libelle;
    }

}
