package inf112.core.map;

import inf112.core.tile.Attributes;
import inf112.core.tile.TileId;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TileIdTest {

    @Test
    public void getTileIdReturnsCorrectTileId() {
        assertEquals(TileId.WALL_SOUTH, TileId.getTileId(29));
        assertEquals(TileId.WALL_NORTH_EAST, TileId.getTileId(16));
    }

    @Test
    public void WALL_SOUTHShouldHaveAttributesSOUTH(){
        Attributes attributes = TileId.WALL_SOUTH.getAttributes().get(0);
        assertEquals(attributes, Attributes.SOUTH);
    }

    @Test
    public void WALL_NORTHShouldHaveAttributesNORTH(){
        Attributes attributes = TileId.WALL_NORTH.getAttributes().get(0);
        assertEquals(attributes, Attributes.NORTH);
    }

    @Test
    public void WALL_EASTShouldHaveAttributesEAST(){
        Attributes attributes = TileId.WALL_EAST.getAttributes().get(0);
        assertEquals(attributes, Attributes.EAST);
    }

    @Test
    public void WALL_WESTShouldHaveAttributesWEST(){
        Attributes attributes = TileId.WALL_WEST.getAttributes().get(0);
        assertEquals(attributes, Attributes.WEST);
    }

    @Test
    public void WALL_SOUTH_WESTShouldHaveAttributesSOUTHAndWEST() {
        List<Attributes> attributes = TileId.WALL_SOUTH_WEST.getAttributes();
        for(Attributes attribute : attributes)
            assert(attribute.equals(Attributes.WEST) || attribute.equals(Attributes.SOUTH));
    }

    @Test
    public void WALL_NORTH_WESTShouldHaveAttributesNORTHAndWEST() {
        List<Attributes> attributes = TileId.WALL_NORTH_WEST.getAttributes();
        for(Attributes attribute : attributes)
            assert(attribute.equals(Attributes.WEST) || attribute.equals(Attributes.NORTH));
    }

    @Test
    public void WALL_NORTH_EASTShouldHaveAttributesNORTHAndEAST() {
        List<Attributes> attributes = TileId.WALL_NORTH_EAST.getAttributes();
        for(Attributes attribute : attributes)
            assert(attribute.equals(Attributes.EAST) || attribute.equals(Attributes.NORTH));
    }

    @Test
    public void WALL_SOUTH_EASTShouldHaveAttributesSOUTHAndEAST() {
        List<Attributes> attributes = TileId.WALL_SOUTH_EAST.getAttributes();
        for(Attributes attribute : attributes)
            assert(attribute.equals(Attributes.EAST) || attribute.equals(Attributes.SOUTH));
    }
}
