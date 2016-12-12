/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

import java.util.HashMap;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class MemoryIndex {
    private char initial;
    private HashMap<Integer, HashMap<Integer,Double>> index;
    
    public MemoryIndex(char i, HashMap<Integer, HashMap<Integer,Double>> idx){
        this.initial = i;
        this.index = idx;
    }
    
    public MemoryIndex(char i){
        this.initial = i;
        this.index = null;
    }

    public char getInitial() {
        return initial;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getIndex() {
        return index;
    }
    
    @Override
    public boolean equals(Object o){
        if(o==null){
            return false;
        }
        else if(this==o){
            return true;
        }
        MemoryIndex e = (MemoryIndex)o;
        return this.initial == e.initial;
    }
}
