package com.soroush.ast;

import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Lexer l = new Lexer();
        StateMachine m = new StateMachine();
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String statement = input.nextLine();
            ArrayList<Pair<Lexer.Token, String>> tokens = l.tokenize(statement);
            for(Pair<Lexer.Token, String> p : tokens) {
                m.move(p);
            }
            if(m.isAccepted()) {
                System.out.println(m.getAST().toDotGraph(statement));
            } else {
                System.out.println("Syntax Error!");
            }
        }
    }
}
