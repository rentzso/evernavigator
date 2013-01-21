package twitterApi.classes;


import twitterApi.dao.TwitterUserDAO;
import twitterApi.dao.TweetDAO;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/twitter")
public class TwitterUserController {
	
	private static Logger log = Logger.getLogger(TwitterUserController.class.getName());
	
	private TwitterUserDAO userDAO;
	private TweetDAO tweetDAO;
	
	private Validator validator;

	
	
	@Autowired
	public TwitterUserController(TwitterUserDAO userDAO, TweetDAO tweetDAO, Validator validator){
		this.validator = validator;
		this.userDAO = userDAO;
		this.tweetDAO = tweetDAO;
	}
	
	@RequestMapping(value="/tweets", method=RequestMethod.POST)
	public @ResponseBody List<Tweet> getTweets(
			@RequestBody Map<String, String> body,
			HttpServletResponse response
			){
		String username = body.get("username");
		String apiKey = body.get("apiKey");
		String keyword = body.get("keyword");
		TwitterUser user = this.userDAO.authenticate(username, apiKey, response);
		if (user==null){
			return Collections.emptyList();
		} else {
			List<Tweet> tweets = this.tweetDAO.getTweets(user, keyword);
			return tweets;
		}	
	}
	
	@RequestMapping(value="/follow", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> follow(
			@RequestBody Map<String, String> body,
			HttpServletResponse response
			){
		String username = body.get("username");
		String apiKey = body.get("apiKey");
		String followed = body.get("followed");
		TwitterUser user = this.userDAO.authenticate(username, apiKey, response);
		if (user==null){
			return Collections.singletonMap("success", "false");
		}
		Set<ConstraintViolation<TwitterUser>> failures = validator.validate(user);
		if (!failures.isEmpty()){
			return validationMessages(failures);
		}
		if (userDAO.followUser(user, followed)==1 ){
			return Collections.singletonMap("success", "true");
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return Collections.singletonMap("success", "false");
	}
	
	@RequestMapping(value="/unfollow", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> unfollow(
			@RequestBody Map<String, String> body,
			HttpServletResponse response
			){
		String username = body.get("username");
		String apiKey = body.get("apiKey");
		String followed = body.get("followed");
		TwitterUser user = this.userDAO.authenticate(username, apiKey, response);
		if (user==null){
			return Collections.singletonMap("success", "false");
		}
		Set<ConstraintViolation<TwitterUser>> failures = validator.validate(user);
		if (!failures.isEmpty()){
			return validationMessages(failures);
		}
		if (userDAO.unfollowUser(user, followed) == 1 ){
			return Collections.singletonMap("success", "true");
		}
		if (userDAO.unfollowUser(user, followed) > 1){
			return Collections.singletonMap("success", "error");
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return Collections.singletonMap("success", "false");
	}
	
	@RequestMapping(value="/followers", method=RequestMethod.POST)
	public @ResponseBody Map<String, ? extends Object> getFollowers( 
			@RequestBody Map<String, String> body,
			HttpServletResponse response
			){
		String name = body.get("username");
		String apiKey = body.get("apiKey");
		TwitterUser user = this.userDAO.authenticate(name, apiKey, response);
		if (user==null){
			return Collections.singletonMap("success", "false");
		}
		Set<ConstraintViolation<TwitterUser>> failures = validator.validate(user);
		if (failures.isEmpty()){
			Map<String, List<String>> results = new HashMap<String, List<String>>();
			results.put("followers", userDAO.getFollowers(user));
			results.put("followed", userDAO.getFollowed(user));
			return results;
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return validationMessages(failures);
	}
	
	// internal helpers
	
	private Map<String, String> validationMessages(Set<ConstraintViolation<TwitterUser>> failures) {
		Map<String, String> failureMessages = new HashMap<String, String>();
		for (ConstraintViolation<TwitterUser> failure : failures) {
			failureMessages.put(failure.getPropertyPath().toString(), failure.getMessage());
		}
		return failureMessages;
	}

}
