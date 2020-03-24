package com.nelsontron.sqlite.obj;

import java.util.List;
import java.util.Objects;

public class Test {
    private int id;
    private String val;
    private List<TestReference> testReferences;
    public Test() {
        id = 0;
        val = null;
        testReferences = null;
    }

    public int getId() {
        return id;
    }
    public String getVal() {
        return val;
    }
    public List<TestReference> getTestReferences() {
        return testReferences;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setVal(String val) {
        this.val = val;
    }
    public void setTestReferences(List<TestReference> testReferences) {
        this.testReferences = testReferences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return id == test.getId() &&
                Objects.equals(val, test.getVal());
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, val);
    }
}
