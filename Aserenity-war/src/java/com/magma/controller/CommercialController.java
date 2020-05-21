package com.magma.controller;

import com.magma.controller.util.JsfUtil;
import com.magma.entity.CategorieClient;
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

@ManagedBean(name = "commercialController")
@SessionScoped
public class CommercialController implements Serializable {

    private Commercial selected;
    private Commercial selectedSingle;
    private List<Commercial> items = null;
    @EJB
    private CommercialFacadeLocal ejbFacade;
    @EJB
    private DelegationFacadeLocal ejbFacadeDelegation;
    @EJB

    private GouvernoratFacadeLocal ejbFacadeGouvernorat;
    @EJB
    private PaysFacadeLocal ejbFacadePays;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    private boolean errorMsg;
    private Long idTemp;
    private Utilisateur commercial;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;

    private List<Pays> listePays = null;
    private List<Gouvernorat> listeGouvernorat = null;
    private List<Delegation> listeDelegation = null;
    private List<CategorieClient> listeCategorieClient = null;

    private List<Gouvernorat> listeGouvernoratSelected = null;
    private List<Delegation> listeDelegationSelected = null;
    private List<CategorieClient> listeCategorieClientSelected = null;

    private Pays selectedPays = null;
    private Gouvernorat selectedGouvernorat = null;
    private Delegation selectedDelegation = null;
    private CategorieClient selectedCategorieClient = null;

    private List<Client> source;
    private List<Client> target;
    private List<Client> listClientCommercials;

    private DualListModel dualList;

