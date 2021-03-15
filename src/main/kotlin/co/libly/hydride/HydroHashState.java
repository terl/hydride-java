package co.libly.hydride;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import java.lang.String;

public class HydroHashState extends Structure {

    public int[] state = new int[12];

    public byte buf_off;

    public byte[] align = new byte[3];

    public HydroHashState() {
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("state", "buf_off", "align");
    }

    public HydroHashState(int[] state, byte buf_off, byte[] align) {
        super();
        if ((state.length != this.state.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.state = state;
        this.buf_off = buf_off;
        if ((align.length != this.align.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.align = align;
    }

    public HydroHashState(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends HydroHashState implements Structure.ByReference {
    }

    public static class ByValue extends HydroHashState implements Structure.ByValue {
    }
}
