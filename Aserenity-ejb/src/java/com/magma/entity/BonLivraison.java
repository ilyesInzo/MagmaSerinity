/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.FonctionsMathematiques;
import com.magma.bibliotheque.TraitementDate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_BonLivraison")
public class BonLivraison implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "BLiv_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "BLiv_DateBonLivraison")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBonLivraison;

    @Column(name = "BLiv_Numero", unique = true)
    private String numero;

    @Column(name = "Cli_Id")
    private Long idClient;

    /*@Column(name = "Cli_Code")
    private String codeClient;*/
    @Column(name = "Cli_Libelle")
    private String libelleClient;

    /*@Column(name = "Cli_IdLivraison")
    private Long idClientLivraison;

    @Column(name = "Cli_CodeLivraison")
    private String codeClientLivraison;

    @Column(name = "Cli_LibelleLivraison")
    private String libelleClientLivraison;*/
    @Column(name = "BLiv_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;
    
    @Column(name = "BLiv_MontantTVA", scale = 3, precision = 28)
    private BigDecimal montantTVA;

    @Column(name = "BLiv_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;

    @Column(name = "BLiv_Reste", scale = 3, precision = 28)
    private BigDecimal reste;

    /* @Column(name = "Liv_Id", length = 30)
    private Long idVendeur;

    @Column(name = "Liv_Code", length = 30)
    private String codeVendeur;

    @Column(name = "Liv_CodeCommercial")
    private String codeCommercialVendeur;

    @Column(name = "Liv_Vendeur")
    private String vendeur;

    @Column(name = "Cli_Av")
    private boolean avImpot;

    @Column(name = "BLiv_Longitude")
    private String longitude;

    @Column(name = "BLiv_Latitude")
    private String latitude;

    @Column(name = "BLiv_Precision")
    private String precision;

    @Column(name = "BLiv_AppVersion")
    private String appVersion;*/
    // 0 : bonCommande , 1: Devis , 2: vente Directe
    @Column(name = "BLiv_OrigineBonLivraison")
    private Integer origineBonLivraison;

    @Column(name = "Usr_Id")
    private Long idUtilisateur;

    @Column(name = "Usr_Nom")
    private String nom;

    @Column(name = "Usr_Prenom")
    private String prenom;

    /*@Column(name = "Dom_Id")
    private Long idDomaine;

    @Column(name = "Dom_Libelle")
    private String libelleDomaine;

    @Column(name = "Dom_Code")
    private String codeDomaine;*/
    @OneToMany(mappedBy = "bonLivraison", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LigneBonLivraison> listeLigneBonLivraisons;

    @Column(name = "BLiv_idFacture")
    private Long idFacture;

    @Column(name = "BLiv_Supprimer")
    private boolean supprimer;

    // 0: Pas de Paiement // 1: Paiement Total // 2: Annulé  //3: Paiement Partiel
    @Column(name = "BLiv_Etat")
    private int etat;

    @Column(name = "Dev_Libelle")
    private String libelleDevis;

    @Column(name = "Dev_Id")
    private Long idDevis;

    // 0: client / 1: Livreur
    @Column(name = "BLiv_TypeVente")
    private Integer typeVente;

    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;
    
    @Column(name = "Ret_Id")
    private Long idRetour;

    @Transient
    private Devis devis;
    @Transient
    private Client client;

    @OneToMany(mappedBy = "bonLivraison", fetch = FetchType.EAGER)
    private List<EncaissementBonLivraison> listEncaissementBonLivraisons;
    
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

    public String getOrigineBonLivraisonString() {
        try {

            if (origineBonLivraison == 0) {
                return "Bon commande";
            } else if (origineBonLivraison == 1) {
                return "Devis";
            } else if (origineBonLivraison == 2) {
                return "Vente directe";
            } else {
                return "---";
            }

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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    /* public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }*/
    public String getLibelleClient() {
        return libelleClient;
    }

    public void setLibelleClient(String libelleClient) {
        this.libelleClient = libelleClient;
    }

    public BigDecimal getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public BigDecimal getMontantTVA() {
        return FonctionsMathematiques.arrondiBigDecimal(montantTVA, 3);
    }

    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    /*public Long getIdVendeur() {
        return idVendeur;
    }

    public void setIdVendeur(Long idVendeur) {
        this.idVendeur = idVendeur;
    }

    public String getCodeVendeur() {
        if (codeVendeur != null) {
            return codeVendeur;
        } else {
            return "---";
        }
    }

    public void setCodeVendeur(String codeVendeur) {
        this.codeVendeur = codeVendeur;
    }

    public String getCodeCommercialVendeur() {
        return codeCommercialVendeur;
    }

    public void setCodeCommercialVendeur(String codeCommercialVendeur) {
        this.codeCommercialVendeur = codeCommercialVendeur;
    }

    public String getVendeur() {
        return vendeur;
    }

    public void setVendeur(String vendeur) {
        this.vendeur = vendeur;
    }

    public boolean isAvImpot() {
        return avImpot;
    }

    public void setAvImpot(boolean avImpot) {
        this.avImpot = avImpot;
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

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }*/
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<LigneBonLivraison> getListeLigneBonLivraisons() {
        return listeLigneBonLivraisons;
    }

    public void setListeLigneBonLivraisons(List<LigneBonLivraison> listeLigneBonLivraisons) {
        this.listeLigneBonLivraisons = listeLigneBonLivraisons;
    }

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
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
    
    public String getDateBonLivraisonStringFr() {
        try {
            if (dateBonLivraison != null) {
                return TraitementDate.returnDate(dateBonLivraison);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
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

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public BigDecimal getReste() {
        return reste;
    }

    public void setReste(BigDecimal reste) {
        this.reste = reste;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public boolean isNePeutModifierBonLivraison() {
        switch (etat) {
            case 0:
                return false || isNePeutApprouverBonLivraison();

            case 1:
                return true;

            case 2:
                return true;

            case 3:
                return true;

        }
        return false;
    }

    public boolean isNePeutApprouverBonLivraison() {
        if (idFacture != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getLibelleDevis() {
        return libelleDevis;
    }

    public void setLibelleDevis(String libelleDevis) {
        this.libelleDevis = libelleDevis;
    }

    public Long getIdDevis() {
        return idDevis;
    }

    public void setIdDevis(Long idDevis) {
        this.idDevis = idDevis;
    }

    public Integer getOrigineBonLivraison() {
        return origineBonLivraison;
    }

    public void setOrigineBonLivraison(Integer origineBonLivraison) {
        this.origineBonLivraison = origineBonLivraison;
    }

    public List<EncaissementBonLivraison> getListEncaissementBonLivraisons() {
        return listEncaissementBonLivraisons;
    }

    public void setListEncaissementBonLivraisons(List<EncaissementBonLivraison> listEncaissementBonLivraisons) {
        this.listEncaissementBonLivraisons = listEncaissementBonLivraisons;
    }

    public Integer getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(Integer typeVente) {
        this.typeVente = typeVente;
    }

    public String getTypeVenteString() {
        switch (typeVente) {
            case 0:
                return "Client";
            case 1:
                return "Vendeur";
            default:
                break;
        }
        return null;
    }
    
    public String getEtatString()
    { 
        switch (etat) {
            case 0:
                return "Pas de Paiement";

            case 1:
                return "Paiement Total";

            case 2:
                return "Annulé";

            case 3:
                return "Paiement Partiel";
            
            default: return "---";

        }
    }

    /*
    public Long getIdDomaine() {
        return idDomaine;
    }

    public void setIdDomaine(Long idDomaine) {
        this.idDomaine = idDomaine;
    }

    public String getLibelleDomaine() {
        return libelleDomaine;
    }

    public void setLibelleDomaine(String libelleDomaine) {
        this.libelleDomaine = libelleDomaine;
    }

    public String getCodeDomaine() {
        return codeDomaine;
    }

    public void setCodeDomaine(String codeDomaine) {
        this.codeDomaine = codeDomaine;
    }*/
    public boolean isAssujettiTVA() {
        return assujettiTVA;
    }

    public void setAssujettiTVA(boolean assujettiTVA) {
        this.assujettiTVA = assujettiTVA;
    }

    public Devis getDevis() {
        return devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public Date getDateBonLivraison() {
        return dateBonLivraison;
    }

    public void setDateBonLivraison(Date dateBonLivraison) {
        this.dateBonLivraison = dateBonLivraison;
    }

    public Long getIdRetour() {
        return idRetour;
    }

    public void setIdRetour(Long idRetour) {
        this.idRetour = idRetour;
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
        if (!(object instanceof BonLivraison)) {
            return false;
        }
        BonLivraison other = (BonLivraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return numero;
    }

}
