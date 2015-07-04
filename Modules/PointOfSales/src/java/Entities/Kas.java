/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author valentinus.r.sjofyan
 */
public class Kas {
    private int kasId;
    private Date tglTrans;
    private String catatan;
    private String noPO;
    private int jumlah;
    private String jenisKas;
    private int saldo;
    private String jenisTrans;
    private String userid;

    public Kas() {
    }

    public int getKasId() {
        return kasId;
    }

    public void setKasId(int kasId) {
        this.kasId = kasId;
    }

    public Date getTglTrans() {
        return tglTrans;
    }

    public void setTglTrans(Date tglTrans) {
        this.tglTrans = tglTrans;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getNoPO() {
        return noPO;
    }

    public void setNoPO(String noPO) {
        this.noPO = noPO;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getJenisKas() {
        return jenisKas;
    }

    public void setJenisKas(String jenisKas) {
        this.jenisKas = jenisKas;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getJenisTrans() {
        return jenisTrans;
    }

    public void setJenisTrans(String jenisTrans) {
        this.jenisTrans = jenisTrans;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    
    
}
