package com.classes.visualization;

import java.util.ArrayList;
import java.util.List;

import com.classes.data.Note;
import com.classes.data.RelatedTag;
import com.classes.data.Tag;

class Node {
	int id;
	int count;
	String tag;
	List<Note> notes;
	List<RelatedTag> relatedTags;
	List<Integer> related;
	
	Node(Tag tag, int id){
		this.id = id;
		this.count = tag.getCount();
		this.tag = tag.getTag();
		this.notes = tag.getNotes();
		this.relatedTags = new ArrayList<RelatedTag>();
		this.related = new ArrayList<Integer>();
	}
	
	public String toString(){
		return this.tag;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getCount(){
		return this.count;
	}
	
	public List<Note> getNotes(){
		return this.notes;
	}
	
	public List<Integer> getRelated(){
		return this.related;
	}
	public boolean isFocus(){
		return false;
	}
}
