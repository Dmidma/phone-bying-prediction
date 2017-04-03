package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;


import graph.graphical.GraphicalInterface;


/**
 * This class is used to calculate "Betweeness Centrality" and "Closeness Centrality".
 * It is separated from other classes and implements Runnable, so it can run the algorithms in 
 * separated thread. 
 * @author Oussema Hidri d.oussema.d@gmail.com
 *
 */
public class Centrality implements Runnable {
	
	
	// representation of the nodes and edges
	private HashMap<Integer, HashSet<Integer>> vertices;
	
	// type of algorithm that will be run on the set of data
	private String type;
	
	// the result of either algorithms will be set on this map
	private HashMap<Integer, Double> result;
	
	// reference to the graphical interface
	// in case of usage without one, this will be set to null
	private GraphicalInterface gui;
	
	/**
	 * The only constructor of this class.
	 * It will take gui if the class is used in a graphical interface, the set of nodes and edges, 
	 * and finally the type of algorithm that will be run.
	 * @param gui reference to the graphical interface.
	 * @param vertices set of nodes and edges.
	 * @param type type of algorithm, either "Betweeness" or "Closeness".
	 */
	public Centrality(GraphicalInterface gui, HashMap<Integer, HashSet<Integer>> vertices, String type) {
		this.vertices = vertices;
		this.type = type;
		this.gui = gui;
	}
	
	
	// getter for the result
	public HashMap<Integer, Double> getResult() {
		return this.result;
	}


	/**
	 * I will try to calculate the Closeness Centrality for each node in the current Graph.
	 *
	 * To determine the distance between two pairs of nodes, I will use "Dijkstra" algorithm.
	 *
	 * Using "Dijkstra" will not be a wise choice, since we will not use any priority to determine 
	 * the next node that will be selected.
	 *
	 *
	 * To be able to calculate the path between two pairs of nodes in the graph, I need to initialize the 
	 * distance of the edges at 1, so I can be able to use previous version of the Dijkstra algorithm.	
	 * 
	 */

	// Probably need to return the distance between the two given nodes.
	// Have a structure that contains two nodes, and a distance associated to it.
	// Each time look into the structure and determine if the graph already contains the two nodes.
	// After finishing the calculation of the distance of all pairs of nodes, we can determine
	// the closeness centrality.


	/*
	BFS doesn't account for edge weights, only number of edges. (which I am working on right now)
	 */

	private boolean BFSSearch(int start, int goal, HashMap<Integer, Integer> parentMap) {

		// Using the HashSet to mark the visited Nodes
		HashSet<Integer> visited = new HashSet<Integer>();
		// Using the Queue to mark the nodes that will be visited
		Queue<Integer> toExplore = new LinkedList<Integer>();

		// starting point is the start node
		toExplore.add(start);

		// Start searching
		boolean found = false;
		while (!toExplore.isEmpty()) {
			int curr = toExplore.remove();

			// Break off the while loop if the we reached our goal
			if (curr == goal) {	
				found = true;
				break;
			}

			// Get a list of neighbors of the current node
			List<Integer> neighbors = getNeighbors(curr);
			ListIterator<Integer> it = neighbors.listIterator(neighbors.size());

			while (it.hasPrevious()) 
			{
				int next = it.previous();
				if (!visited.contains(next)) 
				{
					visited.add(next);
					parentMap.put(next, curr);
					toExplore.add(next);
				}
			}
		}

		return found;
	}


