public class CPUControl {
    
    // THESE ARE THE REAL CONTROL BITS
	//
	// For the ALU control bits, we will ignore the fact that the "main"
	// control produces a 2-bit value, which is combined with the funct
	// field to produce a 3-bit control (bNegate+ALUop).  We'll ignore the
	// 2-bit from the main control; we will *only* report the 3-bit
	// control that comes out of the ALU Control field.
	ALUControl ALU;
	int ALUsrc;    

	int memRead;
	int memWrite;   
	int memToReg;

	int regDst;
	int regWrite;

	int branch;
	int jump;

    
    // EXTRA "WORDS". A word is an int.
	//
	// You are not required to use these fields.  But if you want to
	// support other instructions, some additional control bits might be
	// necessary.  You may use these fields for anything you want; the
	// testcases will ignore them.
	int extra1, extra2, extra3;

	public CPUControl() {
		ALU = new ALUControl();
	}
}

