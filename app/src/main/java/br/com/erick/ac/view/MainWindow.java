package br.com.erick.ac.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.security.InvalidParameterException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.erick.ac.Events;
import br.com.erick.ac.model2D.CellularAutomata2D;

public class MainWindow extends JFrame implements ButtonListener{
	
	private CellularAutomata2D cellularAutomata;
	private JTextField ruleField;
	private JTextField fpsField;
	private JTextField sizeField;
	private Grid grid;
	private ControlButton pauseButton;
	
	public MainWindow(int size) {
		setSize(new Dimension(720, 780));
		cellularAutomata = new CellularAutomata2D(size, 0);
		cellularAutomata.setFps(10);
		
		this.grid = new Grid(cellularAutomata, size);

		cellularAutomata.setGrid(grid);

		ruleField = new JTextField("" + Integer.parseInt(cellularAutomata.getRule(), 2));

		fpsField = new JTextField("" + cellularAutomata.getFps());

		sizeField = new JTextField("" + size);
		
		ControlButton playButton = new ControlButton(Events.PLAY);
		playButton.addListener(cellularAutomata);
		
		pauseButton = new ControlButton(Events.PAUSE);		
		pauseButton.addListener(cellularAutomata);
		
		ControlButton clearButton = new ControlButton(Events.CLEAR);		
		clearButton.addListener(cellularAutomata);
		
		ControlButton changeButton = new ControlButton(Events.CHANGERULE);		
		changeButton.addListener(this);
		
		ControlButton changeFrameRate = new ControlButton(Events.CHANGEFRAMERATE);
		changeFrameRate.addListener(this);
		
		ControlButton changeSize = new ControlButton(Events.CHANGESIZE);
		changeSize.addListener(this);
		
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout());
		northPanel.add(playButton);
		northPanel.add(pauseButton);
		northPanel.add(clearButton);
		northPanel.add(changeButton);
		northPanel.add(ruleField);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout());
		southPanel.add(changeFrameRate);
		southPanel.add(fpsField);
		southPanel.add(changeSize);
		southPanel.add(sizeField);
		
		setLayout(new BorderLayout());
		add(northPanel, BorderLayout.NORTH);
		add(grid, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MainWindow(100);
	}

	@Override
	public void actionPerformed(Events e) {
		if(e == Events.CHANGERULE) {
			String strRule;
			try {
				strRule = ruleField.getText();
			} catch (NullPointerException e2) {
				strRule = "0";
			}
			int num;
			try {
				num = Integer.parseInt(strRule, 10);
			} catch (Exception e2) {
				num = 0;
				ruleField.setText("0");
				JOptionPane.showMessageDialog(this, "Max rule number: 2.147.483.647\nOnly numbers!");
			}
			num = Math.abs(num);
			cellularAutomata.setRule(num);			
		}else if(e == Events.CHANGEFRAMERATE) {
			int num = 0;
			try {
				num = Integer.parseInt(fpsField.getText(), 10);
			}catch(NumberFormatException e3){
				JOptionPane.showMessageDialog(this, "Insert a valid fps number");
			}finally {
				try {
					cellularAutomata.setFps(num);
				} catch (InvalidParameterException e2) {
					JOptionPane.showMessageDialog(this, "Insert a valid fps number");
				}
			}
		}else if(e == Events.CHANGESIZE) {
			int size = 10;
			try {
				size = Math.abs(Integer.parseInt(sizeField.getText()));
			}catch(Exception e2) {
				JOptionPane.showMessageDialog(this, "Insert a valid size");
			}finally {
				if(size > 150 || size < 1) {
					JOptionPane.showMessageDialog(this, "Min size: 1\nMax size: 150");
					size = 10;
				}
				this.dispose();
				new MainWindow(size);
			}
		}
	}
}
