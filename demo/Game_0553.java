package demo;

import java.util.ArrayList;
import java.lang.Math;

import common.Game;
import common.Machine;

public class Game_0553 extends Game {

	private ArrayList<Machine> machinesList;
	private int num_of_faulty_machines;
	private int no_of_machines;

	//constructor to initialise the variables to default values
	public Game_0553()
	{
		machinesList = new ArrayList<Machine>();
		num_of_faulty_machines = 0;
		no_of_machines = 0;
	}

	//adds machines to itself and sends the list to each machine in the list
	@Override
	public void addMachines(ArrayList<Machine> machines, int numFaulty) {
		// TODO Auto-generated method stub
	
		machinesList = machines;
		num_of_faulty_machines = numFaulty;
		no_of_machines = machinesList.size();
		for(Machine machine : machinesList)
		{
			machine.setMachines(machines);
		}
	}

	//when we have to randomly set the leader and faulty machines ourselves
	@Override
	public void startPhase() {
		// TODO Auto-generated method stub
		double a = 10.0;
		int temp = (int) (no_of_machines / a);
		while(temp != 0)
		{
			a*=10.0;
			temp = (int) (temp / a);
		}
		int rand1 = (int)(Math.random() * a) % no_of_machines;
		temp = 0;
		int rand;
		for(Machine machine : machinesList)
		{
			if(temp < num_of_faulty_machines)
			{
				rand = (int)(Math.random() * 10) % 2;
				if(rand == 0)
				{
					machine.setState(false);
					temp++;
				}
				else
				{
					machine.setState(true);
				}
			}
			else
			{
				machine.setState(true);
			}
		}
		machinesList.get(rand1).setLeader();
	}

	//when the leader and faulty machines are sent by GUI
	@Override
	public void startPhase(int leaderId, ArrayList<Boolean> areCorrect) {
		int i = 0;
		for(Machine machine : machinesList)
		{
			machine.setState(areCorrect.get(i++));
		}
		machinesList.get(leaderId).setLeader();
	}
}
