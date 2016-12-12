/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.Objects.MemoryIndex;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.nustaq.serialization.FSTObjectInput;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class MemoryIndexManager {
    private final int maxIndex;
    private final String indexPath;
    Queue<MemoryIndex> indexes;
    
    public MemoryIndexManager(int maxIndex, String indexPath){
        this.maxIndex = maxIndex;
        this.indexPath = indexPath;
        indexes = new CircularFifoQueue<>(maxIndex);
    }
    
    public MemoryIndex loadIndex(char initial){
        MemoryIndex idx = new MemoryIndex(initial);
        if(indexes.contains(idx)){
            for(MemoryIndex m : indexes){
                if(m.equals(idx)){
                    idx = m;
                    break;
                }
            }
        }else{
            try {
            HashMap<Integer, HashMap<Integer,Double>> i;
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(indexPath + initial));
            i = (HashMap<Integer, HashMap<Integer,Double>>)in.readObject();
            in.close();
            idx = new MemoryIndex(initial, i);
            
            } catch (IOException | ClassNotFoundException ex) {
            }
        }
        return idx;
    }
}
