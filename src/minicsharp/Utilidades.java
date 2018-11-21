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
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class Utilidades {

    public static Tabla unaTabla = new Tabla("C:\\Users\\david\\Documents\\miniCsharp\\miniCsharp\\src\\minicsharp\\TablaSimbolos.txt");
    public static int contadorAmbito = 0;
    public static String nombreAmbito = "Global";
    public static String ambitoAnterior = "Global";
    public static Object resultadoActual = null;
    public static Stack<String> pilaAmbito = new Stack();
    public static String Lvalue = "";
    public static String params = "";
    public static LinkedList<Expresion> GlobalExpList = new LinkedList<Expresion>();

    public static void AnadirTabla(Simbolo s) {
        unaTabla.addTabla(s);
    }

    public static void Actualizar(String nombre, String ambito, Object valor, String tipo) {
        Simbolo s = null;
        String log = "";
        boolean cambio = false;
        for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
            if (unaTabla.Tabla_simbolos.get(i).id.equals(nombre) && unaTabla.Tabla_simbolos.get(i).NombreAmbito.equals(ambito)) {
                try {

                    if ((unaTabla.Tabla_simbolos.get(i).tipo.equals(tipo)) || (unaTabla.Tabla_simbolos.get(i).tipo.equals("double") && tipo.equals("int"))) {
                        if(unaTabla.Tabla_simbolos.get(i).desc.equals("Constante")&&!unaTabla.Tabla_simbolos.get(i).valor.equals(""))
                        {
                            System.out.println("NO SE PUEDE ASIGNAR VALOR A UNA CONSTANTE YA ASIGNADA, ID: "+nombre);
                            return;
                        }
                        unaTabla.Tabla_simbolos.get(i).valor = String.valueOf(valor);
                        if (unaTabla.Tabla_simbolos.get(i).tipo.equals("double") && tipo.equals("int")) {
                            unaTabla.Tabla_simbolos.get(i).valor = String.valueOf(valor) + ".0";
                        }
                        s = unaTabla.Tabla_simbolos.get(i);
                        String s1 = String.format("%-32s", s.id).replace(' ', ' ');
                        String s2 = String.format("%-20s", s.NombreAmbito).replace(' ', ' ');
                        String s3 = String.format("%-20s", s.tipo).replace(' ', ' ');
                        String s4 = String.format("%-20s", s.desc).replace(' ', ' ');

                        log = "Cambio:  ID: " + s1 + "\t Ambito: " + s2 + "\t Tipo:" + s3 + "\t Objeto: " + s4 + "\t Valor: " + s.valor + System.lineSeparator();
                        Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND); //exception handling left as an exercise for the reader
                        cambio = true;
                    } else {
                        s = unaTabla.Tabla_simbolos.get(i);
                        log = "ERROR DE ASIGNACION PARA: " + s.id + " SE TRATO DE ASIGNAR: " + valor + System.lineSeparator();
                        Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND);
                        cambio = true;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (cambio == false) {
            for (int j = 0; j < unaTabla.Tabla_simbolos.size(); j++) {
                if (unaTabla.Tabla_simbolos.get(j).id.equals(ambito) && !(unaTabla.Tabla_simbolos.get(j).NombreAmbito.equals(ambito))) {
                    ambito = unaTabla.Tabla_simbolos.get(j).NombreAmbito;
                    Actualizar(nombre, ambito, valor, tipo);
                }
            }
            //System.out.println("ERROR AL ASIGNAR, NO SE ENCONTRO LA VARIABLE: "+nombre);

        }

    }

    public static void Parametros(String nombreFuncion, String ambito, String[] params) {
        Simbolo s = null;
        for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
            if (unaTabla.Tabla_simbolos.get(i).id.equals(nombreFuncion) && unaTabla.Tabla_simbolos.get(i).NombreAmbito.equals(ambito)) {
                unaTabla.Tabla_simbolos.get(i).params = params;
                s = unaTabla.Tabla_simbolos.get(i);
                String s1 = String.format("%-32s", s.id).replace(' ', ' ');
                String s2 = String.format("%-20s", s.NombreAmbito).replace(' ', ' ');
                String s3 = String.format("%-20s", s.tipo).replace(' ', ' ');
                String s4 = String.format("%-20s", s.desc).replace(' ', ' ');
                String log = "";
                String s5 = String.join(",", s.params);
                log = "Params:  ID: " + s1 + "\t Ambito: " + s2 + "\t Tipo:" + s3 + "\t Objeto: " + s4 + "\t Valor: " + s.valor + "\t: PARAMS: " + s5 + System.lineSeparator();
                try {
                    Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND); //exception handling left as an exercise for the reader
                } catch (IOException ex) {
                    Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static Expresion nuevaSuma(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        String aval = String.valueOf(a.value).replace("\"", "");
        String bval = String.valueOf(b.value).replace("\"", "");
        Expresion e = new Expresion();
        try {
            if (a.tipo.equals("int") && b.tipo.equals("int")) {
                e.tipo = "int";
                int val = Integer.valueOf(aval) + Integer.valueOf(bval);
                e.value = String.valueOf(val);
            } else if (a.tipo.equals("double") && b.tipo.equals("double")) {
                e.tipo = "double";
                double val = Double.valueOf(aval) + Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else if ((a.tipo.equals("double") && b.tipo.equals("int")) || (b.tipo.equals("double") && a.tipo.equals("int"))) {
                e.tipo = "double";
                double val = Double.valueOf(aval) + Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else if (a.tipo.equals("string") && b.tipo.equals("string")) {
                e.tipo = "string";
                e.value = "\"" + aval + bval + "\"";
            } else if ((a.tipo.equals("string") && b.tipo.equals("int")) || (a.tipo.equals("string") && b.tipo.equals("double"))) {
                e.tipo = "string";
                e.value = "\"" + aval + bval + "\"";
            } else if (("string".equals(b.tipo) && "int".equals(a.tipo)) || ("string".equals(b.tipo) && "double".equals(a.tipo))) {
                e.tipo = "string";
                e.value = "\"" + aval + bval + "\"";
            } else {
                //suma invalida
            }

        } catch (NumberFormatException v) {
            System.out.println("ERROR EN LA SUMA DE: " + a.value + "Con: " + b.value);
        }

        return e;
    }

    public static Expresion nuevaResta(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        String aval = String.valueOf(a.value).replace("\"", "");
        String bval = String.valueOf(b.value).replace("\"", "");
        Expresion e = new Expresion();
        try {

            if ("int".equals(a.tipo) && "int".equals(b.tipo)) {
                e.tipo = "int";
                int val = Integer.valueOf(aval) - Integer.valueOf(bval);
                e.value = String.valueOf(val);
            } else if ("double".equals(a.tipo) && "double".equals(b.tipo)) {
                e.tipo = "double";
                double val = Double.valueOf(aval) - Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else if (("double".equals(a.tipo) && "int".equals(b.tipo)) || ("double".equals(b.tipo) && "int".equals(a.tipo))) {
                e.tipo = "double";
                double val = Double.valueOf(aval) - Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else {
                //error
            }

        } catch (NumberFormatException v) {
            System.out.println("ERROR EN LA RESTA DE: " + a.value + "Con: " + b.value);
        }

        return e;
    }

    public static Expresion newNegativo(Expresion a) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }

        String aval = String.valueOf(a.value).replace("\"", "");
        Expresion e = new Expresion();
        if (a.tipo.equals("int")) {
            e.tipo = "int";
            int val = -Integer.valueOf(aval);
            e.value = String.valueOf(val);
        } else if (a.tipo.equals("double")) {
            e.tipo = "double";
            double val = -Double.valueOf(aval);
            e.value = String.valueOf(val);
        } else {
            //errror
        }
        return e;
    }

    public static Expresion nuevaMult(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        String aval = String.valueOf(a.value).replace("\"", "");
        String bval = String.valueOf(b.value).replace("\"", "");
        Expresion e = new Expresion();
        try {
            if ("int".equals(a.tipo) && "int".equals(b.tipo)) {
                e.tipo = "int";
                int val = Integer.valueOf(aval) * Integer.valueOf(bval);
                e.value = String.valueOf(val);
            } else if ("double".equals(a.tipo) && "double".equals(b.tipo)) {
                e.tipo = "double";
                double val = Double.valueOf(aval) * Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else if (("double".equals(a.tipo) && "int".equals(b.tipo)) || ("double".equals(b.tipo) && "int".equals(a.tipo))) {
                e.tipo = "double";
                double val = Double.valueOf(aval) * Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else {
                //error
            }

        } catch (NumberFormatException v) {
            System.out.println("ERROR EN LA MULTIPLICACION DE: " + a.value + "Con: " + b.value);
        }
        return e;
    }

    public static Expresion nuevaDiv(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        String aval = String.valueOf(a.value).replace("\"", "");
        String bval = String.valueOf(b.value).replace("\"", "");
        Expresion e = new Expresion();
        try {

            if ("int".equals(a.tipo) && "int".equals(b.tipo)) {
                e.tipo = "int";
                int val = Integer.valueOf(aval) / Integer.valueOf(bval);
                e.value = String.valueOf(val);
            } else if ("double".equals(a.tipo) && "double".equals(b.tipo)) {
                e.tipo = "double";
                double val = Double.valueOf(aval) / Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else if (("double".equals(a.tipo) && "int".equals(b.tipo)) || ("double".equals(b.tipo) && "int".equals(a.tipo))) {
                e.tipo = "double";
                double val = Double.valueOf(aval) / Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else {
                //error
                ;
            }

        } catch (NumberFormatException v) {
            System.out.println("ERROR EN LA DIVISION DE: " + a.value + "Con: " + b.value);
        }
        return e;
    }

    public static Expresion nuevoMod(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        String aval = String.valueOf(a.value).replace("\"", "");
        String bval = String.valueOf(b.value).replace("\"", "");
        Expresion e = new Expresion();
        try {

            if ("int".equals(a.tipo) && "int".equals(b.tipo)) {
                e.tipo = "int";
                int val = Integer.valueOf(aval) % Integer.valueOf(bval);
                e.value = String.valueOf(val);
            } else if ("double".equals(a.tipo) && "double".equals(b.tipo)) {
                e.tipo = "double";
                double val = Double.valueOf(aval) % Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else if (("double".equals(a.tipo) && "int".equals(b.tipo)) || ("double".equals(b.tipo) && "int".equals(a.tipo))) {
                e.tipo = "double";
                double val = Double.valueOf(aval) % Double.valueOf(bval);
                e.value = String.valueOf(val);
            } else {
                //error
                ;
            }

        } catch (NumberFormatException v) {
            System.out.println("ERROR EN EL MOD DE: " + a.value + "Con: " + b.value);
        }
        return e;
    }

    public static Expresion nuevaEqual(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        Expresion e = new Expresion();
        if ((a.tipo.equals(b.tipo))) {
            e.tipo = "bool";
            if (a.value.equals(b.value)) {

                e.value = "true";
            } else {
                e.value = "false";
            }

        } else {
            //error, no se pueden comparar de tipos diferentes

        }
        return e;

    }

    public static Expresion nuevaNotEqual(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        Expresion e = new Expresion();
        if ((a.tipo.equals(b.tipo))) {
            e.tipo = "bool";
            if (!a.value.equals(b.value)) {

                e.value = "true";
            } else {
                e.value = "false";
            }

        } else {
            //error, no se pueden comparar de tipos diferentes
        }
        return e;

    }

    public static Expresion nuevaAnd(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        Expresion e = new Expresion();
        if ((a.tipo.equals(b.tipo)) && a.tipo.equals("bool")) {
            e.tipo = "bool";
            if (a.value.equals(b.value) && a.value.equals(true)) {

                e.value = true;
            } else {
                e.value = false;
            }

        } else {
            //error, no se pueden comparar de tipos diferentes
        }
        return e;
    }

    public static Expresion nuevaOr(Expresion a, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        Expresion e = new Expresion();
        if ((a.tipo.equals(b.tipo)) && a.tipo.equals("bool")) {
            e.tipo = "bool";
            if (a.value.equals(true) || b.value.equals(true)) {

                e.value = true;
            } else {
                e.value = false;
            }

        } else {
            //error, no se pueden comparar de tipos diferentes
        }
        return e;
    }

    public static Expresion nuevaComp(Expresion a, String cmp, Expresion b) {
        if (a.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
            a.tipo = s.tipo;
            a.value = s.valor;
        }
        if (b.tipo.equals("ID")) {
            Simbolo s = Utilidades.traerVariable(String.valueOf(b.value), Utilidades.nombreAmbito);
            b.tipo = s.tipo;
            b.value = s.valor;
        }
        Expresion e = new Expresion();
        if (a.tipo.equals(b.tipo)) {
            if (a.tipo.equals("int")) {
                e.tipo = "bool";
                int aval = Integer.valueOf(a.value.toString());
                int bval = Integer.valueOf(b.value.toString());
                if (cmp.equals(">")) {
                    e.value = aval > bval;
                } else if (cmp.equals("<")) {
                    e.value = aval < bval;
                } else if (cmp.equals(">=")) {
                    e.value = aval >= bval;
                } else if (cmp.equals("<=")) {
                    e.value = aval < bval;
                }
            } else if (a.tipo.equals("double")) {
                e.tipo = "bool";
                double aval = Double.valueOf(a.value.toString());
                double bval = Double.valueOf(b.value.toString());
                if (cmp.equals(">")) {
                    e.value = aval > bval;
                } else if (cmp.equals("<")) {
                    e.value = aval < bval;
                } else if (cmp.equals(">=")) {
                    e.value = aval >= bval;
                } else if (cmp.equals("<=")) {
                    e.value = aval < bval;
                }
            }
        } else {
            //error
        }
        return e;
    }

    public static Expresion nuevaNot(Expresion a) {
        try {
            if (a.tipo.equals("ID")) {
                Simbolo s = Utilidades.traerVariable(String.valueOf(a.value), Utilidades.nombreAmbito);
                a.tipo = s.tipo;
                a.value = s.valor;
            }

            boolean b = true;
            Expresion e = new Expresion();
            if (a.tipo.equals("bool")) {
                e.tipo = "bool";
                e.value = !a.value.equals(true);
            } else {
                //error
            }
            return e;
        } catch (NullPointerException f) {
            System.out.println("ERROR EN EL NOT DE: " + a.value + "Con: ");
        }
        return null;

    }

    public static Simbolo traerVariable(String id, String ambitoActual) {
        for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
            if (unaTabla.Tabla_simbolos.get(i).id.equals(id) && unaTabla.Tabla_simbolos.get(i).NombreAmbito.equals(ambitoActual)) {
                if (unaTabla.Tabla_simbolos.get(i).tipo.equals("int") && unaTabla.Tabla_simbolos.get(i).valor.equals("")) {
                    unaTabla.Tabla_simbolos.get(i).valor = "0";
                }
                if (unaTabla.Tabla_simbolos.get(i).tipo.equals("double") && unaTabla.Tabla_simbolos.get(i).valor.equals("")) {
                    unaTabla.Tabla_simbolos.get(i).valor = "0.0";
                }
                return unaTabla.Tabla_simbolos.get(i);
            }
        }
        //si llega aca, entonces no encontro nada
        if (ambitoActual.equals("Global")) {
            for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
                if (unaTabla.Tabla_simbolos.get(i).id.equals(ambitoActual)) {
                    ambitoActual = unaTabla.Tabla_simbolos.get(i).id;
                    return traerVariable(id, ambitoActual);
                }
            }
            System.out.println("No se encontro el simbolo: " + id);
            return null;
        } else {
            for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
                if (unaTabla.Tabla_simbolos.get(i).id.equals(ambitoActual) && !(unaTabla.Tabla_simbolos.get(i).NombreAmbito.equals(ambitoActual))) {
                    ambitoActual = unaTabla.Tabla_simbolos.get(i).NombreAmbito;
                    return traerVariable(id, ambitoActual);
                }
            }
            System.out.println("No se encontro el simbolo: " + id + " en el ambito: " + ambitoActual);
            return null;//si llega aca, signigica que no encontro el simbolo

        }
        //no es global, por lo que aun esta insertado en algun lado, buscar el ambito en el que esta insertado

    }

    public static void EliminarVariable(String unAmbito) {

        LinkedList<Simbolo> aux = new LinkedList();

        for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
            if (unaTabla.Tabla_simbolos.get(i).NombreAmbito.equals(unAmbito)) {
                aux.add(unaTabla.Tabla_simbolos.get(i));
            }
        }
        for (int i = 0; i < aux.size(); i++) {
            String log = "";
            Simbolo s = null;
            unaTabla.Tabla_simbolos.remove(aux.get(i));
            s = aux.get(i);
            String s1 = String.format("%-32s", s.id).replace(' ', ' ');
            String s2 = String.format("%-20s", s.NombreAmbito).replace(' ', ' ');
            String s3 = String.format("%-20s", s.tipo).replace(' ', ' ');
            String s4 = String.format("%-20s", s.desc).replace(' ', ' ');
            log = "ELIMINO:  ID: " + s1 + "\t Ambito: " + s2 + "\t Tipo:" + s3 + "\t Objeto: " + s4 + "\t Valor: " + s.valor + System.lineSeparator();
            try {
                Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND); //exception handling left as an exercise for the reader
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static boolean yaExiste(String id, String ambitoInicio) {
        for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
            if (unaTabla.Tabla_simbolos.get(i).id.equals(id) && nombreAmbito.equals(ambitoInicio)) {
                return true;
            }
        }
        //no esta en el ambito actual, buscar en otro ambito
        if (ambitoInicio.equals("Global")) {
            return false;//llego hasta global y no encontro la el identificador
        } else {
            //aun no ha llegado a global, por lo que se puede ascender en la pila de ambitos
            for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
                if (unaTabla.Tabla_simbolos.get(i).id.equals(ambitoInicio)) {
                    ambitoInicio = unaTabla.Tabla_simbolos.get(i).NombreAmbito;
                    return yaExiste(id, ambitoInicio);
                }
            }
            System.out.print("El ambito no existe");
            return false;//;a variable no existe
        }

    }

    public static boolean buscarIdentificador(String id, String ambitoInicio) throws IOException {
        for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
            if (unaTabla.Tabla_simbolos.get(i).id.equals(id) && nombreAmbito.equals(ambitoInicio)) {
                return true;//lo encontro, por lo que deberia de existir
            }
        }
        //no esta en el ambito actual, buscar en otro ambito
        if (ambitoInicio.equals("Global")) {
            //no existe, deberia de reportar un error
            String log = "EL IDENTIFICADOR: " + id + " NO HA SIDO DECLARADO EN EL AMBITO ACTUAL NI EN UN AMBITO SUPERIOR" + System.lineSeparator();
            Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND);
            return false;//llego hasta global y no encontro la el identificador
        } else {
            //aun no ha llegado a global, por lo que se puede ascender en la pila de ambitos
            for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
                if (unaTabla.Tabla_simbolos.get(i).id.equals(ambitoInicio)) {
                    ambitoInicio = unaTabla.Tabla_simbolos.get(i).NombreAmbito;
                    return yaExiste(id, ambitoInicio);
                }
            }
            System.out.print("EL AMBITO ES INCORRECTO");
            return false;//;a variable no existe
        }
    }

    public static Simbolo TraerSimbolo(String id, String ambitoInicio) {
        for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
            if (unaTabla.Tabla_simbolos.get(i).id.equals(id) && nombreAmbito.equals(ambitoInicio)) {
                return unaTabla.Tabla_simbolos.get(i);//lo encontro, por lo que deberia de existir
            }
        }
        //no esta en el ambito actual, buscar en otro ambito
        if (ambitoInicio.equals("Global")) {
            //no existe, deberia de reportar un error
            String log = "EL IDENTIFICADOR: " + id + " NO HA SIDO DECLARADO EN EL AMBITO ACTUAL NI EN UN AMBITO SUPERIOR" + System.lineSeparator();
            try {
                Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;//llego hasta global y no encontro la el identificador
        } else {
            //aun no ha llegado a global, por lo que se puede ascender en la pila de ambitos
            for (int i = 0; i < unaTabla.Tabla_simbolos.size(); i++) {
                if (unaTabla.Tabla_simbolos.get(i).id.equals(ambitoInicio)) {
                    ambitoInicio = unaTabla.Tabla_simbolos.get(i).NombreAmbito;
                    return TraerSimbolo(id, ambitoInicio);
                }
            }
            System.out.print("EL AMBITO ES INCORRECTO");
            return null;//;a variable no existe
        }
    }

    public static void VerificarParametros(String id, String unAmbito) {
        //Primero identificar las variables
        //Se supone que la lista de global de expresiones viene
        //verificar si el simbolo existe
        Simbolo s = TraerSimbolo(id, unAmbito);
        if (s != null) {
            for (int i = 0; i < s.params.length; i++) {
                System.out.println(s.params[i]);
            }
            System.out.println("Parametros");
            for (int i = 0; i < GlobalExpList.size(); i++) {
                System.out.println(GlobalExpList.get(i).tipo + ":" + GlobalExpList.get(i).value);
            }
            //verificar el tipo de parametro en orden
            if (s.params.length != GlobalExpList.size()) {
                String log = "EL PROCEDIMIENTO/FUNCTION: " + id + " NO RECIBE ESA CANTIDAD DE PARAMETROS" + System.lineSeparator();
                try {
                    Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND);
                } catch (IOException ex) {
                    Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                for (int i = 0; i < GlobalExpList.size(); i++) {
                    String aux = GlobalExpList.get(i).tipo;
                    if (aux.equals("ID")) {
                        //ir a traer el tipo de la variable
                        Simbolo s1 = TraerSimbolo(String.valueOf(GlobalExpList.get(i).value), unAmbito);
                        if (s1 != null) {
                            aux = s1.tipo;
                        }
                    }
                    //Posicion 0 es el tipo
                    if (aux != "ID") {

                        String Tipo = s.params[i].split(":")[0];
                        if (!aux.equals(Tipo)) {
                            String log = "EL PROCEDIMIENTO/FUNCTION: " + id + " NO RECIBE PARAMETROS TIPO: " + aux + System.lineSeparator();
                            try {
                                Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND);
                            } catch (IOException ex) {
                                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }

                }
            }

        } else {
            String log = "EL IDENTIFICADOR: " + id + " NO HA SIDO DECLARADO EN EL AMBITO ACTUAL NI EN UN AMBITO SUPERIOR" + System.lineSeparator();
            try {
                Files.write(Paths.get(unaTabla.FilePath), log.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Expresion SimboloAExpresion(Simbolo s) {
        Expresion e = new Expresion();
        e.tipo = s.tipo;
        e.value = s.valor;
        return e;

    }

    public static void verReturn(Expresion e) {

        if (e != null) {
            // ir a buscar el simbolo con en la tabla de simbolos cuyo id es el mismo que su ambito
            if (e.tipo != "ID") {
                Actualizar(nombreAmbito, nombreAmbito, e.value, e.tipo);
            } 
            else {
                Simbolo s = Utilidades.traerVariable(String.valueOf(e.value), nombreAmbito);
                if (s == null) {
                    return;
                } else {
                    e.tipo = s.tipo;
                    e.value = s.valor;
                }
                Actualizar(nombreAmbito, nombreAmbito, e.value, e.tipo);
            }
            System.out.println("La función: " + nombreAmbito + " Regresa: " + e.tipo + ":" + e.value);
        } else {
            System.out.println("El objeto no regresa nada");

        }

    }

}
