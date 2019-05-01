import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

class player{

	public int drct,x,y,lv,hp,atk,def,gold,exp,Ykey,Bkey,Rkey,fl;
	public JLabel ehp=new JLabel(), eatk=new JLabel(),edef=new JLabel(),php=new JLabel(),patk=new JLabel(),pdef=new JLabel();

	public player()
	{
		lv=1;
		hp=1000;
		atk=10;
		def=10;
		gold=0;
		exp=0;
		Ykey=0;
		Bkey=0;
		Rkey=0;
		fl=0;
		drct=1;
		x=5;
		y=9;
		ehp.setBounds(28,118,300,300);
		ehp.setFont(new Font("Serif",0,30));
		ehp.setForeground(Color.WHITE);
		eatk.setBounds(28,278,300,300);
		eatk.setFont(new Font("Serif",0,30));
		eatk.setForeground(Color.WHITE);
		edef.setBounds(28,438,300,300);
		edef.setFont(new Font("Serif",0,30));
		edef.setForeground(Color.WHITE);
		php.setBounds(688,118,300,300);
		php.setFont(new Font("Serif",0,30));
		php.setForeground(Color.WHITE);
		patk.setBounds(688,278,300,300);
		patk.setFont(new Font("Serif",0,30));
		patk.setForeground(Color.WHITE);
		pdef.setBounds(688,438,300,300);
		pdef.setFont(new Font("Serif",0,30));
		pdef.setForeground(Color.WHITE);
		mota.battleFrame.add(ehp,2,0);
		mota.battleFrame.add(eatk,3,0);
		mota.battleFrame.add(edef,4,0);
		mota.battleFrame.add(php,5,0);
		mota.battleFrame.add(patk,6,0);
		mota.battleFrame.add(pdef,7,0);
	}

	public void move(int cx, int cy)
	{
		x=cx;y=cy;
	}

	JLabel temp=new JLabel();
	public void battle(enemy e)
	{
		try{temp=new JLabel(new ImageIcon(e.draw()));}catch(IOException ex){};
		temp.setBounds(55,70,72,72);
		mota.battleFrame.add(temp,8,0);
		ehp.setText(e.hp+"");
		eatk.setText(e.atk+"");
		edef.setText(e.def+"");
		php.setText(hp+"");
		patk.setText(atk+"");
		pdef.setText(def+"");
		mota.battleFrame.setVisible(true);
		mota.inConversation=true;
		Timer battleFrame=new Timer(500,new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent ex)
				{
					attack(e);
					ehp.setText(e.hp+"");
					php.setText(hp+"");
					mota.fr.repaint();
					if(hp<=0 || e.hp<=0)
					{
						mota.battleFrame.setVisible(false);
						mota.inConversation=false;
						exp+=e.exp;
						gold+=e.gold;
						mota.displayMessage("Recieve "+e.exp+" exp and "+e.gold+" gold");
						mota.battleFrame.remove(temp);
						((Timer)ex.getSource()).stop();
					}
				}
			});
		battleFrame.start();
	}

	public String forecast(enemy e)
	{
		if(atk<=e.def)
			return "???";
		else if (def>=e.atk)
			return 0+"";
		else
		{
			//int one = (e.hp/(atk-e.def))-1; //how many times enemy will strike before it dies
			return ((e.hp/(atk-e.def))*(e.atk-def))+"";
		}
	}

	public void attack(enemy e)
	{
		if(atk>e.def)
		e.hp=e.hp-atk+e.def;
		if(e.hp<=0) return;
		if(e.atk>def)
		hp=hp-e.atk+def;
		if(atk<e.def && e.atk<def) return;
	}

	public BufferedImage draw() throws IOException
	{
		if(drct==0)
			return ImageIO.read(new File(System.getProperty("user.dir")+"/player_left.png"));
		if(drct==1)
			return ImageIO.read(new File(System.getProperty("user.dir")+"/player_front.png"));
		if(drct==2)
			return ImageIO.read(new File(System.getProperty("user.dir")+"/player_right.png"));
		if(drct==3)
			return ImageIO.read(new File(System.getProperty("user.dir")+"/player_back.png"));
		return null;
	}

	public BufferedImage drawFront() throws IOException
	{
		return ImageIO.read(new File(System.getProperty("user.dir")+"/player_front.png"));
	}
}

class sprite{

	int id;

	public BufferedImage draw() throws IOException
	{
		if(this instanceof enemy)
		return mota.enemyImgSheet.getSubimage(72*id,0,72,72);
		else if(this instanceof dialogue)
		return mota.dialogueImgSheet.getSubimage(72*id,0,72,72);
		else
		return mota.itemImgSheet.getSubimage(72*id,0,72,72);
	}
}

class enemy extends sprite{
	int hp,atk,def,gold,exp;
	public enemy (int name)
	{
		id=name;
		hp=(int)mota.enemyDict[name][1];
		atk=(int)mota.enemyDict[name][2];
		def=(int)mota.enemyDict[name][3];
		gold=(int)mota.enemyDict[name][4];
		exp=(int)mota.enemyDict[name][5];
	}
}

class item extends sprite{
	public item(int index)
	{
		id=index;
	}
	public void use()
	{
		switch(id)
		{
			case 0: mota.player_1.Ykey++; mota.displayMessage("Yellow Key+1"); break;
			case 1: mota.player_1.Bkey++; mota.displayMessage("Blue Key+1"); break;
			case 2: mota.player_1.Rkey++; mota.displayMessage("Red Key+1"); break;
			case 3: mota.player_1.hp+=200; mota.displayMessage("Obtain Red Potion! hp+200"); break;
			case 4: mota.player_1.hp+=500; mota.displayMessage("Obtain Blue Potion! hp+500"); break;
			case 5: mota.player_1.atk+=3; mota.displayMessage("Obtain Red Crystal! atk+3"); break;
			case 6: mota.player_1.def+=3; mota.displayMessage("Obtain Blue Crystal! def+3"); break;
			case 7: mota.player_1.atk+=10; mota.displayMessage("Obtain Iron Sword! atk+10"); break;
			case 8: mota.player_1.def+=10; mota.displayMessage("Obtain Iron Shield! def+10"); break;
			case 9: mota.player_1.def+=85; mota.displayMessage("Obtain Knight Shield! def+85"); break;
			case 10: mota.player_1.Ykey++; mota.player_1.Bkey++;mota.player_1.Rkey++; mota.displayMessage("Recieve the Magic Key! All Keys+1");break;
			case 11: mota.player_1.lv+=3; mota.player_1.hp+=3000; mota.player_1.atk+=21; mota.player_1.def+=21; mota.displayMessage("Recieve The Feather! Level+3"); break;
			case 12: mota.player_1.gold+=300; mota.displayMessage("Recieve the Lucky Coin! Gold+300");break;
			case 13: mota.hasCross=true; mota.displayMessage("You find the Cross! Take it to the elf to recieve power. "); break;
			case 14: mota.fly=true; mota.displayMessage("You find the snowflake! Press J to see detail "); break;
			case 15: mota.hasPickaxe=true; mota.displayMessage("You find the Pickaxe! Take it to the thief. ");break;
			case 16: mota.player_1.hp=mota.player_1.hp*2; mota.displayMessage("You find the elixir! Double the current HP ");break;
			case 17: mota.forecast=true; mota.displayMessage("Obtain the Orb of Hero! Press L to see detail "); break;
			case 18: mota.player_1.atk+=70; mota.displayMessage("Obtain Knight Sword! atk+70"); break;
			case 19: mota.player_1.atk+=110; mota.displayMessage("Obtain Holy Sword! atk+110"); break;
			case 20: mota.player_1.def+=120; mota.displayMessage("Obtain Holy Shield! def+120"); break;
		}
	}
}