	/**
	 * Modified BFS, this method method will try to find the shortest path between 
	 * start node and goal node, it will also save the distance between "x-y" node 
	 * so it will not recalculate BFS for neither "x-y" nor "y-x".
	 * @param start the starting node.
	 * @param goal the ending node.
	 * @param distances the map that will save distances of other nodes between start 
	 * and goal nodes.
	 * @return the path between start and goal.
	 * @see BFS(int start, int goal)
	 */
	public List<Integer> BFS(int start, int goal, HashMap<String, Integer> distances) {

		// Make sure that the start and goal locations are valid
		if (!vertices.keySet().contains(start) || !vertices.keySet().contains(goal)) {
			throw new IllegalArgumentException("Start or goal node does not exist in the graph");
		}


		// Check if there's a path between start, and goal nodes
		HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
		boolean found = BFSSearch(start, goal, parentMap);


		// No path found
		if (!found) {
			System.out.println("No path exists");
			// retrun an empty list
			return new ArrayList<Integer>();
		}

		// If we are here, then a path was found and we need to return right ordered list
		LinkedList<Integer> path = new LinkedList<Integer>();
		int curr = goal;
		while (curr != start)
		{
			path.addFirst(curr);
			curr = parentMap.get(curr);
		}
		path.addFirst(start);


		// added calculated distance
		// the shortest path between two points contains also shortest path of other nodes
		for (int i = 1; i < path.size(); i++) {
			for (int j = path.size() - 1; j > (i + 1); j--) {
				String ij = new String(path.get(i) + "-" + path.get(j));
				String ji = new String(path.get(j) + "-" + path.get(i));

				if (!distances.containsKey(ij) && 
						!distances.containsKey(ji))
					distances.put(ij, j - i);
			}

		}

		// That's ll Folks!
		return path;
	}

	
	/**
	 * The default BFS, this method will try to find the shortest path between start 
	 * and goal using BFS algorithm.
	 * @param start the starting node.
	 * @param goal the ending node.
	 * @return the path from start to goal.
	 */
	public List<Integer> BFS(int start, int goal) {

		// Make sure that the start and goal locations are valid
		if (!vertices.keySet().contains(start) || !vertices.keySet().contains(goal)) {
			throw new IllegalArgumentException("Start or goal node does not exist in the graph");
		}


		// Check if there's a path between start, and goal nodes
		HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
		boolean found = BFSSearch(start, goal, parentMap);


		// No path found
		if (!found) {
			System.out.println("No path exists");
			// retrun an empty list
			return new ArrayList<Integer>();
		}

		// If we are here, then a path was found and we need to return right ordered list
		LinkedList<Integer> path = new LinkedList<Integer>();
		int curr = goal;
		while (curr != start)
		{
			path.addFirst(curr);
			curr = parentMap.get(curr);
		}
		path.addFirst(start);


		return path;

	}


	/**
	 * This method will return the closeness centrality of every node in the graph.
	 * This method will "Brute Force" it's way to determine the closenes centrality 
	 * of each node.
	 * @return the map which will contain each node and its closeness value.
	 */
	public HashMap<Integer, Double> closenessCentrality() {


		int n_1 = vertices.keySet().size() - 1;

		// Initialize the map
		HashMap<Integer, Double> closeness = new HashMap<Integer, Double>();


		// Initialize the map that will hold the calculated distances
		HashMap<String, Integer> distances = new HashMap<String, Integer>();

		// for each node of the graph
		// O(N²) already  !!!!
		for (int i : vertices.keySet()) {

			// put the current node in the map
			// add the number of adjacent node the map
			closeness.put(i, (double) vertices.get(i).size());

			// remove current node and every adjacent node to it
			Set<Integer> otherNodes = new HashSet<Integer>(vertices.keySet());

			for (int x : getNeighbors(i)) {
				otherNodes.remove(x);
			}
			int currentDistance = 0;
			// for each of the other nodes in the graph
			for (int j : otherNodes) {
				double oldValue = closeness.get(i);

				String ij = new String(i + "-" + j);
				String ji = new String(j + "-" + i);

				// check if the distance is already in the map so we can avoid
				// reusing of BFS
				if (distances.containsKey(ji) || distances.containsKey(ij)) {
					
					// the distance between i-j is the same as j-i
					// the map will holds one of them and not both.
					if (distances.get(ij) != null) {
						currentDistance = distances.get(ij);
					}
					else {
						currentDistance = distances.get(ji);
					}
				}
				// calculate distance using BFS
				else {
					currentDistance = BFS(i, j, distances).size() - 1;
					distances.put(ij, currentDistance);
				}


				// add the current distance to the old value
				closeness.replace(i, oldValue + currentDistance);
			}


			// A this point we need to calculate the inverse of the sum
			// and divide it with (N - 1) to normalize it
			closeness.replace(i, ((1 /closeness.get(i)) / n_1));

		}


		return closeness;
	}


