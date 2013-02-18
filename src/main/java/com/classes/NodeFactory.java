package com.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeFactory{
	// map from tag to integer to identify nodes
	private HashMap<String, Integer> nodeIndex;
	private ArrayList<Node> nodes;
	private ArrayList<Link> links;
	private ArrayList<Tag> tags;
	
	public NodeFactory(){
		nodeIndex = new HashMap<String, Integer>();
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
	}
	
	
	public Node getNode(Tag tag){
		int i = nodes.size();
		Node n = new Node(tag, i);
		nodeIndex.put(tag.getTag(), i);
		nodes.add(n);
		for (RelatedTag rt: tag.getRelated()){
			Integer j = nodeIndex.get(rt.tag);
			if (j != null){
				HashMap<String, String> attributes = new HashMap<String, String>();
				attributes.put("count", ((Integer)rt.count).toString());
				Link link = new Link((int)j, i, attributes);
				links.add(link);
				n.relatedTags.add(rt);
				n.related.add(j);
				nodes.get(j).relatedTags.add( new RelatedTag(n.tag, tag.getGuid(), rt.count) );
				nodes.get(j).related.add(i);
			}
		}
		return n;
	}
	
	public Graph buildGraph(){
		return new Graph(nodes, links);
	}

}
