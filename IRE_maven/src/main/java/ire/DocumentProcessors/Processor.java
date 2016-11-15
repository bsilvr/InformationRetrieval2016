/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.DocumentProcessors;

import ire.CorpusFile;
import ire.Document;
import ire.DocumentContent;
import java.util.ArrayList;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public interface Processor {
    public ArrayList<Document> process(CorpusFile file);
    
    public DocumentContent getDocument();
}
