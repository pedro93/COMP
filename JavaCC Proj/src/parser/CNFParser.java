/* Generated By:JJTree&JavaCC: Do not edit this line. CNFParser.java */
package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import common.Structure;
import common.Type;
import algorithm.ChomskyConverter;

public class CNFParser implements/*@bgen(jjtree)*/ CNFParserTreeConstants,Runnable, CNFParserConstants {/*@bgen(jjtree)*/
  protected static JJTCNFParserState jjtree = new JJTCNFParserState();
        private TreeMap<Structure, List<Vector<Structure>>> SymbolTable = new TreeMap<Structure,List<Vector<Structure>>>();
        private Vector<Structure> AcessableTokens = new Vector<Structure>();
        private String filePath;
        private File grammarFile;

        public boolean isValid=false;
        public boolean saveSucessful=false;


        public CNFParser(String path) throws FileNotFoundException
        {
                this(new FileInputStream(new File(path)), null);
                this.filePath = path;
                grammarFile=new File(filePath);
        }

        @Override
        public void run()
        {
                try {
                        SimpleNode root = Expression(); // devolve referência para o nó raiz da àrvore 

                        //Create Symbol Table
                        this.createSymbolTable(root);

                        this.SemanticAnalysis();

                        if(!isValid)
                                return;

                        printSymbolTable();

                        ChomskyConverter cnf = new ChomskyConverter(SymbolTable) ;
                        Vector < Vector <String >> productions = new Vector < Vector<String >>() ;
                        productions = cnf.CNFconverter() ;

                        saveGrammarToFile( productions );

                        System.out.println("Data structure from: "+grammarFile.getName()+" parsed and validated, next run CYK Algorithm");


                }catch(ParseException i){
                        i.printStackTrace();
                }
                catch (Exception e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                }
        }

        public void saveGrammarToFile( Vector<Vector<String >>productions ){
                String fileName = grammarFile.getName().substring(0, grammarFile.getName().lastIndexOf("."));

                System.out.println("Saving "+fileName);
                FileOutputStream fOut=null;
                ObjectOutputStream oOut=null;

                try{
                        //Save to file

                        fOut= new FileOutputStream(fileName+".ser");
                        oOut = new ObjectOutputStream(fOut);
                        oOut.writeObject(productions);
                        System.out.println("Save sucessful");
                        saveSucessful=true;
                }
                catch(IOException e) {
                        System.err.println("Error: "+e.toString());
                        e.printStackTrace();
                }
                finally {
                        try {
                                oOut.flush();
                                oOut.close();
                                fOut.close();
                        }
                        catch (IOException e1) {
                                e1.printStackTrace();
                        }
                }

        }

        void printSymbolTable()
        {
                List<Vector<Structure>> value;
                Structure key;
                System.out.println("TreeHash");
                for(Entry<Structure, List<Vector<Structure>>> entry : SymbolTable.entrySet())
                {
                        value=entry.getValue();
                        key=entry.getKey();
                        System.out.print("Key: "+key.toString()+" -> Values:");
                        for (Vector<Structure> i : value) {
                                System.out.print("[");
                                for(Structure x:i)
                                {
                                        System.out.print(x.toString()+",");
                                }
                                System.out.print("]");
                        }
                        System.out.println();
                }
        }

