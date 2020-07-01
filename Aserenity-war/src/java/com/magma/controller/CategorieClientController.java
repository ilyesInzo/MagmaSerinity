package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.CategorieClient;
import com.magma.entity.Utilisateur;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "categorieClientController")
@SessionScoped
public class CategorieClientController implements Serializable {

    private CategorieClient selected;
    private CategorieClient selectedSingle;
    private List<CategorieClient> items = null;
    @EJB
    private CategorieClientFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private CategorieClient categorieClient;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    public CategorieClientController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (categorieClient.getIdEntrepriseSuivi() != null && categorieClient.getIdEntrepriseSuivi() != 0) {
         idEntreprise = categorieClient.getIdEntrepriseSuivi();
         } else {
         idEntreprise = categorieClient.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GCategorieClient", "MClient", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GCategorieClient", utilisateur);
            /*if (categorieClient.getIdEntrepriseSuivi() != null && categorieClient.getIdEntrepriseSuivi() != 0) {
             idEntreprise = categorieClient.getIdEntrepriseSuivi();
             } else {
             idEntreprise = categorieClient.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../categorieClient/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<CategorieClient> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- CategorieClientController - getItems: " + e.getMessage());
            return null;
        }
    }

    public CategorieClient getSelected() {
        return selected;
    }

    public void setSelected(CategorieClient selected) {
        this.selected = selected;
    }

    public CategorieClient getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(CategorieClient selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private CategorieClientFacadeLocal getFacade() {
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
        selected = new CategorieClient();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {

            String clause = " where o.CCl_Supprimer = 0 and o.CCl_Libelle like '" + selected.getLibelle().trim().toUpperCase() + "' ";

            if (selected.getParent() != null) {
                clause = clause + " and o.CCl_IdParent = " + selected.getParent().getId() + "";
            } else {
                clause = clause + " and o.CCl_Rang = " + selected.getRang() + "";
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

                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- CategorieClientController - create: " + e.getMessage());
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
            idTemp = selected.getId();
            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {
            String clause = " where o.CCl_Supprimer = 0 and o.CCl_Libelle like '" + selected.getLibelle().trim().toUpperCase() + "' and o.CCl_Id <> " + selected.getId();

            if (selected.getParent() != null) {
                clause = clause + " and o.CCl_IdParent = " + selected.getParent().getId() + "";
            } else {
                clause = clause + " and o.CCl_Rang = " + selected.getRang() + "";
            }

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
            System.out.println("Erreur- CategorieClientController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<CategorieClientPVT> temps = ejbFacadeCategorieClientPVT.findAll("where o.idCategorieClient = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- CategorieClientController - performDestroy: " + e.getMessage());
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

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    /* public SelectItem[] getItemsAvailableSelectOne() {
     return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
     }*/
    public SelectItem[] getItemsAvailableSelectOneParents() {
        return JsfUtil.getSelectItems(getFacade().findAll(" where o.supprimer = 0 and  o.dernierRang = 0 order by o.libelle asc"), true);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getFacade().findAll(" where o.supprimer = 0 and  o.dernierRang = 1 order by o.libelle asc"), true);
    }

    public CategorieClient getCategorieClient(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = CategorieClient.class)
    public static class CategorieClientControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategorieClientController controller = (CategorieClientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categorieClientController");
            return controller.getCategorieClient(getKey(value));
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
            if (object instanceof CategorieClient) {
                CategorieClient o = (CategorieClient) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CategorieClient.class.getName());
            }
        }

    }

}
