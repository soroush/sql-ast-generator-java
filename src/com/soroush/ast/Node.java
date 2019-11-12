package com.soroush.ast;

import java.util.ArrayList;

public class Node {
    public Node() {
        this.children = new ArrayList<Node>();
        this.uid = nextUID();
    }

    public Node(String label, String value) {
        this();
        this.label = label;
        this.value = value;
    }

    @Override
    public String toString() {
        return uid;
    }

    public String label;
    public String value;
    public String uid;
    public ArrayList<Node> children;

    private static int max_uid = 1;
    private String nextUID() {
        return String.format("node_%d", ++max_uid);
    }

}
