package fi.eyecloud.gui.fixation;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.input.ReadTextFile;

@SuppressWarnings("serial")
public class FixationPaint extends JPanel {
	private Image inputMedia;
	private Image scaledMedia;
	
	public FixationPaint(){
		try {
			inputMedia = ImageIO.read(new File("data/mediaPDF.png"));
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
        this.drawFixation(g);
    }
    
    private void drawPoint(Graphics g){
		ReadTextFile data = new ReadTextFile("data/testPDF.txt");
		while (data.readNextLine() != null) {
			int x = Integer.parseInt(data.getField(Constants.GazePointX))/GuiConstants.SCREEN_RATE;
			int y = Integer.parseInt(data.getField(Constants.GazePointY))/GuiConstants.SCREEN_RATE - GuiConstants.ERROR;
			
			g.fillOval(x, y, GuiConstants.DOT_SIZE, GuiConstants.DOT_SIZE);
		}
    }
    
    private void drawFixation(Graphics g){
		ReadTextFile data = new ReadTextFile("data/resultPDF.txt");
		while (data.readNextLine() != null) {
			float x = Float.parseFloat(data.getField(Constants.GazePointX))/GuiConstants.SCREEN_RATE;
			float y = Float.parseFloat(data.getField(Constants.GazePointY))/GuiConstants.SCREEN_RATE - GuiConstants.ERROR;
			int size = Integer.parseInt(data.getField(Constants.Duration))/GuiConstants.CIRCLE_SIZE_RATE;
			
			g.drawOval((int)x, (int)y, size, size);
		}    	
    }
}
