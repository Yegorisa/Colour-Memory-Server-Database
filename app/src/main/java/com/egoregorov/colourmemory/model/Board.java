package com.egoregorov.colourmemory.model;

import com.egoregorov.colourmemory.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Egor on 04.12.2017.
 */

public class Board {
    private List<Card> mCardArrayList;
    private int mCardsLeft;

    public Board() {
        mCardArrayList = new LinkedList<>();
        mCardArrayList.add(new Card(R.drawable.colour1));
        mCardArrayList.add(new Card(R.drawable.colour1));
        mCardArrayList.add(new Card(R.drawable.colour2));
        mCardArrayList.add(new Card(R.drawable.colour2));
        mCardArrayList.add(new Card(R.drawable.colour3));
        mCardArrayList.add(new Card(R.drawable.colour3));
        mCardArrayList.add(new Card(R.drawable.colour4));
        mCardArrayList.add(new Card(R.drawable.colour4));
        mCardArrayList.add(new Card(R.drawable.colour5));
        mCardArrayList.add(new Card(R.drawable.colour5));
        mCardArrayList.add(new Card(R.drawable.colour6));
        mCardArrayList.add(new Card(R.drawable.colour6));
        mCardArrayList.add(new Card(R.drawable.colour7));
        mCardArrayList.add(new Card(R.drawable.colour7));
        mCardArrayList.add(new Card(R.drawable.colour8));
        mCardArrayList.add(new Card(R.drawable.colour8));
        Collections.shuffle(mCardArrayList);
        mCardsLeft = 16;
    }


    public Card getCard(int position) {
        return mCardArrayList.get(position);
    }

    public int getCardPosition(Card card) {
        return mCardArrayList.indexOf(card);
    }

    public void minusTwoCards() {
        mCardsLeft = mCardsLeft - 2;
    }

    public int getCardsLeft() {
        return mCardsLeft;
    }
}
