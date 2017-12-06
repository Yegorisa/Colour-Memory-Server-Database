package com.egoregorov.colourmemory.test;

import android.content.Context;

import com.egoregorov.colourmemory.model.Card;
import com.egoregorov.colourmemory.presenter.BoardPresenter;
import com.egoregorov.colourmemory.view.IBoardView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * Created by Egor on 06.12.2017.
 */
public class BoardPresenterTest {

    @Mock
    IBoardView boardView;

    @Mock
    Context activity;

    private BoardPresenter board;

    @Before
    public void init() {
        board = new BoardPresenter(boardView, activity);

    }

    @After
    public void destroy() {
        board = null;
    }

    @Test
    public void selectCardCheckSelected() throws Exception {
        Card card = board.onCardSelected(0);
//        assertEquals(new Card(R.drawable.colour1), card);
        assertTrue(card.isSelected());
    }

    @Test
    public void selectCardAndDontSelectSecondTime() throws Exception {
        board.onCardSelected(0);
        assertNull(board.onCardSelected(0));
    }

}