/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_Gouvernorat")
public class Gouvernorat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Gov_Id")
    private Long id;

    @Column(name = "Gov_Libelle")
    private String libelle;

    @Column(name = "Gov_Description")
    private String description;
  
    @Column(name = "Tab_dateSynch")
    private Long dateSynch;

    @OneToMany(mappedBy = "gouvernorat", fetch = FetchType.EAGER)
    private List<Delegation> listDelegations;
    
    @ManyToOne
    @JoinColumn(name = "Pys_Id", referencedColumnName = "Pys_Id", nullable = true)
    private Pays pays;

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

    public long getDateSynch() {
        return dateSynch;
    }

    public void setDateSynch(long dateSynch) {
        this.dateSynch = dateSynch;
    }

    public List<Delegation> getListDelegations() {
        return listDelegations;
    }

    public void setListDelegations(List<Delegation> listDelegations) {
        this.listDelegations = listDelegations;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }
    
        @PreUpdate
    void preupdate() {
        this.dateSynch = System.currentTimeMillis();

    }
    @PrePersist
    void prepersist() {
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
        if (!(object instanceof Gouvernorat)) {
            return false;
        }
        Gouvernorat other = (Gouvernorat) object;
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
