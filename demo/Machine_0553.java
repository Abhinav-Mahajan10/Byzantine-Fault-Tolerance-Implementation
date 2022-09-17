package demo;

import common.Location;
import common.Machine;
import java.util.ArrayList;
import java.lang.Math;

public class Machine_0553 extends Machine {

	//default ctor
	public Machine_0553() {
		id = nextId++;
		round_1_type1 = round_1_type_2 = round_2_type1 = round_2_type_2 = 0;
		//decision_flag = false;
		round_1 = false;
		round_2 = false;
	}

	//initialises the list of machine objects, as well as finds out the maximum number of faulty machines
	@Override
	public void setMachines(ArrayList<Machine> machines)
	{
		machinesList = machines;
		t_max = (int)(machinesList.size() / 3);
		if(machinesList.size() % 3 == 0)
		{
			t_max--;
		}
	}

	//sets step size
	@Override
	public void setStepSize(int stepSize) {
		step = stepSize;
	}

	//sets the state
	@Override
	public void setState(boolean isCorrect) {
		/*if(isCorrect == false)
		{
			System.out.println(id + " is faulty");
		}*/
		is_Correct = isCorrect;
		round_1 = false;
		round_2 = false;
		round_1_type1 = 0;
		round_1_type_2 = 0;
		round_2_type1 = 0;
		round_2_type_2 = 0;
		//decision_flag = false;
	}

	//sets the leader and carries out round 0
	@Override
	public void setLeader() {
		//System.out.println("Leader is " + id);
		int decision = (int)(Math.random() * 10) % 2;
		if(is_Correct == true)
		{
			for(Machine machine : machinesList)
			{
				machine.sendMessage(id, PhaseNo, 0, decision);
			}
		}
		else
		{
			//if machine is faulty, it may change its decision as well as choose upto t_max machines to not propagate its message
			int temp = 0;
			decision = (int)(Math.random() * 10) % 2;
			for(Machine machine : machinesList)
			{
				if(temp < t_max)
				{
					int rand = (int)(Math.random() * 10) % 2;
					if(rand == 0)
					{
						temp++;
					}
					else
					{
						machine.sendMessage(id, PhaseNo, 0, decision);
					}
				}
				else
				{
					machine.sendMessage(id, PhaseNo, 0, decision);
				}
			}
		}
		PhaseNo++;
	}

