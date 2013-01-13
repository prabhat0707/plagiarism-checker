package com.plagiarism;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SynonymRetriever {
    public static List<String> getSynonyms(String word) throws Exception {
        Set<String> result = new HashSet<String>();
        
        URL url = new URL("http://www.dictionaryapi.com/api/v1/references/collegiate/xml/" + word + "?key=9f63884f-3614-43a8-abf8-b9f8e42aa807");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(url.openStream());
        
        NodeList synonyms = document.getElementsByTagName("sc");
        for (int i = 0; i < synonyms.getLength(); i++) {
            result.add(synonyms.item(i).getTextContent());
        }
        
        List<String> resultList = new ArrayList<String>();
        for (String s : result) resultList.add(s);
            
        return resultList;
    }
}
