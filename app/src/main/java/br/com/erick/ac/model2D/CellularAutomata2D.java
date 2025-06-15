package br.com.erick.ac.model2D;

import java.security.InvalidParameterException;

import br.com.erick.ac.Cell;
import br.com.erick.ac.Events;
import br.com.erick.ac.view.ButtonListener;
import br.com.erick.ac.view.Grid;

public class CellularAutomata2D implements ButtonListener{
	
	private Cell[][] matrix;
	private String rule;
	private int size;
	private int miliPause = 100;
	private boolean paused = false;
    private final Object lock = new Object();
    private Thread t = new Thread(() -> {
		this.infiniteGenerations();
	});
    private Grid grid;
	
	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public int getFps() {
		return 1000 / this.miliPause;
	}

	public void setFps(int fps) {
		if(fps <= 0) {
			throw new InvalidParameterException();
		}
		this.miliPause = 1000 / fps;
	}
	
	public CellularAutomata2D(int size, int rule) {
		setRule(rule);
		this.size = size + 2;
		initMatrix(this.size);
	}
	
	public String getRule() {
		return this.rule;
	}
	
	public Cell[][] getMatrix() {
		return matrix;
	}
	
	public void clearMatrix() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				matrix[i][j].setCurrentState(false);
			}
		}
		grid.refreshColors();
	}

	public void setRule(int rule) {
		String num = Integer.toBinaryString(rule);
		while(num.length() < 32) {
			num = "0" + num;
		}
		this.rule = new StringBuilder(num).reverse().toString();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				matrix[i][j].setRule(this.rule);
			}
		}
	}
	
	private void initMatrix(int size) {
		this.matrix = new Cell[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				matrix[i][j] = new Cell(this.rule);
			}
		}
	}
	
	public void nextGen() {
		for(int i = 1; i < size - 1; i++) {
			for(int j = 1; j < size - 1; j++) {
				try {
					String lrn = "" + matrix[i - 1][j].getPreviousStateString() +
							matrix[i][j - 1].getPreviousStateString() +
							matrix[i][j] + 
							matrix[i][j + 1] +
							matrix[i + 1][j];
					matrix[i][j].applyRule(lrn);
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
	}
	
	public void infiniteGenerations() {
		while (true) {
			synchronized (lock) {
                while (paused) {
                    try {
                        lock.wait(); // A thread fica em espera até ser notificada
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					try {
						String lrn = "" + matrix[i - 1][j].getPreviousStateString()
								+ matrix[i][j - 1].getPreviousStateString() + matrix[i][j] + matrix[i][j + 1]
								+ matrix[i + 1][j];
						matrix[i][j].applyRule(lrn);
					} catch (ArrayIndexOutOfBoundsException e) {
						continue;
					}
				}
			}
			grid.refreshColors();
			try {
				Thread.sleep(this.miliPause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getFormatedFrame() {
		String frame = "";
		for(int i = 1; i < size - 1; i++) {
			for(int j = 1; j < size - 1; j++){
				frame += matrix[i][j] + " ";
			}
			frame += "\n";
		}
		return frame;
	}
	
	public String getFrame() {
		String frame = "";
		for(int i = 1; i < size - 1; i++) {
			for(int j = 1; j < size - 1; j++){
				frame += matrix[i][j];
			}
		}
		return frame;
	}
	
	public void setDefaultInitialPoint() {
		matrix[size/2][size/2].setCurrentState(true);
	}

	@Override
	public void actionPerformed(Events e) {
		if(e == Events.PAUSE) {
			synchronized (lock) {				
				paused = true;
			}
		}else if(e == Events.PLAY) {
			try {
				t.start();
			} catch(IllegalThreadStateException e2) {
				synchronized (lock) {
					paused = false;
					lock.notify();
				}
			}
		}else if(e == Events.CLEAR) {
			clearMatrix();
		}
	}
}
