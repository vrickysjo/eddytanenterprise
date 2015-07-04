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
 * @author viriya.putta
 */
public class Pengeluaran {
    private String noPO;
    private Date tglTrans;
    private String jnsPengeluaran;
    private String pmbryn;
    private String status;
    private String catatan;
    private Date tglPersetujuan;
    private String userid;
    private String userApp;
    private int totalPengeluaran;
    private String supplier;
    private Date tglJatuhTempo;
    private List<Kas> cashflow = new ArrayList<Kas>();
    
    public Date getTglPersetujuan() {
        return tglPersetujuan;
    }

    public void setTglPersetujuan(Date tglPersetujuan) {
        this.tglPersetujuan = tglPersetujuan;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    
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

    public String getJnsPengeluaran() {
        return jnsPengeluaran;
    }

    public void setJnsPengeluaran(String jnsPengeluaran) {
        this.jnsPengeluaran = jnsPengeluaran;
    }

    public String getPmbryn() {
        return pmbryn;
    }

    public void setPmbryn(String pmbryn) {
        this.pmbryn = pmbryn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getUserApp() {
        return userApp;
    }

    public void setUserApp(String userApp) {
        this.userApp = userApp;
    }

    public int getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public void setTotalPengeluaran(int totalPengeluaran) {
        this.totalPengeluaran = totalPengeluaran;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
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
   
    
}
