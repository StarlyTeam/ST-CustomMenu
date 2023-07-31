package net.starly.custommenu.action.data;

import java.io.Serializable;
import java.util.List;

public interface IAction extends Serializable {

    String getActionType();
    List<String> getArgs();
}
