package LEXER;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by Андрюха on 11.10.2018.
 */
public class Lexer {

    private int position = 0;
    String tempStr = "";
    Lexeme tempLexeme;
    boolean waitFlag = true;

    private String format(String str) {
        return str.substring(0, str.length() - 1);
    }

    private boolean find()
    {
        for (Lexeme lexeme : Lexeme.values())
        {
            Matcher matcher = lexeme.getPattern().matcher(tempStr);
            if (matcher.matches())
            {
                tempLexeme = lexeme;
                return true;
            }
        }
        return false;
    }

    public List<Token> recognition(String str)
    {
        List<Token> tokens = new ArrayList<>();

        if (str.length() != 0)
        {
            while (position < str.length())
            {
                tempStr += str.charAt(position++);
                boolean findFlag = find();
                if (!findFlag)
                {
                    if (!waitFlag)
                    {
                        waitFlag =true;
                        tokens.add(new Token(tempLexeme, format(tempStr)));
                        tempStr = "";
                        position --;
                    }
                    else
                    {
                        waitFlag = true;
                        System.err.println("Ошибка: набор символов не распознан!");
                        System.exit(2);
                    }
                }
                else
                {
                    waitFlag = false;
                }
            }
            tokens.add(new Token(tempLexeme, tempStr));
        }
        else
        {
            System.err.println("Ошибка: Введена пустая строка!");
            System.exit(1);
        }
        return tokens;
    }

}
