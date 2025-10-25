package farmerconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import farmerconnect.model.ShippingDetails;

public interface ShippingRepository extends JpaRepository<ShippingDetails, Integer> {

}
