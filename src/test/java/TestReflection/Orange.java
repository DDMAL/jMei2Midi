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
public class Orange extends Fruit {
    private final String taste;
    private final double cost;
    
    public Orange(String taste, double cost) {
        super("Orange","Orange");
        this.taste = taste;
        this.cost = cost;
    }
    
    public String getTaste() {
        return taste;
    }
    
    public double cost() {
        return cost;
    }
    
    @Override
    public String toString() {
        return super.toString() + " Costs:" + cost + " Taste:" + taste;
    }
}
