package com.darkxell.client.state.menu.menus;

import java.awt.Rectangle;

import com.darkxell.client.launchable.ClientSettings;
import com.darkxell.client.launchable.Persistence;
import com.darkxell.client.renderers.layers.AbstractGraphiclayer;
import com.darkxell.client.state.menu.AbstractMenuState;
import com.darkxell.client.state.menu.MenuOption;
import com.darkxell.client.state.menu.OptionSelectionMenuState;
import com.darkxell.common.util.Logger;
import com.darkxell.common.util.language.Localization;
import com.darkxell.common.util.language.Localization.Language;
import com.darkxell.common.util.language.Message;

public class LanguageSelectionMenuState extends OptionSelectionMenuState<MenuOption> {

    public static interface LanguageSelectionListener {
        public void onLangSelected(Language lang);
    }

    private Language[] languages;
    public LanguageSelectionListener listener = null;
    private AbstractMenuState<?> parent;

    public LanguageSelectionMenuState(AbstractMenuState<?> parent, AbstractGraphiclayer backgroundState,
            boolean isOpaque) {
        super(backgroundState, isOpaque);
        this.parent = parent;
        this.languages = Localization.Language.values();
        this.createOptions();
    }

    @Override
    protected void createOptions() {
        MenuTab<MenuOption> tab = new MenuTab<>(new Message("menu.settings.choose_language"));
        for (Language l : languages)
            tab.addOption(new MenuOption(new Message(l.name, false)));
        this.tabs.add(tab);
    }

    @Override
    protected Rectangle mainWindowDimensions() {
        Rectangle r = super.mainWindowDimensions();
        return new Rectangle(40, 40, r.width, r.height);
    }

    @Override
    protected void onExit() {
    }

    @Override
    protected void onOptionSelected(MenuOption option) {
        Language lang = this.languages[this.optionIndex()];
        ClientSettings.setSetting(ClientSettings.LANGUAGE, lang.id);
        Localization.setLanguage(lang);
        if (this.listener != null)
            this.listener.onLangSelected(lang);
        else if (this.parent != null)
            Persistence.stateManager.setState(this.parent);
        else
            Logger.e("Language menu state has no listener or parent!");
    }

}
