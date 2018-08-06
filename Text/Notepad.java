import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

public class Notepad {
    static JFrame frame;
    static JTextArea previewPane;
    static JTextPane editorPane;
    static HashSet<String> reservedWords;
    static JTextField filenameField;
    static JTextField workingPath;
    static int keyCTRLCount;
    
    public static void createNotepadMainWindow() {
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize(); 
	frame = new JFrame("Notepad");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setPreferredSize(fullscreen);
	frame.setJMenuBar(createDefaultMenuBar());
	frame.setLayout(new BoxLayout(frame.getContentPane(),
				      BoxLayout.X_AXIS));
	frame.getContentPane().add(createLeftPanel());
	frame.getContentPane().add(createRightPanel());
	frame.pack();
	frame.setVisible(true);
    }
    
    public static int normalWidth(int width) {
	if (width > 1920)
	    return width/2;
	return width;
    }
    
    public static JPanel createSaveFilenameTextField() {
	JPanel sfPanel = new JPanel();
	BoxLayout saveFilenameConsoleBoxLayout =
	    new BoxLayout(sfPanel, BoxLayout.Y_AXIS);
	sfPanel.setLayout(saveFilenameConsoleBoxLayout);
	
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize();
	int thisWidth = normalWidth((int)fullscreen.getWidth()) -
	    (int) Math.floor(normalWidth(
	    (int)fullscreen.getWidth())*0.618);
	JTextArea saveFilenameConsole = new JTextArea();
	saveFilenameConsole.setEditable(false);
	    saveFilenameConsole.setPreferredSize(
	    new Dimension(thisWidth-21,
			  (int)((double)thisWidth*0.618) - 21));
	saveFilenameConsole.setFont(new Font("Lucida Console",
					     Font.PLAIN, 13));
	saveFilenameConsole.setForeground(new Color(236, 237, 233));
	saveFilenameConsole.setBackground(new Color(0, 0, 0));
	saveFilenameConsole.setText("Console >>>\n" +
				    "Welcome to Hao's Notepad!\n" +
				    "Type 'save yourfilename.ext' to save\n" +
				    "Type 'open yourfilename.ext' to open\n" +
				    "Double-Tap CTRL to switch between CONSOLE"
				    + " and Main Editor\n\n" +
				    "Enjoy!\n\n>>>\n");
	sfPanel.add(saveFilenameConsole);

	workingPath = new JTextField();
	workingPath.setPreferredSize(
	   new Dimension(thisWidth-21,
			 (int)((double)21.)));
	workingPath.setFont(new Font("Lucida Console", Font.PLAIN, 13));
	workingPath.setForeground(new Color(46, 52, 54));
	workingPath.setBackground(new Color(188, 188, 188));
	// thanks to Qazi and BalusC @ stackoverflow.com::java
	// for the PWD function in Java
	try {
	    String pwd = new java.io.File(".").getCanonicalPath();
	    workingPath.setText(pwd);
	} catch(Exception e) {
	    System.err.println(":(");
	}
	sfPanel.add(workingPath);
	
	filenameField = new JTextField();
	filenameField.setPreferredSize(
	   new Dimension(thisWidth-21,
			 (int)((double)21.)));
	filenameField.setFont(new Font("Lucida Console", Font.PLAIN, 13));
	filenameField.setForeground(new Color(211, 211, 210));
	filenameField.setBackground(new Color(35, 39, 41));
	filenameField.setText("save NameMeSomething.java");
	filenameField.addKeyListener(new KeyListener() {
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			keyCTRLCount++;
			//System.out.println(keyCTRLCount);
			if (keyCTRLCount >= 2) {
			    editorPane.requestFocusInWindow();
			    keyCTRLCount = 0;
			}
		    }
		}

		public void keyReleased(KeyEvent e) {
		    ;
		}

		public void keyTyped(KeyEvent e) {;};
	    });
	filenameField.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    updatePreviewTextArea();
		    java.util.Scanner scanCommand =
			new java.util.Scanner(e.getActionCommand());
		    scanCommand.useDelimiter(" ");

		    String scanned = scanCommand.next();
		    if (scanned.equals("save")) {
			//debug: System.out.println("Saving...");
			
			String filename = scanCommand.next();
			File file = null;
			FileOutputStream fos = null;
			PrintWriter pw = null;
			try {
			    String pwd = workingPath.getText();
			    //System.out.println(pwd);
			    if (pwd.charAt(pwd.length()-1) == '/')
				pwd = pwd.substring(0, pwd.length()-1);
			    //System.out.println(pwd);
			    file = new File(pwd + "/" + filename);
			    fos = new FileOutputStream(file);
			    pw = new PrintWriter(fos);
			    pw.print(editorPane.getText());
			    pw.println();
			    pw.flush();
			} catch(Exception except) {
			System.err.println(":(");
			} finally {
			    try {
				pw.close();
			    fos.close();
			    } catch(Exception err) {
				System.err.println(":(");
			    }
			}			
			saveFilenameConsole.setText(
			   saveFilenameConsole.getText() +
			   '\n' +
			   filenameField.getText());
			String consoleText = filenameField.getText();
		    } else if (scanned.equals("open")) {
			String filename = scanCommand.next();
			File file = null;
			FileReader fr = null;
			    try {
				String pwd = workingPath.getText();
				//System.out.println(pwd);
				if (pwd.charAt(pwd.length()-1) == '/')
				    pwd = pwd.substring(0, pwd.length()-1);
				//System.out.println(pwd);
				file = new File(pwd + "/" + filename);
				fr = new FileReader(file);
				StringBuilder sb = new StringBuilder();
				while(fr.ready()) {
				    sb.append((char)fr.read());
				}
				
				editorPane.setText(sb.toString());
				previewPane.setText(sb.toString());
			    } catch(Exception except) {
				System.err.println(":(");
			    } finally {
				try {
				    fr.close();
				} catch (Exception ex) {
				    System.err.println(":(");
				}
			    }
			saveFilenameConsole.setText(
			   saveFilenameConsole.getText() +
			   '\n' +
			   filenameField.getText());
			String consoleText = filenameField.getText();
		    } else if (scanned.equals("exit") ||
			       scanned.equals("quit") ||
			       scanned.equals("leave")) {
			String filetrace = "Notepad.history";
			String pwd = workingPath.getText();
			//System.out.println(pwd);
			if (pwd.charAt(pwd.length()-1) == '/')
			    pwd = pwd.substring(0, pwd.length()-1);
			//System.out.println(pwd);
			File f = new File(pwd + "/" +
					  filetrace);
			StringBuilder sb = null;
			
			try {
			    FileReader fr = new FileReader(f);
			   sb = new StringBuilder();
			    
			    while(fr.ready()) {
				sb.append((char)fr.read());
			    }

			    try {
				fr.close();
			    } catch(Exception err) {
				System.err.println(":(");
			    }
			} catch (Exception error) {
			    System.err.println(":(");
			}
			  			    
			PrintWriter tracepw = null;
			try { 
			    tracepw = new PrintWriter(f);
			    if (sb != null)
				tracepw.append(sb.toString());
			    tracepw.append(saveFilenameConsole.getText());
			    tracepw.append('\n');
			    tracepw.flush();
			} catch (Exception err) {
			    System.err.println(":(");
			}
			    
			try {
			    tracepw.close();
			} catch (Exception err) {
			    System.err.println(":(");
			}
			frame.dispatchEvent(
			       new WindowEvent(frame,
					       WindowEvent.WINDOW_CLOSING));
		    }
		    
		}
	    });
	sfPanel.add(filenameField);
	
	return sfPanel;
    }
	
    public static JPanel createRightPanel() {
	JPanel rPanel = new JPanel();
	BoxLayout bLayout = new BoxLayout(rPanel, BoxLayout.Y_AXIS);
	rPanel.setLayout(bLayout);
	previewPane = createPreviewTextArea();
	JScrollPane rightPanel =
	    new JScrollPane(previewPane);
	JScrollPane placeHolderPanel = new JScrollPane(createSaveFilenameTextField());
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize();
	int thisWidth = normalWidth((int)fullscreen.getWidth()) -
	                            (int) Math.floor(normalWidth(
				    (int)fullscreen.getWidth())*0.618);
	rightPanel.setPreferredSize(new Dimension(thisWidth,
		                    (int) Math.floor(thisWidth/1.618)));

	rPanel.add(rightPanel);
	rPanel.add(placeHolderPanel);
	return rPanel;
    }
    
    public static JScrollPane createLeftPanel() {
	editorPane = createMainTextField();
	editorPane.addKeyListener(new KeyListener() {
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			keyCTRLCount++;
			//System.out.println(keyCTRLCount);
			if (keyCTRLCount >= 2) {
			    filenameField.requestFocusInWindow();
			    keyCTRLCount = 0;
			}
		    }
		}
		
		public void keyReleased(KeyEvent e) {
		    ;
		}
		
		public void keyTyped(KeyEvent e) {;};
	    });
	JScrollPane leftPanel = new JScrollPane(editorPane);
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize();
	keyCTRLCount = 0;
	leftPanel.setPreferredSize(
	               new Dimension((int) Math.floor(
	           0.618*normalWidth((int) fullscreen.getWidth())),
		     	             (int) fullscreen.getHeight()));
       
	return leftPanel;
    }

    public static JTextArea createPreviewTextArea() {
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize();
	JTextArea textField = new JTextArea();
	textField.setEditable(false);
	textField.setFont(new Font("Courier", Font.PLAIN, 21));
	textField.setForeground(new Color(211, 211, 210));
	textField.setBackground(new Color(35, 39, 41));
	
	return textField;
    }
    
    public static JTextPane createMainTextField() {
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize();
	JTextPane textField = new JTextPane();
	textField.setFont(new Font("Courier", Font.PLAIN, 34));
	// Colors from In Color Balance From Pinterest
	textField.setForeground(new Color(211, 211, 210));
	textField.setBackground(new Color(35, 39, 41));
	textField.setCaretColor(new Color(211, 211, 210));
						      
	return textField;
    }
    
    public static JMenu createMenu(String name, int key) {
	JMenu menu = new JMenu(name);
	menu.setMnemonic(key);
	return menu;
    }

    public static JMenuItem createMenuItem(String name, int key) {
	JMenuItem menuItem = new JMenuItem(name, key);
	return menuItem;
    }

    public static void createSaveMenuItems(JMenu menu) {
	JMenuItem saveMenuItem = createMenuItem("Save...", KeyEvent.VK_S);
	JMenuItem saveAllMenuItem = createMenuItem("Save All...", -1);
	JSeparator separator = new JSeparator();

	saveMenuItem.setAccelerator(
	   KeyStroke.getKeyStroke(KeyEvent.VK_S,
	  		          ActionEvent.CTRL_MASK));
	saveMenuItem.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    updatePreviewTextArea();
		    String filename = filenameField.getText();
		    File file = null;
		    FileOutputStream fos = null;
		    PrintWriter pw = null;
		    try {
			String pwd = workingPath.getText();
			if(pwd.charAt(pwd.length()-1) == '/')
			    pwd = pwd.substring(0, pwd.length()-1);
			file = new File(pwd + "/" +
					filename);
			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.print(editorPane.getText());
			pw.println();
			pw.flush();
		    } catch(Exception except) {
			System.err.println(":(");
		    } finally {
			try {
			    pw.close();
			    fos.close();
			} catch(Exception err) {
			    System.err.println(":(");
			}
		    }
		}
	    });
	menu.add(saveMenuItem);
	menu.add(saveAllMenuItem);
	menu.add(separator);
    }

    public static void updatePreviewTextArea() {	
	previewPane.setText(editorPane.getText());
    }
		
    public static void createFindMenuItems(JMenu menu) {
	JMenuItem findMenuItem =
	    createMenuItem("Find...",
			   KeyEvent.VK_G);
	JMenuItem findAndReplaceMenuItem =
	    createMenuItem("Find and Replace...",
			   KeyEvent.VK_H);
	JMenuItem clearHighlightMenuItem =
	    createMenuItem("Clear Highlight...",
			   KeyEvent.VK_C);
	JMenuItem gotoLineMenuItem =
	    createMenuItem("Go to Line...",
			   KeyEvent.VK_R);
	JSeparator separator = new JSeparator();

	menu.add(findMenuItem);
	menu.add(findAndReplaceMenuItem);
	menu.add(clearHighlightMenuItem);
	menu.add(gotoLineMenuItem);
	menu.add(separator);	
    }

    public static void createCloseMenuItems(JMenu menu) {
	JMenuItem closeMenuItem =
	    createMenuItem("Close...",
			   KeyEvent.VK_X);
	JMenuItem closeAllMenuItem =
	    createMenuItem("Close All...",
			   KeyEvent.VK_K);
	
	menu.add(closeMenuItem);
	menu.add(closeAllMenuItem);
    }

    public static void createOpenMenuItems(JMenu menu) {
	JMenuItem openMenuItem =
	    createMenuItem("Open...", KeyEvent.VK_E);
	JMenuItem otherDocumentsMenuItem =
	    createMenuItem("Other Documents...", KeyEvent.VK_D);
	
	menu.add(openMenuItem);
	menu.add(otherDocumentsMenuItem);
    }

    public static void createPrintMenuItems(JMenu menu) {
	JMenuItem printMenuItem = createMenuItem("Print...", KeyEvent.VK_T);
	
	menu.add(printMenuItem);
    }
    
    public static JMenu createFileMenu() {
	JMenu fileMenu = createMenu("File", KeyEvent.VK_F);
	createSaveMenuItems(fileMenu);
	createFindMenuItems(fileMenu);
	createCloseMenuItems(fileMenu);
	
	return fileMenu;
    }

    public static JMenu createOpenMenu() {
	JMenu openMenu = createMenu("Open", KeyEvent.VK_O);
	createOpenMenuItems(openMenu);
		
	return openMenu;
    }

    public static JMenu createPrintMenu() {
	JMenu printMenu = createMenu("Print", KeyEvent.VK_P);
	createPrintMenuItems(printMenu);
		
	return printMenu;
    }
        
    public static JMenuBar createDefaultMenuBar() {
	JMenuBar menuBar = new JMenuBar();

	menuBar.add(createFileMenu());
	menuBar.add(createOpenMenu());
	menuBar.add(createPrintMenu());
	return menuBar;
    }	

    public static void populate(String filename) {
	Scanner scan;
	scan = new Scanner(filename);
	while(scan.hasNext()) {
	    String word = scan.next();
	    if(!reservedWords.contains(word))
		reservedWords.add(word);
	}
    }

    public static void createDictionary() {
	reservedWords = new HashSet<String>();
    }
    
    public static void populateReservedWords()
    {
	createDictionary();
	populate("clang_reserved.txt");
	populate("cpp_reserved.txt");
	populate("scheme_reserved.txt");
	populate("java_reserved.txt");    
    }

    public static void highlightPreviewPane() {
	String text = previewPane.getText();
	
	
    };

    public static void highlightEditorPane() {
    };
    
    public static void highlightReservedWords() {
	highlightPreviewPane();
	highlightEditorPane();
    }
    
    public static void main(String... args) {
	populateReservedWords();
	createNotepadMainWindow();
    }
}
