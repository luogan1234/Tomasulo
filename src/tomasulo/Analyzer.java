package tomasulo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyzer {
	public TomasuloCore tomasulo;
	
	public Analyzer() {
		tomasulo = new TomasuloCore();
	}
	
	public boolean addInst(String inst) { // format: type op1 op2 op3
		String[] insts = inst.split(" ");
		if (insts.length != 4) return false;
		InstType type;
		switch (insts[0]) {
		case "ADDD":
			type = InstType.ADDD;
			break;
		case "SUBD":
			type = InstType.SUBD;
			break;
		case "MULD":
			type = InstType.MULTD;
			break;
		case "DIVD":
			type = InstType.DIVD;
			break;
		case "LD":
			type = InstType.LD;
			break;
		case "ST":
			type = InstType.ST;
			break;
		case "SET":
			type = InstType.SET;
		default:
			return false;
		}
		
		if (type == InstType.ADDD || type == InstType.SUBD || type == InstType.MULTD || type == InstType.DIVD) {
			if (!(insts[1].matches("F[0-9]+") && insts[2].matches("F[0-9]+") && insts[3].matches("F[0-9]+"))) return false;
			int op1 = getOp(insts[1]);
			int op2 = getOp(insts[2]);
			int op3 = getOp(insts[3]);
			if (op1 < 0 || op1 > 10 || op2 < 0 || op2 > 10 || op3 < 0 || op3 > 10) return false; 
			tomasulo.addInst(type, op1, op2, op3);
			return true;
		}
		else if (type == InstType.LD || type == InstType.ST) {
			if (!(insts[1].matches("F[0-9]+") && insts[2].matches("[0-9]+") && insts[3].matches("R[0-9]+"))) return false;
			int op1 = getOp(insts[1]);
			int op3 = getOp(insts[3]);
			int op2 = Integer.parseInt(insts[2]);
			if (op1 < 0 || op1 > 10 || op3 < 0 || op3 > 10) return false;
			// TODO: judge 4096
			tomasulo.addInst(type, op1, op2, op3);
			return true;
		}
		else if (type == InstType.SET){
			if (!(insts[1].matches("[FMR]") && insts[2].matches("[0-9]+") && insts[3].matches("[0-9]+"))) return false;
			String op1 = insts[1];
			int op2 = getOp(insts[2]);
			int op3 = getOp(insts[3]);
			switch (op1)
			{
			case "F":
				if (op2 >= 0 && op2 <= 10)
					tomasulo.resource.freg[op2].value = op3;
				break;
			case "M":
				if (op2 >= 0 && op2 <= 4096)
					tomasulo.resource.mem.store(op2, op3);
				break;
			case "R":
				if (op2 >= 0 && op2 <= 10)
					tomasulo.resource.reg[op2].value = op3;
				break;
			default:
				break;
			}
			return true;
		}
		else return false;
	}
	
	private int getOp(String reg) {
		Pattern p = Pattern.compile("[^0-9]");
		Matcher m = p.matcher(reg);
		return Integer.parseInt(m.replaceAll("").trim());
	}
}
