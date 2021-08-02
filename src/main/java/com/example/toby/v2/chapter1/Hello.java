package com.example.toby.v2.chapter1;

public class Hello {
    String name;
    Printer printer;

    public void print(){
        printer.print(sayHello());
    }

    public String sayHello(){
        return "Hello " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Printer getPrinter() {
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
