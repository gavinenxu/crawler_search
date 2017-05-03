package crawler;

import java.util.*;

public class Question {

	private Status status;
	private String theme;
	private List<String> categories;
	//private Map<String user, enum operations> started;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public String toString(){
		return status.toString() + "\n" + theme + "\n" + categories.toString() + "\n";
	}
}

class Status {
	private String votes;
	private String answered;
	private String views;
	
	public String getVotes() {
		return votes;
	}
	public void setVotes(String votes) {
		this.votes = votes;
	}
	public String getAnswered() {
		return answered;
	}
	public void setAnswered(String answered) {
		this.answered = answered;
	}
	public String getViews() {
		return views;
	}
	public void setViews(String views) {
		this.views = views;
	}
	
	public String toString() {
		return "Votes:" + votes + " Answered:" + answered + " Views:" + views;
	}
}