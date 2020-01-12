/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.io.Serializable;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author LENOVO
 */
public class TraitementDate implements Serializable {
    
    //if dataBase =1 => sqlServer
    //id dataBase =2 => mySQL
    public static int dataBase = 1;
    private static final String anneeFormat = "yyyy";
    private static final String moisFormat = "MM";
    private static final String dateFormatJour = "yyyyMMdd";
    private static final String dateFormatJour2 = "yyMMdd";
    private static final String dateFormatJourHeure = "yyyyMMddHHmm";
    private static final String dateFormatJourHeureSecondes = "yyyyMMddHHmmssSSS";
    private static final String heureFormat = "HH:mm:ss";
    private static final String heureMinuteFormat = "HHmm";
    
    private static final String dateHeureFormatEn = "MM/dd/yyyy HH:mm:ss";
    private static final String dateFormatEn = "MM/dd/yyyy";
    private static final String dateHeureFormat = "dd/MM/yyyy HH:mm:ss";
    private static final String dateFormat = "dd/MM/yyyy";
    
    private static final String dateHeureFormatEnMySQL = "yyyy-MM-dd HH:mm:ss";
    private static final String dateFormatEnMySQL = "yyyy-MM-dd";
    private static final String dateHeureFormatMySQL = "yyyy-MM-dd HH:mm:ss";
    private static final String dateFormatMySQL = "yyyy-MM-dd";
    
    public static String returnAnneeEnCours(Date dateTemp) {
        Format format = new SimpleDateFormat(anneeFormat);
        return format.format(dateTemp);
    }
    
    public static String returnAnnee(Date dateTemp, int pas) {
        Calendar temp = new GregorianCalendar();
        temp.setTime(dateTemp);
        temp.add(Calendar.YEAR, pas);
        Format format = new SimpleDateFormat(anneeFormat);
        return format.format(temp.getTime());
    }
    
    public static String returnMoisEnCours(Date dateTemp) {
        Format format = new SimpleDateFormat(moisFormat);
        return format.format(dateTemp);
    }
    
