package fi.eyecloud.gui.heatmap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.Intensity;

@SuppressWarnings("serial")
public class HeatmapPaint extends JPanel{
	private Image inputMedia;
	private Image scaledMedia;
	private BufferedImage heatmap;
	
	public HeatmapPaint(String media, Intensity intensity){
		try {
			inputMedia = ImageIO.read(new File(media));
			scaledMedia =inputMedia.getScaledInstance(GuiConstants.MEDIA_WIDTH, GuiConstants.MEDIA_HEIGHT, 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        long start = System.currentTimeMillis();
        Colorization color = new Colorization(intensity.getIntensity(), 
				GuiConstants.MEDIA_WIDTH, GuiConstants.MEDIA_HEIGHT);
        heatmap = color.getImage();
        System.out.println("Colorizing time: " + (System.currentTimeMillis() - start));
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(scaledMedia, 0, 0, null);
        g.drawImage(heatmap, 0, 0, null);
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}    
}
