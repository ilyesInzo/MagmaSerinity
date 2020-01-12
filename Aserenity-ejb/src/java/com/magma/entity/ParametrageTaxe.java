/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.FonctionsMathematiques;
import com.magma.bibliotheque.FonctionsString;
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

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_ParametrageTaxe")
public class ParametrageTaxe implements Serializable {
     private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PTa_Id")
    private Long id;

    @Column(name = "PTa_Libelle")
    private String libelle;

    @Column(name = "PTa_Valeur", scale = 3, precision = 28)
    private BigDecimal valeur;

    @Column(name = "PTa_Operation")
    private String operation;
    
    
    //0: Pourcentage & 1: valeur fixe
    @Column(name = "PTa_TypeTaxe")
    private String typeTaxe;
    
    
    //0:avant tva ; 1:apresTva    
    @Column(name = "PTa_ApresTva")
    private boolean apresTva;
    
    @Column(name = "PTa_Description")
    private String description;
    
    

   
    
    @OneToMany(mappedBy = "parametrageTaxe", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TaxesFacture> listsTaxe;
    
       @Column(name = "Tab_dateSynch")
    private Long dateSynch; 
    
        @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
        
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
       try {
            this.libelle = FonctionsString.toutMajuscule(libelle);
        } catch (Exception e) {
            this.libelle = libelle;
        }
    }

    public BigDecimal getValeur() {
        return FonctionsMathematiques.arrondiBigDecimal(valeur, 3);
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public String getLibelleString() {
        if (libelle != null && !libelle.equals("")) {
            return libelle;
        } else {
            return "---";
        }
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTypeTaxe() {
        return typeTaxe;
    }

    public void setTypeTaxe(String typeTaxe) {
        this.typeTaxe = typeTaxe;
    }
    
    public String getTypeTaxeString() {

        if (typeTaxe.equals("0")) {
            return "Pourcentage";
        } else if (typeTaxe.equals("1")) {
            return "Fixe";
        } else {
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

    public boolean isApresTva() {
        return apresTva;
    }

    public void setApresTva(boolean apresTva) {
        this.apresTva = apresTva;
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

    public List<TaxesFacture> getListsTaxe() {
        return listsTaxe;
    }

    public void setListsTaxe(List<TaxesFacture> listsTaxe) {
        this.listsTaxe = listsTaxe;
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
    public String toString() {
        return typeTaxe + " " + valeur + " " + operation;
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
        if (!(object instanceof ParametrageTaxe)) {
            return false;
        }
        ParametrageTaxe other = (ParametrageTaxe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
