package fr.darkxell.dataeditor.application.data;

import com.darkxell.common.util.language.Message;

public class DialogOptionTableItem {

    public String message;
    public Boolean translate;

    public DialogOptionTableItem(String message, Boolean translate) {
        this.message = message;
        this.translate = translate;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTranslate() {
        return String.valueOf(this.translate);
    }

    public Message toMessage() {
        return new Message(this.message, this.translate);
    }

}
