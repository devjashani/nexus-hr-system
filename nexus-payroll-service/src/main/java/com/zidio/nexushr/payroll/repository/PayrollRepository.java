
package com.zidio.nexushr.payroll.repository;

import com.zidio.nexushr.payroll.model.Payroll;
import com.zidio.nexushr.payroll.model.PayrollStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeIdOrderByYearDescMonthDesc(Long employeeId);
    Optional<Payroll> findByEmployeeIdAndMonthAndYear(Long employeeId, Integer month, Integer year);
    List<Payroll> findByStatus(PayrollStatus status);
}
