/* 
 * Author: Carlos Silva 
 * 
 * First two functions of Simulation Project 4.
 */

public class Sim4 {

    /* HELPER FUNCTIONS THAT YOU CAN CALL */
	public int signExtend16to32(int val16) {
		if ((val16 & 0x8000) != 0)
			return val16 | 0xFFFF0000;
		else
			return val16;

	}



	public void extractInstructionFields(int instruction, InstructionFields fieldsOut) {
		fieldsOut.opcode = instruction >>> 26;
		fieldsOut.rs = (instruction >>> 21) & 0x1F;
		fieldsOut.rt = (instruction >>> 16) & 0x1F;
		fieldsOut.rd = (instruction >>> 11) & 0x1F;
		fieldsOut.shamt = (instruction >>> 6) & 0x1F;
		fieldsOut.funct = instruction & 0x3F;
		fieldsOut.address = instruction & 0x3FFFFFF;
		fieldsOut.imm16 = instruction & 0xFFFF;
		fieldsOut.imm32 = signExtend16to32(fieldsOut.imm16);
	}

	public int fillCPUControl(InstructionFields fields, CPUControl controlOut) {
		controlOut.ALUsrc = 0;
		controlOut.memRead = 0;
		controlOut.memWrite = 0;
		controlOut.memToReg = 0;
		controlOut.regDst = 0;
		controlOut.regWrite = 0;
		controlOut.branch = 0;
		controlOut.jump = 0;
		controlOut.ALU.op = 0;
		controlOut.ALU.bNegate = 0;
		
		if (fields.opcode == 0) {
			if (fields.funct == 32 || fields.funct == 33) {
				controlOut.ALU.op = 2;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
			} else if (fields.funct == 34 || fields.funct == 35) {
				controlOut.ALU.op = 2;
				controlOut.ALU.bNegate = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
			} else if (fields.funct == 36) {
				controlOut.ALU.op = 0;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
			} else if (fields.funct == 37) {
				controlOut.ALU.op = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
			} else if (fields.funct == 38) {
				controlOut.ALU.op = 4;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
			} else if (fields.funct == 42) {
				controlOut.ALU.op = 3;
				controlOut.ALU.bNegate = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
			} 
		} else if (fields.opcode == 8 || fields.opcode == 9) {
			controlOut.ALU.op = 2;
			controlOut.ALUsrc = 1;
			controlOut.regWrite = 1;
		} else if (fields.opcode == 10) {
			controlOut.ALU.op = 3;
			controlOut.ALU.bNegate = 1;
			controlOut.ALUsrc = 1;
			controlOut.regWrite = 1;
		} else if (fields.opcode == 35) {
			controlOut.ALU.op = 2;
			controlOut.ALUsrc = 1;
			controlOut.memRead = 1;
			controlOut.memToReg = 1;
			controlOut.regWrite = 1;
		} else if (fields.opcode == 43) {
			controlOut.ALU.op = 2;
			controlOut.ALUsrc = 1;
			controlOut.memWrite = 1;
		} else if (fields.opcode == 4) {
			controlOut.ALU.op = 2;
			controlOut.ALU.bNegate = 1;
			controlOut.branch = 1;
		} else if (fields.opcode == 2) {
			controlOut.jump = 1;
		} 
		
        return 0;
	}
    
    
    // Method signatures corresponding to function prototypes in sim4.h
	public int getInstruction(int curPC, int[] instructionMemory) {
		// TODO on milestone 2: Your code goes here
        
        return 0;
	}

	public int getALUinput1(CPUControl controlIn, InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
		// TODO on milestone 2: Your code goes here
        return 0;
	}

	public int getALUinput2(CPUControl controlIn, InstructionFields fieldsIn,
                                   int rsVal, int rtVal, int reg32, int reg33, int oldPC) {
		// TODO on milestone 2: Your code goes here
        return 0;
	}

	public void executeALU(CPUControl controlIn, int input1, int input2, ALUResult aluResultOut) {
		// TODO on milestone 2: Your code goes here

	}

	public void executeMEM(CPUControl controlIn, ALUResult aluResultIn,
                                  int rsVal, int rtVal, int[] memory, MemResult resultOut) {
		// TODO on milestone 2: Your code goes here

	}

	public int getNextPC(InstructionFields fields, CPUControl controlIn, int aluZero,
                                int rsVal, int rtVal, int oldPC) {
		// TODO on milestone 2: Your code goes here
        return 0;
	}

	public void executeUpdateRegs(InstructionFields fields, CPUControl controlIn,
                                         ALUResult aluResultIn, MemResult memResultIn, int[] regs) {
		// TODO on milestone 2: Your code goes here

	}
}