    public CommercialController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (commercial.getIdEntrepriseSuivi() != null && commercial.getIdEntrepriseSuivi() != 0) {
         idEntreprise = commercial.getIdEntrepriseSuivi();
         } else {
         idEntreprise = commercial.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GCommercial", "MCommercial", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GCommercial", utilisateur);
            /*if (commercial.getIdEntrepriseSuivi() != null && commercial.getIdEntrepriseSuivi() != 0) {
             idEntreprise = commercial.getIdEntrepriseSuivi();
             } else {
             idEntreprise = commercial.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../commercial/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
    }

    public List<Commercial> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.nom asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- CommercialController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Commercial getSelected() {
        return selected;
    }

    public void setSelected(Commercial selected) {
        this.selected = selected;
    }

    public Commercial getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Commercial selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private CommercialFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        commercial = null;
        listClientCommercials = null;
        return "List";
    }

    public String prepareView() {
        if (selected != null) {

            if (selected.getSequenceClientID() != null && !"".equals(selected.getSequenceClientID())) {
                listClientCommercials = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 and Cli_Id  in (" + selected.getSequenceClientID() + ")");
            } else {
                listClientCommercials = new ArrayList<>();
            }

            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new Commercial();
        commercial = new Utilisateur();
        errorMsg = false;

        listeCategorieClient = new ArrayList<>();
        listeDelegation = new ArrayList<>();
        listeGouvernorat = new ArrayList<>();
        listePays = new ArrayList<>();

        listeCategorieClientSelected = new ArrayList<>();
        listeDelegationSelected = new ArrayList<>();
        listeGouvernoratSelected = new ArrayList<>();

        selectedPays = null;
        selectedGouvernorat = null;
        selectedDelegation = null;
        selectedCategorieClient = null;

        source = new ArrayList<Client>();
        source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0");

        target = new ArrayList<Client>();

        dualList = new DualListModel<Client>(source, target);

        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(commercial.getEmail().trim());
            if (errorMsg == false) {
                selected.setId(commercial.getId());
                selected.setCode(commercial.getCode());
                selected.setDateSynch(new Date().getTime());
                selected.setNom(commercial.getNom());
                selected.setPrenom(commercial.getPrenom());
                selected.setEmail(commercial.getEmail());
                selected.setGsm(commercial.getGsm());
                selected.setPasswd(commercial.getPasswd());
                selected.setEtatUsr(commercial.isEtatUsr());

                List<Client> listeClients = dualList.getTarget();
                // Affectation par client
                String clause = "";
                for (Client temp : listeClients) {
                    if (clause.equals("")) {
                        clause = temp.getId() + "";
                    } else {
                        clause = clause + "," + temp.getId();
                    }
                }

                selected.setSequenceClientID(clause);

                getFacade().create(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getEmail() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- CommercialController - create: " + e.getMessage());
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
            listeCategorieClient = new ArrayList<>();
            listeDelegation = new ArrayList<>();
            listeGouvernorat = new ArrayList<>();
            listePays = new ArrayList<>();

            listeCategorieClientSelected = new ArrayList<>();
            listeDelegationSelected = new ArrayList<>();
            listeGouvernoratSelected = new ArrayList<>();

            selectedPays = null;
            selectedGouvernorat = null;
            selectedDelegation = null;
            selectedCategorieClient = null;

            
            if (selected.getSequenceClientID() != null && !"".equals(selected.getSequenceClientID())) {
                 source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 and Cli_Id not  in (" + selected.getSequenceClientID() + ")");
            } else {
                 source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 ");
            }
            dualList = new DualListModel<Client>(source, listClientCommercials);

            return "Edit";
        }
        return "List";
    }

    public String update() {
        try {
            errorMsg = getFacade().verifierUnique(selected.getEmail().trim(), selected.getId());

            if (errorMsg == false) {
                
                List<Client> listeClients = dualList.getTarget();
                // Affectation par client
                String clause = "";
                for (Client temp : listeClients) {
                    if (clause.equals("")) {
                        clause = temp.getId() + "";
                    } else {
                        clause = clause + "," + temp.getId();
                    }
                }
                selected.setSequenceClientID(clause);

                getFacade().edit(selected);
                return prepareList();
                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getEmail() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- CommercialController - update: " + e.getMessage());
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
            System.out.println("Erreur- CommercialController - performDestroy: " + e.getMessage());
        }
    }

    //Multi selection
    public void changedPayss() {
        System.out.println("listePays" + listePays.size());
        if (listePays != null && !listePays.isEmpty()) {
            String clause = "";
            for (Pays temp : listePays) {
                if (clause.equals("")) {
                    clause = temp.getId() + "";
                } else {
                    clause = clause + "," + temp.getId();
                }
            }
            if (!clause.equals("")) {
                listeGouvernorat = ejbFacadeGouvernorat.findAllNative(" where o.Pys_Id in (" + clause + ") ");

            }

        } else {
            System.out.println("cc");
            listeGouvernorat = new ArrayList<>();
            listeDelegation = new ArrayList<>();
            listeGouvernoratSelected = new ArrayList<>();
            listeDelegationSelected = new ArrayList<>();

        }

        updateClientList();

    }

    public void changedGouvernorats() {
        System.out.println("listeGouvernorat " + listeGouvernoratSelected.size());
        if (listeGouvernoratSelected != null && !listeGouvernoratSelected.isEmpty()) {
            String clause = "";
            for (Gouvernorat temp : listeGouvernoratSelected) {
                if (clause.equals("")) {
                    clause = temp.getId() + "";
                } else {
                    clause = clause + "," + temp.getId();
                }
            }
            if (!clause.equals("")) {
                listeDelegation = ejbFacadeDelegation.findAllNative(" where o.Gov_Id in (" + clause + ") ");

            }

        } else {
            System.out.println("cc");
            listeDelegation = new ArrayList<>();
            listeDelegationSelected = new ArrayList<>();

        }
        updateClientList();
    }

    public void changedDelegations() {
        System.out.println("listeDelegation " + listeGouvernoratSelected.size());
        if (listeDelegationSelected != null && !listeDelegationSelected.isEmpty()) {
            String clause = "";
            for (Delegation temp : listeDelegationSelected) {
                if (clause.equals("")) {
                    clause = temp.getId() + "";
                } else {
                    clause = clause + "," + temp.getId();
                }
            }
            if (!clause.equals("")) {

            }

        } else {
            System.out.println("cc");

        }
        updateClientList();
    }

    private void updateClientList() {

        String clause = "";

        if (listePays != null && !listePays.isEmpty()) {
            String clausePays = "";
            for (Pays temp : listePays) {
                if (clausePays.equals("")) {
                    clausePays = temp.getId() + "";
                } else {
                    clausePays = clausePays + "," + temp.getId();
                }
            }

            if (listeGouvernoratSelected != null && !listeGouvernoratSelected.isEmpty()) {
                String clauseGouvernerat = "";
                for (Gouvernorat temp : listeGouvernoratSelected) {
                    if (clauseGouvernerat.equals("")) {
                        clauseGouvernerat = temp.getId() + "";
                    } else {
                        clauseGouvernerat = clauseGouvernerat + "," + temp.getId();
                    }
                }

                if (listeDelegationSelected != null && !listeDelegationSelected.isEmpty()) {
                    String clauseDelegation = "";
                    for (Delegation temp : listeDelegationSelected) {
                        if (clauseDelegation.equals("")) {
                            clauseDelegation = temp.getId() + "";
                        } else {
                            clauseDelegation = clauseDelegation + "," + temp.getId();
                        }
                    }

                    clause = "Pys_Id in (" + clausePays + ")" + " and " + "Gov_Id in (" + clauseGouvernerat + ")" + " and " + "Del_Id in (" + clauseDelegation + ")";

                } else {
                    clause = "Pys_Id in (" + clausePays + ")" + " and " + "Gov_Id in (" + clauseGouvernerat + ")";
                }

            } else {
                clause = "Pys_Id in (" + clausePays + ")";
            }

        }
        String clauseTarget = "";
        for (Client temp : target) {
            if (clauseTarget.equals("")) {
                clauseTarget = temp.getId() + "";
            } else {
                clauseTarget = clauseTarget + "," + temp.getId();
            }
        }

        clause = clauseTarget.equals("") ? clause : (clause + " and Cli_Id not in (" + clauseTarget + ")");
        System.out.println("clause" + clause);
        if (!clause.equals("")) {
            source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 and " + clause);
        } else {
            source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 ");
        }
        //target = new ArrayList<>();
        System.out.println("source " + source.size());
        dualList = new DualListModel<Client>(source, target);

    }

    // one selection
    public void changedGouvernorat() {
        if (selectedPays != null) {
            listeGouvernorat = selectedPays.getListeGouvernorat();
        } else {
            listeGouvernorat = null;
        }
    }

    public void changedDeligation() {
        if (selectedGouvernorat != null) {
            listeDelegation = selectedGouvernorat.getListDelegations();
        } else {
            listeDelegation = null;
        }
    }

    public void chargeClient() {

        String clause = "";

        if (selectedDelegation != null) {

            clause = "Del_Id = " + selectedDelegation.getId() + "";

        } else if (selectedGouvernorat != null) {

            clause = "Gov_Id = " + selectedGouvernorat.getId() + "";

        } else if (selectedPays != null) {

            clause = "Pys_Id = " + selectedPays.getId() + "";

        }

        if (selectedCategorieClient != null) {

            if (!clause.equals("")) {

                clause = clause + " and CCl_Id = " + selectedCategorieClient.getId() + "";

            } else {
                clause = " CCl_Id = " + selectedCategorieClient.getId() + "";
            }

        }

        String clauseTarget = "";
        List<Client> listeClients = dualList.getTarget();
        for (Client temp : listeClients) {
            if (clauseTarget.equals("")) {
                clauseTarget = temp.getId() + "";
            } else {
                clauseTarget = clauseTarget + "," + temp.getId();
            }
        }

        clause = clauseTarget.equals("") ? clause : (clause + " and Cli_Id not in (" + clauseTarget + ")");
        System.out.println("" + clause);
        if (!clause.equals("")) {
            source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 and " + clause);
        } else {
            source = ejbFacadeClient.findAllNative(" where o.Cli_Supprimer = 0 ");
        }
        System.out.println("" + source.size());
        dualList = new DualListModel<Client>(source, listeClients);
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Utilisateur getCommercial() {
        return commercial;
    }

    public void setCommercial(Utilisateur commercial) {
        this.commercial = commercial;
    }

    public List<Pays> getListePays() {
        return listePays;
    }

    public void setListePays(List<Pays> listePays) {
        this.listePays = listePays;
    }

    public List<Gouvernorat> getListeGouvernorat() {
        return listeGouvernorat;
    }

    public void setListeGouvernorat(List<Gouvernorat> listeGouvernorat) {
        this.listeGouvernorat = listeGouvernorat;
    }

    public List<Delegation> getListeDelegation() {
        return listeDelegation;
    }

    public void setListeDelegation(List<Delegation> listeDelegation) {
        this.listeDelegation = listeDelegation;
    }

    public List<CategorieClient> getListeCategorieClient() {
        return listeCategorieClient;
    }

    public void setListeCategorieClient(List<CategorieClient> listeCategorieClient) {
        this.listeCategorieClient = listeCategorieClient;
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

    public List<Gouvernorat> getListeGouvernoratSelected() {
        return listeGouvernoratSelected;
    }

    public void setListeGouvernoratSelected(List<Gouvernorat> listeGouvernoratSelected) {
        this.listeGouvernoratSelected = listeGouvernoratSelected;
    }

    public List<Delegation> getListeDelegationSelected() {
        return listeDelegationSelected;
    }

    public void setListeDelegationSelected(List<Delegation> listeDelegationSelected) {
        this.listeDelegationSelected = listeDelegationSelected;
    }

    public List<CategorieClient> getListeCategorieClientSelected() {
        return listeCategorieClientSelected;
    }

    public void setListeCategorieClientSelected(List<CategorieClient> listeCategorieClientSelected) {
        this.listeCategorieClientSelected = listeCategorieClientSelected;
    }

    public Pays getSelectedPays() {
        return selectedPays;
    }

    public void setSelectedPays(Pays selectedPays) {
        this.selectedPays = selectedPays;
    }

    public Gouvernorat getSelectedGouvernorat() {
        return selectedGouvernorat;
    }

    public void setSelectedGouvernorat(Gouvernorat selectedGouvernorat) {
        this.selectedGouvernorat = selectedGouvernorat;
    }

    public Delegation getSelectedDelegation() {
        return selectedDelegation;
    }

    public void setSelectedDelegation(Delegation selectedDelegation) {
        this.selectedDelegation = selectedDelegation;
    }

    public CategorieClient getSelectedCategorieClient() {
        return selectedCategorieClient;
    }

    public void setSelectedCategorieClient(CategorieClient selectedCategorieClient) {
        this.selectedCategorieClient = selectedCategorieClient;
    }

    public List<Client> getListClientCommercials() {
        return listClientCommercials;
    }

    public void setListClientCommercials(List<Client> listClientCommercials) {
        this.listClientCommercials = listClientCommercials;
    }

    public SelectItem[] getItemsAvailableSelectManyGouvernerat() {
        return JsfUtil.getSelectItems(listeGouvernorat, false);
    }

    public SelectItem[] getItemsAvailableSelectManyDelegation() {
        return JsfUtil.getSelectItems(listeDelegation, false);
    }

    public SelectItem[] getItemsAvailableSelectManyCategorieClient() {
        return JsfUtil.getSelectItems(listeCategorieClient, false);
    }

    public SelectItem[] getItemsAvailableSelectOneGouvernerat() {
        if (listeGouvernorat == null) {
            listeGouvernorat = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listeGouvernorat, true);
    }

    public SelectItem[] getItemsAvailableSelectOneDelegation() {
        if (listeDelegation == null) {
            listeDelegation = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listeDelegation, true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAllNative(" where o.Usr_EtatUsr = 1"), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAllNative(" where o.Usr_EtatUsr = 1"), true);
    }

    @FacesConverter(forClass = Commercial.class)
    public static class CommercialControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommercialController controller = (CommercialController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "commercialController");
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
            if (object instanceof Commercial) {
                Commercial o = (Commercial) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Commercial.class.getName());
            }
        }

    }

}
