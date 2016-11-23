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
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Tokenizer {
    private final Pattern pattern = Pattern.compile("\\W");
    private final Pattern space_char = Pattern.compile("\\s+|[-()\\./_]");
    private boolean stemming;
    private boolean removeStopWords;
    private boolean debug;
    private final ArrayList<String> tokens;
    String [] stopWordsList;
    englishStemmer stemmer;
    
    public Tokenizer(String [] stopWordsList, boolean stemming, boolean removeStopWords, boolean debug){
        this.stopWordsList = stopWordsList;
        this.stemmer = new englishStemmer();
        this.tokens = new ArrayList<>();
        this.debug = debug;
        this.stemming = stemming;
        this.removeStopWords = removeStopWords;
    }
    public Tokenizer(String [] stopWordsList, boolean stemming, boolean removeStopWords){
        this.stopWordsList = stopWordsList;
        this.stemmer = new englishStemmer();
        this.tokens = new ArrayList<>();
        this.debug = false;
        this.stemming = stemming;
        this.removeStopWords = removeStopWords;
    }
    public Tokenizer(String [] stopWordsList, boolean debug){
        this.stopWordsList = stopWordsList;
        this.stemmer = new englishStemmer();
        this.tokens = new ArrayList<>();
        this.debug = debug;
        this.stemming = true;
        this.removeStopWords = true;
    }
    
    public String[] tokenize(String content){
        
        String [] words = space_char.split(content);
        String word;
        for(int i = 0; i < words.length; i++){
            word = transform(words[i]);
            if(word != null){
                tokens.add(word);
            } 
        }         
        return tokens.toArray(new String[0]);  
    }
        
    public String transform(String term){
        term = term.toLowerCase();
        
        term = pattern.matcher(term).replaceAll("");
        if(term.length()<2 || term.length() > 18){
            return null;
        }
        
        if(removeStopWords){
            if(useArraysBinarySearch(stopWordsList, term)){
                return null;
            }
        }
        
        if(stemming){
            stemmer.setCurrent(term);
            if (stemmer.stem()){
                return stemmer.getCurrent();
            }
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
