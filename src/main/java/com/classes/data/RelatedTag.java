package com.classes.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RelatedTag {
	public String guid;
	public String tag;
	public int count;
	
	public RelatedTag(String tag, String guid, int count){
		this.tag = tag;
		this.guid = guid;
		this.count = count;
	}
	
	public String getGuid(){
		return this.guid;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public int getCount(){
		return this.count;
	}
	

}
