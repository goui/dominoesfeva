package fr.goui.dominosfevahh.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fr.goui.dominosfevahh.Constants;

public class DominoModel {
    private static final String TAG = DominoModel.class.getSimpleName();

    private Domino[] dominoes = new Domino[Constants.NUMBER_OF_DOMINOES];

    private List<Domino> stack = new ArrayList<>();

    private Set<Integer> picked = new HashSet<>();

    private Random random = new Random();

    public DominoModel() {
        int count = 0;
        for (int i = 0; i <= 6; i++) {
            for (int j = 0; j <= i; j++) {
                dominoes[count] = new Domino(i, j);
                stack.add(dominoes[count]);
                count++;
            }
        }
    }

    public Domino pickRandom() {
        Domino domino = dominoes[pickNumber()];
        stack.remove(domino);
        return domino;
    }

    public List<Domino> pickRandom(int size) {
        if (dominoes.length - picked.size() >= size) {
            List<Domino> ret = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Domino domino = dominoes[pickNumber()];
                ret.add(domino);
                stack.remove(domino);
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

    public boolean isDoubleInStack(int doubleNumber) {
        boolean ret = false;
        for (Domino domino : stack) {
            if (domino.getFirst() == doubleNumber && domino.getSecond() == doubleNumber) {
                ret = true;
            }
        }
        return ret;
    }
}
