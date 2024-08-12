package me.bergenfly.nations.api.model.organization;

public interface Settlement extends PlayerGroup, Named, LandAdministrator, Led {
    boolean register();
}
