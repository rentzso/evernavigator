package com.classes.visualization;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	public List<Node> nodes;
	public List<Link> links;
	
	public Graph(ArrayList<Node> nodes, ArrayList<Link> links){
		this.nodes = nodes;
		this.links = links;
	}
	
	public List<Node> getNodes(){
		return this.nodes;
	}
	
	public List<Link> getLinks(){
		return this.links;
	}

}
