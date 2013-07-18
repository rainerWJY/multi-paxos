package cocagne.paxos.practical;

import cocagne.paxos.essential.EssentialAcceptor;
import cocagne.paxos.essential.ProposalID;

public interface PracticalAcceptor extends EssentialAcceptor {

	public abstract boolean persistenceRequired();

	public abstract void recover(ProposalID promisedID, ProposalID acceptedID,
			Object acceptedValue);

	public abstract void persisted();

	public abstract boolean isActive();
    /**The 'active' attribute is a boolean value indicating whether or not
    the Proposer should send outgoing messages (defaults to True). Setting
    this attribute to false places the Proposer in a "passive" mode where
    it processes all incoming messages but drops all messages it would
    otherwise send.*/
	public abstract void setActive(boolean active);

}