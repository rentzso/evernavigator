package com.classes.visualization;

import java.util.HashMap;

public class Link {
	int source, destination;
	HashMap<String, String> attributes;
	
	public Link(int source, int destination, HashMap<String, String> attributes){
		this.source = source;
		this.destination = destination;
		this.attributes = attributes;
	}
	public int getSource(){
		return this.source;
	}
	
	public int getTarget(){
		return this.destination;
	}
	
	public String getCount(){
		return this.attributes.get("count");
	}
	public boolean isShow(){
		return true;
	}
	
	public boolean equal(Link link){
		
		return (this.attributes == link.attributes && 
				( (this.source == link.source && this.destination == link.destination) ||
				(this.source == link.destination && this.destination == link.source) ) );
	}
	
	public String toString(){
		return "(" + this.source + ", " + this.destination + ")";
	}

}
