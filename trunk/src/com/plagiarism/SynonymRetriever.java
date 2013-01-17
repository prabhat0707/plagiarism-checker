package com.plagiarism;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SynonymRetriever {
    public static List<String> getSynonyms(String word) throws Exception {
        if (skipWord(word)) return Arrays.asList(word);
        Set<String> result = new HashSet<String>();
        
        URL url = new URL("http://www.dictionaryapi.com/api/v1/references/thesaurus/xml/" + word + "?key=f5cde69e-2ade-4caa-bd80-7b397c93d9af");        
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(url.openStream());              
        
        Node synonyms = document.getElementsByTagName("syn").item(0);
        if (synonyms != null && synonyms.getTextContent() != null) {
            for (String synonym : synonyms.getTextContent().split(",")) {
                result.add(synonym.trim());
            }
        }
        
        List<String> resultList = new ArrayList<String>();
        for (String s : result) resultList.add(s);
            
        return resultList;
    }
    
    
    private static boolean skipWord(String s) {
        return !s.equals("bulky");
    }
}
