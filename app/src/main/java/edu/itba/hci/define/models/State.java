package edu.itba.hci.define.models;


public class State {

    private int stateId;
    private String name;

    public State(int stateId, String name) {

        this.stateId = stateId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateId=" + stateId +
                ", name='" + name + '\'' +
                '}';
    }

    public int getStateId() {
        return stateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (stateId != state.stateId) return false;
        return !(name != null ? !name.equals(state.name) : state.name != null);

    }

    public String getName() {
        return name;
    }
}
