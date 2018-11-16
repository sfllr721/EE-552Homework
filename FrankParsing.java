/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frankparsing;

/**
 *
 * @author Sam Fuller
 */
import java.io.*;
import java.util.*;
/**
 *
 * @author Sam Fuller
 */
public class FrankParsing {
    final private HashMap<String, Integer> counter = new HashMap<>();
    final private String pattern;
    private String pattern2;
    public FrankParsing(String fileName) throws FileNotFoundException, IOException{
      BufferedReader br = new BufferedReader(new FileReader("shortwords.txt"));
      String line;
      pattern = "[﻿.,;:!?_'-`’‘”“\"—\\)\\(]";
      pattern2 = "[\\s]";
      while((line = br.readLine())!= null){
        pattern2 = pattern2 + "|"+ "(" + "\\b"+line+"\\b" +")";
      }
       System.out.println(pattern2);
       String bookLine;
       BufferedReader book = new BufferedReader(new FileReader(fileName));
       while((bookLine = book.readLine()) != null){
            String check2 = bookLine.replaceAll(pattern, "");
            String[] result = check2.split(pattern2);
            if(result.length > 0){
                if(result.length == 1)
                    assign(result[0]);
                if(result.length > 1){
                     if(result[1].length() == 0 && result[0].length() != 0){
                          assign(result[0]);
                      }
                      if(result.length >= 2){

                          for(int i = 1; i <= result.length - 2; i++){
                              if((result[i-1].length() == 0 || result[i+1].length() == 0) && result[i].length() != 0){
                                 assign(result[i]);   
                              }
                          } 
                          if(result[result.length - 2].length() == 0 && result[result.length-1].length() != 0){
                              assign(result[result.length-1]);

                          } 
                      } 
                 }
            }
             String[] assurance = bookLine.split("[\\s]");
             if(assurance.length > 1){
                if(assurance[assurance.length-1].matches(pattern2))
                    assign(result[result.length-1]);
             }
      
    }
       Iterator it = counter.entrySet().iterator();
             while (it.hasNext()){
                 Map.Entry pair = (Map.Entry)it.next();
                 System.out.println(pair.getKey() + " = " + pair.getValue());
             }
    }
    
    private void assign (String word){
        boolean exists = false;
        Iterator it = counter.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            if(!pair.getKey().equals(word)){}  
            else 
                exists = true; 
        }
        if(exists){
            counter.put(word, counter.get(word)+1);
        }
        if(!exists){
            counter.put(word, 1);
        }
    }
 
    public static void main(String[] args) throws IOException {
        
        FrankParsing f = new FrankParsing("Frankenstein.txt");
        
    }
}

