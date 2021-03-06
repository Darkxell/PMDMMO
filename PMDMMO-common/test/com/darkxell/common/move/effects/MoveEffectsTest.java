package com.darkxell.common.move.effects;

import static com.darkxell.common.testutils.AssertUtils.containsObjectOfClass;
import static com.darkxell.common.testutils.AssertUtils.getObjectOfClass;
import static com.darkxell.common.testutils.TestUtils.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.darkxell.common.dungeon.floor.Tile;
import com.darkxell.common.event.Event;
import com.darkxell.common.event.Event.MessageEvent;
import com.darkxell.common.event.dungeon.DungeonExitEvent;
import com.darkxell.common.event.dungeon.FloorStatusCreatedEvent;
import com.darkxell.common.event.dungeon.TrapDestroyedEvent;
import com.darkxell.common.event.dungeon.weather.WeatherCreatedEvent;
import com.darkxell.common.event.item.ItemCreatedEvent;
import com.darkxell.common.event.item.ItemMovedEvent;
import com.darkxell.common.event.move.MoveSelectionEvent;
import com.darkxell.common.event.move.MoveUseEvent;
import com.darkxell.common.event.pokemon.BlowbackPokemonEvent;
import com.darkxell.common.event.pokemon.DamageDealtEvent;
import com.darkxell.common.event.pokemon.HealthRestoredEvent;
import com.darkxell.common.event.pokemon.PokemonTeleportedEvent;
import com.darkxell.common.event.pokemon.StatusConditionCreatedEvent;
import com.darkxell.common.event.pokemon.StatusConditionEndedEvent;
import com.darkxell.common.event.stats.PPChangedEvent;
import com.darkxell.common.event.stats.StatChangedEvent;
import com.darkxell.common.item.ItemStack;
import com.darkxell.common.model.pokemon.Stat;
import com.darkxell.common.move.Move;
import com.darkxell.common.move.MoveBuilder;
import com.darkxell.common.move.MoveCategory;
import com.darkxell.common.move.MoveRange;
import com.darkxell.common.move.behavior.MoveBehavior;
import com.darkxell.common.move.effect.MoveEffect;
import com.darkxell.common.pokemon.LearnedMove;
import com.darkxell.common.pokemon.PokemonType;
import com.darkxell.common.registry.Registries;
import com.darkxell.common.status.AppliedStatusCondition;
import com.darkxell.common.status.FloorStatuses;
import com.darkxell.common.status.StatusConditions;
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

    private MoveBehavior buildBehavior(MoveEffect... effects) {
        return new MoveBehavior(EID, effects);
    }

    private MoveTestBuilder builder(Move move) {
        MoveTestBuilder test = new MoveTestBuilder(getLeftPokemon(), getRightPokemon()).withMove(move);
        return test;
    }

    @Test
    public void testApplyStatusCondition() {
        Move move = new MoveBuilder().withCategory(MoveCategory.Status).withoutDamage()
                .withBehavior(buildBehavior(new ApplyStatusConditionEffect(StatusConditions.Asleep, 100))).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, StatusConditionCreatedEvent.class));
        StatusConditionCreatedEvent e = getObjectOfClass(events, StatusConditionCreatedEvent.class);
        Assert.assertEquals(StatusConditions.Asleep, e.condition.condition);
        Assert.assertEquals(getRightPokemon(), e.condition.pokemon);
    }

    @Test
    public void testBlowback() {
        Move move = new MoveBuilder().withCategory(MoveCategory.Status).withoutDamage()
                .withBehavior(buildBehavior(new BlowbackEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, BlowbackPokemonEvent.class));
        BlowbackPokemonEvent e = getObjectOfClass(events, BlowbackPokemonEvent.class);
        Assert.assertEquals(getRightPokemon(), e.pokemon);
    }

    @Test
    public void testBreakStatusCondition() {
        getRightPokemon()
                .inflictStatusCondition(StatusConditions.Light_screen.create(getFloor(), getRightPokemon(), null));
        Move move = new MoveBuilder()
                .withBehavior(buildBehavior(new RemoveStatusConditionBeforeDamageEffect(StatusConditions.Light_screen)))
                .build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, StatusConditionEndedEvent.class));
        StatusConditionEndedEvent e = getObjectOfClass(events, StatusConditionEndedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.condition.pokemon);
        Assert.assertEquals(StatusConditions.Light_screen, e.condition.condition);
    }

    @Test
    public void testCannotKO() {
        Move move = new MoveBuilder().withPower(99999).withBehavior(buildBehavior(new CannotKOEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(getRightPokemon().getHp() - 1, e.damage);
    }

    @Test
    public void testCantMiss() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new CantMissEffect())).build();
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
    public void testChangeTypeWithUserId() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new ChangeTypeWithUserId())).build();
        Assert.assertEquals(PokemonType.values()[Math.abs((int) getLeader().id())], move.getType(getLeader()));
    }

    @Test
    public void testCureAilments() {
        AppliedStatusCondition c = new AppliedStatusCondition(StatusConditions.Asleep, getRightPokemon(), null, 10);
        getRightPokemon().inflictStatusCondition(c);
        Move move = new MoveBuilder().withBehavior(buildBehavior(new CureAilmentsEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, StatusConditionEndedEvent.class));
        StatusConditionEndedEvent e = getObjectOfClass(events, StatusConditionEndedEvent.class);
        Assert.assertEquals(c, e.condition);
    }

    @Test
    public void testDamageSelf() {
        Move move = new MoveBuilder().withoutDamage()
                .withBehavior(buildBehavior(new DealHpMultiplierDamageToSelfEffect(.5))).build();
        Registries.moves().register(move);
        ArrayList<Event> events = new MoveSelectionEvent(getFloor(), null, new LearnedMove(move.getID()),
                getLeftPokemon()).processServer();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(getLeftPokemon().getHp() / 2, e.damage);
    }

    @Test
    public void testDestroyTrap() {
        Tile t = getLeftPokemon().tile().adjacentTile(getLeftPokemon().facing());
        t.trap = TrapRegistry.WONDER_TILE;
        Move move = new MoveBuilder().withBehavior(buildBehavior(new DestroyTrapEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, TrapDestroyedEvent.class));
        TrapDestroyedEvent e = getObjectOfClass(events, TrapDestroyedEvent.class);
        Assert.assertEquals(t, e.tile);
    }

    @Test
    public void testDrain() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new DrainEffect(50))).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, HealthRestoredEvent.class));
        HealthRestoredEvent e = getObjectOfClass(events, HealthRestoredEvent.class);
        Assert.assertEquals(getLeftPokemon(), e.target);
    }

    @Test
    public void testDropItem() {
        getRightPokemon().setItem(new ItemStack(1));
        Move move = new MoveBuilder().withBehavior(buildBehavior(new DropItemEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, ItemMovedEvent.class));
        ItemMovedEvent e = getObjectOfClass(events, ItemMovedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.source());
    }

    @Test
    public void testDropMoneyOnKill() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new DropsMoneyOnKillEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, ItemCreatedEvent.class));
        ItemCreatedEvent e = getObjectOfClass(events, ItemCreatedEvent.class);
        Assert.assertFalse(e.isValid());
        getRightPokemon().setHP(0);
        Assert.assertTrue(e.isValid());
    }

    @Test
    public void testEscapeDungeon() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new EscapeDungeonEffect())).build();
        ArrayList<Event> events = new MoveTestBuilder(getLeftPokemon(), getLeftPokemon()).withMove(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DungeonExitEvent.class));
    }

    @Test
    public void testFixedDamage() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new FixedDamageEffect(1337))).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(1337, e.damage);
    }

    @Test
    public void testFloorStatus() {
        Move move = new MoveBuilder()
                .withBehavior(buildBehavior(new CreateFloorStatusEffect(FloorStatuses.Reduce_electric))).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, FloorStatusCreatedEvent.class));
        FloorStatusCreatedEvent e = getObjectOfClass(events, FloorStatusCreatedEvent.class);
        Assert.assertEquals(FloorStatuses.Reduce_electric, e.status.status);
    }

    @Test
    public void testHalfTargetHPDamage() {
        getRightPokemon().setHP(10);
        Move move = new MoveBuilder().withBehavior(buildBehavior(new HalfTargetHPDamageEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(5, e.damage);

        getRightPokemon().setHP(7);
        events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(3, e.damage);
    }

    @Test
    public void testHeal() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new HealEffect(.5))).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, HealthRestoredEvent.class));
        HealthRestoredEvent e = getObjectOfClass(events, HealthRestoredEvent.class);
        Assert.assertEquals(Math.round(getRightPokemon().getMaxHP() * .5), e.health);
    }

    @Test
    public void testHPDifferenceDamage() {
        getLeftPokemon().setHP(5);
        getRightPokemon().setHP(7);
        Move move = new MoveBuilder().withBehavior(buildBehavior(new HPDifferenceDamageEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(2, e.damage);

        getLeftPokemon().setHP(5);
        getRightPokemon().setHP(1);
        events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(0, e.damage);
    }

    @Test
    public void testHPRecoil() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new HPRecoilEffect(10))).build();
        ArrayList<Event> events = this.builder(move).build();

        MoveTestUtils.assertDealtDamage(events, getLeftPokemon());
    }

    @Test
    public void testMaxHpMultiplierDamage() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new DealMaxHpMultiplierDamageEffect(.5))).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(Math.round(getRightPokemon().getMaxHP() * .5), e.damage);
    }

    @Test
    public void testRandomFixedDamage() {
        Move move = new MoveBuilder().withPower(9999).withRange(MoveRange.Floor)
                .withBehavior(buildBehavior(new RandomFixedDamageEffect(5, 6, 7, 8, 9, 10))).build();
        Registries.moves().register(move);
        ArrayList<Event> events = new MoveSelectionEvent(getFloor(), null, new LearnedMove(move.getID()),
                getLeftPokemon()).processServer();

        Assert.assertTrue(containsObjectOfClass(events, MessageEvent.class));
        Assert.assertTrue(containsObjectOfClass(events, MoveUseEvent.class));
        events = getObjectOfClass(events, MoveUseEvent.class).processServer();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertTrue(e.damage >= 5 && e.damage <= 10);
    }

    @Test
    public void testSetPPToZero() {
        getEventProcessor()
                .processEvent(new MoveSelectionEvent(getFloor(), null, getRightPokemon().move(0), getRightPokemon()));
        Move move = new MoveBuilder().withBehavior(buildBehavior(new SetPPtoZeroEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, PPChangedEvent.class));
    }

    @Test
    public void testStatChange() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new StatChangeEffect(Stat.SpecialDefense, 2, 100)))
                .build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, StatChangedEvent.class));
        StatChangedEvent e = getObjectOfClass(events, StatChangedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.target);
        Assert.assertEquals(Stat.SpecialDefense, e.stat);
        Assert.assertEquals(2, e.stage);
    }

    @Test
    public void testStealItem() {
        getRightPokemon().setItem(new ItemStack(1));
        Move move = new MoveBuilder().withBehavior(buildBehavior(new StealItemEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, ItemMovedEvent.class));
        ItemMovedEvent e = getObjectOfClass(events, ItemMovedEvent.class);
        Assert.assertEquals(getRightPokemon(), e.source());
        Assert.assertEquals(getLeftPokemon(), e.destination());
    }

    @Test
    public void testTeleportToOtherRoom() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new TeleportToOtherRoomEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, PokemonTeleportedEvent.class));
    }

    @Test
    public void testUserLevelDamage() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new UserLevelDamageEffect())).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, DamageDealtEvent.class));
        DamageDealtEvent e = getObjectOfClass(events, DamageDealtEvent.class);
        Assert.assertEquals(getLeftPokemon().level(), e.damage);
    }

    @Test
    public void testCopyLastMove() {
        getEventProcessor()
                .processEvent(new MoveSelectionEvent(getFloor(), null, getRightPokemon().move(0), getRightPokemon()));
        Move move = new MoveBuilder().withBehavior(buildBehavior(new CopyLastMoveEffect())).build();
        ArrayList<Event> events = this.builder(move).build();
        
        Assert.assertTrue(containsObjectOfClass(events, MoveSelectionEvent.class));
        MoveSelectionEvent e = getObjectOfClass(events, MoveSelectionEvent.class);
        Assert.assertEquals(getRightPokemon().move(0).moveId(), e.usedMove().move.moveId());
    }

    @Test
    public void testUserStatChange() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new UserStatChangeEffect(Stat.SpecialDefense, 2, 100)))
                .build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, StatChangedEvent.class));
        StatChangedEvent e = getObjectOfClass(events, StatChangedEvent.class);
        Assert.assertEquals(getLeftPokemon(), e.target);
        Assert.assertEquals(Stat.SpecialDefense, e.stat);
        Assert.assertEquals(2, e.stage);
    }

    @Test
    public void testWeatherChange() {
        Move move = new MoveBuilder().withBehavior(buildBehavior(new WeatherChangeEffect(Weather.SUNNY))).build();
        ArrayList<Event> events = this.builder(move).build();

        Assert.assertTrue(containsObjectOfClass(events, WeatherCreatedEvent.class));
        WeatherCreatedEvent e = getObjectOfClass(events, WeatherCreatedEvent.class);
        Assert.assertEquals(Weather.SUNNY, e.weather.weather);
    }

}
