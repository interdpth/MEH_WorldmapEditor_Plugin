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

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.zzl.minegaming.GBAUtils.BitConverter;
import org.zzl.minegaming.GBAUtils.GBAImage;
import org.zzl.minegaming.GBAUtils.GBAImageType;
import org.zzl.minegaming.GBAUtils.GBARom;
import org.zzl.minegaming.GBAUtils.Lz77;
import org.zzl.minegaming.GBAUtils.Palette;
import org.zzl.minegaming.MEH.DataStore;
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
	public  GBAImage rawImage;
	//if engine == rse
	private Palette p; 
	private BufferedImage bi;
	private byte[] dcmpTilemap;
	//uf engine == frlg
	private Palette[] myPal;
	private BufferedImage[] mybi;
	private long[] newdcmpTilemap;
	private MapData mapData;//Some times firered wants to see the world burn.
	private MapTileData mapTileData;//So burn baby, burn. 
	
	
	
	
	private GBARom myRom;
	private int[] dcmpGFX;
	
	 int srcX;
	    int srcY;
		public boolean bLoaded;
	
	 public BufferedImage get8BPPTile(int tileNum)
		{
			
			
			int x = ((tileNum) % (64)) * 8;
			int y = ((tileNum) / (64)) * 8;
			BufferedImage toSend = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
			try
			{
				toSend =  bi.getSubimage(x, y, 8, 8);
			}
			catch(Exception e)
			{
				//e.printStackTrace();
				//System.out.println("Attempted to read 8x8 at " + x + ", " + y);
			}
		
			
			return toSend;
		}
	 
	 public BufferedImage get4BPPTile(long tile){
		 return get4BPPTile((int)tile&0x3FF, (int)((tile&0xF000)>>12), (tile&0x400)==0x400, (tile&0x800)==0x800);
	 }
	 public BufferedImage get4BPPTile(long tileNum, long palette, boolean xFlip, boolean yFlip)
		{
		
			
		 int x = (int) (((tileNum & 0x3FF) % (32)) * 8);
		 int y = (int) (((tileNum & 0x3FF) / (32)) * 8);
			BufferedImage toSend = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
			try
			{
				toSend =  mybi[(int) palette].getSubimage(x, y, 8, 8);
			}
			catch(Exception e)
			{
				//e.printStackTrace();
			//	System.out.println("Attempted to read 8x8 at " + x + ", " + y);
			}
	

			
			return toSend;
		}
	    void DrawPal(){
	    	
	    int x = 0;
		int i=0;
		if(i==0)return;
		Palette pal= (Palette) (DataStore.EngineVersion==1? myPal:p);
		for(i = 0; i < pal.getSize(); i++)
			{
				while(x < 16)
				{
					try
					{
						gcBuff.setColor(pal.getIndex(x));
						gcBuff.fillRect(270+x*8, 256+i*8, 8, 8);
					}
					catch(Exception e){}
					x++;
				}
				x = 0;
			}
	    }
		void DrawFRLG(){
			int i=0;
			int len=(dcmpTilemap.length);//We're 8bpp now just trying to get the regular tiles to display
										  //VBA shows 4bpp rows in tile viewer as 0-31, 8bpp does 0-62
			int screenX=0;
		
			int x=0;
			int y=0;
			int xLoop=0;
			int yLoop=0;
			i=0;
			int tile_x=0;
	        int tile_y=0;
	        int tileCounter=0;
	        long tile=0;
	        //This is gonna be dumb...
	        i=0;
	        int[] tiles=new int[dcmpTilemap.length/2];
	        byte[] special=new byte[dcmpTilemap.length/2];
	        int counter=0;
	        for(i=0;i<len/2;i++){
	        	
	        	tiles[i]=(dcmpTilemap[counter] & 0xFF) + ((dcmpTilemap[counter+1] & 0xFF) << 8);
	        	special[i]=dcmpTilemap[counter+1];
	        	counter+=2;
	        }
            for(i=0;i<tiles.length;i++){
                 tile_x=(i%30);
                 tile_y=(i/30);
            	 gcBuff.setColor(Color.red);
		            gcBuff.drawRect(tile_x*8,tile_y*8, 8, 8);
            	try{
            	int posInMap=0x0;
            	int val=tiles[(byte)posInMap+i]+(special[(byte)posInMap+i] << 8);
            	int curtile=tiles[posInMap+i] & 0x3FF;
            	int pal=(special[(byte)posInMap+i]&0xF0) >> 4;
            	boolean hf=(special[(byte)posInMap+i]&0x4)==4;
            	boolean hv= (special[(byte)posInMap+i]&0x8)==8;
            	
            	System.out.println(String.format("%05x", i*2    ) + " " +
            			String.format("%04x",tile_x) + " " + 
            			String.format("%04x",tile_y) + " " +  
            			String.format("%04x",val   ) + " " + 
            			String.format("%04x",(pal)) + " " +
            			String.format("%04x",(curtile)) + " " + 
            			          Boolean.toString(hf) +" " +Boolean.toString(hv) );
            				       
            					 
            	  gcBuff.drawImage(get4BPPTile( curtile, pal , hf, hv),
            			  tile_x*8,tile_y*8,null);
            	}catch(Exception e){}
            }
	      
	        
            bLoaded=true;     
            DrawPal();
            DrawTileset();
				
			this.repaint();
		}
	    void DrawRSE(){
		int i=0;
		int len=(dcmpTilemap.length);//We're 8bpp now just trying to get the regular tiles to display
									  //VBA shows 4bpp rows in tile viewer as 0-31, 8bpp does 0-62
		int screenX=0;
	
		int x=0;
		int y=0;
		int xLoop=0;
		int yLoop=0;
		i=0;
		
	
				
                int tile_x=0;
                int tile_y=0;
                for(tile_y =0; tile_y < 32; tile_y++)
                {
	           			
	                for(tile_x = 0; tile_x <32; tile_x++)
					{
		         
						
			                
		                	
		                	int srcx=(tile_x)*8;
			                int srcy=(tile_y)*8;
			                
			                int kX=tile_x;
			                int kY=(tile_y) * 64;
			                int kZ=0x000;
			                gcBuff.drawImage(get8BPPTile(dcmpTilemap[kX + kY +kZ ] & 0xFF),
				    						srcx,
				    						srcy,null);
					}
				}
                bLoaded=true;  
                DrawPal();
                DrawTileset();
			
		this.repaint();
	}
        void SetupRSE(GBARom rom, int map){
    	dcmpTilemap=BitConverter.toBytes(Lz77.decompressLZ77(rom,DataStore.WorldMapTileMap[map]));
    	
    	rom.Seek(DataStore.WorldMapPal[map]);
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
 		
		p = new Palette(GBAImageType.c256 ,pal);
		
		rawImage = new GBAImage(dcmpGFX,p,new Point(512,512));//pntSz);
		
		bi = rawImage.getBufferedImage();
		
		imgBuffer = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
		
		gcBuff=imgBuffer.getGraphics();
		DrawRSE();
    }
	  
    
    void SetupFRLG(GBARom rom, int map){
    	int offset=(int)(rom.getPointer(DataStore.WorldMapTileMap[map]) & 0x1FFFFFF);
    	dcmpTilemap=BitConverter.toBytes(Lz77.decompressLZ77(rom,offset));
    int i=0;
    int tilecounter=0;

    
     myPal = new Palette[6];
	 i=0;
     rom.Seek(DataStore.WorldMapPal[map]);
        
		for(i = 0; i < 6; i++)
		{
			byte[] a=rom.readBytes(32);
			a[0]=(byte) 0xC3;a[1]=(byte) 0x71;
			myPal[i] = new Palette(GBAImageType.c16,a );
		}
 
 		
         rawImage = new GBAImage(dcmpGFX,myPal[0],new Point(256,256));//pntSz);	
		
         mybi = new BufferedImage[6];
		for(i=0;i<6;i++){
			mybi[i] = rawImage.getBufferedImageFromPal(myPal[i],false);
		}
		imgBuffer = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_ARGB);
		
		gcBuff=imgBuffer.getGraphics();
		DrawFRLG();
    }
    public void DrawTileset(){
    	int selected=UI.cboPal.getSelectedIndex();
    	if(selected==-1){
    		UI.cboPal.setSelectedIndex(0);
    	}
    	selected=UI.cboPal.getSelectedIndex();
    	if(DataStore.EngineVersion==1){
    	gcBuff.drawImage(rawImage.getBufferedImageFromPal(myPal[selected]),270,0,this);
    	}else{
    		gcBuff.drawImage(rawImage.getBufferedImage().getSubimage(0, 0, 512, 512),270,0,this);
    	}
    }
	void Load(GBARom rom, int map) {
	
		UI.cboPal.removeAllItems();
		int i=0;	
		for(i=0;i<DataStore.WorldMapSlot[map];i++){
			UI.cboPal.addItem(Integer.toHexString(i));
		}
	
	

        dcmpGFX=Lz77.decompressLZ77(rom,DataStore.WorldMapGFX[map]);
		
        switch((int)DataStore.EngineVersion){
        case 1:
	    	SetupFRLG(rom, map);
	    	break;
        case 0:
		
	    	SetupRSE(rom, map);
	        break;
        }
	
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
		
			g.drawImage(imgBuffer, 0, 0, this);
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
