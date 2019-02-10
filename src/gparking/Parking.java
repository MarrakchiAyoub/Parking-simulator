/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gparking;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Ayoub Marrakchi
 */
public class Parking extends JFrame{

    private final JPanel panel = new ImagePanel(this);
    private boolean[] places={true, true, true, true, true};
    Semaphore rampeE = new Semaphore(1);
    Semaphore rampeS = new Semaphore(1);
    Semaphore coul = new Semaphore(1);
    Semaphore park = new Semaphore(5);
    
    public Parking() {
        initWindow();
    }
    private void initWindow(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Simulateur de Parking");
        this.setLocation(200, 120);
        this.setSize(846, 458);
        this.setResizable(false);
        this.setContentPane(panel);
        try {
            Image i = ImageIO.read(new File(Gparking.imgDir,"icon.png"));
            setIconImage(i);
        } catch (Exception e) {
            System.err.println(e);
        }
        this.setVisible(true);
    }
    public void demarer(){
        Voiture v = new Voiture(this);
        Thread t = new Thread(v);
        this.panel.add(v);
        t.start();
    }
    public int getFree(){ 
        for(int i=0; i<5; i++){
            if (this.places[i]){
                return i; // premiere place libre
            }
        }
        return -1;
    }
    public void reserver(int i){
        places[i]=false;
    }
    public void relacher(int i){
        places[i]=true;
    }
}

class ImagePanel extends JPanel{
    private Image image;
    private Parking parking;
    private JTextField t ;
    private JButton b ;
    private JLabel message = new JLabel();
    public ImagePanel(Parking p) {
        super();
        parking=p;
        try {
            image = ImageIO.read(new File(Gparking.imgDir,"parking.png"));
        } catch (IOException e) {
            System.err.println(e);
        }
        this.setLayout(null);
        this.initComponents();
    }
    private void initComponents(){
        JLabel l = new JLabel("Nombre de voitures :");
        l.setBounds(275, 388, 150, 20);
        message.setBounds(335, 412, 200, 20);
        message.setForeground(Color.red);
        b = new JButton("démarrer");
        b.setBounds(465, 385, 100, 30);
        t = new JTextField();
        t.setBounds(400, 385, 50, 30);
        t.setBackground(new Color(166, 164, 249));
        t.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        t.setFont(t.getFont().deriveFont(20f));
        t.setText("10");
        b.addActionListener((ActionEvent e) -> {
            message.setText("");
            int k=0;
            try {
                k = Integer.decode(t.getText());
            } catch (NumberFormatException ex) {
                message.setText("Veulliez entrer un numero entier!");
            }
            for(int i=0; i<k; i++){
                parking.demarer();
            }
        });
        this.add(message);
        this.add(l);
        this.add(b);
        this.add(t);
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}