package com.jonah.repository.specification;

import com.jonah.dto.filter.ExpenseFilterDto;
import com.jonah.model.Expense;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ExpenseSpecifications {
    /**
     *  Builds expense specification for complex filtering
     *  Always filters by userId to ensure user data isolation
     * */
    public static Specification<Expense> buildFilter(ExpenseFilterDto filter, Long userId){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // User ID filter ( Required to ensure user Data isolation)
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            // Expense ID filter
            if(filter.getExpenseType() < 0 ){
                predicates.add(criteriaBuilder.equal(root.get("expenseType"), filter.getExpenseType()));
            }

            // Category Filter
            if( filter.getCategory() != null && !filter.getCategory().isEmpty()){
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("category")),
                        "%" + filter.getCategory() + "%"
                ));
            }

            // Start date filter
            if( filter.getStartDate() != null && !filter.getStartDate().isEmpty()) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("date"),
                        filter.getStartDate()
                ));
            }

            // End date filter
            if( filter.getEndDate() != null && !filter.getEndDate().isEmpty()) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("date"),
                        filter.getEndDate()
                ));
            }

            // minimum amount filter
            if( filter.getMinAmount() != null ){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("amount"),
                        filter.getMinAmount()
                ));
            }

            // Maximum amount filter
            if( filter.getMaxAmount() != null ) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("amount"),
                        filter.getMaxAmount()
                ));
            }

            Order order = buildOrder(root, query, criteriaBuilder, filter.getSortBy(), filter.getSortOrder());

            if (query == null) {
                throw new IllegalStateException("Query cannot be null");
            }
            query.orderBy(order);

            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private static Order buildOrder(Root<Expense> root, CriteriaQuery<?> query,
                                    CriteriaBuilder cb, String sortBy, String sortOrder) {

        Path<?> sortPath = switch (sortBy != null ? sortBy.toLowerCase() : "date") {
            case "amount" -> root.get("amount");
            case "category" -> root.get("category");
            default -> root.get("date");
        };

        boolean isAscending = "asc".equalsIgnoreCase(sortOrder);
        return isAscending ? cb.asc(sortPath) : cb.desc(sortPath);
    }
}
