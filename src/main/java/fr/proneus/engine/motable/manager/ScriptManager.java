package fr.proneus.engine.motable.manager;

import fr.proneus.engine.motable.GameState;
import fr.proneus.engine.motable.Script;
import fr.themode.motable.network.ScriptEvent;
import fr.themode.motable.network.packet.event.ServerEventPacket;
import fr.themode.utils.MathUtils;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;

import javax.script.*;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ScriptManager {

    private ScriptEngineManager scriptEngineManager;
    private ScriptEngine scriptEngine;
    private ScriptContext ctxt;
    private SimpleScriptContext tempctxt;
    private Bindings bindings;

    private Map<ScriptEvent, List<Script>> basicEvents;
    private Map<String, List<Script>> customEvents;

    public ScriptManager(List<Script> scripts) {
        this.scriptEngineManager = new ScriptEngineManager();
        this.scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
        this.ctxt = scriptEngine.getContext();
        this.tempctxt = new SimpleScriptContext();

        this.basicEvents = new HashMap<>();
        this.customEvents = new HashMap<>();

        scripts.forEach(script -> {
            script.compile(scriptEngine);
            boolean isBasic = script.getEvent() != null;
            boolean isCustom = script.getCustomEvent() != null;

            if (isBasic) {
                addBasicEventScript(script);
            } else if (isCustom) {
                addCustomEventScript(script);
            }

        });
    }

    // TODO multiple bindings
    public void callEvent(ScriptEvent event) {
        List<Script> scripts = this.basicEvents.computeIfAbsent(event, (list) -> new ArrayList<>());
        if (scripts.isEmpty())
            return;
        scripts.forEach(script -> {
            try {
                script.getCompiledScript().eval(tempctxt);
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        });
    }

    public void setupBindings(GameState gameState) {
        BiFunction<String, ScriptObjectMirror, Void> serverCall = (name, object) -> {
            Object[] args = (Object[]) ScriptUtils.convert(object, Object[].class);
            for (int i = 0; i < args.length; i++) {
                Object value = args[i];
                if (value instanceof Integer) {
                    args[i] = ((Integer) value).floatValue();
                } else if (value instanceof Double) {
                    args[i] = ((Double) value).floatValue();
                }
            }
            ServerEventPacket eventPacket = new ServerEventPacket();
            eventPacket.eventName = name;
            eventPacket.args = args;
            gameState.getClient().sendTCP(eventPacket);
            return null;
        };

        this.bindings = scriptEngine.createBindings();
        bindings.put("callServerEvent", serverCall);
        bindings.put("client", gameState.getServerClient());
        bindings.put("input", gameState.getGame().getInput());
        bindings.put("MathHelper", MathUtils.class);

        bindings.put("objectManager", gameState.getGameObjectManager());

        bindings.put("log", (Consumer<String>) System.out::println);

        // Set context
        tempctxt.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
        tempctxt.setBindings(ctxt.getBindings(ScriptContext.GLOBAL_SCOPE),
                ScriptContext.GLOBAL_SCOPE);
        tempctxt.setWriter(ctxt.getWriter());
        tempctxt.setReader(ctxt.getReader());
        tempctxt.setErrorWriter(ctxt.getErrorWriter());
    }

    private void addBasicEventScript(Script script) {
        ScriptEvent event = script.getEvent();
        List<Script> scripts = this.basicEvents.computeIfAbsent(event, (list) -> new ArrayList<>());
        scripts.add(script);
        sortScripts(scripts);
        this.basicEvents.put(event, scripts);
    }

    private void addCustomEventScript(Script script) {
        String event = script.getCustomEvent();
        List<Script> scripts = this.customEvents.computeIfAbsent(event, (list) -> new ArrayList<>());
        scripts.add(script);
        sortScripts(scripts);
        this.customEvents.put(event, scripts);
    }

    private void sortScripts(List<Script> scripts) {
        scripts.sort(Comparator.comparing(script -> script.getPriority()));
    }

}
