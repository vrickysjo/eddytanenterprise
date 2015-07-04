/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessProcess;

import Interfaces.ReportingStatelessLocal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Stateless
public class ReportingStateless implements ReportingStatelessLocal {
    private static DataSource ds;
    private static Connection con;
    
    @Override
    public void inventoriReport(String jndi) throws Exception{
        ds = (DataSource) new InitialContext().lookup(jndi);
        Map<String, Object> param = new HashMap<String, Object>();
        
        JasperReport report = JasperCompileManager.compileReport("C:\\Users\\valentinus.r.sjofyan\\JaspersoftWorkspace\\MyReports\\Inventori.jrxml");
        JasperPrint JPrint = JasperFillManager.fillReport(report, param, ds.getConnection());
        JasperViewer.viewReport(JPrint, false);
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
