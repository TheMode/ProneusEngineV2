package fr.proneus.engine.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Script {

    private ScriptEngine engine;

    public Script(ScriptEngine engine) {
        this.engine = engine;
    }

    public <T> T execute(String function, Object... args) {
        Invocable inv = (Invocable) engine;
        try {
            return (T) inv.invokeFunction(function, args);
        } catch (NoSuchMethodException | ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasFunction(String function)
            throws ScriptException {
        String test = "typeof " + function
                + " === 'function' ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE";
        return (Boolean) engine.eval(test);
    }
}
