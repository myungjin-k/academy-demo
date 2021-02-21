package my.myungjin.academyDemo.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TopSellerRepository extends JpaRepository<TopSeller, String> {

    List<TopSeller> findByCreateAtAfter(LocalDateTime at);

}
