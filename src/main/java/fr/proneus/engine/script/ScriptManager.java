package fr.proneus.engine.script;

import fr.themode.utils.file.FileUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptManager {

    private ScriptEngineManager manager = new ScriptEngineManager();

    public Script loadScript(String code) {
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            engine.eval(code);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return new Script(engine);
    }

    public Script loadInternalScriptFile(String path) {
        return loadScript(FileUtils.getInternalFileString(path));
    }

    public Script loadExternalScriptFile(String path) {
        return loadScript(FileUtils.getExternalFileString(path));
    }

}
