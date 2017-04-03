package graph.graphical;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;


import general.definition.Menu;
import graph.FacebookGraph;


/**
 * This class will create a Graphical interface to display loaded graph, 
 * to apply the algorithms on the graph, and to rock things out.
 * The events of the GUI will be handled by ListenerHandler class.
 * @author Oussema Hidri d.oussema.d@gmail.com
 *
 */
public class GraphicalInterface {


	// two side panels
	private JPanel leftSidePanel;
	private JPanel rightSidePanel;

	// main Frame
	private JFrame window;


	private MultiGraph visualGraph;

	private FacebookGraph fbGraph;
	private FacebookGraph origFbGraph;

	private ViewPanel viewPanel;

	private Stack<HashMap<Integer, String[]>> nextGeneration;
	private Stack<HashMap<Integer, String[]>> prevGeneration;

	private ListenerHandler handler;
	
	
	private JTextField[] changePhones;

	public GraphicalInterface(FacebookGraph fbGraph, MultiGraph visualGraph) {


		nextGeneration = new Stack<HashMap<Integer, String[]>>();
		prevGeneration = new Stack<HashMap<Integer, String[]>>();

		handler = new ListenerHandler(this);

		this.visualGraph = visualGraph;
		viewPanel = startVisualGraph(visualGraph);

		viewPanel.addKeyListener(handler);

		this.fbGraph = fbGraph;
		this.origFbGraph = null;

		configureSidePanels();
		configureMainFrame();
		configureMenu();

		
		// a welcome message
		JLabel welcome = new JLabel("Welcome Welcome to the Lands of Social Networks", SwingConstants.CENTER);
		welcome.setFont(welcome.getFont().deriveFont(40.0f));

		
		window.getContentPane().add(welcome, BorderLayout.CENTER);
		
		// That's all Folks!
		window.setVisible(true);
	}



	public void reinitializeStacks() {
		nextGeneration.removeAllElements();
		prevGeneration.removeAllElements();
	}
	
	

	
	public void addFullSizeLeftPanel() {




		leftSidePanel.setPreferredSize(new Dimension(200, 450));



		window.getContentPane().add(leftSidePanel, BorderLayout.WEST);

		JLabel b = new JLabel("left_left_arrow");


		b.setBounds(new Rectangle(170, 0, 40, 40));



		b.setIcon(new ImageIcon("data/img/left_arrow.png"));



		b.addMouseListener(handler);

		drawKeyNodes();

		drawKeyButton();

		leftSidePanel.add(b);

	}


	public void addReducedSizeLeftPanel() {
		leftSidePanel.setPreferredSize(new Dimension(25, 450));

		window.getContentPane().add(leftSidePanel, BorderLayout.WEST);

		JLabel b = new JLabel("left_right_arrow");
		b.setBounds(new Rectangle(-5, 0, 40, 40));


		b.setIcon(new ImageIcon("data/img/right_arrow.png"));


		b.addMouseListener(handler);


		leftSidePanel.add(b);


	}

	public void addFullSizeRightPanel() {


		rightSidePanel.setPreferredSize(new Dimension(250, 450));

		window.getContentPane().add(rightSidePanel, BorderLayout.EAST);

		JLabel b = new JLabel("right_right_arrow");
		b.setBounds(new Rectangle(-5, 0, 40, 40));

		b.setIcon(new ImageIcon("data/img/right_arrow.png"));


		b.addMouseListener(handler);


		drawGenertionButtons();

		rightSidePanel.add(b);

	}

	public void addReducedSizeRightPanel() {
		rightSidePanel.setPreferredSize(new Dimension(25, 450));

		window.getContentPane().add(rightSidePanel, BorderLayout.EAST);

		JLabel b = new JLabel("right_left_arrow");
		b.setBounds(new Rectangle(-5, 0, 40, 40));


		b.setIcon(new ImageIcon("data/img/left_arrow.png"));


		b.addMouseListener(handler);


		rightSidePanel.add(b);



	}

	public void showGraph() {
		addVisualGraphToFrame();
	}

	public void drawSmart() {
		addVisualGraphToFrame();
		addReducedSizeLeftPanel();
		addReducedSizeRightPanel();


	}
	
