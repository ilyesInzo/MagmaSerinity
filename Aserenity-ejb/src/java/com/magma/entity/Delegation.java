/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import com.magma.bibliotheque.TraitementDate;
import java.io.Serializable;
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

/**
 *
 * @author inzo
 */
@Entity
@Table(name="T_Delegation")
public class Delegation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="Del_Id")
    private Long id;
    
    @Column(name="Del_Libelle")
    private String libelle;

    
    @ManyToOne
    @JoinColumn(name="Gov_Id",referencedColumnName = "Gov_Id",nullable = false)
    private Gouvernorat gouvernorat;


    @Column(name="Del_Description")
    private String description;
        @Column(name = "Tab_dateSynch")
    private Long dateSynch;
        
    @Column(name = "Tab_DateCreation")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreation;
        
    public String getDescriptionString() {
        if (description != null && !description.equals("")) {
            return description;
        } else {
            return "---";
        }
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
        this.libelle = libelle;
    }

    public long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public Gouvernorat getGouvernorat() {
        return gouvernorat;
    }

    public void setGouvernorat(Gouvernorat gouvernorat) {
        this.gouvernorat = gouvernorat;
    }

    public String getDescription() {
        return description;
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

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof Delegation)) {
            return false;
        }
        Delegation other = (Delegation) object;
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
