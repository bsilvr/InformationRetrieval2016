/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;
/*
import ire.Indexer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
/*public class IW_Worker extends Thread {
    private final String basefolder = "indexes/";
    private static int indexCount = 0;
    private Indexer indexer;
    public IW_Worker(){
        indexer = new Indexer();
    }
    
    @Override
    public void run(){
        HashMap<Integer, HashMap<Integer,Double>> dict = indexer.writeIndex();
        
        ObjectOutputStream oos = null;
        try {
            String filename = basefolder + "index_" + indexCount;
            indexCount++;
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(dict);
            oos.close();
        } catch (IOException ex) {
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
            }
        }
        
        System.err.println("Finished writing index....");
    }
}
*/