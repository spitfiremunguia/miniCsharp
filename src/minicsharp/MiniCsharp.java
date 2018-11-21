/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minicsharp;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;



/**
 *
 * @author david
 */
public class MiniCsharp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        // TODO code application logic here
        Scanner scanner=new Scanner(System.in);
        
        /*int flag=scanner.nextInt();*/
        /*scanner=new Scanner(System.in);*/
        System.out.println("Set the path for the C# code text file");
        String input=scanner.nextLine();
        if(input.contains(".frag")){
            AnalizerInit(input);
        }
        else{
            System.out.println("Unexpected file extension. The extension should be .frag");
        }
          
        
    }
    
    private static void AnalizerInit(String csharpCodePath) throws IOException, Exception{
        try{
            Analyzer csharpAnalyzer=new Analyzer(new BufferedReader(new FileReader(csharpCodePath)));
            parser p=new parser(csharpAnalyzer);
            
            p.parse();
            LinkedList<Simbolo>l=Utilidades.unaTabla.Tabla_simbolos;
            

            File f=new File(csharpCodePath);
            Analyzer.CreateOutputFile(f.getParent()+"\\"+f.getName().substring(0,f.getName().indexOf("."))+".out");
            }
            catch (IOException e){
            System.out.println("Something wrong happen with the file...");
            }
        
    }
    
}
