# Dungeon engine

This document describes how the Dungeon engine works.

## Summary

1. Event handler
2. Event listening
3. Turn order
4. Pokémon AI
5. Multiplayer communication
6. Saving in a dungeon
7. Event list

# Event handler

All the behavior in the Dungeons is described with **Events**. These Events are
handled by the **EventProcessor** class. Any new Event is appended to a 
*pendingEvents* Stack (last in, first out list) stored in the EventProcessor. 
Then, after an Event is processed, the EventProcessor will check its *pendingEvents*
and process any pending Events.

All Events have a **floor** attribute as a reference. They also have an **actor**
attribute, which may be *null*. This references a Pokémon and will consume that
Pokémon's turn if not *null*. Events also have a list of Messages that should
be displayed to the player as gameplay messages. Finally, Events have a
*processServer()* method. This method is called to process that Event.
It should apply any direct changes, and add resulting Events to the
*resultingEvents* list. This avoids code redundancy and allows complex
interactions.

# Event listening

Objects from the entire project may implement the *DungeonEventListener* interface.
If so and if implemented properly, these Objects will have a method called after and 
before an Event is processed. This allows for a multitude of complex mechanics. 
For now, only Abilities and Status Conditions implement this Interface, but more 
will be added if necessary. While in the *preProcess()* method, an event may be 
destroyed to prevent it from being processed by using its *consume()* method.

Events can also have **flags**. These should have no impact on the behavior but should
help comunication between other objects that manipulate Events (for example, to avoid
doing the same process multiple times on a single Event).

# Turn order

Due to movement speed affecting how many times a Pokémon may act in a turn,
each turn is divided into **subturns**. There are 4 subturns in each turn 
(equal to the maximum speedboost).

Each Pokémon has an **Actor** object. This object remembers whether the 
Pokémon has acted yet during the current subturn. If it doesn't, it gets
an opportunity if its speed matches it (see table below). If a nonleader 
Pokémon skips its turn, and another Pokémon of the same team acts after, 
that Pokémon gets another chance to act. This allows, for example, team
members to travel in a line properly.

| Speed | Subturn 0 | Subturn 1 | Subturn 2 | Subturn 3 |
| - | ---- | ---- | ---- | ---- |
| 0.5 |    |      |      | Once every 2 turns |
| 0 |      |      |      | Acts |
| 1 |      |      | Acts | Acts |
| 2 |      | Acts | Acts | Acts |
| 3 | Acts | Acts | Acts | Acts |

Actors are sorted by apparition. Whenever a Pokémon spawns, an Actor object is
created for it and placed at the end of the Actor list. Players are an exception
to this as they are placed at the start of the list, so that they play first.

At the end of each turn, each Pokémon also gets an opportunity to rotate. This
allows allies to face back the leader if needed. All events are also stored in a
**GameTurn** object, which is then added to a list stored in Dungeon.

# Pokémon AI

Each Pokémon has an **AI** object. This object is different for Allies, Enemies,
and *null* for leaders. This object has an **AIState** that is set changed whenever
necessary. The AIState then decides what action (Event) the Pokémon will choose.

# Multiplayer communication

Multiplayer is not yet supported in Dungeons. However, implementing it should be 
fairly straightforward.

Since all randomness is computed using a seed shared between all players, only
the events that are directly triggered by user input are necessary to be sent to
other players. We will call those **Player action events** (PAE).

An input counter could be implemented to avoid communication errors: it would be 
transferred with the PAE and checked by the server to avoid late senders, and
then checked by the other clients to avoid late receivers.

# Saving in a dungeon

Saving and quitting in a Dungeon works similarly to multiplayer communication. Since 
randomness uses a seed, only PAE need to be saved. When reloading, these events are 
processed in order and the Player can get back to the game.

However this feature will only be possible for singleplayer explorations, since you can't
force another player to quit.

# Event list

Here is a list of all Events. Will be updated with the game. Bold events are PAE. Italic events are Events that use the Pokémon's turn.

## ■ Dungeon & Floor Events

| Event name              | Description                                                                       | May trigger               |
|-------------------------|-----------------------------------------------------------------------------------|-------------------------- |
| BossDefeatedevent       | Called when a Boss is defeated. EventProcessor then does what is required.        | ExplorationStopEvent      |
| **DungeonExitEvent**    | Exits the Dungeon.                                                                | ExplorationStopEvent      |
| ExplorationStopEvent    | Notifies the game that the Dungeon exploration is over. Stops the Dungeon engine. |                           |
| FloorStatusCreatedEvent | Called when a Floor Status is created.                                            | Calls status start events. |
| FloorStatusEndedEvent   | Called when a Floor Status ends.                                                  | Calls status end events. |
| **NextFloorEvent**      | Changes to the next floor.                                                        | Calls turn start events.  |
| PlayerLosesEvent        | Called when a Player loses this Dungeon exploration.                              | ExplorationStopEvent      |
| TrapDestroyedEvent      | Called when a Trap is destroyed.                                                  | |
| TrapSteppedOnEvent      | Called when a Pokémon steps on a Trap.                                            | Calls the Trap's effects. |
| WeatherDamageEvent      | Called when a weather condition deals damage to Pokémon on the floor.             | DamageDealtEvent          |
| WeatherCleanedEvent     | Called when a weather condition fades away.                                       | WeatherChangedEvent       |
| WeatherCreatedEvent     | Called when a weather condition is created.                                       | WeatherChangedEvent       |
| WeatherChangedEvent     | Called when the main (active) weather condition changes.                          |                           |

