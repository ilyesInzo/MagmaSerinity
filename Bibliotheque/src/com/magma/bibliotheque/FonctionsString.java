/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.text.Normalizer;
import java.util.Random;

/**
 *
 * @author Symatique
 */
public class FonctionsString {

    public static String PremiereLetteMajuscule(String libelle) {
        try {
            if (libelle.length() == 1) {
                return libelle.toUpperCase();
            }
            return libelle.substring(0, 1).toUpperCase() + libelle.substring(1);
        } catch (Exception e) {
            return libelle;
        }
    }

    public static String toutMajuscule(String libelle) {
        try {
            return libelle.toUpperCase();
        } catch (Exception e) {
            return libelle;
        }
    }

    public static String supprimerCaracteresSpeciaux(String chaine) {
        try {
            String s = Normalizer.normalize(chaine, Normalizer.Form.NFD);
            String ss = s.replaceAll("^[\\\\u0000-\\\\u007F]*$", "");
            String sss = ss.replaceAll("\\p{M}", "");
            return sss;
        } catch (Exception e) {
            return chaine;
        }

    }
    
    public static String genereUnMot(int longueur){
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_@#&";
        Random rand = new Random();
        String mot= "";
        for (int i=0; i<longueur; i++){
                mot = mot + alphabet.charAt(rand.nextInt(alphabet.length()));
        }
        return mot;
    }

    public static String retourMotSelonLongeur(String mot, int size){
        if(mot.length() < size){
            int pas = size - mot.length();
            for(int i = 0; i < pas; i++){
                mot = "0"+mot;
            }
        }
        return mot;
    }
    
    public static Long retourMontantEnMillime(Double montant){
        String montantString = montant+"";
        String dinar = montantString.substring(0, montantString.indexOf("."));
        String millimes = montantString.substring(montantString.indexOf(".")+1, montantString.length());
        if(millimes.length() < 3){
            int pas = 3-millimes.length();
            for(int i =0; i<pas;i++){
                millimes=millimes+"0";
            }
        }else{
            if(millimes.length() > 3){
                millimes = millimes.substring(0, 3);
            }
        }
        
        montantString = dinar+""+millimes;
        return Long.parseLong(montantString);
    }

}
