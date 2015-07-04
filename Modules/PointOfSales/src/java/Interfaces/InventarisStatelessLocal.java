/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entities.Inventaris;
import Entities.InventarisHist;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Local
public interface InventarisStatelessLocal {

    public List<Inventaris> getInvList(String jndi) throws Exception;

    public List<String> getInvNameList(String jndi) throws Exception;

    public List<InventarisHist> getInvHist(String jndi, String invName) throws Exception;

    public Inventaris getInventarisByName(String jndi, String invName) throws Exception;

    public void modifyInv(Inventaris i, String jndi) throws Exception;

    public void insertInvHist(InventarisHist ih, String jndi) throws Exception;

    public void insertInv(Inventaris i, String jndi) throws Exception;
    
}
