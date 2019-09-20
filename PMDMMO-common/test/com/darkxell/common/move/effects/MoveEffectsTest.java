package com.darkxell.common.move.effects;

import static com.darkxell.common.testutils.TestUtils.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.FloorStatusCreatedEvent;
import com.darkxell.common.event.dungeon.TrapDestroyedEvent;
import com.darkxell.common.event.dungeon.weather.WeatherCreatedEvent;
import com.darkxell.common.event.item.ItemCreatedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.PokemonTeleportedEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.stats.PPChangedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.Move.MoveCategory;
import com.darkxell.common.move.MoveBuilder;
import com.darkxell.common.pokemon.BaseStats.Stat;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.FloorStatuses;
import com.darkxell.common.status.StatusConditions;
import com.darkxell.common.testutils.AssertUtils;
import com.darkxell.common.testutils.move.MoveTestBuilder;
import com.darkxell.common.testutils.move.MoveTestUtils;
import com.darkxell.common.trap.TrapRegistry;
import com.darkxell.common.weather.Weather;

public class MoveEffectsTest {

    public static final int EID = 10000;

    @Before
    public void before() {
        generateALL();
    }

    private MoveTestBuilder builder(Move move) {
        MoveTestBuilder test = new MoveTestBuilder(getLeftPokemon(), getRightPokemon()).withMove(move);
        return test;
    }

