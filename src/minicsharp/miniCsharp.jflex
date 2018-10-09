//packages
package minicsharp;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java_cup.runtime.Symbol;
%%

%{
//Developer´s extra code declaration
private Symbol symbol(int type){
	return new Symbol(type,yyline,yycolumn,yytext());
}

private Symbol symbol(int type,Object value){
	return new Symbol(type,yyline,yycolumn,value	);
}
private static int errorCounter=0;
public   boolean commentError=false;
class token{
	public String content="";
	public int line=0;
	public int column=0;
	public token(String aContent,int aLine,int aColumn){
			content=aContent;
			line=aLine;
			column=aColumn;
	}
}
class Yytoken{
		public Yytoken(){

		}
	}
public static ArrayList<String>TokenList=new ArrayList<String>();
private static String  addsTabs(int tokenLength){
	String tabs="";
	for(int i=32-tokenLength;i>0;i--){
		tabs+=" ";
	}
	return tabs;

}
public static String CreateTokenLog(Boolean isError,String token, int lineNumber,int columnNumber,String description){
	String newToken=token+addsTabs(token.length())+"\t\tLine Number: "+(lineNumber+1)+"\t"+"Column Number: "+(columnNumber)+" To: "+getLastColumn(columnNumber,token.length())+"\tToken Type: "+description+" Token: "+token+"\n";

	String ErrorToken=token+addsTabs(token.length())+"\t\tError at line: "+(lineNumber+1)+"\tDescription: "+description+"\n";

	return isError?ErrorToken:newToken;
}
public static int getLastColumn(int firstColumns,int tokenLength){
	return firstColumns+tokenLength;
}
public static void CreateOutputFile(String outputPath) throws IOException{
	FileWriter writer = null; 
      try {
          writer = new FileWriter(outputPath);
      } catch (IOException ex) {
          System.out.println("Something wrong happened with the output file...");
      }
for(String str: TokenList) {
  writer.write(str);
}
writer.close();
System.out.println("Done!");
if(errorCounter==0){
	System.out.println("The file contains no errors, This file is a cs file");
}
else{
	System.out.println("The file contains "+errorCounter+" errors, This is not a cs file");
}
}


%}
//Setting the .jflex file
%public
%class Analyzer
%column
%line
%unicode
%cup
//RE declaration area
ARITMETIC="+"|"-"|"*"|"/"|"%"
LOGIC="=="|"<"|">"|">="|"<="|"&&"|"||"|"!="
EQUAL="="|"+="|"-="|"*="|"/="
ID =  [a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*
BOOLEAN=true|false
INTEGER=((0|[0-9][0-9]*)|(0((x|X)[0-9a-fA-F]+)|[0-7]))
DOUBLE=([0-9]+\.[0-9]*({INTEGER}*[eE]?[-+]?{INTEGER}*[\.]?[0-9]*)?)
STRING=(\"([^\"\n]|\\.)*\")
MULTILINECOMMENTERROR="/*"("*"[^/]|[^*/]|[^*]"/")*
MULTILINECOMMENT="\/\*"~"\*\/"
NORMALCOMMENT="\/\/"~"\n"
WHITESPACES=[\r|\t|\f|\s|\g]
NEWLINE=\n

%%
<YYINITIAL> {

    //RE behaviour code

{EQUAL}					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"="));
							return new Symbol(sym.equal,yycolumn,yyline,yytext());
						}

{ARITMETIC}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,yytext()));
							return new Symbol(sym.arit,yycolumn,yyline,yytext());
					    }
				
{LOGIC}					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,yytext()));
							return new Symbol(sym.logic,yycolumn,yyline,yytext());
					    }
"!"						{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"!"));
							return new Symbol(sym.exclamation,yycolumn,yyline,yytext());
						}				

"implements"		{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"implements"));
						return new Symbol(sym.implements_t,yycolumn,yyline,yytext());
					}

"extends"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"extends"));
						return new Symbol(sym.extends_t,yycolumn,yyline,yytext());
					}

"void"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"void"));
						return new Symbol(sym.VOID,yycolumn,yyline,yytext());
					}

"int"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"int"));
						return new Symbol(sym.INT,yycolumn,yyline,yytext());
					}
"double"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"double"));
						return new Symbol(sym.DOUBLE,yycolumn,yyline,yytext());
					}
"bool"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"bool"));
						return new Symbol(sym.BOOL,yycolumn,yyline,yytext());
					}
