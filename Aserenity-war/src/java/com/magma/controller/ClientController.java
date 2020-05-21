package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.Client;
import com.magma.entity.Commercial;
import com.magma.entity.Delegation;
import com.magma.entity.Gouvernorat;
import com.magma.entity.Pays;
import com.magma.entity.Utilisateur;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.CommercialFacadeLocal;
import com.magma.session.DelegationFacadeLocal;
import com.magma.session.GouvernoratFacadeLocal;
import com.magma.session.PaysFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
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

@ManagedBean(name = "clientController")
@SessionScoped
public class ClientController implements Serializable {

    private Client selected;
    private Client selectedSingle;
    private List<Client> items = null;
    @EJB
    private ClientFacadeLocal ejbFacade;
    @EJB
    private DelegationFacadeLocal ejbFacadeDelegation;
    @EJB

    private GouvernoratFacadeLocal ejbFacadeGouvernorat;
    @EJB
    private PaysFacadeLocal ejbFacadePays;

    @EJB
    private CommercialFacadeLocal ejbFacadeCommercial;

    private boolean errorMsg;
    private Long idTemp;
    private Client client;
    private long idEntreprise = 0;
    private List<Delegation> listDelegation = null;
    private List<Gouvernorat> listGouvernorat = null;
    private Utilisateur utilisateur;

    public ClientController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (client.getIdEntrepriseSuivi() != null && client.getIdEntrepriseSuivi() != 0) {
         idEntreprise = client.getIdEntrepriseSuivi();
         } else {
         idEntreprise = client.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GClient", "MClient", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GClient", utilisateur);
            /*if (client.getIdEntrepriseSuivi() != null && client.getIdEntrepriseSuivi() != 0) {
             idEntreprise = client.getIdEntrepriseSuivi();
             } else {
             idEntreprise = client.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../client/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Client> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- ClientController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Client getSelected() {
        return selected;
    }

    public void setSelected(Client selected) {
        this.selected = selected;
    }

    public Client getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Client selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private ClientFacadeLocal getFacade() {
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
        selected = new Client();
        selected.setAssujettiTVA(true);
        errorMsg = false;
        listDelegation = new ArrayList<>();
        listGouvernorat = new ArrayList<>();
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getGsm());

            if (errorMsg == false) {


                if (selected.getPays() != null) {
                    selected.setIdPays(selected.getPays().getId());
                    selected.setLibellePays(selected.getPays().getLibelle());

                }
                
                    if (selected.getGouvernorat() != null) {
                        selected.setIdGouvernorat(selected.getGouvernorat().getId());
                        selected.setLibelleGouvernorat(selected.getGouvernorat().getLibelle());

                        if (selected.getDelegation() != null) {
                            selected.setIdDelegation(selected.getDelegation().getId());
                            selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                        }
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
            System.out.println("Erreur- ClientController - create: " + e.getMessage());
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


            Pays pays = new Pays();
            if (selected.getIdPays() != null) {
                pays = new Pays();
                pays.setId(selected.getIdPays());
                pays.setLibelle(selected.getLibellePays());
                selected.setPays(pays);
            }

            listGouvernorat = ejbFacadeGouvernorat.findAllNative(" where o.Pys_Id = " + selected.getIdPays() + " ");

            Gouvernorat gouvernorat = new Gouvernorat();
            if (selected.getIdGouvernorat() != null) {
                gouvernorat = new Gouvernorat();
                gouvernorat.setId(selected.getIdGouvernorat());
                gouvernorat.setLibelle(selected.getLibelleGouvernorat());
                gouvernorat.setPays(pays);
                selected.setGouvernorat(gouvernorat);
            }

            listDelegation = ejbFacadeDelegation.findAllNative(" where o.Gov_Id = " + selected.getIdGouvernorat() + " ");

            if (selected.getLibelleDelegation() != null && !selected.getLibelleDelegation().equals("")) {
                Delegation delegation = new Delegation();
                delegation.setId(selected.getIdDelegation());
                delegation.setLibelle(selected.getLibelleDelegation());
                delegation.setGouvernorat(gouvernorat);
                selected.setDelegation(delegation);
            }

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getGsm(), selected.getId());

            if (errorMsg == false) {


                if (selected.getPays() != null) {
                    selected.setIdPays(selected.getPays().getId());
                    selected.setLibellePays(selected.getPays().getLibelle());

                }
                
                    if (selected.getGouvernorat() != null) {
                        selected.setIdGouvernorat(selected.getGouvernorat().getId());
                        selected.setLibelleGouvernorat(selected.getGouvernorat().getLibelle());

                        if (selected.getDelegation() != null) {
                            selected.setIdDelegation(selected.getDelegation().getId());
                            selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                        }
                    }

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
            System.out.println("Erreur- ClientController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<ClientPVT> temps = ejbFacadeClientPVT.findAll("where o.idClient = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- ClientController - performDestroy: " + e.getMessage());
        }
    }

    public List<Gouvernorat> getListGouvernorat() {
        return listGouvernorat;
    }

    public void setListGouvernorat(List<Gouvernorat> listGouvernorat) {
        this.listGouvernorat = listGouvernorat;
    }

    public List<Delegation> getListDelegation() {
        return listDelegation;
    }

    public void setListDelegation(List<Delegation> listDelegation) {
        this.listDelegation = listDelegation;
    }

    public void changedGouvernorat() {
        if (selected.getPays() != null) {
            listGouvernorat = selected.getPays().getListeGouvernorat();
        } else {
            listGouvernorat = null;
        }
    }

    public void changedDeligation() {
        if (selected.getGouvernorat() != null) {
            listDelegation = selected.getGouvernorat().getListDelegations();
        } else {
            listDelegation = null;
        }
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public SelectItem[] getItemsAvailableSelectOneGouvernerat() {
        if (listGouvernorat == null) {
            listGouvernorat = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listGouvernorat, true);
    }

    public SelectItem[] getItemsAvailableSelectOneDelegation() {
        if (listDelegation == null) {
            listDelegation = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listDelegation, true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Client getClient(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Client.class)
    public static class ClientControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClientController controller = (ClientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clientController");
            return controller.getClient(getKey(value));
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
            if (object instanceof Client) {
                Client o = (Client) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Client.class.getName());
            }
        }

    }

}
