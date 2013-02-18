package com.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import evernote.MongoTagsDAO;

@Component
@RequestMapping(value="/")
public class TagController {
	MongoTagsDAO mongoTagsDAO;
	MongoTemplate t;
	
	@Autowired
	public TagController(MongoTagsDAO mongoTagsDAO, MongoTemplate t){
		this.mongoTagsDAO = mongoTagsDAO;
		this.t = t;
	}
	
	
	@RequestMapping(value="/graph", method=RequestMethod.GET)
	public @ResponseBody Graph buildTagGraph(@RequestParam int userId,
			HttpServletResponse response){
		
		return this.mongoTagsDAO.getGraphForUser(userId);
	}
	
	@RequestMapping(value="/links", method=RequestMethod.GET)
	public @ResponseBody List<Link> buildTagLinks(@RequestParam int userId,
			HttpServletResponse response){
		
		return this.mongoTagsDAO.getGraphForUser(userId).links;
	}

}
