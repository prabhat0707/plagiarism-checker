package com.plagiarism;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

public class PlagiarismChecker {
    private static List<String> possibleSentences = new ArrayList<String>();
    
    private static void constructPossibleSentences(List<String> words, Map<String, Set<String>> synonyms, int position, String currentWord) {
        if (position >= words.size()) {
            possibleSentences.add(currentWord);
            return;
        }
        
        String word = words.get(position);
        
        for (String synonym : synonyms.get(word)) {
            constructPossibleSentences(words, synonyms, position+1, currentWord + " " + synonym);
        }
    }
    
    private static List<String> splitToSentences(String search) throws Exception {
        List<String> result = new ArrayList<String>();
        
        List<String> words = new ArrayList<String>();
        Map<String, Set<String>> synonyms = new HashMap<String, Set<String>>();
        
        for (String sentence : search.split("\\.")) {
            for (String word : sentence.split(" ")) {
                words.add(word);
                if (synonyms.get(word) == null) {
                    synonyms.put(word, new HashSet<String>());
                    synonyms.get(word).add(word);
                    synonyms.get(word).addAll(SynonymRetriever.getSynonyms(word));
                }
            }            
            constructPossibleSentences(words, synonyms, 0, "");
            result.addAll(possibleSentences);
            
            words.clear();
            possibleSentences.clear();
        }        
        
        return result;
    }
    public static void main(String[] args) throws Exception {
        String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String search = "This is really the sentence that I written on my own in lab. I claim this one is also mine but the next is stolen. " +
        		"Once upon a time in a land far far away called Britain a young and beautiful lady was doing her washing." +
                "And this should appear also mine not plagiased.";
        String charset = "UTF-8";
        
        
        List<String> sentences = splitToSentences(search);
        for (String sentence : sentences) {            
            URL url = new URL(google + URLEncoder.encode("\"" + sentence + "\"", charset));
            Reader reader = new InputStreamReader(url.openStream(), charset);
            
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            String s;
            while ((s = bufferedReader.readLine())!=null)
                System.out.println(s);
            bufferedReader.close();
            SearchResult results = new Gson().fromJson(reader, SearchResult.class);
            
            if (!results.getResponseData().getResults().isEmpty()) {
                System.out.println("The following sentence is plagiarized on web page:");
                System.out.println(sentence);
                System.out.println(results.getResponseData().getResults().get(0).getTitle());
                System.out.println(results.getResponseData().getResults().get(0).getUrl());
            }
        }
    }    
}
