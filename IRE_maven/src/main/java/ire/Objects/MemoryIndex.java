/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

import java.util.HashMap;

/**
 *
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class MemoryIndex {
    private final char initial;
    private final HashMap<Integer, HashMap<Integer,Double>> index;
    
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
        else if (getClass() != o.getClass()){
            return false;
        }
        MemoryIndex e = (MemoryIndex)o;
        return this.initial == e.initial;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.initial;
        return hash;
    }
}
