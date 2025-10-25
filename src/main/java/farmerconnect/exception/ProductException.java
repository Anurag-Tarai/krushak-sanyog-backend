package farmerconnect.exception;

import java.io.IOException;

public class ProductException extends RuntimeException {

	public ProductException() {
	}
	public ProductException(String msg) {
		super(msg);
	}

	public ProductException(String failedToUploadImage, IOException e) {
		super(failedToUploadImage, e);
	}

}
