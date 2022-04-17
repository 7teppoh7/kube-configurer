package ru.sstu.hobbyboard.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.PhotoFeedback;

public interface PhotoFeedbackRepository extends PagingAndSortingRepository<PhotoFeedback, Integer> {
}
