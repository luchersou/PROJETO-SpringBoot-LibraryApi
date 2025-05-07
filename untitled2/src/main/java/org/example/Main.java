package org.example;

import java.util.Stack;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello, World!");

        Stack<String> lucas = new Stack<>();

        lucas.add("12");
        lucas.add("lucas");
        lucas.hashCode(lucas);

        System.out.println(lucas);
    }
}