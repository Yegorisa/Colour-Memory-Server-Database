package com.egoregorov.colourmemory.test;

import com.egoregorov.colourmemory.R;
import com.egoregorov.colourmemory.model.Board;
import com.egoregorov.colourmemory.model.Card;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Egor on 06.12.2017.
 */
public class BoardTest {
    @Test
    public void boardWithNoShuffle() throws Exception {
        Board board = new Board();
        assertEquals(new Card(R.drawable.colour1).getImageResourceId(), board.getCard(0).getImageResourceId());
        assertEquals(new Card(R.drawable.colour3).getImageResourceId(), board.getCard(5).getImageResourceId());
        assertEquals(new Card(R.drawable.colour8).getImageResourceId(), board.getCard(14).getImageResourceId());
    }

    @Test
    public void boardWithShuffle() throws Exception {
        Board board = new Board().shuffle();
        assertNotEquals(board.getCard(0).getImageResourceId(), board.getCard(1).getImageResourceId());
        assertNotEquals(board.getCard(2).getImageResourceId(), board.getCard(3).getImageResourceId());
        assertNotEquals(board.getCard(4).getImageResourceId(), board.getCard(5).getImageResourceId());
//        assertEquals(new Card(R.drawable.colour3), board.getCard(5));
//        assertEquals(new Card(R.drawable.colour8), board.getCard(14));
    }

    @Test
    public void getCardsLeft() throws Exception {
        Board board = new Board();
        assertEquals(16, board.cardsLeft());
        board.removeTwoCards();
        assertEquals(14, board.cardsLeft());
    }

}