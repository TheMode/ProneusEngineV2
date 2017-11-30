package fr.proneus.engine.script;

import fr.proneus.engine.utils.FileUtils;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

public class ScriptManager {

    private Globals globals = JsePlatform.standardGlobals();

    public void ScriptManager() {

    }

    public Script loadScript(String path) {
        LuaValue value = globals.load(FileUtils.getInternalFileString(path));
        value.call();
        return new Script(globals, value);
    }
}
