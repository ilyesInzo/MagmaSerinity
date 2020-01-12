import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "messagesView")
@SessionScoped
public class MessagesView implements Serializable {

    private List<String> ListMessageS;
    private String message;

    public String initPage() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            //utilisateur = (Utilisateur) context.getExternalContext().getSessionMap().get("user");
            menuModules();
            menuFonctionnalites();
            //recreateModel();
            FacesContext.getCurrentInstance().getExternalContext().redirect("essai.xhtml");
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
        context.getExternalContext().getSessionMap().put("LightMenu", "active-menuitem");
        context.getExternalContext().getSessionMap().put("DarkMenu", "");
        context.getExternalContext().getSessionMap().put("GUser", "");
    }

    public void info() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "PrimeFaces Rocks."));
    }

    public void warn() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Watch out for PrimeFaces."));
    }

    public void error() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Contact admin."));
    }

    public void fatal() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", "System Error"));
    }

    public List<String> getItems() {
        if (ListMessageS == null) {

            ListMessageS = new ArrayList<>();
            ListMessageS.add("cc");
            ListMessageS.add("de");
            ListMessageS.add("z1");
        }
        return ListMessageS;

    }

    public List<String> getListMessageS() {
        return ListMessageS;
    }

    public void setListMessageS(List<String> ListMessageS) {
        this.ListMessageS = ListMessageS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
