/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.LigneRetour;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface LigneRetourFacadeLocal {

    void create(LigneRetour ligneRetour);

    void edit(LigneRetour ligneRetour);

    void remove(LigneRetour ligneRetour);

    LigneRetour find(Object id);

    List<LigneRetour> findAll();

    List<LigneRetour> findRange(int[] range);
    
    void create(List<LigneRetour> ligneFacture);

    void edit(List<LigneRetour> ligneFacture);

    void remove(List<LigneRetour> ligneFacture);

    int count();
    
}
