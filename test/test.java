package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author cdi313
 */
public class test {
    public static void main(String[] args) {
        float t = 10.29866f;
      
        System.out.println(t);
        float f = ((float)((int)(t*100))) / 100;
        System.out.println(f);
        System.out.println(Math.floor(t));
    }
}
