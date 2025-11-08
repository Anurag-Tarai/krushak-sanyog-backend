package farmerconnect.service.impl;

import java.util.List;

import farmerconnect.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import farmerconnect.enums.UserAccountStatus;
import farmerconnect.exception.UserException;
import farmerconnect.model.User;

import farmerconnect.repository.UserRepository;
import farmerconnect.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    @Override
    public CustomUserDetails getCurrentAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

    @Override
	public User getUserByEmailId(String emailId) throws UserException {
		return userRepository.findByEmail(emailId).orElseThrow(() -> new UserException("User not found"));

	}
	
	@Override
	public String deactivateUser(Integer userId) throws UserException {
		User existingUser = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
		existingUser.setUserAccountStatus(UserAccountStatus.DEACTIVETE);
		userRepository.save(existingUser);
		return "Account deactivet Succesfully";
	}

	@Override
	public User getUserById(Integer userId) throws UserException {
        return userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
	}

	@Override
	public List<User> getAllUserDetails() throws UserException {

		List<User> existingAllUser = userRepository.findAll();
		if (existingAllUser.isEmpty()) {
            throw new UserException("User list is Empty");
		}
		return existingAllUser;
	}

	

}