"string"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"string"));
						return new Symbol(sym.STRING,yycolumn,yyline,yytext());
					}
"class"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"class"));
						return new Symbol(sym.CLASS,yycolumn,yyline,yytext());
					}
"interface"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"interface"));
						return new Symbol(sym.interface_t,yycolumn,yyline,yytext());
					}
"null"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"null"));
						return new Symbol(sym.NULL,yycolumn,yyline,yytext());
					}
"this"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"this"));
						return new Symbol(sym.THIS,yycolumn,yyline,yytext());
					}
"while"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"while"));
						return new Symbol(sym.WHILE,yycolumn,yyline,yytext());
					}
"if"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"if"));
						return new Symbol(sym.IF,yycolumn,yyline,yytext());
					}
"else"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"else"));
						return new Symbol(sym.else_t,yycolumn,yyline,yytext());
					}
"return"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"return"));
						return new Symbol(sym.RETURN,yycolumn,yyline,yytext());
					}
"break"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"break"));
						return new Symbol(sym.BREAK,yycolumn,yyline,yytext());
					}
"New"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"New"));
						return new Symbol(sym.New,yycolumn,yyline,yytext());
					}
"NewArray"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"NewArray"));
						return new Symbol(sym.NewArray,yycolumn,yyline,yytext());
					}
"Print"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"Print"));
						return new Symbol(sym.Print,yycolumn,yyline,yytext());
					}
"ReadInteger"		{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"ReadInteger"));
						return new Symbol(sym.ReadInteger,yycolumn,yyline,yytext());
					}
"ReadLine"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"ReadLine"));
						return new Symbol(sym.ReadLine,yycolumn,yyline,yytext());
					}
"Malloc"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"Malloc"));
						return new Symbol(sym.Malloc,yycolumn,yyline,yytext());
					}




";"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,";"));
						return new Symbol(sym.semicolon,yycolumn,yyline,yytext());
					}
"."					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"."));
						return new Symbol(sym.dot,yycolumn,yyline,yytext());
					}
"("					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"("));
						return new Symbol(sym.leftparen,yycolumn,yyline,yytext());
					}
")"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,")"));
						return new Symbol(sym.rightparen,yycolumn,yyline,yytext());
					}
"["					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"["));
						return new Symbol(sym.leftsquarebrace,yycolumn,yyline,yytext());
					}
"]"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"]"));
						return new Symbol(sym.rightsquarebrace,yycolumn,yyline,yytext());
					}
"{"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"{"));
						return new Symbol(sym.leftbrace,yycolumn,yyline,yytext());
					}
"}"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"}"));
						return new Symbol(sym.rightbrace,yycolumn,yyline,yytext());
					}
","					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,","));
						return new Symbol(sym.comma,yycolumn,yyline,yytext());
					}



{INTEGER}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"INTEGER"));
							return new Symbol(sym.INTCONSTANT,yycolumn,yyline,yytext());
						}
{DOUBLE}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"DOUBLE"));
							return new Symbol(sym.DOUBLECONSTANT,yycolumn,yyline,yytext());
						}



{BOOLEAN}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"BOOLEAN"));
							return new Symbol(sym.BOOLEANCONSTANT,yycolumn,yyline,yytext());
						}
{ID}       				 {if(yytext().length()>31){
			
							TokenList.add(CreateTokenLog(true,yytext().substring(0,30),yyline,yycolumn,"IDENTIFIER_TO_LONG,_MAX_SIZE_31_CHARACTERS"));
							errorCounter+=1;
						}
						else{
							TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"IDENTIFIER"));
							return new Symbol(sym.IDENT,yycolumn,yyline,yytext());
						}
						}		



{STRING}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"STRING"));
							return new Symbol(sym.STRINGCONSTANT,yycolumn,yyline,yytext());
						}
							
							
{MULTILINECOMMENT}		{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"MULTILINE_COMMENT"));*/}
{MULTILINECOMMENTERROR} { TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"MULTILINE_COMMENT_ERROR_MISSING *\\"));errorCounter+=1;}
{NORMALCOMMENT}			{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"NORMAL_COMMENT"));*/}

{NEWLINE}				{/*Do nothing...*/}
{WHITESPACES}			{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"WHITE_SPACE"));*/}



    //Error code management
.           {TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"UNRECOGNIZED CHARACTER")); errorCounter+=1;
				System.out.println("Caracter no reconocido en la linea: "+yyline+" columna: "+yycolumn+" caracter: "+yytext());}
}
