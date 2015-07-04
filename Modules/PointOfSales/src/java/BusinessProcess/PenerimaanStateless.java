/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessProcess;

import Entities.Penerimaan;
import Interfaces.PenerimaanStatelessLocal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Stateless
public class PenerimaanStateless implements PenerimaanStatelessLocal {

    private static DataSource ds;
    private static Connection con;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public String generateNoPOPenerimaan(String jndi, String jnsPenerimaan) throws Exception {
        String q = "select po_penerimaan "
                + "from penerimaan "
                + "where jenis_penerimaan = ? "
                + "order by created_date desc "
                + "limit 0,1";

        System.out.println("Datasource lookup connection");
        ds = (DataSource) new InitialContext().lookup(jndi);

        con = ds.getConnection();

        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, jnsPenerimaan);
        ResultSet rs = ps.executeQuery();

        
        String po = "";
        while (rs.next()) {
            po = rs.getString(1);
        }

        rs.close();
        ps.close();
        con.close();

        int nextPONo = Integer.parseInt(po.substring(3)) + 1;
        int poLackLength = po.length() - (po.substring(0, 3) + nextPONo).length();
        String newPONo = po.substring(0, 3);

        for (int i = 0; i < poLackLength; i++) {
            newPONo = newPONo + "0";
        }

        newPONo = newPONo + nextPONo;

        return newPONo;

    }

    @Override
    public void insertPenerimaan(Penerimaan p, String jndi) throws Exception {
        String q = "insert into penerimaan (po_penerimaan,tanggal_transaksi, gross_penerimaan, "
                + "pembayaran, catatan, jenis_penerimaan, diskon, userid, created_date, last_upd_date, "
                + "net_penerimaan, tgl_jatuh_tempo, sisa_pembayaran) "
                + "values (?,?,?,?,?,?,?,?,now(),now(),?,?,?)";
        
        System.out.println("Datasource lookup connection");
        ds = (DataSource) new InitialContext().lookup(jndi);
        
        con = ds.getConnection();

        PreparedStatement ps = con.prepareStatement(q);

        ps.setString(1, p.getNoPO());
        ps.setDate(2, new Date(p.getTglTrans().getTime()));
        ps.setInt(3, p.getGrossPenerimaan());
        ps.setString(4, p.getPmbyrn());
        ps.setString(5, p.getCatatan());
        ps.setString(6, p.getJnsPenerimaan());
        ps.setInt(7, p.getDiskon());
        ps.setString(8, p.getUserid());
        ps.setInt(9, p.getNetPenerimaan());
        ps.setDate(10, new Date(p.getTglJatuhTempo().getTime()));
        ps.setInt(11, p.getSisaPembayaran());
        
        ps.executeUpdate();
        ps.close();
        con.close();
    }
    
    @Override
    public void updatePenerimaan(Penerimaan p, String jndi) throws Exception {
        String q = "update penerimaan set tanggal_transaksi = ?, gross_penerimaan = ?, "
                + "net_penerimaan = ?, pembayaran = ?, catatan = ?, jenis_penerimaan = ?, "
                + "diskon = ?, userid = ?, last_upd_date = now(), tgl_jatuh_tempo = ?, "
                + "sisa_pembayaran = ? where po_penerimaan = ?";
        System.out.println("Datasource lookup connection");
        ds = (DataSource) new InitialContext().lookup(jndi);
        
        con = ds.getConnection();

        PreparedStatement ps = con.prepareStatement(q);
        ps.setDate(1, new Date(p.getTglTrans().getTime()));
        ps.setInt(2, p.getGrossPenerimaan());
        ps.setInt(3, p.getNetPenerimaan());
        ps.setString(4, p.getPmbyrn());
        ps.setString(5, p.getCatatan());
        ps.setString(6, p.getJnsPenerimaan());
        ps.setInt(7, p.getDiskon());
        ps.setString(8, p.getUserid());
        ps.setDate(9, new Date(p.getTglJatuhTempo().getTime()));
        ps.setInt(10, p.getSisaPembayaran());
        ps.setString(11, p.getNoPO());
        
        ps.executeUpdate();
        ps.close();
        con.close();
    }
    
    @Override
    public List<String> NoPOPiutangList(String jndi) throws Exception{
        String q = "select po_penerimaan "
                + "from penerimaan "
                + "where sisa_pembayaran > 0 "
                + "order by created_date desc";

        System.out.println("Datasource lookup connection");
        ds = (DataSource) new InitialContext().lookup(jndi);

        con = ds.getConnection();
        List<String> poList = new ArrayList<String>();
        PreparedStatement ps = con.prepareStatement(q);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            poList.add(rs.getString(1));
        }
        
        rs.close();
        ps.close();
        con.close();
        
        return poList;
    }
    
    @Override
    public Penerimaan searchPenerimaaanByNoPO(String noPO, String jndi) throws Exception {
        String q = "select * "
                + "from penerimaan "
                + "where po_penerimaan = ?";

        System.out.println("Datasource lookup connection");
        ds = (DataSource) new InitialContext().lookup(jndi);

        con = ds.getConnection();
        Penerimaan p = new Penerimaan();
        
        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, noPO);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
            p.setCatatan(rs.getString("catatan"));
            p.setDiskon(rs.getInt("diskon"));
            p.setGrossPenerimaan(rs.getInt("gross_penerimaan"));
            p.setJnsPenerimaan(rs.getString("jenis_penerimaan"));
            p.setNetPenerimaan(rs.getInt("net_penerimaan"));
            p.setNoPO(rs.getString("po_penerimaan"));
            p.setPmbyrn(rs.getString("pembayaran"));
            p.setSisaPembayaran(rs.getInt("sisa_pembayaran"));
            p.setTglJatuhTempo(rs.getDate("tgl_jatuh_tempo"));
            p.setTglTrans(rs.getDate("tanggal_transaksi"));
            p.setUserid(rs.getString("userid"));
        }
        
        rs.close();
        ps.close();
        con.close();
        return p;
    }
}
