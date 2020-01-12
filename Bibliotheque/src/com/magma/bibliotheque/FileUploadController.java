/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author LENOVO
 */
public class FileUploadController {

    public static boolean uploderFichier(String fileName, InputStream in, String destination) {
        try {
            OutputStream out = new FileOutputStream(new File(destination + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            
            
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    

    public static boolean eraseFile(String filename) throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }
        if (!file.canWrite()) {
            return false;
        }
        return file.delete();
    }

    public static boolean copyFile(File source, File dest) {
        try {
            java.io.FileInputStream sourceFile = new java.io.FileInputStream(source);

            try {
                java.io.FileOutputStream destinationFile = null;
                try {
                    destinationFile = new FileOutputStream(dest);
                    byte buffer[] = new byte[512 * 1024];
                    int nbLecture;
                    while ((nbLecture = sourceFile.read(buffer)) != -1) {
                        destinationFile.write(buffer, 0, nbLecture);
                    }
                } finally {
                    System.out.println("close destination file");
                    destinationFile.close();
                }
            } finally {
                System.out.println("close source file");
                sourceFile.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            return false;
        }
        return true;
    }

    public String lireFile(String filename) {
        Scanner scanner = null;
        String ligne = "";
        try {
            scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ligne = ligne + line + "\n";
            }
            return ligne;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            return "";
        } finally {
            try {
                scanner.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void ecrireFile(String filename, String texte) {
        try {
            PrintWriter ecrire = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            ecrire.println(texte);
            ecrire.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
