package fr.proneus.engine.script;

import org.luaj.vm2.Varargs;

public class ScriptValue {

    private Varargs args;

    protected ScriptValue(Varargs args) {
        this.args = args;
    }

    public ScriptArg getArg(int arg) {
        return new ScriptArg(args, arg);
    }

}
