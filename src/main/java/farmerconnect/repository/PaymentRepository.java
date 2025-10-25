package farmerconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import farmerconnect.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	
}
