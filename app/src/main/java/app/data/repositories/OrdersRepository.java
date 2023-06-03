package app.data.repositories;

import app.data.models.OrdersDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<OrdersDao, Integer> {
    List<OrdersDao> findAllWhereStatusIn(List<String> statusList);
}
