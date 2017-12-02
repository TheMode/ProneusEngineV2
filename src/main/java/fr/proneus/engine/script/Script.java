package fr.proneus.engine.script;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class Script {

    private Globals globals;
    private LuaValue value;

    public Script(Globals globals, LuaValue value) {
        this.globals = globals;
        this.value = value;
    }

    public ScriptValue runMethod(String name, Object... args) {
        LuaValue value = globals.get(name);
        LuaValue[] luaArgs = new LuaValue[args.length];
        for (int i = 0; i < args.length; i++) {
            luaArgs[i] = CoerceJavaToLua.coerce(args[i]);
        }
        if (!value.isnil()) {
            return new ScriptValue(value.invoke(luaArgs));
        }
        return null;
    }

}
