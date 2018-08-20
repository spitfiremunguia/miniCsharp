/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minicsharp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

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
        AnalizarInit(args[1]);
    }
    private static void AnalizarInit(String csharpCodePath) throws IOException{
        Analyzer chsarpAnalizer=new Analyzer(new BufferedReader(new StringReader(csharpCodePath)));
        while (chsarpAnalizer.yylex()!=null){
            //this here do stuff
            
        }
    }
    
}
