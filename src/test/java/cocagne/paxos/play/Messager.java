package cocagne.paxos.play;

import java.util.HashMap;
import java.util.Map;

import cocagne.paxos.essential.ProposalID;
import cocagne.paxos.practical.PNode;
import cocagne.paxos.practical.PracticalMessenger;

public class Messager implements PracticalMessenger {

    /**
     * unique id in this Node , same as UID in PracticalNode
     */
    private String                                   uid;

    /**
     * node in group
     */
    private Map<String/* nodename */, PNode> map      = new HashMap<String, PNode>();

    private volatile boolean                         isMaster = false;

    public Messager(String id){
        super();
        this.uid = id;
    }

    @Override
    public void sendPrepare(ProposalID proposalID) {
        System.out.println("send cmd, step 1 : sendPrepare " + proposalID + ", " + uid);
        // step 1 , can only send prepare request to quorum number of accepters
        // to save
        // net traffic
        for (PNode pnode : map.values()) {
            // net
            try {
                pnode.receivePrepare(proposalID.getUID(), proposalID);
            } catch (Exception e) {
                System.err.println(e.getCause().getMessage());
            }
            System.out.println("store to file , proposalID" + proposalID);
            // tell the paxos that persistence has been done
            pnode.persisted();
        }

    }

    @Override
    public void sendPromise(String proposerUID, ProposalID proposalID, ProposalID previousID, Object acceptedValue) {
        System.out.println("send cmd, step 2 : sendPromise, leader election : " + proposalID + ", " + uid);
        // step 2 . optimized : may only send message to the node who need our
        // promise?
        PNode pnode = map.get(proposerUID);
        // take care , in this situation, request's uid is proposerUID in src
        // node , and
        // in target node , they will be named 'fromUID'. so the first argument
        // at receivePromise
        // is fromUID which is named proposerUID in sendPromise method.
        try {
            // net
            pnode.receivePromise(uid, proposalID, previousID, acceptedValue);
        } catch (Exception e) {
            System.err.println(e.getCause().getMessage());
        }
    }

    @Override
    public void sendAccept(ProposalID proposalID, Object proposalValue) {
        for (PNode pnode : map.values()) {
            // net
            try {
                pnode.receiveAcceptRequest(proposalID.getUID(), proposalID, proposalValue);
            } catch (Exception e) {
               System.err.println(e.getCause().getMessage());
            }
            // store to file
            // tell the paxos that persistence has been done
            pnode.persisted();
        }
    }

    @Override
    public void sendAccepted(ProposalID proposalID, Object acceptedValue) {
        System.out.println("sendAccepted : " + proposalID + ", " + uid);
        System.err.println("restore proposal value in " + uid + ", value is " + acceptedValue);
        PNode pnode = map.get(proposalID.getUID());
        // take care , in this situation, request's uid is proposerUID in src
        // node , and
        // in target node , they will be named 'fromUID'. so the first argument
        // at receivePromise
        // is fromUID which is named proposerUID in sendPromise method.
        try {
            // net
            pnode.receiveAccepted(uid, proposalID, acceptedValue);
        } catch (Exception e) {
            System.err.println(e.getCause().getMessage());
        }

    }

    @Override
    public void onResolution(ProposalID proposalID, Object value) {
        System.out.println("onResolution : " + proposalID + ", " + uid);
        // I 'm the master , and the proposal what I sent has been accepted .
        // then I must restore to file.
        // restore to file
        System.err.println("send succ to Client : " + uid);
    }
    

    @Override
    public void sendPrepareNACK(String proposerUID, ProposalID proposalID, ProposalID promisedID) {
        PNode pnode = map.get(proposalID.getUID());
        // take care , in this situation, request's uid is proposerUID in src
        // node , and
        // in target node , they will be named 'fromUID'. so the first argument
        // at receivePromise
        // is fromUID which is named proposerUID in sendPromise method.
        try {
            // net
            pnode.receivePrepareNACK(uid, proposalID, promisedID);
        } catch (Exception e) {
            System.err.println(e.getCause().getMessage());
        }
        System.out.println("sendPrepareNACK : " +proposalID + ", " + uid);
    }

    @Override
    public void sendAcceptNACK(String proposerUID, ProposalID proposalID, ProposalID promisedID) {
        PNode pnode = map.get(proposalID.getUID());
        // take care , in this situation, request's uid is proposerUID in src
        // node , and
        // in target node , they will be named 'fromUID'. so the first argument
        // at receivePromise
        // is fromUID which is named proposerUID in sendPromise method.
        try {
            // net
            pnode.receiveAcceptNACK(uid, proposalID, promisedID);
        } catch (Exception e) {
            System.err.println(e.getCause().getMessage());
        }
        System.out.println("sendPrepareNACK : " +proposalID + ", " + uid);
  
    }

    @Override
    public void onLeadershipAcquired() {
        isMaster = true;
        System.out.println("onLeadershipAcquired : " + uid);
    }

    public boolean put(String nodeName, PNode e) {
        return map.put(nodeName, e) == null;
    }

    public Map<String, PNode> getMap() {
        return map;
    }

}
