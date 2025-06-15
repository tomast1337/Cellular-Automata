package br.com.erick.ac.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import br.com.erick.ac.Cell;
import br.com.erick.ac.Events;

public class CellButton extends JButton implements MouseListener{
	
	private Cell cell;
	
	private List<ButtonListener> listeners = new ArrayList<>();
	
	public CellButton(Cell c) {
		super();
		this.cell = c;
		addMouseListener(this);
	}
	
	public void addListeners(ButtonListener l) {
		listeners.add(l);
	}
	
	public void notifyListeners(Events e) {
		listeners.forEach(l -> l.actionPerformed(e));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) {
			this.cell.setPreviousState(cell.isCurrentState());
			if(cell.isCurrentState()) {
				cell.setCurrentState(false);
			}else {
				cell.setCurrentState(true);				
			}
			notifyListeners(Events.REFRESH);
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
