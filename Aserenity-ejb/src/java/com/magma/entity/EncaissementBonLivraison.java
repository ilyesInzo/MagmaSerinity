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
@Table(name = "T_EncaissementBonLivraison")
public class EncaissementBonLivraison implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "EBLiv_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BLiv_Id", referencedColumnName = "BLiv_Id", nullable = false)
    private BonLivraison bonLivraison;

    @Column(name = "BLiv_Numero")
    private String numero;

    //0:espece; 1: credit; 2: cheque; 3:cheque antidaté; 4:ticket resto; 5: virement bancaire
    @Column(name = "EBLiv_TypeEncaissement")
    private int typeEncaissement;

    @Column(name = "EBLiv_DateEncaissement")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateEncaissement;

    @Column(name = "EBLiv_Montant", scale = 3, precision = 28)
    private BigDecimal montant;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;

    @Column(name = "EBLiv_NumCheque")
    private String numCheque;

    @Column(name = "Ban_Id")
    private Long idBanque;

    @Column(name = "Ban_Libelle")
    private String libelleBanque;

    @Column(name = "EBLiv_Ville")
    private String ville;

    @Column(name = "EBLiv_DateCheque")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCheque;

    @Column(name = "EBLiv_Tireur")
    private String tireur;

    @Column(name = "EBLiv_ValeurUnitaireTicket", scale = 3, precision = 28)
    private BigDecimal valeurUnitaireTicket;

    @Column(name = "EBLiv_NbrTicket")
    private int nbrTicket;

    @Column(name = "EBLiv_LibelleTicket")
    private String libelleTicket;

    @Column(name = "EBLiv_DeductionPourcentageTicket", scale = 3, precision = 28)
    private BigDecimal deductionPourcentageTicket;

    @Column(name = "EBLiv_Supprimer")
    private boolean supprimer;
    
    @ManyToOne
    @JoinColumn(name = "TEVnt_Id", referencedColumnName = "TEVnt_Id", nullable = false)
    private TypeEncaissementVente typeEncaissementVente;
    
    @Column(name = "EBLiv_TauxRetenu", scale = 3, precision = 28)
    private BigDecimal tauxRetenu;
    
    
    // 0: client / 1: Livreur
    @Column(name = "EBLiv_TypeVente")
    private Integer typeVente;
    
    @Column(name = "Tkt_Id")
    private Long idTicket;
        @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Transient
    private Banque banque;
    
    @Transient
    private Ticket ticket;
    
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

    public BonLivraison getBonLivraison() {
        return bonLivraison;
    }

    public void setBonLivraison(BonLivraison bonLivraison) {
        this.bonLivraison = bonLivraison;
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

    public int getTypeEncaissement() {
        return typeEncaissement;
    }

    public void setTypeEncaissement(int typeEncaissement) {
        this.typeEncaissement = typeEncaissement;
    }

    public Date getDateEncaissement() {
        return dateEncaissement;
    }

    public void setDateEncaissement(Date dateEncaissement) {
        this.dateEncaissement = dateEncaissement;
    }

    public BigDecimal getMontant() {
        return FonctionsMathematiques.arrondiBigDecimal(montant, 3);
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
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

    public Long getIdBanque() {
        return idBanque;
    }

    public void setIdBanque(Long idBanque) {
        this.idBanque = idBanque;
    }

    public String getLibelleBanque() {
        return libelleBanque;
    }

    public void setLibelleBanque(String libelleBanque) {
        this.libelleBanque = libelleBanque;
    }

    public Banque getBanque() {
        return banque;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    public Date getDateCheque() {
        return dateCheque;
    }

    public void setDateCheque(Date dateCheque) {
        this.dateCheque = dateCheque;
    }

    public String getDateChequeStringFr() {
        try {
            if (dateCheque != null) {
                return TraitementDate.returnDateHeure(dateCheque);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getNumCheque() {
        return numCheque;
    }

    public void setNumCheque(String numCheque) {
        this.numCheque = numCheque;
    }

    public BigDecimal getValeurUnitaireTicket() {
        return FonctionsMathematiques.arrondiBigDecimal(valeurUnitaireTicket, 3);
    }

    public void setValeurUnitaireTicket(BigDecimal valeurUnitaireTicket) {
        this.valeurUnitaireTicket = valeurUnitaireTicket;
    }

    public int getNbrTicket() {
        return nbrTicket;
    }

    public void setNbrTicket(int nbrTicket) {
        this.nbrTicket = nbrTicket;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTireur() {
        return tireur;
    }

    public void setTireur(String tireur) {
        this.tireur = tireur;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLibelleTicket() {
        return libelleTicket;
    }

    public void setLibelleTicket(String libelleTicket) {
        this.libelleTicket = libelleTicket;
    }

    public String getDateEncaissementStringFr() {
        try {
            if (dateEncaissement != null) {
                return TraitementDate.returnDateHeure(dateEncaissement);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateEncaissementStringEn() {
        try {
            if (dateEncaissement != null) {
                return TraitementDate.returnDateHeureEn(dateEncaissement);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public BigDecimal getDeductionPourcentageTicket() {
        return deductionPourcentageTicket;
    }

    public void setDeductionPourcentageTicket(BigDecimal deductionPourcentageTicket) {
        this.deductionPourcentageTicket = deductionPourcentageTicket;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public TypeEncaissementVente getTypeEncaissementVente() {
        return typeEncaissementVente;
    }

    public void setTypeEncaissementVente(TypeEncaissementVente typeEncaissementVente) {
        this.typeEncaissementVente = typeEncaissementVente;
    }

    public BigDecimal getTauxRetenu() {
        return tauxRetenu;
    }

    public void setTauxRetenu(BigDecimal tauxRetenu) {
        this.tauxRetenu = tauxRetenu;
    }

    public Integer getTypeVente() {
        return typeVente;
    }

    public void setTypeVente(Integer typeVente) {
        this.typeVente = typeVente;
    }

    public Long getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(Long idTicket) {
        this.idTicket = idTicket;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
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
        if (!(object instanceof EncaissementBonLivraison)) {
            return false;
        }
        EncaissementBonLivraison other = (EncaissementBonLivraison) object;
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
