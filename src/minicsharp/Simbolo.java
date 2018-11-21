/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minicsharp;

/**
 *
 * @author david
 */
public class Simbolo {
    
    public String id="";
    public String NombreAmbito="";
    public String tipo="";
    public String desc="";
    public String params[]=new String[]{};
    public String valor="";
    
    
    
    public Simbolo(){
        
    }
    public Simbolo (String unId,String nombreAmbito,String unTipo,String unvalor){
        this.id=unId;
        this.NombreAmbito=nombreAmbito;
        this.tipo=unTipo;
        this.valor=unvalor;
    }
    
     public Simbolo (String unId,String nobreAmbito,String unTipo,String params[],String unvalor){
        this.id=unId;
        this.NombreAmbito=nobreAmbito;
        this.tipo=unTipo;
        this.params=params;
        this.valor=unvalor;
    }
     
      public Simbolo (String unId,String nombreAmbito,String unTipo){
        this.id=unId;
        this.NombreAmbito=nombreAmbito;
        this.tipo=unTipo;
        
    }
    
}
