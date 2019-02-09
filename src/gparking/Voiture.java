/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gparking;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Ayoub Marrakchi
 */
public class Voiture extends JLabel implements Runnable{

    private final int dx=2; //px
    private final int dy=1; //px
    private final int dt=15; //ms
    private Parking parking;
    private final String color = getColor();
    ImageIcon v1= new ImageIcon(Gparking.imgDir+color+"1.png");  //position horizontal
    ImageIcon v2 = new ImageIcon(Gparking.imgDir+color+"2.png"); //position incliné '\'
    ImageIcon v3 = new ImageIcon(Gparking.imgDir+color+"3.png"); //posision incliné '/'
    ImageIcon v4 = new ImageIcon(Gparking.imgDir+color+"4.png"); //posistion vertical

    public Voiture(Parking p) {
        super();
        this.parking=p;
        this.setIcon(v1);
        this.setBounds(-114 , 60, 114, 114);
    }
    private String getColor(){
        int i = Math.round((float) Math.random());  // 0 ou 1
        if(i==0){
            return "voiture1"; // coulour orange
        }
        return "voiture2"; //couleur bleu
    }
    private void entreP(){
        int x=this.getX();
        int y=this.getY();
        while (x<9) {            
            x=x+dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        this.setIcon(v2);
        while (x<130) {            
            x=x+dx;
            y=y+dy;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
    private void parque(int i){
        int x=this.getX();
        int y=this.getY();
        int[] pos= {240, 325, 405, 480, 565}; //pos par raport a x
        while (x<170) {            
            x=x+dx;
            y=y+dy;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        this.setIcon(v1);
        while (x<pos[i]) {            
            x=x+dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        this.setIcon(v4);
        while (y<255) {            
            y=y+dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
    private void sortieC(){
        int x=this.getX();
        int y=this.getY();
        while (y>145) {            
            y=y-dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        this.setIcon(v1);
        while (x<560) {            
            x=x+dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
    private void sortieP(){
        int x=this.getX();
        int y=this.getY();
        this.setIcon(v3);
        while (x<720) {            
            x=x+3;
            y=y-dy;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
        this.setIcon(v1);
        while (x<850) {            
            x=x+dx;
            this.setLocation(x, y);
            try {
                Thread.sleep(dt);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public void run() {
        try {
            parking.rampeE.acquire();
            entreP();
            parking.park.acquire();
            parking.coul.acquire();
            parking.rampeE.release();
            int p = parking.getFree();
            parking.reserver(p);
            this.parque(p);
            parking.coul.release();
            Thread.sleep(Math.round((float) (Math.random()*5000)+5000)); //interuption d'un temps aleatoire entre 5 et 10 sec
            parking.coul.acquire();
            parking.park.release();
            parking.relacher(p);
            this.sortieC();
            parking.coul.release();
            parking.rampeS.acquire();
            this.sortieP();
            parking.rampeS.release();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