	public JPanel showAvailablePhones() {

		// widht*height
		// 450*350
		JPanel panel = new JPanel();
		panel.setLayout(null);

		// Available Phones
		JLabel key = new JLabel("Available Phones:");
		key.setFont(key.getFont().deriveFont(40.0f));
		key.setBounds(new Rectangle(10, 10, 350, 50));
		panel.add(key);

		String[] labels = {"Name:", "Price:"};
		int xbase = 20;
		int ybase = 100;
		int j = 0;
		for (int i = 0; i < 2; i++) {
			JLabel label = new JLabel(labels[i]);
			label.setFont(label.getFont().deriveFont(20.0f));
			label.setBounds(new Rectangle(xbase, ybase + j, 100, 20));

			j+=70;

			panel.add(label);
		}

		StringBuilder sb = new StringBuilder(64);
		sb.append("<html>number Features:</html>");
		JLabel label = new JLabel(sb.toString());
		label.setFont(label.getFont().deriveFont(20.0f));
		label.setBounds(new Rectangle(xbase, ybase + j - 50, 100, 100));
		panel.add(label);

		JSeparator separator1 = new JSeparator(JSeparator.VERTICAL);
		separator1.setBounds(new Rectangle(240, 95, 50, 200));
		panel.add(separator1);

		JSeparator separator2 = new JSeparator(JSeparator.VERTICAL);
		separator2.setBounds(new Rectangle(245, 95, 50, 200));
		panel.add(separator2);


		String[] names = new String[3];
		
		names[0] = fbGraph.getSmartPhones().getSmartPhone(1).getName();
		names[1] = Float.toString(fbGraph.getSmartPhones().getSmartPhone(1).getPrice()) + "$";
		names[2] = Integer.toString(fbGraph.getSmartPhones().getSmartPhone(1).getNumberFeatures());
		
		xbase = 150;
		ybase = 100;
		j = 0;
		for (String i : names) {
			JLabel labela = new JLabel(i);
			labela.setFont(labela.getFont().deriveFont(15.0f));
			labela.setBounds(new Rectangle(xbase, ybase + j, 100, 20));

			j+=70;

			panel.add(labela);
		}


		names[0] = fbGraph.getSmartPhones().getSmartPhone(2).getName();
		names[1] = Float.toString(fbGraph.getSmartPhones().getSmartPhone(2).getPrice()) + "$";
		names[2] = Integer.toString(fbGraph.getSmartPhones().getSmartPhone(2).getNumberFeatures());

		xbase = 300;
		ybase = 100;
		j = 0;
		for (String i : names) {
			JLabel labela = new JLabel(i);
			labela.setFont(labela.getFont().deriveFont(15.0f));
			labela.setBounds(new Rectangle(xbase, ybase + j, 100, 20));

			j+=70;

			panel.add(labela);
		}


		return panel;

	}

	public JPanel changeAvailablePhones() {

		// widht*height
		// 450*400

		JPanel panel = new JPanel();
		panel.setLayout(null);

		// Available Phones
		JLabel key = new JLabel("Available Phones:");
		key.setFont(key.getFont().deriveFont(40.0f));
		key.setBounds(new Rectangle(10, 10, 350, 50));
		panel.add(key);

		String[] labels = {"Name:", "Price:"};
		int xbase = 20;
		int ybase = 100;
		int j = 0;
		for (String i : labels) {
			JLabel label = new JLabel(i);
			label.setFont(label.getFont().deriveFont(20.0f));
			label.setBounds(new Rectangle(xbase, ybase + j, 100, 20));

			j+=70;

			panel.add(label);
		}

		StringBuilder sb = new StringBuilder(64);
		sb.append("<html>number Features:</html>");
		JLabel label = new JLabel(sb.toString());
		label.setFont(label.getFont().deriveFont(20.0f));
		label.setBounds(new Rectangle(xbase, ybase + j - 50, 100, 100));
		panel.add(label);

		JSeparator separator1 = new JSeparator(JSeparator.VERTICAL);
		separator1.setBounds(new Rectangle(240, 95, 50, 200));
		panel.add(separator1);

		JSeparator separator2 = new JSeparator(JSeparator.VERTICAL);
		separator2.setBounds(new Rectangle(245, 95, 50, 200));
		panel.add(separator2);


		
		// 6 textFields
		changePhones = new JTextField[6];

		xbase = 150;
		ybase = 100;
		j = 0;
		for (int i = 0; i < 3; i++) {
			changePhones[i] = new JTextField();
			changePhones[i].setFont(changePhones[i].getFont().deriveFont(15.0f));
			changePhones[i].setBounds(new Rectangle(xbase, ybase + j, 70, 30));

			j+=70;

			panel.add(changePhones[i]);
		}


		xbase = 300;
		ybase = 100;
		j = 0;
		for (int i = 3; i < 6; i++) {
			changePhones[i] = new JTextField();
			changePhones[i].setFont(changePhones[i].getFont().deriveFont(15.0f));
			changePhones[i].setBounds(new Rectangle(xbase, ybase + j, 70, 30));

			j+=70;

			panel.add(changePhones[i]);
		}


		JButton button = new JButton("Change");
		button.setFont(button.getFont().deriveFont(15.0f));
		button.setBounds(new Rectangle(190, 310, 100, 30));
		button.addActionListener(handler);
		
		
		panel.add(button);

		return panel;

	}


