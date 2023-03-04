package entities;
public class mThief extends Thread {

    protected int state;

    public void changeCurState(int n)
    {
        this.state = n;
    }

    public int getCurState()
    {
        return state;
    }

    mThief()
    {
        this.state = 0;
    }

    @Override
    public void run()
    {

    }
}
