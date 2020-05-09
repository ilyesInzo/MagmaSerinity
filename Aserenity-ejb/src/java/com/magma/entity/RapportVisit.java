/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_RapportVisit")
public class RapportVisit implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "RVst_Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Cli_Id")
    private Long idClient;

    @Column(name = "Cli_Libelle")
    private String libelleClient;
    
    @Column(name = "Com_Id")
    private Long idCommercial;
    
    @Column(name = "Com_Nom")
    private String nomCommercial;

    @Column(name = "Com_Prenom")
    private String prenomCommercial;
    
    @Column(name = "RVst_Description")
    private String description;
    
    @Column(name = "RVst_DateCreation")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreation;
    
    @OneToMany(mappedBy = "rapportVisit", fetch = FetchType.LAZY)
    private List<RapportVisitArticle> listRapportVisitArticles;
    
    @OneToOne
    @JoinColumn(name = "PVi_Id", referencedColumnName = "PVi_Id", nullable = true)
    private  PlanificationVisite planificationVisite;;

    public PlanificationVisite getPlanificationVisite() {
        return planificationVisite;
    }

    public void setPlanificationVisite(PlanificationVisite planificationVisite) {
        this.planificationVisite = planificationVisite;
    }
    
    
    

    public List<RapportVisitArticle> getListRapportVisitArticles() {
        return listRapportVisitArticles;
    }

    public void setListRapportVisitArticles(List<RapportVisitArticle> listRapportVisitArticles) {
        this.listRapportVisitArticles = listRapportVisitArticles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdCommercial() {
        return idCommercial;
    }

    public void setIdCommercial(Long idCommercial) {
        this.idCommercial = idCommercial;
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

    public String getDescription() {
        return description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public String getNomPrenomCommercial() {
        return prenomCommercial + " " + nomCommercial;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RapportVisit)) {
            return false;
        }
        RapportVisit other = (RapportVisit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.magma.entity.RapportVisit[ id=" + id + " ]";
    }
    
    
}
