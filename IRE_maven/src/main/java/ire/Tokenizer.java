/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Tokenizer {
    private Pattern pattern = Pattern.compile("\\W");
    private Pattern space_char = Pattern.compile("\\s+");
    private Pattern as_space = Pattern.compile("[-()]");
    
    private ArrayList<String> tokens = new ArrayList<>();
    String [] stopWordsList;
    englishStemmer stemmer = new englishStemmer();
    
    public Tokenizer(String [] stopWordsList, Pattern pattern, Pattern space_char, Pattern as_space){
        this.stopWordsList = stopWordsList;
        this.pattern = pattern;
        this.space_char = space_char;
        this.as_space = as_space;
    }
    
    public String[] tokenize(String content, Document doc){
        
        content = as_space.matcher(content).replaceAll("");
        String [] words = space_char.split(content);
        
        for(int i = 0; i < words.length; i++){
            String word = transform(words[i]);
            if(word != null){
                //Token tk = new Token(word, doc);
                tokens.add(word);
            } 
        }         
        return tokens.toArray(new String[0]);  
    }
        
    public String transform(String term){
        term = term.toLowerCase();
        
        //term = term.replaceAll("\\W", "");
        term = pattern.matcher(term).replaceAll("");
        if(term.length()<2){
            return null;
        }
        
        if(useArraysBinarySearch(stopWordsList, term)){
            return null;
        }

        // Stemming
        stemmer.setCurrent(term);
        if (stemmer.stem()){
            return stemmer.getCurrent();
        }
        
        return term;
    }
    
    public ArrayList<String> getTokens() {
        return tokens;
    }
    
    // http://www.programcreek.com/2014/04/check-if-array-contains-a-value-java/
    public static boolean useArraysBinarySearch(String[] arr, String targetValue) {	
	int a =  Arrays.binarySearch(arr, targetValue);
	if(a > 0)
            return true;
	else
            return false;
    }
}
