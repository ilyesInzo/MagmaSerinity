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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Cmd_Id")
    private Long id;
    
    @Column(name = "Cmd_Numero")
    private String numero;

    @Column(name = "Cmd_DateCommande")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCommande;
    
    @Column(name = "Cmd_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;

    @Column(name = "Cmd_MontantTVA", scale = 3, precision = 28)
    private BigDecimal montantTVA;

    @Column(name = "Cmd_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;
    
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
    
    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;

    @Column(name = "Com_Id")
    private Long idCommercial;

    @Column(name = "Com_Libelle")
    private String libelleCommercial;
    
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
    
   /* @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ECm_Id", referencedColumnName = "ECm_Id", nullable = false)
    private EtatCommande etatCommande;*/
    

    @OneToMany(mappedBy = "bonCommandeCommercial", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LigneBonCommandeCommercial> listeLigneBonCommandeCommercials;
    
    @Transient
    private Client client;
    @Transient
    private CategorieClient categorieClient;
    
    @Transient
    private Client clientLivraison;
    @Transient
    private CategorieClient categorieClientLivraison;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public BigDecimal getMontantHT() {
        return FonctionsMathematiques.arrondiBigDecimal(montantHT, 3);
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
        return FonctionsMathematiques.arrondiBigDecimal(montantTTC, 3);
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public String getLibelleClient() {
        return libelleClient;
    }

    public void setLibelleClient(String libelleClient) {
        this.libelleClient = libelleClient;
    }

    public boolean isAssujettiTVA() {
        return assujettiTVA;
    }

    public void setAssujettiTVA(boolean assujettiTVA) {
        this.assujettiTVA = assujettiTVA;
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
    
    

    /*public EtatCommande getEtatCommande() {
        return etatCommande;
    }

    public void setEtatCommande(EtatCommande etatCommande) {
        this.etatCommande = etatCommande;
    }*/

    public List<LigneBonCommandeCommercial> getListeLigneBonCommandeCommercials() {
        return listeLigneBonCommandeCommercials;
    }

    public void setListeLigneBonCommandeCommercials(List<LigneBonCommandeCommercial> listeLigneBonCommandeCommercials) {
        this.listeLigneBonCommandeCommercials = listeLigneBonCommandeCommercials;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
    }

    public String getLibelleCommercial() {
        return libelleCommercial;
    }

    public void setLibelleCommercial(String libelleCommercial) {
        this.libelleCommercial = libelleCommercial;
    }
    
    public String getDateCommandeStringFr() {
        try {
            if (dateCommande != null) {
                return TraitementDate.returnDateHeure(dateCommande);
            }
            return "---";
        } catch (Exception e) {
            e.printStackTrace();
            return "---";
        }
    }

    public String getDateCommandeStringEn() {
        try {
            if (dateCommande != null) {
                return TraitementDate.returnDateHeureEn(dateCommande);
            }
            return "---";
        } catch (Exception e) {
            e.printStackTrace();
            return "---";
        }
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

    public String getDateCreationStringFr() {
        try {
            if (dateCreation != null) {
                return TraitementDate.returnDateHeure(dateCreation);
            }
            return "---";
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
        return numero;
    }
    
}
