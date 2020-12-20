import java.io.*;

import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Frame;
import java.awt.Window;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Choice;
import java.awt.Button;
import java.awt.Font;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import json.*;


class GitDiffTool extends JFrame implements ItemListener, ActionListener
{
	DiffBlocksViewer currentBlock = null;
	
	JComboBox<String> ch = new JComboBox<String>();
	
	JButton refresh = new JButton("Refresh");
	JButton checkout = new JButton("Checkout");	
	JButton checkoutAll = new JButton("Checkout All");
	JButton saveFile = new JButton("Save File");
	JButton changeDirectory = new JButton("Change Project");

	int diffBlockY1 = 0;
	int diffBlockY2 = 0;

	String projPath = null;
	String branchName = "";
	File gitDiffProps = null;
	
	Label titleBarLabel = null;

	public static void main(String args[]) throws Exception
	{
		new GitDiffTool();
	}
	
	GitDiffTool() throws Exception
	{
		if(Util.isWindows())
			setUndecorated(true);

		SetBoundsToFrame();

		setVisible(true);
		setLayout(null);
		getContentPane().setBackground( new Color(64,64,64) );
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		readProperties();

		setTextOnTitleBar("Fetching modified files list...");
		
		float w = getWidth();
		float h = getHeight();

		if(Util.isWindows())
		{
			addHeader(0, 0, (int)w, (int)(h*0.03f));
			diffBlockY1 = (int)(h*0.03f);
		}
		
		if(Util.isUnix())
			diffBlockY2 = (int)(h*0.90f);
		else
			diffBlockY2 = (int)(h*0.935f);

		int startY = diffBlockY2+10;

		ch.setBounds(0,startY,(int)(w*0.5),(int)(h*0.03f));
		ch.setFont(new Font("TimesRoman", Font.BOLD, 15));
		ch.setBackground(Color.darkGray);
		ch.setForeground(Color.white);
		ch.addActionListener(this);
		add(ch);

		refresh.setBounds((int)(w*0.50), startY,(int)(w*0.1), (int)(h*0.03f));
		refresh.setFont(new Font("TimesRoman", Font.BOLD, 15));
		refresh.setBackground(Color.darkGray);
		refresh.setForeground(Color.white);
		refresh.setOpaque(true);
		refresh.addActionListener(this);
		add(refresh);

		checkout.setBounds((int)(w*0.60), startY, (int)(w*0.1), (int)(h*0.03f));
		checkout.setFont(new Font("TimesRoman", Font.BOLD, 15));
		checkout.setBackground(Color.darkGray);
		checkout.setForeground(Color.white);
		checkout.addActionListener(this);
		checkout.setOpaque(true);
		add(checkout);

		checkoutAll.setBounds((int)(w*0.70), startY, (int)(w*0.1), (int)(h*0.03f));
		checkoutAll.setFont(new Font("TimesRoman", Font.BOLD, 15));
		checkoutAll.setBackground(Color.darkGray);
		checkoutAll.setForeground(Color.white);
		checkoutAll.addActionListener(this);
		checkoutAll.setOpaque(true);
		add(checkoutAll);

		saveFile.setBounds((int)(w*0.80), startY, (int)(w*0.1), (int)(h*0.03f));
		saveFile.setFont(new Font("TimesRoman", Font.BOLD, 15));
		saveFile.setBackground(Color.darkGray);
		saveFile.setForeground(Color.white);
		saveFile.addActionListener(this);
		saveFile.setOpaque(true);
		add(saveFile);

		changeDirectory.setBounds((int)(w*0.90), startY, (int)(w*0.1), (int)(h*0.03f));
		changeDirectory.setFont(new Font("TimesRoman", Font.BOLD, 15));
		changeDirectory.setBackground(Color.darkGray);
		changeDirectory.setForeground(Color.white);
		changeDirectory.addActionListener(this);
		changeDirectory.setOpaque(true);
		add(changeDirectory);
		
		onRefesh();
		repaint();
		invalidate();
	}

