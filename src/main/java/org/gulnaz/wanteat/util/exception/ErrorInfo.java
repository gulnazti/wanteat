package org.gulnaz.wanteat.util.exception;

/**
 * @author gulnaz
 */
public class ErrorInfo {
    private final String url;
    private final String[] details;

    public ErrorInfo(CharSequence url, String[] details) {
        this.url = url.toString();
        this.details = details;
    }
}