	public void showAdditionalFrame(String title, int width, int height, JPanel panel) {
		JFrame frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(window);
	}



	public void updateVisualGraph() {
		for (int i : fbGraph.getVertices()) {
			if (fbGraph.getSmartPhones().getSmartPhone(1).getName().
					equals(fbGraph.getNode(i).getCurrentPhone()))
				visualGraph.getNode(Integer.toString(i)).setAttribute("ui.class", "A");
			else if (fbGraph.getSmartPhones().getSmartPhone(2).getName().
					equals(fbGraph.getNode(i).getCurrentPhone()))
				visualGraph.getNode(Integer.toString(i)).setAttribute("ui.class", "B");
			else 
				visualGraph.getNode(Integer.toString(i)).setAttribute("ui.class", "C");
		}
	}
	
	public void aboutMe() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("<html>Hello World.<br/>My name is Oussema Hidri and this is my Capstone Project.<br/>"
				+ "Thanks to Coursera and UC San Diego team I was able to create this great project.<br/>"
				+ "For more informations or reports please contact me at: <b>d.oussema.d@gmail.com</b></html>");
		JLabel label = new JLabel(sb.toString());
		label.setFont(label.getFont().deriveFont(20.0f));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		
		showAdditionalFrame("Who ?", 600, 250, panel);
	}
	
	
	

	public JPanel getLeftSidePanel() {
		return this.leftSidePanel;
	}

	public JPanel getRightSidePanel() {
		return this.rightSidePanel;
	}

	public JFrame getWindow() {
		return this.window;
	}

	public Stack<HashMap<Integer, String[]>> getPrevGeneration() {
		return this.prevGeneration;
	}

	public Stack<HashMap<Integer, String[]>> getNextGeneration() {
		return this.nextGeneration;
	}

	public FacebookGraph getFbGraph() {
		return this.fbGraph;
	}
		
	public MultiGraph getVisualGraph() {
		return this.visualGraph;
	}

	public ViewPanel getViewPanel() {
		return this.viewPanel;
	}
	
	
	public FacebookGraph getOrigFbGraph() {
		return this.origFbGraph;
	}
	
	public JTextField[] getChangePhones() {
		return this.changePhones;
	}

	
	public void setFbGraph(FacebookGraph graph) {
		this.fbGraph = graph;
	}
	
	public void setOrigFbGraph(FacebookGraph graph) {
		this.origFbGraph = graph;
	}
	

	

	
	/**
	 * Initial configuration of the main frame.
	 */
	private void configureMainFrame() {
		// Look & Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't use system look and feel.");
		}

		window = new JFrame();
		window.setTitle("Capstone Project");
		window.setSize(1000, 700);
		window.setLocationRelativeTo(null);
		window.setResizable(true);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}


	private void configureSidePanels() {

		// Initializes left Panel
		leftSidePanel = new JPanel();
		leftSidePanel.setLayout(null);
		// Initializes right Panel
		rightSidePanel = new JPanel();
		rightSidePanel.setLayout(null);

	}

	private void configureMenu() {

		// Create menu
		Menu menu = new Menu();

		// Main menu
		String[] menus = {"Open", "Display", "Phones", "About", "Exit"};
		menu.addMenuElems(menus);

		// under Open
		String[] openItems = {"Show Graph", "Start Smart", "Centrality"};
		menu.addItemsToMenu(openItems, "Open");
		
		
		// under Display
		String[] displayItems = {"All Graph", "Get Egonet"};
		menu.addItemsToMenu(displayItems, "Display");
		
		// under Phones
		String[] phonesItems = {"Current Phones", "Change Phones"};
		menu.addItemsToMenu(phonesItems, "Phones");

		// under About
		String[] aboutItems = {"Who?"};
		menu.addItemsToMenu(aboutItems, "About");
		
		
		// add listeners to all items
		menu.addActionListenerToItems(handler);
		menu.addMenuListenerToMenu(handler);

		// That's all Folks !!!
		window.setJMenuBar(menu);
	}


	private ViewPanel startVisualGraph(MultiGraph visualGraph) {
		

		// Gets the view panel so it can be added to main Frame 
		Viewer viewer = new Viewer(visualGraph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		ViewPanel view = viewer.addDefaultView(false);

		// enables auto location of nodes
		viewer.enableAutoLayout();

		view.setLayout(null);

		view.addMouseListener(handler);

		// images for zooming in and out
		JLabel zoomIn = new JLabel("Zoom_In");
		zoomIn.setIcon(new ImageIcon("data/img/zoom_in.png"));
		zoomIn.setBounds(new Rectangle(5, 10, 50, 50));
		zoomIn.addMouseListener(handler);

		JLabel zoomOut = new JLabel("Zoom_Out");
		zoomOut.setIcon(new ImageIcon("data/img/zoom_out.png"));
		zoomOut.setBounds(new Rectangle(5, 65, 50, 50));
		zoomOut.addMouseListener(handler);

		view.add(zoomIn);
		view.add(zoomOut);




		return view;

	}

	private void addVisualGraphToFrame() {

		window.getContentPane().removeAll();

		// adds everything to the main Frame
		window.getContentPane().add(viewPanel, BorderLayout.CENTER);

	}

	private void drawKeyNodes() {


		// indicator
		JLabel keySection = new JLabel("Nodes Representation");
		keySection.setBounds(new Rectangle(2, 50, 200, 30));
		keySection.setFont(keySection.getFont().deriveFont(20.0f));
		leftSidePanel.add(keySection);

		int xbase = 50;
		int ybase = 100;

		String[] paths = {"data/img/black_dot.png", 
				"data/img/red_dot.png", 
		"data/img/blue_dot.png"};
		String[] names = fbGraph.getSmartPhones().getNamesSmartPhone();
		for (int i = 0, j = 0; i < 3; i++, j+=60) {
			// set key nodes
			JLabel node = new JLabel();
			node.setIcon(new ImageIcon(paths[i]));
			node.setText(names[i]);

			node.setFont(node.getFont().deriveFont(16.0f));

			node.setBounds(new Rectangle(xbase, ybase + j, 100, 30));

			leftSidePanel.add(node);

		}
	}


	private void drawKeyButton() {
		
		// draw randomize button
		JButton randButton = new JButton("Random");
		randButton.addActionListener(handler);
		randButton.setFont(randButton.getFont().deriveFont(16.0f));
		drawWithShadow(leftSidePanel, randButton, 58, 450, 100, 50);
	}
	
	
	public void drawCentralityPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(new Dimension(500, 400));
		
		
		StringBuilder sb = new StringBuilder(64);
		sb.append("<html>Select between the two available centralities.<br/>After selecting one it will take time to calculate"
				+ " the centrality, but you will get notified.<hr/></html>");
		JLabel label = new JLabel(sb.toString(), SwingConstants.CENTER);
		label.setFont(label.getFont().deriveFont(16.0f));
		label.setBounds(new Rectangle(0, 0, 500, 200));
		panel.add(label);
		
		
		String [] buttons = {"Betweeness", "Closeness"};
		
		int xbase = 80, ybase = 180, width = 120, height = 50, j = 0;
		for (String i : buttons) {
			JButton button = new JButton(i);
			button.addActionListener(handler);
			button.setBounds(new Rectangle(xbase + j, ybase, width, height));
			panel.add(button);
			
			j += 200;
		}
		
		
		showAdditionalFrame("Centralities", 500, 300, panel);
	}



	private void drawGenertionButtons() {

		// set the key text of the generation buttons
		JLabel whatToDo = new JLabel("Which generation ?");
		whatToDo.setFont(whatToDo.getFont().deriveFont(20.0f));
		whatToDo.setBounds(new Rectangle(40, 70, 200, 50));
		rightSidePanel.add(whatToDo);


		// Buttons' text
		String[] buttons = {"Next", "Previous", "Restart"};

		// base coordinates
		int xbase = 70;
		int ybase = 150;

		for (int i = 0, j = 0; i < 3; i++, j+=100) {
			JButton button = new JButton(buttons[i]);
			button.addActionListener(handler);

			// draw buttons with shadow
			drawWithShadow(rightSidePanel, button, xbase, ybase + j, 100, 50);
		}
	}
	


	private void drawWithShadow(JPanel panel, Component comp, int x, int y, int width, int height) {

		JLabel shadow = new JLabel();

		shadow.setBounds(new Rectangle(x + 5, y + 5, width, height));
		shadow.setBackground(new Color(73, 73, 73));
		shadow.setOpaque(true);

		shadow.setFocusable(false);

		panel.add(shadow);

		comp.setBounds(new Rectangle(x, y, width, height));


		panel.add(comp);
	}


	

}
