package my.myungjin.academyDemo.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EventTargetRepository extends JpaRepository<EventTarget, String> {

    Set<EventTarget> findByEvent(Event event);

}