	/**
	 * This method will calculate Betweeness Centrality using the implementation of 
	 * "A Faster Algorithm for Betweeness Centrality by Ulrik Brandes". 
	 * @return the map which will contains each node and its Betweeness value.
	 */
	public HashMap<Integer, Double> betweenessCentrality() {

		// Global initialization
		HashMap<Integer, Double> bCMap = new HashMap<Integer, Double>();
		for (int i : vertices.keySet()) {
			bCMap.put(i, 0.0);
		}

		for (int s : vertices.keySet()) {

			// Local initialization
			Stack<Integer> S = new Stack<Integer>(); 

			Queue<Integer> Q = new LinkedList<Integer>();

			HashMap<Integer, ArrayList<Integer>> P = new HashMap<Integer, ArrayList<Integer>>();

			HashMap<Integer, Double> sigma = new HashMap<Integer, Double>();
			HashMap<Integer, Integer> d = new HashMap<Integer, Integer>();

			for (int t : vertices.keySet()) {
				P.put(t, new ArrayList<Integer>());
				sigma.put(t, 0.0);
				d.put(t, -1);
			}

			sigma.replace(s, 1.0);
			d.replace(s, 0);
			Q.add(s);

			// BFS traversal

			while (!Q.isEmpty()) {
				int v = Q.poll();
				S.push(v);

				for (int w : vertices.get(v)) {

					// w found for the first time
					if (d.get(w) < 0) {
						Q.add(w);
						d.replace(w, d.get(v) + 1);
					}

					if (d.get(w) == (d.get(v) + 1)) {
						sigma.replace(w, sigma.get(w) + sigma.get(v));
						P.get(w).add(v);
					}
				}
			}

			// Dependency accumulation
			HashMap<Integer, Double> Si = new HashMap<Integer, Double>();
			for (int j : vertices.keySet()) {
				Si.put(j, 0.0);
			}

			while (!S.isEmpty()) {
				int w = S.pop();

				for (int v : P.get(w)) {
					Si.replace(v, Si.get(v) + ((sigma.get(v) / sigma.get(w)) * (1 + Si.get(w))));

				}

				if (w != s) {
					bCMap.replace(w, bCMap.get(w) + Si.get(w));

				}
			}
		}

		return bCMap;
	}


	/**
	 * This method is used as a helper method to calculate Top-k Closeness Centrality.
	 * It's the implementation of the algorithm in the paper "Computing Top-k Closeness Centrality
	 * Faster in Unweighted Graphs by Elisabetta Bergamini and Henning Meyerhenke".
	 * 
	 * @return the map of neighbor based lower bounds.	
	 */
	private HashMap<Integer, Integer> neighbBasedLowerBound() {

		HashMap<Integer, Integer> lowerbounds = new HashMap<Integer, Integer>();

		HashMap<Integer, HashMap<Integer, Integer>> levels = new HashMap<Integer, HashMap<Integer, Integer>>();

		HashMap<Integer, Integer> nVisited = new HashMap<Integer, Integer>();

		HashMap<Integer, Boolean> finished = new HashMap<Integer, Boolean>();

		int k = 2;

		for (int v : vertices.keySet()) {
			int currentDeg = vertices.get(v).size();
			levels.put(v, new HashMap<Integer, Integer>());
			levels.get(v).put(k - 1, currentDeg);
			lowerbounds.put(v, currentDeg);
			nVisited.put(v, currentDeg + 1);
			finished.put(v, false);
		}

		int nFinished = 0;

		while (nFinished < vertices.size()) {
			for (int s : vertices.keySet()) {
				int currentDeg = vertices.get(s).size();
				int sum = 0;
				for (int w : vertices.get(s)) {
					sum += levels.get(w).get(k - 1);
				}
				
				if (k == 2)
					sum -= currentDeg;
				else 
					sum -= (levels.get(s).get(k - 2))*(currentDeg - 1);

				levels.get(s).put(k, sum);
			}

			for (int s : vertices.keySet()) {
				if (finished.get(s)) {
					continue;
				}

				levels.get(s).replace(k - 2, levels.get(s).get(k - 1));
				levels.get(s).replace(k - 1, levels.get(s).get(k));

				if (vertices.size() - nVisited.get(s) > levels.get(s).get(k - 1)) {
					lowerbounds.replace(s, lowerbounds.get(s) + k * (levels.get(s).get(k - 1)));
					nVisited.replace(s, nVisited.get(s) + levels.get(s).get(k - 1));
				}
				else {
					lowerbounds.replace(s, lowerbounds.get(s) + k * (vertices.size() - nVisited.get(s)));
					nVisited.replace(s, vertices.size());
					nFinished++;
					finished.replace(s, true);
				}
			}

			k++;
		}

		return lowerbounds;
	}

