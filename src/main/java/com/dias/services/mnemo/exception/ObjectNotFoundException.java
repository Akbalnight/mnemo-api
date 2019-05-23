package com.dias.services.mnemo.exception;

public class ObjectNotFoundException extends Exception {
    private Long objectId;

    public ObjectNotFoundException(Long objectId) {
        this.objectId = objectId;
    }

    public Long getObjectId() {
        return objectId;
    }

    @Override
    public String getMessage() {
        return "Object not found: id = " + objectId;
    }

}
