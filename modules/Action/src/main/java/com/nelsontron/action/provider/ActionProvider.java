package com.nelsontron.action.provider;

import com.nelsontron.action.SqliteContext;
import com.nelsontron.action.entity.Action;
import com.nelsontron.action.util.LocationUtil;
import com.nelsontron.core.data.StandardProvider;
import org.bukkit.Location;

import java.util.List;

public class ActionProvider extends StandardProvider<Action> {
    private static final String table = "warps";
    public ActionProvider(SqliteContext context) {
        super(context, table);
    }

    // getters
    public Action getAction(Location location) { return super.get("location", LocationUtil.locationToString(location)); }

    // methods
    public List<Action> update() {
        return getContext().selectAll("SELECT * FROM " + table + " WHERE category = 'global';", Action.class);
    }
    @Override
    public void init(Object o) {}
    @Override
    public void saveAll() {
        // remove
        for (Action w : update()) {
            if (!getList().contains(w)) {
                getContext().delete(table, w);
            }
        }
        // update
        for (Action w : getList()) {
            getContext().updateOrInsert(table, w);
        }
    }
    @Override
    public void loadAll() {
        getList().addAll(update());
    }
}
