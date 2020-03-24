package com.nelsontron.sqlite.obj;

import java.util.Objects;

public class TestReference {
    private int id;
    private int testId;
    public TestReference() {
        id = 0;
        testId = 0;
    }

    public int getId() {
        return id;
    }
    public int getTestId() {
        return testId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTestId(int testId) {
        this.testId = testId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestReference that = (TestReference) o;
        return id == that.id &&
                testId == that.testId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testId);
    }
}
