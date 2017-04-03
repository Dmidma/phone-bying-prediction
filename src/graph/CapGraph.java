/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	// main sturctures
	private HashMap<Integer, HashSet<Integer>> vertices;
	
	private long edgesNumber;


	public CapGraph() {
		this.vertices = new HashMap<Integer, HashSet<Integer>>();
		this.edgesNumber = 0;
	}



	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {

		if (!vertices.containsKey(num)) {
			vertices.put(num, new HashSet<Integer>());
		}

	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		if (vertices.containsKey(from) && !vertices.get(from).contains(to)) {
			vertices.get(from).add(to);
			
			if ((vertices.containsKey(to) && 
					!vertices.get(to).contains(from)) || 
					(!vertices.containsKey(to)))
			edgesNumber++;
		}

	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {

		// Initialize the returned graph
		Graph retGraph = new CapGraph();


		// if the graph does not contain the center node, return an empty graph
		if (!vertices.containsKey(center)) {
			return retGraph;
		}

		// add the center node to the new graph
		retGraph.addVertex(center);

		// iterate over the neighbors
		for (int i : vertices.get(center)) {


			// add the neighbor node to the graph
			retGraph.addVertex(i);
			// add the edge to the graph
			retGraph.addEdge(center, i);

			// iterate over the neighbor edges and add only that have connection with 
			// the neighbors of the center node
			for (int j : vertices.get(i)) {
				if (vertices.get(center).contains(j)) {
					retGraph.addEdge(i, j);
				}
			}
			
			retGraph.addEdge(i, center);
		}


		return retGraph;
	}


	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub

		Stack<Integer> vertices = new Stack<Integer>();
		for (int i : this.vertices.keySet()) {
			vertices.push(i);
		}

		// compute transpose of graph
		CapGraph GT = new CapGraph();
		for (int i : getVertices()) {
			GT.addVertex(i);
			for (int j : getVertices()) {
				if (j != i && getNeighbors(j).contains(i)) {
					GT.addEdge(i, j);
				}
			}	
		}

		return DFSList(GT, DFS(this, vertices));

	}



	private Stack<Integer> DFS(Graph g, Stack<Integer> vertices) {
		
		// initialize the visited set and the finished stack
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();

		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				DFSVisit(g, v, visited, finished);
			}
		}
		return finished;
	}



	private LinkedList<Graph> DFSList(Graph g, Stack<Integer> vertices) {
		
		// initialize the visited set and the finished stack
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		
		// initialize the list of sub-graphs
		LinkedList<Graph> list = new LinkedList<Graph>();
		
		
		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				DFSVisit(g, v, visited, finished);

				// create a sub-graph
				CapGraph gr = new CapGraph();
				
				// iterate over the nodes of the finished stack
				// and add them to the stack
				while (!finished.isEmpty()) {
					gr.addVertex(finished.pop());
				}

				// fixing edges
				HashSet<Integer> gVert = ((CapGraph)gr).getVertices();
				for (int i : gVert) {
					for (int j : gVert) {
						if (i != j && getNeighbors(i).contains(j)) {
							gr.addEdge(i, j);
						}
					}
				}
				// add the sub-graph to the list
				list.add(gr);
			}
		}
		
		// That's it Folks!
		return list;
	}


	private void DFSVisit(Graph g, int v, HashSet<Integer> visited, Stack<Integer> finished) {

		visited.add(v);
		for (int i : ((CapGraph)g).getNeighbors(v)) {
			if (!visited.contains(i)) {
				DFSVisit(g, i, visited, finished);
			}
		}
		finished.push(v);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {

		return new HashMap<Integer, HashSet<Integer>>(vertices);
	}


	/**
	 * This method will return the number of vertices in the graph.
	 * @return number of vertices.
	 */
	public int getNumVertices() {
		return this.vertices.size();
	}

	public HashSet<Integer> getNeighbors(int v) {
		return new HashSet<Integer>(vertices.get(v));
	}

	public HashSet<Integer> getVertices() {
		return new HashSet<Integer>(this.vertices.keySet());
	}
	
	public long getNumberEdges() {
		return this.edgesNumber;
	}
}
