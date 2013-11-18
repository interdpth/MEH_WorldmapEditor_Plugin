package org.zzl.minegaming.TestPlugin;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.UIManager;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.GBAImage;
import org.zzl.minegaming.GBAUtils.GBAImageType;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.Lz77;
import org.zzl.minegaming.GBAUtils.Palette;
import org.zzl.minegaming.MEH.DataStore;
import org.zzl.minegaming.MEH.MapElements.OverworldSprites;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.FlowLayout;

public class UI extends JFrame {
	WorldMapEditorPanel wmep;
	UI(GBARom rom){
		
		if(DataStore.bDataStoreInited==false){
			new DataStore("MEH.ini", rom.getFriendlyROMHeader());//Init the store. 
					
		}
	
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 539, 240);
		getContentPane().add(panel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(483, 228, 89, 23);
		getContentPane().add(btnSave);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(408, 11, 178, 160);
		getContentPane().add(panel_1);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(526, 202, 46, 14);
		getContentPane().add(lblNewLabel);
		lblNewLabel.setText(Long.toHexString(DataStore.AttackNameList));
		panel.repaint();
		
		 wmep = new WorldMapEditorPanel();
		panel.add(wmep);
		wmep.setPreferredSize(new Dimension(512, 512));
		wmep.setLayout(null);
		wmep.setBorder(UIManager.getBorder("SplitPane.border"));
		
		wmep.Load(rom,2);
	}
}
