import java.util.HashMap;

public class Viterbi 
{
	public static void main(String[] args) 
	{
		String[] hidden_vars = new String[] {"Hot", "Cold"};
		String[] observations = new String[] {"331122313", "331123312"};

		// starting probabilities
		HashMap<String, Double> start_prob = new HashMap<>();
		start_prob.put("Hot", 0.8);
		start_prob.put("Cold", 0.2);

		// transition_probabilities
		HashMap<String, HashMap<String, Double>> trans_prob = new HashMap<>();
		HashMap<String, Double> temp1 = new HashMap<>();
		temp1.put("Hot", 0.7);
		temp1.put("Cold", 0.3);
		HashMap<String, Double> temp2 = new HashMap<>();
		temp2.put("Hot", 0.4);
		temp2.put("Cold", 0.6);
		trans_prob.put("Hot", temp1);
		trans_prob.put("Cold", temp2);

		// emission_probabilities
		HashMap<String, HashMap<String, Double>> em_prob = new HashMap<>();
		HashMap<String, Double> temp3 = new HashMap<>();
		temp3.put("1", 0.2);		
		temp3.put("2", 0.4); 
		temp3.put("3", 0.4);
		HashMap<String, Double> temp4 = new HashMap<>();
		temp4.put("1", 0.5);		
		temp4.put("2", 0.4); 
		temp4.put("3", 0.1);
		em_prob.put("Hot", temp3);
		em_prob.put("Cold", temp4);

		for (int i = 0; i < observations.length; i++) {
			System.out.println("Most likely Weather sequence for observation sequence " + observations[i] + " is:: \n" + forward_viterbi(observations[i].split(""),hidden_vars,start_prob,trans_prob,em_prob));
			System.out.println("--------------------------------------------------------------------------------------------");
		}
	}

	public static String forward_viterbi(String[] obs, String[] hidden_vars,HashMap<String, Double> start_p,
			HashMap<String, HashMap<String, Double>> trans_p,HashMap<String, HashMap<String, Double>> emit_p){

		HashMap<String, Pair> viterbi = new HashMap<>();
		for (String var : hidden_vars)
			viterbi.put(var, new Pair (start_p.get(var), var));

		for (String output : obs){
			HashMap<String, Pair> temp = new HashMap<>();
			for (String next_state : hidden_vars){
				double valmax = 0, v_prob = 1;
				String argmax = "", v_path = "";

				for (String source_state : hidden_vars){
					Pair objs = viterbi.get(source_state);
					v_path = objs.getPath();
					v_prob = objs.getProbability();

					double p = emit_p.get(source_state).get(output) * trans_p.get(source_state).get(next_state);
					v_prob *= p;
					if (v_prob > valmax){
						argmax = v_path + "->" + next_state;
						valmax = v_prob;
					}
				}
				temp.put(next_state, new Pair(valmax, argmax));
			}
			viterbi = temp;			
		}

		double valmax = 0, v_prob;
		String argmax = "", v_path;

		for (String state : hidden_vars){
			Pair objs = viterbi.get(state);
			v_path = objs.getPath();
			v_prob = objs.getProbability();
			if (v_prob > valmax){
				argmax = v_path;
				valmax = v_prob;
			}
		}	
		return argmax;	
	}
}

class Pair{
	private double prob;
	private String path;
	Pair(double prob1, String path1){
		path = path1;
		prob = prob1;
	}
	
	public double getProbability(){
		return this.prob;
	}
	
	public String getPath(){
		return this.path;
	}
}