package fr.proneus.engine.script;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.Varargs;

public class ScriptArg {

    private Varargs args;
    private int arg;

    // TODO check if
    protected ScriptArg(Varargs args, int arg) {
        this.args = args;
        this.arg = arg;
    }

    public boolean toBoolean() {
        return args.toboolean(arg);
    }

    public String toString() {
        return args.checkjstring(arg);
    }

    public boolean isInt() {
        try {
            args.checkint(arg);
            return true;
        } catch (LuaError exception) {
            return false;
        }
    }

    public int toInt() {
        return args.toint(arg);
    }

    public long toLong() {
        return args.checklong(arg);
    }

    public double toDouble() {
        return args.checkdouble(arg);
    }
}
