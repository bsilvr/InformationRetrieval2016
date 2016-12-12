/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

/**
 *
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
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
        Cache.query = query;
    }
    public void setContent(String content){
        Cache.content = content;
    }
}
