package cocagne.paxos.practical;

import cocagne.paxos.essential.EssentialLearner;


public interface PNode extends PracticalProposer, PracticalAcceptor, EssentialLearner{
    public Object getAcceptedValue();
}
