/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Post {
    private int nDoc;
    private double wtnorm;
    
    public Post(int nDoc){
        this.nDoc = nDoc;
    }
    public Post(int nDoc, double weight){
        this.nDoc = nDoc;
        this.wtnorm = weight;
    }
    
    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        
        Post p = (Post) o;
        return this.nDoc == p.nDoc;
    }
    
    public void setWtmorn(double w){
        this.wtnorm = w;
    }
    
    public int getNdoc(){
        return this.nDoc;
    }

}
