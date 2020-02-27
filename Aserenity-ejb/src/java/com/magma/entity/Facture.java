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
@Table(name = "T_Facture")
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Fct_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "Fct_DateFacture")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFacture;

    @Column(name = "Fct_Numero", unique = true)
    private String numero;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Code")
    private String codeClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;

    @Column(name = "Fct_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;
    
    @Column(name = "Fct_MontantTVA", scale = 3, precision = 28)
    private BigDecimal montantTVA;

    @Column(name = "Fct_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;

    @Column(name = "Fct_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;
    
    @Column(name = "Fct_TotalTVA", scale = 3, precision = 28)
    private BigDecimal totalTVA;

    @Column(name = "Fct_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    @Column(name = "Fct_Reste", scale = 3, precision = 28)
    private BigDecimal reste;

    @Column(name = "Fct_Synchroniser")
    private boolean synchroniser;

    @Column(name = "Usr_Id")
    private Long idUtilisateur;

    @Column(name = "Usr_Nom")
    private String nom;

    @Column(name = "Usr_Prenom")
    private String prenom;

    @Column(name = "Fct_TotalTaxe", precision = 28, scale = 3)
    private BigDecimal totalTaxe;

    @OneToMany(mappedBy = "facture", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LigneFacture> listeLigneFactures;

    /*@OneToMany(mappedBy = "facture", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TaxesFacture> listsTaxe;*/
    @OneToMany(mappedBy = "facture", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Encaissement> listeEncaissements;

    @OneToMany(mappedBy = "facture", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TaxesFacture> listsTaxe;

    //  0 : VenteDirecte,  1 : Devis , 2 : Commande , 3 : BonLivraison 
    @Column(name = "Fct_Origine")
    private Integer origine;
    
    @Column(name = "Fct_IdDocumentOrigine")
    private Long idDocumentOrigine;
    
    @Column(name = "Fct_NumeroDocumentOrigine")
    private String numeroDocumentOrigine;

    // 0: client / 1: Livreur
    @Column(name = "Fct_TypeVente")
    private Integer typeVente;

    @Column(name = "Fct_Avoir")
    private boolean avoir;

    @Column(name = "AVnt_Id")
    private Long idAvoir;

    @Column(name = "AVnt_Numero")
    private String numeroAvoir;

    @Column(name = "AVnt_TypeAvoir")
    private Integer typeAvoir;

    // 0: Pas de Paiement // 1: Paiement Total // 2: Annulé  //3: Paiement Partiel
    @Column(name = "Fct_Etat")
    private int etat;

    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;
    
    @Column(name = "Ret_Id")
    private Long idRetour;
    
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    @Transient
    private Client client;

    @Transient
    private Devis devis;
    
    @Transient
    private BonCommandeVente bonCommande;

    @Transient
    private Utilisateur utilisateur;
    
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
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

    public String getClientString() {
        if (codeClient != null && !codeClient.equals("")) {
            if (libelleClient != null && !libelleClient.equals("")) {
                if (libelleClient.contains(codeClient)) {
                    return libelleClient;
                } else {
                    return codeClient + " " + libelleClient;
                }
            }
            return codeClient;
        }
        return "---";
    }

    public Integer getOrigine() {
        return origine;
    }

    public void setOrigine(Integer origine) {
        this.origine = origine;
    }

    
    
    public String getOrigineFactureString() {
        try {

            if (origine == 0) {
                return "Devis";
            } else if (origine == 1) {
                return "Bon commande";
            } else if (origine == 2) {
                return "Bons livraison";
            } else if (origine == 3) {
                return "Vente directe";
            } else {
                return "---";
            }

        } catch (Exception e) {
            return "---";
        }

    }

    public String getAvoirString() {
        try {

            if (avoir == true) {
                return numeroAvoir;
            } else if (avoir == false) {
                return "---";
            } else {
                return "---";
            }
        } catch (Exception e) {
            return "---";
        }

    }

    public BigDecimal getMontantHT() {
        return FonctionsMathematiques.arrondiBigDecimal(montantHT, 3);
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public BigDecimal getMontantTTC() {
        return FonctionsMathematiques.arrondiBigDecimal(montantTTC, 3);
    }

    public BigDecimal getMontantTTCTF() {
        /*if (avImpot == true) {
            return FonctionsMathematiques.arrondiBigDecimal((montantTTC.multiply(new BigDecimal("1.01"))).add(new BigDecimal("0.6")), 3);
        }*/
        return montantTTC.add(new BigDecimal("0.6"));
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isSynchroniser() {
        return synchroniser;
    }

    public void setSynchroniser(boolean synchroniser) {
        this.synchroniser = synchroniser;
    }

    public List<LigneFacture> getListeLigneFactures() {
        return listeLigneFactures;
    }

    public void setListeLigneFactures(List<LigneFacture> listeLigneFactures) {
        this.listeLigneFactures = listeLigneFactures;
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

    public String getDateFactureStringFr() {
        try {
            if (dateFacture != null) {
                return TraitementDate.returnDate(dateFacture);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }
    
    public String getLibelleClient() {
        return libelleClient;
    }

    public void setLibelleClient(String libelleClient) {
        this.libelleClient = libelleClient;
    }

    public BigDecimal getTotalHT() {
        return FonctionsMathematiques.arrondiBigDecimal(totalHT, 3);
    }

    public void setTotalHT(BigDecimal totalHT) {
        this.totalHT = totalHT;
    }

    public BigDecimal getTotalTTC() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTTC, 3);
    }

    public BigDecimal getTotalTVA() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTVA, 3);
    }

    public void setTotalTVA(BigDecimal totalTVA) {
        this.totalTVA = totalTVA;
    }

    public void setTotalTTC(BigDecimal totalTTC) {
        this.totalTTC = totalTTC;
    }

    public BigDecimal getTotalTaxe() {
        return totalTaxe;
    }

    public void setTotalTaxe(BigDecimal totalTaxe) {
        this.totalTaxe = totalTaxe;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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

    public BigDecimal getReste() {
        return reste;
    }

    public void setReste(BigDecimal reste) {
        this.reste = reste;
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

    public Long getIdAvoir() {
        return idAvoir;
    }

    public void setIdAvoir(Long idAvoir) {
        this.idAvoir = idAvoir;
    }

    public String getNumeroAvoir() {
        return numeroAvoir;
    }

    public void setNumeroAvoir(String numeroAvoir) {
        this.numeroAvoir = numeroAvoir;
    }

    public boolean isAvoir() {
        return avoir;
    }

    public void setAvoir(boolean avoir) {
        this.avoir = avoir;
    }

    public Integer getTypeAvoir() {
        return typeAvoir;
    }

    public void setTypeAvoir(Integer typeAvoir) {
        this.typeAvoir = typeAvoir;
    }

    public List<Encaissement> getListeEncaissements() {
        return listeEncaissements;
    }

    public void setListeEncaissements(List<Encaissement> listeEncaissements) {
        this.listeEncaissements = listeEncaissements;
    }

    public List<TaxesFacture> getListsTaxe() {
        return listsTaxe;
    }

    public void setListsTaxe(List<TaxesFacture> listsTaxe) {
        this.listsTaxe = listsTaxe;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
    
    public BigDecimal getTotalPaiement()
    {   
        return totalTTC.subtract(reste);
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
    
    public boolean isNePeutModifierFacture() {
        switch (etat) {
            case 0:
                return  avoir || idRetour != null;

            case 1:
                return true;

            case 2:
                return true;

            case 3:
                return true;

        }
        return false;
    }
    
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

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Long getIdRetour() {
        return idRetour;
    }

    public void setIdRetour(Long idRetour) {
        this.idRetour = idRetour;
    }

    public BigDecimal getMontantTVA() {
        return FonctionsMathematiques.arrondiBigDecimal(montantTVA, 3);
    }

    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public Long getIdDocumentOrigine() {
        return idDocumentOrigine;
    }

    public void setIdDocumentOrigine(Long idDocumentOrigine) {
        this.idDocumentOrigine = idDocumentOrigine;
    }

    public String getNumeroDocumentOrigine() {
        return numeroDocumentOrigine;
    }

    public void setNumeroDocumentOrigine(String numeroDocumentOrigine) {
        this.numeroDocumentOrigine = numeroDocumentOrigine;
    }

    public BonCommandeVente getBonCommande() {
        return bonCommande;
    }

    public void setBonCommande(BonCommandeVente bonCommande) {
        this.bonCommande = bonCommande;
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
        if (!(object instanceof Facture)) {
            return false;
        }
        Facture other = (Facture) object;
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
