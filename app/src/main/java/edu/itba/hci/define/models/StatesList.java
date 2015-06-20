package edu.itba.hci.define.models;

import java.util.List;


public class StatesList extends ApiResponse {

    private List<State> states;

    public StatesList(List<State> states) {

        this.states = states;
    }

    @Override
    public String toString() {
        return "StatesList{" +
                "states=" + states +
                '}';
    }

    public List<State> getStates() {
        return states;
    }
}