	/**
	 * This method is used to calculate Top-k Closeness Centrality.
	 * It's the implementation of the algorithm in the paper "Computing Top-k Closeness Centrality
	 * Faster in Unweighted Graphs by Elisabetta Bergamini and Henning Meyerhenke".
	 * 
	 * @return the map that will contains top-k nodes and its closeness value.
	 */
	public HashMap<Integer, Double> topKCloseness(int k) {

		HashMap<Integer, Integer> lowerBounds = neighbBasedLowerBound();

		PriorityQueue<LowerBoundsPriority> queue = new PriorityQueue<LowerBoundsPriority>();

		HashMap<Integer, Boolean> exact = new HashMap<Integer, Boolean>();

		for (int v : vertices.keySet()) {
			queue.add(new LowerBoundsPriority(v, lowerBounds.get(v)));
			exact.put(v, false);
		}

		int i = 0;
		HashMap<Integer, Double> topK = new HashMap<Integer, Double>();

		while (i < k) {
			LowerBoundsPriority current = queue.poll();

			if (exact.get(current.getNode())) {
				
				topK.put(current.getNode(), Double.parseDouble(Integer.toString(current.getPriority())));
				i++;
			}
			else {
				int sum = 0;
				for (int w : vertices.keySet())
					if (w != current.getNode())
						sum += BFS(current.getNode(), w).size() - 1;
			
				current.setPriority(sum);
				queue.add(current);
				exact.replace(current.node, true);
				lowerBounds.replace(current.getNode(), sum);
			}
		}

		return topK;

	}
	
	
	/**
	 * This is a helper method that will take a node as argument and return 
	 * a list of neighbor nodes.
	 * @param v the node id.
	 * @return a list of all neighbor nodes.
	 */
	private List<Integer> getNeighbors(int v) {

		ArrayList<Integer> neighbors = new ArrayList<Integer>();

		for (int i : vertices.get(v)) {
			neighbors.add(i);
		}

		return neighbors;
	}
	
	
	/**
	 * This Class will be used to set the PriorityQueue in the topKCloseness.
	 * It will contains the node and its priority. 
	 */
	private class LowerBoundsPriority implements Comparable<LowerBoundsPriority> {


		private int node;
		private int priority;

		public LowerBoundsPriority(int node, int priority) {
			this.node = node;
			this.priority = priority;
		}

		@Override
		public int compareTo(LowerBoundsPriority arg0) {
			if (this.priority < arg0.getPriority())
				return -1;

			if (this.priority > arg0.getPriority())
				return 1;

			return 0;
		}

		public int getNode() {
			return this.node;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public int getPriority() {
			return this.priority;
		}

	}


	
	@Override
	public void run() {
		
		if ("Betweeness".equals(type)) {
			result = betweenessCentrality();
		}
		else if ("Closeness".equals(type)) {
			result = topKCloseness(5);
		}
		
		
		// if the user used the class without a graphical interface
		// print the result to out stream.
		if (gui == null) {
			for (int i : result.keySet()) {
				System.out.println(i + "->" + result.get(i));
			}
			return;
		}
		
		// display Dialog
		JOptionPane.showMessageDialog(gui.getWindow(), "Done calculating Centrality!");
		
		// display Dialog
		JOptionPane.showMessageDialog(gui.getWindow(), "Go to \"Open > Show\" Graph, so you can see the 5 nodes with higher centrality values");
	
		
		
		for (int i : gui.getFbGraph().getVertices()) {
			gui.getVisualGraph().getNode(Integer.toString(i)).removeAttribute("ui.class");
			
		}
		
		PriorityQueue<TopNodes> topQueue = new PriorityQueue<TopNodes>();
		for (int i : result.keySet()) {
			
			topQueue.add(new TopNodes(i, result.get(i)));
		}
		
		if ("Betweeness".equals(type)) {
			for (int i = 1; i <= 5; i++) {
				String currentNode = Integer.toString(topQueue.poll().node);
				gui.getVisualGraph().getNode(currentNode).setAttribute("ui.class", "cent" + i);
			}
		}
		else {
			for (int i = 5; i >= 1; i--) {
				String currentNode = Integer.toString(topQueue.poll().node);
				gui.getVisualGraph().getNode(currentNode).setAttribute("ui.class", "cent" + i);
			}
		}
		
		
		
		
		
		
	}
	
	
	/**
	 * This class is used to determine the top 5 nodes in the run method.
	 *
	 */
	private class TopNodes implements Comparable<TopNodes> {


		private int node;
		private double priority;

		public TopNodes(int node, double priority) {
			this.node = node;
			this.priority = priority;
		}

		@Override
		public int compareTo(TopNodes arg0) {
			if (this.priority < arg0.getPriority())
				return 1;

			if (this.priority > arg0.getPriority())
				return -1;

			return 0;
		}
		
		
		public double getPriority() {
			return this.priority;
		}

	}


}
