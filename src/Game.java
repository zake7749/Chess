import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;//
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JMenuBar;//

public class Game extends JFrame implements MouseListener , ActionListener{//test

	private JPanel contentPane;
	private JLabel chessBoardpic; 
	private Chess[][] chessBoard,bChessBoard,wChessBoard,oChessBoard;
	
	private ColorPanel colorpanel;
	private AI ai;
	private boolean notDual;
	private JPanel panel_1,panel_2,panel_3,panel_4;
	
	private int state;
	private int nowcamp;
	private int stateX, stateY;
	
	private JButton Dual = new JButton("Dual");
	private JButton vsCom = new JButton("vs Com");
	private JButton unDo = new JButton("Undo");
	private JLabel lblNewLabel;
	private JLabel history;
	
	public static void main(String[] args) {
		try {
			Clip clip=AudioSystem.getClip();
			AudioInputStream audioStream=AudioSystem.getAudioInputStream(new File("bgm.wav"));
			clip.open(audioStream);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			Thread.sleep(1000);//looping as long as this thread alive
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game frame = new Game();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Game() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 10, 875, 675);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.addMouseListener(this);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		chessBoardpic = new JLabel("");
		chessBoardpic.setBounds(16, 0, 605, 637);
		chessBoardpic.setIcon(new ImageIcon("ChessBoard.png"));
		contentPane.add(chessBoardpic);		
		
		colorpanel = new ColorPanel();
		colorpanel.addColor(chessBoardpic);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 255, 0)));
		panel.setBounds(629, 21, 229, 343);
		panel.setBackground(Color.DARK_GRAY);
		contentPane.add(panel);
		panel.setLayout(null);
		Dual.setForeground(Color.DARK_GRAY);
		Dual.setFont(new Font("Heiti TC", Font.BOLD, 16));
		
		Dual.addActionListener(this);
		Dual.setBounds(10, 214, 209, 36);
		vsCom.setForeground(Color.DARK_GRAY);
		vsCom.setFont(new Font("Heiti TC", Font.BOLD, 16));
		vsCom.addActionListener(this);
		vsCom.setBounds(10, 256, 209, 36);
		unDo.setForeground(Color.DARK_GRAY);
		unDo.setBackground(Color.RED);
		unDo.setFont(new Font("Heiti TC", Font.BOLD, 16));
		unDo.addActionListener(this);
		unDo.setBounds(10, 298, 209, 36);
		panel.add(Dual);
		panel.add(vsCom);
		panel.add(unDo);
		
		JTextArea txtrHsChess = new JTextArea();
		txtrHsChess.setText("HS - Chess");
		txtrHsChess.setForeground(Color.WHITE);
		txtrHsChess.setFont(new Font("Serif", Font.BOLD, 32));
		txtrHsChess.setBackground(Color.DARK_GRAY);
		txtrHsChess.setBounds(37, 38, 165, 30);
		panel.add(txtrHsChess);
		
		JLabel hs_logo = new JLabel();
		
		hs_logo.setBounds(10, 64, 192, 147);
		//hs_logo.setIcon(new ImageIcon("hearthstone-logo.png"));
		panel.add(hs_logo);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(629, 390, 230, 226);
		panel_1.setLayout(null);
		panel_1.setBackground(Color.DARK_GRAY);
		contentPane.add(panel_1);
		
		panel_2 = new JPanel();		
		panel_2.setBorder(null);
		JTextArea campName = new JTextArea();
		campName.setForeground(new Color(255, 255, 255));
		campName.setBackground(new Color(70, 130, 180,0));
		panel_2.setBackground(new Color(70, 130, 180,0));
		Font font = new Font("Verdana", Font.BOLD, 20);
		campName.setFont(font);
		campName.setText("Humans Alliance");
		panel_2.setBounds(10, 30, 210, 40);
		panel_2.add(campName);
		panel_1.add(panel_2);//
		
		panel_3 = new JPanel();//
		JTextArea campName2 = new JTextArea();
		campName2.setForeground(new Color(248, 248, 255));
		campName2.setBackground(new Color(70,130,180,0));
		panel_3.setBackground(new Color(70,130,180,0));
		campName2.setFont(font);
		campName2.setText("Dark Dragon");
		panel_3.setBounds(10, 30, 210, 40);
		panel_3.add(campName2);
		panel_1.add(panel_3);//
		panel_3.setVisible(false);
		
		panel_4 = new JPanel();//
		panel_4.setForeground(new Color(70, 130, 180,0));
		panel_4.setBorder(null);
		panel_4.setBackground(new Color(70, 130, 180,0));
		panel_4.setBounds(10, 95, 210, 90);
		panel_1.add(panel_4);
		
		history = new JLabel("Result");
		history.setFont(new Font("Verdana", Font.BOLD, 20));
		history.setForeground(new Color(248, 248, 255));
		panel_4.add(history);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("Black-texture.jpg"));
		lblNewLabel.setLayout(new BorderLayout());
		lblNewLabel.setBounds(0, 0, 875, 637);
		contentPane.add(lblNewLabel);
		
		buildChessboard();
		
		
		chessBoardpic.repaint();
		
		state = 2;//first state=2
		nowcamp = 0;
		stateX = -1;
		stateY = -1;
	}
	
	private void buildChessboard(){
		
		chessBoard = new Chess[8][8];
		bChessBoard = new Chess[8][8];
		wChessBoard = new Chess[8][8];
		oChessBoard = new Chess[8][8];
		
		chessBoard[4][7] = new King("wKing",4,7,0);//test
		chessBoard[4][7].setImage();
		chessBoard[4][7].icon.setBounds((chessBoard[4][7].x)*70+19,(chessBoard[4][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[4][7].icon);
		
		chessBoard[3][0] = new King("bKing",3,0,1);//test
		chessBoard[3][0].setImage();
		chessBoard[3][0].icon.setBounds((chessBoard[3][0].x)*70+19,(chessBoard[3][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[3][0].icon);
		
		chessBoard[3][7] = new Queen("wQueen",3,7,0);//test
		chessBoard[3][7].setImage();
		chessBoard[3][7].icon.setBounds((chessBoard[3][7].x)*70+19,(chessBoard[3][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[3][7].icon);
		
		chessBoard[4][0] = new Queen("bQueen",4,0,1);//test
		chessBoard[4][0].setImage();
		chessBoard[4][0].icon.setBounds((chessBoard[4][0].x)*70+19,(chessBoard[4][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[4][0].icon);
		
		for(int i = 0; i < 8; i++){
			chessBoard[i][1] = new Pawn("bPawn",i,1,1);//test
			chessBoard[i][1].setImage();
			chessBoard[i][1].icon.setBounds((chessBoard[i][1].x)*70+19,(chessBoard[i][1].y)*70+39,70,70);
			chessBoardpic.add(chessBoard[i][1].icon);
		}
		
		for(int i = 0; i < 8; i++){
			chessBoard[i][6] = new Pawn("wPawn",i,6,0);//test
			chessBoard[i][6].setImage();
			chessBoard[i][6].icon.setBounds((chessBoard[i][6].x)*70+19,(chessBoard[i][6].y)*70+39,70,70);
			chessBoardpic.add(chessBoard[i][6].icon);
		}
		
		chessBoard[0][0] = new Rook("bRock",0,0,1);//test
		chessBoard[0][0].setImage();
		chessBoard[0][0].icon.setBounds((chessBoard[0][0].x)*70+19,(chessBoard[0][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[0][0].icon);
		chessBoard[7][0] = new Rook("bRock",7,0,1);//test
		chessBoard[7][0].setImage();
		chessBoard[7][0].icon.setBounds((chessBoard[7][0].x)*70+19,(chessBoard[7][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[7][0].icon);
		
		chessBoard[0][7] = new Rook("wRock",0,7,0);//test
		chessBoard[0][7].setImage();
		chessBoard[0][7].icon.setBounds((chessBoard[0][7].x)*70+19,(chessBoard[0][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[0][7].icon);
		chessBoard[7][7] = new Rook("wRock",7,7,0);//test
		chessBoard[7][7].setImage();
		chessBoard[7][7].icon.setBounds((chessBoard[7][7].x)*70+19,(chessBoard[7][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[7][7].icon);
		
		chessBoard[1][0] = new Knight("bKnight",1,0,1);//test
		chessBoard[1][0].setImage();
		chessBoard[1][0].icon.setBounds((chessBoard[1][0].x)*70+19,(chessBoard[1][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[1][0].icon);
		chessBoard[6][0] = new Knight("bKnight",6,0,1);//test
		chessBoard[6][0].setImage();
		chessBoard[6][0].icon.setBounds((chessBoard[6][0].x)*70+19,(chessBoard[6][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[6][0].icon);
		
		chessBoard[1][7] = new Knight("wKnight",1,7,0);//test
		chessBoard[1][7].setImage();
		chessBoard[1][7].icon.setBounds((chessBoard[1][7].x)*70+19,(chessBoard[1][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[1][7].icon);
		chessBoard[6][7] = new Knight("wKnight",6,7,0);//test
		chessBoard[6][7].setImage();
		chessBoard[6][7].icon.setBounds((chessBoard[6][7].x)*70+19,(chessBoard[6][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[6][7].icon);
		
		chessBoard[2][0] = new Bishop("bBishop",2,0,1);//test
		chessBoard[2][0].setImage();
		chessBoard[2][0].icon.setBounds((chessBoard[2][0].x)*70+19,(chessBoard[2][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[2][0].icon);
		chessBoard[5][0] = new Bishop("bBishop",5,0,1);//test
		chessBoard[5][0].setImage();
		chessBoard[5][0].icon.setBounds((chessBoard[5][0].x)*70+19,(chessBoard[5][0].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[5][0].icon);
		
		chessBoard[2][7] = new Bishop("wBishop",2,7,0);//test
		chessBoard[2][7].setImage();
		chessBoard[2][7].icon.setBounds((chessBoard[2][7].x)*70+19,(chessBoard[2][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[2][7].icon);
		chessBoard[5][7] = new Bishop("wBishop",5,7,0);//test
		chessBoard[5][7].setImage();
		chessBoard[5][7].icon.setBounds((chessBoard[5][7].x)*70+19,(chessBoard[5][7].y)*70+39,70,70);
		chessBoardpic.add(chessBoard[5][7].icon);
		
		setLastChessBoard(0);
		setLastChessBoard(1);
	}
	
	/* transform the position mouse clicked 
	 * to a corresponding grid on the chess board image */
	private Point determineGrid(int x,int y){ 
		int chessBoardX=24,chessBoardY=41;
		int gridWidth=70,gridHeight=70;
		
		int gx = (x - chessBoardX)/gridWidth;
		int gy = (y - chessBoardY)/gridHeight;
		Point xy = new Point(gx,gy);
		
		return xy;
	}
	
	/* check if this click is on the board
	 * and is clicking on our camp. */
	private boolean isValidClick(Point p){
		return (p.x <=7 && p.y <= 7 && chessBoard[p.x][p.y] != null && chessBoard[p.x][p.y].camp() == nowcamp);
	}
	
	private boolean isAnEnemy(Point p){
		return (chessBoard[p.x][p.y]!=null&&chessBoard[p.x][p.y].camp!=nowcamp);
	}
	
	
	/* Operation of the chess */
	private void moveAChessTo(Point p){
		
		/*
		 * (stateX,stateY) -> (p.x,p.y)
		 */
		
		chessBoard[stateX][stateY].icon.setIcon(null);
		chessBoard[stateX][stateY].moveXY(p.x,p.y);
		chessBoard[p.x][p.y] = chessBoard[stateX][stateY];
		chessBoard[stateX][stateY] = null;
		
		chessBoard[p.x][p.y].setImage();
		chessBoard[p.x][p.y].icon.setBounds((p.x)*70+19,(p.y)*70+39,70,70);
		chessBoardpic.add(chessBoard[p.x][p.y].icon);

	}
	
	private void removeAChess(Point p){

		chessBoard[p.x][p.y].icon.setIcon(null);
		chessBoard[p.x][p.y].setMusicDead();
		chessBoard[p.x][p.y].removed();
	}

	private void clickAChess(Point p){
		
		chessBoard[p.x][p.y].setMusic();
		Stack<Point> moves = chessBoard[p.x][p.y].getReachableGrid(chessBoard);
		colorpanel.showPath(moves);
		chessBoardpic.repaint();
		
		/* stateX,stateY is the (x,y) value of a selected chess. */
		stateX = p.x;
		stateY = p.y;
	}
	
	/* Operation on the state of game */
	private boolean isGameOver(Point p){
		
		if(chessBoard[p.x][p.y] != null && chessBoard[p.x][p.y].isCritical()){
			state = 2;
			GameOver G = new GameOver();
			G.setVisible(true);
			return true;
		}
		return false;
	}
	
	public void mouseClicked(MouseEvent e) {
		
		Point p = determineGrid(e.getX(),e.getY());
		
		/*
		 * state 0 : Select a chess and focus on it.
		 * state 1 : Move the focused chess.
		 * state 2 : Game over. pause the ui.
		 */

		
		if(state == 0 && isValidClick(p)){
			clickAChess(p);
			System.out.println("BOARD:"+this.getPostionString(p.x, p.y)+
					"\tFACT:"+this.getPostionString(chessBoard[p.x][p.y].getX(), chessBoard[p.x][p.y].getY()));			state = 1;		
	
		}else if(state == 1){
			
			colorpanel.clearPath();
			
			if(chessBoard[stateX][stateY].isReachable(chessBoard,p.x,p.y)){
			
			/*is able to move to (px,py) from (sx,sy)*/
				
				if(!isGameOver(p)){
					state = 0;
				}
				
				if(isAnEnemy(p)){
					removeAChess(p);
				}
				moveAChessTo(p);
				setLastChessBoard(nowcamp);
					
				int temp1 = 8 - stateY;
				int temp2 = 8 - p.y;
				history.setText(""+intToChar(stateX)+temp1+" move to "+intToChar(p.x)+temp2);
				panel_1.repaint();
				if(notDual){
					AIstep();
				}	
				else{
					if(nowcamp == 0){
						nowcamp = 1;
						panel_2.setVisible(false);
						panel_3.setVisible(true);
					}
					else{
						nowcamp = 0;
						panel_2.setVisible(true);
						panel_3.setVisible(false);
					}
				}
					
			}else if(chessBoard[p.x][p.y]!=null&&chessBoard[p.x][p.y].camp == chessBoard[stateX][stateY].camp){
				//select another chess.
				clickAChess(p);
				System.out.println("BOARD:"+this.getPostionString(p.x, p.y)+
						"\tFACT:"+this.getPostionString(chessBoard[p.x][p.y].getX(), chessBoard[p.x][p.y].getY()));			}
			else if(stateX == p.x && stateY == p.y){
				//click the same chess again.
				state = 0;
				System.out.println("BOARD:"+this.getPostionString(p.x, p.y)+
						"\tFACT:"+this.getPostionString(chessBoard[p.x][p.y].getX(), chessBoard[p.x][p.y].getY()));
			}
				
		}
	}
	
	private String getPostionString(int x,int y){
		int temp1 = 8 - y;
		return "("+intToChar(x)+","+temp1+")";
	}
	
	private void AIstep(){
		
			chessBoardpic.paintImmediately(chessBoardpic.getBounds());
			chessBoardpic.repaint();
			colorpanel.clearPath();
			boolean GG = false;

			ai.setChessBoard(chessBoard);
			ai.alphaBetaMax(-9999999,9999999, 0);
			Point m = ai.getChoice();
			Point s = ai.getSelected();
			ai.echoCount();
			int temp1 = 8 - s.y;
			int temp2 = 8 - m.y;
			history.setText(""+intToChar(s.x)+temp1+" move to "+intToChar(m.x)+temp2);
			panel_1.repaint();

			if(chessBoard[m.x][m.y] != null){
				removeAChess(new Point(m.x,m.y));
				if(chessBoard[m.x][m.y].isCritical())
					GG = true;
			}


			chessBoard[s.x][s.y].icon.setIcon(null);//clear picture
			chessBoard[s.x][s.y].setFirstStep(false);
			

			
			chessBoard[s.x][s.y].moveXY(m.x,m.y);
			chessBoard[m.x][m.y] = chessBoard[s.x][s.y];
			chessBoard[s.x][s.y] = null;
			
			chessBoard[m.x][m.y].setImage();
			chessBoard[m.x][m.y].icon.setBounds((m.x)*70+19,(m.y)*70+39,70,70);
			chessBoardpic.add(chessBoard[m.x][m.y].icon);
			chessBoardpic.repaint();
			
			if(GG){
				GameOver G = new GameOver();
				G.setVisible(true);
				state = 2;
			}
						
			nowcamp = 0;
			panel_2.setVisible(true);
			panel_3.setVisible(false);
	}
	
	/* �Ω󮬴� */
	private void setLastChessBoard(int ncamp){
		if(nowcamp == 0){
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					wChessBoard[i][j] = chessBoard[i][j];
				}
			}
		}
		else if(nowcamp == 1){
			for(int i = 0; i < 8; i++){
				for(int j = 0; j < 8; j++){
					bChessBoard[i][j] = chessBoard[i][j];
				}
			}
		}
	}	

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void actionPerformed(ActionEvent e){
		state = 0;
		Object item = e.getSource();
		if(item == vsCom){
			ai = new AI(1);
			notDual = true;
		}
		if(item == unDo && state == 0){
			if(nowcamp == 0){
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						if(wChessBoard[i][j] != chessBoard[i][j]){
							if(chessBoard[i][j] != null){
								chessBoard[i][j].icon.setIcon(null);//clear picture
							}
						}
					}
				}
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						if(wChessBoard[i][j] != chessBoard[i][j]){
							chessBoard[i][j] = wChessBoard[i][j];
							if(wChessBoard[i][j] != null){
								chessBoard[i][j].moveXY(i,j);
								chessBoard[i][j].setImage();//
								chessBoard[i][j].icon.setBounds((i)*70+19,(j)*70+39,70,70);
								chessBoardpic.add(chessBoard[i][j].icon);
								chessBoardpic.repaint();
								colorpanel.clearPath();//
								if(wChessBoard[i][j].getName() == "wPawn" && j == 6){
									chessBoard[i][j].setFirstStep(true);
								}
								if(wChessBoard[i][j].getName() == "bPawn" && j == 1){
									chessBoard[i][j].setFirstStep(true);
								}
							}
						}
					}
				}
			}
			else if(nowcamp == 1){
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						if(bChessBoard[i][j] != chessBoard[i][j]){
							if(chessBoard[i][j] != null){
								chessBoard[i][j].icon.setIcon(null);//clear picture
							}
						}
					}
				}
				for(int i = 0; i < 8; i++){
					for(int j = 0; j < 8; j++){
						if(bChessBoard[i][j] != chessBoard[i][j]){
							chessBoard[i][j] = bChessBoard[i][j];
							if(bChessBoard[i][j] != null){
								chessBoard[i][j].moveXY(i,j);
								chessBoard[i][j].setImage();//
								chessBoard[i][j].icon.setBounds((i)*70+19,(j)*70+39,70,70);
								chessBoardpic.add(chessBoard[i][j].icon);
								chessBoardpic.repaint();
								colorpanel.clearPath();//
								if(bChessBoard[i][j].getName() == "bPawn" && j == 1){
									chessBoard[i][j].setFirstStep(true);
								}
								if(bChessBoard[i][j].getName() == "wPawn" && j == 6){
									chessBoard[i][j].setFirstStep(true);
								}
							}
						}
					}
				}
			}
		}
    }
	
	public char intToChar(int inputInt){
		switch(inputInt){
			case 0:
				return 'A';
			case 1:
				return 'B';
			case 2:
				return 'C';
			case 3:
				return 'D';
			case 4:
				return 'E';
			case 5:
				return 'F';
			case 6:
				return 'G';
			case 7:
				return 'H';
			default:
				System.out.println("GO TO DEFAULT");
		}
		return '5';
	}

	private class GameOver extends JFrame implements ActionListener {
		public GameOver() {
			setSize(200, 100);
			setLocation(350,350);
			setLayout(new BorderLayout());
			JLabel confirmLabel = new JLabel("G A M E   O V E R");
			add(confirmLabel, BorderLayout.CENTER);
			JButton exitButton = new JButton("Yes");
			exitButton.addActionListener(this);
			add(exitButton, BorderLayout.SOUTH);
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}