	void readProperties() throws Exception
	{
		String userHome = System.getProperty("user.home");
		
		//System.out.println("user.home <"+userHome+">");

		File gitDiffDir = new File(userHome+"/GitDiffTool");
		gitDiffProps = new File(userHome+"/GitDiffTool/GitDiffTool.properties");

		if(!gitDiffDir.exists())
		{
			//System.out.println("folder not exist"+gitDiffDir.getPath());

			gitDiffDir.mkdir();

			if(!gitDiffDir.exists())
			{
				JOptionPane.showMessageDialog(this, "Failed to create directory at "+gitDiffDir.getAbsolutePath());
				System.exit(0);
			}	
		}

		if(!gitDiffProps.exists())
		{
			projPath = dialogueForProjectDirectory();

			if(projPath == null)
				System.exit(0);

			JsonObject obj = JsonObject.readFrom("{}");
			obj.set("ProjectDirectory", projPath);
			
			Util.write( gitDiffProps, obj.toString(WriterConfig.PRETTY_PRINT));
		}

		if(projPath == null)
		{
			JsonObject jsonObj = (JsonObject)Json.parse(new FileReader(gitDiffProps));
			projPath = jsonObj.get("ProjectDirectory").asString();
		}

		branchName = Util.getGitBranch(projPath);
	}

	String dialogueForProjectDirectory() throws Exception
	{
		String msg = "Enter project path.";
		String tempProjPath = "";

		do
		{
			 tempProjPath = JOptionPane.showInputDialog(this, msg);
			 
			 if(tempProjPath == null)
			 {
			 	return null;
			 }
			 //System.out.println("projPath:<"+projPath+">");

			 msg = "Invalid Entry, Enter valid project path.";
		}
		while(!Util.isGitFolder(tempProjPath));

		return tempProjPath;
	}

