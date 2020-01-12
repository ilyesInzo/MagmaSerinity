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

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_AvoirVente")
public class AvoirVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "AVnt_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "AVnt_Numero")
    private String numero;

    @Column(name = "AVnt_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;
    
    @Column(name = "AVnt_MontantTVA", scale = 3, precision = 28)
    private BigDecimal montantTVA;

    @Column(name = "AVnt_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;

    // le reste egale a si 0 montant avoir < au total du montant payée de la facture
    // sinon egale au montant que je dois rembourser ( montant avoir - total du montant payée de la facture)
    // les taxes ajoutées aprés leTVA n'entre pas en jeu
    @Column(name = "AVnt_Reste", scale = 3, precision = 28)
    private BigDecimal reste;

    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(name = "AVnt_DateAvoir")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateAvoir;

    @Column(name = "AVnt_Supprimer")
    private boolean supprimer;

    @Column(name = "Fct_Numero")
    private String numeroFacture;

    @Column(name = "Fct_Id")
    private Long idFacture;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Code")
    private String codeClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;

    @Column(name = "AVnt_Remise")
    private BigDecimal remise;

    @ManyToOne
    @JoinColumn(name = "MAvr_Id", referencedColumnName = "MAvr_Id", nullable = false)
    private MotifAvoir motifAvoir;
    //0 : Avoir commercial(exp remise) | 1 : Avoir retour marchandise | 2 : Correction erreur facture
    @Column(name = "AVnt_TypeAvoir")
    private Integer typeAvoir;

    @OneToMany(mappedBy = "avoirVente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LigneAvoirVente> listLigneAvoirVentes;

    @Column(name = "AVnt_Description")
    private String description;
    
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

    public BigDecimal getMontantTTC() {
        return FonctionsMathematiques.arrondiBigDecimal(montantTTC, 3);
    }

    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    public BigDecimal getMontantHT() {
        return FonctionsMathematiques.arrondiBigDecimal(montantHT, 3);
    }

    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public Long getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Long idFacture) {
        this.idFacture = idFacture;
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

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public MotifAvoir getMotifAvoir() {
        return motifAvoir;
    }

    public void setMotifAvoir(MotifAvoir motifAvoir) {
        this.motifAvoir = motifAvoir;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getTypeAvoir() {
        return typeAvoir;
    }

    public void setTypeAvoir(Integer typeAvoir) {
        this.typeAvoir = typeAvoir;
    }

    public String getTypeAvoirString() {
        try {
            if (typeAvoir == 0) {
                return "Avoir commercial";
            } else if (typeAvoir == 1) {
                return "Avoir retour marchandise";
            } else if (typeAvoir == 2) {
                return "Avoir suite à une erreur";
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public List<LigneAvoirVente> getListLigneAvoirVentes() {
        return listLigneAvoirVentes;
    }

    public void setListLigneAvoirVentes(List<LigneAvoirVente> listLigneAvoirVentes) {
        this.listLigneAvoirVentes = listLigneAvoirVentes;
    }

    public BigDecimal getReste() {
        return FonctionsMathematiques.arrondiBigDecimal(reste, 3);
    }

    public void setReste(BigDecimal reste) {
        this.reste = reste;
    }

    public Date getDateAvoir() {
        return dateAvoir;
    }

    public void setDateAvoir(Date dateAvoir) {
        this.dateAvoir = dateAvoir;
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

    public String getDateAvoirStringFr() {
        try {
            if (dateAvoir != null) {
                return TraitementDate.returnDateHeure(dateAvoir);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDateAvoirStringEn() {
        try {
            if (dateAvoir != null) {
                return TraitementDate.returnDateHeureEn(dateAvoir);
            }
            return "---";
        } catch (Exception e) {
            return "---";
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionString() {
        if (description != null && !description.equals("")) {
            return description;
        } else {
            return "---";
        }
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
        if (!(object instanceof AvoirVente)) {
            return false;
        }
        AvoirVente other = (AvoirVente) object;
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
