package com.magma.controller;

import com.magma.bibliotheque.AlgorithmesCryptage;
import com.magma.bibliotheque.FileUploadController;
import com.magma.bibliotheque.FonctionsString;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.OperationEmail;
import com.magma.bibliotheque.TraitementImage;
import com.magma.entity.Utilisateur;
import com.magma.session.UtilisateurFacadeLocal;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Commercial;
import com.magma.entity.Delegation;
import com.magma.entity.Departement;
import com.magma.entity.Entreprise;
import com.magma.entity.Gouvernorat;
import com.magma.entity.Poste;
import com.magma.session.CommercialFacadeLocal;
import com.magma.session.DelegationFacadeLocal;
import com.magma.session.EntrepriseFacadeLocal;
import com.magma.session.PosteFacadeLocal;
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
import java.util.Objects;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.bean.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "utilisateurController")
@SessionScoped
public class UtilisateurController implements Serializable {

    private Utilisateur selected;
    private Utilisateur selectedSingle;
    private List<Utilisateur> items = null;
    @EJB
    private UtilisateurFacadeLocal ejbFacade;
    @EJB
    private DelegationFacadeLocal ejbFacadeDelegation;
    @EJB
    private PosteFacadeLocal ejbFacadePoste;
    @EJB
    private EntrepriseFacadeLocal ejbFacadeEntreprise;
    @EJB
    private CommercialFacadeLocal ejbFacadeCommercial;

    private List<Delegation> listeDelegations = null;
    private List<Poste> listePostes = null;
    private boolean errorMsg;
    private Long idTemp;
    private Utilisateur utilisateur;
    private long idEntreprise = 0;
    private boolean verifCompte;

    private UploadedFile fileRegistre;
    private StreamedContent photo;
    private boolean boolFichier = false;

    private String oldEmail;
    private String oldPwd;
    private String newPwd;
    private String confirmeNewPwd;

