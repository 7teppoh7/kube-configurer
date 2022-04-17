package ru.sstu.hobbyboard.exceptionsOLD;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MyBadRequestException extends RuntimeException {
    private String myMessage;

    public MyBadRequestException(String message) {
        this.myMessage = message;
    }

    public String getMyMessage() {
        return myMessage;
    }

    public void setMyMessage(String myMessage) {
        this.myMessage = myMessage;
    }
}
