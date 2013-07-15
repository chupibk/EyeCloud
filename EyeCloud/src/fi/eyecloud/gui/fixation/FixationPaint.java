package fi.eyecloud.gui.fixation;

import java.awt.Color;
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
        this.drawFixation(g);
        //this.drawPoint(g);
    }
    
    public void drawPoint(Graphics g){
		ReadTextFile data = new ReadTextFile("data/17June.txt");
		int preX, preY;
		preX = preY = Constants.UNKNOWN;
		while (data.readNextLine() != null) {
			int x = Integer.parseInt(data.getField(Constants.GazePointX))/GuiConstants.SCREEN_RATE;
			int y = Integer.parseInt(data.getField(Constants.GazePointY))/GuiConstants.SCREEN_RATE - GuiConstants.ERROR_USED;
			int size  = GuiConstants.DOT_SIZE;
			
			if (x !=Constants.UNKNOWN && y!=Constants.UNKNOWN){
				int drawX = (int)x-size/2;
				int drawY = (int)y-size/2;
			
				g.drawOval(drawX, drawY, size, size);
				if (preX > Constants.UNKNOWN && preY != Constants.UNKNOWN){
					g.drawLine(preX, preY, drawX+size/2, drawY+size/2);
				}
				preX = drawX+size/2;
				preY = drawY+size/2;
			}
		}
    }
    
    public void drawFixation(Graphics g){
		ReadTextFile data = new ReadTextFile("data/17June_40");
		ReadTextFile offline = new ReadTextFile("data/17JuneResult.txt");
		
		int preX, preY;
		preX = preY = Constants.UNKNOWN;
		while (data.readNextLine() != null) {
			float x = Float.parseFloat(data.getField(Constants.GazePointX))/GuiConstants.SCREEN_RATE;
			float y = Float.parseFloat(data.getField(Constants.GazePointY))/GuiConstants.SCREEN_RATE - GuiConstants.ERROR_USED;
			int size = Integer.parseInt(data.getField(Constants.Duration))/GuiConstants.CIRCLE_SIZE_RATE;
			if (size < GuiConstants.SMALLEST_SIZE) size = GuiConstants.SMALLEST_SIZE;
			
			int drawX = (int)x-size/2;
			int drawY = (int)y-size/2;
			
			g.setColor(Color.BLUE);
			while (offline.readNextLine() != null){
				if (Integer.parseInt(data.getField(Constants.Timestamp)) == 
					Integer.parseInt(offline.getField(Constants.Timestamp)) &&
					Integer.parseInt(data.getField(Constants.Duration)) == 
					Integer.parseInt(offline.getField(Constants.Duration)) ){
					g.setColor(Color.RED);
					break;
				}
			}
			// Reset offline file result
			offline.resetFile();
			
			g.drawOval(drawX, drawY, size, size);
			if (preX > 0 && preY > 0 && x > 0 && y > 0){
				//g.drawLine(preX, preY, drawX+size/2, drawY+size/2);
			}
			preX = drawX+size/2;
			preY = drawY+size/2;
		}    	
    }
}
