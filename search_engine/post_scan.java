/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_engine;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chandra
 */
public class post_scan {
    public void merge_all()
    {
        System.out.println("Merging id:title started");
        merge_idtitle it = new merge_idtitle();
        it.merge();
        System.out.println("Merging indexes started");
        kway_merge km = new kway_merge();
        km.merge();
        try {
            QueryProcessing.processQuery();
        } catch (IOException ex) {
            Logger.getLogger(post_scan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
