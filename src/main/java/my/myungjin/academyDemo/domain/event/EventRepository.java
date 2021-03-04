package my.myungjin.academyDemo.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    // 활성화 되어있는 이벤트 조회
    List<Event> findByStatusEquals(EventStatus status);

}
