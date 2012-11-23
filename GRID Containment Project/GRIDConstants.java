/* Auth: Shannon Foss
 * Date: Spring 2011
 * File: GRIDConstants
 * Desc: Repository for all modifiable constants
 */
public class GRIDConstants {

	//change these values for different sized containers plus the spacing between them
	public static final double CTRLENGTH = 40.5;//feet
	public static final double CTRWIDTH = 8.5;
	public static final double CTRHEIGHT = 8.5;

	//change these values for a different sized grid 
	public static final int ROWS = 20;//26
	public static final int COLS = 20;//24
	public static final int HEIGHT = 5;
	
	//change this value for the starting percentage of grid filled
	public static final double PCTGRIDFILLED = .35;
	
	//change these values to modify the number of TUs used on the train
	public static final int NUMTU = 14;
	public static final int NUMTUSONTRAIN = 10;
	
	//modify these values for different speeds and accelerations
	public static final double VMAXL = 3.35;//feet per second - loaded
	public static final double VMAXU = 5.025;//feet per second - unloaded
	public static final double ACCEL = .67;//feet per second^2
	public static final double VBEAML = 3.35;//feet per second - loaded
	public static final double VBEAMU = 5.025;//feet per second - unloaded
}
