/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.AlgorithmesCryptage;
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
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_Utilisateur")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "Usr_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Usr_Code")
    private String code;

    @Column(name = "Usr_Nom")
    private String nom;

    @Column(name = "Usr_Prenom")
    private String prenom;

    @Column(name = "Usr_Email", unique = true, nullable = false)
    private String email;

    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(name = "Usr_Gsm")
    private String gsm;

    @Column(name = "Usr_TelFix")
    private String telFixe;

    @Column(name = "Usr_EtatUsr")
    private boolean etatUsr;

    @Column(name = "Usr_EstActif")
    private boolean estActif;

    @Column(name = "Usr_EstEmploye")
    private boolean estEmploye;
    
    @Column(name = "Usr_DateNaissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;

    @Column(name = "Usr_Description")
    private String description;

    @Column(name = "Usr_AffilieCNSS")
    private boolean affilieCNSS;

    @Column(name = "Usr_NumeroCNSS")
    private String numeroCNSS;

    @Column(name = "Usr_EmailProfessionel")
    private String emailProfessionel;

    @Column(name = "Usr_TelephoneProfessionel")
    private String telephoneProfessionel;

    @Column(name = "Usr_Statut")
    private String statut;
    
    @Column(name = "Usr_Photo")
    private String photo;

    @ManyToOne
    @JoinColumn(name = "Pro_Id", referencedColumnName = "Pro_Id", nullable = false)
    private Profile profile;
    
    @Column(name = "Usr_Password")
    private String passwd;

    @Column(name = "Gov_Id")
    private Long idGouvernorat;

    @Column(name = "Gov_Libelle")
    private String libelleGouvernorat;

    @Column(name = "Del_Id")
    private Long idDelegation;
    
    @Column(name = "Del_Libelle")
    private String libelleDelegation;
    
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
    
    @Transient
    private Gouvernorat gouvernorat;

    @Transient
    private Delegation delegation;
    
    @Transient
    private Departement departement;

    @Transient
    private Poste poste;
    
    @ManyToOne
    @JoinColumn(name = "Ent_Id", referencedColumnName = "Ent_Id", nullable = false)
    private Entreprise entreprise;
    
    
    //*********************** Visibilite Modules **************************//

    @Transient
    private String visibiliteMCommande;
            
    @Transient
    private String visibiliteMVentes;
    @Transient
    private String visibiliteSousMVentes;

    
    @Transient
    private String visibiliteMStockArticle;

    @Transient
    private String visibiliteMClient;

    @Transient
    private String visibiliteMProduit;
    @Transient
    private String visibiliteSousMProduit;
    
    @Transient
    private String visibiliteMParametrage;
    @Transient
    private String visibiliteMJourneaux;
    
    
    
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getTelFixe() {
        return telFixe;
    }

    public void setTelFixe(String telFixe) {
        this.telFixe = telFixe;
    }

    public boolean isEtatUsr() {
        return etatUsr;
    }

    public void setEtatUsr(boolean etatUsr) {
        this.etatUsr = etatUsr;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
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

    public boolean isAffilieCNSS() {
        return affilieCNSS;
    }

    public void setAffilieCNSS(boolean affilieCNSS) {
        this.affilieCNSS = affilieCNSS;
    }

    public String getNumeroCNSS() {
        return numeroCNSS;
    }

    public void setNumeroCNSS(String numeroCNSS) {
        this.numeroCNSS = numeroCNSS;
    }

    public String getEmailProfessionel() {
        return emailProfessionel;
    }

    public void setEmailProfessionel(String emailProfessionel) {
        this.emailProfessionel = emailProfessionel;
    }

    public String getTelephoneProfessionel() {
        return telephoneProfessionel;
    }

    public void setTelephoneProfessionel(String telephoneProfessionel) {
        this.telephoneProfessionel = telephoneProfessionel;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public boolean isEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

    public String getDateNaissanceStringFr() {
        try {
            if (dateNaissance != null) {
                return TraitementDate.returnDate(dateNaissance);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateNaissanceStringEn() {
        try {
            if (dateNaissance != null) {
                return TraitementDate.returnDateHeureEn(dateNaissance);
            }
            return "---";
        } catch (Exception e) {
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

    public boolean isEstEmploye() {
        return estEmploye;
    }

    public void setEstEmploye(boolean estEmploye) {
        this.estEmploye = estEmploye;
    }
    
    public String getNomPrenom() {
        return statut + " " + prenom + " " + nom;
    }
    
    public String getEmail() {
        return email;
    }

    public String getEmailString() {
        if (email != null && !email.equals("")) {
            return email;
        }
        return "---";
    }

    public void setEmail(String email) {
        this.email = email.trim();
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

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }
    
    public String getPasswd() {
        try {
            return passwd;
        } catch (Exception ex) {
            return "error";
        }
    }

    public void setPasswd(String passwd) {
        try {
            this.passwd = AlgorithmesCryptage.encoderEnMD5(passwd);
            System.out.println(passwd + "------------");
        } catch (Exception ex) {
            System.out.println("--");
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

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVisibiliteMVentes() {
        return visibiliteMVentes;
    }

    public void setVisibiliteMVentes(String visibiliteMVentes) {
        this.visibiliteMVentes = visibiliteMVentes;
    }

    public String getVisibiliteSousMVentes() {
        return visibiliteSousMVentes;
    }

    public void setVisibiliteSousMVentes(String visibiliteSousMVentes) {
        this.visibiliteSousMVentes = visibiliteSousMVentes;
    }

    public String getVisibiliteMStockArticle() {
        return visibiliteMStockArticle;
    }

    public void setVisibiliteMStockArticle(String visibiliteMStockArticle) {
        this.visibiliteMStockArticle = visibiliteMStockArticle;
    }

    public String getVisibiliteMClient() {
        return visibiliteMClient;
    }

    public void setVisibiliteMClient(String visibiliteMClient) {
        this.visibiliteMClient = visibiliteMClient;
    }

    public String getVisibiliteMProduit() {
        return visibiliteMProduit;
    }

    public void setVisibiliteMProduit(String visibiliteMProduit) {
        this.visibiliteMProduit = visibiliteMProduit;
    }

    public String getVisibiliteSousMProduit() {
        return visibiliteSousMProduit;
    }

    public void setVisibiliteSousMProduit(String visibiliteSousMProduit) {
        this.visibiliteSousMProduit = visibiliteSousMProduit;
    }

    public String getVisibiliteMParametrage() {
        return visibiliteMParametrage;
    }

    public void setVisibiliteMParametrage(String visibiliteMParametrage) {
        this.visibiliteMParametrage = visibiliteMParametrage;
    }

    public String getVisibiliteMJourneaux() {
        return visibiliteMJourneaux;
    }

    public void setVisibiliteMJourneaux(String visibiliteMJourneaux) {
        this.visibiliteMJourneaux = visibiliteMJourneaux;
    }

    public String getVisibiliteMCommande() {
        return visibiliteMCommande;
    }

    public void setVisibiliteMCommande(String visibiliteMCommande) {
        this.visibiliteMCommande = visibiliteMCommande;
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
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }

}
