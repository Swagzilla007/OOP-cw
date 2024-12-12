package org.ticktet_software.cw_20234071.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ticktet_software.cw_20234071.entity.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Vendor findByVendorName(String vendorName);
}
