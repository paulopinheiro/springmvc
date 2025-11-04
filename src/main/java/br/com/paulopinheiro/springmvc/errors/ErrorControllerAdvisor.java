package br.com.paulopinheiro.springmvc.errors;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorControllerAdvisor {
//    @ExceptionHandler(SQLException.class)
//    public String handleSQLException(HttpServletRequest request, Exception ex) {
//        return "sql_error";
//    }
//
//    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "IOException occured")
//    @ExceptionHandler(IOException.class)
//    public void handleIOException() {
//        System.out.println("In the handleIOException() method");
//        // returning 404 error code
//    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle(Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
        return "404";
    }
}
