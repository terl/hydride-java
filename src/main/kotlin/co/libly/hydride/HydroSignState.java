package co.libly.hydride;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import java.lang.String;

public class HydroSignState extends Structure {

    public HydroHashState hash_st;

    public HydroSignState() {
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("hash_st");
    }

    public HydroSignState(HydroHashState hash_st) {
        super();
        this.hash_st = hash_st;
    }

    public HydroSignState(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends HydroSignState implements Structure.ByReference {
    }

    public static class ByValue extends HydroSignState implements Structure.ByValue {
    }
}
