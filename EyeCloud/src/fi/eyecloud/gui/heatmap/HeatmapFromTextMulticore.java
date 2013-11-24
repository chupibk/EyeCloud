package fi.eyecloud.gui.heatmap;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.StringLabel;

public class HeatmapFromTextMulticore {

	private JPanel Screen = new JPanel();
	
	public HeatmapFromTextMulticore(String media, BufferedImage heatmap){
		JFrame frame = new JFrame(StringLabel.HEATMAP_FRAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		Screen.setLayout(new BorderLayout());
		HeatmapPaint paint = new HeatmapPaint(media, heatmap);
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
		//HeatmapIntensity intensity = new HeatmapIntensity();
		HeatmapMulticore intensity = new HeatmapMulticore();
		if (args.length == 0)
			intensity.run("data/heatmap/100000.txt", 1, GuiConstants.MEDIA_WIDTH, GuiConstants.MEDIA_HEIGHT);
		else
			intensity.run(args[0], 1, GuiConstants.MEDIA_WIDTH, GuiConstants.MEDIA_HEIGHT);
		long start1 = System.currentTimeMillis();
		Colorization color = new Colorization(intensity.getIntensity(), 
				GuiConstants.MEDIA_WIDTH, GuiConstants.MEDIA_HEIGHT);
		System.out.println("Colorization time: " + (System.currentTimeMillis() - start1));
		System.out.println("Running time: " + (System.currentTimeMillis() - start));
		new HeatmapFromTextMulticore("data/17JuneMedia.png", color.getImage());
	}

}
