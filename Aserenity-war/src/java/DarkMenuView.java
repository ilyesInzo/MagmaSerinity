
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Symatique003
 */
@ManagedBean(name = "darkMenuView")
@SessionScoped
public class DarkMenuView implements Serializable{
    
    
    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            //utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
            menuModules();
            menuFonctionnalites();
            //recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    
        private void menuModules() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("MenuModes", "display: block");
        
        }
        
        private void menuFonctionnalites() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("LightMenu", "");
        context.getExternalContext().getSessionMap().put("DarkMenu", "active-menuitem");
        context.getExternalContext().getSessionMap().put("GUser", "");
        
        }
    
}
