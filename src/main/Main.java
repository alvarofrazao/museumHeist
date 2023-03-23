package src.main;


import src.sharedRegions.ControlCollectionSite;
import src.sharedRegions.ConcentrationSite;
import src.sharedRegions.Museum;
import src.sharedRegions.AssaultParty;
import src.sharedRegions.GeneralRepos;
import src.entities.mThief;
import src.entities.oThief;

public class Main {
    public static void main(String[] args)
    {
        try {
            AssaultParty[] aParties = new AssaultParty[2];
            Museum museum = new Museum(5,30,15,16,8);
            GeneralRepos repos = new GeneralRepos("logfile.txt");
            for(int i = 0; i< 2; i++){
                aParties[i] = new AssaultParty(i, 3, 3, 3)
            }
    
            ControlCollectionSite controlSite = new ControlCollectionSite(aParties, repos, 5, 6);
            ConcentrationSite concentSite = new ConcentrationSite(aParties);
    
            mThief master = new mThief(aParties,controlSite,concentSite);
            oThief[] thieves = new oThief[6];
    
            for(int i = 0; i < 6; i++){
                thieves[i] = new oThief(i, aParties, controlSite, concentSite, museum, 6, 2);
            }
    
            master.start();
            for(int i = 0; i < 6; i++){
                thieves[i].start();
            }
        } catch (Exception e) {
        }
    }
}
