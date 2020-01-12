/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author : Amine ABDELKEFI
 * @email: amine.abdelkefi@symatique.com
 * @Mobile: (+216) 55 429 622
 * @Company: SYMATIQUE
 * @Web: www.symatique.com
 */
public class OperationString implements Serializable {

    public static String genereUnMot(int longueur) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_@#&";
        Random rand = new Random();
        String mot = "";
        for (int i = 0; i < longueur; i++) {
            mot = mot + alphabet.charAt(rand.nextInt(alphabet.length()));
        }
        return mot;
    }

    public static String retourMotSelonLongeur(String mot, int size) {
        if (mot.length() < size) {
            int pas = size - mot.length();
            for (int i = 0; i < pas; i++) {
                mot = "0" + mot;
            }
        }
        return mot;
    }

    public static Long retourMontantEnMillime(Double montant) {
        String montantString = montant + "";
        String dinar = montantString.substring(0, montantString.indexOf("."));
        String millimes = montantString.substring(montantString.indexOf(".") + 1, montantString.length());
        if (millimes.length() < 3) {
            int pas = 3 - millimes.length();
            for (int i = 0; i < pas; i++) {
                millimes = millimes + "0";
            }
        } else {
            if (millimes.length() > 3) {
                millimes = millimes.substring(0, 3);
            }
        }

        montantString = dinar + "" + millimes;
        return Long.parseLong(montantString);
    }

    public static String returnFileExtention(String File) {

        if (File == null) {
            return null;
        }

        int pos = File.lastIndexOf(".");

        if (pos == -1) {
            return File;
        }

        return File.substring(1, pos);
    }

    public static String returnFileName(String File) {
        if (File == null) {
            return null;
        }

        int pos = File.lastIndexOf(".");

        if (pos == -1) {
            return File;
        }

        return File.substring(0, pos);
    }

}
