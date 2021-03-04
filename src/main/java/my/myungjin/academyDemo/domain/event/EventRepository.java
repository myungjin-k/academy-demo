package my.myungjin.academyDemo.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 활성화 되어있는 이벤트 조회
    List<Event> findByStatusEquals(EventStatus status);

}
