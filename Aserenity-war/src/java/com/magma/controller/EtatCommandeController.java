package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.Departement;
import com.magma.entity.EtatCommande;
import com.magma.entity.Poste;
import com.magma.entity.Utilisateur;
import com.magma.session.EtatCommandeFacadeLocal;
import com.magma.session.PosteFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "etatCommandeController")
@SessionScoped
public class EtatCommandeController implements Serializable {

    private EtatCommande selected;
    private EtatCommande selectedSingle;
    private List<EtatCommande> items = null;
    @EJB
    private EtatCommandeFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private EtatCommande etatCommande;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    private List<Poste> listePostes = null;
    @EJB
    private PosteFacadeLocal ejbFacadePoste;

    public EtatCommandeController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (etatCommande.getIdEntrepriseSuivi() != null && etatCommande.getIdEntrepriseSuivi() != 0) {
         idEntreprise = etatCommande.getIdEntrepriseSuivi();
         } else {
         idEntreprise = etatCommande.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GEtatCommande", "MCommande", null, utilisateur);

            // MenuTemplate.menuFonctionnalitesModules("GEtatCommande", utilisateur);
            /*if (etatCommande.getIdEntrepriseSuivi() != null && etatCommande.getIdEntrepriseSuivi() != 0) {
             idEntreprise = etatCommande.getIdEntrepriseSuivi();
             } else {
             idEntreprise = etatCommande.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../etatCommande/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<EtatCommande> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll(" where o.supprimer = 0 order by o.libelle asc");

            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- EtatCommandeController - getItems: " + e.getMessage());
            return null;
        }
    }

    public EtatCommande getSelected() {
        return selected;
    }

    public void setSelected(EtatCommande selected) {
        this.selected = selected;
    }

    public EtatCommande getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(EtatCommande selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private EtatCommandeFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new EtatCommande();
        listePostes = null;
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {

            String clause = " where o.ECm_Supprimer = 0 and o.ECm_Libelle like '" + selected.getLibelle().trim().toUpperCase() + "' ";

            if (selected.getParent() != null) {
                clause = clause + " and o.ECm_IdParent = " + selected.getParent().getId() + "";
            } else {
                clause = clause + " and o.ECm_Rang = " + selected.getRang() + "";
            }

            //errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());
            errorMsg = getFacade().verifierUnique(clause);

            if (errorMsg == false) {
                creationInfo();
                if (selected.getParent() != null) {
                    //  selected.setIdParent(selected.getParent().getId());
                    selected.setLibelleParent(selected.getParent().getLibelle());
                    selected.setRang(selected.getParent().getRang() + 1);
                    if (selected.getParent().getRang() > 0) {
                        selected.setIdPremierParent(selected.getParent().getIdPremierParent());
                        selected.setIdSuiteParent(selected.getParent().getIdSuiteParent() + " > " + selected.getParent().getId());
                        selected.setLibelleSuiteParent(selected.getParent().getLibelleSuiteParent() + " > " + selected.getParent().getLibelle());
                    } else {
                        selected.setIdPremierParent(selected.getParent().getId());
                        selected.setIdSuiteParent(selected.getParent().getId() + "");
                        selected.setLibelleSuiteParent(selected.getParent().getLibelle());
                    }
                } else {
                    selected.setLibelleSuiteParent("---");
                    selected.setIdSuiteParent("---");
                    selected.setRang(0);
                    selected.setIdPremierParent(null);
                    // selected.setIdParent(null);
                }

                if (selected.getDepartement() != null) {
                    selected.setIdDepartement(selected.getDepartement().getId());
                    selected.setLibelleDepartement(selected.getDepartement().getLibelle());

                    if (selected.getPoste() != null) {
                        selected.setIdPoste(selected.getPoste().getId());
                        selected.setLibellePoste(selected.getPoste().getLibelle());
                    }

                }

                selected.setSupprimer(false);
                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EtatCommandeController - create: " + e.getMessage());
            return null;
        }

    }

    public Long getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(Long idTemp) {
        this.idTemp = idTemp;
    }

    public String prepareEdit() {
        if (selected != null) {
            errorMsg = false;

            Departement departement = new Departement();
            if (selected.getIdDepartement() != null) {
                departement = new Departement();
                departement.setId(selected.getIdDepartement());
                departement.setLibelle(selected.getLibelleDepartement());
                selected.setDepartement(departement);
                listePostes = ejbFacadePoste.findAllNative(" where o.Dep_Id = " + selected.getIdDepartement() + " ");
            }

            if (selected.getLibellePoste() != null && !selected.getLibellePoste().equals("")) {
                Poste poste = new Poste();
                poste.setId(selected.getIdPoste());
                poste.setLibelle(selected.getLibellePoste());
                poste.setDepartement(departement);
                selected.setPoste(poste);
            }

            idTemp = selected.getId();
            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {

            String clause = " where o.ECm_Supprimer = 0 and o.ECm_Libelle like '" + selected.getLibelle().trim().toUpperCase() + "' and o.ECm_Id <> " + selected.getId();

            if (selected.getParent() != null) {
                clause = clause + " and o.ECm_IdParent = " + selected.getParent().getId() + "";
            } else {
                clause = clause + " and o.ECm_Rang = " + selected.getRang() + "";
            }

            //errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());
            errorMsg = getFacade().verifierUnique(clause);

            if (errorMsg == false) {
                editionInfo();
                if (selected.getParent() != null) {
                    //  selected.setIdParent(selected.getParent().getId());
                    selected.setLibelleParent(selected.getParent().getLibelle());
                    selected.setRang(selected.getParent().getRang() + 1);
                    if (selected.getParent().getRang() > 0) {
                        selected.setIdPremierParent(selected.getParent().getIdPremierParent());
                        selected.setIdSuiteParent(selected.getParent().getIdSuiteParent() + " > " + selected.getParent().getId());
                        selected.setLibelleSuiteParent(selected.getParent().getLibelleSuiteParent() + " > " + selected.getParent().getLibelle());
                    } else {
                        selected.setIdPremierParent(selected.getParent().getId());
                        selected.setIdSuiteParent(selected.getParent().getId() + "");
                        selected.setLibelleSuiteParent(selected.getParent().getLibelle());
                    }
                } else {
                    selected.setLibelleSuiteParent("---");
                    selected.setIdSuiteParent("---");
                    selected.setRang(0);
                    selected.setIdPremierParent(null);
                    // selected.setIdParent(null);
                }
                selected.setSupprimer(false);
                getFacade().edit(selected);
                return prepareList();
                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EtatCommandeController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<EtatCommandePVT> temps = ejbFacadeEtatCommandePVT.findAll("where o.idEtatCommande = " + selectedSingle.getId() + " ");
            //if (temps == null || temps.isEmpty()) {
            performDestroy();
            /*} else {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SuppressionNonAutorisé")));
             }*/
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet")));
        }
        return prepareList();
    }

    private void performDestroy() {
        try {
            getFacade().remove(selectedSingle);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EtatCommandeController - performDestroy: " + e.getMessage());
        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void changedPoste() {
        listePostes = null;
        if (selected.getDepartement() != null) {
            listePostes = ejbFacadePoste.findAllNative(" where o.Dep_Id = " + selected.getDepartement().getId());
        }
    }

    public void changeCouleur() {
        if (selected.isDernierRang() == true) {
            if (selected.isAcceptation() == true) {
                selected.setCouleur("7ab02c");
            } else {
                selected.setCouleur("C34A2C");
            }
        } else {
            selected.setCouleur("ffffff");
        }
    }

    private void creationInfo() {
        selected.setIdUserCreate(utilisateur.getId());
        selected.setLibelleUserCreate(utilisateur.getNomPrenom());
    }

    private void editionInfo() {
        selected.setIdUserModif(utilisateur.getId());
        selected.setLibelleUserModif(utilisateur.getNomPrenom());
    }

    public SelectItem[] getItemsAvailableSelectOnePoste() {
        if (listePostes == null) {
            listePostes = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listePostes, true);
    }

    public SelectItem[] getItemsAvailableSelectOneProduits() {
        return JsfUtil.getSelectItems(getFacade().findAll(" where o.supprimer = 0 and o.dernierRang = 1 order by o.libelle asc"), true);
    }

    public SelectItem[] getItemsAvailableSelectOneParents() {
        return JsfUtil.getSelectItems(getFacade().findAll(" where o.supprimer = 0 and  o.dernierRang = 0 order by o.libelle asc"), true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public EtatCommande getEtatCommande(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = EtatCommande.class)
    public static class EtatCommandeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EtatCommandeController controller = (EtatCommandeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "etatCommandeController");
            return controller.getEtatCommande(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EtatCommande) {
                EtatCommande o = (EtatCommande) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + EtatCommande.class.getName());
            }
        }

    }

}
