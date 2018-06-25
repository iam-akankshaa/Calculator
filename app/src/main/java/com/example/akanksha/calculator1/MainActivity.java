package com.example.akanksha.calculator1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {


    private static final String OPERATORS =  "*/+-";
    private static final HashMap<String,Integer> prec = new HashMap<>();

    String currentToken = null;
    String tokensString = "";
    String operator=null;
    boolean isLastTokenOperation = false;
    boolean isLastOperationEvaluated = false;

    ArrayList<String> tokens= new ArrayList<>();
    TextView stack;
    TextView resultV;
    TextView operatorV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prec.put("*",2);
        prec.put("/",2);
        prec.put("+",1);
        prec.put("-",1);
        stack = findViewById(R.id.stack);
        resultV = findViewById(R.id.result);
        operatorV=findViewById(R.id.operator);
    }


    public void click(View view){

        String text = ((Button)view).getText().toString();
        String tag = (String) view.getTag();
        if(isLastOperationEvaluated){ //if last operation is evaluated
            tokens.remove(tokens.size()-1);
        }
        if(tag.equals("point")){ //if current token is NULL then make it 0

            if(operator!=null)
            {
                tokensString =tokensString.concat(operator);
                stack.setText(tokensString);
                operator=null;
            }
            if(currentToken == null){
                currentToken = "0";
            }
            if(!currentToken.contains(".")){ //if it doesn't contain a point already
                currentToken = currentToken.concat(text);
                tokensString = tokensString.concat(text);
                isLastTokenOperation = false;
                isLastOperationEvaluated = false;
                stack.setText(tokensString);
            }

        }
        if(tag.equals("value")){ //if it is a value

            if(operator!=null)
            {
                tokensString =tokensString.concat(operator);
                stack.setText(tokensString);
                operator=null;
            }

            if(currentToken == null){
                currentToken = "";
            }
            operatorV.setText("");
            currentToken = currentToken.concat(text);
            tokensString = tokensString.concat(text);
            isLastOperationEvaluated = false;
            isLastTokenOperation = false;
            stack.setText(tokensString);

            eval();
        }
        if(tag.equals("equal")){
            show();

        }
        if(tag.equals("operation")){

            if(currentToken != null && !isLastTokenOperation  ){ //when last token was not operator

                tokens.add(currentToken); //add current token in list
                currentToken = "";
                isLastOperationEvaluated = false;
                isLastTokenOperation = true;
                tokens.add(text); //add operator in list
                operator=text;
                operatorV.setText(text);

            }
            else if(currentToken!=null && isLastTokenOperation){ //when last token was operator
                currentToken="";
                isLastTokenOperation=true;
                isLastOperationEvaluated = false;
                tokens.remove(tokens.size()-1);
                tokens.add(text); //change the operator
                operator=text;
                operatorV.setText(text);


            }
        }
        if(tag.equals("clear")){ //delete everything
            tokens.clear();
            tokensString = "";
            currentToken = null;
            operator=null;
            isLastTokenOperation = false;
            isLastOperationEvaluated = false;
            stack.setText("");
            resultV.setText("");
            resultV.setTextSize(30);
        }
    }

    public void eval(){
        if(tokens.size() >= 2 && !isLastTokenOperation){
            tokens.add(currentToken);
            Double result = evaluate(); //evaluate the result
            isLastOperationEvaluated = true;
            isLastTokenOperation = false;
            resultV.setText(result+"");
        }
    }
    public Double evaluate(){
        ArrayList<String> postfixTokens = infixToPostfix(tokens);
        Stack<String> stack = new Stack<>();
        for(String token: postfixTokens){
            if(OPERATORS.contains(token)){ //if it is an operator
                Double val2 = Double.parseDouble(stack.pop());
                Double val1 = Double.parseDouble(stack.pop());
                Double result = operate(token,val1,val2);
                stack.push(result+"");
            }
            else {
                stack.push(token);
            }
        }
        return Double.parseDouble(stack.pop());
    }

    private Double operate(String token, Double val1, Double val2) {
        switch (token){
            case "*": return val1 * val2;
            case "/": return val1/val2;
            case "+": return val1+ val2;
            case "-": return val1 -val2;
            default:return  Double.MIN_VALUE;
        }
    }

    public ArrayList<String> infixToPostfix(ArrayList<String> infix){
        ArrayList<String>postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for(String token: infix){
            if(OPERATORS.contains(token)){
                while (!stack.isEmpty()){
                    String topOperator = stack.peek();
                    if(prec.get(topOperator) >= prec.get(token)){
                        postfix.add(stack.pop());
                    }
                    else {
                        break;
                    }
                }
                stack.push(token);
            }
            else {
                postfix.add(token);
            }

        }
        while (!stack.isEmpty()){
            postfix.add(stack.pop());
        }
        return postfix;
    }

    public void show()
    {
        resultV.setTextSize(40); //increase the size of result
    }
}


