package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;



import org.graphstream.graph.implementations.MultiGraph;


/**
 * This class will represent the Facebook Graph.
 * It will contain the smart phones, Facebook nodes, and nodes/edges.
 * @author Oussema Hidri d.oussema.d@gmail.com
 *
 */
public class FacebookGraph implements Graph {



	private HashMap<Integer, HashSet<Integer>> vertices;

	private HashMap<Integer, FacebookNode> nodesId;

	private int graphEdgesNumber;

	// All smart Phones
	private SmartPhones smartPhones;


	public FacebookGraph() {

		this.vertices = new HashMap<Integer, HashSet<Integer>>();
		this.nodesId = new HashMap<Integer, FacebookNode>();
		this.graphEdgesNumber = 0;

	}


	public void initSmartPhones() {
		this.smartPhones = new SmartPhones();
	}
	
	
	public SmartPhones getSmartPhones() {
		return this.smartPhones;
	}
	
	public void setSmartPhones(SmartPhones smartPhones) {
		this.smartPhones = smartPhones;
	}
	
	
	/**
	 * This method will randomize the phones of the nodes of the current graph.
	 */
	public void randomizePhones() {
		
		for (int i : nodesId.keySet()) {
			// set a random previous phone
			nodesId.get(i).setPreviousPhone(smartPhones.getRandomPhone());
			
			// set a current phone
			nodesId.get(i).setCurrentPhone(smartPhones.getRandomPhone());
			
			// It is ok to have a current phone "N/A" if the previous one
			// is also "N/A"
			while ("N/A".equals(nodesId.get(i).getCurrentPhone()) && 
					!"N/A".equals(nodesId.get(i).getPreviousPhone())) {
				
				nodesId.get(i).setCurrentPhone(smartPhones.getRandomPhone());
			}
			
		}
		
		for (int i : nodesId.keySet()) {
			for (int j : vertices.get(i)) {
				nodesId.get(i).addPhoneToFriend(j, nodesId.get(j).getCurrentPhone());
			}
		}
	}
	
	
	@Override
	public void addVertex(int num) {

		if (!vertices.containsKey(num)) {
			vertices.put(num, new HashSet<Integer>());

			// create a Node with the specified ID
			nodesId.put(num, new FacebookNode());
		}

	}

	@Override
	public void addEdge(int from, int to) {

		if (vertices.containsKey(from) && 
				!vertices.get(from).contains(to)) {
			vertices.get(from).add(to);
			if ((vertices.containsKey(to) && 
					!vertices.get(to).contains(from)) || 
					(!vertices.containsKey(to)))
				graphEdgesNumber++;
		}
	}

