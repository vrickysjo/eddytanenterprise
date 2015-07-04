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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author valentinus.r.sjofyan
 */
@ManagedBean
@ApplicationScoped
public class UserBean {

    /**
     * Creates a new instance of UserBean
     */
    private String newUser;
    private String newPassword;
    private String jamKerjaStart;
    private String jamKerjaEnd;
    
    private String lovQuery;

    private List<String> posAuthDb = new ArrayList<String>();
    private List<String> payrollAuthDb = new ArrayList<String>();
    private List<String> glAuthDb = new ArrayList<String>();
    private List<String> posAuthVl = new ArrayList<String>();
    private List<String> payrollAuthVl = new ArrayList<String>();
    private List<String> glAuthVl = new ArrayList<String>();

    private List<String> posCatCh = new ArrayList<String>();
    private List<String> posCatChVelvet = new ArrayList<String>();
    private List<String> payrollCatCh = new ArrayList<String>();
    private List<String> payrollCatChVelvet = new ArrayList<String>();
    private List<String> bbCatCh = new ArrayList<String>();
    private List<String> bbCatChVelvet = new ArrayList<String>();

    private Map<String, String> posCat = new HashMap<String, String>();
    private Map<String, String> posCatVelvet = new HashMap<String, String>();
    private Map<String, String> payrollCat = new HashMap<String, String>();
    private Map<String, String> payrollCatVelvet = new HashMap<String, String>();
    private Map<String, String> bbCat = new HashMap<String, String>();
    private Map<String, String> bbCatVelvet = new HashMap<String, String>();

    private HashMap<String, String> generalOpt = new HashMap<String, String>();
    private HashMap<String, String> generalOptVelvet = new HashMap<String, String>();

    private String check = "Check All";
    private String checkVv = "Check All";

    private boolean addUserDb;
    private boolean addUserVl;

    private String vvUsername;
    private String vvPassword;
    private String vvUrl;

    private String dbUsername;
    private String dbPassword;
    private String dbUrl;

    private String plUsername;
    private String plPassword;
    private String plUrl;

    private Connection velvetCon;
    private Connection dybestCon;
    private Connection portalCon;

    private String insertLoginSql;
    private String insertHakAksesSql;
    private String deleteUserSql;
    private String deleteAccessRightsSql;
    private String resetUserSql;

    public UserBean() {
    }

    @PostConstruct
    public void getConnection() {
        try {
            System.out.println("Initiating User Bean");
            Class.forName("com.mysql.jdbc.Driver");
            dybestCon = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            velvetCon = DriverManager.getConnection(vvUrl, vvUsername, vvPassword);
            portalCon = DriverManager.getConnection(plUrl, plUsername, plPassword);

            //Populating DYBEST LOV
            PreparedStatement ps = dybestCon.prepareStatement(lovQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("LOV_GROUP").equalsIgnoreCase("POS")) {
                    posCat.put(rs.getString("LOV_NAME"), rs.getString("LOV_NAME"));
                } else if (rs.getString("LOV_GROUP").equalsIgnoreCase("PAYROLL")) {
                    payrollCat.put(rs.getString("LOV_NAME"), rs.getString("LOV_NAME"));
                } else if (rs.getString("LOV_GROUP").equalsIgnoreCase("BUKUBESAR")) {
                    bbCat.put(rs.getString("LOV_NAME"), rs.getString("LOV_NAME"));
                } else {
                    generalOpt.put(rs.getString("LOV_NAME"), rs.getString("LOV_NAME"));
                }
            }
            rs.close();

            //Populating VELVET LOV
            PreparedStatement ps2 = velvetCon.prepareStatement(lovQuery);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                if (rs2.getString("LOV_GROUP").equalsIgnoreCase("POS")) {
                    posCatVelvet.put(rs2.getString("LOV_NAME"), rs2.getString("LOV_NAME"));
                } else if (rs2.getString("LOV_GROUP").equalsIgnoreCase("PAYROLL")) {
                    payrollCatVelvet.put(rs2.getString("LOV_NAME"), rs2.getString("LOV_NAME"));
                } else if (rs2.getString("LOV_GROUP").equalsIgnoreCase("BUKUBESAR")) {
                    bbCatVelvet.put(rs2.getString("LOV_NAME"), rs2.getString("LOV_NAME"));
                } else {
                    generalOptVelvet.put(rs2.getString("LOV_NAME"), rs2.getString("LOV_NAME"));
                }
            }

