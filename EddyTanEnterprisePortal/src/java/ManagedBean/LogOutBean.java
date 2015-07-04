/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author valentinus.r.sjofyan
 */
@ManagedBean
@RequestScoped
public class LogOutBean {

    /**
     * Creates a new instance of LogOutBean
     */
   
    private String updateLoginHist;
    private String updateUserStat;

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    private Connection con;

    public LogOutBean() {
    }

    @PostConstruct
    public void getConnection() {
        try {
            System.out.println("Initiating Portal Bean");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (Exception ex) {
            System.out.println("Failed to open DB Connection");
            ex.printStackTrace();
        }
    }

    @PreDestroy
    public void clear() {
        try {
            System.out.println("Clearing Log Out Bean");
            con.close();
        } catch (Exception ex) {
            System.out.println("Failed to close DB Connection");
            ex.printStackTrace();
        }

    }

    public String logout() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String passedID = facesContext.getExternalContext().getRequestParameterMap().get("id");
            String passedSession = facesContext.getExternalContext().getRequestParameterMap().get("session");

            //Update Login Hist
            PreparedStatement ps = con.prepareStatement(updateLoginHist);
            ps.setString(1, passedID);
            ps.setString(2, passedSession);
            ps.executeUpdate();
            ps.close();

            //Update User status
            PreparedStatement ps2 = con.prepareStatement(updateUserStat);
            ps2.setString(1, "Active");
            ps2.setString(2, passedID);
            ps2.executeUpdate();
            ps2.close();

            return "index.xhtml";
        } catch (Exception ex) {
            System.out.println("Exception While Loggin out : " + ex.getMessage());
            ex.printStackTrace();
            return "index.xhtml";
        }

    }

    public String getUpdateLoginHist() {
        return updateLoginHist;
    }

    public void setUpdateLoginHist(String updateLoginHist) {
        this.updateLoginHist = updateLoginHist;
    }

    public String getUpdateUserStat() {
        return updateUserStat;
    }

    public void setUpdateUserStat(String updateUserStat) {
        this.updateUserStat = updateUserStat;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
    
    
}
