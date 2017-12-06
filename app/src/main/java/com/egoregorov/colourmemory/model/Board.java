package com.egoregorov.colourmemory.model;

import com.egoregorov.colourmemory.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Egor on 04.12.2017.
 */

public class Board {
    private List<Card> mCards;
    private int mCardsLeft;

    public Board() {
        mCards = new ArrayList<>();
        mCards.add(new Card(R.drawable.colour1));
        mCards.add(new Card(R.drawable.colour1));
        mCards.add(new Card(R.drawable.colour2));
        mCards.add(new Card(R.drawable.colour2));
        mCards.add(new Card(R.drawable.colour3));
        mCards.add(new Card(R.drawable.colour3));
        mCards.add(new Card(R.drawable.colour4));
        mCards.add(new Card(R.drawable.colour4));
        mCards.add(new Card(R.drawable.colour5));
        mCards.add(new Card(R.drawable.colour5));
        mCards.add(new Card(R.drawable.colour6));
        mCards.add(new Card(R.drawable.colour6));
        mCards.add(new Card(R.drawable.colour7));
        mCards.add(new Card(R.drawable.colour7));
        mCards.add(new Card(R.drawable.colour8));
        mCards.add(new Card(R.drawable.colour8));
        mCardsLeft = mCards.size();
    }

    public Board shuffle() {
        Collections.shuffle(mCards);
        return this;
    }


    public Card getCard(int position) {
        return mCards.get(position);
    }

    public int getCardPosition(Card card) {
        return mCards.indexOf(card);
    }

    public void removeTwoCards() {
        mCardsLeft -= 2;
    }

    public int cardsLeft() {
        return mCardsLeft;
    }
}