class dialogue extends sprite{
	public dialogue(int index)
	{
		id=index;
	}
	public void perform() throws IOException
	{
		String[] messages;
		BufferedImage[] characters= new BufferedImage[50];
		int[] w= new int[50];
		int[] h= new int[50];
		for(int x=0;x<50;x++)
		{
			w[x]=530;
			h[x]=170;
		}
		for(int x=0;x<characters.length;x++)
		{
			if(x%2==0)
				characters[x]=ImageIO.read(new File(System.getProperty("user.dir")+"/player_front.png"));
			else characters[x]=this.draw();
		}
		switch (id)
		{
			case 0:
				messages=new String[]{". . . . . .",
				"you waked up!",
				". . .\nwho am I? Where am I?",
				"I am the fairy of this place. You've been knocked down by the monster here.",
				". . .\nSword, sword, where is my sword?",
				"The evil monsters had taken your sword. I only had enough time to save you.",
				"Where is the princess, then? I am here to rescue the princess.",
				"The princess in in this tower. But right now you do not have enough power to defeat the monsters there.",
				"What should I do, then? I promised the king to rescue the princess. What should I do now?",
				"Don't worry, brave hero. I can borrow you my power so you can beat the monster in the tower. However, you must find me something first.",
				"Find something? What thing?",
				"It's a treasure, with a red ruby in the middle.",
				"What does it do?",
				"I was the guard of this tower. But not long ago the monster army from the north invaded. They occuipied the tower, and lock my power in this cross. If you can bring it to me, I will be able to recover my power and give them to you.",
				". . .\nOkay, I will give it a try.",
				"Your sword is at the 3rd floor, your shield is at the 5th floor, and the cross is at the 7th floor. And also, there are many other legendary treasures at higher floor. They will be a great help to you.",
				". . .\nBut, how should I get in?",
				"I have three keys here. Take them. There are more keys in the tower. Use them wisely. Good luck, brave hero!"};
				h[9]=300;
				h[13]=330;
				h[15]=330;
				h[17]=200;
				mota.talk(messages,characters,w,h);
				mota.spriteMap[0][4][8]=new dialogue(1);
       			mota.spriteMap[0][5][8]=null;
       			mota.player_1.Ykey++;
       			mota.player_1.Bkey++;
       			mota.player_1.Rkey++; break;
			case 1:
				if(!mota.hasCross)
				{mota.inConversation=false; return;}
				String[] messages1={"You brought the cross back! I will now grant you my power. . ."};
				BufferedImage[] characters1= {this.draw()};
				mota.talk(messages1,characters1,w,h);
				mota.player_1.hp=mota.player_1.hp*4/3;
				mota.player_1.atk=mota.player_1.atk*4/3;
				mota.player_1.def=mota.player_1.def*4/3;
				mota.spriteMap[0][4][8]=null; break;
			case 2:
				mota.player_1.atk+=30;
				mota.spriteMap[2][7][10]=null;
				mota.fr.repaint();
				mota.inConversation=false; break;
			case 3:
				mota.player_1.def+=30;
				mota.spriteMap[2][9][10]=null;
				mota.fr.repaint();
				mota.inConversation=false; break;
			case 4: mota.store(0); break;
			case 5: mota.store(0); break;
			case 6: mota.store(0); break;
			case 7: mota.store(1); break;
			case 8: mota.store(2); break;
			case 9: mota.store(3); break;
			case 10: mota.store(3); break;
			case 11: mota.store(3); break;
			case 12:
				messages =new String[]{"You are saved! ",
						"My name is Alan Zhu and I am a thief. I was captured by the evil while trying to sneak in the tower. ",
						"No matter who you are, you are freed. Now leave the tower. I am going to rescue the princess.",
						"No, I won't leave because you will need me. As a repay, I will open the gate at LV2 for you. If you can bring me my pickaxe I will be able to open lv 18 floor"};
				h[3]=330;
				mota.talk(messages,characters,w,h);
				mota.LvMap[2][6][1]=1;
       			mota.spriteMap[4][5][0]=new dialogue(13); break;
       		case 13:
       			if(!mota.hasPickaxe)
				{
					mota.inConversation=false;
					return;
				}
       			messages= new String[]{"Is this the pickaxe you're talking about?", "You brought me the pickaxe! I will open the pathway for you. Good Bye!"};
       			mota.talk(messages,characters,w,h);
       			mota.spriteMap[4][5][0]=null;
       			mota.LvMap[18][8][5]=1;
				mota.LvMap[18][9][5]=1; break;
			case 14: mota.store(4); break;
			case 15: mota.store(5); break;
			case 16:
				messages=new String[]{"If you have 500 EXP, I will give you a legendary sword."};
				characters[0]=this.draw();
				mota.talk(messages,characters,w,h);
				if(mota.player_1.exp>=500)
				{
				    mota.player_1.exp-=500;
					mota.player_1.atk+=110;
					mota.spriteMap[15][4][3]=null;} break;
			case 17:
				messages=new String[]{"If you have 500 GOLD, I will give you a legendary shield."};
				characters[0]=this.draw();
				mota.talk(messages,characters,w,h);
				if(mota.player_1.gold>=500) {
                mota.player_1.gold-=500;
                mota.player_1.def+=120;
				mota.spriteMap[15][4][5]=null;} break;
			case 18:
				messages=new String[]{"Tell me, where is the princess!",
						"Shut up, mortal. Let's fight!"};
                characters[0]=new enemy(29).draw();
				mota.talk(messages,characters,w,h);
				mota.spriteMap[16][5][4]=null; break;
			case 19:
				if(mota.talked)
				{
					mota.inConversation=false;
					return;
				}
				messages=new String[]{"Dear princess, you are saved! ",
						"Oh, brave hero. Did you come to save me? ",
						"Yes, I am. I followed the King's command to rescue you. Now let's leave the tower! ",
						"No, I don't want to leave yet. The demon lord is still alive. The evilness still remains in the tower will be a great threat to our kingdom. ",
						"The demon lord? I already killed him! ",
						"No, the one you defeated was not the real one. The real demon lord is at level 21. ",
						"Fine, I will come back to rescue you if I defeat him. ",
						"Please defeat the evilness for us! Good luck, brave hero!"
				};
				h[3]=330;
				mota.talk(messages, characters, w,h);
				mota.LvMap[18][10][10]=8;
				mota.talked=true; break;
			case 20:
				messages=new String[]{"This is impossible! Nooooooooo.... "};
				characters[0]=new enemy(32).draw();
				mota.talk(messages, characters, w,h);
				mota.inConversation=true;
				JOptionPane.showMessageDialog(mota.fr,"恭喜通关！您的通关时间是："+mota.gameMin+" 分, "+(int)mota.gameSec+" 秒");
				PrintWriter pr= new PrintWriter(new BufferedWriter(new FileWriter("通关记录.txt", true)));
				pr.println(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date())+"  "+mota.gameMin+" 分, "+(int)mota.gameSec+" 秒");
				pr.close();
				mota.spriteMap[21][5][1]=null; break;
		}
	}
}

public class mota extends JPanel{

