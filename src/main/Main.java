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
            GeneralRepos repos = new GeneralRepos("logfile.txt");
            Museum museum = new Museum(5,30,15,16,8, repos);
            for(int i = 0; i< 2; i++){
                aParties[i] = new AssaultParty(i, 3, 3, 3,museum,repos);
            }
    
            ControlCollectionSite controlSite = new ControlCollectionSite(aParties, repos, 5, 6);
            ConcentrationSite concentSite = new ConcentrationSite(aParties, repos);
    
            mThief master = new mThief(aParties,controlSite,concentSite);
            oThief[] thieves = new oThief[6];
    
            for(int i = 0; i < 6; i++){
                thieves[i] = new oThief(i, aParties, controlSite, concentSite, museum, 6, 2);
                repos.setOrdinaryThiefMD(i, thieves[i].getMD());
            }
    
            master.start();
            for(int i = 0; i < 6; i++){
                thieves[i].start();
            }
        } catch (Exception e) {
        }
    }
}
