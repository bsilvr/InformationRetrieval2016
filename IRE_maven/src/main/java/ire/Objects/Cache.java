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
public class Cache {
    private static String query;
    private static String content;
    
    public String getQuery(){
        return query;
    }
    
    public String getContent(){
        return content;
    }
    public void setQuery(String query){
        this.query = query;
    }
    public void setContent(String content){
        this.content = content;
    }
}
