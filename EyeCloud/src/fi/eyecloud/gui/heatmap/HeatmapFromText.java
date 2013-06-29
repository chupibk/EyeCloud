package fi.eyecloud.gui.heatmap;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.Intensity;
import fi.eyecloud.gui.lib.StringLabel;

public class HeatmapFromText {

	private JPanel Screen = new JPanel();
	
	public HeatmapFromText(String media, Intensity intensity){
		JFrame frame = new JFrame(StringLabel.HEATMAP_FRAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		Screen.setLayout(new BorderLayout());
		HeatmapPaint paint = new HeatmapPaint(media, intensity);
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
		long start = System.currentTimeMillis();
		HeatmapIntensity intensity = new HeatmapIntensity();
		intensity.run("data/17JuneResult.txt", 0, GuiConstants.MEDIA_WIDTH, GuiConstants.MEDIA_HEIGHT);
		System.out.println("Running time: " + (System.currentTimeMillis() - start));
		
		new HeatmapFromText("data/17JuneMedia.png", intensity);
	}

}
