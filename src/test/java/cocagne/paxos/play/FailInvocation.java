package cocagne.paxos.play;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;

import cocagne.paxos.play.TestLeaderSituation.FailHook;

public class FailInvocation implements InvocationHandler {

    private String  methodName;
    private Boolean throwExceptionInBegin     = true;
    private Object  objOriginal;
    private int exceptionTimes = 0;
    /**
     * for some of response msg , we have to let one of the node can't send a
     * msg to leader . and exception must be handled by Messager itself in our
     * mock test. so in this case, we skip the method to mock those situation.
     */
    private boolean skipMethodToMockException = false;

    public FailInvocation(FailHook failHook, Object objOriginal){
        super();
        this.methodName = failHook.failMethod0;
        this.throwExceptionInBegin = failHook.throwExceptionInBegin;
        this.objOriginal = objOriginal;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (throwExceptionInBegin) {

            if (methodName.equalsIgnoreCase(method.getName())) {
                exceptionTimes ++;
                if (!skipMethodToMockException) 
                {
                    throw new SocketTimeoutException("Socket Time out Exception on method : " + methodName);
                }
                else
                {
                    System.err.println("dummy exception method : " + method.getName());
                }
                
            }

            result = method.invoke(this.objOriginal, args);
        
            

        } else {

            result = method.invoke(this.objOriginal, args);

            if (methodName.equalsIgnoreCase(method.getName())) {
                exceptionTimes ++;
                if (!skipMethodToMockException) {
                    throw new SocketTimeoutException("Socket Time out Exception");
                }
                else
                {
                    exceptionTimes ++;
                    System.err.println("dummy exception method : " + method.getName());
                }
              
               
            }
        
            
        }
        return result;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getThrowExceptionInBegin() {
        return throwExceptionInBegin;
    }

    public void setThrowExceptionInBegin(Boolean throwExceptionInBegin) {
        this.throwExceptionInBegin = throwExceptionInBegin;
    }

    public Object getObjOriginal() {
        return objOriginal;
    }

    public void setObjOriginal(Object objOriginal) {
        this.objOriginal = objOriginal;
    }

    public boolean isSkipMethodToMockException() {
        return skipMethodToMockException;
    }

    public void setSkipMethodToMockException(boolean skipMethodToMockException) {
        this.skipMethodToMockException = skipMethodToMockException;
    }

    
    public int getExceptionTimes() {
        return exceptionTimes;
    }

    
    public void setExceptionTimes(int exceptionTimes) {
        this.exceptionTimes = exceptionTimes;
    }
    

}
