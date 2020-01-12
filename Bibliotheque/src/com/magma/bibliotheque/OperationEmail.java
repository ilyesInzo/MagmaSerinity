/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.bibliotheque;

import java.io.Serializable;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

/**
 *
 * @author : Amine ABDELKEFI
 * @email: amine.abdelkefi@symatique.com
 * @Mobile: (+216) 55 429 622
 * @Company: SYMATIQUE
 * @Web: www.symatique.com
 */
public final class OperationEmail implements Serializable {

    private Session session = null;
    private Transport transport = null;

    public OperationEmail() {
        connect();
    }

    public void connect() {
//        Properties properties = new Properties();
//        properties.setProperty("mail.transport.protocol", LireParametrage.returnValeurParametrage("protocoleEnvoie"));
//        properties.put("mail.smtp.auth", LireParametrage.returnValeurParametrage("authentification"));
//        properties.setProperty("mail.smtp.host", LireParametrage.returnValeurParametrage("serveurEmailEnvoi"));
//        properties.setProperty("mail.smtp.port", LireParametrage.returnValeurParametrage("portSMTP"));
//        properties.setProperty("mail.smtp.from", LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification"));
//        properties.setProperty("mail.smtp.password", LireParametrage.returnValeurParametrage("pwdEmailEnvoi"));
//        this.session = Session.getDefaultInstance(properties, null);
//        try {
//            this.transport = this.session.getTransport();
//            this.transport.connect();
//            System.out.println("connect :: true" );
//        } catch (MessagingException ex) {
//            System.out.println("connect MessagingException:: " + ex.getMessage());
//        }

        Properties properties = new Properties();
//        System.out.println("///// LireParametrage.returnValeurParametrage(\"protocoleEnvoie\"): "+LireParametrage.returnValeurParametrage("protocoleEnvoie"));
//        System.out.println("///// LireParametrage.returnValeurParametrage(\"authentification\"): "+LireParametrage.returnValeurParametrage("authentification"));
//        System.out.println("///// LireParametrage.returnValeurParametrage(\"serveurEmailEnvoi\"),: "+LireParametrage.returnValeurParametrage("serveurEmailEnvoi"));
//        System.out.println("///// LireParametrage.returnValeurParametrage(\"adresseEmailEnvoiNotification\"): "+LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification"));
//        System.out.println("///// LireParametrage.returnValeurParametrage(\"pwdEmailEnvoi\"): "+LireParametrage.returnValeurParametrage("pwdEmailEnvoi"));
        properties.setProperty("mail.transport.protocol", LireParametrage.returnValeurParametrage("protocoleEnvoie"));
        properties.put("mail.smtp.auth", LireParametrage.returnValeurParametrage("authentification"));
        // For gmail
        properties.put("mail.smtp.starttls.enable", LireParametrage.returnValeurParametrage("starttls"));

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification"), LireParametrage.returnValeurParametrage("pwdEmailEnvoi"));
            }
        };
        
        this.session = Session.getInstance(properties, auth);
        try {
            this.transport = this.session.getTransport();
            this.transport.connect(LireParametrage.returnValeurParametrage("serveurEmailEnvoi"), Integer.parseInt(LireParametrage.returnValeurParametrage("portSMTP")), LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification"), LireParametrage.returnValeurParametrage("pwdEmailEnvoi"));
        } catch (MessagingException ex) {
            System.out.println("connect :: " + ex.getMessage());
        }
    }

    public void envoieEmailNotification(String email, String subject, String body) {
        try {
            if (this.transport.isConnected() == false) {
                connect();
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //message.addRecipient(Message.RecipientType.BCC, new InternetAddress(LireParametrage.returnValeurParametrage("adresseEmailCopie1")));
            String adresseEmailEnvoiNotification = LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification");
            message.setFrom(new InternetAddress(adresseEmailEnvoiNotification));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));
            this.transport.close();
        } catch (MessagingException ex) {
            System.out.println("envoieEmailNotification(Utilisateur pDestinataire, String subject, String body) :: " + ex.getMessage());
        }
    }

    public void envoieEmailNotification(String email, String emailCopie, String subject, String body) {
        try {
            if (this.transport.isConnected() == false) {
                connect();
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCopie));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(LireParametrage.returnValeurParametrage("adresseEmailCopie1")));
            String adresseEmailEnvoiNotification = LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification");
            message.setFrom(new InternetAddress(adresseEmailEnvoiNotification));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));
            this.transport.close();
        } catch (MessagingException ex) {
            System.out.println("envoieEmailNotification(Utilisateur pDestinataire, String subject, String body) :: " + ex.getMessage());
        }
    }

    public void envoieEmailNotification(String email, List<String> emailCopies, String subject, String body) {
        try {
            if (this.transport.isConnected() == false) {
                connect();
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            for (int f = 0; f < emailCopies.size(); f++) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCopies.get(f)));
            }
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(LireParametrage.returnValeurParametrage("adresseEmailCopie1")));
            String adresseEmailEnvoiNotification = LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification");
            message.setFrom(new InternetAddress(adresseEmailEnvoiNotification));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));
            this.transport.close();
        } catch (MessagingException ex) {
            System.out.println("envoieEmailNotification(Utilisateur pDestinataire, String subject, String body) :: " + ex.getMessage());
        }
    }

    public void envoieEmailNotification(List<String> emails, String subject, String body) {
        try {
            if (this.transport.isConnected() == false) {
                connect();
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            for (int f = 0; f < emails.size(); f++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(f)));
            }
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(LireParametrage.returnValeurParametrage("adresseEmailCopie1")));
            String adresseEmailEnvoiNotification = LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification");
            message.setFrom(new InternetAddress(adresseEmailEnvoiNotification));

            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));
            this.transport.close();
        } catch (MessagingException ex) {

            System.out.println("envoieEmailNotification(Utilisateur pDestinataire, String subject, String body) :: " + ex.getMessage());

        }
    }

    public void envoieEmailNotification(List<String> emails, String emailCopie, String subject, String body) {
        try {
            if (this.transport.isConnected() == false) {
                connect();
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            for (int f = 0; f < emails.size(); f++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(f)));
            }
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCopie));
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(LireParametrage.returnValeurParametrage("adresseEmailCopie1")));
            String adresseEmailEnvoiNotification = LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification");
            message.setFrom(new InternetAddress(adresseEmailEnvoiNotification));

            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));
            this.transport.close();
        } catch (MessagingException ex) {

            System.out.println("envoieEmailNotification(List<Utilisateur> pDestinataires, List<String> pDestinataires2, String subject, String body, int i) :: " + ex.getMessage());

        }
    }

    public void envoieEmailNotification(List<String> emails, List<String> emailCopies, String subject, String body) {
        try {
            if (this.transport.isConnected() == false) {
                connect();
            }
            MimeMessage message = new MimeMessage(this.session);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            for (int f = 0; f < emails.size(); f++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(f)));
            }
            for (int f = 0; f < emailCopies.size(); f++) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailCopies.get(f)));
            }
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(LireParametrage.returnValeurParametrage("adresseEmailCopie1")));
            String adresseEmailEnvoiNotification = LireParametrage.returnValeurParametrage("adresseEmailEnvoiNotification");
            message.setFrom(new InternetAddress(adresseEmailEnvoiNotification));

            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            this.transport.sendMessage(message, message.getRecipients(Message.RecipientType.BCC));
            this.transport.close();
        } catch (MessagingException ex) {

            System.out.println("envoieEmailNotification(List<Utilisateur> pDestinataires, List<String> pDestinataires2, String subject, String body, int i) :: " + ex.getMessage());

        }
    }

}
