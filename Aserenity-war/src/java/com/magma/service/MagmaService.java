/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.service;

import com.google.gson.Gson;
import com.magma.bibliotheque.LireParametrage;
import com.magma.entity.*;
import com.magma.session.ArticleFacadeLocal;
import com.magma.session.CategorieFacadeLocal;
import com.magma.session.CommercialFacadeLocal;
import com.magma.session.EntrepriseFacadeLocal;
import com.magma.session.EtatCommandeFacadeLocal;
import com.magma.session.GouvernoratFacadeLocal;
import com.magma.webService.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

/**
 *
 * @author inzo
 */
@Path("/MagmaService")
@RequestScoped
public class MagmaService {

    @EJB
    private CommercialFacadeLocal ejbFacadeCommercial;
    @EJB
    private EntrepriseFacadeLocal ejbFacadeEntreprise;
    @EJB
    private GouvernoratFacadeLocal ejbFacadeGouvernorat;
    @EJB
    private CategorieFacadeLocal ejbFacadeCategorie;
    @EJB
    private ArticleFacadeLocal ejbFacadeArticle;
    @EJB
    private EtatCommandeFacadeLocal ejbFacadeEtatCommande;

    @GET
    @Path("/synchroniser")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String hallo() {
        return "cc";
    }

    private UtilisateurWS initUtilisateurWS(String login, int typeUtilisateur) {
        UtilisateurWS utilisateurWS = new UtilisateurWS();
        utilisateurWS.setId(0l);
        utilisateurWS.setLogin(login);
        utilisateurWS.setCode("");
        utilisateurWS.setStatut("");
        utilisateurWS.setNom("");
        utilisateurWS.setPrenom("");
        utilisateurWS.setGsm("");
        utilisateurWS.setEmail("");
        utilisateurWS.setType(typeUtilisateur);
        utilisateurWS.setEtatCompte(0);
        utilisateurWS.setIdEntreprise(0l);
        utilisateurWS.setLibelleEntreprise("");
        utilisateurWS.setModuleArticles(false);
        utilisateurWS.setModuleSuggestions(false);
        utilisateurWS.setModuleVeilles(false);
        utilisateurWS.setModuleCommerciale(false);
        utilisateurWS.setModuleProduction(false);
        utilisateurWS.setModuleActualites(false);
        utilisateurWS.setModuleCommande(false);
        utilisateurWS.setModuleReclamations(false);
        utilisateurWS.setModuleClient(false);
        utilisateurWS.setModuleVente(false);
        return utilisateurWS;
    }

    @GET
    @Path("/autentification/{login}/{pwd}/{typeUtilisateur}/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public UtilisateurWS authentification(@PathParam("login") String login, @PathParam("pwd") String pwd, @PathParam("typeUtilisateur") int typeUtilisateur, @PathParam("dateSynch") long dateSynch) {

        UtilisateurWS utilisateurWS = initUtilisateurWS(login, typeUtilisateur);
        try {
            System.err.println(" ==> autentification smartProspect");

            List<Commercial> utilisateurs = ejbFacadeCommercial.findAllNative(" where o.Usr_Email like '" + login + "'  ");
            if (utilisateurs != null && !utilisateurs.isEmpty()) {
                if (utilisateurs.get(0).getPasswd() != null && utilisateurs.get(0).getPasswd().equals(pwd)) {
                    if (utilisateurs.get(0).isEtatUsr() == true) {
                        utilisateurWS.setId(utilisateurs.get(0).getId());
                        utilisateurWS.setLogin(login);
                        utilisateurWS.setCode(utilisateurs.get(0).getCode());
                        utilisateurWS.setStatut("");
                        utilisateurWS.setNom(utilisateurs.get(0).getNom());
                        utilisateurWS.setPrenom(utilisateurs.get(0).getPrenom());
                        utilisateurWS.setGsm(utilisateurs.get(0).getGsm());
                        utilisateurWS.setEmail(utilisateurs.get(0).getEmail());
                        utilisateurWS.setType(1);
                        utilisateurWS.setEtatCompte(0);
                        return utilisateurWS;
                    } else {
                        utilisateurWS.setEtatCompte(1);
                        return utilisateurWS;
                    }
                } else {
                    utilisateurWS.setEtatCompte(2);
                    return utilisateurWS;
                }
            } else {
                utilisateurWS.setEtatCompte(3);
                return utilisateurWS;
            }

        } catch (Exception e) {
            System.out.println("MagmaService autentification ERREUR : " + e.getMessage());
            utilisateurWS.setEtatCompte(4);
            return utilisateurWS;
        }

    }

