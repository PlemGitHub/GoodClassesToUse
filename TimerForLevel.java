package mech;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import data.Constants;
import visual.panels.GamePanel;

@SuppressWarnings("serial")
public class TimerForLevel extends Timer implements Constants, ActionListener{
	private GamePanel gamePanel;
	private int remainSeconds;
	private int remainMinutes;

	public TimerForLevel(int delay, ActionListener listener, int level, GamePanel gamePanel) {
		super(delay, listener);
		this.gamePanel = gamePanel;
		level = (level < 10)? 10: level;
		remainSeconds = level*K_FOR_TIME;
		remainMinutes = remainSeconds/60;
		remainSeconds = remainSeconds-remainMinutes*60;
		setTextOnTimeLabel();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		remainSeconds--;
		if (remainSeconds < 0){
			remainMinutes--;
			remainSeconds = 59;
			if (remainMinutes < 0){
				remainMinutes = 0;
				remainSeconds = 0;
			}
				
		}
		setTextOnTimeLabel();
	}

	private void setTextOnTimeLabel() {
		String minutes = Integer.toString(remainMinutes);
			minutes = (remainMinutes < 10)? "0"+minutes : minutes;
		String seconds = Integer.toString(remainSeconds);
			seconds = (remainSeconds < 10)? "0"+seconds : seconds;
		String str = minutes+":"+seconds;
		gamePanel.setTextOnTimeLabel(str);
	}

}
