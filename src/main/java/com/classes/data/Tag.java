package com.classes.data;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Document
public class Tag {
	@Id
	private String guid;
	private int count;
	private String tag;
	private int userId;
	private List<Note> notes;
	private List<RelatedTag> related;
	
	public Tag(int userId, String tag, String guid, int count, ArrayList<Note> notes, ArrayList<RelatedTag> related){
		this.userId = userId;
		this.tag = tag;
		this.guid = guid;
		this.count = count;
		this.notes = notes;
		this.related = related;
	}
	
	public String getGuid(){
		return this.guid;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public int getUserId(){
		return this.userId;
	}
	
	public int getCount(){
		return this.count;
	}
	
	public List<Note> getNotes(){
		return this.notes;
	}
	
	public List<RelatedTag> getRelated(){
		return this.related;
	}
}
