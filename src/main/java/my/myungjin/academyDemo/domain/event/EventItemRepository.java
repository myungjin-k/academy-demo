package my.myungjin.academyDemo.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventItemRepository extends JpaRepository<EventItem, String> {

    List<EventItem> findByEventSeq(long seq);

}
