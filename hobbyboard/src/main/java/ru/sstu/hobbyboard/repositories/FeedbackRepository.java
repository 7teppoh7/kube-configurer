package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.Feedback;

public interface FeedbackRepository extends PagingAndSortingRepository<Feedback, Integer> {
}
