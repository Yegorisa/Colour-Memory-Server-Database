package com.egoregorov.colourmemory.test;

import android.content.Context;

import com.egoregorov.colourmemory.model.Card;
import com.egoregorov.colourmemory.presenter.BoardEngine;
import com.egoregorov.colourmemory.view.IBoardView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;

/**
 * Created by Egor on 06.12.2017.
 */
public class BoardEngineTest {

    @Mock
    IBoardView boardView;

    @Mock
    Context activity;

    private BoardEngine board;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        board = new BoardEngine(boardView, activity);
        board.onCreate();
    }

    @After
    public void destroy() {
        board = null;
    }

    @Test
    public void selectCardCheckSelected() throws Exception {
        Card card = board.onCardSelected(0);
        assertTrue(card.isSelected());
    }

    @Test
    public void selectCardAndDontSelectSecondTime() throws Exception {
        board.onCardSelected(0);
        assertNull(board.onCardSelected(0));
    }

}