package net.starly.custommenu.action.expansion;

import java.util.List;

public interface IExpansionRegistry {

    void registerExpansion(IExpansion expansion);

    IExpansion getExpansion(String type);

    List<IExpansion> getAllExpansion();

    boolean isExpansionRegistered(String type);

    void unregisterExpansion(String type);

    void unregisterAll();
}
