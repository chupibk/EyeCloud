package fi.eyecloud.gui.fixation;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.StringLabel;

public class FixationFromText {
	private JPanel Screen = new JPanel();
	
	/**
	 * Draw fixations over stimulus for visulization
	 */
	public FixationFromText(){
		JFrame frame = new JFrame(StringLabel.FIXATION_FRAME_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		Screen.setLayout(new BorderLayout());
		FixationPaint paint = new FixationPaint();
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
		new FixationFromText();
	}

}
