package graph.test;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import graph.FacebookGraph;

public class FacebookGraphTester {
	
	
	
	FacebookGraph fbGraph;

	@Before
	public void setUp() {
		
	}

	
	
	@Test
	public void testingLoad() {

		// start with testing number of nodes and edges
		assertEquals("Check Number of Nodes", 4039, fbGraph.getVertices().size());
		assertEquals("Check Number of Edges", 88234, fbGraph.getGraphEdgesNumber());

		// check the sum of the phones 
		for (int i : fbGraph.getVertices()) {
			assertEquals("Node " + i + " number nodes", 
					fbGraph.getFriends(i).size(), 
					fbGraph.getNode(i).getNumberOfPhone("N/A") + 
					fbGraph.getNode(i).getNumberOfPhone("SamsungG") +
					fbGraph.getNode(i).getNumberOfPhone("IPhone"));
		}


		// check the number of SCCs
		assertEquals("Check number of SCCs", 1, fbGraph.getSCCs().size());

	}

	
}
