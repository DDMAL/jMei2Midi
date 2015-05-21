/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestReflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dinamix
 */
public class RunReflect {
    public static void main(String[] args) throws IllegalAccessException, 
                                                  InstantiationException, 
                                                  ClassNotFoundException, 
                                                  NoSuchMethodException,
                                                  InvocationTargetException {
        Class<?> clazz = Class.forName("TestReflection.Apple");
        Constructor<?> constructor = clazz.getConstructor(String.class,double.class);
        Fruit insurance = (Fruit)constructor.newInstance("good",0.99);
        System.out.println(insurance.toString());
    }
}
