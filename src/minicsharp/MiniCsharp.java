/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minicsharp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author david
 */
public class MiniCsharp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        System.out.println("Set the path for the C# code text file");
        //Scanner scanner=new Scanner(System.in);
        String input="C:\\Users\\david\\Desktop\\test.in";
        AnalizerInit(input);
        
    }
    private static void AnalizerInit(String csharpCodePath) throws IOException{
        
        Analyzer chsarpAnalizer=new Analyzer(new BufferedReader(new FileReader(csharpCodePath))); 
        while (chsarpAnalizer.yylex()!=null){
            //this here do stuff
            
        }
        Analyzer.CreateOutputFile("C:\\Users\\david\\Desktop\\test.out");
        
    }
    
}