        void createSymbolTable(SimpleNode node) {
                for(int i=0; i< node.jjtGetNumChildren(); i++) {
                        createSymbolTable((SimpleNode) node.jjtGetChild(i));
                }
                if(node.id == CNFParserTreeConstants.JJTSTARTATRIBUTION) {
                        if(node.Variables.size()!=0)
                        {
                                List<Vector<Structure>> aux;
                                if(SymbolTable.get(node.Symbol)==null)
                                {
                                        SymbolTable.put(node.Symbol, node.Variables);
                                }
                                else
                                {
                                        aux = SymbolTable.get(node.Symbol);
                                        for(Vector<Structure> temp: node.Variables)
                                                aux.add(temp);
                                }
                                aux = node.Variables;
                                for (Vector<Structure> temp : aux) {
                                        for (Structure struct : temp) {
                                                if(!AcessableTokens.contains(struct))
                                                        AcessableTokens.add(struct);
                                        }
                                }
                                return;
                        }
                        else //gramatica não permite chegar a este ponto
                        {
                                System.err.println("[Error] Invalid attribution in line "+node.Symbol.line+" ,column "+node.Symbol.column);
                                isValid=false;
                        }
                }
                else if(node.id == CNFParserTreeConstants.JJTATRIBUTION) {
                        if(node.Variables.size()!=0)
                        {
                                List<Vector<Structure>> aux;
                                if(SymbolTable.get(node.Symbol)==null)
                                {
                                        SymbolTable.put(node.Symbol, node.Variables);
                                }
                                else
                                {
                                        aux = SymbolTable.get(node.Symbol);
                                        for(Vector<Structure> temp: node.Variables)
                                                aux.add(temp);
                                }
                                aux = node.Variables;
                                for (Vector<Structure> temp : aux) {
                                        for (Structure struct : temp) {
                                                if(!AcessableTokens.contains(struct))
                                                        AcessableTokens.add(struct);
                                        }
                                }
                                return;
                        }
                        else //gramatica não permite chegar a este ponto
                        {
                                System.err.println("[Error] Invalid attribution in line "+node.Symbol.line+" ,column "+node.Symbol.column);
                                isValid=false;
                        }
                }
                else if(node.id!=CNFParserTreeConstants.JJTEXPRESSION)
                {
                        System.err.println("[Error] Ilegal operator in line "+node.Symbol.line+" ,column "+node.Symbol.column+" !");
                        isValid=false;
                }
                else
                        isValid=true;
                return;
        }

        void SemanticAnalysis()
        {
                boolean fail=false;
                List<Vector<Structure>> value;
                Structure key;
                for(Entry<Structure, List<Vector<Structure>>> entry : SymbolTable.entrySet()) {
                        value= entry.getValue();
                        key=entry.getKey();
                        if(!Analyse(key))
                                System.err.println("[Warning] "+key.type+" "+key.name+" cannot be derived from START attribution, line "+key.line+", column:"+key.column);
                        for (Vector<Structure> i : value) {
                                for(Structure x:i)
                                {
                                        if(x.type!=Type.NONTERM)
                                                continue;
                                        else if(!SymbolTable.containsKey(x))
                                        {
                                                System.err.println("[Error] Found "+x.type+": " +x.name+" without production in line "+x.line+", coloumn "+x.column);
                                                fail=true;
                                        }
                                }
                        }
                }
                if(fail)
                        isValid=false;
        }

        private boolean Analyse(Structure key) {
                if(key.type!=Type.START)
                {
                        if(AcessableTokens.contains(key))
                                return true;
                        else
                                return false;
                }
                return true;
        }

