/* Generated By:JavaCC: Do not edit this line. ExpresionRegularParserConstants.java */
package es.ubu.inf.tfg.regex.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ExpresionRegularParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int KLEENE = 4;
  /** RegularExpression Id. */
  int CONCAT = 5;
  /** RegularExpression Id. */
  int UNION = 6;
  /** RegularExpression Id. */
  int OPEN_PAREN = 7;
  /** RegularExpression Id. */
  int CLOSE_PAREN = 8;
  /** RegularExpression Id. */
  int SYMBOL = 9;
  /** RegularExpression Id. */
  int EPSILON = 10;
  /** RegularExpression Id. */
  int END = 11;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"*\"",
    "<CONCAT>",
    "\"|\"",
    "\"(\"",
    "\")\"",
    "<SYMBOL>",
    "<EPSILON>",
    "\"\\n\"",
  };

}
