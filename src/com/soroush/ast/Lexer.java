package com.soroush.ast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public enum Token {
        insert_into,    // ^\s*INSERT\s+INTO\s*
        select,         // ^\s*SELECT\s*
        from,           // ^\s*FROM\s*
        where,          // ^\s*WHERE\s*
        delete_,        // ^\s*DELETE\s*
        use,            // ^\s*USE\s*
        is_not,         // ^\s*IS\s*NOT\s*
        is,             // ^\s*IS\s*
        greater,        // ^\s*>\s*
        lesser,         // ^\s*<\s*
        equal,          // ^\s*=\s*
        order_by,       // ^\s*ORDER\s*BY\s*
        desc,           // ^\s*DESC\s*
        asc,            // ^\s*ASC\s*
        identifier,     // ^\s*[[:alpha:]_][[:alnum:]_]*\s*
        left_paren,     // ^\s*\(\s*
        right_paren,    // ^\s*\)\s*
        comma,          // ^\s*,\s*
        values,         // ^\s*VALUES\s*
        value,          // ^\s*([[:alpha:]_][[:alnum:]_]*(\(\))|[[:digit:]]+|\".*\")\s*
        semicolon,      // ^\s*;\s*
        invalid,
    }

    public Lexer() {
        acceptors = new ArrayList<Pair<Token, Pattern>>();
        acceptors.add(new Pair<Token, Pattern>(Token.insert_into,
                Pattern.compile("^\\s*INSERT\\s+INTO\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.values,
                Pattern.compile("^\\s*VALUES\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.select,
                Pattern.compile("^\\s*SELECT\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.from,
                Pattern.compile("^\\s*FROM\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.where,
                Pattern.compile("^\\s*WHERE\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.delete_,
                Pattern.compile("^\\s*DELETE\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.use,
                Pattern.compile("^\\s*USE\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.is_not,
                Pattern.compile("^\\s*IS\\s*NOT\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.is,
                Pattern.compile("^\\s*IS\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.order_by,
                Pattern.compile("^\\s*ORDER\\s*BY\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.asc,
                Pattern.compile("^\\s*ASC\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.desc,
                Pattern.compile("^\\s*DESC\\s+", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.greater,
                Pattern.compile("^\\s*>\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.lesser,
                Pattern.compile("^\\s*<\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.equal,
                Pattern.compile("^\\s*=\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.left_paren,
                Pattern.compile("^\\s*\\(\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.right_paren,
                Pattern.compile("^\\s*\\)\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.comma,
                Pattern.compile("^\\s*,\\s*", Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.value,
                Pattern.compile("^\\s*([a-zA-Z_][a-zA-Z0-9_]*(\\(\\))|[0-9]+|\\\".*\\\"|NULL)\\s*",
                        Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.identifier,
                Pattern.compile("^\\s*[a-zA-Z_][a-zA-Z0-9_]*(\\.[[a-zA-Z_][a-zA-Z0-9_]]+)?\\s*",
                        Pattern.CASE_INSENSITIVE)));
        acceptors.add(new Pair<Token, Pattern>(Token.semicolon,
                Pattern.compile("^\\s*;\\s*", Pattern.CASE_INSENSITIVE)));

    }

    public ArrayList<Pair<Token, String>> tokenize(String input) {
        ArrayList<Pair<Token, String>> result = new ArrayList<Pair<Token, String>>();
        Boolean found = false;
        do {
            found = false;
            for (Pair<Token, Pattern> item : acceptors) {
                Token t = item.getFirst();
                Pattern p = item.getSecond();
                Matcher m = p.matcher(input);
                if (m.find()) {
                    result.add(new Pair<Token, String>(t, m.group()));
                    input = input.replaceAll(p.toString(), "");
                    found = true;
                    break;
                }
            }
        } while (found);
        return result;
    }
//
//    public Pair<Token, String> nextToken(String input) {
//    }

    private ArrayList<Pair<Token, Pattern>> acceptors;
}
