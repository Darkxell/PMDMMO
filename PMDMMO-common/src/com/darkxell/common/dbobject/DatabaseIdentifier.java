package com.darkxell.common.dbobject;

public class DatabaseIdentifier {

    public long id;

    public DatabaseIdentifier(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DatabaseIdentifier && this.id == ((DatabaseIdentifier) obj).id;
    }

    @Override
    public String toString() {
        return "ID=" + this.id;
    }

}
