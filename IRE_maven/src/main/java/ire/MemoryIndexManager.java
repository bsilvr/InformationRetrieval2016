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
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class MemoryIndexManager {
    private final String indexPath;
    private final Queue<MemoryIndex> indexes;
    
    public MemoryIndexManager(int maxIndex, String indexPath){
        this.indexPath = indexPath;
        this.indexes = new CircularFifoQueue<>(maxIndex);
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
                try (FSTObjectInput in = new FSTObjectInput(new FileInputStream(indexPath + initial))) {
                    i = (HashMap<Integer, HashMap<Integer,Double>>)in.readObject();
                }
            idx = new MemoryIndex(initial, i);
            indexes.add(idx);
            
            } catch (IOException | ClassNotFoundException ex) {
            }
        }
        return idx;
    }
}
