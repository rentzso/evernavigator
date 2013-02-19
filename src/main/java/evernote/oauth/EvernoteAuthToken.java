package evernote.oauth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;

/** 
 * A Scribe AccessToken that contains Evernote-specific items from the OAuth response. 
 */
public class EvernoteAuthToken extends Token {

  private static final long serialVersionUID = -6892516333656106315L;

  private static final Pattern NOTESTORE_REGEX = Pattern.compile("edam_noteStoreUrl=([^&]+)");
  private static final Pattern WEBAPI_REGEX = Pattern.compile("edam_webApiUrlPrefix=([^&]+)");
  private static final Pattern USERID_REGEX = Pattern.compile("edam_userId=([^&]+)");

  private String noteStoreUrl;
  private String webApiUrlPrefix;
  private int userId;

  public EvernoteAuthToken(Token token) {
    super(token.getToken(), token.getSecret(), token.getRawResponse());
    this.noteStoreUrl = extract(getRawResponse(), NOTESTORE_REGEX);
    this.webApiUrlPrefix = extract(getRawResponse(), WEBAPI_REGEX);
    this.userId = Integer.parseInt(extract(getRawResponse(), USERID_REGEX));
  }
  
  private String extract(String response, Pattern p) {
    Matcher matcher = p.matcher(response);
    if (matcher.find() && matcher.groupCount() >= 1) {
      return OAuthEncoder.decode(matcher.group(1));
    } else {
      throw new OAuthException("Response body is incorrect. " +
          "Can't extract token and secret from this: '" + response + "'", null);
    }
  }

  /**
   * Get the Evernote web service NoteStore URL from the OAuth access token response.
   */
  public String getNoteStoreUrl() {
    return noteStoreUrl;
  }

  /**
   * Get the Evernote web API URL prefix from the OAuth access token response.
   */
  public String getWebApiUrlPrefix() {
    return webApiUrlPrefix;
  }

  /**
   * Get the numeric Evernote user ID from the OAuth access token response.
   */
  public int getUserId() {
    return userId;
  }
}

