/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessProcess;

import Entities.Klien;
import Interfaces.KlienStatelessLocal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Stateless
public class KlienStateless implements KlienStatelessLocal {

    private static Connection con;
    private static DataSource ds;
    
    @Override
    public void insertNewKlien(Klien k, String jndi) throws Exception {
        String q = "insert into klien (nama_klien, alamat, email, no_telp, created_date, last_upd_date, catatan) "
                + "values (?,?,?,?,now(),now(),?) ";

        System.out.println("Datasource lookup connection");
        ds = (DataSource) new InitialContext().lookup(jndi);

        con = ds.getConnection();
        
        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, k.getNama());
        ps.setString(2, k.getAlamat());
        ps.setString(3, k.getEmail());
        ps.setString(4, k.getNoTelp());
        ps.setString(5, k.getCatatan());
        
        ps.executeUpdate();
        ps.close();
        con.close();
    }
    
}
