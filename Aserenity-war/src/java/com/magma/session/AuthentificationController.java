/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.bibliotheque.AlgorithmesCryptage;
import com.magma.bibliotheque.GenerationPdf;
import com.magma.bibliotheque.TraitementDate;
import com.magma.entity.Privileges;
import com.magma.entity.Utilisateur;
import com.magma.util.MenuTemplate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author inzo
 */
public class AuthentificationController implements Serializable {

    private String nomUtilisateur = "";
    private String pwdUtilisateur = "";
    private Utilisateur utilisateur;
    @EJB
    private UtilisateurFacadeLocal ejbFacade;

    public AuthentificationController() {
        nomUtilisateur = "";
        pwdUtilisateur = "";
    }

    public String connecter() {
        try {
            TraitementDate.dataBase = 2;
            FacesContext context = FacesContext.getCurrentInstance();

            //GenerationPdf.generationPdf();
            utilisateur = ejbFacade.findUserByEmail(nomUtilisateur);

            if (utilisateur != null) {

                String pwd = AlgorithmesCryptage.encoderEnMD5(pwdUtilisateur);

                if (utilisateur.getPasswd().equals(pwd)) {

                    if (utilisateur.isEtatUsr() == true) {
                        MenuTemplate.initMenu();
                        utilisateur.setVisibiliteMCommande("display:none;");

                        utilisateur.setVisibiliteMVentes("display:none;");
                        utilisateur.setVisibiliteSousMVentes("display:none;");
                        
                        utilisateur.setVisibiliteMProduit("display:none;");
                        utilisateur.setVisibiliteSousMProduit("display:none;");
                        
                        utilisateur.setVisibiliteMStockArticle("display:none;");
                        
                        utilisateur.setVisibiliteMClient("display:none;");
                        
                        utilisateur.setVisibiliteMCommercial("display:none;");
                        utilisateur.setVisibiliteSousMCommercial("display:none;");
                        
                        utilisateur.setVisibiliteMJourneaux("display:none;");
                        utilisateur.setVisibiliteMParametrage("display:none;");
                        
                        utilisateur.setVisibiliteMVeille("display:none;");
                        
                        Map<String, Privileges> mapPrivilege;

                        // the key may be replaced by the repo where the xhtml file are stored
                        // we will use this map to enable/desiable accessing page when using the url
                        // and to access directly the concerned privilege
                        mapPrivilege = utilisateur.getProfile().getListPrivileges().stream().collect(
                                Collectors.toMap(Privileges::getLocation, x -> x));

                        utilisateur.getProfile().setMapPrivilege(mapPrivilege);

                        for (Privileges privilege : utilisateur.getProfile().getListPrivileges()) {

                            if (privilege.isLecture() == true) {
                                context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");

                            } else {
                                context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                            }
                            
                            if (privilege.isLecture() == true && privilege.getOrderModule() == 1 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMCommande("");

                                // if (privilege.isLecture() == true) {
                                //    context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                                // } else {
                                //      context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                                // }
                            }


                            if (privilege.isLecture() == true && privilege.getOrderModule() == 2 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMVentes("");
                                if (privilege.isSousModule() == true) {
                                    utilisateur.setVisibiliteSousMVentes("");
                                }
                                // if (privilege.isLecture() == true) {
                                //    context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                                // } else {
                                //      context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                                // }
                            }
                            
                            if (privilege.isLecture() == true && privilege.getOrderModule() == 6 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMCommercial("");
                                if (privilege.isSousModule() == true) {
                                    utilisateur.setVisibiliteSousMCommercial("");
                                }
                                // if (privilege.isLecture() == true) {
                                //    context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                                // } else {
                                //      context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                                // }
                            }

                            if (privilege.isLecture() == true && privilege.getOrderModule() == 60 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMProduit("");
                                if (privilege.isSousModule() == true) {
                                    utilisateur.setVisibiliteSousMProduit("");
                                }
                                // if (privilege.isLecture() == true) {
                                //    context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "");
                                // } else {
                                //     context.getExternalContext().getSessionMap().put("FV" + privilege.getLibelle(), "display: none;");
                                // }
                            }
                            
                            if (privilege.isLecture() == true && privilege.getOrderModule() == 7 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMVeille("");
                            }

                            if (privilege.isLecture() == true && privilege.getOrderModule() == 13 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMStockArticle("");
                            }
                            if (privilege.isLecture() == true && privilege.getOrderModule() == 62 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMClient("");
                            }

                            if (privilege.isLecture() == true && privilege.getOrderModule() == 90 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMParametrage("");
                            }

                            if (privilege.isLecture() == true && privilege.getOrderModule() == 91 && privilege.isModuleActiver() == true) {
                                utilisateur.setVisibiliteMJourneaux("");
                            }

                        }

                        context.getExternalContext().getSessionMap().put("user", utilisateur);

                        return "Acceuil";

                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Echec"), ResourceBundle.getBundle("/Bundle").getString("CompteDesactiver")));
                        nomUtilisateur = "";
                        pwdUtilisateur = "";
                        return null;
                    }

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Echec") + " : ", ResourceBundle.getBundle("/Bundle").getString("ErreurAuthentification")));
                    nomUtilisateur = "";
                    pwdUtilisateur = "";
                    return null;
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Echec") + " : ", ResourceBundle.getBundle("/Bundle").getString("ErreurAuthentification")));
                nomUtilisateur = "";
                pwdUtilisateur = "";
                return null;
            }

        } catch (Exception ex) {
            System.out.println("Connection Error/ " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ResourceBundle.getBundle("/Bundle").getString("Echec"), ResourceBundle.getBundle("/Bundle").getString("ErreurSystem")));
            nomUtilisateur = "";
            pwdUtilisateur = "";
            return null;
        }
    }

    public String deconnecter() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            Utilisateur temp = (Utilisateur) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
            /*Connections connections = temp.getConnections();
            connections.setDateDeconnection(new Date());
            ejbFacadeConnection.edit(connections);*/
            session.removeAttribute("user");
            // already invalidated by the session counter listner
            session.invalidate();
        }
        return "Deconnexion";
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPwdUtilisateur() {
        return pwdUtilisateur;
    }

    public void setPwdUtilisateur(String pwdUtilisateur) {
        this.pwdUtilisateur = pwdUtilisateur;
    }

    public String acceuil() {

        MenuTemplate.menuFonctionnalitesModules("", utilisateur);

        return "Acceuil";
    }

}
