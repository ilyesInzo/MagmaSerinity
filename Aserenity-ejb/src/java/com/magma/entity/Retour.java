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
import javax.persistence.Transient;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_Retour")
public class Retour implements Serializable {

    @Id
    @Column(name = "Ret_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Code")
    private String codeClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;
    
    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;

    @Column(name = "Ret_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;

    @Column(name = "Ret_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;

    @Column(name = "Ret_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;

    @Column(name = "Ret_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    @Column(name = "Ret_Reste", scale = 3, precision = 28)
    private BigDecimal reste;

    @Column(name = "Ret_Id_FactBL")
    private Long idFactureBL;
    
    @Column(name = "Ret_NumeroFactBL")
    private String numeroFactureBL;

    @Column(name = "Usr_Id")
    private Long idUtilisateur;

    @Column(name = "Usr_Nom")
    private String nom;

    @Column(name = "Usr_Prenom")
    private String prenom;

    @Column(name = "Ret_TotalTaxe", precision = 28, scale = 3)
    private BigDecimal totalTaxe;

    @OneToMany(mappedBy = "retour", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LigneRetour> listeLigneRetours;
    
    // 0: En cours // 1: Valider // 2: Annulé 
    @Column(name = "Ret_Etat")
    private int etat;
    
    @Column(name = "Ret_OrigineRetour")
    private Integer origineRetour;

    @Transient
    private Client client;
    
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

    public BigDecimal getTotalTaxe() {
        return totalTaxe;
    }

    public void setTotalTaxe(BigDecimal totalTaxe) {
        this.totalTaxe = totalTaxe;
    }

    public List<LigneRetour> getListeLigneRetours() {
        return listeLigneRetours;
    }

    public void setListeLigneRetours(List<LigneRetour> listeLigneRetours) {
        this.listeLigneRetours = listeLigneRetours;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

        public String getEtatString()
    { 
        switch (etat) {
            case 0:
                return "En cours";

            case 1:
                return "Validé";

            case 2:
                return "Annulé";
            
            default: return "---";

        }
    }
    
    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public Long getIdFactureBL() {
        return idFactureBL;
    }

    public void setIdFactureBL(Long idFactureBL) {
        this.idFactureBL = idFactureBL;
    }

    public String getNumeroFactureBL() {
        return numeroFactureBL;
    }

    public void setNumeroFactureBL(String numeroFactureBL) {
        this.numeroFactureBL = numeroFactureBL;
    }

    

    public Integer getOrigineRetour() {
        return origineRetour;
    }

    public void setOrigineRetour(Integer origineRetour) {
        this.origineRetour = origineRetour;
    }
    
    public String getOrigineRetourtring() {
        try {

            if (origineRetour == 0) {
                return "Facture";
            } else if (origineRetour == 1) {
                return "Bon Livraison";
            } else{
                return "---";
            }
    
        } catch (Exception e) {
            return "---";
        }

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
        if (!(object instanceof Retour)) {
            return false;
        }
        Retour other = (Retour) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "";
    }
    
}
