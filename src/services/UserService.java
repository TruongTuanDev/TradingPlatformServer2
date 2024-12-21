package services;

import java.util.ArrayList;
import java.util.List;

import dto.SignUpForm;
import model.User;
import repositorys.UserRepository;

public class UserService {
	private User user;
	private UserRepository userRepository;
	public User createUser(String userName,String password,String email) {
	      user = new User();
	      userRepository = new UserRepository();
	      user.setUsername(userName);
	      user.setEmail(email);
	      user.setPassword(password);
	      user = userRepository.save(user);
	      return user;
	    }
	public boolean checkUser(String userName,String password) {
		  boolean isValid = false;
		  userRepository = new UserRepository();
		  isValid = userRepository.checklogin(userName,password);
	      return isValid;
	    }
	public boolean updateStatus(String userName,String password) {
		  boolean isValid = false;
		  userRepository = new UserRepository();
		  isValid = userRepository.updateStatus(userName,password);
	      return isValid;
	    }
	public boolean updateStatusFalse(String userName) {
		  boolean isValid = false;
		  userRepository = new UserRepository();
		  isValid = userRepository.updateStatusFalse(userName);
	      return isValid;
	    }
	public List<User> getListUsers() {
		userRepository = new UserRepository();
	    List<User> userList = new ArrayList<>(); // Danh sách để lưu tất cả người dùng
	    userList = userRepository.findAll();
	    return userList; // Trả về danh sách người dùng
	}
}