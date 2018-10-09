//packages
package minicsharp;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java_cup.runtime.Symbol;
%%

%{
//Developer´s extra code declaration
public static sym asym=new sym();
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
%type Token
%char
%column
%full
%type java_cup.runtime.Symbol
%function next_token
%implements java_cup.runtime.Scanner
%line
%cup
%unicode
//RE declaration area
ARITMETIC=[\+|-|\*|\/|%]
LOGIC=[==|<|>|>=|<=|&&|\|\||(!=)]
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
"!"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"!"));
						return new Symbol(sym.exclamation,new token(yytext(),yyline+1,yycolumn+1));
					}
"="					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"="));
						return new Symbol(sym.equal,new token(yytext(),yyline+1,yycolumn+1));
					}
{LOGIC}					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,yytext()));
						return new Symbol(sym.logic,new token(yytext(),yyline+1,yycolumn+1));
					   }

"implements"		{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"implements"));
						return new Symbol(sym.implements_t,new token(yytext(),yyline+1,yycolumn+1));
					}
"extends"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"extends"));
						return new Symbol(sym.extends_t,new token(yytext(),yyline+1,yycolumn+1));
					}
"void"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"void"));
						return new Symbol(sym.VOID,new token(yytext(),yyline+1,yycolumn+1));
					}
"int"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"int"));
						return new Symbol(sym.INT,new token(yytext(),yyline+1,yycolumn+1));
					}
"double"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"double"));
						return new Symbol(sym.DOUBLE,new token(yytext(),yyline+1,yycolumn+1));
					}
"bool"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"bool"));
						return new Symbol(sym.BOOL,new token(yytext(),yyline+1,yycolumn+1));
					}
"string"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"string"));
						return new Symbol(sym.STRING,new token(yytext(),yyline+1,yycolumn+1));
					}
"class"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"class"));
						return new Symbol(sym.CLASS,new token(yytext(),yyline+1,yycolumn+1));
					}
"interface"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"interface"));
						return new Symbol(sym.interface_t,new token(yytext(),yyline+1,yycolumn+1));
					}
"null"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"null"));
						return new Symbol(sym.NULL,new token(yytext(),yyline+1,yycolumn+1));
					}
"this"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"this"));
						return new Symbol(sym.THIS,new token(yytext(),yyline+1,yycolumn+1));
					}
"while"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"while"));
						return new Symbol(sym.WHILE,new token(yytext(),yyline+1,yycolumn+1));
					}
"if"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"if"));
						return new Symbol(sym.IF,new token(yytext(),yyline+1,yycolumn+1));
					}
"else"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"else"));
						return new Symbol(sym.else_t,new token(yytext(),yyline+1,yycolumn+1));
					}
"return"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"return"));
						return new Symbol(sym.RETURN,new token(yytext(),yyline+1,yycolumn+1));
					}
"break"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"break"));
						return new Symbol(sym.BREAK,new token(yytext(),yyline+1,yycolumn+1));
					}
"New"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"New"));
						return new Symbol(sym.New,new token(yytext(),yyline+1,yycolumn+1));
					}
"NewArray"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"NewArray"));
						return new Symbol(sym.NewArray,new token(yytext(),yyline+1,yycolumn+1));
					}
"Print"				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"Print"));
						return new Symbol(sym.Print,new token(yytext(),yyline+1,yycolumn+1));
					}
"ReadInteger"		{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"ReadInteger"));
						return new Symbol(sym.ReadInteger,new token(yytext(),yyline+1,yycolumn+1));
					}
"ReadLine"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"ReadLine"));
						return new Symbol(sym.ReadLine,new token(yytext(),yyline+1,yycolumn+1));
					}
"Malloc"			{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"Malloc"));
						return new Symbol(sym.Malloc,new token(yytext(),yyline+1,yycolumn+1));
					}




";"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,";"));
						return new Symbol(sym.semicolon,new token(yytext(),yyline+1,yycolumn+1));
					}
"."					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"."));
						return new Symbol(sym.dot,new token(yytext(),yyline+1,yycolumn+1));
					}
"("					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"("));
						return new Symbol(sym.leftparen,new token(yytext(),yyline+1,yycolumn+1));
					}
")"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,")"));
						return new Symbol(sym.rightparen,new token(yytext(),yyline+1,yycolumn+1));
					}
"["					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"["));
						return new Symbol(sym.leftsquarebrace,new token(yytext(),yyline+1,yycolumn+1));
					}
"]"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"]"));
						return new Symbol(sym.rightsquarebrace,new token(yytext(),yyline+1,yycolumn+1));
					}
"{"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"{"));
						return new Symbol(sym.leftbrace,new token(yytext(),yyline+1,yycolumn+1));
					}
"}"					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"}"));
						return new Symbol(sym.rightbrace,new token(yytext(),yyline+1,yycolumn+1));
					}
","					{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,","));
						return new Symbol(sym.comma,new token(yytext(),yyline+1,yycolumn+1));
					}

{ARITMETIC}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,yytext()));
						return new Symbol(sym.arit,new token(yytext(),yyline+1,yycolumn+1));
					   }

{INTEGER}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"INTEGER"));
							return new Symbol(sym.INTCONSTANT,new token(yytext(),yyline+1,yycolumn+1));
						}
{DOUBLE}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"DOUBLE"));
							return new Symbol(sym.DOUBLECONSTANT,new token(yytext(),yyline+1,yycolumn+1));
						}



{BOOLEAN}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"BOOLEAN"));
							return new Symbol(sym.BOOLEANCONSTANT,new token(yytext(),yyline+1,yycolumn+1));
						}
{ID}       				 {if(yytext().length()>31){
			
							TokenList.add(CreateTokenLog(true,yytext().substring(0,30),yyline,yycolumn,"IDENTIFIER_TO_LONG,_MAX_SIZE_31_CHARACTERS"));
							errorCounter+=1;
						}
						else{
							TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"IDENTIFIER"));
							return new Symbol(sym.IDENT,new token(yytext(),yyline+1,yycolumn+1));
						}
						}		



{STRING}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"STRING"));
							return new Symbol(sym.STRINGCONSTANT,new token(yytext(),yyline+1,yycolumn+1));
						}
							
							
{MULTILINECOMMENT}		{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"MULTILINE_COMMENT"));*/}
{MULTILINECOMMENTERROR} { TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"MULTILINE_COMMENT_ERROR_MISSING *\\"));errorCounter+=1;}
{NORMALCOMMENT}			{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"NORMAL_COMMENT"));*/}

{NEWLINE}				{/*Do nothing...*/}
{WHITESPACES}			{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"WHITE_SPACE"));*/}



    //Error code management
.           {TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"UNRECOGNIZED CHARACTER")); errorCounter+=1;}
}
