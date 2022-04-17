package ru.sstu.hobbyboard.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.sstu.hobbyboard.entities.User;

import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User findByEmail(String s);

    User findByNickName(String nickName);

    @Query(value = "select users.*\n" +
            "from users\n" +
            "full join purchase\n" +
            "on users.id = purchase.user_id\n" +
            "group by (users.id)\n" +
            "order by count(purchase.id) desc\n" +
            "limit 3", nativeQuery = true)
    List<Integer> findTop3ByPurchases();
}
