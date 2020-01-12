/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.AvoirVente;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author inzo
 */
@Local
public interface AvoirVenteFacadeLocal {

    void create(AvoirVente avoirVente);

    void edit(AvoirVente avoirVente);

    void remove(AvoirVente avoirVente);

    AvoirVente find(Object id);

    List<AvoirVente> findAll();

    List<AvoirVente> findRange(int[] range);

    int count();

    boolean verifierUniqueNumero(String libelle);

    List<AvoirVente> findAllNative(String clause);

    List<AvoirVente> findAll(String clause);

    List<AvoirVente> searchAllNative(String dateDebut, String dateFin, Long idClient);

    void editRestAvoir(Long idAvoir, BigDecimal montant);

}
