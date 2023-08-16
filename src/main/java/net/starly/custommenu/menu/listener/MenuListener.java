package net.starly.custommenu.menu.listener;

import net.starly.custommenu.menu.listener.event.MenuClickEvent;
import net.starly.custommenu.menu.listener.event.MenuCloseEvent;
import net.starly.custommenu.menu.listener.event.MenuOpenEvent;

public abstract class MenuListener {

    public void onClick(MenuClickEvent event) {}

    public void onOpen(MenuOpenEvent event) {}

    public void onClose(MenuCloseEvent event) {}
}
