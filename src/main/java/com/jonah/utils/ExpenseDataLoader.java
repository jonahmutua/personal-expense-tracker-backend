package com.jonah.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonah.model.Expense;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExpenseDataLoader {

    private static  List<Expense>  expenses = new ArrayList();

    private static Long baseIdCounter = 0L;
    @PostConstruct
    public void init(){
        ObjectMapper mapper = new ObjectMapper();
        // loads expenses.json file
        try (InputStream is = getClass().getResourceAsStream("/expenses.json")) {
            expenses = mapper.readValue(is, new TypeReference<List<Expense>>() {
            });

            baseIdCounter = expenses.stream().map(
                    expense -> expense.getId()
                    ).max((a,b)-> a.compareTo(b))
                    .orElse(0L);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static List<Expense> getExpenses() {
        return expenses;
    }

    public static Long getBaseIdCounter() {
        return baseIdCounter;
    }
}
