/**
 * The gaphical code of the game.
 * 
 * <p>
 * This package contains all the GUI and user interaction stuff. You should
 * probably read up on libGDX and the scene2d part of libGDX to know how
 * everything works here. The classes are layed out in a hierarchy roughly as
 * follows.
 * 
 * <h4>Main and AtcGame</h4>
 * <p>
 * The {@link seprhou.gui.Main} class controls the game startup and the
 * {@link seprhou.gui.AtcGame} class acts as the top of the game hierarchy.
 * However all this class does is pass on events to be handled by the current
 * screen.
 * 
 * <h4>AbstractScreen</h4>
 * <p>
 * The {@link seprhou.gui.AbstractScreen} class manages components common to
 * each screen, the most important being the libGDX
 * {@link com.badlogic.gdx.scenes.scene2d.Stage}. All the events are passed to
 * the stage and are then distributed to whichever objects asked to receive them
 * (like the individual labels or even the whole game area).
 * 
 * <h4>Screen implementations</h4>
 * <p>
 * The Screen implementations ({@link seprhou.gui.MenuScreen},
 * {@link seprhou.gui.GameScreen}, {@link seprhou.gui.GameOverScreen},
 * {@link seprhou.gui.OptionsScreen} and {@link seprhou.gui.HighScoresScreen})
 * managed their individual screens and the components on them. Most use libGDX
 * scene2d objects to draw labels and buttons on the screen. The only exception
 * is GameArea.
 * 
 * <h4>GameArea</h4>
 * <p>
 * Finally the {@link seprhou.gui.GameArea} class handles and renders the game
 * itself.
 */
package seprhou.gui;

