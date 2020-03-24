package com.nelsontron.warps.provider;

import com.nelsontron.core.data.StandardProvider;
import com.nelsontron.warps.entity.SqliteContext;
import com.nelsontron.warps.entity.Warp;

import java.util.List;

public class GlobalProvider extends StandardProvider<Warp> {
    private static final String table = "warps";

    public GlobalProvider(SqliteContext context) {
        super(context, table);
    }

    // getters
    public Warp getWarp(String name) { return super.get("name", name); }

    // methods
    public List<Warp> update() {
        return getContext().selectAll("SELECT * FROM " + table + " WHERE category = 'global';", Warp.class);
    }
    @Override
    public void init(Object o) {}
    @Override
    public void saveAll() {
        // remove
        for (Warp w : update()) {
            if (!getList().contains(w)) {
                delete(w);
            }
        }
        // update
        for (Warp w : getList()) {
            save(w);
        }
    }
    @Override
    public void loadAll() {
        getList().addAll(update());
    }
}
