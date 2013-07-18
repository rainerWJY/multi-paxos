package cocagne.paxos.essential;


public interface EssentialAcceptor {
	/**
	 * phase 1 receive prepare
	 * @param fromUID
	 * @param proposalID
	 */
	public void receivePrepare(String fromUID, ProposalID proposalID);
	
	public void receiveAcceptRequest(String fromUID, ProposalID proposalID, Object value);
}
