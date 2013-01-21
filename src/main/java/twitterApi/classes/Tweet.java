package twitterApi.classes;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.core.style.ToStringCreator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

public class Tweet {
	
	
	private Long id;

	@NotNull
	@Size(min = 1, max = 300)
	private String text;
	
	@NotNull
	@Size(min = 1, max = 50)
	private String username;
	
	@DateTimeFormat(style = "S-")
	@Future
	private Date date = new Date(new Date().getTime());


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date renewalDate) {
		this.date = date;
	}

	public String toString() {
		return new ToStringCreator(this).append("id", id).append("username", username)
				.append("text", text).append("date", date)
				.toString();
	}

}
