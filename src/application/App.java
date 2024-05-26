package application;

import javax.swing.SwingUtilities;

import gui.MainGui;

public class App {

	public static void main(String[] args) {
		MainGui mainGui = new MainGui();
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				mainGui.setVisible(true);
				
			}
		});

	}

}
