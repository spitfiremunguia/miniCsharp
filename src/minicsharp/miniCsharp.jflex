//packages
package minicsharp;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
%%

%{
//Developer´s extra code declaration
private static int errorCounter=0;
public   boolean commentError=false;
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
%char
%column
%line
%unicode
//RE declaration area
ID =  [a-zA-Z_\x7f-\xff][a-zA-Z0-9_\x7f-\xff]*
RESERVEDWORDS=void|int|double|bool|string|class|interface|null|this|extends|implements|for|while|if|else|return|break|New|NewArray
OPERATORS=\[\]|\[|\]|\{\}|\{|\}|\(\)|\(|\)|<|>|\+|-|\*|\/|\%|=|==|\!=|>=|<=|\!|\&\&|\|\||\;|\,|\.
BOOLEAN=true|false
INTEGER=(0|[1-9][0-9]*)|(0((x|X)[0-9a-fA-F]+)|[0-7]+|(b|B)(0|1)+)
DOUBLE=[-+]?[0-9]+\.?[0-9]+([eE]{INTEGER}.?[0-9]*)?/*(([0-9]+|([0-9]*(\.)[0-9]+)|([0-9]+(\.)[0-9]))(e|E)(\+|-)?[0-9]+)*/
STRING=(\"([^\"\n]|\\.)*\")
MULTILINECOMMENTERROR="/*"("*"[^/]|[^*/]|[^*]"/")*
MULTILINECOMMENT="\/\*"~"\*\/"
NORMALCOMMENT="\/\/"~"\n"
WHITESPACES=[\r|\t|\f|\s|\g]
NEWLINE=\n

%%
<YYINITIAL> {

    //RE behaviour code


{RESERVEDWORDS}			{
							TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"RESERVERD_WORD"));}
{OPERATORS}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"OPERATOR"));}
{BOOLEAN}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"BOOLEAN"));}
{ID}       				 {if(yytext().length()>31){
			
			TokenList.add(CreateTokenLog(true,yytext().substring(0,30),yyline,yycolumn,"IDENTIFIER_TO_LONG,_MAX_SIZE_31_CHARACTERS"));
			errorCounter+=1;
			}
			else{
				TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"IDENTIFIER"));
			}
		}
{INTEGER}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"INTEGER"));}
{DOUBLE}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"DOUBLE"));}


{STRING}				{TokenList.add(CreateTokenLog(commentError,yytext(),yyline,yycolumn,"STRING"));}
							
							
{MULTILINECOMMENT}		{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"MULTILINE_COMMENT"));*/}
{MULTILINECOMMENTERROR} { TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"MULTILINE_COMMENT_ERROR_MISSING *\\"));errorCounter+=1;}
{NORMALCOMMENT}			{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"NORMAL_COMMENT"));*/}

{NEWLINE}				{/*Do nothing...*/}
{WHITESPACES}			{/*TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"WHITE_SPACE"));*/}



    //Error code management
.           {TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"UNRECOGNIZED CHARACTER")); errorCounter+=1;}
}
