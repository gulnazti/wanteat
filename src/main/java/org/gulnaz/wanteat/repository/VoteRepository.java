package org.gulnaz.wanteat.repository;

import java.time.LocalDate;
import java.util.List;

import org.gulnaz.wanteat.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gulnaz
 */
@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Override
    @Transactional
    Vote save(Vote vote);

    Vote getByUserIdAndDate(int id, LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.user.id=?1 AND v.date=?2")
    void delete(int id, LocalDate date);

    @Query("SELECT v FROM Vote v JOIN v.restaurant JOIN v.user WHERE v.date=?1")
    List<Vote> getAllVotesForToday(LocalDate date);

    @EntityGraph(attributePaths = "restaurant", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v JOIN v.restaurant WHERE v.user.id=?1 ORDER BY v.date DESC")
    List<Vote> getAllVotesByUserId(int id);
}
