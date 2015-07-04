/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessProcess;

import Entities.Kas;
import Interfaces.KasStatelessLocal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Stateless
public class KasStateless implements KasStatelessLocal {

    private static DataSource ds;
    private static Connection con;
    
    @Override
    public void insertCash(Kas k, String jndi) throws Exception{
        try{
            String q = "insert into cashflow "
                    + "(tanggal_transaksi, catatan, no_po, jumlah, jenis_kas, saldo, "
                    + "created_date, last_upd_date, jenis_transaksi,userid) "
                    + "values (?,?,?,?,?,?,now(),now(),?,?)";
            
            ds = (DataSource) new InitialContext().lookup(jndi);
            con = ds.getConnection();
            
            PreparedStatement ps = con.prepareStatement(q);
            ps.setDate(1, new Date(k.getTglTrans().getTime()));
            ps.setString(2, k.getCatatan());
            ps.setString(3, k.getNoPO());
            ps.setInt(4, k.getJumlah());
            ps.setString(5, k.getJenisKas());
            ps.setInt(6, k.getSaldo());
            ps.setString(7, k.getJenisTrans());
            ps.setString(8, k.getUserid());
            
            ps.executeUpdate();
            ps.close();
            con.close();
            
        }catch(Exception ex){
            con.close();
            throw ex;
        }
    }
    
    @Override
    public int getLastSaldo(String jndi, String jnsKas) throws Exception{
        try{
            String q = "select saldo from cashflow where jenis_kas = ? "
                    + "order by created_date desc limit 0,1";
            
            ds = (DataSource) new InitialContext().lookup(jndi);
            con = ds.getConnection();
            
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, jnsKas);
            ResultSet rs = ps.executeQuery();
            int saldo = 0;
            while(rs.next()){
                saldo = rs.getInt("saldo");
            }
            rs.close();
            ps.close();
            con.close();
            return saldo;
        }catch(Exception ex){
            con.close();
            throw ex;
        }
    }
    
}
