import javax.swing.ImageIcon;
import javax.swing.JLabel;
import sun.audio.*;

public abstract class Chess implements Cloneable{
	
	public Chess(){
		
	}
	
	public Chess(String chessName,int x,int y,int camp){
		
		this.name = chessName;
		this.x = x;
		this.y = y;
		this.camp = camp;
		this.critical = false;
		this.weight = 0;
		
		setImage();
	}
	
	public void moveX(int x){
		this.x = x;
	}
	
	public void moveY(int y){
		this.y = y;
	}
	
	public void moveXY(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getWeight(){
		if(camp==1)
			return weight;
		else{
			return weight+2;
		}
	}
	
	public int camp(){
		return this.camp;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean getFirstStep(){
		return this.firstStep;
	}
	
	public boolean isCritical(){
		return this.critical;
	}
	
	public void setFirstStep(boolean b) {
		firstStep = b;
	}
	
	public Chess clone(){
		return this;
	}
	public abstract void setImage();
	public abstract void setMusic();
	public abstract void setMusicDead();
	public abstract boolean[][] getReachableGrid(Chess[][] chessboard);
	public abstract boolean isReachable(Chess[][]chessboard,int x,int y);	
 
	public AudioStream audioStream;
	public JLabel icon;
	public ImageIcon chessPic;
	
	protected int x,y;
	protected String name;
	protected boolean critical;
	protected boolean firstStep;
	protected int weight;
	protected int camp;

}
