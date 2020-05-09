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
import javax.persistence.ManyToOne;
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
@Table(name = "T_Client")

public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Cli_Id")
    private Long id;

    @Column(name = "Cli_Libelle")
    private String libelle;

    @Column(name = "Cli_Nature")
    private String nature;

    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "Cli_Password")
    private String password;

    @Column(name = "Cli_Gsm")
    private String gsm;

    @Column(name = "Cli_Email")
    private String email;

    @Column(name = "Cli_Photo")
    private String photo;

    @Column(name = "Cli_EtatPwd")
    private boolean etatPwd;

    @Column(name = "Cli_Supprimer")
    private boolean supprimer;

    @Column(name = "Gov_Id")
    private Long idGouvernorat;

    @Column(name = "Gov_Libelle")
    private String libelleGouvernorat;

    @Column(name = "Cli_Adresse")
    private String adresse;

    @Column(name = "Del_Id")
    private Long idDelegation;

    @Column(name = "Del_Libelle")
    private String libelleDelegation;

    @Column(name = "Cli_CPostal")
    private String codePostale;

    @Column(name = "Cli_Description")
    private String description;

    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;

    @Transient
    private Gouvernorat gouvernorat;

    @Transient
    private Delegation delegation;

    @ManyToOne
    @JoinColumn(name = "CCl_Id", referencedColumnName = "CCl_Id", nullable = false)
    private CategorieClient categorieClient;
    
    @ManyToOne
    @JoinColumn(name = "CClt_Id", referencedColumnName = "CClt_Id", nullable = true)
    private ClassificationClient classificationClient;

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

    @Transient
    private Commercial commercial;

    @Column(name = "Com_Id")
    private Long idCommercial;

    @Column(name = "Com_TypeCommercial")
    private int typeCommercial;

    @Column(name = "Com_Nom")
    private String nomCommercial;

    @Column(name = "Com_Prenom")
    private String prenomCommercial;

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

    public String getAdresse() {
        return adresse;
    }

    public String getAdresseString() {
        if (adresse != null && !adresse.equals("")) {
            return adresse;
        }
        return "---";
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Long getIdGouvernorat() {
        return idGouvernorat;
    }

    public void setIdGouvernorat(Long idGouvernorat) {
        this.idGouvernorat = idGouvernorat;
    }

    public String getLibelleGouvernorat() {

        if (libelleGouvernorat != null && !libelleGouvernorat.equals("")) {
            return libelleGouvernorat;
        }
        return "---";
    }

    public void setLibelleGouvernorat(String libelleGouvernorat) {
        this.libelleGouvernorat = libelleGouvernorat;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getPassword() {
        try {
            return password;
        } catch (Exception ex) {
            return "error";
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /* public void setPassword(String password) {
     try {
     if (password != null && !password.equals("")) {
     this.password = AlgorithmesCryptage.encoderEnMD5(password);
     } else {
     this.password = null;
     }
     } catch (Exception ex) {
     System.out.println("--");
     }
     }*/
    public String getGsmString() {
        if (gsm != null && !gsm.equals("")) {
            return gsm;
        }
        return "---";
    }

    public String getEmailString() {
        if (email != null && !email.equals("")) {
            return email;
        }
        return "---";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public CategorieClient getCategorieClient() {
        return categorieClient;
    }

    public void setCategorieClient(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
    }

    public boolean isEtatPwd() {
        return etatPwd;
    }

    public void setEtatPwd(boolean etatPwd) {
        this.etatPwd = etatPwd;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public Long getIdDelegation() {
        return idDelegation;
    }

    public void setIdDelegation(Long idDelegation) {
        this.idDelegation = idDelegation;
    }

    public String getLibelleDelegation() {
        if (libelleDelegation != null && !libelleDelegation.equals("")) {
            return libelleDelegation;
        }
        return "---";
    }

    public void setLibelleDelegation(String libelleDelegation) {
        this.libelleDelegation = libelleDelegation;
    }

    public String getCodePostaleString() {
        if (codePostale != null && !codePostale.equals("")) {
            return codePostale;
        }
        return "---";
    }

    public String getCodePostale() {
        return codePostale;
    }

    public void setCodePostale(String codePostale) {
        this.codePostale = codePostale;
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

    public String getDescription() {
        return description;
    }

    public boolean isAssujettiTVA() {
        return assujettiTVA;
    }

    public void setAssujettiTVA(boolean assujettiTVA) {
        this.assujettiTVA = assujettiTVA;
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

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
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

    public Commercial getCommercial() {
        return commercial;
    }

    public String getNomPrenom() {
        if (nomCommercial != null) {
            return prenomCommercial + " " + nomCommercial;
        } else {
            return "---";
        }

    }

    public void setCommercial(Commercial commercial) {
        this.commercial = commercial;
    }

    public int getTypeCommercial() {
        return typeCommercial;
    }

    public void setTypeCommercial(int typeCommercial) {
        this.typeCommercial = typeCommercial;
    }

    public ClassificationClient getClassificationClient() {
        return classificationClient;
    }

    public void setClassificationClient(ClassificationClient classificationClient) {
        this.classificationClient = classificationClient;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
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
