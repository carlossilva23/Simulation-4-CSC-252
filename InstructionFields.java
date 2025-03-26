public class InstructionFields {
    // THESE ARE NOT ACTUALLY CONTROL BITS
	//
	// However, you have to fill them out.  Fill out *all* of these fields
	// for *all* instructions, even if it doesn't use that particular
	// field.  For instance, opcode 0 means that it is an R format
	// instruction - but you *still* must set the proper values for imm16,
	// imm32, and address.
	//
	// NOTE: imm16 is the field from the instruction,  imm32 is the
	//       sign-extended version.
	int opcode; 
	int rs;
	int rt; 
	int rd;
	int shamt;  
	int funct;
	int imm16, imm32; 
	int address; 	// this is the 26 bit field from the J format
}
