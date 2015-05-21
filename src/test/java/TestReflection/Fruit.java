/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestReflection;

/**
 *
 * @author dinamix
 */
public class Fruit {
    private final String name;
    private final String color;
    
    protected Fruit(String name, String color) {
        this.name = name;
        this.color = color;
    }
    
    @Override
    public String toString() {
        return "Name:" + name + " Color:" + color;
    }
}
