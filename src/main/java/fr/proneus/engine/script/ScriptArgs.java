package fr.proneus.engine.script;

import org.luaj.vm2.Varargs;

public class ScriptArgs {

    private Varargs args;
    private int arg;

    protected ScriptArgs(Varargs args, int arg) {
        this.args = args;
        this.arg = arg;
    }

    public boolean toBoolean() {
        return args.toboolean(arg);
    }

    public String toString() {
        return args.checkjstring(arg);
    }

    public int toInteger() {
        return args.checkint(arg);
    }

    public long toLong() {
        return args.checklong(arg);
    }

    public double toDouble() {
        return args.checkdouble(arg);
    }
}
