package com.magma.controller;

import com.magma.bibliotheque.FileUploadController;
import com.magma.bibliotheque.FonctionsString;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.TraitementImage;
import com.magma.controller.util.JsfUtil;
import com.magma.entity.Article;
import com.magma.entity.Utilisateur;
import com.magma.session.ArticleFacadeLocal;
import com.magma.util.MenuTemplate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
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

@Named("articleController")
@SessionScoped
public class ArticleController implements Serializable {

    private Article selected;
    private Article selectedSingle;
    private List<Article> items = null;
    @EJB
    private ArticleFacadeLocal ejbFacade;
    private boolean errorMsg;
    private Long idTemp;
    private Article article;
    private long idEntreprise = 0;
    private String filterValue;
    private List<Article> initItems = null;
    private Utilisateur utilisateur;

    private UploadedFile fileRegistre;
    private UploadedFile fileRegistre2;
    private UploadedFile fileRegistre3;
    private UploadedFile fileRegistre4;
    private UploadedFile fileRegistre5;

    private List<String> images;

    private StreamedContent photo;
    private StreamedContent photo2;
    private StreamedContent photo3;
    private StreamedContent photo4;
    private StreamedContent photo5;

    private boolean boolFichier = false;
    private boolean showFilmstrip = false;

    public ArticleController() {
        initItems = null;
        items = null;
        errorMsg = false;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        /*if (article.getIdEntrepriseSuivi() != null && article.getIdEntrepriseSuivi() != 0) {
                idEntreprise = article.getIdEntrepriseSuivi();
            } else {
                idEntreprise = article.getEntreprise().getId();
            }*/
    }

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");

            MenuTemplate.menuFonctionnalitesModules("GArticle", "MProduit", null , utilisateur);

           // MenuTemplate.menuFonctionnalitesModules("GArticle", utilisateur);
           /* if (article.getIdEntrepriseSuivi() != null && article.getIdEntrepriseSuivi() != 0) {
                idEntreprise = article.getIdEntrepriseSuivi();
            } else {
                idEntreprise = article.getEntreprise().getId();
            }*/
            recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../article/List.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    private void recreateModel() {
        items = null;
        initItems = null;
        errorMsg = false;
        fileRegistre = null;
        fileRegistre2 = null;
        fileRegistre3 = null;
        fileRegistre4 = null;
        fileRegistre5 = null;
    }

    public List<Article> getItems() {
        try {
            if (items == null) {
                items = getFacade().findAll("order by o.libelle asc ");
                initItems = items;
            }
            return items;
        } catch (Exception e) {
            System.out.println("Erreur- ArticleController - getItems: " + e.getMessage());
            return null;
        }
    }

    public Article getSelected() {
        return selected;
    }

    public void setSelected(Article selected) {
        this.selected = selected;
    }

    public Article getSelectedSingle() {
        return selectedSingle;
    }

    public void setSelectedSingle(Article selectedSingle) {
        this.selectedSingle = selectedSingle;
    }

    private ArticleFacadeLocal getFacade() {
        return ejbFacade;
    }

    public void actualiserTab() {
        recreateModel();
    }

    public String prepareList() {
        recreateModel();
        selectedSingle = null;
        selected = null;
        images = new ArrayList<>();
        return "List";
    }

    public String prepareView() {
        if (selected != null) {
            
            images = new ArrayList<>();
            showFilmstrip = false;
            imagesArticles();
            
            
            return "View";
        }
        return "List";
    }

    public String prepareCreate() {
        selected = new Article();
        errorMsg = false;
        fileRegistre = null;
        fileRegistre2 = null;
        fileRegistre3 = null;
        fileRegistre4 = null;
        fileRegistre5 = null;
        return "Create";
    }

