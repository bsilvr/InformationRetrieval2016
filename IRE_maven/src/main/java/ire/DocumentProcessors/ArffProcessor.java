/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.DocumentProcessors;
/**
 *
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class ArffProcessor {
    
   
    /*public static String process(Document doc){
        
        File cf = new File(doc.getFilePath());
        
        int nLine= doc.getDocStartLine();
        int idx = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(cf))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                idx++;
                if(idx == nLine){
                    String[] parts = StringUtils.split(line, "\"");
                    parts[1] = parts[1].replaceAll("<.e>", "");
                    return parts[1];
                }
                
            }
            // line is not visible here.
        } catch (IOException ex) {
            Logger.getLogger(ArffProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static ArrayList<Document> identify(CorpusFile file){
        ArrayList<Document> documents = new ArrayList<>();
        File cf = new File(file.getPath());
        
        boolean dataFound = false;
        
        try(BufferedReader br = new BufferedReader(new FileReader(cf))) {
            int nLine = 0;
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                nLine++;
                if(!dataFound){
                    if(line.equals("@DATA")){
                        dataFound = true;
                    }
                    continue;
                }
                String[] parts = line.split("\"");
                Document doc = new Document(file.getPath(), nLine, Integer.parseInt(parts[0].substring(0, parts[0].length()-1)));
                documents.add(doc);
            }
            // line is not visible here.
        } catch (IOException ex) {
            Logger.getLogger(ArffProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }*/
}
