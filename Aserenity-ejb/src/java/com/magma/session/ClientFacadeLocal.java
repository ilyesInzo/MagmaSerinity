/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Client;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface ClientFacadeLocal {

    void create(Client client);

    void edit(Client client);

    void remove(Client client);

    Client find(Object id);

    List<Client> findAll();
    
    List<Client> findAll(String clause);
    
    List<Client> findAllNative(String clause);

    List<Client> findRange(int[] range);

    int count();
    
    boolean verifierUnique(String libelle, String gsm);

    boolean verifierUnique(String libelle, String gsm, Long id);
    
}
