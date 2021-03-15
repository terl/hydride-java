package co.libly.hydride;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import java.lang.String;

public class HydroKxKeypair extends Structure {

    public byte[] pk = new byte[32];

    public byte[] sk = new byte[32];

    public HydroKxKeypair() {
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("pk", "sk");
    }

    public HydroKxKeypair(byte[] pk, byte[] sk) {
        super();
        if ((pk.length != this.pk.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.pk = pk;
        if ((sk.length != this.sk.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.sk = sk;
    }

    public HydroKxKeypair(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends HydroKxKeypair implements Structure.ByReference {
    }

    public static class ByValue extends HydroKxKeypair implements Structure.ByValue {
    }
}
