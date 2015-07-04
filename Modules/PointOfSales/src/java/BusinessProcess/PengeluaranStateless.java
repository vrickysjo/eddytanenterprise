/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessProcess;

import Entities.Pengeluaran;
import Interfaces.PengeluaranStatelessLocal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author viriya.putta
 */
@Stateless
public class PengeluaranStateless implements PengeluaranStatelessLocal {

    private static DataSource ds;
    private static Connection con;

    @Override
    public void insertPengeluaran(String jndi, Pengeluaran p) throws Exception {
        String q = "insert into pengeluaran (po_pengeluaran, tanggal_transaksi, pembayaran, "
                + "catatan, userid, status, approvalid, created_date, last_upd_date, jenis_pengeluaran, "
                + "total_pengeluaran,supplier,tgl_jatuh_tempo) "
                + "values (?,?,?,?,?,?,?,now(),now(),?,?,?,?)";

        ds = (DataSource) new InitialContext().lookup(jndi);

        con = ds.getConnection();

        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, p.getNoPO());
        ps.setDate(2, new java.sql.Date(p.getTglTrans().getTime()));
        ps.setString(3, p.getPmbryn());
        ps.setString(4, p.getCatatan());
        ps.setString(5, p.getUserid());
        ps.setString(6, p.getStatus());
        ps.setString(7, p.getUserApp());
        ps.setString(8, p.getJnsPengeluaran());
        ps.setInt(9, p.getTotalPengeluaran());
        ps.setString(10, p.getSupplier());
        ps.setDate(11, new Date(p.getTglJatuhTempo().getTime()));

        ps.executeUpdate();
        ps.close();
        con.close();
    }

    @Override
    public String generateNoPOPengeluaran(String jndi, String jnsPengeluaran) throws NullPointerException, Exception {
        String q = "select po_pengeluaran "
                + "from pengeluaran "
                + "where jenis_pengeluaran = ? "
                + "order by created_date desc "
                + "limit 0,1";

        System.out.println("Datasource lookup connection");
        ds = (DataSource) new InitialContext().lookup(jndi);

        Connection con = ds.getConnection();

        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, jnsPengeluaran);
        ResultSet rs = ps.executeQuery();
        String po = "";
        while (rs.next()) {
            po = rs.getString(1);
        }

        rs.close();
        ps.close();
        con.close();

        if (!po.equals("")) {
            int nextPONo = Integer.parseInt(po.substring(3)) + 1;
            int poLackLength = po.length() - (po.substring(0, 3) + nextPONo).length();
            String newPONo = po.substring(0, 3);

            for (int i = 0; i < poLackLength; i++) {
                newPONo = newPONo + "0";
            }

            newPONo = newPONo + nextPONo;

            return newPONo;
        } else {
            return null;
        }

    }

    /*
     @Override
     public List<DetailPengeluaran> searchPengeluaranDetail(String poNo, DatabaseConnection dbCon, String sqlQuery) throws Exception {
     Connection con = getConnection(dbCon);
     PreparedStatement ps = con.prepareStatement(sqlQuery);
     ps.setString(1, poNo);
     ResultSet rs = ps.executeQuery();

     List<DetailPengeluaran> listDetail = new ArrayList<DetailPengeluaran>();

     while (rs.next()) {
     DetailPengeluaran dp = new DetailPengeluaran();
            
     listDetail.add(dp);
     }
        
     rs.close();
        
     con.close();

     return listDetail;

     }
     */
}
