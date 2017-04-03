package graph.graphical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


import org.graphstream.graph.implementations.MultiGraph;

import graph.FacebookGraph;



/**
 * The same of the class say it all.
 * To launch the GUI, you need to run this class.
 * @author Oussema Hidri d.oussema.d@gmail.com
 *
 */
public class Launch {


	public static void main(String[] args) {
		
		
		// path of the file containing the nodes and edges
		//String pathFacebookData = "data/facebook_1000.txt"; 
		String pathFacebookData = "data/facebook_data/facebook_combined.txt"; 
		//String pathFacebookData = "data/facebook_2000.txt";  
		// Initialize graph
		FacebookGraph fG = new FacebookGraph();
		// visual graph
		MultiGraph visualGraph = new MultiGraph("Facebook Visual Graph");

		
		// Initialize smart phones
		fG.initSmartPhones();
		fG.getSmartPhones().addSmartPhone("IPhone", 600.3f, 50);
		fG.getSmartPhones().addSmartPhone("SamsungG", 550.10f, 53);


	
		// load data to graph
		loadData(pathFacebookData, fG);
		// create the visual graph
		fG.toVisualGraph(visualGraph);
		
		// Let the show go on
		new GraphicalInterface(fG, visualGraph);

	}



	
	/**
	 * This static method will load the content of the file given by path into FacebookGraph.
	 * The content of the file must contain nodes' id separated by space.
	 * Each line must contain only two nodes, which will represent an edge between these two nodes.
	 * @param path the path of the data file.
	 * @param g the FacebookGraph that will get loaded.
	 */
	public static void loadData(String path, FacebookGraph g) {

		// open the file
		File file = new File(path);

		// if the file does not exist, stop everything
		if (!file.exists())
			return;


		// write to the file
		try {
			// open stream
			FileReader fReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fReader);


			// read all the content of the file
			String current = bufferedReader.readLine();
			while (current != null) {
				String[] currentLineNodes = current.split(" ");

				int firstNode = Integer.parseInt(currentLineNodes[0]);
				int secondNode = Integer.parseInt(currentLineNodes[1]);

				g.addVertex(firstNode);
				g.addVertex(secondNode);

				g.addEdge(firstNode, secondNode);
				g.addEdge(secondNode, firstNode);

				current = bufferedReader.readLine();
			}


			// close anything was opened
			bufferedReader.close();
			fReader.close();

		} catch (Exception e) {

		}

	}




}
