package com.xten.cluster.common.lifecycle;


/**
 * Description:
 * User: kongqingyu
 * Date: 2018/1/9
 */
public class Lifecycle {

    private static State state = State.INITIALIZED;

    public enum  State {

        INITIALIZED(0),STARTING(1),RUNNING(2),STOPPING(3),STOPPED(4),CLOSED(5);

        private final int code;

        State(int code){
            this.code = code;
        }

        public boolean equal(int code){
            return this.code == code;
        }

        public int getCode(){
            return this.code;
        }
    }

    public State state() {
        return this.state;
    }

    public boolean canMoveToStart(){
        State localState = this.state();
        if (localState == State.INITIALIZED || localState == State.STOPPED) {
            return true;
        }
        if (localState == State.STARTING || localState == State.RUNNING || localState == State.STOPPING){
            return false;
        }
        if (localState == State.CLOSED) {
            throw new IllegalStateException("Can't move to started state when closed");
        }
        throw new IllegalStateException("Can't move to started with unknown state");

    }

    public boolean moveToStarted() throws IllegalStateException {
        State localState = this.state();
        if (localState == State.INITIALIZED || localState == State.STOPPED) {
            state = State.STARTING;
            return true;
        }
        if (localState == State.RUNNING) {
            return false;
        }
        if (localState == State.CLOSED) {
            throw new IllegalStateException("Can't move to started state when closed");
        }
        throw new IllegalStateException("Can't move to started with unknown state");
    }

    public boolean started() throws IllegalStateException {
        State localState = this.state();
        if (localState == State.STARTING ) {
            state = State.RUNNING;
            return true;
        }else {
            return false;
        }
    }


}
