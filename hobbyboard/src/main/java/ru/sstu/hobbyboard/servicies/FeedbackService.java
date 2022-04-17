package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import ru.sstu.hobbyboard.repositories.FeedbackRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }
}
