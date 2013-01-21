package twitterApi.classes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

public class TwitterUser {


	@NotNull
	@Size(min = 1, max = 50)
	private String username;
	
	@Size(min = 1, max = 50)
	private String realname;
	
	@Size(min = 16, max = 16)
	private String apiKey;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String toString() {
		return new ToStringCreator(this).append("username", username)
				.append("realname", realname)
				.toString();
	}

}
