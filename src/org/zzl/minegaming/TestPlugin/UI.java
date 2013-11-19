package org.zzl.minegaming.TestPlugin;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.ROMManager;
import org.zzl.minegaming.MEH.DataStore;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JButton;

import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class UI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -475765050269912957L;
	WorldMapEditorPanel wmep;
	public static JComboBox cboPal;
	public JComboBox cboMapSel;
	UI(){
	    GBARom rom=ROMManager.currentROM;
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
		wmep.setBounds(10,11,512,512);
		wmep.setPreferredSize(new Dimension(512,512));
		wmep.setBorder(UIManager.getBorder("SplitPane.border"));
		wmep.setLayout(null);
		
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(483, 228, 89, 23);
		getContentPane().add(btnSave);
		
		JLabel lblMap = new JLabel("Map");
		lblMap.setBounds(421, 14, 46, 14);
		getContentPane().add(lblMap);
		
		cboMapSel = new JComboBox();
		cboMapSel.setBounds(443, 11, 103, 20);
		getContentPane().add(cboMapSel);
		//Add items..
		
		int i=0;
		for(i=0;i<DataStore.WorldMapCount;i++){
			String t=Integer.toString(i);
			cboMapSel.addItem(t);
		}
		cboMapSel.setSelectedIndex(0);
		
		JLabel lblPalette = new JLabel("Palette:");
		lblPalette.setBounds(410, 39, 46, 14);
		getContentPane().add(lblPalette);
		
		UI.cboPal = new JComboBox();
		UI.cboPal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(wmep.bLoaded==true){
					wmep.Load(ROMManager.currentROM, cboMapSel.getSelectedIndex());
				}
			}
		});

		UI.cboPal.setBounds(453, 36, 103, 20);
		getContentPane().add(UI.cboPal);
		
		
      
		UI.cboPal.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"}));
		UI.cboPal.setSelectedIndex(0);
        wmep.Load(rom,0);
	}
}