    @POST
    @Path("/Authentification")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response postAuthentification(String authentificationJson) {

        Gson gson = new Gson();
        UtilisateurWS utilisateurWS = gson.fromJson(authentificationJson, UtilisateurWS.class);

        try {

            List<Commercial> utilisateurs = ejbFacadeCommercial.findAllNative(" where o.Usr_Email like '" + utilisateurWS.getLogin() + "'  ");
            if (utilisateurs != null && !utilisateurs.isEmpty()) {
                if (utilisateurs.get(0).getPasswd() != null && utilisateurs.get(0).getPasswd().equals(utilisateurWS.getPasswd())) {
                    if (utilisateurs.get(0).isEtatUsr() == true) {
                        utilisateurWS.setId(utilisateurs.get(0).getId());
                        utilisateurWS.setCode(utilisateurs.get(0).getCode());
                        utilisateurWS.setStatut("");
                        utilisateurWS.setNom(utilisateurs.get(0).getNom());
                        utilisateurWS.setPrenom(utilisateurs.get(0).getPrenom());
                        utilisateurWS.setGsm(utilisateurs.get(0).getGsm());
                        utilisateurWS.setEmail(utilisateurs.get(0).getEmail());
                        utilisateurWS.setType(1);
                        utilisateurWS.setEtatCompte(0);
                        return Response.status(Response.Status.OK).entity(utilisateurWS).build();
                    } else {
                        utilisateurWS.setEtatCompte(1);
                        return Response.status(Response.Status.OK).entity(utilisateurWS).build();
                    }
                } else {
                    utilisateurWS.setEtatCompte(2);
                    return Response.status(Response.Status.OK).entity(utilisateurWS).build();
                }
            } else {
                utilisateurWS.setEtatCompte(3);
                return Response.status(Response.Status.OK).entity(utilisateurWS).build();
            }

        } catch (Exception e) {
            System.out.println("MagmaService autentification ERREUR : " + e.getMessage());
            utilisateurWS.setEtatCompte(4);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }

    }

    @GET
    @Path("/synchroniserGouvernerats/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<GouverneratWS> synchroniserGouvernerats(@PathParam("dateSynch") long dateSynch) {
        List<GouverneratWS> listeGouverneratWS = new ArrayList<>();
        List<Gouvernorat> listGouvernerat;
        // try {
        System.out.println("==> synchroniserGouvernerats");
        if (dateSynch > 0) {
            listGouvernerat = ejbFacadeGouvernorat.findAll("where o.dateSynch  > " + dateSynch + " ");
        } else {
            listGouvernerat = ejbFacadeGouvernorat.findAll();
        }
        if (listGouvernerat != null && !listGouvernerat.isEmpty()) {
            for (Gouvernorat gouvernorat : listGouvernerat) {
                GouverneratWS gouverneratWS = new GouverneratWS();
                gouverneratWS.setId(gouvernorat.getId());
                gouverneratWS.setLibelle(gouvernorat.getLibelle());
                gouverneratWS.setDescription(gouvernorat.getDescription());
                gouverneratWS.setListDelegationWSs(new ArrayList<DelegationWS>());
                gouverneratWS.setDateSynch(gouvernorat.getDateSynch());
                if (gouvernorat.getListDelegations() != null && !gouvernorat.getListDelegations().isEmpty()) {
                    for (Delegation delegation : gouvernorat.getListDelegations()) {
                        DelegationWS delegationWS = new DelegationWS();
                        delegationWS.setId(delegation.getId());
                        delegationWS.setLibelle(delegation.getLibelle());
                        delegationWS.setDescription(delegation.getDescription());
                        delegationWS.setDateSynch(delegation.getDateSynch());
                        gouverneratWS.getListDelegationWSs().add(delegationWS);
                    }
                }
                listeGouverneratWS.add(gouverneratWS);
            }
            return listeGouverneratWS;
        } else {
            return listeGouverneratWS;
        }
        /*   } catch (Exception e) {
         System.out.println("MagmaService synchroniserGouvernerats ERREUR : " + e.getMessage());
         return listeGouverneratWS;
         }*/
    }

