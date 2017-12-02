package fr.proneus.engine.filedialog;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;


public class NotificationDialog {

    protected NotificationDialog(String title, String message, NotificationIcon icon) {
        tinyfd_notifyPopup(title, message, icon.getName());
    }

    public enum NotificationIcon {
        INFO("info"), WARNING("warning"), ERROR("error");

        private String name;

        private NotificationIcon(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
