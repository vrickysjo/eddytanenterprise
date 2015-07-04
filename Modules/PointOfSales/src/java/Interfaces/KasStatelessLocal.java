/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entities.Kas;
import javax.ejb.Local;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Local
public interface KasStatelessLocal {

    public void insertCash(Kas k, String jndi) throws Exception;

    public int getLastSaldo(String jndi,String jnsKas) throws Exception;
    
}
