package com.jonah.controller;

import com.jonah.dto.ApiResponseDto;
import com.jonah.dto.ExpenseDto;
import com.jonah.exception.expense.ExpenseNotFoundException;
import com.jonah.model.AppUser;
import com.jonah.model.Expense;
import com.jonah.service.expense.ExpenseService;
import com.jonah.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Consider Creating a generic ApiResponse DTO Class to encpsulate responses . This makes api responses uniform
@RestController
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<Expense>> getAllUserExpenses(Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        return ResponseEntity.ok( expenseService.getAllUserExpenses( user.getId() ));
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

        //return ResponseEntity.ok( expense ); //new ResponseEntity<>(expense, HttpStatus.OK); // Found the expense with matching id
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
    public ResponseEntity<List<Expense>> getExpenseByCategoriesAndMonth(@PathVariable("category") String category,
                                                                        @RequestParam("month") String month,
                                                                        Authentication authentication){
        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        List<Expense> expenses = expenseService.getExpenseByCategoryAndMonth(category, month, user.getId() );

        if( expenses.isEmpty()) {
            throw new ExpenseNotFoundException("No expenses found for  category:  " + category + " and month: " + month );
        }
        return  ResponseEntity.ok( expenses );
    }

    @PutMapping("/expenses/{id}")
    public ResponseEntity<ApiResponseDto<Expense>> updateExpense(
                    @PathVariable Long id,
                    @RequestBody Expense updatedExpense,
                    Authentication authentication,
                    UriComponentsBuilder uriBuilder){

        String username = authentication.getName();
        AppUser user = userService.findByUsername( username );

        updatedExpense.setId(id);
        Expense expense = expenseService.updateExpense( updatedExpense, user.getId() );

        String location = uriBuilder.path("/expenses/{id}").buildAndExpand(id).toUriString();

        ApiResponseDto<Expense> response = new ApiResponseDto<>(
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
    public ResponseEntity<ApiResponseDto<ExpenseDto>> addExpense(@RequestBody ExpenseDto expenseDto,
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

}
