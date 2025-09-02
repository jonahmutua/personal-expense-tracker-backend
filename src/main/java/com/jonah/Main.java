package com.jonah;

import com.jonah.model.Expense;
import com.jonah.utils.ExpenseDataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    public static void main(String[] args)  {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // tests if expenses.json loads properly
        // List<Expense> expenses = ExpenseDataLoader.getExpenses();
        // expenses.forEach(System.out::println);
    }
}