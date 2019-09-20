package com.darkxell.common.event.pokemon;

import static com.darkxell.common.testutils.TestUtils.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.MissionClearedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent.StatusConditionEndReason;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.mission.InvalidParammetersException;
import com.darkxell.common.mission.Mission;
import com.darkxell.common.mission.MissionReward;
import com.darkxell.common.pokemon.DungeonPokemon;
import com.darkxell.common.pokemon.DungeonPokemon.DungeonPokemonType;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.testutils.AssertUtils;

public class OtherPokemonEventTest {

    private Mission mission;

    @Before
    public void before() {
        generateDefaultObjects();
        try {
            this.mission = new Mission("A", getDungeon().id, 1, 45, 45, -1,
                    new MissionReward(0, new int[] {}, new int[] {}, 0, new String[] {}), Mission.TYPE_RESCUEME);
            getPlayer().getData().missionsids.add(this.mission.toString());
        } catch (InvalidParammetersException e) {
            e.printStackTrace();
        }
        getPlayer().inventory().addItem(new ItemStack(118));
        getPlayer().inventory().addItem(new ItemStack(1));

        generateTestFloor();
    }

    @Test
    public void testIQEvent() {
        int iq = getLeftPokemon().originalPokemon.iq();
        new IncreasedIQEvent(getFloor(), null, getLeftPokemon(), 5).processServer();
        Assert.assertEquals("Incorrect resulting IQ.", iq + 5, getLeftPokemon().originalPokemon.iq());
    }

    @Test
    public void testPokemonRescuedEvent() {
        DungeonPokemon rescued = null;
        for (DungeonPokemon p : getFloor().listPokemon())
            if (p.type == DungeonPokemonType.RESCUEABLE) {
                rescued = p;
                break;
            }
        Tile tile = rescued.tile();
        ArrayList<Event> result = new PokemonRescuedEvent(getFloor(), null, rescued, getPlayer()).processServer();
        Assert.assertNull("Pokemon wasn't removed from Tile.", tile.getPokemon());
        Assert.assertTrue("Missing MissionClearedEvent.",
                AssertUtils.containsObjectOfClass(result, MissionClearedEvent.class));
    }

    @Test
    public void testRecruitAttemptEvent() {
        new RecruitAttemptEvent(getFloor(), null, getLeftPokemon(), getRightPokemon()).processServer();
        // Nothing more to test, outcome is random...
    }

    @Test
    public void testRecruitEvent() {
        getRightPokemon().setHP(2);
        new RecruitedPokemonEvent(getFloor(), null, getLeftPokemon(), getRightPokemon()).processServer();
        Assert.assertEquals("Pokemon wasn't revived.", getRightPokemon().getMaxHP(), getRightPokemon().getHp());
        Assert.assertEquals("Pokemon wasn't added to the team.", 2, getPlayer().getDungeonTeam().length);
        Assert.assertEquals("Pokemon wasn't changed to a Team member type.", DungeonPokemonType.TEAM_MEMBER,
                getRightPokemon().type);
    }

    @Test
    public void testRecruitRequestEvent() {
        new RecruitRequestEvent(getFloor(), null, getLeftPokemon(), getRightPokemon()).processServer();
        // Nothing more to test, outcome is random...
    }

    @Test
    public void testReviveEvent() {
        AppliedStatusCondition status = new AppliedStatusCondition(StatusConditions.Asleep, getRightPokemon(), null, 2);
        getRightPokemon().setHP(2);
        getRightPokemon().inflictStatusCondition(status);
        new RevivedPokemonEvent(getFloor(), null, getRightPokemon()).processServer();
        Assert.assertEquals("Pokemon HP wasn't restored.", getRightPokemon().getMaxHP(), getRightPokemon().getHp());
        Assert.assertFalse("Status condition wasn't cleared.", getRightPokemon().hasStatusCondition(status.condition));
    }

    @Test
    public void testStatusCreatedEvent() {
        new StatusConditionCreatedEvent(getFloor(), null,
                new AppliedStatusCondition(StatusConditions.Asleep, getLeftPokemon(), null, 5)).processServer();
        Assert.assertTrue("Status condition wasn't applied.",
                getLeftPokemon().hasStatusCondition(StatusConditions.Asleep));
    }

    @Test
    public void testStatusEndedEvent() {
        AppliedStatusCondition status = new AppliedStatusCondition(StatusConditions.Asleep, getLeftPokemon(), null, 5);
        getLeftPokemon().inflictStatusCondition(status);
        new StatusConditionEndedEvent(getFloor(), null, status, StatusConditionEndReason.FINISHED).processServer();
        Assert.assertFalse("Status condition wasn't removed.",
                getLeftPokemon().hasStatusCondition(StatusConditions.Asleep));
    }

    @Test
    public void testSwitchedPokemonEvent() {
        Tile left = getLeftPokemon().tile(), right = getRightPokemon().tile();
        new SwitchedPokemonEvent(getFloor(), null, getLeftPokemon(), getRightPokemon()).processServer();
        Assert.assertEquals("Left Pokemon wasn't moved to the correct Tile.", right, getLeftPokemon().tile());
        Assert.assertEquals("Right Pokemon wasn't moved to the correct Tile.", left, getRightPokemon().tile());
        Assert.assertEquals("Left Tile didn't get the correct Pokemon.", getRightPokemon(), left.getPokemon());
        Assert.assertEquals("Right Tile didn't get the correct Pokemon.", getLeftPokemon(), right.getPokemon());
    }

    @Test
    public void testTeleportEvent() {
        Tile origin = getLeftPokemon().tile(), destination = origin.adjacentTile(3, 3);
        new PokemonTeleportedEvent(getFloor(), null, getLeftPokemon(), destination).processServer();
        Assert.assertNull("Pokemon wasn't removed from origin Tile.", origin.getPokemon());
        Assert.assertEquals("Pokemon wasn't moved to destination Tile.", getLeftPokemon(), destination.getPokemon());
        Assert.assertEquals("Pokemon wasn't given the destination Tile.", destination, getLeftPokemon().tile());
    }

    @Test
    public void testTriggeredAbilityEvent() {
        new TriggeredAbilityEvent(getFloor(), null, getLeftPokemon()).processServer();
        // Nothing more to test, just checking there is no exception.
    }

}
