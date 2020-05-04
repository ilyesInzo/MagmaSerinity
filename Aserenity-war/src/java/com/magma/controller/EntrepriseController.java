package com.magma.controller;

import com.magma.bibliotheque.FileUploadController;
import com.magma.bibliotheque.FonctionsString;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.TraitementImage;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Delegation;
import com.magma.entity.Entreprise;
import com.magma.entity.Gouvernorat;
import com.magma.entity.ParametrageEntreprise;
import com.magma.entity.Utilisateur;
import com.magma.session.DelegationFacadeLocal;
import com.magma.session.EntrepriseFacadeLocal;
import com.magma.session.ParametrageEntrepriseFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name= "entrepriseController")
@SessionScoped
public class EntrepriseController implements Serializable {

    private Entreprise selected;
    private Entreprise selectedSingle;
    private List<Entreprise> items = null;
    @EJB
    private EntrepriseFacadeLocal ejbFacade;
    @EJB
    private ParametrageEntrepriseFacadeLocal ejbFacadeParametrageEntreprise;
    private boolean errorMsg;
    private Long idTemp;
    private Entreprise entreprise;
    private long idEntreprise = 0;
    private Utilisateur utilisateur;
    private List<Delegation> listeDelegations = null;
    @EJB
    private DelegationFacadeLocal ejbFacadeDelegation;
    private UploadedFile fileRegistre;
    private StreamedContent photo;
    private boolean boolFichier = false;

