package com.rotef.game.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.badlogic.gdx.files.FileHandle;
import com.rotef.game.world.entity.EntityTemplate;

public class EntityEditorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private EntityTemplate template;

	/**
	 * Create the panel.
	 */
	public EntityEditorPanel() {
		setLayout(new BorderLayout(0, 0));

		JPanel leftPane = new JPanel();
		leftPane.setBackground(Color.DARK_GRAY);
		add(leftPane, BorderLayout.WEST);
		GridBagLayout gbl_leftPane = new GridBagLayout();
		gbl_leftPane.columnWidths = new int[] { 57, 0 };
		gbl_leftPane.rowHeights = new int[] { 25, 0, 0 };
		gbl_leftPane.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_leftPane.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		leftPane.setLayout(gbl_leftPane);

		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				template = new EntityTemplate();
			}
		});
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.insets = new Insets(0, 0, 5, 0);
		gbc_btnNew.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNew.gridx = 0;
		gbc_btnNew.gridy = 0;
		leftPane.add(btnNew, gbc_btnNew);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (template != null) {
					EntityTemplate.saveTemplate(template, new FileHandle(new File("editor/entity.json")));
				}
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 1;
		leftPane.add(btnSave, gbc_btnSave);

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		JPanel editPane = new JPanel();
		editPane.setBackground(Color.LIGHT_GRAY);
		scrollPane.setViewportView(editPane);
		editPane.setLayout(new GridLayout(1, 0, 0, 0));

	}

	private void createEditPane() {

	}

}
