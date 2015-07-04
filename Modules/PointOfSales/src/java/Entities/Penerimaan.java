/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author valentinus.r.sjofyan
 */
public class Penerimaan {
    private String noPO;
    private Date tglTrans;
    private String jnsPenerimaan;
    private String pmbyrn;
    private int grossPenerimaan;
    private int netPenerimaan;
    private int diskon;
    private String catatan;
    private String userid;
    private Date tglJatuhTempo;
    private int sisaPembayaran;
    private List<Kas> cashflow = new ArrayList<Kas>();

    public String getNoPO() {
        return noPO;
    }

    public void setNoPO(String noPO) {
        this.noPO = noPO;
    }

    public Date getTglTrans() {
        return tglTrans;
    }

    public void setTglTrans(Date tglTrans) {
        this.tglTrans = tglTrans;
    }

    public String getJnsPenerimaan() {
        return jnsPenerimaan;
    }

    public void setJnsPenerimaan(String jnsPenerimaan) {
        this.jnsPenerimaan = jnsPenerimaan;
    }

    public String getPmbyrn() {
        return pmbyrn;
    }

    public void setPmbyrn(String pmbyrn) {
        this.pmbyrn = pmbyrn;
    }

    public int getGrossPenerimaan() {
        return grossPenerimaan;
    }

    public void setGrossPenerimaan(int grossPenerimaan) {
        this.grossPenerimaan = grossPenerimaan;
    }

    public int getNetPenerimaan() {
        return netPenerimaan;
    }

    public void setNetPenerimaan(int netPenerimaan) {
        this.netPenerimaan = netPenerimaan;
    }

    public int getDiskon() {
        return diskon;
    }

    public void setDiskon(int diskon) {
        this.diskon = diskon;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getTglJatuhTempo() {
        return tglJatuhTempo;
    }

    public void setTglJatuhTempo(Date tglJatuhTempo) {
        this.tglJatuhTempo = tglJatuhTempo;
    }

    public List<Kas> getCashflow() {
        return cashflow;
    }

    public void setCashflow(List<Kas> cashflow) {
        this.cashflow = cashflow;
    }

    public int getSisaPembayaran() {
        return sisaPembayaran;
    }

    public void setSisaPembayaran(int sisaPembayaran) {
        this.sisaPembayaran = sisaPembayaran;
    }

    

    
    
}
