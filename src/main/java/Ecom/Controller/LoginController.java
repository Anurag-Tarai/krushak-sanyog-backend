package Ecom.Controller;

import Ecom.Model.User;
import Ecom.ModelDTO.UserSignInDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import Ecom.Service.UserService;

@RestController
@RequestMapping("/ecom")
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/signIn")
	public ResponseEntity<UserSignInDetail> getLoggedInCustomerDetailsHandler(
			Authentication auth,
			@RequestParam("role") String role) throws Exception {

		System.out.println("user login started....");

		User customer = userService.getUserByEmailId(auth.getName());
		if (!customer.getRole().toString().equals(role)) {
			System.out.println(role + " " + customer.getRole().toString());
			throw new Exception("Role is not USER");
		}

		System.out.println("User role: " + customer.getRole());
		UserSignInDetail userSignInDetail = new UserSignInDetail();
		userSignInDetail.setId(customer.getUserId());
		userSignInDetail.setFirstNAme(customer.getFirstName());
		userSignInDetail.setLastName(customer.getLastName());
		userSignInDetail.setSigninStatus("Success");

		System.out.println("user login finished....");

		return new ResponseEntity<>(userSignInDetail, HttpStatus.OK);
	}


}
