package org.zzl.minegaming.TestPlugin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.zzl.minegaming.GBAUtils.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.DataStore;
import org.zzl.minegaming.GBAUtils.GBAImage;
import org.zzl.minegaming.GBAUtils.GBAImageType;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.Lz77;
import org.zzl.minegaming.GBAUtils.Palette;
import org.zzl.minegaming.MEH.TilemapBuffer;
import org.zzl.minegaming.MEH.MapElements.MapData;
import org.zzl.minegaming.MEH.MapElements.MapTileData;
import org.zzl.minegaming.MEH.MapElements.OverworldSprites;
import org.zzl.minegaming.MEH.MapElements.Tileset;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;

public class WorldMapEditorPanel extends JPanel
{
	private static final long serialVersionUID = -877213633894324075L;
    private boolean isMouseDown = true;
    static Rectangle mouseTracker;
    public  Graphics gcBuff;
	public  Image imgBuffer;


	private BufferedImage[] mybi;

	
	
	public static TilemapBuffer tilemapBuffer;
	
	
	
	private GBARom myRom;
	private int[] dcmpGFX;
	
	 int srcX;
	    int srcY;
		public boolean bLoaded;
	
	 
	   
	
	    void Draw(){

	    	
	    	WorldMapEditorPanel.tilemapBuffer.DrawMap();
	    	WorldMapPalEditorPanel.bRedraw=true;
            WorldMapTileEditorPanel.bRedraw=true;
                
			
		this.repaint();
	}
        void SetupRSE(GBARom rom, int map){
    	byte[] dcmpTilemap=BitConverter.toBytes(Lz77.decompressLZ77(rom,(int)(rom.getPointer(DataStore.WorldMapTileMap[map]))));
    	
    	rom.Seek((int) rom.getPointer(DataStore.WorldMapPal[map]));
 		int basepal=DataStore.WorldMapSlot[map]*0x10;
 		byte[] pal=new byte[512];
 		int i=0;
 		for(i=0;i<DataStore.WorldMapPalSize[map];i++){
 			pal[basepal+i]=rom.readByte();
 		
 			//Game reads it twice for some reason. 
 		}
 		//For whatever reason
 		for(i=0;i<8;i++){
 			pal[i*16]=(byte) 0xC3;pal[i*16 + 1]=(byte) 0x71;
 		}
 		imgBuffer = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
		
		gcBuff=imgBuffer.getGraphics();
		
		tilemapBuffer=new TilemapBuffer(GBAImageType.c256, new Palette(GBAImageType.c256 ,pal), dcmpTilemap,dcmpGFX );
		Draw();
    }
	  
    
    void SetupFRLG(GBARom rom, int map){
    	int offset=(int)(rom.getPointer(DataStore.WorldMapTileMap[map]));
    	byte[] dcmpTilemap=BitConverter.toBytes(Lz77.decompressLZ77(rom,offset));
    int i=0;
    int tilecounter=0;

    
    Palette[] myPal = new Palette[6];
	 i=0;
     rom.Seek((int) rom.getPointer(DataStore.WorldMapPal[map]));
        
		for(i = 0; i < 6; i++)
		{
			byte[] a=rom.readBytes(32);
			a[0]=(byte) 0xC3;a[1]=(byte) 0x71;
			myPal[i] = new Palette(GBAImageType.c16,a );
		}
		tilemapBuffer=new TilemapBuffer(GBAImageType.c16, myPal, dcmpTilemap,dcmpGFX );
		imgBuffer = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
		
		gcBuff=imgBuffer.getGraphics();
		Draw();
    }

	void Load(GBARom rom, int map) {
	
		UI.cboPal.removeAllItems();
		int i=0;	
		for(i=0;i<DataStore.WorldMapSlot[map];i++){
			UI.cboPal.addItem(Integer.toHexString(i));
		}
	
	

        dcmpGFX=Lz77.decompressLZ77(rom,(int) rom.getPointer(DataStore.WorldMapGFX[map]));
		
        switch((int)DataStore.EngineVersion){
        case 1:
	    	SetupFRLG(rom, map);
	    	break;
        case 0:
		
	    	SetupRSE(rom, map);
	        break;
        }
	    WorldMapEditorPanel.tilemapBuffer.RenderBufferGFX();
	    
		this.repaint();
	}
    public void SetRect(int width, int heigh){
    
        if(heigh>16) heigh=16;
        if(width>16) width=16;
    	mouseTracker.height=heigh;
    	mouseTracker.width=width;
    }
    public void SetRect(){
    	mouseTracker.height=16;
    	mouseTracker.width=16;
    }
	public WorldMapEditorPanel()
	{
       mouseTracker=new Rectangle(0,0,16,16);
       
		this.addMouseMotionListener(new MouseMotionListener()
		{

			@Override
			public void mouseDragged(MouseEvent arg0)
			{
				
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
			
			}

		});

		this.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseClicked(MouseEvent e)
			{
				   System.out.println(Integer.toString(e.getX()) + " " + 
				   			  Integer.toString(e.getY()) + " " + Integer.toString(e.getX()/8) + " " + 
				   			  Integer.toString(e.getY()/8) + " " );
				 
			
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				isMouseDown = true;
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				
					
				
			}

		});

	}
    protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		try{
			g.drawImage(WorldMapEditorPanel.tilemapBuffer.imgBuffer, 0, 0, this);
		}catch(Exception e){
		}
		
			int x = 0;
			int i=0;

			
		
		try
		{
			//best error image.
			//I'll always remember you Smeargle <3
			//g.drawImage(ImageIO.read(MainGUI.class.getResourceAsStream("/resources/smeargle.png")), 100, 240,null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	
}
