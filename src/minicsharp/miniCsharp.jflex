//packages
package minicsharp;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
%%

%{
//Developer´s extra code declaration
class Yytoken{
		public Yytoken(){

		}
	}
public static ArrayList<String>TokenList=new ArrayList<String>();
public static String CreateTokenLog(Boolean isError,String token, int lineNumber,int columnNumber,String description){
	String newToken=token+"\t\tLine Number: "+(lineNumber+1)+"\t"+"Column Number: "+(columnNumber+1)+" To: "+getLastColumn(columnNumber+1,token.length())+"\tToken Type: "+description+"\n";

	String ErrorToken=token+"\t\tError at line: "+(lineNumber+1)+"\tDescription: "+description+"\n";

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
          
      }
for(String str: TokenList) {
  writer.write(str);
}
writer.close();
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
DOUBLE=(([0-9]+|([0-9]*(\.)[0-9]+)|([0-9]+(\.)[0-9]))(e|E)(\+|-)?[0-9]+)
STRING=(\"([^\"\n]|\\.)*\")
MULTILINECOMMENTERROR="\/\*"~""
MULTILINECOMMENT="\/\*"~"\*\/"
NORMALCOMMENT=\/\/"~"\n
WHITESPACES=[\r|\t|\f|\s|\g]
NEWLINE=\n

%%
<YYINITIAL> {

    //RE behaviour code

{MULTILINECOMMENTERROR}<<EOF>> {TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"MULTILINE_COMMENT_ERROR_MISSING \\*"));}
{RESERVEDWORDS}			{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"RESERVERD_WORD"));}
{MULTILINECOMMENT}		{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"MULTILINE_COMMENT"));}
{OPERATORS}				{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"OPERATOR"));}
{BOOLEAN}				{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"BOOLEAN"));}
{ID}        {if(yytext().length()>31){
			TokenList.add(CreateTokenLog(true,yytext().substring(0,30),yyline,yycolumn,"IDENTIFIER_TO_LONG,_MAX_SIZE_31_CHARACTERS"));

			}
			else{
				TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"IDENTIFIER"));
			}
		}

{INTEGER}				{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"INTEGER"));}
{DOUBLE}				{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"DOUBLE"));}
{STRING}				{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"STRING"));}
{NORMALCOMMENT}			{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"NORMAL_COMMENT"));}
{NEWLINE}				{/*Do nothing...*/}
{WHITESPACES}			{TokenList.add(CreateTokenLog(false,yytext(),yyline,yycolumn,"WHITE_SPACE"));}



    //Error code management
.           {TokenList.add(CreateTokenLog(true,yytext(),yyline,yycolumn,"UNRECOGNIZED CHARACTER"));}
}