    public String create() {

        try {
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim());

            if (errorMsg == false) {
                
                
                
            if (fileRegistre != null) {
                selected.setPhoto1(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "1" + System.currentTimeMillis()+fileRegistre.getFileName().substring(fileRegistre.getFileName().lastIndexOf(".")));
                try {
                    InputStream img = fileRegistre.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto1(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("ArticleController - create:   " + e.getMessage());
                    selected.setPhoto1("../resources/images/article.jpg");
                }
            } else {
                selected.setPhoto1("../resources/images/article.jpg");
            }

            if (fileRegistre2 != null) {
                selected.setPhoto2(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "2" + System.currentTimeMillis()+fileRegistre2.getFileName().substring(fileRegistre2.getFileName().lastIndexOf(".")));
                try {
                    InputStream img = fileRegistre2.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre2.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto2(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("ArticleController - create:   " + e.getMessage());
                    selected.setPhoto2("../resources/images/article.jpg");
                }
            } else {
                selected.setPhoto2("../resources/images/article.jpg");
            }

            if (fileRegistre3 != null) {
                selected.setPhoto3(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "3" + System.currentTimeMillis()+fileRegistre3.getFileName().substring(fileRegistre3.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre3.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre3.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto3(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("ArticleController - create:   " + e.getMessage());
                    selected.setPhoto3("../resources/images/article.jpg");
                }
            } else {
                selected.setPhoto3("../resources/images/article.jpg");
            }

            if (fileRegistre4 != null) {
                selected.setPhoto4(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "4" + System.currentTimeMillis()+fileRegistre4.getFileName().substring(fileRegistre4.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre4.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre4.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto4(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("ArticleController - create:   " + e.getMessage());
                    selected.setPhoto4("../resources/images/article.jpg");
                }
            } else {
                selected.setPhoto4("../resources/images/article.jpg");
            }

            if (fileRegistre5 != null) {
                selected.setPhoto5(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "5" + System.currentTimeMillis()+fileRegistre5.getFileName().substring(fileRegistre5.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre5.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre5.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto5(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("ArticleController - create:   " + e.getMessage());
                    selected.setPhoto5("../resources/images/article.jpg");
                }
            } else {
                selected.setPhoto5("../resources/images/article.jpg");
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
            System.out.println("Erreur- ArticleController - create: " + e.getMessage());
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
            errorMsg = getFacade().verifierUnique(selected.getLibelle().trim(), selected.getId());

            if (errorMsg == false) {
                /* if (selected.getMontant().compareTo(BigDecimal.ZERO) == -1) {
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur"), " " + ResourceBundle.getBundle("/Bundle").getString("ValeurIncorrecte")));
                    return null;
                } else {*/
                //selected.setIdEntreprise(idEntreprise);
                
                
            if (fileRegistre != null) {

                if (selected.getPhoto1() != null && selected.getPhoto1().contains("/resources/images/") == false) {
                    File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto1());
                    file.delete();
                }

                selected.setPhoto1(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "1" + selected.getDateCreation().getTime()+ fileRegistre.getFileName().substring(fileRegistre.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto1(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }

                } catch (Exception e) {
                    System.out.println("ArticleController - update:   " + e.getMessage());
                    selected.setPhoto1("../resources/images/article.jpg");
                }

            } else if (selected.getPhoto1() == null || (selected.getPhoto1().contains("/resources/images/") == true)) {
                selected.setPhoto1("../resources/images/article.jpg");
            }

            if (fileRegistre2 != null) {

                if (selected.getPhoto2() != null && selected.getPhoto2().contains("/resources/images/") == false) {
                    File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto2());
                    file.delete();
                }

                selected.setPhoto2(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "2"+ selected.getDateCreation().getTime() + fileRegistre2.getFileName().substring(fileRegistre2.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre2.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre2.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto2(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }

                } catch (Exception e) {
                    System.out.println("ArticleController - update:   " + e.getMessage());
                    selected.setPhoto2("../resources/images/article.jpg");
                }
            } else if (selected.getPhoto2() == null || (selected.getPhoto2().contains("/resources/images/") == true)) {
                selected.setPhoto2("../resources/images/article.jpg");
            }

            if (fileRegistre3 != null) {

                if (selected.getPhoto3() != null && selected.getPhoto3().contains("/resources/images/") == false) {
                    File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto3());
                    file.delete();
                }

                selected.setPhoto3(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "3" + selected.getDateCreation().getTime()+ fileRegistre3.getFileName().substring(fileRegistre3.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre3.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre3.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto3(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }

                } catch (Exception e) {
                    System.out.println("ArticleController - update:   " + e.getMessage());
                    selected.setPhoto3("../resources/images/article.jpg");
                }
            } else if (selected.getPhoto3() == null || (selected.getPhoto3().contains("/resources/images/") == true)) {
                selected.setPhoto3("../resources/images/article.jpg");
            }

            if (fileRegistre4 != null) {

                if (selected.getPhoto4() != null && selected.getPhoto4().contains("/resources/images/") == false) {
                    File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto4());
                    file.delete();
                }

                selected.setPhoto4(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "4" + selected.getDateCreation().getTime()+ fileRegistre4.getFileName().substring(fileRegistre4.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre4.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre4.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto4(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }

                } catch (Exception e) {
                    System.out.println("ArticleController - update:   " + e.getMessage());
                    selected.setPhoto4("../resources/images/article.jpg");
                }
            } else if (selected.getPhoto4() == null || (selected.getPhoto4().contains("/resources/images/") == true)) {
                selected.setPhoto4("../resources/images/article.jpg");
            }

            if (fileRegistre5 != null) {

                if (selected.getPhoto5() != null && selected.getPhoto5().contains("/resources/images/") == false) {
                    File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto5());
                    file.delete();
                }

                selected.setPhoto5(FonctionsString.supprimerCaracteresSpeciaux(selected.getLibelle()) + "5" + selected.getDateCreation().getTime()+ fileRegistre5.getFileName().substring(fileRegistre5.getFileName().lastIndexOf(".")));

                try {
                    InputStream img = fileRegistre5.getInputstream();
                    InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre5.getFileName());
                    boolean res = FileUploadController.uploderFichier(selected.getPhoto5(), is, LireParametrage.returnValeurParametrage("urlUmploadPhoto"));
                    if (res == false) {
                        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Erreur") + ": ", ResourceBundle.getBundle("/Bundle").getString("ErreurUploadFile")));
                        return null;
                    }

                } catch (Exception e) {
                    System.out.println("ArticleController - update:   " + e.getMessage());
                    selected.setPhoto5("../resources/images/article.jpg");
                }
            } else if (selected.getPhoto5() == null || (selected.getPhoto5().contains("/resources/images/") == true)) {
                selected.setPhoto5("../resources/images/article.jpg");
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
            System.out.println("Erreur- ArticleController - update: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        if (selectedSingle != null) {
            //List<ArticlePVT> temps = ejbFacadeArticlePVT.findAll("where o.idArticle = " + selectedSingle.getId() + " ");
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
            System.out.println("Erreur- ArticleController - performDestroy: " + e.getMessage());
        }
    }

    public Object getImageArticle() throws FileNotFoundException, IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
            String img = context.getExternalContext().getRequestParameterMap().get("image");
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + img);

            if (file.exists() == false || img==null || img.isEmpty()) {
                InputStream iStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/noimage.png");
                return new DefaultStreamedContent(iStream);
            }
            photo = new DefaultStreamedContent(new FileInputStream(file));
        }
        return photo;
    }

    public void filterList() {
        items = new ArrayList<>();
        if (filterValue != null && !filterValue.equals("")) {
            for (Article item : initItems) {
                if (item.getLibelle().toLowerCase().contains(filterValue.toLowerCase()) || item.getCode().contains(filterValue)) {
                    items.add(item);
                }
            }
        } else {
            items = initItems;
        }
    }
    
    public Object dynamicPhoto() throws FileNotFoundException, IOException {
        if (fileRegistre != null) {
            InputStream img = fileRegistre.getInputstream();
            InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre.getFileName());
            FileUploadController.uploderFichier(fileRegistre.getFileName(), is, LireParametrage.returnValeurParametrage("urlImageNonPersistant"));
            File file = new File(LireParametrage.returnValeurParametrage("urlImageNonPersistant") + fileRegistre.getFileName());
            photo = new DefaultStreamedContent(new FileInputStream(file));
            return photo;

        } else if (selected.getPhoto1() != null && selected.getPhoto1().contains("/resources/images/") == false) {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto1());
            if (file.exists() == false) {
                return "../resources/images/noimage.png";
            }
            photo = new DefaultStreamedContent(new FileInputStream(file));
            return photo;

        } else {
            return "../resources/images/noimage.png";
        }

    }

    public Object dynamicPhoto2() throws FileNotFoundException, IOException {

        if (fileRegistre2 != null) {
            InputStream img = fileRegistre2.getInputstream();
            InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre2.getFileName());
            FileUploadController.uploderFichier(fileRegistre2.getFileName(), is, LireParametrage.returnValeurParametrage("urlImageNonPersistant"));
            File file = new File(LireParametrage.returnValeurParametrage("urlImageNonPersistant") + fileRegistre2.getFileName());
            photo2 = new DefaultStreamedContent(new FileInputStream(file));
            return photo2;

        } else if (selected.getPhoto2() != null && selected.getPhoto2().contains("/resources/images/") == false) {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto2());
            if (file.exists() == false) {
                return "../resources/images/noimage.png";
            }
            photo2 = new DefaultStreamedContent(new FileInputStream(file));
            return photo2;

        } else {
            return "../resources/images/noimage.png";
        }

    }

    public Object dynamicPhoto3() throws FileNotFoundException, IOException {

        if (fileRegistre3 != null) {
            InputStream img = fileRegistre3.getInputstream();
            InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre3.getFileName());
            FileUploadController.uploderFichier(fileRegistre3.getFileName(), is, LireParametrage.returnValeurParametrage("urlImageNonPersistant"));
            File file = new File(LireParametrage.returnValeurParametrage("urlImageNonPersistant") + fileRegistre3.getFileName());
            photo3 = new DefaultStreamedContent(new FileInputStream(file));
            return photo3;

        } else if (selected.getPhoto3() != null && selected.getPhoto3().contains("/resources/images/") == false) {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto3());
            if (file.exists() == false) {
                return "../resources/images/noimage.png";
            }
            photo3 = new DefaultStreamedContent(new FileInputStream(file));
            return photo3;

        } else {
            return "../resources/images/noimage.png";
        }

    }

    public Object dynamicPhoto4() throws FileNotFoundException, IOException {

        if (fileRegistre4 != null) {
            InputStream img = fileRegistre4.getInputstream();
            InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre4.getFileName());
            FileUploadController.uploderFichier(fileRegistre4.getFileName(), is, LireParametrage.returnValeurParametrage("urlImageNonPersistant"));
            File file = new File(LireParametrage.returnValeurParametrage("urlImageNonPersistant") + fileRegistre4.getFileName());
            photo4 = new DefaultStreamedContent(new FileInputStream(file));
            return photo4;

        } else if (selected.getPhoto4() != null && selected.getPhoto4().contains("/resources/images/") == false) {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto4());
            if (file.exists() == false) {
                return "../resources/images/noimage.png";
            }
            photo4 = new DefaultStreamedContent(new FileInputStream(file));
            return photo4;

        } else {
            return "../resources/images/noimage.png";
        }

    }

    public Object dynamicPhoto5() throws FileNotFoundException, IOException {

        if (fileRegistre5 != null) {
            InputStream img = fileRegistre5.getInputstream();
            InputStream is = TraitementImage.resizeImageMaxHeight(img, 300, fileRegistre5.getFileName());
            FileUploadController.uploderFichier(fileRegistre5.getFileName(), is, LireParametrage.returnValeurParametrage("urlImageNonPersistant"));
            File file = new File(LireParametrage.returnValeurParametrage("urlImageNonPersistant") + fileRegistre5.getFileName());
            photo5 = new DefaultStreamedContent(new FileInputStream(file));
            return photo5;

        } else if (selected.getPhoto5() != null && selected.getPhoto5().contains("/resources/images/") == false) {
            File file = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + selected.getPhoto5());
            if (file.exists() == false) {
                return "../resources/images/noimage.png";
            }
            photo5 = new DefaultStreamedContent(new FileInputStream(file));
            return photo5;

        } else {
            return "../resources/images/noimage.png";
        }

    }
    
    public List<String> imagesArticles() {
        int i = 0;
        if (selected.getPhoto1()!= null && !selected.getPhoto1().contains("resources")) {
            images.add(selected.getPhoto1());
            i++;
        }
        if (selected.getPhoto2()!= null && !selected.getPhoto2().contains("resources")) {
            images.add(selected.getPhoto2());
            i++;
        }
        if (selected.getPhoto3()!= null && !selected.getPhoto3().contains("resources")) {
            images.add(selected.getPhoto3());
            i++;
        }
        if (selected.getPhoto4()!= null && !selected.getPhoto4().contains("resources")) {
            images.add(selected.getPhoto4());
            i++;
        }
        if (selected.getPhoto5()!= null && !selected.getPhoto5().contains("resources")) {
            images.add(selected.getPhoto5());
            i++;
        }
        if (i > 0) {
            showFilmstrip = true;
        }
        if (i == 0) {
            images.add("../resources/images/noimage.png"); // image par defaut(dans le serveur ) insérer dans le composent galeria dans le cas ou il est vide
        }

        return images;
    }
    
    public void uploadRegistre1(FileUploadEvent event) {
        fileRegistre = event.getFile();
    }

    public void uploadRegistre2(FileUploadEvent event) {
        fileRegistre2 = event.getFile();
    }

    public void uploadRegistre3(FileUploadEvent event) {
        fileRegistre3 = event.getFile();
    }

    public void uploadRegistre4(FileUploadEvent event) {
        fileRegistre4 = event.getFile();
    }

    public void uploadRegistre5(FileUploadEvent event) {
        fileRegistre5 = event.getFile();
    }
    
    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ArticleFacadeLocal getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(ArticleFacadeLocal ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public long getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(long idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public List<Article> getInitItems() {
        return initItems;
    }

    public void setInitItems(List<Article> initItems) {
        this.initItems = initItems;
    }

    public UploadedFile getFileRegistre() {
        return fileRegistre;
    }

    public void setFileRegistre(UploadedFile fileRegistre) {
        this.fileRegistre = fileRegistre;
    }

    public boolean isBoolFichier() {
        return boolFichier;
    }

    public void setBoolFichier(boolean boolFichier) {
        this.boolFichier = boolFichier;
    }

    public boolean isShowFilmstrip() {
        return showFilmstrip;
    }

    public void setShowFilmstrip(boolean showFilmstrip) {
        this.showFilmstrip = showFilmstrip;
    }
    
    
    
    public UploadedFile getFileRegistre2() {
        return fileRegistre2;
    }

    public void setFileRegistre2(UploadedFile fileRegistre2) {
        this.fileRegistre2 = fileRegistre2;
    }

    public UploadedFile getFileRegistre3() {
        return fileRegistre3;
    }

    public void setFileRegistre3(UploadedFile fileRegistre3) {
        this.fileRegistre3 = fileRegistre3;
    }

    public UploadedFile getFileRegistre4() {
        return fileRegistre4;
    }

    public void setFileRegistre4(UploadedFile fileRegistre4) {
        this.fileRegistre4 = fileRegistre4;
    }

    public UploadedFile getFileRegistre5() {
        return fileRegistre5;
    }

    public void setFileRegistre5(UploadedFile fileRegistre5) {
        this.fileRegistre5 = fileRegistre5;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;

    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Article getArticle(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Article.class)
    public static class ArticleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ArticleController controller = (ArticleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "articleController");
            return controller.getArticle(getKey(value));
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
            if (object instanceof Article) {
                Article o = (Article) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Article.class.getName());
            }
        }

    }

}
