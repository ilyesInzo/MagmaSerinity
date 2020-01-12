/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.session;

import com.magma.entity.Utilisateur;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author inzo
 */
public class SessionCounterListener implements HttpSessionListener {

    private static int sessionCount;

    /* @EJB
    private ConnectionsFacadeLocal ejbFacadeConnection;*/

    public SessionCounterListener() {
        SessionCounterListener.sessionCount = 0;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lng", "en");
        System.out.println("sessionCreated");
        se.getSession().setMaxInactiveInterval(60000);
        synchronized (this) {
            SessionCounterListener.sessionCount++;
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        Utilisateur temp = (Utilisateur) se.getSession().getAttribute("user");
        System.out.println("sessionDestroyed");
        if (temp != null) {
            /*Connections connections = temp.getConnections();
            connections.setDateDeconnection(new Date());
            ejbFacadeConnection.edit(connections);*/
            se.getSession().removeAttribute("user");
            se.getSession().invalidate();
        }
        synchronized (this) {
            --SessionCounterListener.sessionCount;
        }
    }
}
