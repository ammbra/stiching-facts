package org.acme.experience.stichingfacts.client.exception;

public class FactNotFoundException extends Throwable {
    public FactNotFoundException(String msg) {
        super(msg);
    }
}