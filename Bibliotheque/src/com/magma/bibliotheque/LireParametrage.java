/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author : Mansour Ilyes
 * @email: amine.abdelkefi@symatique.com
 * @Mobile: (+216) 55 429 622
 * @Company: SYMATIQUE
 * @Web: www.symatique.com
 */
public class LireParametrage implements Serializable {
    private static String serveurDomain = System.getProperty("com.sun.aas.domainsRoot");
    private static HashMap<String,String> hashMap = new HashMap(){
        { put("urlUmploadPhoto", serveurDomain+"/domain1/images/");
          put("urlImageNonPersistant", serveurDomain+"/domain1/images/imagesNonPersistant/");
          put("urlDocumentNonPersistant", serveurDomain+"/domain1/contents/documentNonPersistant/");
          put("urlDocumentPersistant", serveurDomain+"/domain1/contents/");
          put("serveurEmailEnvoi", "smtp.gmail.com");
          put("portSMTP", "587");
          put("authentification", "true");
          put("starttls", "true");
          put("protocoleEnvoie", "smtp");
          put("adresseEmailEnvoiNotification", "MagmaCompanies@gmail.com");
          put("pwdEmailEnvoi", "M@gm@Company666");
          put("adresseEmailCopie1", "inzodialo@gmail.com");
        }};

    public static String returnValeurParametrage(String clef) {
        
        
        
        
        return  hashMap.get(clef);
       /* try {
            
            hashMap.get("com.sun.aas.domainsRoot");
            
            //System.out.println("Path 1: " + System.getProperty("com.sun.aas.installRoot"));
            //System.out.println("Path 1: " + System.getProperty("com.sun.aas.domainsRoot"));
            //System.out.println("Path 1: " + System.getProperty("com.sun.aas.instanceRoot"));
            //System.out.println("Path 1: " + System.getProperty("catalina.base")); // get with the appropriete domaine in production

            
            java.util.Properties props = new java.util.Properties();
            java.io.FileInputStream fis;
            //String path = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            //path = path.substring(0, path.indexOf("classes")) + "parametrages.properties";
            String path = "/C:\\parametrage\\parametrages.properties";
            fis = new java.io.FileInputStream(new java.io.File("/" + path));

            try {
                props.load(fis);
                return props.getProperty(clef);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (FileNotFoundException ex) {
        }
        return "";*/
    }
}