## ■ Item related Events

| Event name               | Description                                                                             | May trigger               |
|--------------------------|-----------------------------------------------------------------------------------------|-------------------------- |
| ItemLandedEvent          | Called when an Item falls on the ground.                                                | |
| ***ItemMovedEvent***     | Called when an Item is moved from anywhere to anywhere.                                 | |
| ***ItemSelectionEvent*** | Called when an Item is selected to be used.                                             | ItemUseEvent |
| ***ItemSwappedEvent***   | Called when an Item is swapped with another, from anywhere to anywhere.                 | |
| ***ItemThrownEvent***    | Called when an Item is selected to be thrown.                                           | ProjectileThrownEvent |
| ItemUseEvent             | Called when an Item is used.                                                            | Calls the Item's effects. |
| MoneyCollectedEvent      | Called when money is collected by a Player. May use a turn if intentionnally picked up. | |
| ProjectileThrownEvent    | Called when a Pokémon throws a Projectile to a Tile.                                    | DamageDealtEvent, ItemLandedEvent, ItemUseEvent |

## ■  Move related Events

| Event name               | Description                                                                                       | May trigger |
|--------------------------|---------------------------------------------------------------------------------------------------|--------------------------------------------------------- |
| MoveDiscoveredEvent      | Called when a Pokémon discovers a Move. Will be learned automatically or wait for a Player input. | |
| **MoveEnabledEvent**     | Called when a Player enables or disables a Move.                                                  | |
| MoveLearnedEvent         | Called when a Pokémon learns a new Move.                                                          | |
| ***MoveSelectionEvent*** | Called when a Move is selected to be used.                                                        | BellyChangedEvent, MoveUseEvent |
| **MoveSwitchedEvent**    | Called when a Player switches the position of Moves.                                              | |
| MoveUseEvent             | Called when a Move is used.                                                                       | ExperienceGeneratedEvent. Also calls the Move's effects. |

## ■  Stat related Events

| Event name           | Description                                                             | May trigger               |
|----------------------|-------------------------------------------------------------------------|-------------------------- |
| ExperienceGainedEvent | Called when a Pokémon gains experience. | LevelupEvent |
| ExperienceGeneratedEvent | Called when a Move is used. Is then increased for each fainted Pokémon. | ExperienceGainedEvent |
| LevelupEvent | Called when a Pokémon levels up. | MoveDiscoveredEvent |
| SpeedChangedEvent | Called when a Pokémon has its effective speed changed. | |
| StatChangedEvent | Called when a Pokémon has one of its stat stages changed. | SpeedChangeEvent |

## ■  Action related Events

| Event name               | Description                                         | May trigger               |
|--------------------------|-----------------------------------------------------|-------------------------- |
| PokemonRescuedEvent      | Called when a Pokémon is rescued.                   | MissionClearedEvent |
| PokemonRotateEvent       | Called when a Pokémon changes its facing direction. | |
| PokemonSpawnedEvent      | Called when a Pokémon spawns.                       | |
| ***PokemonTravelEvent*** | Called when a Pokémon travels.                      | BellyChangedEvent. Also calls the destination tile for events. |
| ***TurnSkippedEvent***   | Called when a Pokémon skips their turn.             | BellyChangedEvent |

## ■  Pokemon related Events

| Event name                  | Description                                | May trigger               |
|-----------------------------|--------------------------------------------|-------------------------- |
| BlowbackPokemonEvent        | Called when a Pokémon is blown back.       | DamageDealtEvent |
| DamageDealtEvent            | Called when a Pokémon takes damage.        | FaintedPokemonEvent       |
| FaintedPokemonEvent         | Called when a Pokémon faints.              | BossDefeatedEvent         |
| HealthRestoredEvent         | Called when a Pokémon's HP is healed.      | |
| PokemonTeleportedEvent      | Called when a Pokémon is teleported.       | |
| SwitchedPokemonEvent        | Called when two Pokémon are switched.      | |
| IncreasedIQEvent            | Called when a Pokémon's IQ is raised.      | |
| StatusConditionCreatedEvent | Called when a status condition is created. | |
| StatusConditionEndedEvent   | Called when a status condition fades away. | |
| TriggeredAbilityEvent       | Called when a Pokémon's ability triggers.  | |

## ■ Client-side Events

These events should only trigger other Client-side events and not change any server-side data. Else it will cause 
desynchronization.

| Event name          | Description                                                     | May trigger   |
|---------------------|-----------------------------------------------------------------|-------------- |
| PokemonTravelsEvent | Multiple Pokémon travel at the same time. Used for animations.  |               |
| StairLandingEvent   | Allows automatic display of the NextFloor menu                  |               |



