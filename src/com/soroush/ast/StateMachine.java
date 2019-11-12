package com.soroush.ast;

public class StateMachine {
    private enum State {
        initial,
        // USE
        u1,
        u2,
        // DELETE,
        r1,
        r2,
        r3,
        r4,
        r5,
        r6,
        r7,
        // INSERT INTO ...
        q1,
        q2,
        q3,
        q4,
        q5,
        q6,
        q7,
        q8,
        q9,
        q10,
        q11,
        // SELECT FROM ...
        p1,
        p2,
        p3,
        p4,
        p5,
        p6,
        p7,
        p8,
        p9,
        p10,
        p11,
        accept, // because `final' is a keyword
    }

    public StateMachine() {
        this.m_current = State.initial;
        this.m_ast = new Tree();
    }

    public void move(Pair<Lexer.Token, String> input) {
        Lexer.Token t = input.getFirst();
        String v = input.getSecond();
        switch (m_current) {
            case initial:
                switch (t) {
                    case insert_into:
                        m_ast.insert_init();
                        m_current = State.q1;
                        break;
                    case select:
                        m_ast.select_init();
                        m_current = State.p1;
                        break;
                    case delete_:
                        m_ast.delete_init();
                        m_current = State.r1;
                        break;
                    case use:
                        m_ast.use_init();
                        m_current = State.u1;
                        break;
                    default:
                        break;
                }
                break;
            case q1:
                switch (t) {
                    case identifier:
                        m_ast.insert_set_table(v);
                        m_current = State.q2;
                        break;
                }
                break;
            case q2:
                switch (t) {
                    case left_paren:
                        m_current = State.q3;
                        break;
                }
                break;
            case q3:
                switch (t) {
                    case identifier:
                        m_ast.insert_add_column(v);
                        m_current = State.q4;
                        break;
                }
                break;
            case q4:
                switch (t) {
                    case comma:
                        m_current = State.q3;
                        break;
                    case right_paren:
                        m_current = State.q5;
                        break;
                }
                break;
            case q5:
                switch (t) {
                    case values:
                        m_current = State.q6;
                        break;
                }
                break;
            case q6:
                switch (t) {
                    case left_paren:
                        m_current = State.q7;
                        break;
                }
            case q7:
                switch (t) {
                    case value:
                        m_ast.insert_add_value(v);
                        m_current = State.q10;
                        break;
                    case identifier:
                        m_ast.insert_add_value(v + "()");
                        m_current = State.q8;
                        break;
                }
                break;
            case q8:
                switch (t) {
                    case left_paren:
                        m_current = State.q9;
                        break;
                }
                break;
            case q9:
                switch (t) {
                    case right_paren:
                        m_current = State.q10;
                        break;
                }
                break;
            case q10:
                switch (t) {
                    case right_paren:
                        m_current = State.q11;
                        break;
                    case comma:
                        m_current = State.q7;
                        break;
                }
                break;
            case q11:
                switch (t) {
                    case semicolon:
                        m_current = State.accept;
                        break;
                }
                break;
            case p1:
                switch (t) {
                    case identifier:
                        m_ast.insert_add_column(v);
                        m_current = State.p2;
                        break;
                }
                break;
            case p2:
                switch (t) {
                    case comma:
                        m_current = State.p1;
                        break;
                    case from:
                        m_current = State.p3;
                        break;
                }
                break;
            case p3:
                switch (t) {
                    case identifier:
                        m_ast.select_set_table(v);
                        m_current = State.p4;
                        break;
                }
                break;
            case p4:
                switch (t) {
                    case where:
                        m_ast.select_add_where_clause();
                        m_current = State.p5;
                        break;
                }
                // Bug?
                // else if(t == Lexer.Token.from) {
                //     m_current = State.p3;
                // }
                break;
            case p5:
                switch (t) {
                    case identifier:
                        m_ast.push_stack("where_field", v);
                        m_current = State.p6;
                        break;
                }
                break;
            case p6:
                switch (t) {
                    case is:
                    case is_not:
                    case greater:
                    case lesser:
                    case equal:
                        m_ast.push_stack("where_operator", v);
                        m_current = State.p7;
                        break;
                    default:
                        break;
                }
                break;
            case p7:
                switch (t) {
                    case value:
                        m_ast.push_stack("where_value", v);
                        m_ast.clause_to_postfix();
                        m_current = State.p8;
                        break;
                }
                break;
            case p8:
                switch (t) {
                    case semicolon:
                        m_current = State.accept;
                        break;
                    case order_by:
                        // m_ast.select_add_order_by_statement();
                        m_current = State.p9;
                        break;
                    default:
                        break;
                }
                break;
            case p9:
                switch (t) {
                    case identifier:
                        m_ast.select_add_order_by(v);
                        m_current = State.p10;
                        break;
                }
                break;
            case p10:
                switch (t) {
                    case semicolon:
                        m_current = State.accept;
                        break;
                    case desc:
                    case asc:
                        m_ast.select_add_order_by_direction(v);
                        m_current = State.p11;
                        break;
                    default:
                        break;
                }
                break;
            case p11:
                switch (t) {
                    case semicolon:
                        m_current = State.accept;
                        break;
                }
                break;
            case u1:
                switch (t) {
                    case identifier:
                        m_ast.use_set_database(v);
                        m_current = State.u2;
                        break;
                }
                break;
            case u2:
                switch (t) {
                    case semicolon:
                        m_current = State.accept;
                        break;
                }
                break;
            case r1:
                switch (t) {
                    case from:
                        m_current = State.r2;
                        break;
                }
                break;
            case r2:
                switch (t) {
                    case identifier:
                        m_ast.delete_set_table(v);
                        m_current = State.r3;
                        break;
                }
                break;
            case r3:
                switch (t) {
                    case where:
                        m_ast.select_add_where_clause();
                        m_current = State.r4;
                        break;
                }
                break;
            case r4:
                switch (t) {
                    case identifier:
                        m_ast.push_stack("where_field", v);
                        m_current = State.r5;
                        break;
                }
                break;
            case r5:
                switch (t) {
                    case is:
                    case is_not:
                    case greater:
                    case lesser:
                    case equal:
                        m_ast.push_stack("where_operator", v);
                        m_current = State.r6;
                        break;
                }
                break;
            case r6:
                switch (t) {
                    case value:
                    case identifier:
                        m_ast.push_stack("where_value", v);
                        m_ast.clause_to_postfix();
                        m_current = State.r7;
                        break;
                }
                break;
            case r7:
                switch (t) {
                    case semicolon:
                        m_current = State.accept;
                        break;
                }
                break;
            case accept:
            default:
                break;
        }
    }

    public Boolean isAccepted() {
        return m_current == State.accept;
    }

    public Tree getAST() {
        return m_ast;
    }

    public void reset() {
        m_current = State.initial;
        m_ast.reset();
    }

    private State m_current;
    private Tree m_ast;
}