  static final public SimpleNode Expression() throws ParseException {
                          /*@bgen(jjtree) Expression */
                          SimpleNode jjtn000 = new SimpleNode(JJTEXPRESSION);
                          boolean jjtc000 = true;
                          jjtree.openNodeScope(jjtn000);Token t;
    try {
      label_1:
      while (true) {
        StartAtribution();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case START:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
      }
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case NonTerm:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        Atribution();
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

  static final public void StartAtribution() throws ParseException {
                        /*@bgen(jjtree) StartAtribution */
                         SimpleNode jjtn000 = new SimpleNode(JJTSTARTATRIBUTION);
                         boolean jjtc000 = true;
                         jjtree.openNodeScope(jjtn000);Token lhs,rhs;
    try {
      lhs = jj_consume_token(START);
      jj_consume_token(ASSIGN);
                jjtn000.Symbol.name = new String(lhs.image);
                jjtn000.Symbol.line=lhs.beginLine;
                jjtn000.Symbol.column=lhs.beginColumn;
                jjtn000.Symbol.type=Type.START;
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case Term:
          rhs = jj_consume_token(Term);
                           jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.TERM,rhs.beginLine,rhs.beginColumn));
          break;
        case START:
        case NonTerm:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case NonTerm:
            rhs = jj_consume_token(NonTerm);
                                jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.NONTERM,rhs.beginLine,rhs.beginColumn));
            break;
          case START:
            rhs = jj_consume_token(START);
                              jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.START,rhs.beginLine,rhs.beginColumn));
            break;
          default:
            jj_la1[2] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
          jj_la1[3] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case START:
        case NonTerm:
        case Term:
          ;
          break;
        default:
          jj_la1[4] = jj_gen;
          break label_3;
        }
      }
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case OR:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_4;
        }
        jj_consume_token(OR);
         jjtn000.Variables.add(new Vector<Structure >());
        label_5:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case Term:
            rhs = jj_consume_token(Term);
                                   jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.TERM,rhs.beginLine,rhs.beginColumn));
            break;
          case START:
          case NonTerm:
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case NonTerm:
              rhs = jj_consume_token(NonTerm);
                                        jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.NONTERM,rhs.beginLine,rhs.beginColumn));
              break;
            case START:
              rhs = jj_consume_token(START);
                                      jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.START,rhs.beginLine,rhs.beginColumn));
              break;
            default:
              jj_la1[6] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
            break;
          default:
            jj_la1[7] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case START:
          case NonTerm:
          case Term:
            ;
            break;
          default:
            jj_la1[8] = jj_gen;
            break label_5;
          }
        }
      }
      jj_consume_token(ENDPROD);
    } finally {
          if (jjtc000) {
            jjtree.closeNodeScope(jjtn000, true);
          }
    }
  }

  static final public void Atribution() throws ParseException {
                   /*@bgen(jjtree) Atribution */
                   SimpleNode jjtn000 = new SimpleNode(JJTATRIBUTION);
                   boolean jjtc000 = true;
                   jjtree.openNodeScope(jjtn000);Token lhs,rhs;
    try {
      lhs = jj_consume_token(NonTerm);
      jj_consume_token(ASSIGN);
                jjtn000.Symbol.name = new String(lhs.image);
                jjtn000.Symbol.line=lhs.beginLine;
                jjtn000.Symbol.column=lhs.beginColumn;
                jjtn000.Symbol.type=Type.NONTERM;
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case Term:
          rhs = jj_consume_token(Term);
                           jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.TERM,rhs.beginLine,rhs.beginColumn));
          break;
        case START:
        case NonTerm:
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case NonTerm:
            rhs = jj_consume_token(NonTerm);
                                jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.NONTERM,rhs.beginLine,rhs.beginColumn));
            break;
          case START:
            rhs = jj_consume_token(START);
                              jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.START,rhs.beginLine,rhs.beginColumn));
            break;
          default:
            jj_la1[9] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          break;
        default:
          jj_la1[10] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case START:
        case NonTerm:
        case Term:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_6;
        }
      }
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case OR:
          ;
          break;
        default:
          jj_la1[12] = jj_gen;
          break label_7;
        }
        jj_consume_token(OR);
         jjtn000.Variables.add(new Vector<Structure >());
        label_8:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case Term:
            rhs = jj_consume_token(Term);
                                   jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.TERM,rhs.beginLine,rhs.beginColumn));
            break;
          case START:
          case NonTerm:
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case NonTerm:
              rhs = jj_consume_token(NonTerm);
                                        jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.NONTERM,rhs.beginLine,rhs.beginColumn));
              break;
            case START:
              rhs = jj_consume_token(START);
                                      jjtn000.Variables.get(jjtn000.Variables.size()-1).add(new Structure(rhs.image,Type.START,rhs.beginLine,rhs.beginColumn));
              break;
            default:
              jj_la1[13] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
            break;
          default:
            jj_la1[14] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case START:
          case NonTerm:
          case Term:
            ;
            break;
          default:
            jj_la1[15] = jj_gen;
            break label_8;
          }
        }
      }
      jj_consume_token(ENDPROD);
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
  static final private int[] jj_la1 = new int[16];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x20,0x800,0x820,0x1820,0x1820,0x200,0x820,0x1820,0x1820,0x820,0x1820,0x1820,0x200,0x820,0x1820,0x1820,};
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
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(CNFParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jjtree.reset();
    jj_gen = 0;
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
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
    boolean[] la1tokens = new boolean[13];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 16; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 13; i++) {
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

}
