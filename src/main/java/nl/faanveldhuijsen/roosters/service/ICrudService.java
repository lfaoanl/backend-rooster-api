package nl.faanveldhuijsen.roosters.service;

import java.util.Collection;

public interface ICrudService<D, S> {

    S create(D user);
    S update(Long id, D user);
    D get(Long id);
    S delete(Long id);
    Collection<S> fetch();

    /**
     * @throws nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound When there are no results
     */
    void notFound();
}
