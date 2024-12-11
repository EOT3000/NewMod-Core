package me.bergenfly.nations.api.model.organization;

import java.util.Collection;
import java.util.Map;

public interface Deletable {
    void subscribeToDeletion(DeletionSubscriber subscriber);

    void delete();

    boolean isDeleted();
}
