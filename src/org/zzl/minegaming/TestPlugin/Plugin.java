package org.zzl.minegaming.TestPlugin;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.UIManager;

import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.MEH.MainGUI;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
public class Plugin extends org.zzl.minegaming.MEH.Plugin
{
	UI myWindow;
	@Override
	public void load()
	{
		bLoadROM=true;

		System.out.println("Hello, World!");
		setButtonImage(new ImageIcon(this.getClass().getResource("/resources/button.png")));
		setToolTip("Hello World Plugin v0.1");
		
	
		
	}
	
	@Override
	public void execute()
	{
		
		myWindow= new UI(rom);
		myWindow.setSize(512, 512);
		myWindow.setName("Worldmap editor");
		myWindow.setVisible(true);
		
		
	}
	
	@Override
	public void loadROM(GBARom rom)
	{
		this.rom = rom;
		System.out.println("ROM " + rom.getGameCode() + " " + rom.getGameText() + " loaded!");
		
	}
	
	@Override
	public void loadMap(int map, int bank)
	{
		System.out.println("Map " + map + " bank " + bank + " loaded!");
	}
	
	@Override
	public void unload()
	{
		System.out.println("Exiting!");
	}
}
