package org.gulnaz.wanteat.util.exception;

/**
 * @author gulnaz
 */
public class VoteException extends RuntimeException {
    public static final String VOTING_TIME_EXPIRED = "Voting time is expired. You can vote until 11:00.";

    public VoteException(String message) {
        super(message);
    }
}
