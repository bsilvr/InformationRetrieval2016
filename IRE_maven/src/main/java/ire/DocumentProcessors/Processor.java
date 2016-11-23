/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.DocumentProcessors;

import ire.CorpusFile;
import ire.DocumentContent;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public interface Processor {
    public void process(CorpusFile file);
    
    public DocumentContent getDocument();
}
