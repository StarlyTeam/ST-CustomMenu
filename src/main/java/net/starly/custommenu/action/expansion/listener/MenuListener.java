package net.starly.custommenu.action.expansion.listener;

import net.starly.custommenu.action.expansion.listener.event.MenuButtonClickEvent;
import net.starly.custommenu.action.expansion.listener.event.MenuCloseEvent;
import net.starly.custommenu.action.expansion.listener.event.MenuOpenEvent;

public interface MenuListener {

    void onClick(MenuButtonClickEvent event);

    void onOpen(MenuOpenEvent event);

    void onClose(MenuCloseEvent event);
}
