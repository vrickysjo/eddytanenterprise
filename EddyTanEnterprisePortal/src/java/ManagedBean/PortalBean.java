/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import Entity.UserAuth;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateful;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author valentinus.r.sjofyan
 */
@ManagedBean
@SessionScoped
@Stateful
public class PortalBean {

    private String authQuery;
    private String userListQuery;
    private String insertLoginHist;
    private String updateLoginHist;
    private String latestSession;
    private String updateUserStat;

    private String dybestURL;
    private String velvetURL;

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    private Connection con;
    private UserAuth loggedinUser = new UserAuth();

    private String username;
    private String password;
    private String sessionId;

    private boolean velvetEnabled = false;
    private boolean dybestEnabled = false;
    private boolean portalEnabled = false;
    private boolean addUser = false;
    private boolean systemAksesShow = false;

    private List<UserAuth> userList = new ArrayList<UserAuth>();

    private int failedLoginCount = 0;

    private String errMsg;
    private boolean errMsgShow = false;

    /**
     * Creates a new instance of PortalBean
     */
    public PortalBean() {

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
            System.out.println("Clearing Portal Bean");
            con.close();
        } catch (Exception ex) {
            System.out.println("Failed to close DB Connection");
            ex.printStackTrace();
        }

    }

    public String logout() {
        try {            
            //Update Login Hist
            PreparedStatement ps = con.prepareStatement(updateLoginHist);
            ps.setString(1, loggedinUser.getUserId());
            ps.setString(2, sessionId);
            ps.executeUpdate();
            ps.close();

            //Update User status
            PreparedStatement ps2 = con.prepareStatement(updateUserStat);
            ps2.setString(1, "Active");
            ps2.setString(2, loggedinUser.getUserId());
            ps2.executeUpdate();
            ps2.close();

            sessionId = null;
            loggedinUser = new UserAuth();

            return "index.xhtml";
        } catch (Exception ex) {
            System.out.println("Exception While Loggin out : " + ex.getMessage());
            ex.printStackTrace();
            return "index.xhtml";
        }
    }

    public String login() {
        try {
            System.out.println("DB URL : " + dbUrl);

            PreparedStatement ps = con.prepareStatement(authQuery);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            boolean pass = false;
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            while (rs.next()) {
                loggedinUser.setUserId(rs.getString("userid"));
                loggedinUser.setPassword(rs.getString("password"));
                loggedinUser.setStatus(rs.getString("status"));
                loggedinUser.setPortal(rs.getString("portal"));
                loggedinUser.setDybest(rs.getString("dybest"));
                loggedinUser.setVelvet(rs.getString("velvet"));
                loggedinUser.setEffectiveStart(formatter.parse(rs.getString("effectivestart")));
                loggedinUser.setEffectiveEnd(formatter.parse(rs.getString("effectiveend")));
                loggedinUser.setLastLogin(new Date());
                
                System.out.println(loggedinUser.getEffectiveStart() + " - " + loggedinUser.getEffectiveEnd());
            }

            rs.close();
            String page = "";

            if (loggedinUser.getStatus().equalsIgnoreCase("Online")) {
                //Return Error Message : Failed to Login because User is already logged in at another place
                errMsgShow = true;
                errMsg = "Akun anda terdeksi telah login di tempat lain.";

                pass = false;
                page = "index.xhtml";
            } else if (loggedinUser.getStatus().equalsIgnoreCase("Suspend")) {
                //Return Error Message : Failed to Login because User is suspended
                errMsgShow = true;
                errMsg = "Akun anda sedang di Suspend.";

                pass = false;
                page = "index.xhtml";
            } else if (loggedinUser.getStatus().equalsIgnoreCase("Active")) {
                String timeNow = formatter.format(new Date());
                Date now = formatter.parse(timeNow);
                if (now.after(loggedinUser.getEffectiveStart()) && 
                        now.before(loggedinUser.getEffectiveEnd())) {
                    failedLoginCount = 0;
                    errMsgShow = false;
                    errMsg = "";

                    pass = true;
                    logHistory();
                    updateUser("Online", loggedinUser.getUserId());

                    if (loggedinUser.getPortal().equalsIgnoreCase("Y")) {
                        portalEnabled = true;
                        addUser = true;
                        page = "portal.xhtml";
                    } else {
                        portalEnabled = false;
                        addUser = false;
                    }
                    if (loggedinUser.getDybest().equalsIgnoreCase("Y")) {
                        dybestEnabled = true;
                        System.out.println("Dybest enabled");
                        if (page.equals("")) {
                            //Redirect To DYBEST
                            FacesContext facesContext = FacesContext.getCurrentInstance();
                            ExternalContext extContext = facesContext.getExternalContext();
                            extContext.redirect(dybestURL + "?id=" + username + "&session=" + sessionId);
                        }
                    } else {
                        dybestEnabled = false;
                    }
                    if (loggedinUser.getVelvet().equalsIgnoreCase("Y")) {
                        velvetEnabled = true;
                        if (page.equals("")) {
                            //Redirect To VELVET
                            FacesContext facesContext = FacesContext.getCurrentInstance();
                            ExternalContext extContext = facesContext.getExternalContext();
                            extContext.redirect(velvetURL + "?id=" + username + "&session=" + sessionId);
                        }
                    } else {
                        velvetEnabled = false;
                    }

                    systemAksesShow = velvetEnabled && dybestEnabled;
                } else {
                    //Return Error Message : Failed to Login because User is not allowed to login
                    errMsgShow = true;
                    errMsg = "Akun tidak diizinkan masuk pada jam ini";

                    pass = false;
                    page = "index.xhtml";
                }

            }

            return page;

        } catch (NullPointerException ne) {
            failedLoginCount++;
            if (failedLoginCount == 5) {
                try {
                    updateUser("Suspend", username);
                } catch (Exception ex) {
                    System.out.println("Failed to suspend user : " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            errMsgShow = true;
            errMsg = "Username atau Password salah.";
            return "index.xhtml";
        } catch (Exception ex) {
            System.out.println("Exception when login : " + ex.getMessage());
            ex.printStackTrace();
            return "index.xhtml";
        }
    }

    public void updateUser(String status, String userId) throws Exception {
        PreparedStatement ps = con.prepareStatement(updateUserStat);
        ps.setString(1, status);
        ps.setString(2, userId);
        ps.executeUpdate();
        ps.close();
    }

    public void logHistory() throws Exception {
        sessionId = null;
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");

        PreparedStatement ps2 = con.prepareStatement(latestSession);
        ps2.setString(1, loggedinUser.getUserId());
        ResultSet rs2 = ps2.executeQuery();

        while (rs2.next()) {
            sessionId = rs2.getString(1);
        }

        System.out.println("Session ID : " + sessionId);

        rs2.close();

        if (sessionId == null) {
            System.out.println("Create New Session");
            sessionId = username.substring(0, 3) + formatter.format(new Date());
            PreparedStatement ps = con.prepareStatement(insertLoginHist);

            ps.setString(1, sessionId);
            ps.setString(2, username);

            ps.executeUpdate();

            ps.close();
        }

    }

    public void redirect(String to) {
        try {
            if (to.equalsIgnoreCase("dybest")) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext extContext = facesContext.getExternalContext();
                extContext.redirect(dybestURL + "?id=" + username + "&session=" + sessionId);
            } else if (to.equalsIgnoreCase("velvet")) {

            }

        } catch (Exception ex) {
            System.out.println("Failed To Redirect : " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    public String getAuthQuery() {
        return authQuery;
    }

    public void setAuthQuery(String authQuery) {
        this.authQuery = authQuery;
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

    public UserAuth getLoggedinUser() {
        return loggedinUser;
    }

    public void setLoggedinUser(UserAuth loggedinUser) {
        this.loggedinUser = loggedinUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVelvetEnabled() {
        return velvetEnabled;
    }

    public void setVelvetEnabled(boolean velvetEnabled) {
        this.velvetEnabled = velvetEnabled;
    }

    public boolean isDybestEnabled() {
        return dybestEnabled;
    }

    public void setDybestEnabled(boolean dybestEnabled) {
        this.dybestEnabled = dybestEnabled;
    }

    public boolean isPortalEnabled() {
        return portalEnabled;
    }

    public void setPortalEnabled(boolean portalEnabled) {
        this.portalEnabled = portalEnabled;
    }

    public boolean isAddUser() {
        return addUser;
    }

    public void setAddUser(boolean addUser) {
        this.addUser = addUser;
    }

    public String getUserListQuery() {
        return userListQuery;
    }

    public void setUserListQuery(String userListQuery) {
        this.userListQuery = userListQuery;
    }

    public List<UserAuth> getUserList() {

        userList = new ArrayList<UserAuth>();
        try {
            PreparedStatement ps = con.prepareStatement(userListQuery);
            ps.setString(1, loggedinUser.getDybest());
            ps.setString(2, loggedinUser.getVelvet());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserAuth au = new UserAuth();
                au.setUserId(rs.getString("userid"));
                au.setLastLogin(rs.getTimestamp(2));
                au.setDybest(rs.getString("dybest"));
                au.setVelvet(rs.getString("velvet"));
                au.setStatus(rs.getString("status"));

                userList.add(au);
            }

            rs.close();

        } catch (Exception ex) {
            System.out.println("Exception While Fetching Userlist : " + ex.getMessage());
            ex.printStackTrace();
        }

        return userList;
    }

    public String transform(UserAuth user) {
        if (user.getDybest().equalsIgnoreCase("Y") && user.getVelvet().equalsIgnoreCase("Y")) {
            return "DYBEST, VELVET";
        } else if (user.getDybest().equalsIgnoreCase("Y") && user.getVelvet().equalsIgnoreCase("N")) {
            return "DYBEST";
        } else {
            return "VELVET";
        }
    }

    public void setUserList(List<UserAuth> userList) {
        this.userList = userList;
    }

    public String getInsertLoginHist() {
        return insertLoginHist;
    }

    public void setInsertLoginHist(String insertLoginHist) {
        this.insertLoginHist = insertLoginHist;
    }

    public String getUpdateLoginHist() {
        return updateLoginHist;
    }

    public void setUpdateLoginHist(String updateLoginHist) {
        this.updateLoginHist = updateLoginHist;
    }

    public String getLatestSession() {
        return latestSession;
    }

    public void setLatestSession(String latestSession) {
        this.latestSession = latestSession;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isSystemAksesShow() {
        return systemAksesShow;
    }

    public void setSystemAksesShow(boolean systemAksesShow) {
        this.systemAksesShow = systemAksesShow;
    }

    public String getUpdateUserStat() {
        return updateUserStat;
    }

    public void setUpdateUserStat(String updateUserStat) {
        this.updateUserStat = updateUserStat;
    }

    public String getDybestURL() {
        return dybestURL;
    }

    public void setDybestURL(String dybestURL) {
        this.dybestURL = dybestURL;
    }

    public String getVelvetURL() {
        return velvetURL;
    }

    public void setVelvetURL(String velvetURL) {
        this.velvetURL = velvetURL;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public boolean isErrMsgShow() {
        return errMsgShow;
    }

    public void setErrMsgShow(boolean errMsgShow) {
        this.errMsgShow = errMsgShow;
    }

}
