/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entities.Klien;
import javax.ejb.Local;

/**
 *
 * @author valentinus.r.sjofyan
 */
@Local
public interface KlienStatelessLocal {

    public void insertNewKlien(Klien k, String jndi) throws Exception;
    
    
}
