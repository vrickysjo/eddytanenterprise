/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessProcess;

import Entities.Inventaris;
import Entities.InventarisHist;
import Interfaces.InventarisStatelessLocal;
import java.sql.Connection;
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
public class InventarisStateless implements InventarisStatelessLocal {

    private static DataSource ds;
    private static Connection con;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Inventaris getInventarisByName(String jndi, String invName) throws Exception {
        String q = "Select * from inventori where nama_barang = ?";
        if (ds == null) {
            ds = (DataSource) new InitialContext().lookup(jndi);
        }

        if (con == null || con.isClosed()) {
            con = ds.getConnection();
        }

        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, invName);
        ResultSet rs = ps.executeQuery();
        Inventaris i = new Inventaris();
        while (rs.next()) {
            i.setInvName(rs.getString("nama_barang"));
            i.setInvType(rs.getString("tipe_barang"));
            i.setPrice(rs.getInt("harga"));
            i.setStock(rs.getInt("kuantitas"));
            i.setInvHist(getInvHist(jndi, rs.getString("nama_barang")));
        }

        rs.close();
        ps.close();
        con.close();

        return i;
    }

    @Override
    public List<String> getInvNameList(String jndi) throws Exception {
        String q = "Select distinct(nama_barang) from inventori";
        if (ds == null) {
            ds = (DataSource) new InitialContext().lookup(jndi);
        }

        if (con == null || con.isClosed()) {
            con = ds.getConnection();
        }
        PreparedStatement ps = con.prepareStatement(q);

        List<String> nameList = new ArrayList<String>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            nameList.add(rs.getString("nama_barang"));
        }

        rs.close();
        ps.close();
        con.close();

        return nameList;

    }

    @Override
    public List<InventarisHist> getInvHist(String jndi, String invName) throws Exception {
        String q = "Select * from inventori_hist where nama_barang = ? order by created_date";
        if (ds == null) {
            ds = (DataSource) new InitialContext().lookup(jndi);
        }

        if (con == null || con.isClosed()) {
            con = ds.getConnection();
        }
        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, invName);
        List<InventarisHist> invHistList = new ArrayList<InventarisHist>();

        ResultSet rs2 = ps.executeQuery();
        while (rs2.next()) {
            InventarisHist invHist = new InventarisHist();
            invHist.setInvName(rs2.getString("nama_barang"));
            invHist.setQty(rs2.getInt("kuantitas"));
            invHist.setStock(rs2.getInt("stock"));
            invHist.setNote(rs2.getString("catatan"));

            invHistList.add(invHist);
        }

        rs2.close();
        ps.close();
        con.close();

        return invHistList;

    }

    @Override
    public void modifyInv(Inventaris i, String jndi) throws Exception {

        String q = "update inventori set kuantitas = ?, harga = ?, last_upd_date = now() "
                + "where nama_barang = ?";

        ds = (DataSource) new InitialContext().lookup(jndi);

        con = ds.getConnection();

        PreparedStatement ps = con.prepareStatement(q);
        ps.setInt(1, i.getStock());
        ps.setInt(2, i.getPrice());
        ps.setString(3, i.getInvName());

        ps.executeUpdate();

        ps.close();
        con.close();

    }

    @Override
    public void insertInvHist(InventarisHist ih, String jndi) throws Exception {
        String q = "insert into inventori_hist "
                + "(nama_barang, kuantitas, catatan, created_date, stock) "
                + "values "
                + "(?,?,?,now(),?)";

        if (ds == null) {
            ds = (DataSource) new InitialContext().lookup(jndi);
        }

        if (con == null || con.isClosed()) {
            con = ds.getConnection();
        }
        PreparedStatement ps = con.prepareStatement(q);

        ps.setString(1, ih.getInvName());
        ps.setInt(2, ih.getQty());
        ps.setString(3, ih.getNote());
        ps.setInt(4, ih.getStock());

        ps.executeUpdate();
        ps.close();
        con.close();

    }

    @Override
    public void insertInv(Inventaris i, String jndi) throws Exception {
        String q = "insert into inventori "
                + "(nama_barang, kuantitas, harga, tipe_barang, created_date, last_upd_date) "
                + "values "
                + "(?,?,?,?,now(),now())";

        ds = (DataSource) new InitialContext().lookup(jndi);

        con = ds.getConnection();

        PreparedStatement ps = con.prepareStatement(q);
        ps.setString(1, i.getInvName());
        ps.setInt(2, i.getStock());
        ps.setInt(3, i.getPrice());
        ps.setString(4, i.getInvType());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    @Override
    public List<Inventaris> getInvList(String jndi) throws Exception {
        String q = "Select * from inventori order by last_upd_date desc";

        List<Inventaris> invList = new ArrayList<Inventaris>();

        if (ds == null) {
            ds = (DataSource) new InitialContext().lookup(jndi);
        }

        if (con == null || con.isClosed()) {
            con = ds.getConnection();
        }
        PreparedStatement ps = con.prepareStatement(q);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Inventaris inv = new Inventaris();
            inv.setInvName(rs.getString("nama_barang"));
            inv.setStock(rs.getInt("kuantitas"));
            inv.setPrice(rs.getInt("harga"));
            inv.setInvType(rs.getString("tipe_barang"));
            inv.setInvHist(getInvHist(jndi, rs.getString("nama_barang")));
            invList.add(inv);
        }

        rs.close();
        ps.close();

        con.close();

        return invList;

    }

}
