package br.com.erick.ac;

public class Cell {

	private boolean previousState;
	private boolean currentState;
	private String rule;
	
	public void setRule(String rule) {
		this.rule = rule;
	}

	public Cell(String rule) {
		this.previousState = false;
		this.currentState = false;
		this.rule = rule;
	}
	
	public boolean isPreviousState() {
		return previousState;
	}

	public void setPreviousState(boolean previousState) {
		this.previousState = previousState;
	}

	public boolean isCurrentState() {
		return currentState;
	}

	public void setCurrentState(boolean currentState) {
		this.currentState = currentState;
	}

	@Override
	public String toString() {
		return this.currentState ? "1" : "0";
	}
	
	public void applyRule(String rlm) {
		this.previousState = this.currentState;
		int steps = Integer.parseInt(rlm, 2);
		if(rule.charAt(steps) == '1') {
			this.currentState = true;
		}else {
			this.currentState = false;
		}
	}
	
	public String getPreviousStateString() {
		return this.previousState ? "1" : "0";
	}
}
