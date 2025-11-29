package com.jonah.controller;

import com.jonah.aspect.annotation.Log;
import com.jonah.dto.filter.ExpenseFilterDto;
import com.jonah.dto.response.ApiResponseDto;
import com.jonah.dto.request.ExpenseDto;
import com.jonah.exception.expense.ExpenseNotFoundException;
import com.jonah.model.AppUser;
import com.jonah.model.Expense;
import com.jonah.service.expense.ExpenseService;
import com.jonah.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Consider Creating a generic ApiResponse DTO Class to encpsulate responses . This makes api responses uniform
@RestController
@Log
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/expenses")
    public ResponseEntity<ApiResponseDto<List<ExpenseDto>>> getAllUserExpenses(Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        List<ExpenseDto> expenses = expenseService.getAllUserExpenses( user.getId() );

        ApiResponseDto<List<ExpenseDto>> response = new ApiResponseDto<>(
                true,
                "Successfully retrieved expenses",
                expenses
        );
        //String location = uriBuilder ToDo: build location URI

        return ResponseEntity
                .ok()
                .body(response);
    }

    @GetMapping("/expenses/categories")
    public ResponseEntity<List<String>> getExpenseCategories(Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        List<String> categories = expenseService.getExpenseCategories( user.getId() );

        if( categories.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

       return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/expenses/categories/{month}")
    public ResponseEntity<List<String>> getExpenseCategoriesByMonth(@PathVariable("month") String month,
                                                                    Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        List<String> categories = expenseService.getExpenseCategoriesByMonth( month , user.getId() );

        if( categories.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/expenses/{id}")
    public ResponseEntity<ApiResponseDto<Expense>> getExpenseById(@PathVariable Long id,
                                                  Authentication authentication ,
                                                  UriComponentsBuilder uriBuilder){
         String username = authentication.getName();
         AppUser user = userService.findByUsername( username );

         Expense expense = expenseService.getExpenseById( id, user.getId() )
                .orElseThrow(()->new ExpenseNotFoundException("No expense found for id: "+ id) );

        String location = uriBuilder.path("/expenses/{id}")
                .buildAndExpand(id)
                .toUriString();

        ApiResponseDto<Expense> response = new ApiResponseDto<>(
                true,
                "Expense retrieved successfully",
                expense,
                location
        );

        return ResponseEntity
                .ok()
                .location(URI.create( location))
                .body(response);
    }

    @GetMapping("/expenses/day/{date}")
    public ResponseEntity<List<Expense>> getExpenseByDay(@PathVariable String date,
                                                         Authentication authentication){

        final String username = authentication.getName();

        AppUser user = userService.findByUsername( username );
        List<Expense> expenses = expenseService.getExpenseByDay( date, user.getId() );

        if( expenses.isEmpty() ){
            throw new ExpenseNotFoundException("No Expenses found for date: " + date );
        }
        return new ResponseEntity<>( expenses, HttpStatus.OK );
    }

    @GetMapping("/expenses/categories/{category}/month")
    public ResponseEntity<ApiResponseDto<List<ExpenseDto>>> getExpenseByCategoriesAndMonth(@PathVariable("category") String category,
                                                                        @RequestParam("month") String month,
                                                                        UriComponentsBuilder uriBuilder,
                                                                        Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        List<ExpenseDto> expenses = expenseService.getExpenseByCategoryAndMonth(category, month, user.getId() );

        String location = uriBuilder
                .path("/expenses/categories/{category}/month")
                .queryParam("month", month)
                .buildAndExpand(category).toUriString();

        ApiResponseDto<List<ExpenseDto>> response = new ApiResponseDto<>(
                true,
                "Found expense(s)",
                expenses,
                location
        );
        return  ResponseEntity
                .ok()
                .location(URI.create(location))
                .body( response);
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<ApiResponseDto<ExpenseDto>> updateExpense(
                    @PathVariable Long id,
                    @RequestBody ExpenseDto updatedExpense,
                    Authentication authentication,
                    UriComponentsBuilder uriBuilder){

        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        updatedExpense.setId(id);
        ExpenseDto expense = expenseService.updateExpense( updatedExpense, user.getId() );

        String location = uriBuilder.path("/expenses/{id}").buildAndExpand(id).toUriString();

        ApiResponseDto<ExpenseDto> response = new ApiResponseDto<>(
                true,
                "Successfully Updated Expense",
                expense,
                location);

        return  ResponseEntity
                .ok()
                .location(URI.create(location) )
                .body( response);
    }

    @PostMapping("/expenses")
    public ResponseEntity<ApiResponseDto<ExpenseDto>> addExpense( @Valid @RequestBody ExpenseDto expenseDto,
                                                                 Authentication authentication,
                                                                 UriComponentsBuilder uriBuilder){

        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        ExpenseDto createdExpenseDto = expenseService.addExpense( expenseDto, user.getId() );

        // Build the URI location for the created resource
        String location = uriBuilder.path("/expenses/{id}")
                .buildAndExpand(createdExpenseDto.getId())
                .toUriString();

        ApiResponseDto<ExpenseDto> response = new ApiResponseDto<>(
                true,
                "Expense created successfully",
                createdExpenseDto
        );
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create(location))
                .body(response);
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") Long id,
                                                Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        expenseService.deleteExpense( id, user.getId() );

        // 204 - status code
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/expenses/filter")
    public ResponseEntity<ApiResponseDto<List<ExpenseDto>>> filterExpenses(@RequestBody ExpenseFilterDto filter,
                                                                           Authentication  authentication,
                                                                           UriComponentsBuilder uriBuilder){

        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        List<ExpenseDto> expenses = expenseService.filterExpenses(filter, user.getId());

        // ToDO : Build uri location
        ApiResponseDto<List<ExpenseDto>> response = new ApiResponseDto<>(
                true,
                "Expenses retrieved successfully",
                expenses
        );

        return ResponseEntity
                .ok()
                .body( response);

    }
}