    public UtilisateurController() {
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (utilisateur.getIdEntrepriseSuivi() != null && utilisateur.getIdEntrepriseSuivi() != 0) {
         idEntreprise = utilisateur.getIdEntrepriseSuivi();
         } else {
         idEntreprise = utilisateur.getEntreprise().getId();
         }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GUtilisateur", "MParametrage", null, utilisateur);

            //MenuTemplate.menuFonctionnalitesModules("GUtilisateur", utilisateur);
            /*if (utilisateur.getIdEntrepriseSuivi() != null && utilisateur.getIdEntrepriseSuivi() != 0) {
             idEntreprise = utilisateur.getIdEntrepriseSuivi();
             } else {
             idEntreprise = utilisateur.getEntreprise().getId();
             }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../utilisateur/List.xhtml");
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

    public List<Utilisateur> getItems() {
        try {

            if (items == null) {

                // uniquement le superAdmin peut gerer son profile de super admin
                if (utilisateur.getProfile().isSuperAdmin()) {
                    items = getFacade().findAll("order by o.nom asc ");
                } else {
                    items = getFacade().findAll("where o.profile.superAdmin = 0 order by o.nom asc ");
                }

            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- UtilisateurController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Utilisateur getSelected() {
        return selected;
    }

    public void setSelected(Utilisateur selected) {
        this.selected = selected;
    }

    public Utilisateur getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Utilisateur selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private UtilisateurFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        oldEmail = null;
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {

        selected = new Utilisateur();

        List<Entreprise> listEntreprises = ejbFacadeEntreprise.findAll();

        if (!listEntreprises.isEmpty()) {

            selected.setEntreprise(listEntreprises.get(0));
            errorMsg = false;
            listeDelegations = null;
            listePostes = null;
            fileRegistre = null;
            selected.setGouvernorat(null);
            selected.setDelegation(null);
            return "Create";
        } else {
            return prepareList();
        }
    }

    public String create() {
        try {
            errorMsg = getFacade().verifierUnique(selected.getEmail().trim());
            if (errorMsg == false) {
                if (selected.getGouvernorat() != null) {
                    selected.setIdGouvernorat(selected.getGouvernorat().getId());
                    selected.setLibelleGouvernorat(selected.getGouvernorat().getLibelle());
                }
                if (selected.getDelegation() != null) {
                    selected.setIdDelegation(selected.getDelegation().getId());
                    selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                }

                if (selected.isEstEmploye() == true) {
                    if (selected.getPoste() != null) {
                        selected.setIdPoste(selected.getPoste().getId());
                        selected.setLibellePoste(selected.getPoste().getLibelle());
                    }
                    if (selected.getDepartement() != null) {
                        selected.setIdDepartement(selected.getDepartement().getId());
                        selected.setLibelleDepartement(selected.getDepartement().getLibelle());
                    }
                }

                if (fileRegistre != null) {
                    selected.setPhoto(FonctionsString.supprimerCaracteresSpeciaux(selected.getEmail() + "_" + System.currentTimeMillis() + fileRegistre.getFileName().substring(fileRegistre.getFileName().lastIndexOf("."))));
                    try {
                        InputStream img = fileRegistre.getInputstream();
                        InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, selected.getPhoto());
                        boolean res = FileUploadController.uploderFichier(selected.getPhoto(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                        if (res == false) {
                            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                            return null;
                        }
                    } catch (Exception e) {
                        System.out.println("UtilisateurController - create:   " + e.getMessage());
                        if (selected.getStatut().equals("Mme")) {
                            selected.setPhoto("../resources/images/femme.png");
                        } else {
                            selected.setPhoto("../resources/images/homme.png");
                        }
                    }
                } else {
                    if (selected.getStatut().equals("Mme")) {
                        selected.setPhoto("../resources/images/femme.png");
                    } else {
                        selected.setPhoto("../resources/images/homme.png");
                    }
                }

                String motpasse = FonctionsString.genereUnMot(8);
                selected.setPasswd(motpasse);
                getFacade().create(selected);

                prepareListApresCreation(motpasse, selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getEmail() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- UtilisateurController - create: " + e.getMessage());
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
            oldEmail = selected.getEmail();
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

            if (selected.isEstEmploye()) {
                Departement departement = new Departement();
                if (selected.getIdDepartement() != null) {
                    departement = new Departement();
                    departement.setId(selected.getIdDepartement());
                    departement.setLibelle(selected.getLibelleDepartement());
                    selected.setDepartement(departement);
                }
                listePostes = ejbFacadePoste.findAllNative(" where o.Dep_Id = " + selected.getIdDepartement() + " ");

                if (selected.getLibellePoste() != null && !selected.getLibellePoste().equals("")) {
                    Poste poste = new Poste();
                    poste.setId(selected.getIdPoste());
                    poste.setLibelle(selected.getLibellePoste());
                    poste.setDepartement(departement);
                    selected.setPoste(poste);
                }

            }

            return "Edit";
        }
        return "List";
    }

    public String update() {
        //try {
            errorMsg = getFacade().verifierUnique(selected.getEmail().trim(), selected.getId());

            if (errorMsg == false) {
                if (selected.getGouvernorat() != null) {
                    selected.setIdGouvernorat(selected.getGouvernorat().getId());
                    selected.setLibelleGouvernorat(selected.getGouvernorat().getLibelle());
                }
                if (selected.getDelegation() != null) {
                    selected.setIdDelegation(selected.getDelegation().getId());
                    selected.setLibelleDelegation(selected.getDelegation().getLibelle());
                }

                if (selected.isEstEmploye() == true) {
                    if (selected.getPoste() != null) {
                        selected.setIdPoste(selected.getPoste().getId());
                        selected.setLibellePoste(selected.getPoste().getLibelle());
                    }
                    if (selected.getDepartement() != null) {
                        selected.setIdDepartement(selected.getDepartement().getId());
                        selected.setLibelleDepartement(selected.getDepartement().getLibelle());
                    }
                } else {
                    selected.setIdPoste(null);
                    selected.setLibellePoste(null);
                    selected.setIdDepartement(null);
                    selected.setLibelleDepartement(null);
                }

                if (fileRegistre != null) {

                    if (selected.getPhoto() != null && selected.getPhoto().contains("/resources/images/") == false) {
                        File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto());
                        file.delete();
                    }

                    selected.setPhoto(FonctionsString.supprimerCaracteresSpeciaux(selected.getEmail() + "_" + selected.getDateCreation().getTime() + fileRegistre.getFileName().substring(fileRegistre.getFileName().lastIndexOf("."))));

                    try {
                        InputStream img = fileRegistre.getInputstream();
                        InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre.getFileName());
                        boolean res = FileUploadController.uploderFichier(selected.getPhoto(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                        if (res == false) {
                            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                            return null;
                        }

                    } catch (Exception e) {
                        System.out.println("UtilisateurController - update:   " + e.getMessage());
                        if (selected.getStatut().equals("Mme")) {
                            selected.setPhoto("../resources/images/femme.png");
                        } else {
                            selected.setPhoto("../resources/images/homme.png");
                        }
                    }

                } else if (selected.getPhoto() == null || (selected.getPhoto().contains("/resources/images/") == true)) {
                    if (selected.getStatut().equals("Mme")) {
                        selected.setPhoto("../resources/images/femme.png");
                    } else {
                        selected.setPhoto("../resources/images/homme.png");
                    }
                }

                // Le super admin peux changer son image de profile dans la gestion des utilisateurs
                if (Objects.equals(utilisateur.getId(), selected.getId())) {
                    utilisateur.setPhoto(selected.getPhoto());
                }

                getFacade().edit(selected);
                return prepareList();

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getEmail() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }
       /* } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- UtilisateurController - update: " + e.getMessage());
            return null;
        }*/
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
            if (!utilisateur.getProfile().isSuperAdmin() && !utilisateur.getId().equals(selectedSingle.getId())) {

                getFacade().remove(selectedSingle);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("Inapplicable")));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- UtilisateurController - performDestroy: " + e.getMessage());
        }
    }

    public void ActiverDesactiverMP() {
        //un utilisateur ne peut pas se desactiver
        if (selectedSingle != null) {

            if (!utilisateur.getId().equals(selectedSingle.getId())) {
                PrimeFaces.current().executeScript("PF('dlg2').show();");
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("Inapplicable")));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet")));
        }
    }

    public String activerCompte() {
        selectedSingle.setEtatUsr(verifCompte);
        getFacade().edit(selectedSingle);

        return prepareList();
    }

    public void prepareEditProfile() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            selected = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
            idTemp = selected.getId();

            if (selected.getIdGouvernorat() != null) {
                Gouvernorat gouvernorat = new Gouvernorat();
                gouvernorat.setId(selected.getIdGouvernorat());
                gouvernorat.setLibelle(selected.getLibelleGouvernorat());
                selected.setGouvernorat(gouvernorat);

                listeDelegations = ejbFacadeDelegation.findAllNative(" where o.Gov_Id = " + selected.getIdGouvernorat() + " ");

                if (selected.getLibelleDelegation() != null && !selected.getLibelleDelegation().equals("")) {
                    Delegation delegation = new Delegation();
                    delegation.setId(selected.getIdDelegation());
                    delegation.setLibelle(selected.getLibelleDelegation());
                    delegation.setGouvernorat(gouvernorat);
                    selected.setDelegation(delegation);
                }
            }

            oldEmail = selected.getEmail();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../account/EditProfile.xhtml");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String updateProfile() {

        try {

            errorMsg = getFacade().verifierUnique(selected.getEmail().trim(), selected.getId());

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

                    if (selected.getPhoto() != null && selected.getPhoto().contains("/resources/images/") == false) {
                        File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto());
                        file.delete();
                    }

                    selected.setPhoto(FonctionsString.supprimerCaracteresSpeciaux(selected.getEmail() + "_" + selected.getDateCreation().getTime() + fileRegistre.getFileName().substring(fileRegistre.getFileName().lastIndexOf("."))));

                    try {
                        InputStream img = fileRegistre.getInputstream();
                        InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre.getFileName());
                        boolean res = FileUploadController.uploderFichier(selected.getPhoto(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                        if (res == false) {
                            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                            return null;
                        }

                    } catch (Exception e) {
                        System.out.println("UtilisateurController - update:   " + e.getMessage());
                        if (selected.getStatut().equals("Mme")) {
                            return "../resources/images/femme.png";
                        } else {
                            return "../resources/images/homme.png";
                        }
                    }

                } else if (selected.getPhoto() == null || (selected.getPhoto().contains("/resources/images/") == true)) {
                    if (selected.getStatut().equals("Mme")) {
                        return "../resources/images/femme.png";
                    } else {
                        return "../resources/images/homme.png";
                    }
                }

                if (Objects.equals(utilisateur.getId(), selected.getId())) {
                    utilisateur.setPhoto(selected.getPhoto());
                }

                getFacade().edit(selected);
                if (!oldEmail.equals(selected.getEmail())) {
                    utilisateur.setEmail(selected.getEmail());
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", utilisateur);
                }

                return null;

            } else {
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), selected.getEmail() + " " + ResourceBundle.getBundle("/Bundle").getString("CeChampExist")));
                return null;
            }

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EchecOperation")));
            System.out.println("Erreur- UtilisateurController - update: " + e.getMessage());
            return null;
        }

    }

    public void preparedEditPwd() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            selected = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
            oldPwd = "";
            newPwd = "";
            confirmeNewPwd = "";
            FacesContext.getCurrentInstance().getExternalContext().redirect("../account/EditPwd.xhtml");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void testEmail() {
        prepareListApresReinitialiserMP("test", selected);
    }

    public String updatePwd() {
        String pwdClaire = newPwd;
        oldPwd = AlgorithmesCryptage.encoderEnMD5(oldPwd);
        newPwd = AlgorithmesCryptage.encoderEnMD5(newPwd);
        confirmeNewPwd = AlgorithmesCryptage.encoderEnMD5(confirmeNewPwd);
        if (oldPwd.equals(selected.getPasswd())) {
            if (newPwd.equals(confirmeNewPwd)) {
                /*selected.setPasswd(pwdClaire);
                 getFacade().edit(selected);*/
                prepareListApresReinitialiserMP(pwdClaire, selected);
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage("growlShow", new FacesMessage("Success", ResourceBundle.getBundle("/Bundle").getString("MDPUpdate")));
                return null;
            } else {
                oldPwd = "";
                newPwd = "";
                confirmeNewPwd = "";
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EntreesNonValides")));
                return null;
            }
        } else {
            oldPwd = "";
            newPwd = "";
            confirmeNewPwd = "";
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("EntreesNonValides")));
            return null;
        }

    }

    public String prepareListApresCreation(String motGenerer, Utilisateur userTemp) {
        String sujet = ResourceBundle.getBundle("/Bundle").getString("SubjectNouvCompte");
        String body = userTemp.getNomPrenom() + " " + ResourceBundle.getBundle("/Bundle").getString("MsgBienvenu") + ".\n\n";
        body = body + ResourceBundle.getBundle("/Bundle").getString("BodyCompte") + "\n\n";
        body = body + "\t" + ResourceBundle.getBundle("/Bundle").getString("Identifiant") + ": " + userTemp.getEmail() + "\n";
        body = body + "\t" + ResourceBundle.getBundle("/Bundle").getString("MotPass") + ": " + motGenerer + "\n";
        if (userTemp.getEntreprise() != null) {
            body = body + "\t" + ResourceBundle.getBundle("/Bundle").getString("Entreprise") + ": " + userTemp.getEntreprise().getLibelle() + "\n";
        }
        body = body + "https://www.primefaces.org\n\n";
        body = body + ResourceBundle.getBundle("/Bundle").getString("PlusDInfo") + ": inzodialo@gmail.com\n";
        OperationEmail operationEmail = new OperationEmail();
        operationEmail.envoieEmailNotification(userTemp.getEmail(), sujet, body);
        return prepareList();
    }

    public void prepareListApresReinitialiserMP(String motGenerer, Utilisateur userTemp) {
        String sujet = ResourceBundle.getBundle("/Bundle").getString("SubjectReinitialisationPWD");
        String body = userTemp.getNomPrenom() + " " + ResourceBundle.getBundle("/Bundle").getString("MsgReinitialisationPWD") + ".\n\n";
        body = body + ResourceBundle.getBundle("/Bundle").getString("BodyCompte") + "\n\n";
        body = body + "\t" + ResourceBundle.getBundle("/Bundle").getString("Identifiant") + ": " + userTemp.getEmail() + "\n";
        body = body + "\t" + ResourceBundle.getBundle("/Bundle").getString("MotPass") + ": " + motGenerer + "\n";
        if (userTemp.getEntreprise() != null) {
            body = body + "\t" + ResourceBundle.getBundle("/Bundle").getString("Entreprise") + ": " + userTemp.getEntreprise().getLibelle() + "\n";
        }

        body = body + "https://www.primefaces.org\n\n";
        body = body + ResourceBundle.getBundle("/Bundle").getString("PlusDInfo") + ": inzodialo@gmail.com\n";
        OperationEmail operationEmail = new OperationEmail();
        operationEmail.envoieEmailNotification(userTemp.getEmail(), sujet, body);
        userTemp.setPasswd(motGenerer);
        ejbFacade.edit(userTemp);
        Commercial commercial = ejbFacadeCommercial.find(userTemp.getId());

        if (commercial != null) {
            commercial.setPasswd(userTemp.getPasswd());
            ejbFacadeCommercial.edit(commercial);
        }
        //return prepareList();
    }

    // change the user MP
    public String ReinitialiserMP() {
        if (selectedSingle != null) {
            if (selectedSingle.isEtatUsr() == true) {
                String motpasse = FonctionsString.genereUnMot(8);

                prepareListApresReinitialiserMP(motpasse, selectedSingle);
                return prepareList();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("CeCompteDesactiver")));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("SelectionnerObjet")));
        }
        return prepareList();
    }

    public Object dynamicPhoto() throws FileNotFoundException, IOException {
        try {
            if (fileRegistre != null) {
                InputStream img = fileRegistre.getInputstream();
                InputStream is = TraitementImage.resizeImageMaxHeight(img, 225, fileRegistre.getFileName());
                FileUploadController.uploderFichier(fileRegistre.getFileName(), is, LireParametrage.returnValeurParametrage("urlImageNonPersistant"));
                File file = new File(LireParametrage.returnValeurParametrage("urlImageNonPersistant") + fileRegistre.getFileName());
                photo = new DefaultStreamedContent(new FileInputStream(file));
                return photo;
            } else if (selected.getPhoto() != null && selected.getPhoto().contains("/ressources/images/") == false) {
                File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto());
                if (file.exists() == false) {
                    if (selected.getStatut().equals("Mme")) {
                        return "../resources/images/femme.png";
                    } else {
                        return "../resources/images/homme.png";
                    }
                }
                photo = new DefaultStreamedContent(new FileInputStream(file));
                return photo;
            } else {
                if (selected != null && selected.getStatut() != null) {
                    if (selected.getStatut().equals("Mme")) {
                        return "../resources/images/femme.png";
                    } else {
                        return "../resources/images/homme.png";
                    }
                } else {
                    return "../resources/images/homme.png";
                }
            }
        } catch (Exception e) {
            System.out.println("------");
            return null;
        }
    }

    public Object pathPhoto() throws FileNotFoundException, IOException {
        if (selected.getPhoto() == null || selected.getPhoto().contains("/ressources/images/")) {
            if (selected.getStatut().equals("Mme")) {
                return "../resources/images/femme.png";
            } else {
                return "../resources/images/homme.png";
            }
        } else {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto());
            if (file.exists() == false) {
                if (selected.getStatut().equals("Mme")) {
                    return "../resources/images/femme.png";
                } else {
                    return "../resources/images/homme.png";
                }
            }
            photo = new DefaultStreamedContent(new FileInputStream(file));
            return photo;
        }
    }

    public Object getImageUtilisateur() throws FileNotFoundException, IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String img = context.getExternalContext().getRequestParameterMap().get("image");
            String statut = context.getExternalContext().getRequestParameterMap().get("statut");
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + img);

            if (file.exists() == false || img == null || img.isEmpty()) {
                if (statut.contains("Mr")) {
                    InputStream iStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/homme.png");
                    return new DefaultStreamedContent(iStream);
                } else {
                    InputStream iStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/femme.png");
                    return new DefaultStreamedContent(iStream);
                }

            }

            photo = new DefaultStreamedContent(new FileInputStream(file));
        }
        return photo;
    }

    public void uploadRegistre(FileUploadEvent event) {
        fileRegistre = event.getFile();
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

    public List<Poste> getListePostes() {
        return listePostes;
    }

    public void setListePostes(List<Poste> listePostes) {
        this.listePostes = listePostes;
    }

    public boolean isVerifCompte() {
        return verifCompte;
    }

    public void setVerifCompte(boolean verifCompte) {
        this.verifCompte = verifCompte;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getConfirmeNewPwd() {
        return confirmeNewPwd;
    }

    public void setConfirmeNewPwd(String confirmeNewPwd) {
        this.confirmeNewPwd = confirmeNewPwd;
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

    public UploadedFile getFileRegistre() {
        return fileRegistre;
    }

    public void setFileRegistre(UploadedFile fileRegistre) {
        this.fileRegistre = fileRegistre;
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

    public void changedPoste() {
        listePostes = new ArrayList<>();
        listePostes = selected.getDepartement().getPostes();
    }

    public SelectItem[] getItemsAvailableSelectOneCommercial() {
        return JsfUtil.getSelectItems(getFacade().findAllNative(" where o.Usr_Id not in (select e.Com_Id from T_Commercial as e) and o.Usr_EstEmploye = 1 "), true);
    }

    public SelectItem[] getItemsAvailableSelectOnePoste() {
        if (listePostes == null) {
            listePostes = new ArrayList<>();
        }
        return JsfUtil.getSelectItems(listePostes, true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Utilisateur getUtilisateur(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Utilisateur.class)
    public static class UtilisateurControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UtilisateurController controller = (UtilisateurController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "utilisateurController");
            return controller.getUtilisateur(getKey(value));
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
            if (object instanceof Utilisateur) {
                Utilisateur o = (Utilisateur) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Utilisateur.class.getName());
            }
        }

    }

}
