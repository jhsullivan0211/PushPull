package com.jhsullivan.pushpull.game_logic;






import com.jhsullivan.pushpull.user_interface.DrawingHelper;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DrawingHelperTest {


    /**
     * Tests the mergePaths function on a simple set, one rectangle of points against a square
     * adjacent to it.  Expected result:  the resulting path (i.e. list of vectors in order)
     * circumscribes the resulting L-shape.
     */
    @Test
    public void testMergePathsSimple() {
        List<Vector2D> major = new ArrayList<>();
        List<Vector2D> absorbed = new ArrayList<>();

        major.add(new Vector2D(2, 3));
        major.add(new Vector2D(3, 3));
        major.add(new Vector2D(3, 4));
        major.add(new Vector2D(3, 5));
        major.add(new Vector2D(3, 6));
        major.add(new Vector2D(2, 6));
        major.add(new Vector2D(2, 5));
        major.add(new Vector2D(2, 4));

        absorbed.add(new Vector2D(3, 3));
        absorbed.add(new Vector2D(4, 3));
        absorbed.add(new Vector2D(4, 4));
        absorbed.add(new Vector2D(3, 4));

        List<Vector2D> intersections = new ArrayList<>();
        intersections.add(new Vector2D(3, 3));
        intersections.add(new Vector2D(3, 4));

        DrawingHelper.mergePaths(major, absorbed, intersections);

        List<Vector2D> expected = new ArrayList<>();
        expected.add(new Vector2D(2, 3));
        expected.add(new Vector2D(3, 3));
        expected.add(new Vector2D(4, 3));
        expected.add(new Vector2D(4, 4));
        expected.add(new Vector2D(3, 4));
        expected.add(new Vector2D(3, 5));
        expected.add(new Vector2D(3, 6));
        expected.add(new Vector2D(2, 6));
        expected.add(new Vector2D(2, 5));
        expected.add(new Vector2D(2, 4));

        assertTrue(expected.equals(major));
    }

    /**
     * Tests the mergePaths function's more complicated requirement, the exclusion of "kinks", which
     * form when merging such that a concave area becomes convex.  Tests this by attempting to merge
     * and L-shape with a block that would turn the result into one large square.  Expected result:
     * the resulting path (i.e. list of Vector2Ds) should form a square.
     */
    @Test
    public void testMergePathsMedium() {
        List<Vector2D> major = new ArrayList<>();
        List<Vector2D> absorbed = new ArrayList<>();

        major.add(new Vector2D(2, 3));
        major.add(new Vector2D(3, 3));
        major.add(new Vector2D(4, 3));
        major.add(new Vector2D(4, 4));
        major.add(new Vector2D(3, 4));
        major.add(new Vector2D(3, 5));
        major.add(new Vector2D(2, 5));
        major.add(new Vector2D(2, 4));

        absorbed.add(new Vector2D(3, 4));
        absorbed.add(new Vector2D(4, 4));
        absorbed.add(new Vector2D(4, 5));
        absorbed.add(new Vector2D(3, 5));

        List<Vector2D> intersections = new ArrayList<>();
        intersections.add(new Vector2D(4, 4));
        intersections.add(new Vector2D(3, 4));

        DrawingHelper.mergePaths(major, absorbed, intersections);

        List<Vector2D> expected = new ArrayList<>();
        expected.add(new Vector2D(2, 3));
        expected.add(new Vector2D(3, 3));
        expected.add(new Vector2D(4, 3));
        expected.add(new Vector2D(4, 4));
        expected.add(new Vector2D(4, 5));
        expected.add(new Vector2D(3, 5));
        expected.add(new Vector2D(2, 5));
        expected.add(new Vector2D(2, 4));

        assertTrue(expected.equals(major));

    }

    /**
     * Tests the mergePaths function on a complex shape, by adding squares one at a time to form
     * a big shape, and comparing the result to the expected outer shape.
     */
    @Test
    public void testMergePathsComplex() {

        List<Vector2D> bases = new ArrayList<>();
        bases.add(new Vector2D(0, 0));
        bases.add(new Vector2D(1, 0));
        bases.add(new Vector2D(2, 0));
        bases.add(new Vector2D(3, 0));
        bases.add(new Vector2D(3, 1));
        bases.add(new Vector2D(3, 2));
        bases.add(new Vector2D(4, 2));
        bases.add(new Vector2D(5, 2));
        bases.add(new Vector2D(3, 3));
        bases.add(new Vector2D(0, 1));

        List<Vector2D> major = DrawingHelper.expandOrigin(bases.get(0));

        for (int i = 1; i < bases.size(); i++) {
            List<Vector2D> current = DrawingHelper.expandOrigin(bases.get(i));
            List<Vector2D> intersections = DrawingHelper.getIntersections(major, current);
            DrawingHelper.mergePaths(major, current, intersections);
        }


        List<Vector2D> expected = new ArrayList<>();
        expected.add(new Vector2D(0, 0));
        expected.add(new Vector2D(1, 0));
        expected.add(new Vector2D(2, 0));
        expected.add(new Vector2D(3, 0));
        expected.add(new Vector2D(4, 0));
        expected.add(new Vector2D(4, 1));
        expected.add(new Vector2D(4, 2));
        expected.add(new Vector2D(5, 2));
        expected.add(new Vector2D(6, 2));
        expected.add(new Vector2D(6, 3));
        expected.add(new Vector2D(5, 3));
        expected.add(new Vector2D(4, 3));
        expected.add(new Vector2D(4, 4));
        expected.add(new Vector2D(3, 4));
        expected.add(new Vector2D(3, 3));
        expected.add(new Vector2D(3, 2));
        expected.add(new Vector2D(3, 1));
        expected.add(new Vector2D(2, 1));
        expected.add(new Vector2D(1, 1));
        expected.add(new Vector2D(1, 2));
        expected.add(new Vector2D(0, 2));
        expected.add(new Vector2D(0, 1));

        assertTrue(major.equals(expected));

    }

    /**
     * Tests the getPath function, using the same set of corner points used in the complex
     * test above.
     */
    @Test
    public void testGetPath() {
        List<Vector2D> bases = new ArrayList<>();
        bases.add(new Vector2D(0, 0));
        bases.add(new Vector2D(1, 0));
        bases.add(new Vector2D(2, 0));
        bases.add(new Vector2D(3, 0));
        bases.add(new Vector2D(3, 1));
        bases.add(new Vector2D(3, 2));
        bases.add(new Vector2D(4, 2));
        bases.add(new Vector2D(5, 2));
        bases.add(new Vector2D(3, 3));
        bases.add(new Vector2D(0, 1));

        List<Vector2D> path = DrawingHelper.getPath(bases);

        List<Vector2D> expected = new ArrayList<>();
        expected.add(new Vector2D(0, 0));
        expected.add(new Vector2D(1, 0));
        expected.add(new Vector2D(2, 0));
        expected.add(new Vector2D(3, 0));
        expected.add(new Vector2D(4, 0));
        expected.add(new Vector2D(4, 1));
        expected.add(new Vector2D(4, 2));
        expected.add(new Vector2D(5, 2));
        expected.add(new Vector2D(6, 2));
        expected.add(new Vector2D(6, 3));
        expected.add(new Vector2D(5, 3));
        expected.add(new Vector2D(4, 3));
        expected.add(new Vector2D(4, 4));
        expected.add(new Vector2D(3, 4));
        expected.add(new Vector2D(3, 3));
        expected.add(new Vector2D(3, 2));
        expected.add(new Vector2D(3, 1));
        expected.add(new Vector2D(2, 1));
        expected.add(new Vector2D(1, 1));
        expected.add(new Vector2D(1, 2));
        expected.add(new Vector2D(0, 2));
        expected.add(new Vector2D(0, 1));

        assertTrue(path.equals(expected));


    }

}
