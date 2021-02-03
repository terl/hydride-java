package co.libly.hydride;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

public class NativeSize extends IntegerType {

    private static final long SerialVersionUID = 2398288011955445078L;

    public static int SIZE = Native.SIZETSIZE;

    public NativeSize() {
        this(0);
    }

    public NativeSize(long Value) {
        super(SIZE, Value);
    }
}
