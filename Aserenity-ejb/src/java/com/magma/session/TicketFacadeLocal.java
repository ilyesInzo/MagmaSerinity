/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Ticket;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface TicketFacadeLocal {

    void create(Ticket ticket);

    void edit(Ticket ticket);

    void remove(Ticket ticket);

    Ticket find(Object id);

    List<Ticket> findAll();

    List<Ticket> findRange(int[] range);
    
    List<Ticket> findAll(String clause);

    boolean verifierUnique(String libelle);
    
    boolean verifierUnique(String libelle, Object id);

    int count();
    
}
