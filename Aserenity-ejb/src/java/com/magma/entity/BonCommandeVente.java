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
@Table(name = "T_BonCommandeVente")
public class BonCommandeVente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BCVnt_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Tab_DateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "BCVnt_DateBonCommandeVente")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateBonCommandeVente;

    @Column(name = "BCVnt_Numero", unique = true)
    private String numero;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Code")
    private String codeClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;
    
    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;

    @Column(name = "BCVnt_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;
    
    @Column(name = "BCVnt_MontantTVA", scale = 3, precision = 28)
    private BigDecimal montantTVA;

    @Column(name = "BCVnt_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;

    @Column(name = "BCVnt_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;
    
    @Column(name = "BCVnt_TotalTVA", scale = 3, precision = 28)
    private BigDecimal totalTVA;

    @Column(name = "BCVnt_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    @Column(name = "BCVnt_Reste", scale = 3, precision = 28)
    private BigDecimal reste;
    
    @Column(name = "BCVnt_DescriptionMotifAnnulation")
    private String descriptionMotifAnnulation;


    @Column(name = "BCVnt_TotalTaxe", precision = 28, scale = 3)
    private BigDecimal totalTaxe;

    @OneToMany(mappedBy = "bonCommandeVente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LigneBonCommandeVente> listeLigneBonCommandeVentes;
    
    @OneToMany(mappedBy = "bonCommandeVente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TaxesBonCommandeVente> listsTaxe;

    // 0: client / 1: Livreur
    @Column(name = "BCVnt_TypeVente")
    private Integer typeVente;
    
    // 0: En preparation // 1: Modifie// 2: Rejete // 3: Approuve
    @Column(name = "BCVnt_Etat")
    private int etat;
    
    // 0: vente Directe , 1: Devis
    @Column(name = "BCVnt_OrigineBonLivraison")
    private Integer origineBonCommandeVente;
    
    // 0: En Bon Livraison // 1: Facture 
    @Column(name = "BCVnt_TransformTo")
    private int transFormTo;
    
    @Column(name = "BCVnt_IdDocument")
    private Long idDocument;
    
    @Column(name = "BCVnt_NumeroDocument")
    private String numeroDocument;
    
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
        
    @Transient
    private Client client;

    @Transient
    private Utilisateur utilisateur;
    
    @Transient
    private Devis devis;
    
    @Column(name = "Tab_IdUserCreate")
    private Long idUserCreate;

    @Column(name = "Tab_LibelleUserCreate")
    private String libelleUserCreate;
    
    @Column(name = "Tab_IdUserModif")
    private Long idUserModif;

    @Column(name = "Tab_LibelleUserModif")
    private String libelleUserModif;

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

    public Date getDateBonCommandeVente() {
        return dateBonCommandeVente;
    }

    public void setDateBonCommandeVente(Date dateBonCommandeVente) {
        this.dateBonCommandeVente = dateBonCommandeVente;
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

    public boolean isAssujettiTVA() {
        return assujettiTVA;
    }

    public void setAssujettiTVA(boolean assujettiTVA) {
        this.assujettiTVA = assujettiTVA;
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

    public BigDecimal getTotalHT() {
        return FonctionsMathematiques.arrondiBigDecimal(totalHT, 3);
    }

    public void setTotalHT(BigDecimal totalHT) {
        this.totalHT = totalHT;
    }

    public BigDecimal getTotalTVA() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTVA, 3);
    }

    public void setTotalTVA(BigDecimal totalTVA) {
        this.totalTVA = totalTVA;
    }

    public BigDecimal getTotalTTC() {
        return FonctionsMathematiques.arrondiBigDecimal(totalTTC, 3);
    }

    public void setTotalTTC(BigDecimal totalTTC) {
        this.totalTTC = totalTTC;
    }

    public BigDecimal getReste() {
        return FonctionsMathematiques.arrondiBigDecimal(reste, 3);
    }

    public void setReste(BigDecimal reste) {
        this.reste = reste;
    }


    public String getDescriptionMotifAnnulation() {
        return descriptionMotifAnnulation;
    }

    public void setDescriptionMotifAnnulation(String descriptionMotifAnnulation) {
        this.descriptionMotifAnnulation = descriptionMotifAnnulation;
    }

    public BigDecimal getTotalTaxe() {
        return totalTaxe;
    }

    public void setTotalTaxe(BigDecimal totalTaxe) {
        this.totalTaxe = totalTaxe;
    }

    public List<LigneBonCommandeVente> getListeLigneBonCommandeVentes() {
        return listeLigneBonCommandeVentes;
    }

    public void setListeLigneBonCommandeVentes(List<LigneBonCommandeVente> listeLigneBonCommandeVentes) {
        this.listeLigneBonCommandeVentes = listeLigneBonCommandeVentes;
    }

    public List<TaxesBonCommandeVente> getListsTaxe() {
        return listsTaxe;
    }

    public void setListsTaxe(List<TaxesBonCommandeVente> listsTaxe) {
        this.listsTaxe = listsTaxe;
    }

    public Integer getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(Integer typeVente) {
        this.typeVente = typeVente;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getTransFormTo() {
        return transFormTo;
    }

    public void setTransFormTo(int transFormTo) {
        this.transFormTo = transFormTo;
    }

    public Long getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Long idDocument) {
        this.idDocument = idDocument;
    }

    public String getNumeroDocument() {
        return numeroDocument;
    }

    public void setNumeroDocument(String numeroDocument) {
        this.numeroDocument = numeroDocument;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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

    public Integer getOrigineBonCommandeVente() {
        return origineBonCommandeVente;
    }

    public void setOrigineBonCommandeVente(Integer origineBonCommandeVente) {
        this.origineBonCommandeVente = origineBonCommandeVente;
    }

    public Devis getDevis() {
        return devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
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
    
    public String getDateBonCommandeVenteStringFr() {
        try {
            if (dateBonCommandeVente != null) {
                return TraitementDate.returnDate(dateBonCommandeVente);
            }
            return "---";
        } catch (Exception e) {
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
        if (!(object instanceof BonCommandeVente)) {
            return false;
        }
        BonCommandeVente other = (BonCommandeVente) object;
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
