package me.bergenfly.nations.api.model.organization;

public interface Settlement extends PlayerGroup, Named, LandAdministrator, Led, NationComponent {
    boolean register();

    @Override
    void setNation(Nation nation);
}
