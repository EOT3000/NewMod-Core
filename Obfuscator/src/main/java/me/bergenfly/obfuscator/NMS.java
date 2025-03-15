package me.bergenfly.obfuscator;


import com.comphenix.protocol.events.PacketContainer;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//Imma be honest I don't know who wrote this code. Maybe it was me, but I don't remember doing that.
public class NMS {
    public static void ifPresentProcessVec(PacketContainer container, int index, int addX, int addZ) {
        @SuppressWarnings("unchecked")
        Optional<Vec3> optional = (Optional<Vec3>) container.getModifier().read(index);

        optional.ifPresent(vec3D -> container.getModifier().write(index, vec3D.add(addX, 0, addZ)));
    }

    public enum MovementFlag {
        X,
        Y,
        Z,
        Y_ROT,
        X_ROT;



        public static Set<MovementFlag> fromNMS(Set<Relative> nms) {
            return nms.stream().map((x) -> MovementFlag.values()[x.ordinal()]).collect(Collectors.toSet());
        }

        public static Set<Relative> toNMS(Set<MovementFlag> buk) {
            return buk.stream().map((x) -> Relative.values()[x.ordinal()]).collect(Collectors.toSet());
        }

        public static Set<MovementFlag> fromInt(int mask) {
            Set<Relative> set = Relative.unpack(mask);

            return fromNMS(set);
        }

        public static int toInt(Set<MovementFlag> flags) {
            Set<Relative> nms = toNMS(flags);

            return Relative.pack(nms);
        }
    }
}
