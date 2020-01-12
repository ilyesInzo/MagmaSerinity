/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.FonctionsString;
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

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_TypeEncaissementVente")
public class TypeEncaissementVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEVnt_Id", nullable = false)
    private Long id;

    @Column(name = "TEVnt_Libelle")
    private String libelle;

    @Column(name = "TEVnt_Montant")
    private boolean montant;

    @Column(name = "TEVnt_dateCheque")
    private boolean dateCheque;

    @Column(name = "TEVnt_NumCheque")
    private boolean numCheque;

    @Column(name = "TEVnt_Banque")
    private boolean Banque;

    @Column(name = "TEVnt_Ville")
    private boolean Ville;

    @Column(name = "TEVnt_LibelleTicket")
    private boolean libelleTicket;

    @Column(name = "TEVnt_ValeurUnitaireTicket")
    private boolean valeurUnitaireTicket;

    @Column(name = "TEVnt_DeductionPourcentageTicket")
    private boolean deductionPourcentageTicket;

    @Column(name = "TEVnt_NbrTicket")
    private boolean nbrTicket;

    @Column(name = "TEVnt_TauxRetenu")
    private boolean tauxRetenu;
    
    @Column(name = "TEVnt_Avoir")
    private boolean avoir;
    
    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "TEVnt_Supprimer")
    private boolean supprimer;

    @OneToMany(mappedBy = "typeEncaissementVente", fetch = FetchType.EAGER)
    private List<EncaissementBonLivraison> listEncaissementBonLivraisons;
    
    @OneToMany(mappedBy = "typeEncaissementVente", fetch = FetchType.EAGER)
    private List<Encaissement> listEncaissements;

    @Column(name = "TEVnt_Description")
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = FonctionsString.PremiereLetteMajuscule(libelle);
    }

    public boolean isMontant() {
        return montant;
    }

    public void setMontant(boolean montant) {
        this.montant = montant;
    }

    public boolean isDateCheque() {
        return dateCheque;
    }

    public void setDateCheque(boolean dateCheque) {
        this.dateCheque = dateCheque;
    }

    public boolean isNumCheque() {
        return numCheque;
    }

    public void setNumCheque(boolean numCheque) {
        this.numCheque = numCheque;
    }

    public boolean isBanque() {
        return Banque;
    }

    public void setBanque(boolean Banque) {
        this.Banque = Banque;
    }

    public boolean isVille() {
        return Ville;
    }

    public void setVille(boolean Ville) {
        this.Ville = Ville;
    }

    public boolean isLibelleTicket() {
        return libelleTicket;
    }

    public void setLibelleTicket(boolean libelleTicket) {
        this.libelleTicket = libelleTicket;
    }

    public boolean isValeurUnitaireTicket() {
        return valeurUnitaireTicket;
    }

    public void setValeurUnitaireTicket(boolean valeurUnitaireTicket) {
        this.valeurUnitaireTicket = valeurUnitaireTicket;
    }

    public boolean isDeductionPourcentageTicket() {
        return deductionPourcentageTicket;
    }

    public void setDeductionPourcentageTicket(boolean deductionPourcentageTicket) {
        this.deductionPourcentageTicket = deductionPourcentageTicket;
    }

    public boolean isNbrTicket() {
        return nbrTicket;
    }

    public void setNbrTicket(boolean nbrTicket) {
        this.nbrTicket = nbrTicket;
    }

    public List<EncaissementBonLivraison> getListEncaissementBonLivraisons() {
        return listEncaissementBonLivraisons;
    }

    public void setListEncaissementBonLivraisons(List<EncaissementBonLivraison> listEncaissementBonLivraisons) {
        this.listEncaissementBonLivraisons = listEncaissementBonLivraisons;
    }

    public boolean isTauxRetenu() {
        return tauxRetenu;
    }

    public void setTauxRetenu(boolean tauxRetenu) {
        this.tauxRetenu = tauxRetenu;
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

    public List<Encaissement> getListEncaissements() {
        return listEncaissements;
    }

    public void setListEncaissements(List<Encaissement> listEncaissements) {
        this.listEncaissements = listEncaissements;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public boolean isAvoir() {
        return avoir;
    }

    public void setAvoir(boolean avoir) {
        this.avoir = avoir;
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
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
        if (!(object instanceof TypeEncaissementVente)) {
            return false;
        }
        TypeEncaissementVente other = (TypeEncaissementVente) object;
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