    @GET
    @Path("/synchroniserCategorieArticles/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<CategorieWS> synchroniserCategorieArticles(@PathParam("dateSynch") long dateSynch) {
        List<CategorieWS> listeCategorieWS = new ArrayList<>();
        List<Categorie> listCategorie;
        try {
            System.out.println("==> synchroniserGouvernerats");
            if (dateSynch > 0) {
                listCategorie = ejbFacadeCategorie.findAll("where o.dateSynch  > " + dateSynch + " ");
            } else {
                listCategorie = ejbFacadeCategorie.findAll();
            }
            if (listCategorie != null && !listCategorie.isEmpty()) {
                for (Categorie categorie : listCategorie) {
                    CategorieWS categorieWS = new CategorieWS();
                    categorieWS.setId(categorie.getId());
                    categorieWS.setLibelle(categorie.getLibelle());
                    categorieWS.setDernierRang(categorie.isDernierRang());
                    categorieWS.setActiver(categorie.isActiver());
                    categorieWS.setRang(categorie.getRang());
                    if (categorie.getParent() != null) {
                        categorieWS.setIdParent(categorie.getParent().getId());
                    }
                    categorieWS.setSupprimer(categorie.isSupprimer());
                    categorieWS.setIdPremierParent(categorie.getIdPremierParent());
                    categorieWS.setIdSuiteParent(categorie.getIdSuiteParent());
                    categorieWS.setLibelleSuiteParent(categorie.getLibelleSuiteParentString());
                    categorieWS.setDateSynch(categorie.getDateSynch());
                    listeCategorieWS.add(categorieWS);
                }
                return listeCategorieWS;
            } else {
                return listeCategorieWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserCategorieArticles ERREUR : " + e.getMessage());
            return listeCategorieWS;
        }
    }

    @GET
    @Path("/synchroniserArticles/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ArticleWS> synchroniserArticles(@PathParam("dateSynch") long dateSynch) {
        List<ArticleWS> articlesWS = new ArrayList<>();
        try {
            List<Article> articles;
            if (dateSynch > 0) {
                articles = ejbFacadeArticle.findAll(" where  o.dateSynch > " + dateSynch + "");
            } else {
                articles = ejbFacadeArticle.findAll();
            }
            if (articles != null && !articles.isEmpty()) {
                for (Article article : articles) {
                    ArticleWS articleWS = new ArticleWS();
                    articleWS.setId(article.getId());
                    articleWS.setCode(article.getCode());
                    articleWS.setLibelle(article.getLibelle());

                    articleWS.setPrixRevendeur(article.getPrixRevendeur());
                    articleWS.setTva(BigDecimal.valueOf(article.getTva().getValeur()));
                    articleWS.setDescription(article.getDescription());

                    articleWS.setSupprimer(article.isSupprimer());
                    articleWS.setDateSynch(article.getDateSynch());

                    articleWS.setIdCategorie(article.getCategorie().getId());
                    articleWS.setLibelleCategorie(article.getCategorie().getLibelle());

                    try {
                        File image1 = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + article.getPhoto1());
                        if (image1.exists() == true) {
                            articleWS.setPhoto1("images/" + article.getPhoto1());
                        } else {
                            articleWS.setPhoto1("../resources/images/article.jpg");
                        }
                    } catch (Exception e) {
                        articleWS.setPhoto1("../resources/images/article.jpg");
                        System.out.println("MagmaService synchroniserArticles ERREUR PHOTO 1: " + e.getMessage());
                    }
                    try {
                        File image2 = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + article.getPhoto2());
                        if (image2.exists() == true) {
                            articleWS.setPhoto2("images/" + article.getPhoto2());
                        } else {
                            articleWS.setPhoto2("../resources/images/article.jpg");
                        }
                    } catch (Exception e) {
                        articleWS.setPhoto2("../resources/images/article.jpg");
                        System.out.println("MagmaService synchroniserArticles ERREUR PHOTO 2: " + e.getMessage());
                    }
                    try {
                        File image3 = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + article.getPhoto3());
                        if (image3.exists() == true) {
                            articleWS.setPhoto3("images/" + article.getPhoto3());
                        } else {
                            articleWS.setPhoto3("../resources/images/article.jpg");
                        }
                    } catch (Exception e) {
                        articleWS.setPhoto3("../resources/images/article.jpg");
                        System.out.println("MagmaService synchroniserArticles ERREUR PHOTO 3: " + e.getMessage());
                    }
                    try {
                        File image4 = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + article.getPhoto4());
                        if (image4.exists() == true) {
                            articleWS.setPhoto4("images/" + article.getPhoto4());
                        } else {
                            articleWS.setPhoto4("../resources/images/article.jpg");
                        }
                    } catch (Exception e) {
                        articleWS.setPhoto4("../resources/images/article.jpg");
                        System.out.println("MagmaService synchroniserArticles ERREUR PHOTO 4: " + e.getMessage());
                    }
                    try {
                        File image5 = new File(LireParametrage.returnValeurParametrage("urlUmploadPhoto") + article.getPhoto5());
                        if (image5.exists() == true) {
                            articleWS.setPhoto5("images/" + article.getPhoto5());
                        } else {
                            articleWS.setPhoto5("../resources/images/article.jpg");
                        }
                    } catch (Exception e) {
                        articleWS.setPhoto5("../resources/images/article.jpg");
                        System.out.println("MagmaService synchroniserArticles ERREUR PHOTO 5: " + e.getMessage());
                    }

                    articlesWS.add(articleWS);
                }
                return articlesWS;
            } else {
                return articlesWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserArticles ERREUR : " + e.getMessage());
            return articlesWS;
        }
    }

    @GET
    @Path("/synchroniserEtatCommande/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<EtatCommandeWS> synchroniserEtatCommande(@PathParam("dateSynch") long dateSynch) {
        List<EtatCommandeWS> listeEtatCommandeWS = new ArrayList<>();
        try {

            List<EtatCommande> listeEtatCommande;
            if (dateSynch > 0) {
                listeEtatCommande = ejbFacadeEtatCommande.findAll(" where  o.dateSynch > " + dateSynch + "");
            } else {
                listeEtatCommande = ejbFacadeEtatCommande.findAll();
            }

            if (listeEtatCommande != null && !listeEtatCommande.isEmpty()) {
                for (EtatCommande etatCommande : listeEtatCommande) {
                    EtatCommandeWS etatCommandeWS = new EtatCommandeWS();
                    etatCommandeWS.setId(etatCommande.getId());
                    etatCommandeWS.setLibelle(etatCommande.getLibelle());
                    etatCommandeWS.setRang(etatCommande.getRang());
                    etatCommandeWS.setCouleur(etatCommande.getCouleur());
                    etatCommandeWS.setSupprimer(etatCommande.isSupprimer());
                    etatCommandeWS.setDateSynch(etatCommande.getDateSynch());

                    etatCommandeWS.setDernierRang(etatCommande.isDernierRang());

                    if (etatCommande.getParent() != null) {
                        etatCommandeWS.setIdParent(etatCommande.getParent().getId());
                    }

                    listeEtatCommandeWS.add(etatCommandeWS);
                }
                return listeEtatCommandeWS;
            } else {
                return listeEtatCommandeWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserEtatCommande ERREUR : " + e.getMessage());
            return listeEtatCommandeWS;
        }

    }

}