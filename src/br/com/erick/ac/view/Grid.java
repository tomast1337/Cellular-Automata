package br.com.erick.ac.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import br.com.erick.ac.Cell;
import br.com.erick.ac.Events;
import br.com.erick.ac.model2D.CellularAutomata2D;

public class Grid extends JPanel implements ButtonListener{
	
	private Cell[][] matrix;
	private CellButton[][] bMatrix;
	
	private int lenght;
	
	public Grid(CellularAutomata2D ac, int lenght) {
		super();
		setPreferredSize(new Dimension(720, 720));
		this.lenght = lenght;
		bMatrix = new CellButton[lenght][lenght];
		CellButton cb;
		this.matrix = ac.getMatrix();
		setLayout(new GridLayout(lenght, lenght));
		for(int i = 0; i < lenght; i++) {
			for(int j = 0; j < lenght; j++) {
				cb = new CellButton(matrix[i + 1][j + 1]);
				cb.addListeners(this);
				cb.setBackground(Color.WHITE);
				add(cb);
				bMatrix[i][j] = cb;
			}
		}
	}

	@Override
	public void actionPerformed(Events e) {
		if(e == Events.REFRESH) {
			refreshColors();
		}
	}

	public void refreshColors() {
		for(int i = 0; i < lenght; i++) {
			for(int j = 0; j < lenght; j++) {
				if(matrix[i + 1][j + 1].isCurrentState()) {
					bMatrix[i][j].setBackground(Color.BLACK);
				}else {
					bMatrix[i][j].setBackground(Color.WHITE);					
				}
			}
		}
	}
}
