/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author inzo
 */
@Entity
@Table(name = "T_Privileges")
public class Privileges implements Serializable,Comparable<Privileges> {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "Pri_Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Pri_Libelle", nullable = false, length = 50)
    private String libelle;
    @Column(name = "Pri_Location", nullable = false, length = 50)
    private String location;
    @Column(name = "Pri_Module", nullable = false, length = 50)
    private String module;
    @Column(name = "Pri_Lecture")
    private boolean lecture;
    @Column(name = "Pri_Creation")
    private boolean creation;
    @Column(name = "Pri_Modification")
    private boolean modification;
    @Column(name = "Pri_Supression")
    private boolean supression;

    @Column(name = "Pri_Activation")
    private boolean activation;

    @Column(name = "Pri_Reinitialisation")
    private boolean reinitialisation;

    @Column(name = "Pri_Journalisation")
    private boolean  journalisation;

    @Column(name = "Pri_NbPrivelege")
    private int nbPrivelege;

    @Column(name = "Pri_Order")
    private int order;

    @Column(name = "Pri_OrderModule")
    private int orderModule;

    @Column(name = "Pri_SousModule")
    private boolean sousModule;

    @Column(name = "Pri_ModuleActive")
    private boolean moduleActiver;

    @ManyToOne
    @JoinColumn(name = "Pro_Id", referencedColumnName = "Pro_Id")
    private Profile profile;

    public Privileges() {
    }

    public Privileges(String libelle,String location, boolean lecture, boolean creation, boolean modification, boolean supression,
            boolean activation, int nbPrivelege, int order, String module, int orderModule, boolean reinitialisation, boolean synchronisation, boolean sousModule, boolean moduleActiver) {
        this.libelle = libelle;
        this.location = location;
        this.nbPrivelege = nbPrivelege;
        this.order = order;
        this.module = module;
        this.orderModule = orderModule;
        this.sousModule = sousModule;
        this.moduleActiver = moduleActiver;
        if (moduleActiver == false) {
            this.lecture = false;
            this.creation = false;
            this.modification = false;
            this.supression = false;
            this.activation = false;
            this.reinitialisation = false;
            this.journalisation = false;
        } else {
            this.lecture = lecture;
            this.creation = creation;
            this.modification = modification;
            this.supression = supression;
            this.activation = activation;
            this.reinitialisation = reinitialisation;
            this.journalisation = synchronisation;
        }

    }

    public int getOrderModule() {
        return orderModule;
    }

    public void setOrderModule(int orderModule) {
        this.orderModule = orderModule;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    

    public boolean isLecture() {
        return lecture;
    }

    public String getVisible() {
        if (isLecture() == false) {
            return "display: none";
        }
        return "";
    }

    public void setLecture(boolean lecture) {
        this.lecture = lecture;
    }

    public boolean isCreation() {
        return creation;
    }

    public void setCreation(boolean creation) {
        this.creation = creation;
    }

    public boolean isModification() {
        return modification;
    }

    public void setModification(boolean modification) {
        this.modification = modification;
    }

    public boolean isSupression() {
        return supression;
    }

    public void setSupression(boolean supression) {
        this.supression = supression;
    }

    public boolean isActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

    public boolean isReinitialisation() {
        return reinitialisation;
    }

    public void setReinitialisation(boolean reinitialisation) {
        this.reinitialisation = reinitialisation;
    }

    public boolean isJournalisation() {
        return journalisation;
    }

    public void setJournalisation(boolean journalisation) {
        this.journalisation = journalisation;
    }



    public int getNbPrivelege() {
        return nbPrivelege;
    }

    public String getNbPrivelegeString() {
        return nbPrivelege + "";
    }

    public void setNbPrivelege(int nbPrivelege) {
        this.nbPrivelege = nbPrivelege;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public boolean isSousModule() {
        return sousModule;
    }

    public void setSousModule(boolean sousModule) {
        this.sousModule = sousModule;
    }

    public boolean isModuleActiver() {
        return moduleActiver;
    }

    public void setModuleActiver(boolean moduleActiver) {
        this.moduleActiver = moduleActiver;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (libelle != null ? libelle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Privileges)) {
            return false;
        }
        Privileges other = (Privileges) object;

        /*if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;*/
        
        return other.getLibelle() != null && !other.getLibelle().trim().equals("") && this.getLibelle().trim().toUpperCase().equals(other.getLibelle().trim().toUpperCase());
    }

    @Override
    public String toString() {
        return libelle;
    }

    @Override
    public int compareTo(Privileges o) {
        return Integer.compare(this.getOrder(),o.getOrder());  //To change body of generated methods, choose Tools | Templates.
    }

}
