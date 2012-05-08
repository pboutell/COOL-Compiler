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


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
 
	private int c_count = 0; 
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		51
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NOT_ACCEPT,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NOT_ACCEPT,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"5:8,39:2,41,39:2,4,5:18,39,5,42,5:5,1,3,2,10,12,6,13,9,44:10,8,7,14,11,19,5" +
",18,28,60,27,34,22,21,60,30,20,60:2,23,60,29,25,26,60,31,24,32,37,33,35,60:" +
"3,5,43,5:2,61,5,45,46,47,48,49,38,46,50,51,46:2,52,46,53,54,55,46,56,57,36," +
"58,40,59,46:3,15,5,16,17,5,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,174,
"0,1,2,3,1,4,1,5,1:4,6,1:2,7,1:4,8,9,1,10,11,1:5,12,13,12:2,1,14,12:9,15,12:" +
"2,15,12:2,16,17,18,1,14,19,20,21,15,22,15:15,23,24,25,26,27,28,29,30,31,32," +
"33,34,35,36,37,38,39,40,41,42,18,43,44,45,46,47,48,49,50,51,52,53,54,55,56," +
"57,58,59,60,61,62,63,64,65,36,66,67,68,69,70,71,72,73,74,15,75,76,77,78,79," +
"80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103" +
",104,105,106,15,12,107,108,109,110,111,112,113,114,115")[0];

	private int yy_nxt[][] = unpackFromString(116,62,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,6,20,57,79,121,164,83,166,1" +
"68,164,125,164:2,170,164:2,172,21,164,58,5,56,22,23,6,24,163:2,165,163,167," +
"163,80,119,123,84,169,163:3,171,164,6,-1:64,25,-1:62,26,-1:62,5,-1:34,5:2,-" +
"1:27,55,-1:74,27,-1:48,28,-1:4,29,-1:70,164,30,164:2,173,164:4,31,164:8,30," +
"-1,164,-1:3,164:9,31,164:3,173,164:4,-1:20,163:10,126,128,163:7,-1,163,-1:3" +
",130,163:5,126,163:5,128,163:4,130,-1,23:41,34,78,23:18,-1:44,24,-1:37,164:" +
"19,-1,164,-1:3,164:18,-1:20,164:10,145,164:8,-1,164,-1:3,164:6,145,164:11,-" +
"1,35:40,-1,35:20,-1:20,163:19,-1,163,-1:3,130,163:16,130,1,52,77,76,81:37,2" +
"2,81:20,-1,82,53,-1:60,122:2,-1,122:37,-1,122:20,-1:4,5,-1:15,163:19,5,56,-" +
"1:3,130,163:16,130,-1:20,32,164:18,-1,164,-1:3,164:7,32,164:10,-1:20,61,163" +
":7,132,163:10,-1,163,-1:3,130,132,163:5,61,163:9,130,-1:20,163:10,156,163:8" +
",-1,163,-1:3,130,163:5,156,163:10,130,-1,120,86,76,81:37,-1,81:20,-1,122,85" +
",54,122:37,-1,122:20,-1,23:3,-1,23:36,-1,23:20,-1:20,164:3,127,129,164:14,-" +
"1,164,-1:3,164:8,127,164:4,129,164:4,-1:20,163,59,163:2,142,163:4,60,163:8," +
"59,-1,163,-1:3,130,163:8,60,163:3,142,163:3,130,-1,90,86,76,81:37,-1,81:20," +
"-1,122,-1:2,122:37,-1,122:20,-1:20,164,33,164:16,33,-1,164,-1:3,164:18,-1:2" +
"0,163,62,163:16,62,-1,163,-1:3,130,163:16,130,-1,81,85,76,81:37,-1,81:20,-1" +
",122,85,-1,122:37,-1,122:20,-1:20,164:12,36,164:3,36,164:2,-1,164,-1:3,164:" +
"18,-1:20,163:12,63,163:3,63,163:2,-1,163,-1:3,130,163:16,130,-1,89,86,76,81" +
":37,-1,81:20,-1,124,96,-1,122:37,-1,122:20,-1:20,164:15,37,164:3,-1,164,-1:" +
"3,164:15,37,164:2,-1:20,163:15,64,163:3,-1,163,-1:3,130,163:14,64,163,130,-" +
"1,82,96,-1:79,164:12,38,164:3,38,164:2,-1,164,-1:3,164:18,-1:20,163:12,65,1" +
"63:3,65,163:2,-1,163,-1:3,130,163:16,130,-1:20,164:2,39,164:16,-1,164,-1:3," +
"164:5,39,164:12,-1:20,163:9,71,163:9,-1,163,-1:3,130,163:8,71,163:7,130,-1:" +
"20,164:7,40,164:11,-1,164,-1:3,164:3,40,164:14,-1:20,163:2,45,163:16,-1,163" +
",-1:3,130,163:4,45,163:11,130,-1:20,164:6,41,164:12,-1,164,-1:3,164:11,41,1" +
"64:6,-1:20,163:2,70,163:16,-1,163,-1:3,130,163:4,70,163:11,130,-1:20,164:3," +
"42,164:15,-1,164,-1:3,164:8,42,164:9,-1:20,163:2,66,163:16,-1,163,-1:3,130," +
"163:4,66,163:11,130,-1:20,164:2,43,164:16,-1,164,-1:3,164:5,43,164:12,-1:20" +
",163:7,67,163:11,-1,163,-1:3,130,163:2,67,163:13,130,-1:20,164:9,44,164:9,-" +
"1,164,-1:3,164:9,44,164:8,-1:20,163:6,68,163:12,-1,163,-1:3,130,163:10,68,1" +
"63:5,130,-1:20,164:4,46,164:14,-1,164,-1:3,164:13,46,164:4,-1:20,163:3,69,1" +
"63:15,-1,163,-1:3,130,163:7,69,163:8,130,-1:20,164:2,47,164:16,-1,164,-1:3," +
"164:5,47,164:12,-1:20,163:2,48,163:16,-1,163,-1:3,130,163:4,48,163:11,130,-" +
"1:20,164:14,49,164:4,-1,164,-1:3,164:4,49,164:13,-1:20,163:4,72,163:14,-1,1" +
"63,-1:3,130,163:12,72,163:3,130,-1:20,164:4,50,164:14,-1,164,-1:3,164:13,50" +
",164:4,-1:20,163:2,73,163:16,-1,163,-1:3,130,163:4,73,163:11,130,-1:20,163:" +
"14,74,163:4,-1,163,-1:3,130,163:3,74,163:12,130,-1:20,163:4,75,163:14,-1,16" +
"3,-1:3,130,163:12,75,163:3,130,-1:20,163:2,88,163:2,144,163:13,-1,163,-1:3," +
"130,163:4,88,163:4,144,163:6,130,-1:20,164:2,87,164:2,131,164:13,-1,164,-1:" +
"3,164:5,87,164:4,131,164:7,-1,93,86,76,81:37,-1,81:20,-1:20,163:2,92,163:2," +
"95,163:13,-1,163,-1:3,130,163:4,92,163:4,95,163:6,130,-1,81,86,76,81:37,-1," +
"81:20,-1:20,164:2,91,164:2,94,164:13,-1,164,-1:3,164:5,91,164:4,94,164:7,-1" +
":20,163:2,98,163:16,-1,163,-1:3,130,163:4,98,163:11,130,-1:20,164:4,97,164:" +
"14,-1,164,-1:3,164:13,97,164:4,-1:20,163:17,100,163,-1,163,-1:3,130,163:13," +
"100,163:2,130,-1:20,164:8,99,164:10,-1,164,-1:3,164,99,164:16,-1:20,164:5,1" +
"01,164:13,-1,164,-1:3,164:10,101,164:7,-1:20,163:3,150,163:15,-1,163,-1:3,1" +
"30,163:7,150,163:8,130,-1:20,164:5,103,164:13,-1,164,-1:3,164:10,103,164:7," +
"-1:20,163:8,152,163:10,-1,163,-1:3,130,152,163:15,130,-1:20,164:8,147,164:1" +
"0,-1,164,-1:3,164,147,164:16,-1:20,163:4,102,163:14,-1,163,-1:3,130,163:12," +
"102,163:3,130,-1:20,164:4,105,164:14,-1,164,-1:3,164:13,105,164:4,-1:20,163" +
":4,104,163:14,-1,163,-1:3,130,163:12,104,163:3,130,-1:20,164:2,107,164:16,-" +
"1,164,-1:3,164:5,107,164:12,-1:20,163:8,106,163:10,-1,163,-1:3,130,106,163:" +
"15,130,-1:20,149,164:18,-1,164,-1:3,164:7,149,164:10,-1:20,163:13,154,163:5" +
",-1,154,-1:3,130,163:16,130,-1:20,164:5,151,164:13,-1,164,-1:3,164:10,151,1" +
"64:7,-1:20,163:5,108,163:13,-1,163,-1:3,130,163:9,108,163:6,130,-1:20,164:2" +
",153,164:16,-1,164,-1:3,164:5,153,164:12,-1:20,163:5,110,163:13,-1,163,-1:3" +
",130,163:9,110,163:6,130,-1:20,164:4,109,164:14,-1,164,-1:3,164:13,109,164:" +
"4,-1:20,158,163:18,-1,163,-1:3,130,163:6,158,163:9,130,-1:20,164:3,111,164:" +
"15,-1,164,-1:3,164:8,111,164:9,-1:20,163:4,112,163:14,-1,163,-1:3,130,163:1" +
"2,112,163:3,130,-1:20,113,164:18,-1,164,-1:3,164:7,113,164:10,-1:20,163:4,1" +
"14,163:14,-1,163,-1:3,130,163:12,114,163:3,130,-1:20,164:11,155,164:7,-1,16" +
"4,-1:3,164:12,155,164:5,-1:20,163:5,159,163:13,-1,163,-1:3,130,163:9,159,16" +
"3:6,130,-1:20,157,164:18,-1,164,-1:3,164:7,157,164:10,-1:20,163:2,160,163:1" +
"6,-1,163,-1:3,130,163:4,160,163:11,130,-1:20,164:12,115,164:3,115,164:2,-1," +
"164,-1:3,164:18,-1:20,163:3,116,163:15,-1,163,-1:3,130,163:7,116,163:8,130," +
"-1:20,117,163:18,-1,163,-1:3,130,163:6,117,163:9,130,-1:20,163:11,161,163:7" +
",-1,163,-1:3,130,163:11,161,163:4,130,-1:20,162,163:18,-1,163,-1:3,130,163:" +
"6,162,163:9,130,-1:20,163:12,118,163:3,118,163:2,-1,163,-1:3,130,163:16,130" +
",-1:20,163:3,134,163:4,136,163:10,-1,163,-1:3,130,136,163:6,134,163:8,130,-" +
"1:20,164:5,133,164:13,-1,164,-1:3,164:10,133,164:7,-1:20,163:3,138,140,163:" +
"14,-1,163,-1:3,130,163:7,138,163:4,140,163:3,130,-1:20,164:3,135,164:4,137," +
"164:10,-1,164,-1:3,164,137,164:6,135,164:9,-1:20,163:5,146,163:13,-1,163,-1" +
":3,130,163:9,146,163:6,130,-1:20,164:10,139,164:8,-1,164,-1:3,164:6,139,164" +
":11,-1:20,163:10,148,163:8,-1,163,-1:3,130,163:5,148,163:10,130,-1:20,164:1" +
"0,141,164:8,-1,164,-1:3,164:6,141,164:11,-1:20,164:13,143,164:5,-1,143,-1:3" +
",164:18");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.LPAREN);}
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.MULT);  }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.RPAREN);}
					case -5:
						break;
					case 5:
						{ }
					case -6:
						break;
					case 6:
						{     
	return new Symbol(TokenConstants.ERROR, yytext());
}
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.MINUS); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.SEMI);  }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.COLON); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.DIV);   }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.PLUS);  }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.EQ);    }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.COMMA); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.DOT);   }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.LT);    }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.LBRACE);}
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.RBRACE);}
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.NEG);   }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.AT);    }
					case -20:
						break;
					case 20:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -21:
						break;
					case 21:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -22:
						break;
					case 22:
						{ curr_lineno += 1; }
					case -23:
						break;
					case 23:
						{
	StringTable table = new StringTable();
	String str =  yytext().substring(1,yytext().length());
	assert(str.length() == yytext().length() - 1);
	return new Symbol(TokenConstants.ERROR, table.addString("EOF in string constant"));
}
					case -24:
						break;
					case 24:
						{ 
	IntTable table = new IntTable();
	return new Symbol(TokenConstants.INT_CONST, table.addString(yytext()));
}
					case -25:
						break;
					case 25:
						{ yybegin(COMMENT); c_count = c_count + 1; }
					case -26:
						break;
					case 26:
						{
	return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.DARROW); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.ASSIGN);}
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.LE);    }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.IF);     }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.IN);     }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.FI);     }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.OF);	    }
					case -34:
						break;
					case 34:
						{
	StringTable table = new StringTable();
	String str = FixString.parse_string(yytext());
	return new Symbol(TokenConstants.STR_CONST, table.addString(str));
}
					case -35:
						break;
					case 35:
						{}
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.LET);    }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NEW);    }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.NOT);    }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.ELSE);   }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ESAC);   }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.LOOP);   }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.POOL);   }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.CASE);   }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.THEN);   }
					case -45:
						break;
					case 45:
						{ 
	BoolConst bool = new BoolConst(true);
	return new Symbol(TokenConstants.BOOL_CONST, "true");
}
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.CLASS);  }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.WHILE);  }
					case -48:
						break;
					case 48:
						{ 
	BoolConst bool = new BoolConst(false);
	return new Symbol(TokenConstants.BOOL_CONST, "false");
}
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -50:
						break;
					case 50:
						{ return new Symbol(TokenConstants.INHERITS);}
					case -51:
						break;
					case 51:
						{}
					case -52:
						break;
					case 52:
						{}
					case -53:
						break;
					case 53:
						{ c_count += 1; }
					case -54:
						break;
					case 54:
						{  
	c_count -= 1;
	assert(c_count >= 0);
	if (c_count == 0) {
		yybegin(YYINITIAL);
	}
}
					case -55:
						break;
					case 56:
						{ }
					case -56:
						break;
					case 57:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -57:
						break;
					case 58:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -58:
						break;
					case 59:
						{ return new Symbol(TokenConstants.IF);     }
					case -59:
						break;
					case 60:
						{ return new Symbol(TokenConstants.IN);     }
					case -60:
						break;
					case 61:
						{ return new Symbol(TokenConstants.FI);     }
					case -61:
						break;
					case 62:
						{ return new Symbol(TokenConstants.OF);	    }
					case -62:
						break;
					case 63:
						{ return new Symbol(TokenConstants.LET);    }
					case -63:
						break;
					case 64:
						{ return new Symbol(TokenConstants.NEW);    }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.NOT);    }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.ELSE);   }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.ESAC);   }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.LOOP);   }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.POOL);   }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.CASE);   }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.THEN);   }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.CLASS);  }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.WHILE);  }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.INHERITS);}
					case -75:
						break;
					case 76:
						{}
					case -76:
						break;
					case 77:
						{}
					case -77:
						break;
					case 79:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -78:
						break;
					case 80:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -79:
						break;
					case 81:
						{}
					case -80:
						break;
					case 83:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -81:
						break;
					case 84:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -82:
						break;
					case 85:
						{}
					case -83:
						break;
					case 87:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -84:
						break;
					case 88:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -85:
						break;
					case 89:
						{}
					case -86:
						break;
					case 91:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -87:
						break;
					case 92:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -88:
						break;
					case 94:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -89:
						break;
					case 95:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -90:
						break;
					case 97:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -91:
						break;
					case 98:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -92:
						break;
					case 99:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -93:
						break;
					case 100:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -94:
						break;
					case 101:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -95:
						break;
					case 102:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -96:
						break;
					case 103:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -97:
						break;
					case 104:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -98:
						break;
					case 105:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -99:
						break;
					case 106:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -100:
						break;
					case 107:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -101:
						break;
					case 108:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -102:
						break;
					case 109:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -103:
						break;
					case 110:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -104:
						break;
					case 111:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -105:
						break;
					case 112:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -106:
						break;
					case 113:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -107:
						break;
					case 114:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -108:
						break;
					case 115:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -109:
						break;
					case 116:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -110:
						break;
					case 117:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -111:
						break;
					case 118:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -112:
						break;
					case 119:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -113:
						break;
					case 120:
						{}
					case -114:
						break;
					case 121:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -115:
						break;
					case 122:
						{}
					case -116:
						break;
					case 123:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -117:
						break;
					case 124:
						{}
					case -118:
						break;
					case 125:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -119:
						break;
					case 126:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -120:
						break;
					case 127:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -121:
						break;
					case 128:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -122:
						break;
					case 129:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -123:
						break;
					case 130:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -124:
						break;
					case 131:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -125:
						break;
					case 132:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -126:
						break;
					case 133:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -127:
						break;
					case 134:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -128:
						break;
					case 135:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -129:
						break;
					case 136:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -130:
						break;
					case 137:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -131:
						break;
					case 138:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -132:
						break;
					case 139:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -133:
						break;
					case 140:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -134:
						break;
					case 141:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -135:
						break;
					case 142:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -136:
						break;
					case 143:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -137:
						break;
					case 144:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -138:
						break;
					case 145:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -139:
						break;
					case 146:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -140:
						break;
					case 147:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -141:
						break;
					case 148:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -142:
						break;
					case 149:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -143:
						break;
					case 150:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -144:
						break;
					case 151:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -145:
						break;
					case 152:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -146:
						break;
					case 153:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -147:
						break;
					case 154:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -148:
						break;
					case 155:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -149:
						break;
					case 156:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -150:
						break;
					case 157:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -151:
						break;
					case 158:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -152:
						break;
					case 159:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -153:
						break;
					case 160:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -154:
						break;
					case 161:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -155:
						break;
					case 162:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -156:
						break;
					case 163:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -157:
						break;
					case 164:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -158:
						break;
					case 165:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -159:
						break;
					case 166:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -160:
						break;
					case 167:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -161:
						break;
					case 168:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -162:
						break;
					case 169:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -163:
						break;
					case 170:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -164:
						break;
					case 171:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.OBJECTID, table.addString(yytext()));
}
					case -165:
						break;
					case 172:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -166:
						break;
					case 173:
						{
	IdTable table = new IdTable();
	return new Symbol(TokenConstants.TYPEID, table.addString(yytext()));
}
					case -167:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
