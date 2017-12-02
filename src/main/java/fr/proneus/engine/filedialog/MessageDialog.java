package fr.proneus.engine.filedialog;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

public class MessageDialog {

    private boolean accept;

    protected MessageDialog(String title, String message, DialogType dialogType, MessageIcon messageIcon, MessageDefault messageDefault) {
        accept = tinyfd_messageBox(title, message, dialogType.getName(), messageIcon.getName(), messageDefault.getResult());
    }

    public boolean isAccepted() {
        return accept;
    }

    public enum DialogType {
        OK("ok"), OK_CANCEL("okcancel"), YES_NO("yesno"), YES_NO_CANCEL("yesnocancel");

        private String name;

        private DialogType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    public enum MessageIcon {
        INFO("info"), WARNING("warning"), ERROR("error"), QUESTION("question");

        private String name;

        private MessageIcon(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum MessageDefault {
        ACCEPT(true), REFUSE(false);

        private boolean result;

        private MessageDefault(boolean result) {
            this.result = result;
        }

        public boolean getResult() {
            return result;
        }
    }
}