	@Override
	public Graph getEgonet(int center) {

		// Initialize the returned graph
		Graph retGraph = new FacebookGraph();


		// if the graph does not contain the center node, return an empty graph
		if (!vertices.containsKey(center)) {
			return retGraph;
		}

		// add the center node to the new graph
		retGraph.addVertex(center);


		// add the phones
		((FacebookGraph)retGraph).getNode(center).setCurrentPhone(nodesId.get(center).getCurrentPhone());
		((FacebookGraph)retGraph).getNode(center).setPreviousPhone(nodesId.get(center).getPreviousPhone());




		// iterate over the neighbors
		for (int i : vertices.get(center)) {


			// add the neighbor node to the graph
			retGraph.addVertex(i);


			// add the phones
			((FacebookGraph)retGraph).getNode(i).setCurrentPhone(nodesId.get(i).getCurrentPhone());
			((FacebookGraph)retGraph).getNode(i).setPreviousPhone(nodesId.get(i).getPreviousPhone());

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

	@Override
	public List<Graph> getSCCs() {
		Stack<Integer> vertices = new Stack<Integer>();
		for (int i : this.vertices.keySet()) {
			vertices.push(i);
		}

		// compute transpose of graph
		Graph GT = new FacebookGraph();
		for (int i : getVertices()) {
			GT.addVertex(i);
			for (int j : getVertices()) {
				if (j != i && getFriends(j).contains(i)) {
					GT.addEdge(i, j);
				}
			}	
		}

		return DFSList(GT, DFS(this, vertices));
	}

	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		return new HashMap<Integer, HashSet<Integer>>(vertices);
	}

	/**
	 * Get the number of edges of the current Graph.
	 * @return Number of all edges in the graph.
	 */
	public int getGraphEdgesNumber() {
		return this.graphEdgesNumber;
	}


	public HashSet<Integer> getFriends(int v) {
		return new HashSet<Integer>(vertices.get(v));
	}
	
	
	public boolean hasNodeId(int nodeId) {
		return vertices.containsKey(nodeId);
	}

	public HashSet<Integer> getVertices() {
		return new HashSet<Integer>(this.vertices.keySet());
	}

	public FacebookNode getNode(int nodeId) {
		return this.nodesId.get(nodeId);
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
				FacebookGraph gr = new FacebookGraph();

				// iterate over the nodes of the finished stack
				// and add them to the stack
				while (!finished.isEmpty()) {
					gr.addVertex(finished.pop());
				}

				// fixing edges
				HashSet<Integer> gVert = ((FacebookGraph)gr).getVertices();
				for (int i : gVert) {
					for (int j : gVert) {
						if (i != j && getFriends(i).contains(j)) {
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
		for (int i : ((FacebookGraph)g).getFriends(v)) {
			if (!visited.contains(i)) {
				DFSVisit(g, i, visited, finished);
			}
		}
		finished.push(v);
	}


	/**
	 * From the current graph, this methods creates a visual graph. 
	 * @param graph
	 */
	public void toVisualGraph(MultiGraph graph) {

		if (graph == null)
			return;
		
		graph.addAttribute("ui.stylesheet", "url(data/stylesheets/visualGraph.css)");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
		
		
		HashSet<String> existingNodes = new HashSet<String>();


		for (int i : vertices.keySet()) {


			String firstStrNode = Integer.toString(i);

			// check if the graph does contain the nodes
			if (!existingNodes.contains(firstStrNode)) {
				graph.addNode(firstStrNode);
				//graph.getNode(firstStrNode).addAttribute("ui.label", firstStrNode);
				existingNodes.add(firstStrNode);
			}


			for (int j : vertices.get(i)) {


				String secondStrNode = Integer.toString(j);


				if (!existingNodes.contains(secondStrNode)) {
					graph.addNode(secondStrNode);
					//graph.getNode(secondStrNode).addAttribute("ui.label", secondStrNode);
					existingNodes.add(secondStrNode);
				}

				// add edges
				graph.addEdge(firstStrNode + "," + secondStrNode, firstStrNode, secondStrNode);
			}


		}




	}


	/**
	 * From the current graph, map the nodes that will change their phones in the next generation.
	 * @return
	 */
	public HashMap<Integer, String> nextPhoneGeneration() {

		HashMap<Integer, String> map = new HashMap<Integer, String>();

		// the reasoning is based on the first phone
		float ratioPhones = smartPhones.ratioFeaturePrice(
				smartPhones.getSmartPhone(1),
				smartPhones.getSmartPhone(2));

		for (int i : vertices.keySet()) {
			// initializes a probability of 1/2 on a phone
			float prob = 0.5f;	

			prob += ratioPhones;

			int ownersIphone = nodesId.get(i).getNumberOfPhone(smartPhones.getSmartPhone(1).getName());
			int ownersSamsu = nodesId.get(i).getNumberOfPhone(smartPhones.getSmartPhone(2).getName());

			if (ownersIphone > ownersSamsu) {
				prob += 0.2;
			}
			else if (ownersIphone < ownersSamsu) {
				prob -= 0.2;
			}

			// previous experience
			if (smartPhones.getSmartPhone(1).getName().equals(nodesId.get(i).getPreviousPhone())) {
				prob += 0.2;
			}
			else if (smartPhones.getSmartPhone(2).getName().equals(nodesId.get(i).getPreviousPhone())) {
				prob -= 0.2;
			}



			if (prob < 0.4) {
				map.put(i, smartPhones.getSmartPhone(2).getName());
			}
			else if (prob > 0.6) {
				map.put(i, smartPhones.getSmartPhone(1).getName());
			}

		}

		return map;
	}



	public HashMap<Integer, String[]> snapOfCurrentPhones() {

		HashMap<Integer, String[]> snapCurrentMap = new HashMap<Integer, String[]>();

		for (int i : nodesId.keySet()) {
			snapCurrentMap.put(i, new String[2]);

			snapCurrentMap.get(i)[0] = nodesId.get(i).getPreviousPhone();
			snapCurrentMap.get(i)[1] = nodesId.get(i).getCurrentPhone();
		}


		return snapCurrentMap;
	}


	public void updatePhones(HashMap<Integer, String> newPhones) {

		for (int i : newPhones.keySet()) {
			nodesId.get(i).setPreviousPhone(nodesId.get(i).getCurrentPhone());
			nodesId.get(i).setCurrentPhone(newPhones.get(i));
		}

		updateNumberOfPhones();

	}

	public void loadPhones(HashMap<Integer, String[]> phonesMap) {

		for (int i : phonesMap.keySet()) {
			nodesId.get(i).setPreviousPhone(phonesMap.get(i)[0]);
			nodesId.get(i).setCurrentPhone(phonesMap.get(i)[1]);
		}

		updateNumberOfPhones();

	}

	private void updateNumberOfPhones() {

		// for each node in the graph
		for (int i: nodesId.keySet()) {
			FacebookNode currentNode = nodesId.get(i);

			currentNode.resetMapping();
			// for each adjacent node of the current node
			for (int j : vertices.get(i)) {
				currentNode.addPhoneToFriend(j, nodesId.get(j).getCurrentPhone());
			}
		}
	}

}