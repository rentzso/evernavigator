package com.classes.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Note {
	String guid;
	String title;
	
	public Note(String title, String guid){
		this.title = title;
		this.guid = guid;
	}
	
	public String getGuid(){
		return this.guid;
	}
	
	public String getTitle(){
		return this.title;
	}

}
