package fi.eyecloud.gui.heatmap;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;

@SuppressWarnings("serial")
public class HeatmapPaint extends JPanel{
	private Image inputMedia;
	private Image scaledMedia;
	
	public HeatmapPaint(){
		try {
			inputMedia = ImageIO.read(new File("data/17JuneMedia.png"));
			scaledMedia =inputMedia.getScaledInstance(GuiConstants.MEDIA_WIDTH, GuiConstants.MEDIA_HEIGHT, 5);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(scaledMedia, 0, 0, null);
        
        HeatmapIntensity intensity = new HeatmapIntensity("data/heatmap.txt", 0, 
        								Constants.RESOLUTION_WIDTH, Constants.RESOLUTION_HEIGHT);
        Colorization color = new Colorization(intensity.getIntensity(), 
        								Constants.RESOLUTION_WIDTH, Constants.RESOLUTION_HEIGHT);
        g.drawImage(color.getImage(), 0, 0, null);
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}    
}
