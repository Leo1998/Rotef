package com.rotef.game.editor;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.UIManager;

public class EditorLauncher {

	public static void main(String[] args) {
		launch();
	}

	public static void launch() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// ignore
		}

		File dir = new File("editor");
		dir.mkdir();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditorFrame frame = new EditorFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
