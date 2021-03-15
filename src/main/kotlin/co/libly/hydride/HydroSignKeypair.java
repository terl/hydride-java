package co.libly.hydride;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;
import java.lang.String;

public class HydroSignKeypair extends Structure {

    public byte[] pk = new byte[32];

    public byte[] sk = new byte[64];

    public HydroSignKeypair() {
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("pk", "sk");
    }

    public HydroSignKeypair(byte[] pk, byte[] sk) {
        super();
        if ((pk.length != this.pk.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.pk = pk;
        if ((sk.length != this.sk.length))
            throw new IllegalArgumentException("Wrong array size !");
        this.sk = sk;
    }

    public HydroSignKeypair(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends HydroSignKeypair implements Structure.ByReference {
    }

    public static class ByValue extends HydroSignKeypair implements Structure.ByValue {
    }
}