            rs2.close();

        } catch (Exception ex) {
            System.out.println("Failed to open DB Connection");
            ex.printStackTrace();
        }
    }

    @PreDestroy
    public void clear() {
        try {
            System.out.println("Clearing User Bean");
            dybestCon.close();
            velvetCon.close();
        } catch (Exception ex) {
            System.out.println("Failed to close DB Connection");
            ex.printStackTrace();
        }

    }

    public void checkAllDb(ActionEvent event) {
        if (check.equalsIgnoreCase("Check All")) {
            Iterator it = generalOpt.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                posAuthDb.add(pair.getKey().toString());
                payrollAuthDb.add(pair.getKey().toString());
                glAuthDb.add(pair.getKey().toString());
            }

            it = posCat.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                posCatCh.add(pair.getKey().toString());
            }

            it = payrollCat.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                payrollCatCh.add(pair.getKey().toString());
            }

            it = bbCat.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                bbCatCh.add(pair.getKey().toString());
            }

            addUserDb = true;

            check = "Uncheck All";
        } else {
            posAuthDb.clear();
            payrollAuthDb.clear();
            glAuthDb.clear();

            posCatCh.clear();
            payrollCatCh.clear();
            bbCatCh.clear();

            addUserDb = false;

            check = "Check All";
        }

    }

    public void checkAllVv(ActionEvent event) {
        if (checkVv.equalsIgnoreCase("Check All")) {
            Iterator it = generalOptVelvet.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                posAuthVl.add(pair.getKey().toString());
                payrollAuthVl.add(pair.getKey().toString());
                glAuthVl.add(pair.getKey().toString());
            }

            it = posCatVelvet.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                posCatChVelvet.add(pair.getKey().toString());
            }

            it = payrollCatVelvet.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                payrollCatChVelvet.add(pair.getKey().toString());
            }

            it = bbCatVelvet.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                bbCatChVelvet.add(pair.getKey().toString());
            }

            addUserVl = true;

            checkVv = "Uncheck All";
        } else {
            posAuthVl.clear();
            payrollAuthVl.clear();
            glAuthVl.clear();

            posCatChVelvet.clear();
            payrollCatChVelvet.clear();
            bbCatChVelvet.clear();

            addUserVl = false;

            checkVv = "Check All";
        }
    }

    public void resetUser(UserAuth au){
        try{
            PreparedStatement ps = portalCon.prepareStatement(resetUserSql);
            ps.setString(1, "123");
            ps.setString(2, "Active");
            ps.setString(3, au.getUserId());
            ps.executeUpdate();
            
            ps.close();
        }catch(Exception ex){
            System.out.println("Exception when reset User : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public String suspendUser(UserAuth au){
        try{
            PreparedStatement ps = portalCon.prepareStatement(resetUserSql);
            ps.setString(1, "Suspend");
            ps.setString(2, au.getPassword());
            ps.setString(3, au.getUserId());
            
            ps.executeUpdate();
            
            ps.close();
        }catch(Exception ex){
            System.out.println("Exception when reset User : " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return "index.xhtml";
    }
    
    public String deleteUser(UserAuth au) {
        try {
            if (au.getDybest().equalsIgnoreCase("Y")) {
                PreparedStatement ps2 = dybestCon.prepareStatement(deleteAccessRightsSql);
                ps2.setString(1, au.getUserId());
                ps2.executeUpdate();
                
                ps2.close();
            }

            if (au.getVelvet().equalsIgnoreCase("Y")) {
                PreparedStatement ps3 = velvetCon.prepareStatement(deleteAccessRightsSql);
                ps3.setString(1, au.getUserId());
                ps3.executeUpdate();
                
                ps3.close();
            }

            PreparedStatement ps = portalCon.prepareStatement(deleteUserSql);
            ps.setString(1, au.getUserId());
            ps.executeUpdate();
            
            ps.close();

        } catch (SQLException ex) {
            System.out.println("Exception while deleteing User : " + ex.getMessage());
            ex.printStackTrace();
        }
        return "user.xhtml";
        
    }

    public String addUser() {
        try {
            String portal = "";
            String dybest = "";
            String velvet = "";

            if (addUserDb || addUserVl) {
                portal = "Y";
                addUserDb = false;
                addUserVl = false;
            } else {
                portal = "N";
            }

            if (!posCatCh.isEmpty() || !payrollCatCh.isEmpty() || !bbCatCh.isEmpty()) {
                dybest = "Y";
            } else {
                dybest = "N";
            }

            if (!posCatChVelvet.isEmpty() || !payrollCatChVelvet.isEmpty() || !bbCatChVelvet.isEmpty()) {
                velvet = "Y";
            } else {
                velvet = "N";
            }

            PreparedStatement ps = portalCon.prepareStatement(insertLoginSql);

            ps.setString(1, newUser);
            ps.setString(2, newPassword);
            ps.setString(3, "Active");
            ps.setString(4, portal);
            ps.setString(5, dybest);
            ps.setString(6, velvet);
            ps.setString(7, jamKerjaStart);
            ps.setString(8, jamKerjaEnd);

            ps.executeUpdate();

            //Insert to DYBEST
            PreparedStatement ps2 = dybestCon.prepareStatement(insertHakAksesSql);

            if (dybest.equalsIgnoreCase("Y")) {
                for (String page : posCatCh) {
                    ps2.setString(1, newUser);
                    ps2.setString(2, page);
                    ps2.setString(3, posAuthDb.contains("Insert") ? "Y" : "N");
                    ps2.setString(4, posAuthDb.contains("Update") ? "Y" : "N");

                    ps2.executeUpdate();
                }
                posCatCh.clear();
                posAuthDb.clear();

                for (String page : payrollCatCh) {
                    ps2.setString(1, newUser);
                    ps2.setString(2, page);
                    ps2.setString(3, payrollAuthDb.contains("Insert") ? "Y" : "N");
                    ps2.setString(4, payrollAuthDb.contains("Update") ? "Y" : "N");

                    ps2.executeUpdate();
                }
                payrollCatCh.clear();
                payrollAuthDb.clear();

                for (String page : bbCatCh) {
                    ps2.setString(1, newUser);
                    ps2.setString(2, page);
                    ps2.setString(3, glAuthDb.contains("Insert") ? "Y" : "N");
                    ps2.setString(4, glAuthDb.contains("Update") ? "Y" : "N");

                    ps2.executeUpdate();
                }
                bbCatCh.clear();
                glAuthDb.clear();

            }

            //Insert to VELVET
            PreparedStatement ps3 = velvetCon.prepareStatement(insertHakAksesSql);

            if (velvet.equalsIgnoreCase("Y")) {
                for (String page : posCatChVelvet) {
                    ps3.setString(1, newUser);
                    ps3.setString(2, page);
                    ps3.setString(3, posAuthVl.contains("Insert") ? "Y" : "N");
                    ps3.setString(4, posAuthVl.contains("Update") ? "Y" : "N");

                    ps3.executeUpdate();
                }
                posCatChVelvet.clear();
                posAuthVl.clear();

                for (String page : payrollCatChVelvet) {
                    ps3.setString(1, newUser);
                    ps3.setString(2, page);
                    ps3.setString(3, payrollAuthVl.contains("Insert") ? "Y" : "N");
                    ps3.setString(4, payrollAuthVl.contains("Update") ? "Y" : "N");

                    ps3.executeUpdate();
                }
                payrollCatChVelvet.clear();
                payrollAuthVl.clear();

                for (String page : bbCatChVelvet) {
                    ps3.setString(1, newUser);
                    ps3.setString(2, page);
                    ps3.setString(3, glAuthVl.contains("Insert") ? "Y" : "N");
                    ps3.setString(4, glAuthVl.contains("Update") ? "Y" : "N");

                    ps3.executeUpdate();
                }
                bbCatChVelvet.clear();
                glAuthVl.clear();

            }
            
            newUser = "";
            jamKerjaStart = "";
            jamKerjaEnd = "";

            return "user.xhtml";

        } catch (Exception ex) {
            System.out.println("Exception While Creating a New User : " + ex.getMessage());
            ex.printStackTrace();
            return "TambahPengguna.xhtml";
        }
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getNewUser() {
        return newUser;
    }

    public void setNewUser(String newUser) {
        this.newUser = newUser;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<String> getPosAuthDb() {
        return posAuthDb;
    }

    public void setPosAuthDb(List<String> posAuth) {
        this.posAuthDb = posAuth;
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

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public List<String> getPayrollAuthDb() {
        return payrollAuthDb;
    }

    public void setPayrollAuthDb(List<String> payrollAuth) {
        this.payrollAuthDb = payrollAuth;
    }

    public List<String> getGlAuthDb() {
        return glAuthDb;
    }

    public void setGlAuthDb(List<String> glAuth) {
        this.glAuthDb = glAuth;
    }

    public boolean isAddUserDb() {
        return addUserDb;
    }

    public void setAddUserDb(boolean addUserDb) {
        this.addUserDb = addUserDb;
    }

    public List<String> getPosAuthVl() {
        return posAuthVl;
    }

    public void setPosAuthVl(List<String> posAuthVl) {
        this.posAuthVl = posAuthVl;
    }

    public List<String> getPayrollAuthVl() {
        return payrollAuthVl;
    }

    public void setPayrollAuthVl(List<String> payrollAuthVl) {
        this.payrollAuthVl = payrollAuthVl;
    }

    public List<String> getGlAuthVl() {
        return glAuthVl;
    }

    public void setGlAuthVl(List<String> glAuthVl) {
        this.glAuthVl = glAuthVl;
    }

    public List<String> getPosCatCh() {
        return posCatCh;
    }

    public void setPosCatCh(List<String> posCatCh) {
        this.posCatCh = posCatCh;
    }

    public List<String> getPosCatChVelvet() {
        return posCatChVelvet;
    }

    public void setPosCatChVelvet(List<String> posCatChVelvet) {
        this.posCatChVelvet = posCatChVelvet;
    }

    public List<String> getPayrollCatCh() {
        return payrollCatCh;
    }

    public void setPayrollCatCh(List<String> payrollCatCh) {
        this.payrollCatCh = payrollCatCh;
    }

    public List<String> getPayrollCatChVelvet() {
        return payrollCatChVelvet;
    }

    public void setPayrollCatChVelvet(List<String> payrollCatChVelvet) {
        this.payrollCatChVelvet = payrollCatChVelvet;
    }

    public List<String> getBbCatCh() {
        return bbCatCh;
    }

    public void setBbCatCh(List<String> bbCatCh) {
        this.bbCatCh = bbCatCh;
    }

    public List<String> getBbCatChVelvet() {
        return bbCatChVelvet;
    }

    public void setBbCatChVelvet(List<String> bbCatChVelvet) {
        this.bbCatChVelvet = bbCatChVelvet;
    }

    public Map<String, String> getPosCat() {
        return posCat;
    }

    public void setPosCat(Map<String, String> posCat) {
        this.posCat = posCat;
    }

    public Map<String, String> getPosCatVelvet() {
        return posCatVelvet;
    }

    public void setPosCatVelvet(Map<String, String> posCatVelvet) {
        this.posCatVelvet = posCatVelvet;
    }

    public Map<String, String> getPayrollCat() {
        return payrollCat;
    }

    public void setPayrollCat(Map<String, String> payrollCat) {
        this.payrollCat = payrollCat;
    }

    public Map<String, String> getPayrollCatVelvet() {
        return payrollCatVelvet;
    }

    public void setPayrollCatVelvet(Map<String, String> payrollCatVelvet) {
        this.payrollCatVelvet = payrollCatVelvet;
    }

    public Map<String, String> getBbCat() {
        return bbCat;
    }

    public void setBbCat(Map<String, String> bbCat) {
        this.bbCat = bbCat;
    }

    public Map<String, String> getBbCatVelvet() {
        return bbCatVelvet;
    }

    public void setBbCatVelvet(Map<String, String> bbCatVelvet) {
        this.bbCatVelvet = bbCatVelvet;
    }

    public HashMap<String, String> getGeneralOpt() {
        return generalOpt;
    }

    public void setGeneralOpt(HashMap<String, String> generalOpt) {
        this.generalOpt = generalOpt;
    }

    public String getVvUsername() {
        return vvUsername;
    }

    public void setVvUsername(String vvUsername) {
        this.vvUsername = vvUsername;
    }

    public String getVvPassword() {
        return vvPassword;
    }

    public void setVvPassword(String vvPassword) {
        this.vvPassword = vvPassword;
    }

    public String getVvUrl() {
        return vvUrl;
    }

    public void setVvUrl(String vvUrl) {
        this.vvUrl = vvUrl;
    }

    public String getLovQuery() {
        return lovQuery;
    }

    public void setLovQuery(String lovQuery) {
        this.lovQuery = lovQuery;
    }

    public HashMap<String, String> getGeneralOptVelvet() {
        return generalOptVelvet;
    }

    public void setGeneralOptVelvet(HashMap<String, String> generalOptVelvet) {
        this.generalOptVelvet = generalOptVelvet;
    }

    public String getCheckVv() {
        return checkVv;
    }

    public void setCheckVv(String checkVv) {
        this.checkVv = checkVv;
    }

    public boolean isAddUserVl() {
        return addUserVl;
    }

    public void setAddUserVl(boolean addUserVl) {
        this.addUserVl = addUserVl;
    }

    public String getPlUsername() {
        return plUsername;
    }

    public void setPlUsername(String plUsername) {
        this.plUsername = plUsername;
    }

    public String getPlPassword() {
        return plPassword;
    }

    public void setPlPassword(String plPassword) {
        this.plPassword = plPassword;
    }

    public String getPlUrl() {
        return plUrl;
    }

    public void setPlUrl(String plUrl) {
        this.plUrl = plUrl;
    }

    public String getInsertLoginSql() {
        return insertLoginSql;
    }

    public void setInsertLoginSql(String insertLoginSql) {
        this.insertLoginSql = insertLoginSql;
    }

    public String getInsertHakAksesSql() {
        return insertHakAksesSql;
    }

    public void setInsertHakAksesSql(String insertHakAksesSql) {
        this.insertHakAksesSql = insertHakAksesSql;
    }

    public String getDeleteUserSql() {
        return deleteUserSql;
    }

    public void setDeleteUserSql(String deleteUserSql) {
        this.deleteUserSql = deleteUserSql;
    }

    public String getDeleteAccessRightsSql() {
        return deleteAccessRightsSql;
    }

    public void setDeleteAccessRightsSql(String deleteAccessRightsSql) {
        this.deleteAccessRightsSql = deleteAccessRightsSql;
    }

    public String getResetUserSql() {
        return resetUserSql;
    }

    public void setResetUserSql(String resetUserSql) {
        this.resetUserSql = resetUserSql;
    }

    public String getJamKerjaStart() {
        return jamKerjaStart;
    }

    public void setJamKerjaStart(String jamKerjaStart) {
        this.jamKerjaStart = jamKerjaStart;
    }

    public String getJamKerjaEnd() {
        return jamKerjaEnd;
    }

    public void setJamKerjaEnd(String jamKerjaEnd) {
        this.jamKerjaEnd = jamKerjaEnd;
    }
    
    

}
