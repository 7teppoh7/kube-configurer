package ru.sstu.hobbyboard.exceptionsOLD;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class MyExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(final ResourceNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMyMessage());
        return "error/404";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyBadRequestException.class)
    public String handleMyBadRequestException(final MyBadRequestException ex, Model model) {
        model.addAttribute("messages", Map.of("Сообщение: ", ex.getMyMessage()));
        return "error/400";
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(BindException.class)
//    public String handleConstraintViolationException(final BindException ex, Model model) {
//        Map<String, String> messages = new HashMap<>();
//        for (FieldError error : ex.getFieldErrors()) {
//            messages.put(error.getField(), error.getDefaultMessage());
//        }
//        model.addAttribute("messages", messages);
//        return "error/400";
//    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(MyForbiddenException.class)
    public String handleMyForbiddenException(final MyForbiddenException ex, Model model) {
        model.addAttribute("message", ex.getMyMessage());
        return "error/403";
    }
}
