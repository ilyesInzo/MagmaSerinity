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
@Table(name = "T_BonCommandeCommercial")
public class BonCommandeCommercial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BCom_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Tab_DateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "BCom_DateBonCommandeCommercial")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBonCommandeCommercial;

    @Column(name = "BCom_Numero", unique = true)
    private String numero;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;
    
    @Column(name = "Cli_IdLivraison")
    private Long idClientLivraison;

    @Column(name = "Cli_LibelleLivraison")
    private String libelleClientLivraison;
    
    @Column(name = "CCl_Id")
    private Long idCategorieClient;

    @Column(name = "CCl_Libelle")
    private String libelleCategorieClient;
    
    @Column(name = "CCl_IdLivraison")
    private Long idCategorieClientLivraison;

    @Column(name = "CCl_LibelleLivraison")
    private String libelleCategorieClientLivraison;

    @Column(name = "BCom_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;
    
    @Column(name = "BCom_MontantTVA", scale = 3, precision = 28)
    private BigDecimal montantTVA;

    @Column(name = "BCom_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;

    @Column(name = "BCom_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;
    
    @Column(name = "BCom_TotalTVA", scale = 3, precision = 28)
    private BigDecimal totalTVA;

    @Column(name = "BCom_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    
    @Column(name = "BCom_DescriptionMotifAnnulation")
    private String descriptionMotifAnnulation;


    @Column(name = "BCom_TotalTaxe", precision = 28, scale = 3)
    private BigDecimal totalTaxe;

    @OneToMany(mappedBy = "bonCommandeCommercial", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LigneBonCommandeCommercial> listeLigneBonCommandeCommercials;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ECm_Id", referencedColumnName = "ECm_Id", nullable = false)
    private EtatCommande etatCommande;
    
    // 0: client / 1: Livreur
    @Column(name = "BCom_TypeVente")
    private Integer typeVente;
    
    
    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;
    
    
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
        
    @Transient
    private Client client;
    
    @Transient
    private CategorieClient categorieClient;
    
    @Transient
    private Client clientLivraison;
    
    @Transient
    private CategorieClient categorieClientLivraison;

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

    public Long getIdClientLivraison() {
        return idClientLivraison;
    }

    public void setIdClientLivraison(Long idClientLivraison) {
        this.idClientLivraison = idClientLivraison;
    }

    public String getLibelleClientLivraison() {
        return libelleClientLivraison;
    }

    public void setLibelleClientLivraison(String libelleClientLivraison) {
        this.libelleClientLivraison = libelleClientLivraison;
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

    public Long getIdCategorieClientLivraison() {
        return idCategorieClientLivraison;
    }

    public void setIdCategorieClientLivraison(Long idCategorieClientLivraison) {
        this.idCategorieClientLivraison = idCategorieClientLivraison;
    }

    public String getLibelleCategorieClientLivraison() {
        return libelleCategorieClientLivraison;
    }

    public void setLibelleCategorieClientLivraison(String libelleCategorieClientLivraison) {
        this.libelleCategorieClientLivraison = libelleCategorieClientLivraison;
    }

    public CategorieClient getCategorieClient() {
        return categorieClient;
    }

    public void setCategorieClient(CategorieClient categorieClient) {
        this.categorieClient = categorieClient;
    }

    public Client getClientLivraison() {
        return clientLivraison;
    }

    public void setClientLivraison(Client clientLivraison) {
        this.clientLivraison = clientLivraison;
    }

    public CategorieClient getCategorieClientLivraison() {
        return categorieClientLivraison;
    }

    public void setCategorieClientLivraison(CategorieClient categorieClientLivraison) {
        this.categorieClientLivraison = categorieClientLivraison;
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

    public List<LigneBonCommandeCommercial> getListeLigneBonCommandeCommercials() {
        return listeLigneBonCommandeCommercials;
    }

    public void setListeLigneBonCommandeCommercials(List<LigneBonCommandeCommercial> listeLigneBonCommandeCommercials) {
        this.listeLigneBonCommandeCommercials = listeLigneBonCommandeCommercials;
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
    
    public String getDateBonCommandeCommercialStringFr() {
        try {
            if (dateBonCommandeCommercial != null) {
                return TraitementDate.returnDate(dateBonCommandeCommercial);
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

    public void setTotalTTC(BigDecimal totalTTC) {
        this.totalTTC = totalTTC;
    }

    public BigDecimal getTotalTaxe() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTaxe, 3);

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

    public boolean isAssujettiTVA() {
        return assujettiTVA;
    }

    public void setAssujettiTVA(boolean assujettiTVA) {
        this.assujettiTVA = assujettiTVA;
    }

    public String getDescriptionMotifAnnulation() {
        return descriptionMotifAnnulation;
    }

    public void setDescriptionMotifAnnulation(String descriptionMotifAnnulation) {
        this.descriptionMotifAnnulation = descriptionMotifAnnulation;
    }

    public Date getDateBonCommandeCommercial() {
        return dateBonCommandeCommercial;
    }

    public void setDateBonCommandeCommercial(Date dateBonCommandeCommercial) {
        this.dateBonCommandeCommercial = dateBonCommandeCommercial;
    }

    public BigDecimal getMontantTVA() {
         return FonctionsMathematiques.arrondiBigDecimal(montantTVA, 3);
    }

    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    public BigDecimal getTotalTVA() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTVA, 3);
    }

    public void setTotalTVA(BigDecimal totalTVA) {
        this.totalTVA = totalTVA;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public EtatCommande getEtatCommande() {
        return etatCommande;
    }

    public void setEtatCommande(EtatCommande etatCommande) {
        this.etatCommande = etatCommande;
    }

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
    }

    public Commercial getCommercial() {
        return commercial;
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
        if (nomCommercial != null) {
            return prenomCommercial + " " + nomCommercial;
        } else {
            return "---";
        }

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
        if (!(object instanceof BonCommandeCommercial)) {
            return false;
        }
        BonCommandeCommercial other = (BonCommandeCommercial) object;
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
