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

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_Article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Art_Id")
    private Long id;

    @Column(name = "Art_Code")
    private String code;

    @Column(name = "Art_Libelle")
    private String libelle;

    @Column(name = "Art_Description")
    private String description;

    @Column(name = "Art_PrixRevendeur", scale = 3, precision = 28)
    private BigDecimal prixRevendeur;
    
    @Column(name = "Art_PrixRetour", scale = 3, precision = 28)
    private BigDecimal prixRetour;
    
    @Column(name = "Art_QuantiteStock", scale = 3, precision = 28)
    private BigDecimal quantiteStock = BigDecimal.ZERO;

    @Column(name = "Art_QuantiteMinimal", scale = 3, precision = 28)
    private BigDecimal quantiteMinimal = BigDecimal.ZERO;

    @Column(name = "Art_Supprimer")
    private boolean supprimer;
    
    @Column(name = "Tab_DateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "Art_Photo1")
    private String photo1;

    @Column(name = "Art_Photo2")
    private String photo2;

    @Column(name = "Art_Photo3")
    private String photo3;

    @Column(name = "Art_Photo4")
    private String photo4;

    @Column(name = "Art_Photo5")
    private String photo5;
    
    @Column(name = "Art_Catalogue")
    private boolean catalogue;
    
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;
    
    @ManyToOne
    @JoinColumn(name = "Tva_Id", referencedColumnName = "Tva_Id", nullable = false)
    private Tva tva;
    
    @ManyToOne
    @JoinColumn(name = "Cat_Id", referencedColumnName = "Cat_Id", nullable = false)
    private Categorie categorie;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public BigDecimal getPrixRevendeur() {
        return FonctionsMathematiques.arrondiBigDecimal(prixRevendeur, 3);
    }

    public void setPrixRevendeur(BigDecimal prixRevendeur) {
        this.prixRevendeur = prixRevendeur;
    }

    public BigDecimal getPrixRetour() {
        return FonctionsMathematiques.arrondiBigDecimal(prixRetour, 3);
    }

    public BigDecimal getQuantiteStock() {
        return FonctionsMathematiques.arrondiBigDecimal(quantiteStock, 0);
    }

    public void setQuantiteStock(BigDecimal quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public BigDecimal getQuantiteMinimal() {
        return FonctionsMathematiques.arrondiBigDecimal(quantiteMinimal, 0);
    }

    public void setQuantiteMinimal(BigDecimal quantiteMinimal) {
        this.quantiteMinimal = quantiteMinimal;
    }

    public void setPrixRetour(BigDecimal prixRetour) {
        this.prixRetour = prixRetour;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public Tva getTva() {
        return tva;
    }

    public void setTva(Tva tva) {
        this.tva = tva;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getPhoto5() {
        return photo5;
    }

    public void setPhoto5(String photo5) {
        this.photo5 = photo5;
    }
    
    public String getCategorieSuiteString() {
        try {
            return categorie.getLibelleSuiteParentString();
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
    
    public boolean isAtteintSeuil()
    {
    
        return this.getQuantiteStock().compareTo(this.getQuantiteMinimal())!=1;
    
    }

    public Long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(Long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public boolean isCatalogue() {
        return catalogue;
    }

    public void setCatalogue(boolean catalogue) {
        this.catalogue = catalogue;
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
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
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
