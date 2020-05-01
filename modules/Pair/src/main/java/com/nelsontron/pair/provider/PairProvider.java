package com.nelsontron.pair.provider;

import com.nelsontron.core.data.StandardProvider;
import com.nelsontron.pair.entity.Pair;
import com.nelsontron.pair.entity.SqliteContext;

import java.util.ArrayList;
import java.util.List;


public class PairProvider extends StandardProvider<Pair> {

    public static final String table = "pairs";

    public PairProvider(SqliteContext context) {
        super(context, table);
    }

    // getters
    public Pair getPair(String uuid) {
        Pair result = get("player1", uuid);
        if (result == null)
            result = get("player2", uuid);
        return result;
    }
    public List<Pair> getPlaying() {
        List<Pair> playing = new ArrayList<>();

        for (Pair p : getList()) {
            if (p.isPlaying()) {
                playing.add(p);
            }
        }
        return playing;
    }

    // methods
    public List<Pair> update() {
        return getContext().selectAll("SELECT * FROM " + table, Pair.class);
    }
    @Override
    public void saveAll() {
        // remove
        for (Pair p : update()) {
            if (!getList().contains(p)) {
                delete(p);
            }
        }
        // update
        for (Pair p : getList()) {
            save(p);
        }
    }
    @Override
    public void loadAll() {
        getList().addAll(update());
    }
}
