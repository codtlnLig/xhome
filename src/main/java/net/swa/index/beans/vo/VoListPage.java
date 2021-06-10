
package net.swa.index.beans.vo;

import java.util.ArrayList;
import java.util.List;

/**分页对象**/
public class VoListPage<T>
{
    //是否有next
    private boolean hasNext;

    private boolean success;

    private List<T> result;

    /**当前最大的id**/
    private Object currentId;

    private int totalSize;

    private String message;

    private Integer errorCode;

    public VoListPage()
    {
        this.totalSize = 0;
        this.result = new ArrayList<T>();
        this.success = true;
    }

    public boolean isHasNext()
    {
        return hasNext;
    }

    public void setHasNext(boolean hasNext)
    {
        this.hasNext = hasNext;
    }

    public List<T> getResult()
    {
        return result;
    }

    public void setResult(List<T> result)
    {
        this.result = result;
    }

    public int getTotalSize()
    {
        return totalSize;
    }

    public void setTotalSize(int totalSize)
    {
        this.totalSize = totalSize;
    }

    public Object getCurrentId()
    {
        return currentId;
    }

    public void setCurrentId(Object currentId)
    {
        this.currentId = currentId;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Integer getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode)
    {
        this.errorCode = errorCode;
    }

    public void setAuthError()
    {
        this.errorCode = 0;
        this.success = false;
        this.message = "非法访问，访问凭证无效";
    }
}