package src.main;

import src.sharedRegions.ControlCollectionSite;
import src.sharedRegions.ConcentrationSite;
import src.sharedRegions.Museum;
import src.sharedRegions.AssaultParty;
import src.sharedRegions.GeneralRepos;
import genclass.GenericIO;
import src.entities.mThief;
import src.entities.oThief;

public class Main {
    public static void main(String[] args) {
        try {
            //Object instantiation section and logfile preparation
            AssaultParty[] aParties = new AssaultParty[2];
            GeneralRepos repos = new GeneralRepos("logfile.txt");
            Museum museum = new Museum(5, 30, 15, 16, 8, repos);
            for (int i = 0; i < 2; i++) {
                aParties[i] = new AssaultParty(i, 3, 3, 3, museum, repos);
            }

            ControlCollectionSite controlSite = new ControlCollectionSite(aParties, repos, 5, 6);
            ConcentrationSite concentSite = new ConcentrationSite(aParties, repos);

            mThief master = new mThief(aParties, controlSite, concentSite, 9);
            oThief[] thieves = new oThief[6];

            for (int i = 0; i < 6; i++) {
                thieves[i] = new oThief(i, aParties, controlSite, concentSite, museum, 6, 2);
                repos.setOrdinaryThiefMD(i, thieves[i].getMD());
            }

            //thread start section
            master.start();
            for (int i = 0; i < 6; i++) {
                thieves[i].start();
            }


            //thread join section
            try {
                master.join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("Master has been terminated\n");
            for (int i = 0; i < 6; i++) {
                try {
                    thieves[i].join();
                } catch (InterruptedException e) {
                }
                GenericIO.writelnString("The thief " + (i + 1) + " has terminated.");
            }

        } catch (Exception e) {
        }
    }
}
