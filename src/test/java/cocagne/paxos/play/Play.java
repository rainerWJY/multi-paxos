package cocagne.paxos.play;

import cocagne.paxos.practical.PracticalNode;



public class Play {
    public static void main(String[] args) {
//        //����proposer
        Messenger messager0 = new Messenger("0");

        Messenger messager1 = new Messenger("1");
        
        Messenger messager2 = new Messenger("2");
        
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