	void addHeader(int x, int y, int w, int h)
	{
		Panel titleBar = new Panel();
		titleBar.setLayout(null);
		titleBar.setBounds(x, y, w, h);
		titleBar.setBackground(new Color(184, 134, 11));
		add(titleBar);

		Button close = new Button("X");

		//Font btnFont = close.getFont();
		//btnFont = new Font(btnFont.getFontName(), Font.BOLD, btnFont.getSize());

		Font btnFont = new Font("TimesRoman", Font.BOLD, h-8);

		int btnW = (int)((float)w * 0.02);
		
		close.setBounds(w-btnW, y+1, btnW, h-2);
		close.setBackground(Color.WHITE);
		close.setFont(btnFont);
		close.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			System.exit(0);
		    }
		});

		Button min = new Button("-");
		min.setBounds(w-2*btnW, y+1, btnW, h-2);
		min.setBackground(Color.WHITE);
		min.setFont(btnFont);
		min.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent arg0) {
    			setState ( Frame.ICONIFIED );
		    }
		});
		
		titleBarLabel = new Label();
		titleBarLabel.setBounds(0, y+1, min.getX(), h-2);
		titleBarLabel.setFont(new Font("TimesRoman", Font.BOLD, h-12));
		
		titleBar.add(titleBarLabel);
		titleBar.add(min);
		titleBar.add(close);

		min.setFocusable(false);
		close.setFocusable(false);
	}

	void onRefesh() throws Exception
	{
		addModifiedFilesToChoice();

		//System.out.println("num items : "+ch.getItemCount());

		if(projPath != null && branchName != null && projPath.length() > 0 && branchName.length() != 0)
		{
			setTextOnTitleBar("  [ "+projPath+" : "+branchName+" ]");
		}	
		else
		{
			setTextOnTitleBar("");
		}	


		if(ch.getItemCount() > 0)
		{
			SetDiffBlockViewer(ch.getItemAt(0));
		}	
		else
		{
			if(currentBlock != null)
				remove(currentBlock);

			JOptionPane.showMessageDialog(this,"No files are modified.");
		}
	}

	void addModifiedFilesToChoice() throws Exception
	{
		ch.removeAllItems();

		ArrayList<String> modifiedList = Util.getModifiedFilesList(projPath);
	
		for(int i=0; i<modifiedList.size(); i++)
			ch.addItem(modifiedList.get(i));		
	}

	void SetDiffBlockViewer(String filePath) throws Exception
	{
		if(currentBlock != null)
			remove(currentBlock);

		float w = getWidth();
		float h = getHeight();
		
		ArrayList<String> diffData = Util.getDiffFile(projPath, filePath);
		currentBlock = new DiffBlocksViewer(diffData, 0, diffBlockY1, (int)w, diffBlockY2-diffBlockY1 );

		add( currentBlock );

		setTextOnTitleBar(filePath+"  [ "+projPath+" : "+branchName+" ]");
	}

	public void itemStateChanged(ItemEvent e)
	{
		try{
			SetDiffBlockViewer((String)ch.getSelectedItem());
		}catch(Exception exc){exc.printStackTrace();}
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            if(ae.getSource() == ch)
            {
                SetDiffBlockViewer((String)ch.getSelectedItem());
            }
			else if(ae.getSource() == refresh)
			{
				onRefesh();
			}
			else if(ae.getSource() == checkout)
			{
				//System.out.println("checkout called..."+ch.getItemCount());

				if(ch.getItemCount() > 0)
				{
					//System.out.println("getSelectedItem..."+ch.getSelectedItem());

					Util.checkout(projPath, (String)ch.getSelectedItem());

					onRefesh();
				}
			}
			else if(ae.getSource() == checkoutAll)
			{
				Util.checkoutAll(projPath);
				onRefesh();
			}
			else if(ae.getSource() == saveFile)
			{
				if(ch.getItemCount() > 0)
				{
					ArrayList<String> data = currentBlock.getLeftBlockData();

					//Util.printList(data);

					String filePath = projPath+"/"+ch.getSelectedItem();

					//System.out.println("filePath: <"+filePath+">");

					BufferedWriter bw = new BufferedWriter( new FileWriter(filePath) );

					for(int i=0; i<data.size(); i++)
					{
						String line = data.get(i);

						if(line != null)
						{
							if(i == data.size()-1 && line.equals(" "))
							{
								bw.newLine();
								break;
							}

							if(i != 0)
								bw.newLine();

							//if(!(i == data.size()-1 && line.equals(" ")))
							//	bw.write(line, 1, line.length()-1);

							bw.write(line, 1, line.length()-1);							
							//bw.newLine();

							bw.flush();
						}
					}

					bw.close();
				}
			}
			else if(ae.getSource() == changeDirectory)
			{
				String tempProjPath = dialogueForProjectDirectory();

				if(tempProjPath != null)
				{
					projPath = tempProjPath;
					JsonObject obj = JsonObject.readFrom("{}");
					obj.set("ProjectDirectory", projPath);
					
					Util.write( gitDiffProps, obj.toString(WriterConfig.PRETTY_PRINT));
					branchName = Util.getGitBranch(projPath);

					onRefesh();
				}
			}
		}
		catch(Exception exc){exc.printStackTrace();}		
	}

	void SetBoundsToFrame()
	{
        GraphicsConfiguration gc = Util.getGraphicsConfiguration();
		
		Rectangle bounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

		Rectangle effectiveScreenArea = new Rectangle();

		effectiveScreenArea.x = bounds.x + screenInsets.left;
		effectiveScreenArea.y = bounds.y + screenInsets.top;
		effectiveScreenArea.height = bounds.height - screenInsets.top - screenInsets.bottom;        
		effectiveScreenArea.width = bounds.width - screenInsets.left - screenInsets.right;
		
		setBounds(effectiveScreenArea);
	}
	
	void setTextOnTitleBar(String text)
	{
		if(Util.isUnix())
		{
			setTitle(text);
		}
		else
		{
			if(titleBarLabel != null)
			{
				titleBarLabel.setText(text);
			}
		}
	}
}



	// void SetBoundsToFrame()
	// {
	// 	if (Util.isUnix()) {
	//         getContentPane().setPreferredSize(
	//                 Toolkit.getDefaultToolkit().getScreenSize());
	//         pack();
	//         setResizable(false);
	//         show();

	//         SwingUtilities.invokeLater(new Runnable() {
	//             public void run() {
	//                 Point p = new Point(0, 0);
	//                 SwingUtilities.convertPointToScreen(p, getContentPane());
	//                 Point l = getLocation();
	//                 l.x -= p.x;
	//                 l.y -= p.y;
	//                 setLocation(p);
	//             }
	//         });
 //    	}

 //    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
 //    	setBounds(0, 0, (int) dim.getWidth(), (int) dim.getHeight());
 //    	setLocationRelativeTo(null);
	// }
