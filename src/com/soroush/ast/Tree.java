package com.soroush.ast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Stack;

public class Tree {

    public Tree() {
        this.m_root = new Node();
        this.m_stack = new Stack<Node>();
    }

    // General Tree Manipulation
    private Node findOrCreateChild(Node parent, String label, String value) {
        // Find or create root node
        for (Node n : parent.children) {
            if (n.label.equals(label)) {
                return n;
            }
        }
        // Not found
        Node nodeOfInterest = new Node(label, value);
        parent.children.add(nodeOfInterest);
        return nodeOfInterest;
    }

    // Members for 'Insert Into'
    public void insert_init() {
        m_root = new Node("statement", "insert");
    }

    public void insert_set_table(String id) {
        Node insert = new Node("table", "table");
        Node table = new Node("table", id);
        insert.children.add(table);
        m_root.children.add(insert);
    }

    public void insert_add_column(String column) {
        // Find or create "columns" root node
        Node columnNode = findOrCreateChild(m_root, "columns", "columns");
        Node newNode = new Node("column", column);
        columnNode.children.add(newNode);
    }

    public void insert_add_value(String value) {
        // Find or create 'values' root node
        Node valuesNode = findOrCreateChild(m_root, "values", "values");
        Node valueNode = new Node("value", value);
        valuesNode.children.add(valueNode);
    }

    public void select_init() {
        m_root = new Node("statement", "select");
    }

    public void select_set_table(String id) {
        Node fromNode = new Node("from", "from");
        Node tableNode = new Node("table", id);
        fromNode.children.add(tableNode);
        m_root.children.add(fromNode);
    }

    public void select_add_column(String column) {
        Node columnsNode = findOrCreateChild(m_root, "columns", "columns");
        // TODO: Must be variable or identifier not value. Right?
        columnsNode.children.add(new Node("value", column));
    }

    public void select_add_where_clause() {
        findOrCreateChild(m_root, "where", "where");
    }

    public void push_stack(String label, String value) {
        m_stack.push(new Node(label, value));
    }

    //

    public void select_add_order_by(String field_name) {
        Node orderBy = findOrCreateChild(m_root, "order_by", "order_by");
        orderBy.children.add(new Node("id", field_name));
    }

    public void select_add_order_by_direction(String direction) {
        Node orderBy = findOrCreateChild(m_root, "order_by", "order_by");
        orderBy.children.add(new Node("direction", direction));
    }

    public void clause_to_postfix() {
        Node whereNode = findOrCreateChild(m_root, "where", "where");
        LinkedList<Node> outputs = new LinkedList<Node>();
        LinkedList<Node> operators = new LinkedList<Node>();
        while (!m_stack.empty()) {
            String label = m_stack.peek().label;
            if (label.equals("where_operator")) {
                operators.add(m_stack.pop());
            } else if (label.equals("where_field") ||
                    label.equals("where_value")) {
                outputs.add(m_stack.pop());
            }
        }
        while (!operators.isEmpty()) {
            Node op = operators.get(operators.size() - 1);
            operators.remove(operators.size() - 1);
            // operand #1
            // CAUTION: Pay attention to directions. Must be stack-like
            Node op1 = outputs.get(outputs.size() - 1);
            outputs.remove(outputs.size() - 1);
            // operand #2
            Node op2 = outputs.get(outputs.size() - 1);
            outputs.remove(outputs.size() - 1);
            // Add operands
            op.children.add(op1);
            op.children.add(op2);
            whereNode.children.add(op);
        }
    }

    //
    public void delete_init() {
        m_root = new Node("statement", "delete");
    }

    public void delete_set_table(String name) {
        Node delete_node = new Node("table", "table");
        Node table = new Node("table", name);
        delete_node.children.add(table);
        m_root.children.add(delete_node);
    }

    //
    public void use_init() {
        m_root = new Node("statement", "use");
    }

    public void use_set_database(String dbname) {
        Node use_node = new Node("database", dbname);
        m_root.children.add(use_node);
    }

    //
    public void save(String path, String legend) {
        try {
            FileOutputStream os = new FileOutputStream(path);
            os.write(toDotGraph(legend).getBytes());
            os.close();
        } catch (IOException ex) {
            System.out.println("Unable to open file...");
            System.out.println(ex.getMessage());
        }
    }

    private static void print_nodes(Node n, ByteArrayOutputStream o) {
        String n_label = n.value.replaceAll("\"", "\\\"");
        try {
            o.write(String.format("\t\t\"%s\" [label=\"\"]\n", n.uid, n_label).getBytes());
        } catch (IOException ex) {
            System.out.println("Unable to write into file...");
            System.out.println(ex.getMessage());
        }
        for(Node c : n.children) {
            print_nodes(c, o);
        }
    }

    void print_edges(Node n, ByteArrayOutputStream o) {
        for(Node c : n.children) {
            try {
                o.write(String.format("\t\t %s -> %s \n", n.uid, c.uid).getBytes());
            } catch (IOException ex) {
                System.out.println("Unable to write into file...");
                System.out.println(ex.getMessage());
            }
        }
        for(Node c : n.children) {
            print_edges(c, o);
        }
    }

    public String toDotGraph(String legend) {
        ByteArrayOutputStream str = new ByteArrayOutputStream();
        try {
            str.write("digraph G {\n".getBytes());
            if (!legend.isEmpty()) {
                String label = legend.replaceAll("\"", "\\\"");
                str.write(String.format("\tgraph [\n\t\tlabel = \"%s\"\n\t\tlabelloc = t\n\t]\n\tsubgraph {\n", label).getBytes());
            }
            print_nodes(m_root, str);
            print_edges(m_root, str);
            str.write("\t}\n".getBytes());
            if (!legend.isEmpty()) {
                str.write("}\n".getBytes());
            }

        } catch (IOException ex) {
            System.out.println("Unable to write into file...");
            System.out.println(ex.getMessage());
        }
        return str.toString();
    }

    public void reset() {
        m_root.children.clear();
        m_root.label = "";
        m_root.value = "";
    }

    private Node m_root;
    private Stack<Node> m_stack;
}
