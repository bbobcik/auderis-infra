package cz.auderis.infra.toggle.processor;

import javax.lang.model.element.TypeElement;
import java.util.List;

class RegisteredToggle {

    private final String name;
    private final InstrumentationType type;

    private List<TypeElement> occurrences;

    RegisteredToggle(String name, InstrumentationType type) {
        assert null != name;
        assert null != type;
        this.name = name;
        this.type = type;
    }

    String getName() {
        return name;
    }

    InstrumentationType getType() {
        return type;
    }

    List<TypeElement> getOccurrences() {
        return (null != occurrences) ? occurrences : List.of();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof RegisteredToggle)) {
            return false;
        }
        final RegisteredToggle other = (RegisteredToggle) obj;
        if (type != other.getType()) {return false;} else return name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        int h = 0;
        h = 31 * h + name.hashCode();
        h = 31 * h + type.hashCode();
        return h;
    }
}
