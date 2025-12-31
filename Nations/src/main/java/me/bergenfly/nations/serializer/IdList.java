package me.bergenfly.nations.serializer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IdList implements Serializable {

    private final Collection<? extends Serializable> collection;

    public IdList(Collection<? extends Serializable> collection) {
        this.collection = collection;
    }


    @Override
    public List<String> serialize() {
        return collection.stream().map(Serializable::getId).collect(Collectors.toList());
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("Serlialization error: IdList does not support getId");
    }
}
