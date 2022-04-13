package nl.faanveldhuijsen.roosters.model;

import nl.faanveldhuijsen.roosters.dto.IData;

import java.util.ArrayList;
import java.util.List;

public interface IModel<D, S> {

    D toData();

    S toDataSlim();
}
