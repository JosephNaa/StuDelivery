package com.ssafy.api.service;

import com.ssafy.common.auth.AuthKey;
import com.ssafy.common.exception.enums.ExceptionEnum;
import com.ssafy.common.exception.response.ApiException;
import com.ssafy.common.util.MailUtils;
import com.ssafy.db.entity.AuthProvider;
import java.time.LocalDateTime;

import com.ssafy.db.entity.Attendance;
import com.ssafy.db.entity.Goal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRepositorySupport userRepositorySupport;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	MailService mailService;

	@Override
	@Transactional
	public User createUser(UserRegisterPostReq userRegisterInfo) throws Exception {

		if (userRepository.findByUserId(userRegisterInfo.getId()).isPresent()) {
			return null;
		}

		User user = new User();
		user.setUserId(userRegisterInfo.getId());
		// 보안을 위해서 유저 패스워드 암호화 하여 디비에 저장.
		user.setPassword(passwordEncoder.encode(userRegisterInfo.getPassword()));
		user.setNickName(userRegisterInfo.getNickname());
		user.setCreatedAt(LocalDateTime.now());
		user.setProvider(AuthProvider.local);

		// authKey 생성
		String authKey = new AuthKey().getKey(50);
		user.setAuthKey(authKey);
		user.setAuthStatus(false);

		mailService.sendConfirmMail(user, authKey);

		return userRepository.save(user);
	}

	@Override
	public User getUserByUserId(String userId) {
		// 디비에 유저 정보 조회 (userId 를 통한 조회).
		User user = userRepositorySupport.findUserByUserId(userId).orElseThrow(() -> new ApiException(
			ExceptionEnum.NOT_FOUND_USER));
		return user;
	}

	@Override
	public Boolean isEmailPresent(String email) {
		Boolean flag = userRepositorySupport.findUserByUserId(email).isPresent();
		return flag;
	}

	// authState 변경
	@Override
	public String updateAuthStatus(String userId, String authKey) {
		User user = userRepositorySupport.findUserByUserId(userId).get();

		if (user.getAuthKey().equals(authKey)) {
			user.setAuthStatus(true);
			userRepository.save(user);

			return "SUCCESS";

		} else {
			return "FAILED";
		}
	}

	@Override
	public void updateUser(User user) {
		userRepository.save(user);
	}

}