    static JPanel game;
    static JFrame fr= new JFrame("The Tower of Sorcerer");
    static int currentFloor=0;
    static int maxFloor=0;
    static int gameMin=0;
    static double gameSec=0;
    static JLayeredPane forecastFrame= new JLayeredPane(), jumpFrame= new JLayeredPane(), battleFrame= new JLayeredPane();
    static JLayeredPane msgPane=new JLayeredPane();
	static JLabel msg=new JLabel();
	static JLabel time;
    static BufferedImage imgSheet, enemyImgSheet, itemImgSheet,dialogueImgSheet;
    static BufferedImage background;
    static player player_1=new player();
    static boolean inConversation=false;
    static boolean hasCross=false;
    static boolean hasPickaxe=false;
    static boolean talked= false;
    static boolean fly=false;
    static boolean forecast=false;
    static Object[][] enemyDict= new Object[33][6];
    static int[][][] LvMap={
    	{
    	{2,4,4,4,4,8,4,4,4,4,2},
    	{2,4,4,4,4,1,4,4,4,4,2},
    	{2,4,4,4,4,1,4,4,4,4,2},
    	{2,4,4,4,4,1,4,4,4,4,2},
    	{2,4,4,4,4,1,4,4,4,4,2},
    	{2,4,4,4,4,1,4,4,4,4,2},
    	{2,2,4,4,4,1,4,4,4,2,2},
    	{2,2,2,2,2,5,2,2,2,2,2},
    	{3,2,3,2,1,1,1,2,3,2,3},
    	{3,3,3,3,3,1,3,3,3,3,3},
    	{3,3,3,3,3,1,3,3,3,3,3},
    	},
    	{
    	{8,1,1,1,1,1,1,1,1,1,1},
    	{2,2,2,2,2,2,2,2,2,2,1},
    	{1,1,1,5,1,2,1,1,1,2,1},
    	{1,1,1,2,1,2,1,1,1,2,1},
    	{2,5,2,2,1,2,2,2,1,2,1},
    	{1,1,1,2,1,5,1,1,1,2,1},
    	{1,1,1,2,1,2,2,2,2,2,1},
    	{2,5,2,2,1,1,1,1,1,1,1},
    	{1,1,1,2,2,7,2,2,2,5,2},
    	{1,1,1,2,1,1,1,2,1,1,1},
    	{1,1,1,2,1,9,1,2,1,1,1},
    	},
    	{
    	{9,2,1,1,1,2,1,1,1,1,2},
    	{1,2,1,2,1,2,1,1,1,1,2},
    	{1,2,1,2,1,2,1,1,1,1,2},
    	{1,2,1,2,1,2,2,2,2,5,2},
    	{1,2,1,2,1,1,1,5,1,1,2},
    	{1,2,5,2,2,5,2,2,5,2,2},
    	{1,11,1,1,1,1,2,1,1,1,2},
    	{1,2,5,2,2,6,2,10,2,10,2},
    	{1,2,1,2,1,1,2,1,2,1,2},
    	{1,2,1,2,1,1,2,1,2,1,2},
    	{8,2,1,2,1,1,2,1,2,1,2},
    	},
    	{
    	{1,1,1,2,1,1,1,2,2,2,2},
    	{1,1,1,2,1,1,1,2,1,1,1},
    	{1,1,1,2,2,5,2,2,1,2,1},
    	{2,5,2,2,1,1,1,2,1,2,1},
    	{1,1,1,2,2,2,1,2,1,2,1},
    	{1,2,1,1,1,1,1,2,1,2,1},
    	{1,2,2,2,2,2,1,1,1,2,1},
    	{1,1,1,1,1,2,2,5,2,2,1},
    	{2,2,2,2,1,2,1,1,1,2,1},
    	{2,1,1,1,1,2,1,1,1,2,1},
    	{9,1,2,2,2,2,1,1,1,2,8},
    	},
    	{
    	{1,1,1,2,1,1,1,2,1,1,1},
    	{5,2,5,2,1,1,1,2,5,2,5},
    	{1,2,1,2,2,10,2,2,1,2,1},
    	{1,2,1,2,1,1,1,2,1,2,1},
    	{1,2,1,2,1,1,1,2,1,2,1},
    	{1,2,1,2,2,7,2,2,1,2,1},
    	{1,2,1,2,1,1,1,2,1,2,1},
    	{1,2,1,2,1,1,1,2,1,2,1},
    	{1,2,1,2,2,6,2,2,1,2,1},
    	{1,2,1,2,1,1,1,2,1,2,1},
    	{8,2,1,1,1,1,1,1,1,2,9},
    	},
    	{
    	{1,2,1,2,1,1,1,1,1,1,1},
    	{1,2,1,2,1,1,1,1,1,1,1},
    	{1,2,1,2,1,1,2,2,5,2,2},
    	{1,5,1,2,1,1,2,1,1,1,1},
    	{1,2,1,2,2,2,2,1,1,1,1},
    	{1,2,1,1,1,1,1,1,1,1,1},
    	{1,2,2,1,2,2,2,2,1,1,1},
    	{1,1,2,1,2,1,1,1,1,1,1},
    	{2,2,2,1,2,5,2,6,2,5,2},
    	{1,1,2,1,2,1,2,1,5,1,2},
    	{9,1,1,1,1,1,2,1,2,8,2},
    	},
    	{
    	{1,1,2,1,2,1,1,1,2,1,1},
    	{1,1,2,1,2,1,1,1,2,1,1},
    	{1,1,6,1,6,1,1,1,2,1,1},
    	{1,1,2,1,2,1,1,1,2,1,1},
    	{2,2,2,7,2,2,2,2,2,5,2},
    	{1,1,1,1,1,1,1,1,1,1,1},
    	{1,2,2,2,2,2,2,2,2,2,2},
    	{1,2,1,5,1,1,1,1,1,1,2},
    	{1,2,5,2,5,2,2,2,2,6,2},
    	{1,2,1,2,1,1,2,2,1,1,2},
    	{1,1,1,2,8,1,5,5,1,9,2},
    	},
    	{
    	{8,1,1,1,1,1,1,1,2,2,2},
    	{2,2,1,1,2,6,2,1,1,2,2},
    	{2,1,1,1,2,1,2,1,1,1,2},
    	{1,1,2,2,2,10,2,2,2,1,1},
    	{1,1,6,1,10,1,10,1,6,1,1},
    	{1,2,2,2,2,10,2,2,2,2,1},
    	{1,2,1,1,2,1,2,1,1,2,1},
    	{1,2,1,1,2,6,2,1,1,2,1},
    	{1,2,2,1,1,1,1,1,2,2,1},
    	{1,1,2,2,2,7,2,2,2,1,1},
    	{2,1,1,5,9,1,1,1,1,1,2},
    	},
    	{
    	{9,2,1,1,1,1,2,1,1,1,1},
    	{1,2,1,2,2,5,2,5,2,2,1},
    	{1,2,1,2,1,1,6,1,1,2,1},
    	{1,2,1,2,1,2,2,2,1,2,1},
    	{1,2,1,2,1,2,8,1,1,2,1},
    	{1,2,1,2,1,2,2,2,2,2,1},
    	{1,2,1,2,1,1,1,2,1,1,1},
    	{1,2,1,2,2,2,1,2,5,2,2},
    	{1,2,1,1,1,2,1,2,1,1,1},
    	{1,2,2,2,5,2,1,2,2,2,1},
    	{1,1,1,1,1,2,1,1,1,1,1},
    	},
    	{//9
    	{1,1,1,2,2,2,1,1,1,2,1},
    	{1,1,1,5,1,1,1,2,1,5,1},
    	{2,5,2,2,1,2,2,2,1,2,1},
    	{1,1,1,2,1,2,1,1,1,2,1},
    	{1,1,1,7,1,2,9,2,1,2,1},
    	{2,6,2,2,1,2,2,2,1,2,2},
    	{1,1,1,2,1,2,8,2,1,2,1},
    	{2,5,2,2,1,1,1,5,1,2,1},
    	{1,1,1,2,2,6,2,2,1,2,1},
    	{1,1,1,2,1,1,1,2,1,5,1},
    	{1,1,1,5,1,1,1,2,1,2,1},
    	},
    	{
    	{1,2,2,1,1,2,1,1,2,2,1},//10
    	{1,1,2,2,5,2,5,2,2,1,1},
    	{1,1,1,1,1,2,1,1,1,1,1},
    	{1,2,1,2,2,2,2,2,1,2,2},
    	{1,2,1,1,1,1,1,1,1,2,1},
    	{1,2,1,2,2,2,2,5,2,2,1},
    	{1,2,1,10,1,9,2,1,5,1,1},
    	{1,2,2,2,2,2,2,5,2,2,1},
    	{1,2,1,1,1,2,1,1,1,2,1},
    	{1,2,1,1,1,7,1,2,1,2,1},
    	{8,2,1,1,1,2,1,2,1,2,1},
    	},
    	{
    	{1,2,1,2,1,2,1,2,1,1,1},//11
    	{1,2,1,2,1,2,1,2,1,1,1},
    	{1,2,1,2,1,2,1,2,1,1,1},
    	{5,2,5,2,5,2,5,2,2,6,2},
    	{1,1,1,1,1,2,1,1,1,1,1},
    	{5,2,2,6,2,2,2,6,2,2,5},
    	{1,2,1,1,1,1,1,1,1,2,1},
    	{1,2,1,2,2,2,2,2,1,2,1},
    	{1,2,1,2,1,1,1,2,1,2,1},
    	{2,2,7,2,1,1,1,2,7,2,2},
    	{9,1,1,1,1,1,1,1,1,1,8},
    	},
    	{//12
    	{1,1,2,1,1,1,1,1,2,1,1},
    	{1,1,2,1,2,5,2,1,2,1,1},
    	{1,1,2,1,2,1,2,1,2,1,1},
    	{1,1,2,1,2,1,2,1,2,1,1},
    	{1,1,2,1,2,1,2,1,2,1,1},
    	{2,6,2,1,2,1,2,1,2,6,2},
    	{1,1,1,1,2,1,2,1,1,1,1},
    	{2,2,2,1,2,2,2,1,2,2,2},
    	{1,1,5,1,1,1,1,1,5,1,1},
    	{2,2,2,2,2,6,2,2,2,2,2},
    	{8,1,1,1,1,1,1,1,1,1,9},
    	},
    	{//13
    	{1,1,1,1,1,1,1,2,1,1,1},
    	{1,2,2,2,2,2,5,2,1,2,1},
    	{1,2,1,1,1,1,1,2,1,2,1},
    	{1,2,7,2,2,2,1,2,1,2,1},
    	{1,2,1,1,1,2,1,2,1,2,1},
    	{1,2,1,1,10,2,1,2,1,2,1},
    	{1,2,1,10,1,2,1,2,1,2,1},
    	{1,2,2,2,2,2,1,2,1,2,1},
    	{1,1,1,2,1,1,1,1,1,2,1},
    	{2,2,1,2,1,2,2,2,2,2,1},
    	{9,1,1,6,1,8,2,1,1,5,1},
    	},
    	{//14
    	{2,1,1,1,8,1,1,1,1,1,2},
    	{2,1,1,2,2,2,2,2,1,1,2},
    	{2,1,2,2,2,2,2,2,2,1,2},
    	{2,1,2,2,2,1,2,2,2,1,2},
    	{2,1,2,2,2,10,2,2,2,1,2},
    	{2,1,1,2,2,1,2,2,1,1,2},
    	{2,1,4,4,2,1,2,4,4,1,2},
    	{2,1,4,4,2,1,2,4,4,1,2},
    	{2,1,4,4,2,6,2,4,4,1,2},
    	{2,1,1,1,6,1,6,1,1,1,2},
    	{2,2,2,2,2,9,2,2,2,2,2},
    	},
    	{//15
    	{1,1,1,1,9,4,8,1,1,1,1},
    	{1,4,4,4,4,4,4,4,4,4,1},
    	{1,4,4,2,2,2,2,2,4,4,1},
    	{1,4,2,2,1,2,1,2,2,4,1},
    	{1,4,2,2,1,2,1,2,2,4,1},
    	{1,4,2,2,1,2,1,2,2,4,1},
    	{1,4,4,2,1,2,1,2,4,4,1},
    	{1,4,4,2,5,2,5,2,4,4,1},
    	{1,4,4,4,1,1,1,4,4,4,1},
    	{1,4,4,4,4,7,4,4,4,4,1},
    	{1,1,1,1,1,1,1,1,1,1,1},
    	},
    	{//16
    	{4,4,4,4,4,1,9,4,4,4,4},
    	{4,4,4,4,4,1,4,4,4,4,4},
    	{4,4,4,4,4,1,4,4,4,4,4},
    	{4,4,4,4,2,7,2,4,4,4,4},
    	{4,4,4,2,2,1,2,2,4,4,4},
    	{4,4,4,2,2,1,2,2,4,4,4},
    	{4,4,4,2,2,1,2,2,4,4,4},
    	{4,4,4,2,2,1,2,2,4,4,4},
    	{4,4,4,2,2,8,2,2,4,4,4},
    	{4,4,4,4,2,2,2,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	},
    	{//17
    	{4,1,1,1,1,1,1,1,1,1,1},
    	{4,1,4,4,4,4,4,4,4,4,1},
    	{4,1,4,1,1,1,1,1,1,1,1},
    	{4,1,4,1,4,4,4,4,4,4,4},
    	{4,1,4,1,4,1,1,1,1,1,4},
    	{4,1,4,1,1,1,4,4,4,1,4},
    	{4,1,4,4,4,4,4,1,1,1,4},
    	{4,1,4,4,4,9,4,1,4,4,4},
    	{4,1,1,1,1,1,4,1,1,1,1},
    	{4,4,4,4,4,4,4,4,4,4,1},
    	{8,1,1,1,1,1,1,1,1,1,1},
    	},
    	{//18
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,4,4,2,2,2,4,4,4,4},
    	{4,4,4,2,2,1,2,2,4,4,4},
    	{4,4,4,2,2,10,2,2,4,4,4},
    	{4,4,4,2,2,7,2,2,4,4,4},
    	{4,4,4,4,2,7,2,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{9,1,1,1,1,1,1,1,1,1,1},
    	},
    	{//19
    	{1,1,1,1,1,1,1,1,1,1,1},
    	{1,4,1,4,4,4,4,4,1,4,1},
    	{1,4,1,4,4,4,4,4,1,4,1},
    	{1,4,1,4,4,8,4,4,1,4,1},
    	{1,4,1,4,4,1,4,4,1,4,1},
    	{1,4,1,4,4,1,4,4,1,4,1},
    	{1,4,10,4,4,1,4,4,10,4,1},
    	{1,4,1,4,4,1,4,4,1,4,1},
    	{1,4,4,4,4,1,4,4,4,4,1},
    	{1,4,4,4,4,1,4,4,4,4,1},
    	{1,1,1,1,1,1,1,1,1,1,9},
    	},
    	{//20
    	{1,1,1,1,1,1,1,1,1,1,1},
    	{1,4,1,4,1,4,1,4,1,4,1},
    	{4,1,1,1,1,1,1,1,1,1,4},
    	{1,4,1,4,1,9,1,4,1,4,1},
    	{1,1,1,1,1,1,1,1,1,1,1},
    	{1,4,1,4,1,4,1,4,1,4,1},
    	{1,1,1,1,1,1,1,1,1,1,1},
    	{1,4,1,4,1,8,1,4,1,4,1},
    	{4,1,1,1,1,1,1,1,1,1,4},
    	{1,4,1,4,1,4,1,4,1,4,1},
    	{1,1,1,1,1,1,1,1,1,1,1},
    	},
    	{//21
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,1,1,4,1,4,1,1,4,4},
    	{4,1,1,4,4,1,4,4,1,1,4},
    	{4,1,1,1,4,1,4,1,1,1,4},
    	{4,4,1,1,1,1,1,1,1,4,4},
    	{4,4,1,1,1,1,1,1,1,4,4},
    	{4,4,4,1,1,4,1,1,4,4,4},
    	{4,4,4,4,11,9,11,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	{4,4,4,4,4,4,4,4,4,4,4},
    	},
    };
    static sprite[][][] spriteMap=new sprite[22][11][11];
    static int[][] initPos = {
    	{5,9},
    	{5,9},
    	{0,1},
    	{1,10},
    	{10,9},
    	{1,10},
    	{9,9},
    	{5,10},
    	{0,1},
    	{6,3},
    	{4,6},
    	{1,10},
    	{9,10},
    	{1,10},
    	{5,9},
    	{3,0},
    	{5,0},
    	{5,8},
    	{1,10},
    	{9,10},
    	{5,4},
    	{5,5},
    };
    static int[][] finPos=
    {
    	{5,1},
    	{1,0},
    	{0,9},
    	{10,9},
    	{0,9},
    	{9,9},
    	{5,10},
    	{1,0},
    	{7,4},
    	{6,7},
    	{0,9},
    	{9,10},
    	{1,10},
    	{4,10},
    	{5,0},
    	{7,0},
    	{5,7},
    	{1,10},
    	{9,10},
    	{5,4},
    	{5,8},
    	{},
    };

    public mota() throws IOException
    {
    	setLayout(null);

    	//initializing timeFrame
    	time=new JLabel();
    	time.setBounds(80,800,250,100);
    	time.setForeground(Color.WHITE);
    	time.setFont(new Font("Serif",0,25));

    	//initializing dialogue system
        conversation.setLayout(null);
        dialogueBackground.setIcon(new ImageIcon(ImageIO.read(new File(System.getProperty("user.dir")+"/dialogueBackground.png"))));
		conversation.add(dialogueBackground,1,0);
        dialogueBackground.setBorder(BorderFactory.createLineBorder(new Color(204,102,0),8,true));
		text.setForeground(Color.WHITE);
    	text.setFont(new Font("Serif",0,30));
    	text.setWrapStyleWord(true);
    	text.setLineWrap(true);
    	text.setOpaque(false);
    	text.setEditable(false);
    	text.setFocusable(false);

    	//initializing forecast frame
    	forecastFrame.setLayout(null);
		forecastFrame.setBounds(6*72,72,72*11,72*11);
		forecastFrame.setBackground(Color.BLACK);
		forecastFrame.setOpaque(true);
		forecastFrame.setVisible(false);

		//initializing jump frame
    	jumpFrame.setLayout(null);
		jumpFrame.setBounds(7*72,2*72,72*9,72*9);
        jumpFrame.setBorder(BorderFactory.createLineBorder(new Color(204,102,0),8,true));
		jumpFrame.setBackground(Color.BLACK);
		jumpFrame.setOpaque(true);
		jumpFrame.setVisible(false);

		//initializing battle frame
    	battleFrame.setLayout(null);
		battleFrame.setBounds(170,72*2,949,681);
		JLabel battleBackground= new JLabel(new ImageIcon(ImageIO.read(new File(System.getProperty("user.dir")+"/battleFrame.png"))));
		battleBackground.setBounds(0,0,989,681);
		battleFrame.add(battleBackground,1,0);
		battleFrame.setOpaque(true);
		battleFrame.setVisible(false);

		//initializing msg pane
		msgPane.setLayout(null);
		msgPane.setBounds(10,270,72*18-20,150);
		msg.setBounds(0,0,72*18-20,150);
		msg.setForeground(Color.WHITE);
    	msg.setFont(new Font("Serif",0,50));
    	msg.setHorizontalAlignment(SwingConstants.CENTER);
    	JLabel msgBackground= new JLabel(new ImageIcon(ImageIO.read(new File(System.getProperty("user.dir")+"/dialogueBackground.png"))));
    	msgBackground.setLayout(null);
		msgBackground.setBounds(0,0,72*18-20,150);
		msgPane.add(msgBackground,1,0);
		msgPane.add(msg,2,0);
		msgPane.setOpaque(true);
		msgPane.setVisible(false);

    	//initializing map
    	background= ImageIO.read(new File(System.getProperty("user.dir")+"/background.png"));
    	imgSheet=ImageIO.read(new File(System.getProperty("user.dir")+"/property.png"));
    	enemyImgSheet=ImageIO.read(new File(System.getProperty("user.dir")+"/enemyProperty.png"));
    	itemImgSheet=ImageIO.read(new File(System.getProperty("user.dir")+"/itemProperty.png"));
    	dialogueImgSheet=ImageIO.read(new File(System.getProperty("user.dir")+"/dialogueProperty.png"));
    	BufferedReader br= new BufferedReader(new FileReader("enemy.dat"));
    	for(int x=0;x<enemyDict.length;x++)
    	{
    		enemyDict[x][0]=br.readLine();
    		for(int y=1;y<6;y++)
    		{
    			enemyDict[x][y]=Integer.parseInt(br.readLine());
    		}
    	}
    	BufferedReader br1= new BufferedReader(new FileReader("enemyMap.dat"));
    	String line="";
    	while((line=br1.readLine())!=null)
    	{
    		String[] temp = line.split(" ");
    			mota.spriteMap[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])][Integer.parseInt(temp[2])]=new enemy(Integer.parseInt(temp[3]));
    	}
    	br1.close();
    	BufferedReader br2= new BufferedReader(new FileReader("itemMap.dat"));
    	while((line=br2.readLine())!=null)
    	{
    		String[] temp = line.split(" ");
    		mota.spriteMap[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])][Integer.parseInt(temp[2])]=new item(Integer.parseInt(temp[3]));
    	}
    	br2.close();
    	BufferedReader br3= new BufferedReader(new FileReader("dialogueMap.dat"));
    	while((line=br3.readLine())!=null)
    	{
    		String[] temp = line.split(" ");
    		mota.spriteMap[Integer.parseInt(temp[0])][Integer.parseInt(temp[1])][Integer.parseInt(temp[2])]=new dialogue(Integer.parseInt(temp[3]));
    	}
    	br3.close();

    	//initializing animation
    	new Timer(500,new ActionListener(){
    		boolean change=true;
    		BufferedImage one=ImageIO.read(new File(System.getProperty("user.dir")+"/property.png"));
    		BufferedImage two=ImageIO.read(new File(System.getProperty("user.dir")+"/property2.png"));
    		BufferedImage three=ImageIO.read(new File(System.getProperty("user.dir")+"/enemyProperty.png"));
    		BufferedImage four=ImageIO.read(new File(System.getProperty("user.dir")+"/enemyProperty2.png"));
    		BufferedImage five=ImageIO.read(new File(System.getProperty("user.dir")+"/dialogueProperty.png"));
    		BufferedImage six=ImageIO.read(new File(System.getProperty("user.dir")+"/dialogueProperty2.png"));
    		@Override
    		public void actionPerformed(ActionEvent e)
    		{
    		    mota.gameSec+=0.5;
    		    if(gameSec==60)
                {
                    gameSec=0;
                    gameMin++;
                }
    		    mota.time.setText(" 游戏时间："+gameMin+" 分, "+(int)gameSec+" 秒");
    			if(change)
    			{
    				change=false;
    				imgSheet=one;
    				enemyImgSheet=three;
    				dialogueImgSheet=five;
    			}
    			else{
    				change=true;
    				imgSheet=two;
    				enemyImgSheet=four;
    				dialogueImgSheet=six;
    			}
    			repaint();
    		}
    	}).start();
    }

    @Override
    public void paintComponent(Graphics g)
    {
    	Graphics2D g2= (Graphics2D) g;
    	g2.drawImage(background,0,0,null);
    	for(int x=0;x<11;x++)
    	for(int y=0;y<11;y++)
    		g2.drawImage(imgSheet.getSubimage(72*LvMap[currentFloor][x][y],0,72,72),72*y+72*6,72*x+72,null);

    	try{
    		g2.drawImage(player_1.draw(),(player_1.x+6)*72,(player_1.y+1)*72,null);
    		for(int x=0;x<spriteMap[currentFloor].length;x++)
    		{
    			for(int y=0;y<spriteMap[currentFloor].length;y++)
    			{
    				if(spriteMap[currentFloor][x][y]!=null)
    				g2.drawImage(spriteMap[currentFloor][x][y].draw(),(x+6)*72,(y+1)*72,null);
    			}
    		}
    	}catch(IOException e){};

    	g2.setFont(new Font("Arial",0,30));
    	g2.setColor(Color.WHITE);
    	g2.drawString(player_1.lv+"",280,160);
    	g2.drawString(player_1.hp+"",220,215);
    	g2.drawString(player_1.atk+"",220,270);
    	g2.drawString(player_1.def+"",220,325);
    	g2.drawString(player_1.gold+"",220,380);
    	g2.drawString(player_1.exp+"",220,430);
    	g2.drawString(player_1.Ykey+"",280,535);
    	g2.drawString(player_1.Bkey+"",280,605);
    	g2.drawString(player_1.Rkey+"",280,675);
    	g2.drawString(currentFloor+"",130,750);
    }

    public static void interaction(int x, int y)//main algorithm
	{
		//if unbeatable enemy
		if(spriteMap[currentFloor][x][y] instanceof enemy && (player_1.forecast((enemy)spriteMap[currentFloor][x][y]).equals("???") || Integer.parseInt(player_1.forecast((enemy)spriteMap[currentFloor][x][y]))>=player_1.hp))
			return;
		else if(spriteMap[currentFloor][x][y] instanceof enemy)
		{
			player_1.battle((enemy)spriteMap[currentFloor][x][y]);
			System.out.println ((enemyDict[spriteMap[currentFloor][x][y].id][0]).toString());
			mota.spriteMap[currentFloor][x][y]=null;
		}
		else if(spriteMap[currentFloor][x][y] instanceof item)
		{
			((item)spriteMap[currentFloor][x][y]).use();
			spriteMap[currentFloor][x][y]=null;
		}
		else if(spriteMap[currentFloor][x][y] instanceof dialogue)
		{
			int d=((dialogue)spriteMap[currentFloor][x][y]).id;
			inConversation=true;
			try{
				((dialogue)spriteMap[currentFloor][x][y]).perform();
			}catch(IOException e){};
		}
		else
		switch(LvMap[currentFloor][y][x])
		{
		case 1: player_1.move(x,y); break;
		case 5: if(player_1.Ykey>0){LvMap[currentFloor][y][x]=1; player_1.Ykey--; player_1.move(x,y);} break;
		case 6: if(player_1.Bkey>0){LvMap[currentFloor][y][x]=1; player_1.Bkey--; player_1.move(x,y);} break;
		case 7: if(player_1.Rkey>0){LvMap[currentFloor][y][x]=1; player_1.Rkey--; player_1.move(x,y);} break;
		case 8:
			currentFloor++;
			maxFloor=Math.max(maxFloor,currentFloor);
			player_1.move(initPos[currentFloor][0],initPos[currentFloor][1]); break;
		case 9:
			currentFloor--;
			player_1.move(finPos[currentFloor][0],finPos[currentFloor][1]);  break;
		case 10: LvMap[currentFloor][y][x]=1; break;
		}
	}

	public static void displayForecast()
	{
		inConversation=true;
		forecastFrame.setVisible(true);
		int cnt=0;
		ArrayList<Integer> idList= new ArrayList<Integer>();
		for(int x=0;x<spriteMap[currentFloor].length;x++)
		for(int y=0;y<spriteMap[currentFloor][x].length;y++)
		{
			if(spriteMap[currentFloor][x][y] instanceof enemy && !idList.contains(spriteMap[currentFloor][x][y].id))
			{
				idList.add(spriteMap[currentFloor][x][y].id);
				JLabel temp= new JLabel("NAME: "+(enemyDict[spriteMap[currentFloor][x][y].id][0]).toString()+" HP: "+((enemy)spriteMap[currentFloor][x][y]).hp+" ATK "+((enemy)spriteMap[currentFloor][x][y]).atk+" DEF "+((enemy)spriteMap[currentFloor][x][y]).def+" GOLD "+((enemy)spriteMap[currentFloor][x][y]).gold+" EXP "+((enemy)spriteMap[currentFloor][x][y]).exp+" LOST: "+player_1.forecast((enemy)spriteMap[currentFloor][x][y])+" HP");
				temp.setBounds(100,20+90*cnt,11*72,30);
				temp.setForeground(Color.WHITE);
				temp.setFont(new Font("Serif",0,20));
				JLabel head = new JLabel();
				try{head.setIcon(new ImageIcon(spriteMap[currentFloor][x][y].draw()));}catch(IOException e){};
				head.setBounds(20,20+90*cnt,72,72);
				cnt++;
				forecastFrame.add(temp);
				forecastFrame.add(head);
			}
		}
	}

	static JLabel[] choices = new JLabel[21];
	public static void displayJump()
	{
		for(int x=0;x<Math.min(8,maxFloor);x++)
		{
			JLabel temp= new JLabel(" "+x+"th_floor");
			temp.setFont(new Font("Serif",0,30));
			temp.setForeground(Color.WHITE);
			temp.setBounds(50,150+50*x,200,50);
			jumpFrame.add(temp);
			choices[x]=temp;
		}
		if(maxFloor>=8)
		for(int x=8;x<Math.min(16,maxFloor);x++)
		{
			JLabel temp= new JLabel(" "+x+"th_floor");
			temp.setFont(new Font("Serif",0,30));
			temp.setForeground(Color.WHITE);
			temp.setBounds(250,150+50*(x-8),200,50);
			jumpFrame.add(temp);
			choices[x]=temp;
		}
		if(maxFloor>=16)
		for(int x=16;x<Math.min(21,maxFloor);x++)
		{
			JLabel temp= new JLabel(" "+x+"th_floor");
			temp.setFont(new Font("Serif",0,30));
			temp.setForeground(Color.WHITE);
			temp.setBounds(450,150+50*(x-16),200,50);
			jumpFrame.add(temp);
			choices[x]=temp;
		}

		jumpFrame.setVisible(true);

		fr.addKeyListener(new KeyListener(){
		int selection=0;
		@Override
       	public void keyTyped(KeyEvent e){};
       	@Override
       	public void keyPressed(KeyEvent e)
       	{
       		if(selection!=20 && e.getKeyCode()==e.VK_S&& choices[selection+1]!=null)
       		{
       			choices[selection].setText(choices[selection].getText().replaceAll("→"," "));
       			selection=selection+1;
       			choices[selection].setText(choices[selection].getText().replaceAll(" ","→"));
       			fr.repaint();
       		}
       		if(selection!=0 && e.getKeyCode()==e.VK_W)
       		{
       			choices[selection].setText(choices[selection].getText().replaceAll("→"," "));
       			selection=selection-1;
       			choices[selection].setText(choices[selection].getText().replaceAll(" ","→"));
       			fr.repaint();
       		}
			if(e.getKeyCode()==e.VK_SPACE)
			{
				choices[selection].setText(choices[selection].getText().replaceAll("→"," "));
				player_1.move(initPos[selection][0],initPos[selection][1]);
				currentFloor=selection;
				fr.repaint();
				inConversation=false;
				jumpFrame.removeAll();
				jumpFrame.setVisible(false);
				fr.removeKeyListener(this);
			}
		}
       	@Override
       	public void keyReleased(KeyEvent e){}
		});
	}

	public static void displayMessage(String message)
	{
		msgPane.setVisible(true);
		inConversation=true;
		Timer animat= new Timer(1000,new ActionListener(){
			int count =0;
			@Override
			public void actionPerformed(ActionEvent e)
			{
				count++;
				if(count==2)
				{
					msgPane.setVisible(false);
					inConversation=false;
					((Timer)e.getSource()).stop();
				}
				msg.setText(message);
				fr.repaint();
			}
		});
		animat.setInitialDelay(0);
		animat.start();
	}

	static JLayeredPane conversation=new JLayeredPane();
	static JLabel dialogueBackground= new JLabel();
	static JTextArea text=new JTextArea(20,20);
	static JLabel imgIco=new JLabel();

    public static void talk(String[] messages, BufferedImage[] characters, int[] w, int[] h)
    {
		Insets insets = conversation.getInsets();

    	imgIco.setIcon(new ImageIcon(characters[0]));
       	imgIco.setBounds(20+insets.left,20+ insets.top,72,72);
       	text.setBounds(100+insets.left,20+ insets.top,w[0]-100,h[0]);
       	text.setText(messages[0]);
       	dialogueBackground.setBounds(0,0,w[0],h[0]);
       	conversation.setBounds(675,560,w[0],h[0]);
    	conversation.add(imgIco,2,0);
    	conversation.add(text,3,0);
    	game.add(conversation);
    	game.repaint();

    	fr.addKeyListener(new KeyListener(){
			int count=0;
			int x=0,y=0;
			@Override
       		public void keyTyped(KeyEvent e){};
       		@Override
       		public void keyPressed(KeyEvent e)
       		{
       			if(e.getKeyCode()==e.VK_SPACE)
       			{
       				conversation.remove(imgIco);
       				conversation.remove(text);
       				game.remove(conversation);
       				count++;
       				if(count>=messages.length)
       				{
       					inConversation=false;
       					fr.removeKeyListener(this);
       					return;
       				}
       				if(count%2==1)
       				{x=400; y=310;}
       				else {x=675; y=560;}
       				imgIco.setIcon(new ImageIcon(characters[count]));
       				imgIco.setBounds(20+insets.left,20+ insets.top,72,72);
       				text.setBounds(100+insets.left,20+ insets.top,w[count]-100,h[count]);
       				text.setText(messages[count]);
       				dialogueBackground.setBounds(0,0,w[count],h[count]);
    				conversation.setBounds(x,y,w[count],h[count]);
    				conversation.add(imgIco,2,0);
    				conversation.add(text,3,0);
    				game.add(conversation);
       				game.repaint();
       			}
       		}
       		@Override
       		public void keyReleased(KeyEvent e){};
		});
    }

    static String[] choice= new String[4];
    public static void store(int id)
    {
    	switch(id)
    	{
    		case 0: choice=new String[]{"→800HP(25GOLD)"," 4ATK(25GOLD)"," 4DEF(25GOLD)"," EXIT"};
    				imgIco.setIcon(new ImageIcon(dialogueImgSheet.getSubimage(72*5,0,72,72))); break;
    		case 1: choice=new String[]{"→LEVEL_UP(100EXP)"," 5ATK(30EXP)"," 5DEF(30EXP)"," EXIT"};
    				imgIco.setIcon(new ImageIcon(dialogueImgSheet.getSubimage(72*7,0,72,72))); break;
    		case 2: choice=new String[]{"→Yellow_key(10GOLD)"," Blue_key(50GOLD)"," Red_key(100GOLD)"," EXIT"};
    				imgIco.setIcon(new ImageIcon(dialogueImgSheet.getSubimage(72*8,0,72,72))); break;
    		case 3: choice=new String[]{"→4000HP(100GOLD)"," 20ATK(100GOLD)"," 20DEF(100GOLD)"," EXIT"};
    				imgIco.setIcon(new ImageIcon(dialogueImgSheet.getSubimage(72*10,0,72,72)));break;
    		case 4: choice=new String[]{"→Yellow_key(7GOLD)"," Blue_key(35GOLD)"," Red_key(70GOLD)"," EXIT"};
    				imgIco.setIcon(new ImageIcon(dialogueImgSheet.getSubimage(72*14,0,72,72))); break;
    		case 5: choice=new String[]{"→LEVEL_UP X3(270EXP)"," 17ATK(95EXP)"," 17DEF(95EXP)"," EXIT"};
    				imgIco.setIcon(new ImageIcon(dialogueImgSheet.getSubimage(72*15,0,72,72))); break;
    	}

    	Insets insets = conversation.getInsets();
       	imgIco.setBounds(20+insets.left,20+ insets.top,72,72);
       	text.setBounds(100+insets.left,20+ insets.top,460-100,460);
       	text.setText("choose one: \n "+choice[0]+" \n "+choice[1]+" \n "+choice[2]+" \n "+choice[3]);
       	dialogueBackground.setBounds(0,0,460,460);
       	conversation.setBounds(620,230,460,460);
    	conversation.add(imgIco,2,0);
    	conversation.add(text,3,0);
    	game.add(conversation);
    	game.repaint();

		fr.addKeyListener(new KeyListener(){
		int selection=0;
		String message="choose one: \n "+choice[0]+" \n "+choice[1]+" \n "+choice[2]+" \n "+choice[3];
		@Override
       	public void keyTyped(KeyEvent e){};
       	@Override
       	public void keyPressed(KeyEvent e)
       	{
       		if(selection!=3 && e.getKeyCode()==e.VK_S)
       		{
       			choice[selection]=choice[selection].replaceAll("→"," ");
       			selection=selection+1;
       			choice[selection]=choice[selection].replaceAll(" ","→");
				message="choose one: \n "+choice[0]+" \n "+choice[1]+" \n "+choice[2]+" \n "+choice[3];
       			text.setText(message);
       			fr.repaint();
       		}
       		if(selection!=0 && e.getKeyCode()==e.VK_W)
       		{
       			choice[selection]=choice[selection].replaceAll("→"," ");
       			selection=selection-1;
       			choice[selection]=choice[selection].replaceAll(" ","→");
				message="choose one: \n "+choice[0]+" \n "+choice[1]+" \n "+choice[2]+" \n "+choice[3];
       			text.setText(message);
       			fr.repaint();
       		}
			if(e.getKeyCode()==e.VK_SPACE)
			{
				switch (id)
				{
					case 0:
					switch(selection)
					{
						case 0: if(player_1.gold>=25) {player_1.gold-=25; player_1.hp+=800;} break;
						case 1: if(player_1.gold>=25) {player_1.gold-=25; player_1.atk+=4;} break;
						case 2: if(player_1.gold>=25) {player_1.gold-=25; player_1.def+=4;} break;
						case 3:
						conversation.remove(imgIco);
       					conversation.remove(text);
       					game.remove(conversation);
       					fr.repaint();
						inConversation=false;
						fr.removeKeyListener(this); break;
					}break;
					case 1:
					switch(selection)
					{
						case 0: if(player_1.exp>=100) {player_1.lv++; player_1.exp-=100; player_1.hp+=1000; player_1.atk+=7; player_1.def+=7;} break;
						case 1: if(player_1.exp>=30) {player_1.exp-=30; player_1.atk+=5;} break;
						case 2: if(player_1.exp>=30) {player_1.exp-=30; player_1.def+=5;} break;
						case 3:
						conversation.remove(imgIco);
       					conversation.remove(text);
       					game.remove(conversation);
       					fr.repaint();
						inConversation=false;
						fr.removeKeyListener(this); break;
					}break;
					case 2:
					switch(selection)
					{
						case 0: if(player_1.gold>=10) {player_1.gold-=10; player_1.Ykey++;} break;
						case 1: if(player_1.gold>=50) {player_1.gold-=50; player_1.Bkey++;} break;
						case 2: if(player_1.gold>=100) {player_1.gold-=100; player_1.Rkey++;} break;
						case 3:
						conversation.remove(imgIco);
       					conversation.remove(text);
       					game.remove(conversation);
       					fr.repaint();
						inConversation=false;
						fr.removeKeyListener(this); break;
					}break;
					case 3:
					switch(selection)
					{
						case 0: if(player_1.gold>=100) {player_1.gold-=100; player_1.hp+=4000;} break;
						case 1: if(player_1.gold>=100) {player_1.gold-=100; player_1.atk+=20;} break;
						case 2: if(player_1.gold>=100) {player_1.gold-=100; player_1.def+=20;} break;
						case 3:
						conversation.remove(imgIco);
       					conversation.remove(text);
       					game.remove(conversation);
						fr.repaint();
						inConversation=false;
						fr.removeKeyListener(this); break;
					}break;
					case 4:
					switch(selection)
					{
						case 0: if(player_1.Ykey>0) {player_1.Ykey--; player_1.gold+=7;} break;
						case 1: if(player_1.Bkey>0) {player_1.Bkey--; player_1.gold+=35;} break;
						case 2: if(player_1.Rkey>0) {player_1.Rkey--; player_1.gold+=70;} break;
						case 3:
						conversation.remove(imgIco);
       					conversation.remove(text);
       					game.remove(conversation);
						fr.repaint();
						inConversation=false;
						fr.removeKeyListener(this); break;
					}break;
					case 5:
					switch(selection)
					{
						case 0: if(player_1.exp>=270) {player_1.lv+=3; player_1.exp-=270; player_1.hp+=3000; player_1.atk+=21; player_1.def+=21;} break;
						case 1: if(player_1.exp>=95) {player_1.exp-=95; player_1.atk+=17;} break;
						case 2: if(player_1.exp>=95) {player_1.exp-=95; player_1.def+=17;} break;
						case 3:
						conversation.remove(imgIco);
       					conversation.remove(text);
       					game.remove(conversation);
       					fr.repaint();
						inConversation=false;
						fr.removeKeyListener(this); break;
					}break;
				}
			}
		}
       	@Override
       	public void keyReleased(KeyEvent e){}
		});
    }

    public static void main(String[] args) throws IOException{
        JFrame startFrame= new JFrame();
        JButton startGame= new JButton("Start Game");
        startFrame.addKeyListener(new KeyListener(){
        	@Override
       		public void keyTyped(KeyEvent e){};
        	@Override
        	public void keyPressed(KeyEvent e)
        	{
    	startFrame.setVisible(false);
        try{game= new mota();}catch(IOException ex){};
    	game.setPreferredSize(new Dimension(72*18, 72*13));

		game.add(forecastFrame);
		game.add(jumpFrame);
		game.add(battleFrame);
		game.add(msgPane);
		game.add(time);

		fr.addKeyListener(new KeyListener(){
       		@Override
       		public void keyTyped(KeyEvent e){};
       		@Override
       		public void keyPressed(KeyEvent e)
       		{
       			if(!inConversation)
       			switch(e.getKeyCode())
       			{
       				case 40 : //down
       				if(player_1.y+1<11 && player_1.y+1>=0)
       				{
       					player_1.drct=1;
       					interaction(player_1.x,player_1.y+1);
       					fr.repaint();
       				}break;
       				case 39 : //right
       				if(player_1.x+1<11 && player_1.x+1>=0)
       				{
       					player_1.drct=2;
       					interaction(player_1.x+1,player_1.y);
       					fr.repaint();
       				}break;
       				case 38 : //up
       				if(player_1.y-1<11 && player_1.y-1>=0)
       				{
       					player_1.drct=3;
       					interaction(player_1.x,player_1.y-1);
       					fr.repaint();
       				}break;
       				case 37 : //left
       				if(player_1.x-1<11 && player_1.x-1>=0)
       				{
       					player_1.drct=0;
       					interaction(player_1.x-1,player_1.y);
       					fr.repaint();
       				}break;
       				case 74 : //jump
       				if(fly)
       				{
       					displayJump();
       				}break;
       				case 76 : //forecast
       				if(forecast)
       				{
       					displayForecast();
       				}break;
       			}
       			else if (e.getKeyCode()==e.VK_L)//bug
       			{
       				inConversation=false;
       				forecastFrame.removeAll();
					forecastFrame.setVisible(false);
       			}
       		}
       		@Override
       		public void keyReleased(KeyEvent e){};
    	});

		fr.setContentPane(game);
    	fr.setResizable(false);
    	fr.pack();
    	fr.setLocationRelativeTo(null);
    	fr.setVisible(true);
    	fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        	}
        @Override
       	public void keyReleased(KeyEvent e){};
        });
        startFrame.add(new JLabel(new ImageIcon(ImageIO.read(new File(System.getProperty("user.dir")+"/openFrame.png")))));
        startGame.setBounds(100,100,200,100);
        startFrame.setBackground(Color.BLACK);
        startFrame.setResizable(false);
        startFrame.pack();
        startFrame.setLocationRelativeTo(null);
    	startFrame.setVisible(true);
    	startFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
