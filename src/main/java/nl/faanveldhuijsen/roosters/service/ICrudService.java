package nl.faanveldhuijsen.roosters.service;

import java.util.Collection;

public interface ICrudService<D, S> {

    D create(D user);
    D update(Long id, D user);
    D get(Long id);
    D delete(Long id);
    Collection<S> fetch();

    /**
     * @throws nl.faanveldhuijsen.roosters.utils.exceptions.ResourceNotFound When there are no results
     */
    void notFound();
}
