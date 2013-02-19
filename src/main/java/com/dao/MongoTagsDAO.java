package com.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.classes.data.Tag;
import com.classes.visualization.Graph;
import com.classes.visualization.NodeFactory;

@Repository
public class MongoTagsDAO implements TagsDAO{
	MongoTemplate mongoTemplate;
	
	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public Graph getGraphForUser(int userId) {
		// TODO Auto-generated method stub
		ArrayList<Tag> tags = (ArrayList<Tag>) this.mongoTemplate.find(new Query(where("userId").is(userId)), Tag.class, "tag");
		NodeFactory nodeFactory = new NodeFactory();
		for (Tag tag: tags){
			nodeFactory.getNode(tag);
		}
		return nodeFactory.buildGraph();
	}
	

}
