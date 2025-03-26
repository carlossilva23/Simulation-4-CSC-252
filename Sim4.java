/* 
 * Author: Carlos Silva 
 * 
 * Final Full Implementation of Sim 4.
 */

public class Sim4 {

	/* HELPER FUNCTIONS THAT YOU CAN CALL */
	public int signExtend16to32(int val16) {
		if ((val16 & 0x8000) != 0)
			return val16 | 0xFFFF0000;
		else
			return val16;

	}

	/*
	 * This function will extract all the necessary parameters needed for the
	 * instructions of an operation.
	 */
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

	/*
	 * This function will fill the CPU with the necesarry fields based on the above
	 * instructions extracted.
	 */
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
		controlOut.extra1 = 0;
		controlOut.extra2 = 0;
		controlOut.extra3 = 0;

		// R-Type Instructions
		if (fields.opcode == 0) {
			// ADD, ADDU
			if (fields.funct == 32 || fields.funct == 33) {
				controlOut.ALU.op = 2;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				// SUB SUBU
			} else if (fields.funct == 34 || fields.funct == 35) {
				controlOut.ALU.op = 2;
				controlOut.ALU.bNegate = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				// AND
			} else if (fields.funct == 36) {
				controlOut.ALU.op = 0;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				// OR
			} else if (fields.funct == 37) {
				controlOut.ALU.op = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				// XOR
			} else if (fields.funct == 38) {
				controlOut.ALU.op = 4;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				// SLT
			} else if (fields.funct == 42) {
				controlOut.ALU.op = 3;
				controlOut.ALU.bNegate = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
				// SLL
			} else if (fields.funct == 0) {
				controlOut.ALU.op = 5;
				controlOut.ALUsrc = 1;
				controlOut.regDst = 1;
				controlOut.regWrite = 1;
			}
			// Immediate and Jump Instructions
			// ADDI, ADDIU
		} else if (fields.opcode == 8 || fields.opcode == 9) {
			controlOut.ALU.op = 2;
			controlOut.ALUsrc = 1;
			controlOut.regWrite = 1;
			// ANDI
		} else if (fields.opcode == 10) {
			controlOut.ALU.op = 3;
			controlOut.ALU.bNegate = 1;
			controlOut.ALUsrc = 1;
			controlOut.regWrite = 1;
			// LW
		} else if (fields.opcode == 35) {
			controlOut.ALU.op = 2;
			controlOut.ALUsrc = 1;
			controlOut.memRead = 1;
			controlOut.memToReg = 1;
			controlOut.regWrite = 1;
			// SW
		} else if (fields.opcode == 43) {
			controlOut.ALU.op = 2;
			controlOut.ALUsrc = 1;
			controlOut.memWrite = 1;
			// BEQ
		} else if (fields.opcode == 4) {
			controlOut.ALU.op = 2;
			controlOut.ALU.bNegate = 1;
			controlOut.branch = 1;
			// BNE
		} else if (fields.opcode == 5) {
			controlOut.ALU.op = 2;
			controlOut.ALU.bNegate = 1;
			controlOut.branch = 1;
			// JUMP
		} else if (fields.opcode == 2) {
			controlOut.jump = 1;
		}

		return 0;
	}

	// Method signatures corresponding to function prototypes in sim4.h
	public int getInstruction(int curPC, int[] instructionMemory) {
		return instructionMemory[curPC / 4];
	}

	/*
	 * This function will retrieve the first ALUinput.
	 */
	public int getALUinput1(CPUControl controlIn, InstructionFields fieldsIn, int rsVal, int rtVal, int reg32,
			int reg33, int oldPC) {
		if (controlIn.ALU.op == 5) {
			return rtVal;
		}
		return rsVal;
	}

	/*
	 * This function will return the second ALUinput.
	 */
	public int getALUinput2(CPUControl controlIn, InstructionFields fieldsIn, int rsVal, int rtVal, int reg32,
			int reg33, int oldPC) {
		if (controlIn.ALUsrc == 1) {
			if (controlIn.ALU.op == 0 || controlIn.ALU.op == 1 || controlIn.ALU.op == 4) {
				return fieldsIn.imm16;
			} else if (controlIn.ALU.op == 2 && controlIn.branch == 2) {
				return fieldsIn.imm16;
			}
			return fieldsIn.imm32;
		} else if (controlIn.ALU.op == 5) {
			return fieldsIn.shamt;
		}
		return rtVal;
	}

	/*
	 * This function will execute the ALU and set the results to aluResultOut.
	 */
	public void executeALU(CPUControl controlIn, int input1, int input2, ALUResult aluResultOut) {
		int result = 0;

		if (controlIn.ALU.op == 0) {
			result = input1 + input2;
		} else if (controlIn.ALU.op == 1) {
			result = input1 - input2;
		} else if (controlIn.ALU.op == 2) {
			result = input1 & input2;
		} else if (controlIn.ALU.op == 3) {
			result = input1 | input2;
		} else if (controlIn.ALU.op == 4) {
			result = (input1 < input2) ? 1 : 0;
		} else if (controlIn.ALU.op == 5) {
			result = ~(input1 | input2);
		}

		aluResultOut.result = result;

		if (controlIn.ALU.bNegate == 1) {
			aluResultOut.result = ~aluResultOut.result;
		}

		aluResultOut.zero = (aluResultOut.result == 0) ? 1 : 0;
	}

	/*
	 * This function will handle any instructions that change/concern with memory.
	 */
	public void executeMEM(CPUControl controlIn, ALUResult aluResultIn, int rsVal, int rtVal, int[] memory,
			MemResult resultOut) {
		resultOut.readVal = 0;
		if (controlIn.memRead == 1) {
			resultOut.readVal = memory[aluResultIn.result / 4];
		}
		if (controlIn.memWrite == 1) {
			memory[aluResultIn.result / 4] = rtVal;
		}
	}

	/*
	 * This function will determine the next PC value.
	 */
	public int getNextPC(InstructionFields fields, CPUControl controlIn, int aluZero, int rsVal, int rtVal, int oldPC) {
		// Handles BEQ and BNE branch code
		if (controlIn.branch == 1 && ((fields.opcode == 4 && aluZero == 1) || (fields.opcode == 5 && aluZero == 0))) {
			return oldPC + 4 + (fields.imm32 << 2);
		}
		// Handles Jump Instructions
		if (controlIn.jump == 1) {
			return ((oldPC + 4) & 0xF0000000) | (fields.address << 2);
		}
		return oldPC + 4;
	}

	/*
	 * This function will update registers based on control signals.
	 */
	public void executeUpdateRegs(InstructionFields fields, CPUControl controlIn, ALUResult aluResultIn,
			MemResult memResultIn, int[] regs) {
		if (controlIn.regWrite == 1) {
			int writeReg;
			int writeData;

			if (controlIn.regDst == 1) {
				writeReg = fields.rd;
			} else {
				writeReg = fields.rt;
			}

			if (controlIn.memToReg == 1) {
				writeData = memResultIn.readVal;
			} else {
				writeData = aluResultIn.result;
			}

			if (writeReg != 0) {
				regs[writeReg] = writeData;
			}
		}
	}
}