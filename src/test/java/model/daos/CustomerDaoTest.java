package model.daos;
import model.Customer;
import model.Grade;
import org.junit.jupiter.api.Test;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-22
 * Time: 23:35
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public class CustomerDaoTest {

    public CustomerDao customerDao = new CustomerDao();

    @Test
    public final void saveTest() {
        Customer customer = new Customer();
        customer.setFullName("Lars Westberg");
        customer.setEmail("larwes01@hej.hej");
        customer.setPassword("1234");

        String expectedMessage = "Duplicate";
        String actualMessage = customerDao.saveWithMessage(customer);
        System.out.println(actualMessage);

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public final void saveNullTest() {
//        Customer customer = new Customer();
//        customer.setFullName("Lars Westberg");
//        customer.setEmail("larwes01@hej.hej");
//        customer.setPassword("1234");
//        customerDao.save(customer);

//        assertDoesNotThrow(customerDao.save(new Customer()), NullPointerException.class);
    }
}
