package me.bergenfly.nations.commands.nation;

public class NationAttempt {
    private final Settlement capital;
    private final List<Settlement> agreers = new ArrayList();
    private final String name;


    public NationAttempt(Settlement capital, String name) {
        this.name = name;
        this.capital = capital;
    }

    public void addAgreer(Settlement settlement) {
        agreers.add(settlement);

        if(agreers.size() > 2) {
            Nation nation = Nation.tryCreate();
        }
    }

    public List<Settlement> getAgreers() {
        return new ArrayList(agreers);
    }
}
