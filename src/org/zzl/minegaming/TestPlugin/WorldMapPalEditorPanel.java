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
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

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

public class WorldMapPalEditorPanel extends JPanel
{
	private static final long serialVersionUID = -877213633894324075L;
    private boolean isMouseDown = true;
    static Rectangle mouseTracker;
    public  Graphics gcBuff;
	public  Image imgBuffer;
    public  static boolean bRedraw;
	
	 int srcX;
	 int srcY;
	 public  boolean bLoaded;
	  
	 void DrawPal4BPP(){
		  
		    int x = 0;
			int i=0;
			
			for(i = 0; i < WorldMapEditorPanel.tilemapBuffer.myPal.length; i++)
				{
					while(x < 16)
					{
						try
						{
							gcBuff.setColor(WorldMapEditorPanel.tilemapBuffer.myPal[i].getIndex(x));
							gcBuff.fillRect(x*8, i*8, 8, 8);
						}
						catch(Exception e){}
						x++;
					}
					x = 0;
				}
			 bRedraw=false;
			 this.repaint();
		 
	 }
	 void DrawPal8BPP(){
		 int x = 0;
			int i=0;
			
			Palette pal= (Palette) (WorldMapEditorPanel.tilemapBuffer.myType==GBAImageType.c16? WorldMapEditorPanel.tilemapBuffer.myPal:WorldMapEditorPanel.tilemapBuffer.p);
			for(i = 0; i < WorldMapEditorPanel.tilemapBuffer.p.getSize(); i++)
				{
					while(x < 16)
					{
						try
						{
							gcBuff.setColor(WorldMapEditorPanel.tilemapBuffer.p.getIndex(i*16+x));
							gcBuff.fillRect(x*8, i*8, 8, 8);
						}
						catch(Exception e){}
						x++;
					}
					x = 0;
				}
			 bRedraw=false;
			 this.repaint();
	 
	 }
	 void DrawPal()
	 {
	    	if(WorldMapEditorPanel.tilemapBuffer == null)
	    		return;
		   switch(WorldMapEditorPanel.tilemapBuffer.myType){
		   case c16:
			   DrawPal4BPP();
			   break;
		   case c256:
			   DrawPal8BPP();
			   break;
		   
		   }
		 
		  
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
	public WorldMapPalEditorPanel()
	{
       mouseTracker=new Rectangle(0,0,16,16);
       imgBuffer = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
       bRedraw=true;
		gcBuff=imgBuffer.getGraphics();
		

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
		if(bRedraw){
			DrawPal();
		}
    	super.paintComponent(g);

			g.drawImage(imgBuffer, 0, 0, this);
			File outputfile = new File("saved.png");
		    try
			{
				ImageIO.write((RenderedImage) imgBuffer, "png", outputfile);
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