	@Override
	public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision) {
		
		//System.out.println(""  + is_Correct);
		if(roundNum == 0 && round_1 == false)
		{
			//System.out.println("\nReceiver, " + id + " Sender, " + sourceId);
			//System.out.println("PhaseNo, " + phaseNum + " roundNum, " + roundNum + " decision, " + decision + "\n");
			if(is_Correct == true)
			{
				for(Machine machine : machinesList)
				{
					machine.sendMessage(id, phaseNum, 1, decision);
				}
			}
			else
			{
				//faulty machine may stay quiet for a round
				int quiet = (int)(Math.random() * 10) % 2;
				if(quiet == 1)
				{
					//System.out.println(id + " Remained Quiet in round 0");
					return ;
				}
				//also faulty machines may flip its decision
				decision = (int)(Math.random() * 10) % 2;
				for(Machine machine : machinesList)
				{
					machine.sendMessage(id, phaseNum, 1, decision);
				}
			}
			round_1 = true;
		}
		if(roundNum == 1 && round_2 == false)
		{
			if(decision == 0)
			{
				round_1_type1++;
			}
			else
			{
				round_1_type_2++;
			}
			////System.out.println("\nReceiver, " + id + " Sender, " + sourceId + " type1 = " + round_1_type1 + " type2 = " + round_1_type_2);
			//System.out.println("PhaseNo, " + phaseNum + " roundNum, " + roundNum + " decision, " + decision + "\n");

			//if enough round_1 messages are received 
			if(round_1_type1 > t_max || round_1_type_2 > t_max || round_1_type1 + round_1_type_2 > 2*t_max)
			{
				decision = (round_1_type1 > round_1_type_2 ? 0 : 1);
				round_1_type1 = 0;
				round_1_type_2 = 0;
				if(is_Correct == true)
				{
					for(Machine machine : machinesList)
					{
						machine.sendMessage(id, phaseNum, 2, decision);
					}
				}
				else
				{
					//faulty machine may stay quiet
					int quiet = (int)(Math.random() * 10) % 2;
					if(quiet == 1)
					{
						//System.out.println(id + " Remained Quiet in round 1");
						return ;
					}
					decision = (int)(Math.random() * 10) % 2;
					for(Machine machine : machinesList)
					{
						machine.sendMessage(id, phaseNum, 2, decision);
					}
				}
				round_2 = true;
			}
		}
		if(roundNum == 2)
		{
			if(decision == 0)
			{
				round_2_type1++;
			}
			else
			{
				round_2_type_2++;
			}
			//System.out.println("\nReceiver, " + id + " Sender, " + sourceId + " type1 = " + round_2_type1 + " type2 = " + round_2_type_2);
			//System.out.println("PhaseNo, " + phaseNum + " roundNum, " + roundNum + " decision, " + decision + "\n");

			//when enough round_2 messages are received
			if(round_2_type1 >= 2*t_max + 1 || round_2_type_2 >= 2*t_max + 1)
			{
				decision = (round_2_type1 > round_2_type_2 ? 0 : 1);
				if(is_Correct == false)
				{
					//decision maybe flipped if machine is faulty
					decision = (int)(Math.random() * 10) % 2;
				}
				round_2_type1 = 0;
				round_2_type_2 = 0;
				//for acting on the decision
				if(dir.getX() == 1 && dir.getY() == 0)
				{
					if(decision == 1)
					{
						dir.setLoc(0, 1);
					}
					else
					{
						dir.setLoc(0, -1);
					}
				}
				else if(dir.getX() == -1 && dir.getY() == 0)
				{
					if(decision == 1)
					{
						dir.setLoc(0, -1);
					}
					else
					{
						dir.setLoc(0, 1);
					}
				}
				else if(dir.getX() == 0 && dir.getY() == 1)
				{
					if(decision == 1)
					{
						dir.setLoc(-1, 0);
					}
					else
					{
						dir.setLoc(1, 0);
					}
				}
				else if(dir.getX() == 0 && dir.getY() == -1)
				{
					if(decision == 1)
					{
						dir.setLoc(1, 0);
					}
					else
					{
						dir.setLoc(-1, 0);
					}
				}
				//decision_flag = true;
				round_1 = false;
				round_2 = false;
			}
		}
	}

	@Override
	public
	void move() {
		/*if(decision_flag == true)
		{
			pos.setLoc(pos.getX() + dir.getX()*step, 
					pos.getY() + dir.getY()*step);
			//decision_flag = false;
		}*/
		pos.setLoc(pos.getX() + dir.getX()*step, 
					pos.getY() + dir.getY()*step);
	}

	@Override
	public
	String name() {
		return "demo_"+id;
	}

	@Override
	public Location getPosition() {
		
		return new Location(pos.getX(), pos.getY());
	}

	//maintains list of machines
	private ArrayList<Machine> machinesList = new ArrayList<Machine>();
	//flag variable for whether machine is correct or faulty
	private boolean is_Correct;
	//maintains stepsize
	private int step;
	//maintains position
	private Location pos = new Location(0,0);
	//maintains turning vector
	private Location dir = new Location(0,1); // using Location as a 2d vector. Bad!
	//static machine id accumulator
	private static int nextId = 0;
	//maintains pahseId
	private static int PhaseNo = 0;
	//id of machine
	private int id;
	//max number of faulty machines
	private int t_max;
	//stores number of messages recieved
	private int round_1_type1, round_1_type_2;
	private int round_2_type1, round_2_type_2;
	//private boolean decision_flag;
	//control variables to prevent oversending messages
	private boolean round_1;
	private boolean round_2;
}