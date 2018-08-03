import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.*;
import java.io.*;

public class Notepad {
    static JFrame frame;
    static JTextArea previewPane;
    static JTextPane editorPane;
    static HashSet<String> reservedWords;
    static JTextField filenameField;
    
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

    public static JTextField createSaveFilenameTextField() {
	final int COLUMNS = 55;

	filenameField = new JTextField(COLUMNS);
	filenameField.setFont(new Font("Courier", Font.PLAIN, 21));
	filenameField.setForeground(new Color(200, 197, 192));
	filenameField.setBackground(new Color(44, 40, 39));
	filenameField.setText("NameMeSomething.java");
	return filenameField;
    }
	
    public static JPanel createRightPanel() {
	JPanel rPanel = new JPanel();
	BoxLayout bLayout = new BoxLayout(rPanel, BoxLayout.Y_AXIS);
	rPanel.setLayout(bLayout);
	previewPane = createPreviewTextArea();
	JScrollPane rightPanel =
	    new JScrollPane(previewPane);
	JScrollPane placeHolderPanel =
	    new JScrollPane(createSaveFilenameTextField());
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
	JScrollPane leftPanel = new JScrollPane(editorPane);
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize();
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
	textField.setForeground(new Color(200, 197, 192));
	textField.setBackground(new Color(44, 40, 39));
	
	return textField;
    }
    
    public static JTextPane createMainTextField() {
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension fullscreen = tk.getScreenSize();
	JTextPane textField = new JTextPane();
	textField.setFont(new Font("Courier", Font.PLAIN, 34));
	// Colors from In Color Balance From Pinterest
	textField.setForeground(new Color(200, 197, 192));
	textField.setBackground(new Color(44, 40, 39));
	textField.setCaretColor(new Color(200, 197, 192));
	
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
			file = new File(filename);
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
