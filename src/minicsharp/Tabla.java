/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minicsharp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 *
 * @author david
 */
public class Tabla {
    
    public LinkedList<Simbolo> Tabla_simbolos;
    public String FilePath="";//dirección del archivo de la tabla de simbolos
    
    public Tabla (String path){
        this.Tabla_simbolos=new LinkedList();
        this.FilePath=path;
    }
    
    public void addTabla(Simbolo s){
        if(Utilidades.yaExiste(s.id, Utilidades.nombreAmbito))
        {
            System.out.println("ERROR: LA VARIABLE "+s.id+" YA EXISTE");
            return;
        }
        String log="";
        String s1=String.format("%-32s", s.id).replace(' ', ' ');
        String s2=String.format("%-20s", s.NombreAmbito).replace(' ', ' ');
        String s3=String.format("%-20s", s.tipo).replace(' ', ' ');
        String s4=String.format("%-20s", s.desc).replace(' ', ' ');
        
        log="Añadido: ID: "+s1+"\t Ambito: "+s2+"\t Tipo:"+s3+"\t Objeto: "+s4+"\t Valor: "+s.valor+System.lineSeparator();
        this.Tabla_simbolos.add(s);//anado un simbolo a la tabla
        try {
        Files.write(Paths.get(FilePath), log.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
        //exception handling left as an exercise for the reader
            System.out.println("Error ocurred while traying to write int te log");
        }      
    }
    
    
    
}
