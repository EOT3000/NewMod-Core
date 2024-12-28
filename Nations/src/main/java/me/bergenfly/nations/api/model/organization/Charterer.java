package me.bergenfly.nations.api.model.organization;

public interface Charterer extends LandAdministrator {

    /**
     * Checks if the given company is chartered here. If yes, the company can own land in the area of this charterer.
     *
     * @param company the company to check.
     * @return true if the company is chartered here, false if not.
     */
    boolean isChartered(Company company);

    void charter(Company company);
}
