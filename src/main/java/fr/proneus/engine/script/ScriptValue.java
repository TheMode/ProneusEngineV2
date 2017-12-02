package fr.proneus.engine.script;

import org.luaj.vm2.Varargs;

public class ScriptValue {

    private Varargs args;

    protected ScriptValue(Varargs args) {
        this.args = args;
    }

    public ScriptArgs getArg(int arg) {
        return new ScriptArgs(args, arg);
    }

}
