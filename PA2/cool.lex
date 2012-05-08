/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;
import java.lang.String;
import java.lang.StringBuilder;

class FixString {
    public static String parse_string(String str) {
        String nStr = str.substring(1, str.length()-1);
        StringBuilder rStr = new StringBuilder();
        int x;
        for (x=0; x<nStr.length(); ++x) {
            if (nStr.charAt(x) == '\\' && (x++ < nStr.length())) {
                switch (nStr.charAt(x++)) {
                    case 'n':  rStr.append('\n'); break;
                    case 't':  rStr.append('\t'); break;
                    case '\\': rStr.append('\\'); break;
                    case '"':  rStr.append('"');  break;
                    default:   rStr.append('\\'); break;
                }
                x++;
            }
            else {
                rStr.append(nStr.charAt(x));
            }
        }
        return rStr.toString();
    }
}

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants

    private int curr_lineno = 1;
    int get_curr_lineno() {
        return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
        filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
        return filename;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
        case YYINITIAL:
        /* nothing special to do in the initial state */
        break;
    case COMMENT:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%{ 
    private int c_count = 0; 
%}
%class CoolLexer
%cup
%line
%state COMMENT


ALPHA=[A-Za-z]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\f\v\b\r\013]
STRING_TEXT=(\\.|[^\\\"])* 
COMMENT_TEXT=([^(*\n]|[^*\n]"("[^*)\n]|[^)\n]"*"[^)\n]|"*"[^)\n]|")"[^*\n])*
ALT_COMMENT_TEXT=(--[^\n]+)
%%


<YYINITIAL>"(*" { yybegin(COMMENT); c_count = c_count + 1; }
<YYINITIAL>"*)" {
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
<COMMENT>"(*" { c_count += 1; }
<COMMENT>"*)" {  
    c_count -= 1;
    assert(c_count >= 0);
    if (c_count == 0) {
        yybegin(YYINITIAL);
    }
}
<COMMENT> {COMMENT_TEXT} {}
<COMMENT>. {}
<YYINITIAL> {ALT_COMMENT_TEXT} {}

<YYINITIAL>";"          { return new Symbol(TokenConstants.SEMI);  }
<YYINITIAL>":"          { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"("          { return new Symbol(TokenConstants.LPAREN);}
<YYINITIAL>")"          { return new Symbol(TokenConstants.RPAREN);}
<YYINITIAL>"-"          { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*"          { return new Symbol(TokenConstants.MULT);  }
<YYINITIAL>"/"          { return new Symbol(TokenConstants.DIV);   }
<YYINITIAL>"+"          { return new Symbol(TokenConstants.PLUS);  }
<YYINITIAL>"="          { return new Symbol(TokenConstants.EQ);    }
<YYINITIAL>","          { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>"."          { return new Symbol(TokenConstants.DOT);   }
<YYINITIAL>"<-"         { return new Symbol(TokenConstants.ASSIGN);}
<YYINITIAL>"<"          { return new Symbol(TokenConstants.LT);    }
<YYINITIAL>"<="         { return new Symbol(TokenConstants.LE);    }
<YYINITIAL>"{"          { return new Symbol(TokenConstants.LBRACE);}
<YYINITIAL>"}"          { return new Symbol(TokenConstants.RBRACE);}
<YYINITIAL>"~"          { return new Symbol(TokenConstants.NEG);   }
<YYINITIAL>"@"          { return new Symbol(TokenConstants.AT);    }
<YYINITIAL>"=>"         { return new Symbol(TokenConstants.DARROW); }


<YYINITIAL>[iI][fF]             { return new Symbol(TokenConstants.IF);     }
<YYINITIAL>[fF][iI]             { return new Symbol(TokenConstants.FI);     }
<YYINITIAL>[eE][lL][sS][eE]         { return new Symbol(TokenConstants.ELSE);   }
<YYINITIAL>[lL][oO][oO][pP]         { return new Symbol(TokenConstants.LOOP);   }
<YYINITIAL>[pP][oO][oO][lL]         { return new Symbol(TokenConstants.POOL);   }
<YYINITIAL>[cC][aA][sS][eE]         { return new Symbol(TokenConstants.CASE);   }
<YYINITIAL>[eE][sS][aA][cC]         { return new Symbol(TokenConstants.ESAC);   }
<YYINITIAL>[cC][lL][aA][sS][sS]         { return new Symbol(TokenConstants.CLASS);  }
<YYINITIAL>[iI][nN]             { return new Symbol(TokenConstants.IN);     }
<YYINITIAL>[iI][nN][hH][eE][rR][iI][tT][sS] { return new Symbol(TokenConstants.INHERITS);}
<YYINITIAL>[iI][sS][vV][oO][iI][dD]     { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>[lL][eE][tT]             { return new Symbol(TokenConstants.LET);    }
<YYINITIAL>[tT][hH][eE][nN]         { return new Symbol(TokenConstants.THEN);   }
<YYINITIAL>[wW][hH][iI][lL][eE]         { return new Symbol(TokenConstants.WHILE);  }
<YYINITIAL>[nN][eE][wW]             { return new Symbol(TokenConstants.NEW);    }
<YYINITIAL>[nN][oO][tT]             { return new Symbol(TokenConstants.NOT);    }
<YYINITIAL>[oO][fF]             { return new Symbol(TokenConstants.OF);     }
<YYINITIAL>(t[rR][uU][eE])  { 
    BoolConst bool = new BoolConst(true);
    return new Symbol(TokenConstants.BOOL_CONST, "true");
}
<YYINITIAL>(f[aA][lL][sS][eE])  { 
    BoolConst bool = new BoolConst(false);
    return new Symbol(TokenConstants.BOOL_CONST, "false");
}

<YYINITIAL> {NONNEWLINE_WHITE_SPACE_CHAR}+ { }
<YYINITIAL,COMMENT> \n { curr_lineno += 1; }

<YYINITIAL> \"{STRING_TEXT}\" {
    StringTable table = new StringTable();
    String str = FixString.parse_string(yytext());
    return new Symbol(TokenConstants.STR_CONST, table.addString(str));
}
<YYINITIAL> \"{STRING_TEXT} {
    StringTable table = new StringTable();
    String str =  yytext().substring(1,yytext().length());
    assert(str.length() == yytext().length() - 1);
    return new Symbol(TokenConstants.ERROR, table.addString("EOF in string constant"));
} 

<YYINITIAL>{DIGIT}+ { 
    IntTable table = new IntTable();
    return new Symbol(TokenConstants.INT_CONST, table.addString(yytext()));
}   

<YYINITIAL> ([a-z]({ALPHA}|{DIGIT}|_)*) {
    IdTable table = new IdTable();
    return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}   

<YYINITIAL> ([A-Z]({ALPHA}|{DIGIT}|_)*) {
    IdTable table = new IdTable();
    return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}


. {     
    return new Symbol(TokenConstants.ERROR, yytext());
}

