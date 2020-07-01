package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.CategorieClient;
import com.magma.entity.ClassificationClient;
import com.magma.entity.Client;
import com.magma.entity.Utilisateur;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.ClassificationClientFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.model.DualListModel;

@ManagedBean(name = "classificationClientController")
@SessionScoped
public class ClassificationClientController implements Serializable {

    private ClassificationClient selected;
    private ClassificationClient selectedSingle;
    private List<ClassificationClient> items = null;
    @EJB
    private ClassificationClientFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private ClassificationClient classificationClient;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    private List<Client> source;
    private List<Client> target;
    private DualListModel dualList;
    @EJB
    private CategorieClientFacadeLocal ejbFacadeCategorieClient;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    private List<CategorieClient> listCategorieClient = null;

    public ClassificationClientController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (classificationClient.getIdEntrepriseSuivi() != null && classificationClient.getIdEntrepriseSuivi() != 0) {
         idEntreprise = classificationClient.getIdEntrepriseSuivi();
         } else {
         idEntreprise = classificationClient.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GClassificationClient", "MClient", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GClassificationClient", utilisateur);
            /*if (classificationClient.getIdEntrepriseSuivi() != null && classificationClient.getIdEntrepriseSuivi() != 0) {
             idEntreprise = classificationClient.getIdEntrepriseSuivi();
             } else {
             idEntreprise = classificationClient.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../classificationClient/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<ClassificationClient> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("where o.supprimer = 0 order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- ClassificationClientController - getItems: " + e.getMessage());
            return null;
        }
    }

    public ClassificationClient getSelected() {
        return selected;
    }

    public void setSelected(ClassificationClient selected) {
        this.selected = selected;
    }

    public ClassificationClient getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(ClassificationClient selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private ClassificationClientFacadeLocal getFacade() {
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
        selected = new ClassificationClient();
        errorMsg = false;

        listCategorieClient = null;
        source = new ArrayList<Client>();
        source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 and o.CClt_Id is null ");//and o.Cli_ClientSmartSeller = 1

        target = new ArrayList<Client>();

        dualList = new DualListModel<Client>(source, target);

        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {
                creationInfo();
                // if (dualList.getTarget().isEmpty() == false) {
                List<Client> listeClients = dualList.getTarget();
                // List<ClientsParClassification> listClientsParClassificationsTemp = new ArrayList<>();

                selected.setDateSynch(new Date().getTime());
                selected.setSupprimer(false);

                getFacade().create(selected);

                for (Client client : listeClients) {

                    /*ClientsParClassification clientsParClassification = new ClientsParClassification();
                     //puisque un client ne peut pas etre dans 2 classe en méme temps
                     clientsParClassification.setId(clientTemp.getId());
                     clientsParClassification.setIdClient(clientTemp.getId());
                     clientsParClassification.setCodeClient(clientTemp.getCode());
                     clientsParClassification.setLibelleClient(clientTemp.getLibelle());*/
                    client.setClassificationClient(selected);

                    // listClientsParClassificationsTemp.add(clientsParClassification);
                }
                ejbFacadeClient.edit(listeClients);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- ClassificationClientController - create: " + e.getMessage());
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

            source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 and o.CClt_Id is null ");

            target = selected.getListClients();

            dualList = new DualListModel<Client>(source, target);

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());

            if (errorMsg == false) {
                editionInfo();
                //if (dualList.getTarget().isEmpty() == false) {
                List<Client> listeClients = dualList.getTarget();

                for (Client clientTemp : listeClients) {

                    clientTemp.setClassificationClient(selected);

                }
                ejbFacadeClient.edit(listeClients);

                selected.setDateSynch(new Date().getTime());
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
            System.out.println("Erreur- ClassificationClientController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            performDestroy();
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
            System.out.println("Erreur- ClassificationClientController - performDestroy: " + e.getMessage());
        }
    }

    public void changeClients() {

        String clauseCategorie = "";

        if (listCategorieClient != null && !listCategorieClient.isEmpty()) {
            String tempsIgType = "";
            for (int i = 0; i < listCategorieClient.size(); i++) {
                if (i == 0) {
                    tempsIgType = listCategorieClient.get(i).getId().toString() + "";
                } else {
                    tempsIgType = tempsIgType + ", " + listCategorieClient.get(i).getId().toString();
                }
            }
            clauseCategorie = " o.CCl_Id in (" + tempsIgType + ") ";
        }

        String clause = "";

        if (!clauseCategorie.equals("")) {
            clause = " and " + clauseCategorie;
        }

        source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 " + clause);

        target = new ArrayList<Client>();

        dualList = new DualListModel<Client>(source, target);

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

    public List<Client> getSource() {
        return source;
    }

    public void setSource(List<Client> source) {
        this.source = source;
    }

    public List<Client> getTarget() {
        return target;
    }

    public void setTarget(List<Client> target) {
        this.target = target;
    }

    public DualListModel getDualList() {
        return dualList;
    }

    public void setDualList(DualListModel dualList) {
        this.dualList = dualList;
    }

    public DualListModel getClients() {

//        dualList = new DualListModel<Article>(source, target);
        return dualList;
    }

    public void setClients(DualListModel dualList) {
        this.dualList = dualList;
    }

    public List<CategorieClient> getListCategorieClient() {
        return listCategorieClient;
    }

    public void setListCategorieClient(List<CategorieClient> listCategorieClient) {
        this.listCategorieClient = listCategorieClient;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll("where o.supprimer = 0 order by o.libelle asc "), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll("where o.supprimer = 0 order by o.libelle asc "), true);
    }

    @FacesConverter(forClass = ClassificationClient.class)
    public static class ClassificationClientControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ClassificationClientController controller = (ClassificationClientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "classificationClientController");
            return controller.ejbFacade.find(getKey(value));
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
            if (object instanceof ClassificationClient) {
                ClassificationClient o = (ClassificationClient) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ClassificationClient.class.getName());
            }
        }

    }

}
