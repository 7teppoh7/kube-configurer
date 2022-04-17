package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.repositories.PhotoFeedbackRepository;

@Service
public class PhotoFeedbackService {

    private final PhotoFeedbackRepository photoFeedbackRepository;

    public PhotoFeedbackService(PhotoFeedbackRepository photoFeedbackRepository) {
        this.photoFeedbackRepository = photoFeedbackRepository;
    }
}
