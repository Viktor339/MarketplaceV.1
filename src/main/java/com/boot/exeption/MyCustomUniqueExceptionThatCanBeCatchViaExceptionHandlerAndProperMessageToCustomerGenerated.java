package com.boot.exeption;

public class MyCustomUniqueExceptionThatCanBeCatchViaExceptionHandlerAndProperMessageToCustomerGenerated extends RuntimeException {


    public MyCustomUniqueExceptionThatCanBeCatchViaExceptionHandlerAndProperMessageToCustomerGenerated(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
