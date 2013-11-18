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
import javax.swing.JSpinner;
import javax.swing.JComboBox;

public class UI extends JFrame {
	WorldMapEditorPanel wmep;
	UI(GBARom rom){
	
		if(DataStore.bDataStoreInited==false){
			new DataStore("MEH.ini", rom.getFriendlyROMHeader());//Init the store. 
					
		}
	
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 392, 240);
		getContentPane().add(panel);
		panel.setLayout(null);
		


		
		wmep = new WorldMapEditorPanel();
		panel.add(wmep);
		wmep.setBounds(10,11,372,218);
		wmep.setPreferredSize(new Dimension(512,512));
		wmep.setBorder(UIManager.getBorder("SplitPane.border"));
		wmep.setLayout(null);
		wmep.Load(rom,0);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(483, 228, 89, 23);
		getContentPane().add(btnSave);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(412, 57, 178, 160);
		getContentPane().add(panel_1);
		
		JLabel lblMap = new JLabel("Map");
		lblMap.setBounds(421, 14, 46, 14);
		getContentPane().add(lblMap);
		
		JComboBox cboMapSel = new JComboBox();
		cboMapSel.setBounds(443, 11, 103, 20);
		getContentPane().add(cboMapSel);
		//Add items..
		
		int i=0;
		for(i=0;i<DataStore.WorldMapCount;i++){
			String t=Integer.toString(i);
			cboMapSel.addItem(t);
		}
		cboMapSel.setSelectedIndex(0);
	}
}
