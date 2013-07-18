package cocagne.paxos.play;

import java.lang.reflect.Proxy;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cocagne.paxos.practical.PNode;
import cocagne.paxos.practical.PracticalMessenger;
import cocagne.paxos.practical.PracticalNode;

public class TestLeaderSituation {

    final String  proposal1 = "proposal1";
    public static class FailHook {

        String  failMethod0           = "";
        boolean throwExceptionInBegin = true;
    }

    PNode node0;
    PNode node1;
    PNode node2;
    FailInvocation node0Fail = null;
    FailInvocation node1Fail = null;
    FailInvocation node2Fail = null;
    
    FailInvocation messager0Fail = null;
    FailInvocation messager1Fail = null;
    FailInvocation messager2Fail = null;
    @Before
    public void prepared() throws Exception {
        Messager messager0_ori = new Messager("0");
        messager0Fail = new FailInvocation(new FailHook(), messager0_ori);
        PracticalMessenger messager0 = (PracticalMessenger) Proxy.newProxyInstance(messager0_ori.getClass()
            .getClassLoader(), messager0_ori.getClass().getInterfaces(), messager0Fail);

       
        Messager messager1_ori = new Messager("1");
        messager1Fail = new FailInvocation(new FailHook(), messager1_ori);
        PracticalMessenger messager1 = (PracticalMessenger) Proxy.newProxyInstance(messager1_ori.getClass()
            .getClassLoader(), messager1_ori.getClass().getInterfaces(), messager1Fail);

        
        Messager messager2_ori = new Messager("2");
        messager2Fail = new FailInvocation(new FailHook(), messager2_ori);
        PracticalMessenger messager2 = (PracticalMessenger) Proxy.newProxyInstance(messager2_ori.getClass()
            .getClassLoader(), messager2_ori.getClass().getInterfaces(), messager2Fail);


        node0 = new PracticalNode(messager0, "0", 2);
        node0Fail = new FailInvocation(new FailHook(), node0);
        node0 = (PNode) Proxy.newProxyInstance(node0.getClass()
            .getClassLoader(), node0.getClass().getInterfaces(), node0Fail);

        node1 = new PracticalNode(messager1, "1", 2);
        node1Fail = new FailInvocation(new FailHook(), node1);
        node1 = (PNode) Proxy.newProxyInstance(node1.getClass()
            .getClassLoader(), node1.getClass().getInterfaces(), node1Fail);

        
        node2 = new PracticalNode(messager2, "2", 2);
        node2Fail = new FailInvocation(new FailHook(), node2);
        node2 = (PNode) Proxy.newProxyInstance(node2.getClass()
            .getClassLoader(), node2.getClass().getInterfaces(), node2Fail);
        
        messager0_ori.put("0", node0);
        messager0_ori.put("1", node1);
        messager0_ori.put("2", node2);

        messager1_ori.put("0", node0);
        messager1_ori.put("1", node1);
        messager1_ori.put("2", node2);

        messager2_ori.put("0", node0);
        messager2_ori.put("1", node1);
        messager2_ori.put("2", node2);

    }

    @After
    public void tearDown() throws Exception {
        node0 = null;
        node1 = null;
        node2 = null;
        node0Fail = null;
        node1Fail = null;
        node2Fail = null;
    }
    
    @Test
    public void testNormal() throws Exception {
        // //Æô¶¯proposer

        node0.prepare();
        node0.setProposal(proposal1);
        Assert.assertEquals(proposal1,node2.getAcceptedValue());
        Assert.assertEquals(proposal1,node1.getAcceptedValue());
    }

    @Test
    public void test_failOnPrepare() throws Exception
    {
        node0Fail.setMethodName("receivePrepare");
        
        node0.prepare();
        node0.setProposal(proposal1);
        Assert.assertEquals(proposal1,node2.getAcceptedValue());
        Assert.assertEquals(proposal1,node1.getAcceptedValue());
        //ensure we just make one exception
        Assert.assertEquals(1, node0Fail.getExceptionTimes());
    }
    

    @Test
    public void test_failOnSendPromise() throws Exception
    {
        messager1Fail.setMethodName("sendPromise");
        messager1Fail.setSkipMethodToMockException(true);
        
        node0.prepare();
        node0.setProposal(proposal1);
        Assert.assertEquals(proposal1,node2.getAcceptedValue());
        Assert.assertEquals(proposal1,node0.getAcceptedValue());
        //ensure we just make one exception
        Assert.assertEquals(1, messager1Fail.getExceptionTimes());
    }
    
    @Test
    public void test_failOnAcceptRequest() throws Exception
    {
        node1Fail.setMethodName("receiveAcceptRequest");
        
        node0.prepare();
        node0.setProposal(proposal1);
        Assert.assertEquals(proposal1,node2.getAcceptedValue());
        Assert.assertEquals(proposal1,node0.getAcceptedValue());
        //ensure we just make one exception
        Assert.assertEquals(1, node1Fail.getExceptionTimes());
    }
    
    @Test
    public void test_failOnAccepted() throws Exception
    {
        messager1Fail.setMethodName("sendAccepted");
        messager1Fail.setSkipMethodToMockException(true);
        
        node0.prepare();
        node0.setProposal(proposal1);
        Assert.assertEquals(proposal1,node2.getAcceptedValue());
        Assert.assertEquals(proposal1,node0.getAcceptedValue());
        //ensure we just make one exception
        Assert.assertEquals(1, messager1Fail.getExceptionTimes());
    }
}
