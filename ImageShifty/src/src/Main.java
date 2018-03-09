package src;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;




public class Main extends JPanel{
	static FastRGB fRGB;
	static BufferedImage vImg;
	static int w;
	static int h;
	static GraphicsConfiguration gc = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();
	public static void main(String[] args){
		FileDialog fd = new FileDialog((java.awt.Frame) null);
		fd.setTitle("Choose an image");
		fd.setVisible(true);
		File file = new File(fd.getDirectory() + fd.getFile());
		if(fd.getDirectory() == null || fd.getFile() == null)
			System.exit(0);
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
			img.getType();
		} catch (IOException | NullPointerException e) {}
		JFrame f = new JFrame();
		Main m = new Main();
		f.add(m);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(img.getWidth(), img.getHeight());
		f.setVisible(true);
		fRGB = new FastRGB(img);
		w = img.getWidth();
		h = img.getHeight();

		vImg = gc.createCompatibleImage(w, h);
		update();
		while(true){
			f.repaint();
			try {
				Thread.sleep(3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void update(){
		tick++;
		vImg = gc.createCompatibleImage(w, h);
		Graphics gr2 = vImg.createGraphics();
		int sum = 0;
		int i = 0;
		for(int x = 0; x < w;x++){
			for(int y = 0; y < h;y++){
				i++;
				int av = getAverage(x, y);
				sum += av;
			}
		}
		sum = (int)((double)sum / (double)i);
		int tr = (sum >> 16) & 0xFF;
		int tg = (sum >> 8) & 0xFF;
		int tb = (sum ) & 0xFF;
		for(int x = 0; x < w;x++){
			for(int y = 0; y < h;y++){
				int tRGB = fRGB.getRGB(x, y);
				int r = (tRGB >> 16) & 0xFF;
				int g = (tRGB >> 8) & 0xFF;
				int b = (tRGB) & 0xFF;
				int r2 = Math.abs(r - tr);
				int g2 = Math.abs(g - tg);
				int b2 = Math.abs(b - tb);
				System.out.println(r2 + " " + g2 + " " + b2);
				gr2.setColor(new Color(r2,g2,b2));
				gr2.fillRect(x, y, 0, 0);
			}
		}
	}
	public static int getAverage(int x, int y){
		int i = 0;
		int sumR = 0;
		int sumG = 0;
		int sumB = 0;
		for(int xi = -1; xi < 2;xi++){
			for(int yi = -1; yi < 2;yi++){
				if(xi == 0 && yi == 0){
					continue;
				}
				int xc = xi + x;
				int yc = yi + y;
				if(xc < w && yc < h && xc >= 0 && yc >= 0){
					i++;
					int tRGB = 0;
					tRGB = fRGB.getRGB(xc, yc);
					int tr = (tRGB >> 16) & 0xFF;
					int tg = (tRGB >> 8) & 0xFF;
					int tb = (tRGB) & 0xFF;
					sumR += tr;
					sumG += tg;
					sumB += tb;

				}
			}
		}
		sumR = (int)((double)sumR / (double)i);
		sumG = (int)((double)sumR / (double)i);
		sumB = (int)((double)sumR / (double)i);
		return sumR << 16 | sumG << 8| sumB;
	}
	static int tick = 0;
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(vImg, 0, 0, null);
	}

}
