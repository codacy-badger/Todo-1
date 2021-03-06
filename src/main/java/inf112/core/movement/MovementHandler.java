package inf112.core.movement;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import inf112.core.board.GameBoard;
import inf112.core.player.Direction;
import inf112.core.player.Player;
import java.util.ArrayList;
import java.util.List;
import static inf112.core.board.MapLayer.*;

/**
 * A basic InputAdapter-implementation that is in control of moving the players logically as well
 * as graphically.
 *
 * @author eskil
 */
public class MovementHandler extends InputAdapter {
    private GameBoard board;
    private List<Player> players;
    private Player activePlayer;                 // movement will affect this player. Should be changed actively
    private TiledMapTileLayer playerLayer;       // layer in which all player cells are placed (for graphics)
    private CollisionHandler collisionHandler;

    public MovementHandler(GameBoard board) {
        this.board = board;
        this.playerLayer = board.getLayer(PLAYER_LAYER);
        this.players = new ArrayList<>();
        this.collisionHandler = new CollisionHandler(board, players);
    }

    public boolean add(Player player) {
        if (contains(player))
            return false;
        return players.add(player);
    }

    public boolean contains(Player player) {
        return players.contains(player);
    }

    public void setActive(Player player) {
        if (!contains(player))
            throw new IllegalArgumentException("Unknown player");
        this.activePlayer = player;
    }

    public void drawPlayers() {
        for (Player player : players)
            playerLayer.setCell(player.getX(), player.getY(), player.getCell());
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                attemptToMoveForward(activePlayer);
                break;
            case Input.Keys.DOWN:
                attemptToMoveBackward(activePlayer);
                break;
            case Input.Keys.LEFT:
                activePlayer.rotateLeft();
                break;
            case Input.Keys.RIGHT:
                activePlayer.rotateRight();
                break;
            case Input.Keys.C:
                activePlayer.setBackupHere();
                break;
            case Input.Keys.SPACE:
                moveToBackup(activePlayer);
                break;
            case Input.Keys.S:
                // TODO switch active player
                // dette er egentlig utenfor oppgaven, da activePlayer settes til eieren av programkortet med høyest verdi
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Moves the given player one unit in its current direction (both logially and graphically).
     * Any other players affected by this movement will also be moved in the same manner.
     *
     *
     * @param playerToBeMoved
     */
    public void attemptToMoveForward(Player playerToBeMoved) {
        attemptToMove(playerToBeMoved, playerToBeMoved.getDirection());
    }

    /**
     * Moves the given player one unit in the direction opposite of its current direction (both logially and graphically).
     * Any other players affected by this movement will also be moved in the same manner.
     *
     *
     * @param playerToBeMoved
     */
    public void attemptToMoveBackward(Player playerToBeMoved) {
        attemptToMove(playerToBeMoved, Direction.invert(playerToBeMoved.getDirection()));
    }

    /**
     * Attempts to move the given player one unit in given direction (both logically and graphically).
     * Any other players affected by this movement will also be moved in the same manner.
     * However, if the movement would result in a player moving through something collidable, it will NOT happen.
     *
     * @param playerToBeMoved
     * @param direction
     */
    public void attemptToMove(Player playerToBeMoved, Direction direction) {
        List<Player> affectedPlayers = new ArrayList<>();
        collisionHandler.gatherAffectedPlayers(playerToBeMoved.getPositionCopy(), direction, affectedPlayers);

        Player last = affectedPlayers.get(0);
        if(collisionHandler.canGo(last.getPositionCopy(), direction))
            for (Player affectedPlayer : affectedPlayers) {
                moveUnchecked(affectedPlayer, direction);
                handleOutOfBounds(affectedPlayer);
            }
    }

    /**
     * Moves the given player one unit in the given direction (both logically and graphically),
     * without any regard to the player's surroundings. Use with care.
     *
     * @param playerToBeMoved
     * @param direction
     */
    private void moveUnchecked(Player playerToBeMoved, Direction direction) {
        playerLayer.setCell(playerToBeMoved.getX(), playerToBeMoved.getY(), null);                   // erase player (graphically)
        playerToBeMoved.move(direction);                                                                  // move player  (logically)
        playerLayer.setCell(playerToBeMoved.getX(), playerToBeMoved.getY(), playerToBeMoved.getCell());   // draw player  (graphically)
    }

    /**
     * Moves the Player to his/hers backup position, both logically and graphically.
     *
     * @param playerToBeMoved
     */
    private void moveToBackup(Player playerToBeMoved) {
        playerLayer.setCell(playerToBeMoved.getX(), playerToBeMoved.getY(), null);
        playerToBeMoved.resetPosition();
        playerLayer.setCell(playerToBeMoved.getX(), playerToBeMoved.getY(), playerToBeMoved.getCell());
    }

    /**
     * Checks if the player is outside the board dimensions, and if so, resets the players
     * position and rotation both logically and graphically.
     *
     * @param playerToBeMoved
     */
    private void handleOutOfBounds(Player playerToBeMoved) {
        if (!board.onBoard(playerToBeMoved)) {
            moveToBackup(playerToBeMoved);
        }
    }
}
