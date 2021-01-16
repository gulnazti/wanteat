package org.gulnaz.wanteat.repository;

import java.time.LocalDate;
import java.util.List;

import org.gulnaz.wanteat.model.Vote;
import org.gulnaz.wanteat.model.VoteCount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.voteDate=?2")
    Vote getByUserIdAndDate(int id, LocalDate date);

    @Query("SELECT v.restaurant.id AS restaurantId, COUNT(v.restaurant.id) AS totalVotes FROM Vote v "
           + "WHERE v.voteDate=?1 GROUP BY v.restaurant.id")
    List<VoteCount> countVotesForToday(LocalDate date);

    @EntityGraph(attributePaths = "restaurant", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 ORDER BY v.voteDate DESC")
    List<Vote> getAllVotesByUserId(int id);
}
