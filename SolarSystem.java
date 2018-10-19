/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solarsystementity;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author Sam
 */
public class SolarSystem {
    private ArrayList<SolarSystemEntity> solar;
    private ArrayList<String> names;
    
    public SolarSystem(ArrayList<String> names) throws IOException{
        this.names = names;
        solar = new ArrayList<>();
        for(int i = 0; i< names.size(); i++){
            solar.add(new SolarSystemEntity(names.get(i)));
        }
    }
    public String toString(String desiredPlanet){
        boolean check = false;
        for(int i = 0; i< names.size(); i++){
            if(desiredPlanet.equals(solar.get(i).name)){
                check = true;
                return solar.get(i).toString();
            }
        }
        if(!check)
            return "Planet name was not found";
        return " ";
    }
    
    public static void main(String[] args) throws IOException{
        ArrayList<String> names = new ArrayList<>();
        names.add("Sun");
        names.add("Venus");
        names.add("Earth");
        names.add("Moon");
        SolarSystem m = new SolarSystem(names);
        System.out.println("Enter a planet name");
        Scanner s = new Scanner(System.in);
        System.out.println(m.toString(s.next()));
    }
}
