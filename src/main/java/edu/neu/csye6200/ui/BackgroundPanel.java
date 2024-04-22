package edu.neu.csye6200.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel{
	
	private Image backgroundImage;

    public BackgroundPanel(String fileName) {
        // Load the background image
        backgroundImage = new ImageIcon(fileName).getImage();
    }
    
    public BackgroundPanel(URL imageUrl) {
        // Load the background image from URL
        backgroundImage = new ImageIcon(imageUrl).getImage();
    }
    
    public BackgroundPanel() {
        // Optionally set a default background color or leave it blank
        setBackground(Color.WHITE); // or any other default color
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image.
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

}