    public static String returnMois(Date dateTemp, int pas) {
        Calendar temp = new GregorianCalendar();
        temp.setTime(dateTemp);
        temp.add(Calendar.MONTH, pas);
        Format format = new SimpleDateFormat(moisFormat);
        return format.format(temp.getTime());
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.HOURS);
    }

    public static Date dateDuString(String chaineDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateHeureFormat);
            
            if(dataBase==2){
            sdf = new SimpleDateFormat(dateHeureFormatMySQL);
            }
            
            return sdf.parse(chaineDate);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Date dateDuString(long time) {
        try {
            Date dateTemp = new Date();
            dateTemp.setTime(time);
            return dateTemp;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static long returnDateJour(Date dateTemp) {
        Format format = new SimpleDateFormat(dateFormatJour);
        return Long.parseLong(format.format(dateTemp));
    }

    public static long returnDateJour2(Date dateTemp) {
        Format format = new SimpleDateFormat(dateFormatJour2);
        return Long.parseLong(format.format(dateTemp));
    }

    public static long returnIndexDateHeure(Date dateTemp) {
        Format format = new SimpleDateFormat(dateFormatJourHeure);
        return Long.parseLong(format.format(dateTemp));
    }

    public static long returnIndexDateHeureSecondes(Date dateTemp) {
        Format format = new SimpleDateFormat(dateFormatJourHeureSecondes);
        return Long.parseLong(format.format(dateTemp));
    }

    public static String returnDateHeure(Date dateTemp) {
        Format format = new SimpleDateFormat(dateHeureFormat);
                    if(dataBase==2){
            format = new SimpleDateFormat(dateHeureFormatMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateEn(Date dateTemp) {
        Format format = new SimpleDateFormat(dateFormatEn);
                            if(dataBase==2){
            format = new SimpleDateFormat(dateFormatEnMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateEn(Long time) {
        Date dateTemp = new Date();
        dateTemp.setTime(time);
        Format format = new SimpleDateFormat(dateFormatEn);
                                    if(dataBase==2){
            format = new SimpleDateFormat(dateFormatEnMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateEn(String time) {
        Date dateTemp = new Date();
        dateTemp.setTime(Long.parseLong(time));
        Format format = new SimpleDateFormat(dateFormatEn);
                                            if(dataBase==2){
            format = new SimpleDateFormat(dateFormatEnMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateHeureEn(Date dateTemp) {
        Format format = new SimpleDateFormat(dateHeureFormatEn);
                                                    if(dataBase==2){
            format = new SimpleDateFormat(dateHeureFormatEnMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateHeureEn(Long time) {
        Date dateTemp = new Date();
        dateTemp.setTime(time);
        Format format = new SimpleDateFormat(dateHeureFormatEn);
                                                            if(dataBase==2){
            format = new SimpleDateFormat(dateHeureFormatEnMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateHeureEn(String time) {
        Date dateTemp = new Date();
        dateTemp.setTime(Long.parseLong(time));
        Format format = new SimpleDateFormat(dateHeureFormatEn);
                                                            if(dataBase==2){
            format = new SimpleDateFormat(dateHeureFormatEnMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateHeureFinJournee(Date dateTemp) {
        Format format = new SimpleDateFormat(dateFormat);
                                                            if(dataBase==2){
            format = new SimpleDateFormat(dateFormatMySQL);
            }
        return format.format(dateTemp) + " 23:59:59";
    }

    public static String returnDateHeure(Long time) {
        Date dateTemp = new Date();
        dateTemp.setTime(time);
        Format format = new SimpleDateFormat(dateHeureFormat);
                                                            if(dataBase==2){
            format = new SimpleDateFormat(dateHeureFormatMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDateHeure(String time) {
        Date dateTemp = new Date();
        dateTemp.setTime(Long.parseLong(time));
        Format format = new SimpleDateFormat(dateHeureFormat);
                                                                    if(dataBase==2){
            format = new SimpleDateFormat(dateHeureFormatMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDate(Date dateTemp) {
        Format format = new SimpleDateFormat(dateFormat);
                                                                    if(dataBase==2){
            format = new SimpleDateFormat(dateFormatMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDate(Long time) {
        Date dateTemp = new Date();
        dateTemp.setTime(time);
        Format format = new SimpleDateFormat(dateFormat);
                                                                    if(dataBase==2){
            format = new SimpleDateFormat(dateFormatMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnDate(String time) {
        Date dateTemp = new Date();
        dateTemp.setTime(Long.parseLong(time));
        Format format = new SimpleDateFormat(dateFormat);
                                                                    if(dataBase==2){
            format = new SimpleDateFormat(dateFormatMySQL);
            }
        return format.format(dateTemp);
    }

    public static String returnHeureMinute(Date dateTemp) {
        Format format = new SimpleDateFormat(heureMinuteFormat);
        return format.format(dateTemp);
    }

    public static String returnHeure(Date dateTemp) {
        Format format = new SimpleDateFormat(heureFormat);
        return format.format(dateTemp);
    }

    public static String returnHeure(Long time) {
        Date dateTemp = new Date();
        dateTemp.setTime(time);
        Format format = new SimpleDateFormat(heureFormat);
        return format.format(dateTemp);
    }

    public static String returnHeure(String time) {
        Date dateTemp = new Date();
        dateTemp.setTime(Long.parseLong(time));
        Format format = new SimpleDateFormat(heureFormat);
        return format.format(dateTemp);
    }

    public static int returnNbrAnnees(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        Calendar actuellement = new GregorianCalendar();
        actuellement.setTime(new Date());
        int adjust = 0;
        if (actuellement.get(Calendar.DAY_OF_YEAR) - naissance.get(Calendar.DAY_OF_YEAR) < 0) {
            adjust = -1;
        }
        return actuellement.get(Calendar.YEAR) - naissance.get(Calendar.YEAR) + adjust;
    }

    public static int returnDiffJour(Date dateTemp) {
        Calendar datetrame = new GregorianCalendar();
        datetrame.setTime(dateTemp);
        Calendar actuellement = new GregorianCalendar();
        actuellement.setTime(new Date());
        int adjust = actuellement.get(Calendar.DAY_OF_YEAR) - datetrame.get(Calendar.DAY_OF_YEAR);
        return adjust;
    }

    public static int returnDiffJour(Date datedebut, Date datefin) {
        Calendar datetrame = new GregorianCalendar();
        datetrame.setTime(datedebut);
        Calendar actuellement = new GregorianCalendar();
        actuellement.setTime(datefin);
        int adjust = actuellement.get(Calendar.DAY_OF_YEAR) - datetrame.get(Calendar.DAY_OF_YEAR);
        return adjust;
    }

    public static Date debutJournee(Date debut) {
        try {
            String chaineDate = returnDate(debut) + " 00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " HH:mm:ss");
                                                                    if(dataBase==2){
            sdf = new SimpleDateFormat(dateFormatMySQL);
            }
            return sdf.parse(chaineDate);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static Date finJournee(Date debut) {
        try {
            String chaineDate = returnDate(debut) + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " HH:mm:ss");
                                                                    if(dataBase==2){
            sdf = new SimpleDateFormat(dateFormatMySQL);
            }
            return sdf.parse(chaineDate);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static String dureeEntreDeuxDates(Date dDebut, Date DFin) {
        try {
            Long duree = (DFin.getTime() - dDebut.getTime()) / 1000;
            int ss = (int) (duree % 60);
            int reste = (int) (duree / 60);
            int mm = reste % 60;
            reste = reste / 60;
            int hh = reste % 24;
            reste = reste / 24;
            int jj = reste / 24;
            String msg = "";
            if (reste > 0) {
                msg = msg + reste + "j ";
            }
            String h = hh + "";
            String m = mm + "";
            String s = ss + "";
            if (h.length() == 1) {
                h = "0" + h;
            }
            if (m.length() == 1) {
                m = "0" + m;
            }
            if (s.length() == 1) {
                s = "0" + s;
            }
            msg = msg + h + ":" + m + ":" + s;
            return msg;
        } catch (Exception e) {
            return "";
        }
    }

    public static String duree(Long dureeSecondes) {
        try {
            Long duree = dureeSecondes;
            int ss = (int) (duree % 60);
            int reste = (int) (duree / 60);
            int mm = reste % 60;
            reste = reste / 60;
            int hh = reste % 24;
            reste = reste / 24;
            int jj = reste / 24;
            String msg = "";
            if (reste > 0) {
                msg = msg + reste + "j ";
            }
            String h = hh + "";
            String m = mm + "";
            String s = ss + "";
            if (h.length() == 1) {
                h = "0" + h;
            }
            if (m.length() == 1) {
                m = "0" + m;
            }
            if (s.length() == 1) {
                s = "0" + s;
            }
            msg = msg + h + ":" + m + ":" + s;
            return msg;
        } catch (Exception e) {
            return "";
        }
    }

    public static String duree(int dureeSecondes) {
        try {
            int duree = dureeSecondes;
            int ss = (int) (duree % 60);
            int reste = (int) (duree / 60);
            int mm = reste % 60;
            reste = reste / 60;
            int hh = reste % 24;
            reste = reste / 24;
            int jj = reste / 24;
            String msg = "";
            if (reste > 0) {
                msg = msg + reste + "j ";
            }
            String h = hh + "";
            String m = mm + "";
            String s = ss + "";
            if (h.length() == 1) {
                h = "0" + h;
            }
            if (m.length() == 1) {
                m = "0" + m;
            }
            if (s.length() == 1) {
                s = "0" + s;
            }
            msg = msg + h + ":" + m + ":" + s;
            return msg;
        } catch (Exception e) {
            return "";
        }
    }

    public static Date moinsSemaine(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.WEEK_OF_YEAR, -1);
        return naissance.getTime();
    }
    
    public static Date moinsPlusSemaines(Date dateTemp, int semaines) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.DAY_OF_MONTH, semaines);
        return naissance.getTime();
    }
    
    public static Date moinsJour(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.DAY_OF_MONTH, -1);
        return naissance.getTime();
    }
    
    public static Date moinsMois(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.MONTH, -1);
        return naissance.getTime();
    }

    public static Date moinsPlusMois(Date dateTemp, int jours) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.MONTH, jours);
        return naissance.getTime();
    }
    
    public static Date moinsPlusJours(Date dateTemp, int jours) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.DAY_OF_MONTH, jours);
        return naissance.getTime();
    }

    public static Date plusJour(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.DAY_OF_MONTH, 1);
        return naissance.getTime();
    }
    
    public static Date plusSemaine(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.WEEK_OF_YEAR, 1);
        return naissance.getTime();
    }
    
    public static Date plusMois(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.MONTH, 1);
        return naissance.getTime();
    }
    
    
    
    public static Date plusHeurs(Date dateTemp) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.HOUR, 1);
        return naissance.getTime();
    }

    public static Date moinsPlusHeurs(Date dateTemp, int heure) {
        Calendar naissance = new GregorianCalendar();
        naissance.setTime(dateTemp);
        naissance.add(Calendar.HOUR, heure);
        return naissance.getTime();
    }

    

    public static String debutJourneeString(Date debut) {

        String chaineDate = returnDate(debut) + " 00:00:00";
//            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " HH:mm:ss");
        return chaineDate;

    }
}
