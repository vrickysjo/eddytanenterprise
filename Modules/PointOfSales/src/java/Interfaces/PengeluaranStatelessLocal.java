/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entities.Pengeluaran;
import javax.ejb.Local;

/**
 *
 * @author viriya.putta
 */
@Local
public interface PengeluaranStatelessLocal {
    //public List<DetailPengeluaran> searchPengeluaranDetail(String poNo, DatabaseConnection dbCon, String sqlQuery) throws Exception;

    public String generateNoPOPengeluaran(String jndi, String jnsPengeluaran) throws NullPointerException, Exception;

    public void insertPengeluaran(String jndi, Pengeluaran p) throws Exception;
    
}
