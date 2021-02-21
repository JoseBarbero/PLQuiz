/* Generated By:JavaCC: Do not edit this line. ExpresionRegularParser.java */
package es.ubu.inf.tfg.regex.parser;
import es.ubu.inf.tfg.regex.datos.ExpresionRegular;

public class ExpresionRegularParser implements ExpresionRegularParserConstants {
        private int posicion;

        /*	 * <expresion> :- <operacion> <eof>	 * <operacion> :- <termino> ('|' <termino>)*	 * <termino> :- <unario> ('.' <unario>)*	 * <unario> :- <factor> '*'*	 * <factor> :- <simbolo> | <epsilon> | '(' <operacion> ')'	 */
  final public ExpresionRegular expresion() throws ParseException {
        ExpresionRegular expreg;
                posicion = 1;
    expreg = operacion();
    jj_consume_token(END);
                // Aumentamos la expresi�n regular
                {if (true) return ExpresionRegular.nodoConcat(expreg, ExpresionRegular.nodoAumentado(posicion));}
    throw new Error("Missing return statement in function");
  }

  final public ExpresionRegular operacion() throws ParseException {
        ExpresionRegular expregIzda, expregDcha;
    expregIzda = termino();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case UNION:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      jj_consume_token(UNION);
      expregDcha = termino();
                expregIzda = ExpresionRegular.nodoUnion(expregIzda, expregDcha);
    }
        {if (true) return expregIzda;}
    throw new Error("Missing return statement in function");
  }

  final public ExpresionRegular termino() throws ParseException {
        ExpresionRegular expregIzda, expregDcha;
    expregIzda = unario();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CONCAT:
      case OPEN_PAREN:
      case SYMBOL:
      case EPSILON:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CONCAT:
        jj_consume_token(CONCAT);
        break;
      default:
        jj_la1[2] = jj_gen;
        ;
      }
      // Para hacer el operador concatenaci�n opcional
                      expregDcha = unario();
                        expregIzda = ExpresionRegular.nodoConcat(expregIzda, expregDcha);
    }
        {if (true) return expregIzda;}
    throw new Error("Missing return statement in function");
  }

  final public ExpresionRegular unario() throws ParseException {
        ExpresionRegular expreg;
    expreg = factor();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case KLEENE:
      label_3:
      while (true) {
        jj_consume_token(KLEENE);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case KLEENE:
          ;
          break;
        default:
          jj_la1[3] = jj_gen;
          break label_3;
        }
      }
                        expreg = ExpresionRegular.nodoCierre(expreg);
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
        {if (true) return expreg;}
    throw new Error("Missing return statement in function");
  }

  final public ExpresionRegular factor() throws ParseException {
        Token token;
        ExpresionRegular expreg;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SYMBOL:
      token = jj_consume_token(SYMBOL);
        {if (true) return ExpresionRegular.nodoSimbolo(posicion++, token.image.charAt(0));}
      break;
    case EPSILON:
      jj_consume_token(EPSILON);
        {if (true) return ExpresionRegular.nodoVacio();}
      break;
    case OPEN_PAREN:
      jj_consume_token(OPEN_PAREN);
      expreg = operacion();
      jj_consume_token(CLOSE_PAREN);
                {if (true) return expreg;}
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public ExpresionRegularParserTokenManager token_source;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[6];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x40,0x6a0,0x20,0x10,0x10,0x680,};
   }

  /** Constructor with user supplied CharStream. */
  public ExpresionRegularParser(CharStream stream) {
    token_source = new ExpresionRegularParserTokenManager(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(CharStream stream) {
    token_source.ReInit(stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public ExpresionRegularParser(ExpresionRegularParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ExpresionRegularParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 6; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
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
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[12];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 6; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 12; i++) {
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
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
