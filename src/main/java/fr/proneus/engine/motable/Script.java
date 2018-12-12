package fr.proneus.engine.motable;

import fr.themode.motable.network.ScriptEvent;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Script {

    private String scriptString;

    private ScriptEvent event;
    private String customEvent;
    private CompiledScript compiledScript;
    private int priority;

    public Script(String scriptString, ScriptEvent event, int priority) {
        this.scriptString = scriptString;
        this.event = event;
        this.priority = priority;
    }

    public Script(String scriptString, String customEvent, int priority) {
        this.scriptString = scriptString;
        this.customEvent = customEvent;
        this.priority = priority;
    }

    public String getScriptString() {
        return scriptString;
    }

    public ScriptEvent getEvent() {
        return event;
    }

    public String getCustomEvent() {
        return customEvent;
    }

    public CompiledScript getCompiledScript() {
        return compiledScript;
    }

    public int getPriority() {
        return priority;
    }

    public void compile(ScriptEngine scriptEngine) {
        try {
            this.compiledScript = ((Compilable) scriptEngine).compile(scriptString);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
