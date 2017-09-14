package fr.goui.dominosfevahh.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fr.goui.dominosfevahh.Constants;

public class DominoModel {
    private static final String TAG = DominoModel.class.getSimpleName();

    private Domino[] dominoes = new Domino[Constants.NUMBER_OF_DOMINOES];

    private Set<Integer> picked = new HashSet<>();

    private Random random = new Random();

    public DominoModel() {
        int count = 0;
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= i; j++) {
                dominoes[count] = new Domino(i, j);
                count++;
            }
        }
    }

    public Domino pickRandom() {
        return dominoes[pickNumber()];
    }

    public List<Domino> pickRandom(int size) {
        if (dominoes.length - picked.size() >= size) {
            List<Domino> ret = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ret.add(dominoes[pickNumber()]);
                Log.i(TAG, i + ": " + ret.get(i));
            }
            return ret;
        } else {
            return null;
        }
    }

    private int pickNumber() {
        int pickedNumber = random.nextInt(Constants.NUMBER_OF_DOMINOES);
        while (picked.contains(pickedNumber)) {
            pickedNumber = random.nextInt(Constants.NUMBER_OF_DOMINOES);
        }
        picked.add(pickedNumber);
        return pickedNumber;
    }

    public int getStackSize() {
        return Constants.NUMBER_OF_DOMINOES - picked.size();
    }
}
