package cocagne.paxos.play;

import cocagne.paxos.practical.PracticalNode;



public class Play {
    public static void main(String[] args) {
//        //Æô¶¯proposer
        Messager messager0 = new Messager("0");

        Messager messager1 = new Messager("1");
        
        Messager messager2 = new Messager("2");
        
        PracticalNode node0 = new PracticalNode(messager0, "0", 2);
        
        PracticalNode node1 = new PracticalNode(messager1, "1", 2);
        PracticalNode node2 = new PracticalNode(messager2, "2", 2);
        
        messager0.put("0",node0);
        messager0.put("1",node1);
        messager0.put("2",node2);
        
        messager1.put("0",node0);
        messager1.put("1",node1);
        messager1.put("2",node2);
        
        messager2.put("0",node0);
        messager2.put("1",node1);
        messager2.put("2",node2);
        
        node0.prepare();
        node0.setProposal("proposal1");
        }
}
