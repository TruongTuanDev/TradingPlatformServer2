package model;
import java.io.Serializable;
import java.util.List;

import model.Token;

public class LoginResponse implements Serializable {
	private static final long serialVersionUID = 1L;

    private String status;
    private String userName;
    private List<Token> tokens;

    public LoginResponse(String status, List<Token> tokens) {
        this.status = status;
        this.tokens = tokens;
    }
    
    
    public LoginResponse() {
		
	}


	public LoginResponse(String status, String userName, List<Token> tokens) {
		super();
		this.status = status;
		this.userName = userName;
		this.tokens = tokens;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}


	public String getStatus() {
        return status;
    }

    public List<Token> getTokens() {
        return tokens;
    }


	@Override
	public String toString() {
		return "LoginResponse [status=" + status + ", userName=" + userName + ", tokens=" + tokens + "]";
	}

    
}
