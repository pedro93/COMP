/* Generated By:JJTree&JavaCC: Do not edit this line. CNFParser.java */
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Vector;
import java.lang.String;

public class CNFParser/*@bgen(jjtree)*/implements CNFParserTreeConstants, CNFParserConstants {/*@bgen(jjtree)*/
  protected static JJTCNFParserState jjtree = new JJTCNFParserState();
static HashMap<Structure, List<Vector<Structure>>> SymbolTable = new HashMap<Structure,List<Vector<Structure>>>();

public static void main(String args[]) throws ParseException,FileNotFoundException {
         CNFParser myParser = new CNFParser(new FileInputStream(new File("grammar.txt")));
         SimpleNode root = myParser.Expression(); // Devolve refer�ncia para o n� raiz da �rvore 

        //Create Symbol Table
     myParser.createSymbolTable(root);
     myParser.SemanticAnalysis();

         System.out.println("Tamanho da hash: "+SymbolTable.size());
         root.dump(""); // imprime no ecr� a �rvore
     System.out.println(SymbolTable.toString());
 }

void createSymbolTable(SimpleNode node) {
        for(int i=0; i< node.jjtGetNumChildren(); i++) {
           createSymbolTable((SimpleNode) node.jjtGetChild(i));
        }
        if(node.id == CNFParserTreeConstants.JJTATRIBUTION) {
                        if(node.Variables.size()!=0)
                        {
                        List<Vector<Structure>> aux;
                        if(SymbolTable.get(node.Symbol)==null)
                                aux = new LinkedList<Vector<Structure >>();
                        else
                                aux= SymbolTable.get(node.Symbol);
                                aux.add(node.Variables);
                                SymbolTable.put(node.Symbol, aux);
                                return;
                }
                else //gramatica n�o permite chegar a este ponto
                {
                        System.out.println("Atribui\u00e7\u00e3o inv\u00e1lida na linha "+node.Symbol.line+" ,coluna "+node.Symbol.column);
                        System.exit(1);
                }
        }
        else if(node.id!=CNFParserTreeConstants.JJTEXPRESSION)
        {
                System.out.println("Operador ilegal!");
                        System.exit(1);
        }
        return;
  }

void SemanticAnalysis()
{
        List<Vector<Structure>> value;
        for(Entry<Structure, List<Vector<Structure>>> entry : SymbolTable.entrySet()) {
                value= entry.getValue();
        for (Vector<Structure> i : value) {
            for(Structure x:i)
            {
                                if(x.name.substring(0,1).matches("[a-z]"))
                                        continue;
                else if(!SymbolTable.containsKey(x))
                                {
                                        System.out.println("Found "+x.type+": " +x.name+" without production in line "+x.line+", coloumn "+x.column);
                                        System.exit(1);
                                }
                        }
                }
        }
}

  final public SimpleNode Expression() throws ParseException {
                          /*@bgen(jjtree) Expression */
  SimpleNode jjtn000 = new SimpleNode(JJTEXPRESSION);
  boolean jjtc000 = true;
  jjtree.openNodeScope(jjtn000);
    try {
      label_1:
      while (true) {
        Atribution();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NonTerm:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
      }
      jj_consume_token(END);
                                  jjtree.closeNodeScope(jjtn000, true);
                                  jjtc000 = false;
                                 {if (true) return jjtn000;}
    } catch (Throwable jjte000) {
           if (jjtc000) {
             jjtree.clearNodeScope(jjtn000);
             jjtc000 = false;
           } else {
             jjtree.popNode();
           }
           if (jjte000 instanceof RuntimeException) {
             {if (true) throw (RuntimeException)jjte000;}
           }
           if (jjte000 instanceof ParseException) {
             {if (true) throw (ParseException)jjte000;}
           }
           {if (true) throw (Error)jjte000;}
    } finally {
           if (jjtc000) {
             jjtree.closeNodeScope(jjtn000, true);
           }
    }
    throw new Error("Missing return statement in function");
  }

  static final public void Atribution() throws ParseException {
                   /*@bgen(jjtree) Atribution */
                   SimpleNode jjtn000 = new SimpleNode(JJTATRIBUTION);
                   boolean jjtc000 = true;
                   jjtree.openNodeScope(jjtn000);Token t,t1,t2;
    try {
      //S:= tu e o gato NP VP
        //VP:=Det
        //Det:= tu
        //VP:= e o gato
      
        //SYMBOL:=(NonTerm)+ (Term)* | (Term)+;
      
         t = jj_consume_token(NonTerm);
      jj_consume_token(AT);
   jjtn000.Symbol.name = new String(t.image);
   jjtn000.Symbol.line=t.beginLine;
   jjtn000.Symbol.column=t.beginColumn;
   jjtn000.Symbol.type=Type.NONTERM;
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NonTerm:
        label_2:
        while (true) {
          t1 = jj_consume_token(NonTerm);
                 jjtn000.Variables.add(new Structure(t1.image,Type.NONTERM,t1.beginLine,t1.beginColumn));
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case NonTerm:
            ;
            break;
          default:
            jj_la1[1] = jj_gen;
            break label_2;
          }
        }
        label_3:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case Term:
            ;
            break;
          default:
            jj_la1[2] = jj_gen;
            break label_3;
          }
          t2 = jj_consume_token(Term);
              jjtn000.Variables.add(new Structure(t2.image,Type.TERM,t2.beginLine,t2.beginColumn));
        }
        break;
      case Term:
        label_4:
        while (true) {
          t1 = jj_consume_token(Term);
            jjtn000.Variables.add(new Structure(t1.image,Type.TERM,t1.beginLine,t1.beginColumn));
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case Term:
            ;
            break;
          default:
            jj_la1[3] = jj_gen;
            break label_4;
          }
        }
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      jj_consume_token(9);
    } finally {
     if (jjtc000) {
       jjtree.closeNodeScope(jjtn000, true);
     }
    }
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public CNFParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[5];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x80,0x80,0x100,0x100,0x180,};
   }

  /** Constructor with InputStream. */
  public CNFParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public CNFParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new CNFParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public CNFParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new CNFParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public CNFParser(CNFParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(CNFParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[10];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 5; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 10; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

 /*
int eval(SimpleNode node)
{ 
	if(node.jjtGetNumChildren() == 0) // leaf node with integer value 
 		return node.val; 
 	else if(node.jjtGetNumChildren() == 1) // only one child 
 		return this.eval((SimpleNode) node.jjtGetChild(0)); 
 
 	SimpleNode lhs = (SimpleNode) node.jjtGetChild(0); //left child 
 	SimpleNode rhs = (SimpleNode) node.jjtGetChild(1); // right child 
 
	switch(node.id) { 
	 	case CNFParserTreeConstants.JJTADD : return eval( lhs ) + eval( rhs ); 
	 	case CNFParserTreeConstants.JJTSUB : return eval( lhs ) - eval( rhs ); 
		case CNFParserTreeConstants.JJTMUL : return eval( lhs ) * eval( rhs ); 
		case CNFParserTreeConstants.JJTDIV : return eval( lhs ) / eval( rhs ); 
	 	default : // abort 
	 		System.out.println("Operador ilegal!"); 
	 		System.exit(1); 
 		} 
 	return 0; 
	}
	*/
}