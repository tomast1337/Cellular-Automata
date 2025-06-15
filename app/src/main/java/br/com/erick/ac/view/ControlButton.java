package br.com.erick.ac.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import br.com.erick.ac.Events;

public class ControlButton extends JButton implements MouseListener{
	
	private List<ButtonListener> listeners = new ArrayList<>();
	
	public ControlButton(Events e) {
		addMouseListener(this);
		if(e == Events.PAUSE) {
			setText("Pause");
		}else if(e == Events.PLAY){
			setText("Play");
		}else if(e == Events.CLEAR){
			setText("Clear");
		}else if(e == Events.CHANGERULE) {
			setText("Change Rule");
		}else if(e == Events.CHANGEFRAMERATE) {
			setText("Change framerate");
		}else if(e == Events.CHANGESIZE) {
			setText("Change size");
		}
	}
	
	public void addListener(ButtonListener l) {
		listeners.add(l);
	}
	
	public void notifyListener(Events e) {
		listeners.forEach(l -> {
			l.actionPerformed(e);
		});
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			if(this.getText() == "Pause") {
				notifyListener(Events.PAUSE);
			}else if(this.getText() == "Play"){				
				notifyListener(Events.PLAY);
			}else if(getText() == "Clear"){
				notifyListener(Events.CLEAR);
			}else if(getText() == "Change Rule"){
				notifyListener(Events.CHANGERULE);
			}else if(getText() == "Change framerate"){
				notifyListener(Events.CHANGEFRAMERATE);
			}else {
				notifyListener(Events.CHANGESIZE);
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
