package fi.eyecloud.gui.heatmap;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.StringLabel;

public class HeatmapFromText {

	private JPanel Screen = new JPanel();
	
	public HeatmapFromText(){
		JFrame frame = new JFrame(StringLabel.HEATMAP_FRAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		Screen.setLayout(new BorderLayout());
		HeatmapPaint paint = new HeatmapPaint("data/17JuneMedia.png", "data/17JuneResult.txt");
		Screen.add(paint);
		
		frame.add(Screen);
		
		frame.setSize(GuiConstants.SCREEN_WIDTH, GuiConstants.SCREEN_HEIGHT);
		frame.setVisible(true);
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HeatmapFromText();
	}

}
