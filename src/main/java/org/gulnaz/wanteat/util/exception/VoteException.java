package org.gulnaz.wanteat.util.exception;

/**
 * @author gulnaz
 */
public class VoteException extends RuntimeException {
    public static final String CHANGE_VOTE_TIME_EXPIRED = "You can change your vote until 11:00.";

    public VoteException(String message) {
        super(message);
    }
}
