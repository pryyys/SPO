import LEXER.Lexer;
import LEXER.Token;

/**
 * Created by Андрюха on 11.10.2018.
 */
public class Main {

    public static void main(String [] args)
    {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        String str = "int a = 1; int b = 2; int c = 6; while(a!=c){a = a +1; b = b*2;}";
/*
        for (Token token : lexer.recognition(str))
        {
            System.out.println(token);
        }
*/

        /*for (Token token : parser.pars(lexer.recognition(str)))
        {
            System.out.println(token);
        }
        */

        System.out.println(parser.lang(lexer.recognition(str)));

        StackM stackM = new StackM(parser.lang(lexer.recognition(str)), parser.tableOfValues);
        stackM.start();
        System.out.println(stackM.tableOfValues);
    }

}
