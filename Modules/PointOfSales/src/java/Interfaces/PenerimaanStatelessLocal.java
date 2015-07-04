/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entities.Penerimaan;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Local
public interface PenerimaanStatelessLocal {

    public String generateNoPOPenerimaan(String jndi, String jnsPenerimaan) throws Exception;

    public void insertPenerimaan(Penerimaan p, String jndi) throws Exception;

    public List<String> NoPOPiutangList(String jndi) throws Exception;

    public Penerimaan searchPenerimaaanByNoPO(String noPO, String jndi) throws Exception;

    public void updatePenerimaan(Penerimaan p, String jndi) throws Exception;
    
}
