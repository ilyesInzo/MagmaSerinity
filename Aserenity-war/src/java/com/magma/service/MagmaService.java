/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.service;

import com.google.gson.Gson;
import com.magma.bibliotheque.LireParametrage;
import com.magma.bibliotheque.TraitementDate;
import com.magma.entity.*;
import com.magma.session.ArticleFacadeLocal;
import com.magma.session.CategorieClientFacadeLocal;
import com.magma.session.CategorieFacadeLocal;
import com.magma.session.ClientFacadeLocal;
import com.magma.session.CommercialFacadeLocal;
import com.magma.session.DelegationFacadeLocal;
import com.magma.session.EntrepriseFacadeLocal;
import com.magma.session.EtatCommandeFacadeLocal;
import com.magma.session.GouvernoratFacadeLocal;
import com.magma.session.PaysFacadeLocal;
import com.magma.session.PlanificationVisiteFacadeLocal;
import com.magma.session.ProspectionFacadeLocal;
import com.magma.session.RapportVisitArticleFacadeLocal;
import com.magma.session.RapportVisitFacadeLocal;
import com.magma.webService.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    private PaysFacadeLocal ejbFacadePays;
    @EJB
    private GouvernoratFacadeLocal ejbFacadeGouvernorat;
    @EJB
    private DelegationFacadeLocal ejbFacadeDelegation;
    @EJB
    private CategorieFacadeLocal ejbFacadeCategorie;
    @EJB
    private ArticleFacadeLocal ejbFacadeArticle;
    @EJB
    private EtatCommandeFacadeLocal ejbFacadeEtatCommande;
    
    @EJB
    private CategorieClientFacadeLocal ejbFacadeCategorieClient;
    @EJB
    private ClientFacadeLocal ejbFacadeClient;
    
    @EJB
    private PlanificationVisiteFacadeLocal ejbPlanificationVisite;
    
    @EJB
    private ProspectionFacadeLocal ejbProspection;
    
    @EJB
    private RapportVisitFacadeLocal ejbRapportVisit;
    
    @EJB
    private RapportVisitArticleFacadeLocal ejbRapportVisitArticle;
    
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
                        utilisateurWS.setSequenceClientID(utilisateurs.get(0).getSequenceClientID());
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
    @Path("/synchroniserPayss/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<PaysWS> synchroniserPayss(@PathParam("dateSynch") long dateSynch) {
        List<PaysWS> listePaystWS = new ArrayList<>();
        List<Pays> listePayss = null;
        try {
            System.err.println("==> synchroniserPayss");
            if (dateSynch > 0) {
                listePayss = ejbFacadePays.findAll(" where  o.dateSynch > " + dateSynch + "");
            } else {
                listePayss = ejbFacadePays.findAll();
            }
            
            if (listePayss != null && !listePayss.isEmpty()) {
                for (Pays categorieClient : listePayss) {
                    PaysWS PaysWS = new PaysWS();
                    PaysWS.setId(categorieClient.getId());
                    PaysWS.setLibelle(categorieClient.getLibelle());
                    PaysWS.setDateSynch(categorieClient.getDateSynch());
                    listePaystWS.add(PaysWS);
                }
                return listePaystWS;
            } else {
                return listePaystWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserPayss ERREUR : " + e.getMessage());
            return listePaystWS;
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
                
                if (gouvernorat.getPays() != null) {
                    gouverneratWS.setIdPays(gouvernorat.getPays().getId());
                }
                gouverneratWS.setDateSynch(gouvernorat.getDateSynch());
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
    @Path("/synchroniserDelegations/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<DelegationWS> synchroniserDelegations(@PathParam("dateSynch") long dateSynch) {
        List<DelegationWS> listeDelegationtWS = new ArrayList<>();
        List<Delegation> listeDelegations = null;
        try {
            System.err.println("==> synchroniserDelegations");
            if (dateSynch > 0) {
                listeDelegations = ejbFacadeDelegation.findAll(" where  o.dateSynch > " + dateSynch + "");
            } else {
                listeDelegations = ejbFacadeDelegation.findAll();
            }
            
            if (listeDelegations != null && !listeDelegations.isEmpty()) {
                for (Delegation delegation : listeDelegations) {
                    DelegationWS delegationWS = new DelegationWS();
                    delegationWS.setId(delegation.getId());
                    delegationWS.setLibelle(delegation.getLibelle());
                    delegationWS.setDateSynch(delegation.getDateSynch());
                    delegationWS.setIdGouvernerat(delegation.getGouvernorat().getId());
                    listeDelegationtWS.add(delegationWS);
                }
                return listeDelegationtWS;
            } else {
                return listeDelegationtWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserDelegations ERREUR : " + e.getMessage());
            return listeDelegationtWS;
        }
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
    
    @GET
    @Path("/synchroniserCategorieClients/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<CategorieClientWS> synchroniserCategorieClients(@PathParam("dateSynch") long dateSynch) {
        List<CategorieClientWS> listeCategorieClientWS = new ArrayList<>();
        List<CategorieClient> listeCategorieClients = null;
        try {
            System.err.println("==> synchroniserCategorieClients");
            if (dateSynch > 0) {
                listeCategorieClients = ejbFacadeCategorieClient.findAll(" where  o.dateSynch > " + dateSynch + "");
            } else {
                listeCategorieClients = ejbFacadeCategorieClient.findAll();
            }
            
            if (listeCategorieClients != null && !listeCategorieClients.isEmpty()) {
                for (CategorieClient categorieClient : listeCategorieClients) {
                    CategorieClientWS categorieWS = new CategorieClientWS();
                    categorieWS.setId(categorieClient.getId());
                    categorieWS.setLibelle(categorieClient.getLibelle());
                    categorieWS.setDateSynch(categorieClient.getDateSynch());
                    listeCategorieClientWS.add(categorieWS);
                }
                return listeCategorieClientWS;
            } else {
                return listeCategorieClientWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserCategorieClients ERREUR : " + e.getMessage());
            return listeCategorieClientWS;
        }
    }
    
    @GET
    @Path("/synchroniserClientsByCommercial/{idCom}/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ClientWS> synchroniserClientsByCommercial(@PathParam("idCom") Long idCom, @PathParam("dateSynch") long dateSynch) {
        List<ClientWS> listeClientWS = new ArrayList<>();
        try {
            System.err.println("==> synchroniserClientsByCommercial");
            List<Client> listClient = null;
            
            List<Commercial> commerciaux = ejbFacadeCommercial.findAllNative(" where o.Com_Id = " + idCom + "  ");
            Commercial commercial = commerciaux.get(0);
            if (dateSynch > 0) {
                listClient = ejbFacadeClient.findAllNative(" where o.Cli_Id in ( " + commercial.getSequenceClientID() + ") and o.Cli_DateSynch > " + dateSynch + " ");
            } else {
                listClient = ejbFacadeClient.findAllNative(" where o.Cli_Id in ( " + commercial.getSequenceClientID() + ") ");
            }
            if (listClient != null && !listClient.isEmpty()) {
                for (Client client : listClient) {
                    ClientWS clientWS = new ClientWS();
                    clientWS.setId(client.getId());
                    clientWS.setLibelle(client.getLibelle());
                    clientWS.setGsm(client.getGsm());
                    clientWS.setEmail(client.getEmail());
                    clientWS.setAdresse(client.getAdresse());
                    //clientWS.setSupprimer(listClient1.isSupprimer());
                    clientWS.setDateSynch(client.getDateSynch());
                    
                    clientWS.setIdDelegation(client.getIdDelegation());
                    clientWS.setLibelleDelegation(client.getLibelleDelegation());
                    
                    clientWS.setIdGouvernorat(client.getIdGouvernorat());
                    clientWS.setLibelleGouvernorat(client.getLibelleGouvernorat());
                    
                    clientWS.setIdPays(client.getIdPays());
                    clientWS.setLibellePays(client.getLibellePays());
                    
                    clientWS.setIdCategorieClient(client.getCategorieClient().getId());
                    
                    listeClientWS.add(clientWS);
                }
                return listeClientWS;
            } else {
                return listeClientWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserClientsByCommercial ERREUR : " + e.getMessage());
            return listeClientWS;
        }
    }
    
    @GET
    @Path("/synchroniserClientsByCommercial/{idCom}/{idPays}/{idGouvernorat}/{idDelegation}/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ClientWS> synchroniserClientsByCommercial(@PathParam("idCom") Long idCom, @PathParam("idPays") long idPays, @PathParam("idGouvernorat") long idGouvernorat, @PathParam("idDelegation") long idDelegation, @PathParam("dateSynch") long dateSynch) {
        List<ClientWS> listeClientWS = new ArrayList<>();
        
        try {
            
            String clause = "";
            
            System.err.println("==> synchroniserClientsByCommercial");
            List<Client> listClient = null;
            
            clause = clause + (idPays > 0 ? "and Pys_Id = " + idPays : " ");
            clause = clause + (idGouvernorat > 0 ? "and Gov_Id = " + idGouvernorat : " ");
            clause = clause + (idDelegation > 0 ? "and Del_Id = " + idDelegation : " ");
            
            List<Commercial> commerciaux = ejbFacadeCommercial.findAllNative(" where o.Com_Id = " + idCom + "  ");
            Commercial commercial = commerciaux.get(0);
            if (dateSynch > 0) {
                listClient = ejbFacadeClient.findAllNative(" where o.Cli_Id in ( " + commercial.getSequenceClientID() + ") and o.Cli_DateSynch > " + dateSynch + " " + clause);
            } else {
                listClient = ejbFacadeClient.findAllNative(" where o.Cli_Id in ( " + commercial.getSequenceClientID() + ") " + clause);
            }
            if (listClient != null && !listClient.isEmpty()) {
                for (Client client : listClient) {
                    ClientWS clientWS = new ClientWS();
                    clientWS.setId(client.getId());
                    clientWS.setLibelle(client.getLibelle());
                    clientWS.setGsm(client.getGsm());
                    clientWS.setEmail(client.getEmail());
                    clientWS.setAdresse(client.getAdresse());
                    //clientWS.setSupprimer(listClient1.isSupprimer());
                    clientWS.setDateSynch(client.getDateSynch());
                    
                    clientWS.setIdDelegation(client.getIdDelegation());
                    clientWS.setLibelleDelegation(client.getLibelleDelegation());
                    
                    clientWS.setIdGouvernorat(client.getIdGouvernorat());
                    clientWS.setLibelleGouvernorat(client.getLibelleGouvernorat());
                    
                    clientWS.setIdPays(client.getIdPays());
                    clientWS.setLibellePays(client.getLibellePays());
                    
                    clientWS.setIdCategorieClient(client.getCategorieClient().getId());
                    
                    listeClientWS.add(clientWS);
                }
                return listeClientWS;
            } else {
                return listeClientWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserClientsByCommercial ERREUR : " + e.getMessage());
            return listeClientWS;
        }
    }
    
    @GET
    @Path("/synchroniserAllClients/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ClientWS> synchroniserAllClients(@PathParam("dateSynch") long dateSynch) {
        List<ClientWS> listeClientWS = new ArrayList<>();
        try {
            System.err.println("==> synchroniserClients");
            List<Client> listClient = null;
            if (dateSynch > 0) {
                listClient = ejbFacadeClient.findAllNative(" where  o.Cli_DateSynch > " + dateSynch + " ");
            } else {
                listClient = ejbFacadeClient.findAllNative(" ");
            }
            if (listClient != null && !listClient.isEmpty()) {
                for (Client client : listClient) {
                    ClientWS clientWS = new ClientWS();
                    clientWS.setId(client.getId());
                    clientWS.setLibelle(client.getLibelle());
                    clientWS.setGsm(client.getGsm());
                    clientWS.setEmail(client.getEmail());
                    clientWS.setAdresse(client.getAdresse());
                    //clientWS.setSupprimer(listClient1.isSupprimer());
                    clientWS.setDateSynch(client.getDateSynch());
                    
                    clientWS.setIdDelegation(client.getIdDelegation());
                    clientWS.setLibelleDelegation(client.getLibelleDelegation());
                    
                    clientWS.setIdGouvernorat(client.getIdGouvernorat());
                    clientWS.setLibelleGouvernorat(client.getLibelleGouvernorat());
                    
                    clientWS.setIdPays(client.getIdPays());
                    clientWS.setLibellePays(client.getLibellePays());
                    
                    clientWS.setIdCategorieClient(client.getCategorieClient().getId());
                    clientWS.setLibelleCategorieClient(client.getCategorieClient().getLibelle());
                    listeClientWS.add(clientWS);
                }
                return listeClientWS;
            } else {
                return listeClientWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserAllClients ERREUR : " + e.getMessage());
            return listeClientWS;
        }
    }
    
    @GET
    @Path("/synchroniserPlanificationVisite/{idCom}/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<PlanificationVisiteWS> synchroniserPlanificationVisite(@PathParam("idCom") Long Com_Id, @PathParam("dateSynch") long dateSynch) {
        List<PlanificationVisiteWS> listePlanificationVisiteWS = new ArrayList<>();
        List<PlanificationVisite> listPlanificationVisite;
        try {
            System.err.println("==> synchroniserPlanificationVisite");
            if (dateSynch > 0) {
                listPlanificationVisite = ejbPlanificationVisite.findAllNative(" where o.Com_Id = " + Com_Id + " and o.PVi_DateSynch > " + dateSynch + " ");
            } else {
                String dateDebut = TraitementDate.returnDateHeure(TraitementDate.moinsPlusMois(new Date(), -6));
                String dateFin = TraitementDate.returnDateHeure(new Date());
                listPlanificationVisite = ejbPlanificationVisite.findAllNative(" where o.Com_Id = " + Com_Id); //+ " and o.Cli_DateSynchro between '" + dateDebut + "' and  '" + dateFin + "' "
            }
            if (listPlanificationVisite != null && !listPlanificationVisite.isEmpty()) {
                for (PlanificationVisite planification : listPlanificationVisite) {
                    PlanificationVisiteWS planificationVisiteWS = new PlanificationVisiteWS();
                    planificationVisiteWS.setId(planification.getId());
                    
                    planificationVisiteWS.setDateDebut(planification.getDateDebut().getTime());
                    planificationVisiteWS.setDateFin(planification.getDateFin().getTime());
                    planificationVisiteWS.setDateSynch(planification.getDateSynch());
                    planificationVisiteWS.setDescription(planification.getDescription());
                    planificationVisiteWS.setEtat(planification.getEtat());
                    
                    planificationVisiteWS.setIdClient(planification.getIdClient());
                    planificationVisiteWS.setLibelleClient(planification.getLibelleClient());
                    
                    planificationVisiteWS.setIdPays(planification.getIdPays());
                    planificationVisiteWS.setLibellePays(planification.getLibellePays());
                    
                    planificationVisiteWS.setIdCategorieClient(planification.getIdCategorieClient());
                    
                    planificationVisiteWS.setSupprimer(planification.isSupprimer());
                    
                    listePlanificationVisiteWS.add(planificationVisiteWS);
                }
                return listePlanificationVisiteWS;
            } else {
                return listePlanificationVisiteWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserPlanificationVisite ERREUR : " + e.getMessage());
            return listePlanificationVisiteWS;
        }
    }
    
    @POST
    @Path("/ajouterPlanificationVisite")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response ajouterPlanificationVisite(String planificationVisiteJson) {
        try {
            Gson gson = new Gson();
            PlanificationVisiteWS planification = gson.fromJson(planificationVisiteJson, PlanificationVisiteWS.class);
            PlanificationVisite planificationVisite = new PlanificationVisite();
            planificationVisite.setId(planification.getId());
            
            planificationVisite.setDateDebut(TraitementDate.dateDuString(planification.getDateDebut()));
            planificationVisite.setDateFin(TraitementDate.dateDuString(planification.getDateFin()));
            
            planificationVisite.setDateSynch(planification.getDateSynch());
            
            planificationVisite.setDescription(planification.getDescription());
            
            planificationVisite.setEtat(planification.getEtat());
            
            planificationVisite.setIdClient(planification.getIdClient());
            planificationVisite.setLibelleClient(planification.getLibelleClient());
            
            planificationVisite.setIdPays(planification.getIdPays());
            planificationVisite.setLibellePays(planification.getLibellePays());
            
            planificationVisite.setSupprimer(planification.isSupprimer());
            
            ejbPlanificationVisite.createOrUpdate(planificationVisite);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            System.out.println("MagmaService ajouterPlanificationVisite ERREUR : " + e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
    
    @GET
    @Path("/synchroniserProspections/{dateSynch}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<ClientWS> synchroniserProspections(@PathParam("dateSynch") long dateSynch) {
        List<ClientWS> listeClientWS = new ArrayList<>();
        try {
            System.err.println("==> synchroniserProspections");
            List<Prospection> listProspection = null;
            if (dateSynch > 0) {
                listProspection = ejbProspection.findAllNative(" where  o.Prp_DateSynch > " + dateSynch + " ");
            } else {
                listProspection = ejbProspection.findAllNative(" ");
            }
            if (listProspection != null && !listProspection.isEmpty()) {
                for (Prospection client : listProspection) {
                    ClientWS clientWS = new ClientWS();
                    clientWS.setId(client.getId());
                    clientWS.setLibelle(client.getLibelle());
                    clientWS.setGsm(client.getGsm());
                    clientWS.setAdresse(client.getAdresse());
                    //clientWS.setSupprimer(listClient1.isSupprimer());
                    clientWS.setDateSynch(client.getDateSynch());
                    
                    clientWS.setIdDelegation(client.getIdDelegation());
                    clientWS.setLibelleDelegation(client.getLibelleDelegation());
                    
                    clientWS.setIdGouvernorat(client.getIdGouvernorat());
                    clientWS.setLibelleGouvernorat(client.getLibelleGouvernorat());
                    
                    clientWS.setIdCategorieClient(client.getIdCategorieClient());
                    clientWS.setLibelleCategorieClient(client.getLibelleCategorieClient());
                    
                    clientWS.setIdCommercial(client.getIdCommercial());
                    clientWS.setNomCommercial(client.getNomCommercial());
                    clientWS.setPrenomCommercial(client.getPrenomCommercial());
                    clientWS.setTypeCommercial(client.getType());
                    
                    clientWS.setEtatProspection(client.getEtatProspection());
                    
                    listeClientWS.add(clientWS);
                }
                return listeClientWS;
            } else {
                return listeClientWS;
            }
        } catch (Exception e) {
            System.out.println("MagmaService synchroniserProspections ERREUR : " + e.getMessage());
            return listeClientWS;
        }
    }
    
    @POST
    @Path("/ajouterProspection")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response ajouterProspection(String prospectionJson) {
        try {
            Gson gson = new Gson();
            ClientWS clientPotentiel = gson.fromJson(prospectionJson, ClientWS.class);
            Prospection prospection = new Prospection();
            prospection.setId(clientPotentiel.getId());
            
            prospection.setDateSynch(clientPotentiel.getDateSynch());
            
            prospection.setId(clientPotentiel.getId());
            prospection.setLibelle(clientPotentiel.getLibelle());
            prospection.setGsm(clientPotentiel.getGsm());
            prospection.setAdresse(clientPotentiel.getAdresse());
            
            prospection.setIdDelegation(clientPotentiel.getIdDelegation());
            prospection.setLibelleDelegation(clientPotentiel.getLibelleDelegation());
            
            prospection.setIdGouvernorat(clientPotentiel.getIdGouvernorat());
            prospection.setLibelleGouvernorat(clientPotentiel.getLibelleGouvernorat());
            
            prospection.setIdCategorieClient(clientPotentiel.getIdCategorieClient());
            prospection.setLibelleCategorieClient(clientPotentiel.getLibelleCategorieClient());
            
            prospection.setIdCommercial(clientPotentiel.getIdCommercial());
            prospection.setNomCommercial(clientPotentiel.getNomCommercial());
            prospection.setPrenomCommercial(clientPotentiel.getPrenomCommercial());
            prospection.setType(clientPotentiel.getTypeCommercial());
            
            prospection.setLongitude(clientPotentiel.getLongitude());
            prospection.setLatitude(clientPotentiel.getLatitude());
            
            prospection.setDateCreation(TraitementDate.dateDuString(clientPotentiel.getDateSynch()));
            
            prospection.setEtatProspection(clientPotentiel.getEtatProspection());
            
            ejbProspection.create(prospection);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            System.out.println("MagmaService ajouterProspection ERREUR : " + e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
    
    @POST
    @Path("/ajouterRapportVisite")
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response ajouterRapportVisite(String rapportVisiteJson) {
        try {
            Gson gson = new Gson();
            RapportVisitWS rapportVisitWS = gson.fromJson(rapportVisiteJson, RapportVisitWS.class);
            RapportVisit rapportVisit = new RapportVisit();
            
            rapportVisit.setId(rapportVisitWS.getId());
            rapportVisit.setIdCommercial(rapportVisitWS.getIdCommercial());
            rapportVisit.setNomCommercial(rapportVisitWS.getNomCommercial());
            rapportVisit.setPrenomCommercial(rapportVisitWS.getPrenomCommercial());
            rapportVisit.setIdClient(rapportVisitWS.getIdClient());
            rapportVisit.setLibelleClient(rapportVisitWS.getLibelleClient());
            rapportVisit.setDateCreation(TraitementDate.dateDuString(rapportVisitWS.getDateVisite()));
            PlanificationVisite planificationVisite = new PlanificationVisite();
            planificationVisite.setId(rapportVisitWS.getIdPlanificationVisite());
            rapportVisit.setPlanificationVisite(planificationVisite);
            
            ejbRapportVisit.create(rapportVisit);
            
            List<RapportVisitArticle> listRapportVisitArticle = new ArrayList();
            for (RapportVisitArticleWS rapportVisitArticleWS : rapportVisitWS.getListRapportVisitArticles()) {
                RapportVisitArticle rapportVisitArticle = new RapportVisitArticle();
                rapportVisitArticle.setCodeArticle(rapportVisitArticleWS.getCodeArticle());
                rapportVisitArticle.setLibelleArticle(rapportVisitArticleWS.getLibelleArticle());
                rapportVisitArticle.setIdArticle(rapportVisitArticleWS.getIdArticle());
                
                rapportVisitArticle.setNote(rapportVisitArticleWS.getNote());
                rapportVisitArticle.setCommentaire(rapportVisitArticleWS.getCommentaire());
                rapportVisitArticle.setRapportVisit(rapportVisit);
                listRapportVisitArticle.add(rapportVisitArticle);
            }
            
            ejbRapportVisitArticle.create(listRapportVisitArticle);
            
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            System.out.println("MagmaService ajouterRapportVisite ERREUR : " + e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
    
}
