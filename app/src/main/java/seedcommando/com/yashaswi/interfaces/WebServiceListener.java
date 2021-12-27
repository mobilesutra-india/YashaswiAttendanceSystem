package seedcommando.com.yashaswi.interfaces;

public interface WebServiceListener {
    void onError(final int responseCode, final String errorMessage);

    void onResult(final Object response);
}
