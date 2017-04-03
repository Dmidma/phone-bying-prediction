package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import graph.CapGraph;

public class OneTimeParse {


	public static void main(String[] args) {

		CapGraph g = new CapGraph();
	
		read("data/facebook_data/facebook_combined.txt", g);

	}



	/**
	 * This method will read the content of the file and return it.
	 * It will return a null if the path of the file does not exist
	 * @param path the path of the opened file.
	 * @return the content of the file
	 */
	public static void read(String path, graph.CapGraph g) {

		// open the file
		File file = new File(path);


		// if the file does not exist return null
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


		System.out.println(g.getNumberEdges());
		System.out.println(g.getNumVertices());

	}

}
