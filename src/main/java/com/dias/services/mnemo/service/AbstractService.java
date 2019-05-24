package com.dias.services.mnemo.service;

import com.dias.services.mnemo.exception.ObjectNotFoundException;
import com.dias.services.mnemo.model.AbstractModel;
import com.dias.services.mnemo.repository.AbstractRepository;

import java.util.Optional;

public abstract class AbstractService<T extends AbstractModel> {

    protected abstract AbstractRepository<T> getRepository();

    /**
     * returns model
     * throws {@link ObjectNotFoundException} if not found
     */
    public T getById(Long id) {
        return Optional.ofNullable(getRepository().getById(id))
                .orElseThrow(() -> new ObjectNotFoundException(id));
    }

    public void create(T model) {
        getRepository().create(model);
    }

    public int delete(Long id) {
        checkObjectExists(id);
        return getRepository().delete(id);
    }

    private void checkObjectExists(Long id) {
        if (getById(id) == null) {
            throw new ObjectNotFoundException(id);
        }
    }

}
