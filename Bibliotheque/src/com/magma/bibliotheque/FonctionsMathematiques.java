/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.math.BigDecimal;

/**
 *
 * @author Symatique
 */
public class FonctionsMathematiques {

    public static double arrondiDouble(double val, int nbChiffreApresVirgule) {
        int chiffreApresVirgule = 10 * nbChiffreApresVirgule;
        float chiffreApresVirguleFloat = chiffreApresVirgule;
        return (Math.floor(val * chiffreApresVirguleFloat + 0.5)) / chiffreApresVirgule;
    }

    public static float arrondiFloat(float val, int nbChiffreApresVirgule) {
        int chiffreApresVirgule = 10 * nbChiffreApresVirgule;
        float chiffreApresVirguleFloat = chiffreApresVirgule;
        return (float) ((Math.floor(val * chiffreApresVirguleFloat + 0.5)) / chiffreApresVirgule);
    }

    public static BigDecimal arrondiBigDecimal(BigDecimal val, int nbChiffreApresVirgule) {
        try {
            return val.setScale(nbChiffreApresVirgule, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {

            return new BigDecimal("0.000");
        }
    }
}
