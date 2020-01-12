/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Utilisateur;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface UtilisateurFacadeLocal {

    void create(Utilisateur utilisateur);

    void edit(Utilisateur utilisateur);

    void remove(Utilisateur utilisateur);

    Utilisateur find(Object id);

    List<Utilisateur> findAll();
    
    List<Utilisateur> findAll(String clause);

    List<Utilisateur> findRange(int[] range);
    
    boolean verifierUnique(String libelle);

    boolean verifierUnique(String libelle, Long id);
    
    boolean verifierUnique(String libelle, String code);

    boolean verifierUnique(String libelle, String code, Long id);

    int count();
    
    Utilisateur findUserByEmail(String email);
    
}
