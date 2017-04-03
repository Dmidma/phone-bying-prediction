package graph.graphical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import graph.Centrality;
import graph.FacebookGraph;

/**
 * This class will handle the events of the GUI that is implemented in
 * GraphicalInterface class.
 * @author Oussema Hidri d.oussema.d@gmail.com
 *
 */
public class ListenerHandler implements ActionListener, MouseListener, MenuListener, KeyListener {


	private GraphicalInterface gui;


	public ListenerHandler(GraphicalInterface gui) {
		this.gui = gui;

	}



	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuSelected(MenuEvent e) {

		if ("Exit".equals(((JMenuItem)e.getSource()).getText())) {
			System.exit(0);
		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		if (arg0.getSource() instanceof JLabel) {
			String cmd = ((JLabel)arg0.getSource()).getText();

			if ("Zoom_In".equals(cmd)) {

				double currentViewPercent = gui.getViewPanel().getCamera().getViewPercent();
				currentViewPercent -= 0.1;
				if (currentViewPercent > 0) {
					gui.getViewPanel().getCamera().setViewPercent(currentViewPercent);
				}
			}
			else if ("Zoom_Out".equals(cmd)) {
				double currentViewPercent = gui.getViewPanel().getCamera().getViewPercent();
				currentViewPercent += 0.1;
				if (currentViewPercent < 100) {
					gui.getViewPanel().getCamera().setViewPercent(currentViewPercent);
				}

			}
			else if ("left_left_arrow".equals(cmd)) {
				gui.getLeftSidePanel().removeAll();
				gui.getWindow().getContentPane().remove(gui.getLeftSidePanel());
				gui.addReducedSizeLeftPanel();
			}
			else if ("right_right_arrow".equals(cmd)) {
				gui.getRightSidePanel().removeAll();
				gui.getWindow().getContentPane().remove(gui.getRightSidePanel());
				gui.addReducedSizeRightPanel();
			}
			else if ("left_right_arrow".equals(cmd)) {
				gui.getLeftSidePanel().removeAll();
				gui.getWindow().getContentPane().remove(gui.getLeftSidePanel());
				gui.addFullSizeLeftPanel();
			}
			else if ("right_left_arrow".equals(cmd)) {

				gui.getRightSidePanel().removeAll();
				gui.getWindow().getContentPane().remove(gui.getRightSidePanel());
				gui.addFullSizeRightPanel();
			}

			gui.getWindow().repaint();
			gui.getWindow().validate();


		}


	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if ("Who?".equals(e.getActionCommand())) {
			gui.aboutMe();
		}
		else if ("Get Egonet".equals(e.getActionCommand())) {

			egonetSection();

		}
		else if ("All Graph".equals(e.getActionCommand())) {
			if (gui.getOrigFbGraph() != null) {
				gui.setFbGraph(gui.getOrigFbGraph());
				gui.setOrigFbGraph(null);
				gui.getVisualGraph().clear();
				gui.getFbGraph().toVisualGraph(gui.getVisualGraph());
			}

		}
		else if ("Show Graph".equals(e.getActionCommand())) {
			gui.showGraph();
			gui.getWindow().repaint();
			gui.getWindow().validate();
		}
		else if ("Start Smart".equals(e.getActionCommand())) {
			gui.drawSmart();
			gui.getWindow().repaint();
			gui.getWindow().validate();
		}
		else if ("Current Phones".equals(e.getActionCommand())) {
			gui.showAdditionalFrame("Current Phones", 450, 350, gui.showAvailablePhones());
		}
		else if ("Change Phones".equals(e.getActionCommand())) {

			gui.showAdditionalFrame("Change Phones", 450, 400, gui.changeAvailablePhones());

		}
		else if ("Next".equals(e.getActionCommand())) {

			// add current state to previous stack
			gui.getPrevGeneration().push(gui.getFbGraph().snapOfCurrentPhones());

			// if the next generation exists already in the stack
			if (!gui.getNextGeneration().isEmpty()) {
				gui.getFbGraph().loadPhones(gui.getNextGeneration().pop());
			}
			else {
				// update phones in Facebook graph
				gui.getFbGraph().updatePhones(gui.getFbGraph().nextPhoneGeneration());
			}

			gui.updateVisualGraph();
		}
		else if ("Previous".equals(e.getActionCommand())) {


			if (!gui.getPrevGeneration().isEmpty()) {
				gui.getNextGeneration().push(gui.getFbGraph().snapOfCurrentPhones());
				gui.getFbGraph().loadPhones(gui.getPrevGeneration().pop());
				gui.updateVisualGraph();
			}
		}
		else if ("Restart".equals(e.getActionCommand())) {

			boolean notOldest = false;
			if (!gui.getPrevGeneration().isEmpty())
				notOldest = true;

			while (!gui.getPrevGeneration().isEmpty()) {
				gui.getNextGeneration().push(gui.getPrevGeneration().pop());
			}

			if (!gui.getNextGeneration().isEmpty() && notOldest) {
				gui.getFbGraph().loadPhones(gui.getNextGeneration().pop());

				gui.updateVisualGraph();
			}
		}
		else if ("Random".equals(e.getActionCommand())) {
			gui.getFbGraph().randomizePhones();
			gui.updateVisualGraph();
			gui.reinitializeStacks();
		}
		else if ("Change".equals(e.getActionCommand())) {

			// change phones for fbGraph
			changePhones();

		}
		else if ("Centrality".equals(e.getActionCommand())) {

			gui.drawCentralityPanel();

		}
		else if ("Betweeness".equals(e.getActionCommand())) {

			Centrality betwCen = new Centrality(gui, gui.getFbGraph().exportGraph(), "Betweeness");

			Thread thread = new Thread(betwCen);

			thread.start();

			JOptionPane.showMessageDialog(gui.getWindow(), "Calculating ...");
		}
		else if ("Closeness".equals(e.getActionCommand())) {

			Centrality closCen = new Centrality(gui, gui.getFbGraph().exportGraph(), "Closeness");

			Thread thread = new Thread(closCen);

			thread.start();

			JOptionPane.showMessageDialog(gui.getWindow(), "Calculating ...");

		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// zoom in
		if (107 == e.getKeyCode()) {
			double currentViewPercent = gui.getViewPanel().getCamera().getViewPercent();
			currentViewPercent -= 0.1;
			if (currentViewPercent > 0) {
				gui.getViewPanel().getCamera().setViewPercent(currentViewPercent);
			}
		}
		// zoom out
		else if (109 == e.getKeyCode()) {
			double currentViewPercent = gui.getViewPanel().getCamera().getViewPercent();
			currentViewPercent += 0.1;
			if (currentViewPercent < 100) {
				gui.getViewPanel().getCamera().setViewPercent(currentViewPercent);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}



	/**
	 * This method is missing the control of all cases.
	 * i.e. the case of price is a string not a float, etc ...
	 */
	private void changePhones() {
		JTextField[] fields = gui.getChangePhones();

		// make sure none is empty.
		for (JTextField i : fields) {
			if (i.getText().equals("") || i.getText() == null) {
				JOptionPane.showMessageDialog(gui.getWindow(), "Must enter all fields");
				return;
			}
		}


		gui.getFbGraph().getSmartPhones().getSmartPhone(1).setName(fields[0].getText());
		gui.getFbGraph().getSmartPhones().getSmartPhone(1).setPrice(Float.parseFloat(fields[1].getText()));
		gui.getFbGraph().getSmartPhones().getSmartPhone(1).setNumberFeatures(Integer.parseInt(fields[2].getText()));

		gui.getFbGraph().getSmartPhones().getSmartPhone(2).setName(fields[3].getText());
		gui.getFbGraph().getSmartPhones().getSmartPhone(2).setPrice(Float.parseFloat(fields[4].getText()));
		gui.getFbGraph().getSmartPhones().getSmartPhone(2).setNumberFeatures(Integer.parseInt(fields[5].getText()));

		// randomize phones and empty stacks
		gui.getFbGraph().randomizePhones();
		gui.reinitializeStacks();

		JOptionPane.showMessageDialog(gui.getWindow(), "Done!");

	}


	private void egonetSection() {

		String id = JOptionPane.showInputDialog(gui.getWindow(), "Enter the id of node: ");

		try {
			int nodeId = Integer.parseInt(id);
			// the original Facebook Graph is loaded in origFbGraph
			if (gui.getOrigFbGraph() != null) {
				if (gui.getOrigFbGraph().hasNodeId(nodeId)) {
					gui.setFbGraph((FacebookGraph)gui.getOrigFbGraph().getEgonet(nodeId));
				}
				else {
					JOptionPane.showMessageDialog(gui.getWindow(), "Node Id does not exist in the graph!");
				}
			}
			else {
				if (gui.getFbGraph().hasNodeId(nodeId)) {
					gui.setOrigFbGraph(gui.getFbGraph());
					gui.setFbGraph((FacebookGraph) gui.getFbGraph().getEgonet(nodeId));
				}
				else {
					JOptionPane.showMessageDialog(gui.getWindow(), "Node Id does not exist in the graph!");
				}
			}


			// copy smart phones and updta visual graph
			gui.getFbGraph().setSmartPhones(gui.getOrigFbGraph().getSmartPhones());
			gui.getVisualGraph().clear();
			gui.getFbGraph().toVisualGraph(gui.getVisualGraph());



			JOptionPane.showMessageDialog(gui.getWindow(), "Done!");
		} catch (NumberFormatException exc) {

			JOptionPane.showMessageDialog(gui.getWindow(), "Please enter a valid node id");
		}
	}
}
