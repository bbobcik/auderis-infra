package cz.auderis.infra.toggle.instrument.proc;

import java.util.Map;

/**
 * Created on 28.3.2023.
 */
class RegisteredToggleManager {

    private final Map<String, RegisteredToggle> toggleByName;

    public RegisteredToggleManager() {
        toggleByName = Map.of();
    }
}
