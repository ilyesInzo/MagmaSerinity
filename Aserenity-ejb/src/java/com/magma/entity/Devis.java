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
@Table(name = "T_Devis")
public class Devis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "Dev_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Tab_DateCreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "Dev_DateDevis")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDevis;

    @Column(name = "Dev_Numero", unique = true)
    private String numero;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Code")
    private String codeClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;

    @Column(name = "Dev_MontantHT", scale = 3, precision = 28)
    private BigDecimal montantHT;
    
    @Column(name = "Dev_MontantTVA", scale = 3, precision = 28)
    private BigDecimal montantTVA;

    @Column(name = "Dev_MontantTTC", scale = 3, precision = 28)
    private BigDecimal montantTTC;

    @Column(name = "Dev_TotalHT", scale = 3, precision = 28)
    private BigDecimal totalHT;
    
    @Column(name = "Dev_TotalTVA", scale = 3, precision = 28)
    private BigDecimal totalTVA;

    @Column(name = "Dev_TotalTTC", scale = 3, precision = 28)
    private BigDecimal totalTTC;

    @Column(name = "Dev_Reste", scale = 3, precision = 28)
    private BigDecimal reste;

    @Column(name = "Dev_IdFact")
    private Long idFact;

    @Column(name = "Usr_Id")
    private Long idUtilisateur;

    @Column(name = "Usr_Nom")
    private String nom;

    @Column(name = "Usr_Prenom")
    private String prenom;
    
    @Column(name = "Dev_DescriptionMotifAnnulation")
    private String descriptionMotifAnnulation;


    @Column(name = "Dev_TotalTaxe", precision = 28, scale = 3)
    private BigDecimal totalTaxe;

    @OneToMany(mappedBy = "devis", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LigneDevis> listeLigneDeviss;

    @ManyToOne
    @JoinColumn(name = "MRDVe_Id", referencedColumnName = "MRDVe_Id", nullable = false)
    private MotifRejetDevisVente motifRejetDevisVente;

    
    @OneToMany(mappedBy = "devis", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TaxesDevis> listsTaxe;

    // 0: client / 1: Livreur
    @Column(name = "Dev_TypeVente")
    private Integer typeVente;
    // 0: En preparation // 1: Opportunite // 2: Modifie// 3: Rejete // 4: Approuve
    @Column(name = "Dev_Etat")
    private int etat;
    
    // 0: En Bon Livraison // 1: Facture 
    @Column(name = "Dev_TransformTo")
    private int transFormTo;
    
    @Column(name = "Cli_AssujettiTVA")
    private boolean assujettiTVA;
    
    @Column(name = "BLiv_IdBl")
    private Long idBl;
        @Column(name = "Tab_dateSynch")
    private Long dateSynch;
        
    @Transient
    private Client client;

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

    public List<LigneDevis> getListeLigneDeviss() {
        return listeLigneDeviss;
    }

    public void setListeLigneDeviss(List<LigneDevis> listeLigneDeviss) {
        this.listeLigneDeviss = listeLigneDeviss;
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
    
    public String getDateDevisStringFr() {
        try {
            if (dateDevis != null) {
                return TraitementDate.returnDate(dateDevis);
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



    public Long getIdFact() {
        return idFact;
    }

    public void setIdFact(Long idFact) {
        this.idFact = idFact;
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

    public boolean isNePeutModifierDevis() {
        switch (etat) {
            case 0:
                return false;

            case 1:
                return false;

            case 2:
                return true;

            case 3:
                return true;

            case 4:
                return true;
        }
        return false;
    }
    
    public boolean isNePeutApprouverDevis() {
        switch (etat) {
            case 0:
                return true;

            case 1:
                return false;

            case 2:
                return true;

            case 3:
                return true;

            case 4:
                return true;
        }
        return false;
    }
    
    public boolean isNePeutRejeterDevis() {
        switch (etat) {
            case 0:
                return false;

            case 1:
                return false;

            case 2:
                return true;

            case 3:
                return true;

            case 4:
                return true;
        }
        return false;
    }

    public List<TaxesDevis> getListsTaxe() {
        return listsTaxe;
    }

    public void setListsTaxe(List<TaxesDevis> listsTaxe) {
        this.listsTaxe = listsTaxe;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public MotifRejetDevisVente getMotifRejetDevisVente() {
        return motifRejetDevisVente;
    }

    public void setMotifRejetDevisVente(MotifRejetDevisVente motifRejetDevisVente) {
        this.motifRejetDevisVente = motifRejetDevisVente;
    }

    public int getTransFormTo() {
        return transFormTo;
    }

    public void setTransFormTo(int transFormTo) {
        this.transFormTo = transFormTo;
    }

    public boolean isAssujettiTVA() {
        return assujettiTVA;
    }

    public void setAssujettiTVA(boolean assujettiTVA) {
        this.assujettiTVA = assujettiTVA;
    }

    public Long getIdBl() {
        return idBl;
    }

    public void setIdBl(Long idBl) {
        this.idBl = idBl;
    }

    public String getDescriptionMotifAnnulation() {
        return descriptionMotifAnnulation;
    }

    public void setDescriptionMotifAnnulation(String descriptionMotifAnnulation) {
        this.descriptionMotifAnnulation = descriptionMotifAnnulation;
    }

    public Date getDateDevis() {
        return dateDevis;
    }

    public void setDateDevis(Date dateDevis) {
        this.dateDevis = dateDevis;
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
        if (!(object instanceof Devis)) {
            return false;
        }
        Devis other = (Devis) object;
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
