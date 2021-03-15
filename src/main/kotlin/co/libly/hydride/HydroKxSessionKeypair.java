package co.libly.hydride;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import java.lang.String;

public class HydroKxSessionKeypair extends Structure {

    public byte[] rx = new byte[32];

    public byte[] tx = new byte[32];

    public HydroKxSessionKeypair() {
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("rx", "tx");
    }

    public HydroKxSessionKeypair(byte[] rx, byte[] tx) {
        super();
        if ((rx.length != this.rx.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.rx = rx;
        if ((tx.length != this.tx.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.tx = tx;
    }

    public HydroKxSessionKeypair(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends HydroKxSessionKeypair implements Structure.ByReference {
    }

    public static class ByValue extends HydroKxSessionKeypair implements Structure.ByValue {
    }
}
