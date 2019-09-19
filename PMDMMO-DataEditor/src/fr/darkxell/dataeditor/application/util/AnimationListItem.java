package fr.darkxell.dataeditor.application.util;

import com.darkxell.client.mechanics.animation.Animations;
import com.darkxell.client.mechanics.animation.Animations.AnimationGroup;
import com.darkxell.common.item.Item;
import com.darkxell.common.move.Move;
import com.darkxell.common.pokemon.ability.Ability;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.status.StatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.util.Pair;
import com.darkxell.common.util.language.Localization;

public class AnimationListItem extends CustomTreeItem implements Comparable<AnimationListItem> {

    public static AnimationListItem create(String anim) {
        Pair<Integer, AnimationGroup> id = Animations.splitID(anim);
        if (id == null)
            return null;
        return new AnimationListItem(id.second, id.first);
    }

    public final AnimationGroup group;
    public final int id;

    public AnimationListItem(AnimationGroup folder, int id) {
        super();
        this.group = folder;
        this.id = id;
    }

    @Override
    public int compareTo(AnimationListItem o) {
        if (this.group.equals(o.group))
            return Integer.compare(this.id, o.id);
        return this.group.compareTo(o.group);
    }

    private String nameDetails() {
        String detail = null;
        switch (this.group) {
        case Abilities:
            Ability a = Ability.find(this.id);
            if (a != null)
                detail = a.name().toString();
            break;

        case Items:
            Item i = Registries.items().find(this.id);
            if (i != null)
                detail = i.name().toString();
            break;

        case Moves:
            Move m = Registries.moves().find(this.id);
            if (m != null)
                detail = m.name().toString();
            break;

        case Projectiles:
            detail = Integer.toString(this.id);
            if (this.id >= 1000) {
                Move mp = Registries.moves().find(this.id - 1000);
                if (mp != null)
                    detail = mp.name().toString() + " projectile";
            }
            break;

        case Statuses:
            StatusCondition s = StatusConditions.find(this.id);
            if (s != null)
                detail = s.name().toString();
            break;

        case MoveTargets:
            Move m2 = Registries.moves().find(this.id);
            if (m2 != null)
                detail = m2.name().toString() + " target";
            break;

        default:
            if (Localization.containsKey("animation.custom." + this.id))
                detail = Localization.translate("animation.custom." + this.id);
            break;
        }
        if (detail == null)
            return "";
        detail = detail.replaceAll("<.*?>", "");
        while (detail.startsWith(" "))
            detail = detail.substring(1);
        return detail;
    }

    @Override
    public String toString() {
        return this.id + "- " + this.nameDetails();
    }

}
