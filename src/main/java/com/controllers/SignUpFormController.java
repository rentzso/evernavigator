package com.controllers;

import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.*;
import org.scribe.model.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.classes.credentials.Cred;
import com.classes.users.User;
import com.dao.JDBCUserDAO;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;

import evernote.oauth.*;
import evernote.tags.EverMongoBridge;
import evernote.tags.EvernoteHandle;

@Controller
@RequestMapping(value="/user")
@SessionAttributes("user")
public class SignUpFormController {

	private static final Logger logger = Logger.getLogger(SignUpFormController.class);
	private static final String consumerKey = Cred.consumerKey;
	private static final String consumerSecret = Cred.consumerSecret;
	  
	  /*
	   * Replace this value with https://www.evernote.com to switch from the Evernote
	   * sandbox server to the Evernote production server.
	   */
	private static final String urlBase = "https://sandbox.evernote.com";
	private static final String userStoreUrl = urlBase +"/edam/user";
	  
	private static final String requestTokenUrl = urlBase + "/oauth";
	private static final String accessTokenUrl = urlBase + "/oauth";
	private static final String authorizationUrlBase = urlBase + "/OAuth.action";
	  
	private static final String callbackUrl = "callbackReturn";
	
	
	private JDBCUserDAO userdao;
	private MongoTemplate mongoTemplate;
	
	@Autowired
	public SignUpFormController(MongoTemplate mongoTemplate, JDBCUserDAO userdao){
		this.userdao = userdao;
		this.mongoTemplate = mongoTemplate;
	}

	@RequestMapping(method= RequestMethod.GET)
	public String showform(Model model){
		logger.info("Model custom logging");
		logger.info(model);
        model.addAttribute("user", new User());
        return "user/signup";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="registration")
	public String registration(@ModelAttribute("user") User user, BindingResult result, 
			Model model, HttpServletRequest request, HttpSession session) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException, UnknownHostException{
		logger.info("started registration");
		logger.info(user.getUsername());
		String thisUrl = request.getRequestURL().toString();
	    String cbUrl = thisUrl.substring(0, thisUrl.lastIndexOf('/') + 1) + callbackUrl;
	    
	    Class providerClass = org.scribe.builder.api.EvernoteApi.Sandbox.class;
	    if (urlBase.equals("https://www.evernote.com")) {
	      providerClass = org.scribe.builder.api.EvernoteApi.class;
	    }
	    OAuthService service = new ServiceBuilder()
	        .provider(providerClass)
	        .apiKey(consumerKey)
	        .apiSecret(consumerSecret)
	        .callback(cbUrl)
	        .build();
	    
		Token scribeRequestToken = service.getRequestToken();
		String requestToken = scribeRequestToken.getToken();
	    session.setAttribute("requestToken", requestToken);
	    session.setAttribute("requestTokenSecret", scribeRequestToken.getSecret());
	    session.setAttribute("user", user);

	    // this link will redirect the user to the evernote authorization page
		String authorizationUrl = authorizationUrlBase + "?oauth_token=" + requestToken;
		return "redirect:" + authorizationUrl;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="callbackReturn")
	public String authorization(HttpServletRequest request, HttpSession session) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException, UnknownHostException{
		
		logger.info("started authorization");
		User user = (User) session.getAttribute("user"); 
		logger.info(user.getUsername());
		
		String accessToken = (String)session.getAttribute("accessToken");
		String requestToken = (String)session.getAttribute("requestToken");
		String requestTokenSecret = (String)session.getAttribute("requestTokenSecret");
		
		
		String thisUrl = request.getRequestURL().toString();
	    String cbUrl = thisUrl.substring(0, thisUrl.lastIndexOf('/') + 1) + callbackUrl;
	    
	    Class providerClass = org.scribe.builder.api.EvernoteApi.Sandbox.class;
	    if (urlBase.equals("https://www.evernote.com")) {
	      providerClass = org.scribe.builder.api.EvernoteApi.class;
	    }
	    OAuthService service = new ServiceBuilder()
	        .provider(providerClass)
	        .apiKey(consumerKey)
	        .apiSecret(consumerSecret)
	        .callback(cbUrl)
	        .build();
	    
	    
	    	
    	int userid;
    	
    	requestToken = request.getParameter("oauth_token");
        String verifier = request.getParameter("oauth_verifier");
        session.setAttribute("verifier", verifier);
        

        // Verify the session with the verifier and...

        Token scribeRequestToken = new Token(requestToken, requestTokenSecret);
        Verifier scribeVerifier = new Verifier(verifier);
        EvernoteAuthToken token = new EvernoteAuthToken(service.getAccessToken(scribeRequestToken, scribeVerifier));

        // ... obtain the accessToken
        accessToken = token.getToken();
        userid = token.getUserId();
        user.setUserid(userid);
        user.setAccesstoken(accessToken);

        String noteStoreUrl = token.getNoteStoreUrl();
        session.setAttribute("accessToken", accessToken);
        EvernoteHandle handle = new EvernoteHandle(accessToken, noteStoreUrl);
        EverMongoBridge bridge = new EverMongoBridge(this.mongoTemplate, handle, userid);
        // dump the tags in mongo
        bridge.dumpTagsToCollection();
        // add notes references to each record
        bridge.dumpNotesToMongo();
    	this.userdao.registerUser(user);
		return "redirect:../visual?userId="+userid;
	}
}
