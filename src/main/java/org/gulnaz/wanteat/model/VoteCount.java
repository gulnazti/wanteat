package org.gulnaz.wanteat.model;

/**
 * @author gulnaz
 */
// Customizing the Result with Spring Data Projection
// https://www.baeldung.com/jpa-queries-custom-result-with-aggregation-functions#solution_interface_jpa
public interface VoteCount {
    Integer getRestaurantId();
    Integer getTotalVotes();
}
