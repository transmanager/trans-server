package channy.transmanager.shaobao.service;

import channy.transmanager.shaobao.data.TokenDao;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.service.user.UserService;
import channy.util.ErrorCode;

public class TokenService {
	public ErrorCode auth(String employeeId, String token) {
		UserService userService = new UserService();
		User user = userService.getByEmployeeId(employeeId);
		return TokenDao.authenticate(user, token);
	}
	
}
