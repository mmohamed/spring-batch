package dev.medinvention.writer;

public class PersonWriterException extends RuntimeException {

    private static final long serialVersionUID = -8975219543865911500L;

    public PersonWriterException(String message) {
        super("[PersonWriterException] " + message);
    }
}
