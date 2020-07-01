package com.magma.controller;

import com.magma.entity.Poste;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Utilisateur;
import com.magma.session.PosteFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

@ManagedBean(name= "posteController")
@SessionScoped
public class PosteController implements Serializable {

    private Poste selected;
    private Poste selectedSingle;
    private List<Poste> items = null;
    @EJB
    private PosteFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private Poste poste;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    private List<Poste> listePostes = null;

    public PosteController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (poste.getIdEntrepriseSuivi() != null && poste.getIdEntrepriseSuivi() != 0) {
                idEntreprise = poste.getIdEntrepriseSuivi();
            } else {
                idEntreprise = poste.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GPoste", "MParametrage", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GPoste", utilisateur);
            /*if (poste.getIdEntrepriseSuivi() != null && poste.getIdEntrepriseSuivi() != 0) {
                idEntreprise = poste.getIdEntrepriseSuivi();
            } else {
                idEntreprise = poste.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../poste/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Poste> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- PosteController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Poste getSelected() {
        return selected;
    }

    public void setSelected(Poste selected) {
        this.selected = selected;
    }

    public Poste getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Poste selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private PosteFacadeLocal getFacade() {
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
        selected = new Poste();
        errorMsg = false;
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getDepartement().getId());
            if (errorMsg == false) {
                creationInfo();
                if (selected.getParent() != null) {
                    selected.setLibelleParent(selected.getParent().getLibelle());
                    selected.setRang(selected.getParent().getRang() + 1);
                    if (selected.getParent().getRang() > 0) {
                        selected.setIdSuiteParent(selected.getParent().getIdSuiteParent() + " > " + selected.getParent().getId());
                        selected.setLibelleSuiteParent(selected.getParent().getLibelleSuiteParent() + " > " + selected.getParent().getLibelle());
                    } else {
                        selected.setIdSuiteParent(selected.getParent().getId() + "");
                        selected.setLibelleSuiteParent(selected.getParent().getLibelle());
                    }

                } else {
                    selected.setIdSuiteParent("---");
                    selected.setRang(0);
                    selected.setLibelleSuiteParent("---");
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
            System.out.println("Erreur- PosteController - create: " + e.getMessage());
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
            listePostes = ejbFacade.findAllNative(" where o.Dep_Id = " + selected.getDepartement().getId() + " and  o.Pst_DernierRang = 0");

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId(), selected.getDepartement().getId());
            if (errorMsg == false) {
                editionInfo();
                if (selected.getParent() != null) {

                    selected.setLibelleParent(selected.getParent().getLibelle());
                    selected.setRang(selected.getParent().getRang() + 1);
                    if (selected.getParent().getRang() > 0) {
                        selected.setIdSuiteParent(selected.getParent().getIdSuiteParent() + " > " + selected.getParent().getId());
                        selected.setLibelleSuiteParent(selected.getParent().getLibelleSuiteParent() + " > " + selected.getParent().getLibelle());
                    } else {
                        selected.setIdSuiteParent(selected.getParent().getId() + "");
                        selected.setLibelleSuiteParent(selected.getParent().getLibelle());
                    }
                } else {
                    selected.setIdSuiteParent("---");
                    selected.setRang(0);
                    selected.setLibelleSuiteParent("---");
                }
                getFacade().edit(selected);
                return prepareList();
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- PosteController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<PostePVT> temps = ejbFacadePostePVT.findAll("where o.idPoste = " + selectedSingle.getId() + " ");
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
            getFacade().edit(selectedSingle);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- PosteController - performDestroy: " + e.getMessage());
        }
    }

    public void changedPosteParent() {
        listePostes = new ArrayList<>();
        listePostes = selected.getDepartement().getPostes().stream()
                .filter(x -> !x.isDernierRang())
                .collect(Collectors.toList());
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

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<Poste> getListePostes() {
        return listePostes;
    }

    public void setListePostes(List<Poste> listePostes) {
        this.listePostes = listePostes;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Poste getPoste(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Poste.class)
    public static class PosteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PosteController controller = (PosteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "posteController");
            return controller.getPoste(getKey(value));
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
            if (object instanceof Poste) {
                Poste o = (Poste) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Poste.class.getName());
            }
        }

    }

}
