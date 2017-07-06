/**
 * Created by Simon on 15.05.2017.
 */
public class XCSConfig {
    public static boolean SHOULD_LOAD_FROM_CSV = false; // makes XCS load from CSV
    public static boolean SHOULD_SAVE_TO_CSV = true; // makes XCS save to CSV
    public static String CSV_PREFIX = "AI_Test_";

    public static double alpha = 0.1;
    public static double beta = 0.2;
    public static double gamma = 0.71;
    public static double delta = 0.1;
    public static double nu = 5;
    public static double epsilon0 = 0.01;
    public static double predictionI = 0.0; //TODO richtigen Wert herausfinden bzw berechnen
    public static double epsilonI = 0.0;
    public static double FI = 0.01;
    public static int thetaGA = 40;
    public static int thetaDEL = 50;
    public static double thetaSUB = 20;
    public static double thetaRLS = 1000;
    public static int thetaMNA = 5; //Anzahl der Aktionen die minimal im Match Set vorhanden sein müssen, damit kein Covering betrieben wird
    public static double chi = 0.8;
    public static CrossoverType crossoverType = CrossoverType.UNIFORM;
    public static double my = 0.04;
    public static int N = 6400;
    public static double m0 = 1.6;
    public static double r0 = 1.6;
    public static boolean doActionSetSubsumption = true;
    public static boolean doGASubsumption = true;
    public static double tournamentSize = 0.2; //Anteil des ActionSets, der für die Tournament Selection verwendet wird
    public static SelectionType selectionType = SelectionType.TOURNAMENT;
    public static double pExp = 0.1; //Wahrscheinlichkeit für exploration
}
