//packages
package minicsharp;
%%

%{
//Developer´s extra code declaration
class Yytoken{
		public Yytoken(){

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
%%
<YYINITIAL> {

    //RE behaviour code
{ID}        {}
    //Error code management
.           {}
}