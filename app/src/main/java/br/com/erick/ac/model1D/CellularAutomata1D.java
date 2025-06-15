package br.com.erick.ac.model1D;

import br.com.erick.ac.Cell;

public class CellularAutomata1D {
	
	private Cell[] array;
	private String rule;
	private int size;
	
	public CellularAutomata1D(int size, int rule) {
		setRule(rule);
		this.size = size;
		initArray(size);
	}
	
	public String getRegra() {
		return this.rule;
	}
	
	public void setRule(int rule) {
		String num = Integer.toBinaryString(rule);
		while(num.length() < 8) {
			num = "0" + num;
		}
		this.rule = new StringBuilder(num).reverse().toString();
	}
	
	private void initArray(int tamanho) {
		this.array = new Cell[tamanho];
		for(int i = 0; i < tamanho; i++) {
			array[i] = new Cell(this.rule);
		}
	}
	
	public void nextGen() {
		for(int i = 0; i < size; i++) {
			try {
				String lrn = "" + array[i - 1].getPreviousStateString() +
				array[i] + array[i + 1];
				array[i].applyRule(lrn);
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
	}
	
	public String getFormatedFrame() {
		String frame = "";
		for(int i = 1; i < size - 1; i++) {
			frame += array[i] + " ";
		}
		return frame;
	}
	
	public String getFrame() {
		String frame = "";
		for(int i = 1; i < size - 1; i++) {
			frame += array[i];
		}
		return frame;
	}
	
	public void setDefaultInitialPoint() {
		array[size/2].setCurrentState(true);
	}
}
