
package solarsystementity;

import java.io.*;
import java.util.*;

/**
 *
 * @author Sam
 */
public class SolarSystemEntity {
    public String name; 
    private String orbit;
    private double mass, diameter, peri, aphe, meanDistance;
    private boolean done = false; 
    public SolarSystemEntity (String name) throws FileNotFoundException, IOException{
         this.name = name;
         Scanner s = new Scanner((new BufferedReader(new FileReader("solarsystem.dat"))));
         String next;
         while(s.hasNext()&& !done){
             next = s.next();
             if(next.equals(name)){
                orbit = s.next();
                mass = Double.parseDouble(s.next());
                diameter = Double.parseDouble(s.next());
                peri = Double.parseDouble(s.next());
                aphe = Double.parseDouble(s.next());
                done = true;
             }     
         }
         meanDistance = (peri + aphe)/2;
         
    }
    
    public String toString(){
        return "Name: " + name + "\n" + "Orbits: " + orbit + "\n" +"Mass: " + mass + "\n" +"Diameter: " + diameter + "\n" +"Mean Distance: " + meanDistance;
    }
    
  
    public static void main(String[] args) throws FileNotFoundException, IOException {
        SolarSystemEntity m = new SolarSystemEntity("Mars");
        System.out.println(m.toString());
        String h = m.name;
    }
    
}
