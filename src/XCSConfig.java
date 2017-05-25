/**
 * Created by Simon on 15.05.2017.
 */
public class XCSConfig {
    public static boolean SHOULD_LOAD_FROM_CSV = true; // makes XCS load fron CSV and prevents save to CSV

    public static double alpha = 0.1;
    public static double beta = 0.2;
    public static double gamma = 0.71;
    public static double delta = 0.1;
    public static double nu = 5;
    public static double epsilon0 = 0.01;
    public static double predictionI = 0.0; //TODO richtigen Wert herausfinden bzw berechnen
    public static double epsilonI = 0.0;
    public static double FI = 0.01;
    public static int thetaGA = 50;
    public static int thetaDEL = 50;
    public static double thetaSUB = 50;
    public static double thetaRLS = 1000;
    public static int thetaMNA = 12; //Anzahl der Aktionen die minimal im Match Set vorhanden sein müssen, damit kein Covering betrieben wird
    public static double chi = 0.8;
    public static double my = 0.04;
    public static int N = 6400;
    public static double m0 = 0.2;
    public static double r0 = 0.33;
    public static boolean doActionSetSubsumption = true;
    public static boolean doGASubsumption = true;
    public static double pExp = 0.5; //Wahrscheinlichkeit für exploration
}
