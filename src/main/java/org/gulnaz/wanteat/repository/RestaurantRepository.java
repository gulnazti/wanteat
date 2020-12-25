package org.gulnaz.wanteat.repository;

import java.time.LocalDate;
import java.util.List;

import org.gulnaz.wanteat.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gulnaz
 */
@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Override
    @Transactional
    Restaurant save(Restaurant restaurant);

    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @EntityGraph(attributePaths = "menu", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r LEFT JOIN r.menu m ON r.id = m.restaurant.id AND m.created = ?1")
    List<Restaurant> getAllWithMenu(LocalDate date);

    @EntityGraph(attributePaths = "menu", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r LEFT JOIN r.menu m ON r.id = m.restaurant.id AND m.created = ?2 WHERE r.id = ?1")
    Restaurant getWithMenu(int id, LocalDate date);
}
