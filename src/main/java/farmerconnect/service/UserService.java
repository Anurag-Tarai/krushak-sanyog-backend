package farmerconnect.service;

import java.util.List;

import org.springframework.stereotype.Service;

import farmerconnect.exception.UserException;
import farmerconnect.model.User;
import farmerconnect.dto.AdminDTO;
import farmerconnect.dto.CustomerDTO;
import farmerconnect.dto.UserDTO;


@Service
public interface UserService {
	
	
	
	public User getUserByEmailId(String emailId)throws UserException;

	public User addUser(CustomerDTO customer)  throws UserException;
	
	public User addUserAdmin(AdminDTO admin	)  throws UserException;

	public User changePassword(Integer userId, UserDTO customer)  throws UserException;

	public String deactivateUser(Integer userId) throws UserException;

	public User getUserDetails(Integer userId)throws UserException;

	public List<User> getAllUserDetails() throws UserException;
}