    @Test
    public void testApplyStatusCondition() {
        Move move = new MoveBuilder().withCategory(MoveCategory.Status).withoutDamage()
                .withEffect(new ApplyStatusConditionEffect(EID, StatusConditions.Asleep, 100)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, StatusConditionCreatedEvent.class));
        StatusConditionCreatedEvent e = AssertUtils.getObjectOfClass(events, StatusConditionCreatedEvent.class);
        Assert.assertEquals(StatusConditions.Asleep, e.condition.condition);
        Assert.assertEquals(getRightPokemon(), e.condition.pokemon);
    }

    @Test
    public void testBlowback() {
        Move move = new MoveBuilder().withCategory(MoveCategory.Status).withoutDamage()
                .withEffect(new BlowbackEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, BlowbackPokemonEvent.class));
        BlowbackPokemonEvent e = AssertUtils.getObjectOfClass(events, BlowbackPokemonEvent.class);
        Assert.assertEquals(getRightPokemon(), e.pokemon);
    }

    @Test
    public void testCannotKO() {
        Move move = new MoveBuilder().withPower(99999).withEffect(new CannotKOEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(getRightPokemon().getHp() - 1, e.damage);
    }

    @Test
    public void testCantMiss() {
        Move move = new MoveBuilder().withEffect(new CantMissEffect(EID)).build();
        getLeftPokemon().stats.addStage(Stat.Accuracy, -10);
        boolean missed = false;
        for (int i = 0; i < 20; ++i) { // 20 hits with Accuracy -10 should be very unlikely... right?
            MoveTestBuilder builder = this.builder(move);
            builder.build();
            if (builder.missed()) { // We should never go there since we can't miss!
                missed = true;
                break;
            }
        }
        Assert.assertFalse(missed);
    }

    @Test
    public void testCureAilments() {
        AppliedStatusCondition c = new AppliedStatusCondition(StatusConditions.Asleep, getRightPokemon(), null, 10);
        getRightPokemon().inflictStatusCondition(c);
        Move move = new MoveBuilder().withEffect(new CureAilmentsEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, StatusConditionEndedEvent.class));
        StatusConditionEndedEvent e = AssertUtils.getObjectOfClass(events, StatusConditionEndedEvent.class);
        Assert.assertEquals(c, e.condition);
    }

    @Test
    public void testDestroyTrap() {
        Tile t = getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing());
        t.trap = TrapRegistry.WONDER_TILE;
        Move move = new MoveBuilder().withEffect(new DestroyTrapEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, TrapDestroyedEvent.class));
        TrapDestroyedEvent e = AssertUtils.getObjectOfClass(events, TrapDestroyedEvent.class);
        Assert.assertEquals(t, e.tile);
    }

    @Test
    public void testDrain() {
        Move move = new MoveBuilder().withEffect(new DrainEffect(EID, 50)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, HealthRestoredEvent.class));
        HealthRestoredEvent e = AssertUtils.getObjectOfClass(events, HealthRestoredEvent.class);
        Assert.assertEquals(getLeftPokemon(), e.target);
    }

    @Test
    public void testDropItem() {
        getRightPokemon().setItem(new ItemStack(1));
        Move move = new MoveBuilder().withEffect(new DropItemEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, ItemMovedEvent.class));
        ItemMovedEvent e = AssertUtils.getObjectOfClass(events, ItemMovedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.source());
    }

    @Test
    public void testDropMoneyOnKill() {
        Move move = new MoveBuilder().withEffect(new DropsMoneyOnKillEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, ItemCreatedEvent.class));
        ItemCreatedEvent e = AssertUtils.getObjectOfClass(events, ItemCreatedEvent.class);
        Assert.assertFalse(e.isValid());
        getRightPokemon().setHP(0);
        Assert.assertTrue(e.isValid());
    }

    @Test
    public void testEscapeDungeon() {
        Move move = new MoveBuilder().withEffect(new EscapeDungeonEffect(EID)).build();
        ArrayList<Event> events = new MoveTestBuilder(getLeftPokemon(), getLeftPokemon()).withMove(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DungeonExitEvent.class));
    }

    @Test
    public void testFixedDamage() {
        Move move = new MoveBuilder().withEffect(new FixedDamageEffect(EID, 1337)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(1337, e.damage);
    }

    @Test
    public void testFloorStatus() {
        Move move = new MoveBuilder().withEffect(new CreateFloorStatusEffect(EID, FloorStatuses.Reduce_electric))
                .build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, FloorStatusCreatedEvent.class));
        FloorStatusCreatedEvent e = AssertUtils.getObjectOfClass(events, FloorStatusCreatedEvent.class);
        Assert.assertEquals(FloorStatuses.Reduce_electric, e.status.status);
    }

    @Test
    public void testHPDifferenceDamage() {
        getLeftPokemon().setHP(5);
        getRightPokemon().setHP(7);
        Move move = new MoveBuilder().withEffect(new HPDifferenceDamageEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(2, e.damage);

        getLeftPokemon().setHP(5);
        getRightPokemon().setHP(1);
        events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        e = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(0, e.damage);
    }

    @Test
    public void testHPRecoil() {
        Move move = new MoveBuilder().withEffect(new HPRecoilEffect(EID, 10)).build();
        ArrayList<Event> events = this.builder(move).build();

        MoveTestUtils.assertDealtDamage(events, getLeftPokemon());
    }

    @Test
    public void testSetPPToZero() {
        getEventProcessor()
                .processEvent(new MoveSelectionEvent(getFloor(), null, getRightPokemon().move(0), getRightPokemon()));
        Move move = new MoveBuilder().withEffect(new SetPPtoZeroEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, PPChangedEvent.class));
    }

    @Test
    public void testStatChange() {
        Move move = new MoveBuilder().withEffect(new StatChangeEffect(EID, Stat.SpecialDefense, 2, 100)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, StatChangedEvent.class));
        StatChangedEvent e = AssertUtils.getObjectOfClass(events, StatChangedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.target);
        Assert.assertEquals(Stat.SpecialDefense, e.stat);
        Assert.assertEquals(2, e.stage);
    }

    @Test
    public void testStealItem() {
        getRightPokemon().setItem(new ItemStack(1));
        Move move = new MoveBuilder().withEffect(new StealItemEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, ItemMovedEvent.class));
        ItemMovedEvent e = AssertUtils.getObjectOfClass(events, ItemMovedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.source());
        Assert.assertEquals(getLeftPokemon(), e.destination());
    }

    @Test
    public void testTeleportToOtherRoom() {
        Move move = new MoveBuilder().withEffect(new TeleportToOtherRoomEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, PokemonTeleportedEvent.class));
    }

    @Test
    public void testUserLevelDamage() {
        Move move = new MoveBuilder().withEffect(new UserLevelDamageEffect(EID)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = AssertUtils.getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(getLeftPokemon().level(), e.damage);
    }

    @Test
    public void testUserStatChange() {
        Move move = new MoveBuilder().withEffect(new UserStatChangeEffect(EID, Stat.SpecialDefense, 2, 100)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, StatChangedEvent.class));
        StatChangedEvent e = AssertUtils.getObjectOfClass(events, StatChangedEvent.class);
        Assert.assertEquals(getLeftPokemon(), e.target);
        Assert.assertEquals(Stat.SpecialDefense, e.stat);
        Assert.assertEquals(2, e.stage);
    }

    @Test
    public void testWeatherChange() {
        Move move = new MoveBuilder().withEffect(new WeatherChangeEffect(EID, Weather.SUNNY)).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(AssertUtils.containsObjectOfClass(events, WeatherCreatedEvent.class));
        WeatherCreatedEvent e = AssertUtils.getObjectOfClass(events, WeatherCreatedEvent.class);
        Assert.assertEquals(Weather.SUNNY, e.weather.weather);
    }

}
