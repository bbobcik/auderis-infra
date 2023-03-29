package cz.auderis.infra.toggle.instrument.proc;

import javax.lang.model.element.TypeElement;

enum InstrumentationType {

    FEATURE_TOGGLE,
    FEATURE_METHOD_PAIR,
    TOGGLE_OBSERVER,
    ;


    static InstrumentationType forType(TypeElement elem) {
        final var qname = elem.getQualifiedName().toString();
        return switch (qname) {
            case "cz.auderis.infra.toggle.FeatureToggle" -> FEATURE_TOGGLE;
            case "cz.auderis.infra.toggle.FeatureMethod" -> FEATURE_METHOD_PAIR;
            case "cz.auderis.infra.toggle.FeatureToggleObserver" -> TOGGLE_OBSERVER;
            default -> throw new IllegalArgumentException("Unsupported annotation type: " + qname);
        };
    }

}
