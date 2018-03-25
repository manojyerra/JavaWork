import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.awt.GraphicsEnvironment;
import java.io.Console;

class FolderUtils
{
	JFrame frame = null;
	Process p = null;


	public static void main(String[] args) throws Exception
	{
		new FolderUtils();
	}


	FolderUtils() throws Exception
	{
		int frameW = 600;
		int numTabs = 3;
		int tabW = (frameW-100) / numTabs;
		int tabH = 25;

		JTabbedPane tabPane = new JTabbedPane();

		tabPane.addTab("Calculate Size", createPanelForFolderSize());
		tabPane.addTab("Delete", createPanelForFolderDelete());
		tabPane.addTab("Copy", createPanelForFolderCopy());

		tabPane.setTabComponentAt(0, createLabel("Calculate Size", 17, tabW, tabH));
		tabPane.setTabComponentAt(1, createLabel("Delete", 17, tabW, tabH));
		tabPane.setTabComponentAt(2, createLabel("Copy", 17, tabW, tabH));

		frame = new JFrame();
		frame.setBounds(500,200,frameW,300);
		frame.setTitle("Folder Utils");
		frame.getContentPane().add(tabPane);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
	}


	JPanel createPanelForFolderSize()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));

		new FileDrop(panel, new FileDrop.Listener()
		{
			public void  filesDropped( java.io.File[] files )
			{
			   if(files.length == 1)
			   {
			   		if(!files[0].isDirectory())
					{
						JOptionPane.showMessageDialog(panel, files[0].getPath()+"  is not a folder.");
						return;
					}

					try
					{
						launchTerminal("FolderSize", files[0].getAbsolutePath());
					}
					catch(Exception e){e.printStackTrace();}
			   }
			}
		});

		panel.add(createLabel("Drag and drop folder to calculate size.", 20));
		return panel;	
	}


	JPanel createPanelForFolderCopy()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));

		new FileDrop(panel, new FileDrop.Listener()
		{
			public void  filesDropped( java.io.File[] files )
			{
			   if(files.length == 1)
			   {
			   		if(!files[0].isDirectory())
					{
						JOptionPane.showMessageDialog(panel, files[0].getPath()+"  is not a folder.");
						return;
					}

					try
					{
						launchTerminal("FolderSize", files[0].getAbsolutePath());
					}
					catch(Exception e){e.printStackTrace();}
			   }
			}
		});

		panel.add(createLabel("Drag and drop folder to calculate size.", 20));
		return panel;	
	}

	JPanel createPanelForFolderDelete()
	{
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridLayout(1,1));

		new FileDrop(panel, new FileDrop.Listener()
		{
			public void  filesDropped( java.io.File[] files )
			{
			   if(files.length == 1)
			   {
					//System.out.println("folder path : "+files[0].getAbsolutePath());

			   		if(!files[0].isDirectory())
					{
						JOptionPane.showMessageDialog(panel, files[0].getPath()+"  is not a folder.");
						return;
					}

					try
					{
						int reply = JOptionPane.showConfirmDialog(panel, "Are you sure want to permanantly delete ", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        				
        				if (reply == JOptionPane.YES_OPTION)
        				{
							launchTerminal("FolderDelete", files[0].getAbsolutePath() );
        				}
					}
					catch(Exception e){e.printStackTrace();}
			   }
			}
		});

		panel.add( createLabel("Drag and drop folder to delete.", 20));
		return panel;	
	}
	
	JLabel createLabel(String txt, int fontSize)
	{
		JLabel label = new JLabel(txt);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setOpaque(true);
		label.setBackground(new Color(200,214,242));
		label.setForeground(Color.black);
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, fontSize));
		return label;		
	}

	JLabel createLabel(String txt, int fontSize, int preferredW, int preferredH)
	{
		JLabel label = new JLabel(txt);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(preferredW, preferredH));
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, fontSize));
		return label;		
	}

	void launchTerminal(String className, String folderPath) throws Exception
    {
    	if(p != null)
    		p.destroyForcibly();

    	boolean isWindows = isWindows();

    	if(isWindows)
    	{
			String command= "java "+className+" \""+folderPath+"\"";
			Runtime rt = Runtime.getRuntime();
			p = rt.exec("cmd.exe /c start "+command, null, new File("./"));
    	}
    	else
    	{
			//ProcessBuilder pb = new ProcessBuilder("/usr/bin/x-terminal-emulator", "--disable-factory", "-e", "java", className, folderPath);
			ProcessBuilder pb = new ProcessBuilder("/usr/bin/xterm", "-e", "java", className, folderPath);
			p = pb.start();
		}
    }
	
	public static boolean isUnix() 
	{
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

	public static boolean isWindows() 
	{
		//return true;
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}
}