    public EntrepriseController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (entreprise.getIdEntrepriseSuivi() != null && entreprise.getIdEntrepriseSuivi() != 0) {
                idEntreprise = entreprise.getIdEntrepriseSuivi();
            } else {
                idEntreprise = entreprise.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GEntreprise", "MParametrage", null,utilisateur);

            System.out.println("Path : " + System.getProperty("com.sun.aas.installRoot"));
            System.out.println("Path : " + System.getProperty("com.sun.aas.domainsRoot"));
            System.out.println("Path : " + System.getProperty("com.sun.aas.instanceRoot"));
            System.out.println("Path : " + System.getProperty("catalina.base"));

            //MenuTemplate.menuFonctionnalitesModules("GEntreprise", utilisateur);
            /*if (entreprise.getIdEntrepriseSuivi() != null && entreprise.getIdEntrepriseSuivi() != 0) {
                idEntreprise = entreprise.getIdEntrepriseSuivi();
            } else {
                idEntreprise = entreprise.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../entreprise/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        errorMsg = false;
        fileRegistre = null;
    }

    public List<Entreprise> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- EntrepriseController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Entreprise getSelected() {
        return selected;
    }

    public void setSelected(Entreprise selected) {
        this.selected = selected;
    }

    public Entreprise getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Entreprise selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private EntrepriseFacadeLocal getFacade() {
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
        selected = new Entreprise();
        listeDelegations = null;
        errorMsg = false;
        fileRegistre = null;
        selected.setParametrageEntreprise(new ParametrageEntreprise());
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {
                if (selected.getGouvernorat() != null) {
                    selected.setIdGouvernorat(selected.getGouvernorat().getId());
                    selected.setLibelleGouvernorat(selected.getGouvernorat().getLibelle());
                }
                if (selected.getDelegation() != null) {
                    selected.setIdDelegation(selected.getDelegation().getId());
                    selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                }

                if (fileRegistre != null) {
                    selected.setLogo(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle().trim() + "_" + System.currentTimeMillis()+fileRegistre.getFileName().substring(fileRegistre.getFileName().lastIndexOf("."))));
                    try {
                        InputStream img = fileRegistre.getInputstream();
                        InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, selected.getLogo());
                        boolean res = FileUploadController.uploderFichier(selected.getLogo(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                        if (res == false) {
                            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                            return null;
                        }
                    } catch (Exception e) {
                        System.out.println("EntrepriseController - create:   " + e.getMessage());
                        selected.setLogo("../resources/images/company.png");
                    }
                } else {
                    selected.setLogo("../resources/images/company.png");
                }

                getFacade().create(selected);
                ejbFacadeParametrageEntreprise.create(selected.getParametrageEntreprise());
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EntrepriseController - create: " + e.getMessage());
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

            Gouvernorat gouvernorat = new Gouvernorat();
            if (selected.getIdGouvernorat() != null) {
                gouvernorat = new Gouvernorat();
                gouvernorat.setId(selected.getIdGouvernorat());
                gouvernorat.setLibelle(selected.getLibelleGouvernorat());
                selected.setGouvernorat(gouvernorat);
            }
            listeDelegations = ejbFacadeDelegation.findAllNative(" where o.Gov_Id = " + selected.getIdGouvernorat() + " ");

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
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());

            if (errorMsg == false) {
                if (selected.getGouvernorat() != null) {
                    selected.setIdGouvernorat(selected.getGouvernorat().getId());
                    selected.setLibelleGouvernorat(selected.getGouvernorat().getLibelle());
                }
                if (selected.getDelegation() != null) {
                    selected.setIdDelegation(selected.getDelegation().getId());
                    selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                }

                

                if (fileRegistre != null) {

                    if (selected.getLogo() != null && selected.getLogo().contains("/resources/images/") == false) {
                        File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getLogo());
                        file.delete();
                    }

                    selected.setLogo(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle().trim() + "_" + selected.getDateCreation().getTime() +fileRegistre.getFileName().substring(fileRegistre.getFileName().lastIndexOf("."))));

                    try {
                        InputStream img = fileRegistre.getInputstream();
                        InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre.getFileName());
                        boolean res = FileUploadController.uploderFichier(selected.getLogo(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                        if (res == false) {
                            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                            return null;
                        }

                    } catch (Exception e) {
                        System.out.println("EntrepriseController - update:   " + e.getMessage());
                        selected.setLogo("../resources/images/ompany.png");
                    }

                } else if (selected.getLogo() == null || (selected.getLogo().contains("/resources/images/") == true)) {
                    selected.setLogo("../resources/images/ompany.png");
                }

                getFacade().edit(selected);
                
                ejbFacadeParametrageEntreprise.edit(selected.getParametrageEntreprise());
                
               /* if (selected.getParametrageEntreprise() != null) {
                    selected.setIdDelegation(selected.getDelegation().getId());
                    selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                }*/
                
                return prepareList();
                //}
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getLibelle() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- EntrepriseController - update: " + e.getMessage());
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
            System.out.println("Erreur- EntrepriseController - performDestroy: " + e.getMessage());
        }
    }

    public Object dynamicPhoto() throws FileNotFoundException, IOException {

        if (fileRegistre != null) {

            FileUploadController.uploderFichier(fileRegistre.getFileName(), fileRegistre.getInputstream(), LireParametrage.returnValeurParametrage("urlImageNonPersistant"));

            File file = new File(LireParametrage.returnValeurParametrage("urlImageNonPersistant") + fileRegistre.getFileName());
            photo = new DefaultStreamedContent(new FileInputStream(file));

            return photo;

        } else if (selected.getLogo() != null && selected.getLogo().contains("/resources/images/") == false) {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getLogo());
            if (file.exists() == false) {
                return "../resources/images/company.png";
            }
            photo = new DefaultStreamedContent(new FileInputStream(file));
            return photo;

        } else {
            return "../resources/images/company.png";
        }

    }

    public Object pathPhoto() throws FileNotFoundException, IOException {

        if (selected.getLogo() == null || selected.getLogo().contains("/resources/images/")) {
            return "../resources/images/company.png";
        } else {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getLogo());

            if (file.exists() == false) {

                return "../resources/images/company.png";

            }
            photo = new DefaultStreamedContent(new FileInputStream(file));
            return photo;
        }

    }

    public Object getLogoEntreprise() throws FileNotFoundException, IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String img = context.getExternalContext().getRequestParameterMap().get("image");
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + img);

            if (file.exists() == false) {
                InputStream iStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/company.png");
                return new DefaultStreamedContent(iStream);
            }
            photo = new DefaultStreamedContent(new FileInputStream(file));
        }
        return photo;
    }

    public void uploadRegistre(FileUploadEvent event) {
        fileRegistre = event.getFile();
    }

    public StreamedContent getPhoto() {
        return photo;
    }

    public void setPhoto(StreamedContent photo) {
        this.photo = photo;
    }

    public boolean isBoolFichier() {
        return boolFichier;
    }

    public void setBoolFichier(boolean boolFichier) {
        this.boolFichier = boolFichier;
    }

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<Delegation> getListeDelegations() {
        return listeDelegations;
    }

    public void setListeDelegations(List<Delegation> listeDelegations) {
        this.listeDelegations = listeDelegations;
    }

    public void changedDelegation() {
        listeDelegations = new ArrayList<>();
        listeDelegations = selected.getGouvernorat().getListDelegations();
    }

    public SelectItem[] getItemsAvailableSelectOneDelegation() {
        if (listeDelegations == null) {
            listeDelegations = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listeDelegations, true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Entreprise getEntreprise(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Entreprise.class)
    public static class EntrepriseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EntrepriseController controller = (EntrepriseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "entrepriseController");
            return controller.getEntreprise(getKey(value));
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
            if (object instanceof Entreprise) {
                Entreprise o = (Entreprise) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Entreprise.class.getName());
            }
        }

    }

}
