package org.eclipse.smarthome.automation.module.script.jsext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.eclipse.smarthome.automation.module.script.ScriptExtensionProvider;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class JsExtRuleSupportScriptExtension implements ScriptExtensionProvider {

    private static String TIMER_PRESET = "Timer";
    private static String TIMER_INSTANCE = "timer";
    private static String TIMER_CLASS = "Timer";

    private Map<String, Map<String, Object>> types = new HashMap<>();

    @Override
    public Collection<String> getDefaultPresets() {
        return Collections.emptyList();
    }

    @Override
    public Collection<String> getPresets() {
        return Collections.singleton(TIMER_PRESET);
    }

    @Override
    public Collection<String> getTypes() {
        return Arrays.asList(TIMER_INSTANCE, TIMER_CLASS);
    }

    @Override
    public Object get(String scriptIdentifier, String type) throws IllegalArgumentException {
        if (types.containsKey(scriptIdentifier)) {
            Map<String, Object> map = types.get(scriptIdentifier);
            return map.get(type);
        }
        return null;
    }

    @Override
    public Map<String, Object> importPreset(String scriptIdentifier, String preset) {

        unload(scriptIdentifier);

        Map<String, Object> presetMap = new HashMap<String, Object>();
        presetMap.put(TIMER_INSTANCE, new Timer(TIMER_INSTANCE, false));
        presetMap.put(TIMER_CLASS, Timer.class);

        types.put(scriptIdentifier, presetMap);

        return presetMap;
    }

    @Override
    public void unload(String scriptIdentifier) {
        if (types.containsKey(scriptIdentifier)) {
            Map<String, Object> map = types.get(scriptIdentifier);
            if (map.containsKey(TIMER_INSTANCE)) {
                Timer timer = (Timer) map.get(TIMER_INSTANCE);
                timer.cancel();
            }
        }
    }
}