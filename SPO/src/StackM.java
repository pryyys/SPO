import LEXER.Lexeme;
import LEXER.Lexer;
import LEXER.Token;

import java.util.*;

/**
 * Created by Андрюха on 26.10.2018.
 */
public class StackM {

    Map<String, Integer> tableOfValues = new HashMap<>();
    Stack<String> stack = new Stack<>();
    List<Token> poliz;

    public StackM(List<Token> poliz, Map<String, Integer> tableOfValues)
    {
        this.poliz = poliz;
        this.tableOfValues = tableOfValues;
    }

    private int getValue(String str)
    {
        if (tableOfValues.containsKey(str))
        {
            return tableOfValues.get(str);
        }
        else
        {
            return Integer.parseInt(str);
        }
    }

    public void start()
    {
        int a, b, res;
        int a1, b1;
        boolean lRes;
        for (int i = 0; i < this.poliz.size(); i++)
        {
            switch(poliz.get(i).getValue())
            {
                case "+":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    res = a + b;
                    stack.push(String.valueOf(res));
                    break;
                case "-":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    res = b - a;
                    stack.push(String.valueOf(res));
                    break;
                case "*":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    res = a * b;
                    stack.push(String.valueOf(res));
                    break;
                case "/":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    res = b/a;
                    stack.push(String.valueOf(res));
                    break;
                case "%":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    res = b%a;
                    stack.push(String.valueOf(res));
                    break;
                case "^":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    res =(int) Math.pow(b, a);
                    stack.push(String.valueOf(res));
                    break;
                case "=":
                    a = getValue(stack.pop());
                    tableOfValues.put(stack.pop(), a);
                    break;
                case "<":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    lRes = b < a;
                    stack.push(String.valueOf(lRes));
                    break;
                case ">":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    lRes = b > a;
                    stack.push(String.valueOf(lRes));
                    break;
                case "<=":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    lRes = b <= a;
                    stack.push(String.valueOf(lRes));
                    break;
                case ">=":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    lRes = b >= a;
                    stack.push(String.valueOf(lRes));
                    break;
                case "!=":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    lRes = b != a;
                    stack.push(String.valueOf(lRes));
                    break;
                case "==":
                    a = getValue(stack.pop());
                    b = getValue(stack.pop());
                    lRes = b == a;
                    stack.push(String.valueOf(lRes));
                    break;
                case "while":
                    a1 = i;
                    if (stack.pop().equals("true"))
                    {
                    }
                    else
                    {
                        while(i < poliz.size() && poliz.get(i).getValue() != "}")
                        {
                            i++;
                        }
                    }
                    break;
                case "}":
                    while (i > 3)
                    {
                        i--;
                        if (poliz.get(i).getLexeme() == Lexeme.WHILE)
                        {
                            i = i - 4;
                            break;
                        }
                    }
                    break;

                default:
                    stack.push(poliz.get(i).getValue());
                    break;
            }
            System.out.println(tableOfValues);
        }
    }




}
