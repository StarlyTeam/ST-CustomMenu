package net.starly.custommenu.expansion.action.data;

import java.io.Serializable;
import java.util.List;

public interface ActionBase extends Serializable {

    String getActionType();
    List<String> getArgs();
}
