/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.ArffProcessor;
import java.util.ArrayList;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentProcessor {
    
    private ArrayList<Document> documents;
    
    public DocumentProcessor(){
        documents = new ArrayList<>();
        // criar pasta para guardar os documentos em formato unico
    }
    
    public void processDocument(CorpusFile cfile){
        // Ver a extensao e enviar o path para a função adequada.
        if(cfile.getExtension().equals("arff")){
            documents.addAll(ArffProcessor.process(cfile));
        }
    }
    
/*    public void processArff(String path){
        // Cada documentos desse ficheiro e guardar na pasta temporaria.
        // Adiciona documento a arrayList
        // Processa documento retirando tags, etc...
    }
*/    
    public void writeFile(String document){
        //Escreve para o ficheiro
    }
    
}
