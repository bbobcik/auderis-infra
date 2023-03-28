package cz.auderis.infra.toggle.processor;

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
