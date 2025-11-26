package com.jonah.repository;

import com.jonah.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserIdOrderByDate(Long userId);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);

    /**
     * For large amount of expenses use query instead of relying with Java in memory Operations
     */
    @Query(""" 
            SELECT e FROM Expense e 
                WHERE e.userId = :userId 
                    AND LOWER(e.category) = LOWER(:category) 
                    AND e.date LIKE CONCAT(:month,'%')
                ORDER BY e.date DESC
            """)
    List<Expense> findByCategoryAndMonthAndUserId(@Param("category") String category,
                                                      @Param("month") String month, @Param("userId") Long userId);

}
