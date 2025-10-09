package me.bergenfly.nations.api.model.organization;

public interface DeletionSubscriber {
    void deleted(Deletable deletable);
}
