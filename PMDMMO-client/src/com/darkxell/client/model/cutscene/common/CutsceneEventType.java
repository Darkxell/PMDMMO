package com.darkxell.client.model.cutscene.common;


public enum CutsceneEventType {
    // Don't forget to also modify EditCutsceneController#onCreate().
    animate("Play animation"),
    camera("Move camera"),
    delay("Wait X ticks"),
    despawn("Despawn Entity"),
    dialog("Show Dialog"),
    drawmap("Draw Map"),
    function("Call function"),
    move("Move Entity"),
    music("Change soundtrack"),
    option("Select option"),
    optionresult("Trigger events by option result"),
    rotate("Rotate Entity"),
    setanimated("Animate Pokemon"),
    setstate("Set Pokemon State"),
    sound("Play sound"),
    spawn("Spawn Entity"),
    wait("Wait for events to finish");

    public final String description;

    CutsceneEventType(String name) {
        this.description = name;
    }
}