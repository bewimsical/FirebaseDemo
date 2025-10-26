package aydin.firebasedemo;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AccessDataView{

    private final StringProperty personName = new SimpleStringProperty();
    private final int age=0;
    private final StringProperty personPhoneNumber = new SimpleStringProperty();
    private final ReadOnlyBooleanWrapper writePossible = new ReadOnlyBooleanWrapper();

    public AccessDataView() {
        writePossible.bind(personName.isNotEmpty());
    }

    public StringProperty personNameProperty() {
        return personName;
    }
    public StringProperty personPhoneNumberProperty() {
        return personPhoneNumber;
    }

    public ReadOnlyBooleanProperty isWritePossibleProperty() {
        return writePossible.getReadOnlyProperty();
    }
}
