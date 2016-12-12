/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

/**
 *
 * @author bernardo
 */
public class Query {
    private static String query;
    
    public Query(String query){
        this.query = query;
    }
    public Query(){
        
    }
    
    public String getQuery(){
        return query;
    }
}
