package LEXER;

/**
 * Created by Андрюха on 11.10.2018.
 */
public class Token {
    private Lexeme lexeme;
    private String value;

    public Token(Lexeme lexeme, String value)
    {
        this.lexeme = lexeme;
        this.value = value;
    }

    public Lexeme getLexeme()
    {
        return this.lexeme;
    }

    public String getValue()
    {
        return this.value;
    }

    @Override
    public String toString()
    {
        return "Токен: " +
                  this.lexeme + ", \""
                  + this.value + "\"";
    }

}
