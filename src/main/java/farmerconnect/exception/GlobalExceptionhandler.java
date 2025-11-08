package farmerconnect.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionhandler {



    @ExceptionHandler(ProductAlreadyInWishlistException.class)
    public ResponseEntity<Map<String, Object>> handleProductAlreadyInWishlistException(ProductAlreadyInWishlistException ex) {
        Map<String, Object> body = Map.of(
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ProductNotFoundException ex) {
        Map<String, Object> body = Map.of(
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleImageNotFound(ImageNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ImageLimitExceedException.class)
    public ResponseEntity<Map<String, Object>> handleImageLimitExceedException(ImageLimitExceedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentialException(InvalidCredentialException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }


    //    ----------------------------------------------------------------------------------
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MyErrorClass> getException(MethodArgumentNotValidException e, WebRequest req) {

		MyErrorClass e1 = new MyErrorClass();
		e1.setMessage(e.getBindingResult().getFieldError().getDefaultMessage());
		e1.setLocalDateTimes(LocalDateTime.now());
		e1.setDesc(req.getDescription(false));

		return new ResponseEntity<MyErrorClass>(e1, HttpStatus.BAD_GATEWAY);

	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<MyErrorClass> getException(NoHandlerFoundException e, WebRequest req) {

		MyErrorClass e1 = new MyErrorClass();
		e1.setMessage("handle not found Exception");
		e1.setLocalDateTimes(LocalDateTime.now());
		e1.setDesc(req.getDescription(false));

		return new ResponseEntity<MyErrorClass>(e1, HttpStatus.BAD_GATEWAY);

	}

	@ExceptionHandler(UserException.class)
	public ResponseEntity<MyErrorClass> getException(UserException e, WebRequest req) {

		MyErrorClass e1 = new MyErrorClass();
		e1.setMessage(e.getMessage());
		System.out.println("inside the global order exception");
		e1.setLocalDateTimes(LocalDateTime.now());
		e1.setDesc(req.getDescription(false));

		return new ResponseEntity<MyErrorClass>(e1, HttpStatus.BAD_GATEWAY);

	}

	@ExceptionHandler(AddressException.class)
	public ResponseEntity<MyErrorClass> getException(AddressException e, WebRequest req) {

		MyErrorClass e1 = new MyErrorClass();
		e1.setMessage(e.getMessage());
		e1.setLocalDateTimes(LocalDateTime.now());
		e1.setDesc(req.getDescription(false));

		return new ResponseEntity<MyErrorClass>(e1, HttpStatus.NOT_FOUND);
	}




	@ExceptionHandler(Exception.class)
	public ResponseEntity<MyErrorClass> getException(Exception e, WebRequest req) {

		MyErrorClass e1 = new MyErrorClass();
		e1.setMessage(e.getMessage());
		e1.setLocalDateTimes(LocalDateTime.now());
		e1.setDesc(req.getDescription(false));

		return new ResponseEntity<MyErrorClass>(e1, HttpStatus.BAD_GATEWAY);

	}

}
