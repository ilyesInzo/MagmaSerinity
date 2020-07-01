/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.bibliotheque.OperationString;
import com.magma.entity.Utilisateur;
import java.io.IOException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author inzo
 */
public class SmartSession implements PhaseListener {

    @Override
    public void beforePhase(PhaseEvent pe) {
        FacesContext context = pe.getFacesContext();
        ExternalContext ext = context.getExternalContext();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Utilisateur utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
        HttpServletRequest request = (HttpServletRequest) ext.getRequest();
        if (!request.getRequestURI().toLowerCase().contains("login.xhtml") && !request.getRequestURI().toLowerCase().contains("index.xhtml") && !request.getRequestURI().toLowerCase().contains("css") && request.getRequestURI().toLowerCase().contains("xhtml")) {
            if (utilisateur == null) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml");

                } catch (IOException ex) {
                    System.out.println(" SmartSession Erroor redirection ==> " + ex.getMessage());
                }
            } else {
                // utilisateur connecter => on vérifie
                try {
                    //PhaseId id = pe.getPhaseId(); 
                    int a = request.getRequestURI().lastIndexOf("/");
                    String repository = request.getRequestURI().substring(a + 1);
                    String fileName = OperationString.returnFileName(repository);
                    repository = request.getRequestURI().substring(1, a);
                    a = repository.lastIndexOf("/");
                    //req = request.getRequestURI().substring(0, a);
                    repository = repository.substring(a + 1);

                    if (utilisateur.getProfile().toPrivilege(repository) != null) {

                        boolean load = true;

                        switch (fileName) {

                            case "Create":
                                load = utilisateur.getProfile().toPrivilege(repository).isCreation();
                                break;
                            case "Edit":
                                load = utilisateur.getProfile().toPrivilege(repository).isModification();
                                break;
                            case "List":
                            case "ListVisiteSecteur":
                                load = utilisateur.getProfile().toPrivilege(repository).isLecture();
                                break;
                            case "View":
                            case "ViewVisiteSecteur":
                                load = utilisateur.getProfile().toPrivilege(repository).isLecture();
                                break;
                            default: load = true;

                        }

                        if (!load) {
                                FacesContext.getCurrentInstance().getExternalContext().redirect("../erreur/access.xhtml");
                        }

                    }

                    // all the url => System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer"));
                } catch (Exception ex) {
                    System.out.println(" SmartSession Erroor Autorization ==> " + ex.getMessage());
                }

            }
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
