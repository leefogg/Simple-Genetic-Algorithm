import java.io.*;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Main extends JPanel {
	
	private static final long serialVersionUID = -1444235245224332930L;
	static BufferedImage Image;
	static Genetic pixel[][];
	static int imagewidth, imageheight;
	static Robot robot;
	static short scale = 2, refreshSpeed = 500, Mutation = 5;
	
	public static void main(String args[]) {
		String path = "Mario.png";
		if (args.length != 0) {
			if (args[0].contains("/?")) {
				System.out.println("Syntax: \"Image name and extention\" [Scale] [Frame delay in ms] [Mutation in percent]");
				System.out.println("e.g \"Mario.png\" 4 1000 1");
				System.exit(1);
			} else {
				path = args[0];
			}
			if (args.length > 1) scale = Short.valueOf(args[1]);
			if (args.length > 2) refreshSpeed = Short.valueOf(args[2]);
			if (args.length > 3) Mutation = Short.valueOf(args[3]);
		}
		
		try {
			Image = ImageIO.read(Main.class.getResource(path));
		} catch (IOException e2) {
			System.out.println("Image not found in Jar.");
			System.exit(1);
		}
		
		// Setting the max length of both two dimensional arrays according to the image.
		imagewidth = Image.getWidth();
		imageheight = Image.getHeight();
		pixel = new Genetic[imagewidth][imageheight];
		
		// Go though every pixel on the image
		Random r = new Random();
		for(int x=0; x<imagewidth; x++) {
			for(int y=0; y<imageheight; y++) {
				pixel[x][y] = new Genetic(
							new Color(Image.getRGB(x,y)),
							new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255)),
							Mutation
							);
			}
		}
		JFrame frame = new JFrame();
		Main border = new Main();
		border.setFocusable(true);
		frame.setContentPane(border);
		frame.setTitle("Genetic Algerithm Demonstration");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int windowwidth, windowheight;
		windowwidth = (short) (imagewidth*(2*scale)+50);
		windowheight = (short) (imageheight*scale+70);
		frame.setSize(windowwidth, windowheight);
		frame.setVisible(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screen.width/2-windowwidth/2, screen.height/2-windowheight/2);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent e) {
	           System.exit(0);
	         }
	    });
		
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			System.err.println("Robot could not be created.");
			System.exit(1);
		}
	}
	
	public Main() { super(); }
	
	public void paintComponent(Graphics canvas) {
		super.paintComponent(canvas);
		
		canvas.setColor(Color.black);
		canvas.drawString("Template", 10, 15);
		canvas.drawImage(Image, 10, 20, imagewidth*scale, imageheight*scale, Color.white, this);
		for(int x=0; x<imagewidth; x++) {
			for(int y=0; y<imageheight; y++) {
				canvas.setColor(pixel[x][y].Child[0]);
				canvas.fillRect(imagewidth*scale+20+(x*scale), 20+(y*scale), scale, scale);
			}
		}
		
		canvas.setColor(Color.black);
		canvas.drawString("Generation: " + String.valueOf(pixel[0][0].Generation), imagewidth*scale+20, 15);
		for(int x=0; x<imagewidth; x++) {
			for(int y=0; y<imageheight; y++) {
				pixel[x][y].newGeneration();
				pixel[x][y].GenerateOffspring();
			}
		}
		
		if (refreshSpeed != 0) robot.delay(refreshSpeed);
		repaint();
	}
}