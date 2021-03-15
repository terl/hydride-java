package co.libly.hydride;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import java.lang.String;

public class HydroKxState extends Structure {

    public HydroKxKeypair eph_kp;

    public HydroHashState h_st;

    public HydroKxState() {
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("eph_kp", "h_st");
    }

    public HydroKxState(HydroKxKeypair eph_kp, HydroHashState h_st) {
        super();
        this.eph_kp = eph_kp;
        this.h_st = h_st;
    }

    public HydroKxState(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends HydroKxState implements Structure.ByReference {
    }

    public static class ByValue extends HydroKxState implements Structure.ByValue {
    }
}
