package org.zzl.minegaming.TestPlugin;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.ROMManager;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;

import com.jgoodies.forms.factories.DefaultComponentFactory;

public class UI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -475765050269912957L;
	public WorldMapEditorPanel wmep;
	public WorldMapTileEditorPanel wmtep;
	public static JComboBox cboPal;
	public JComboBox cboMapSel;
	public static boolean bRefreshTileset;
	public WorldMapPalEditorPanel wmpep;
	UI(){
	    GBARom rom=ROMManager.currentROM;
		if(DataStore.bDataStoreInited==false){
			new DataStore("MEH.ini", rom.getFriendlyROMHeader());//Init the store. 
					
		}
	
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(20, 14, 272, 192);
		getContentPane().add(panel);
		panel.setLayout(null);
		


		
		wmep = new WorldMapEditorPanel();
		panel.add(wmep);
		wmep.setBounds(10,11,256,172);
		wmep.setPreferredSize(new Dimension(512,512));
		wmep.setBorder(UIManager.getBorder("SplitPane.border"));
		wmep.setLayout(null);
		
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(493, 10, 89, 23);
		getContentPane().add(btnSave);
		
		JLabel lblMap = new JLabel("Map");
		lblMap.setBounds(324, 14, 46, 14);
		getContentPane().add(lblMap);
		
		cboMapSel = new JComboBox();
		cboMapSel.setBounds(380, 11, 103, 20);
		getContentPane().add(cboMapSel);
		//Add items..
		
		int i=0;
		for(i=0;i<DataStore.WorldMapCount;i++){
			String t=Integer.toString(i);
			cboMapSel.addItem(t);
		}
		cboMapSel.setSelectedIndex(0);
		
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
		
		JLabel lblMap_1 =  new JLabel("Map:");
		lblMap_1.setBounds(10, 0, 88, 14);
		getContentPane().add(lblMap_1);
		
		JLabel lblTileset = new JLabel("Tileset:");
		lblTileset.setBounds(20, 217, 46, 14);
		getContentPane().add(lblTileset);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(30, 233, 258, 192);
		getContentPane().add(panel_1);
		
		wmtep = new WorldMapTileEditorPanel();
		panel_1.add(wmtep);
		wmtep.setBounds(10,11,512,512);
		wmtep.setPreferredSize(new Dimension(256, 176));
		wmtep.setBorder(UIManager.getBorder("SplitPane.border"));
		wmtep.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(324, 217, 229, 53);
		getContentPane().add(panel_2);
		
		JLabel lblPalette = new JLabel("Palette:");
		lblPalette.setBounds(324, 183, 46, 30);
		getContentPane().add(lblPalette);
		
		 wmpep = new WorldMapPalEditorPanel();
		panel_2.add(wmpep);
		wmpep.setBounds(0,0,512,512);
		wmpep.setPreferredSize(new Dimension(512, 512));
		wmpep.setBorder(UIManager.getBorder("SplitPane.border"));
		wmpep.setLayout(null);
		
      
		UI.cboPal.setModel(new DefaultComboBoxModel(new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"}));
		UI.cboPal.setSelectedIndex(0);
        wmep.Load(rom,0);
	}
}
