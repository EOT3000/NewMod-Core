package me.bergenfly.newmod.flyfun.technology;

import me.bergenfly.newmod.flyfun.technology.util.ComponentAccess;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a task an energy component can do.
 */
//TODO: do this
public interface EnergyComponentTask extends Consumer<ComponentAccess> {
    static EnergyComponentTask generate(Function<ComponentAccess, Integer> generatorFunction) {
        return (x) -> x.addCharge(generatorFunction.apply(x));
    }
}
