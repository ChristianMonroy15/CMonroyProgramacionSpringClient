
package com.digi01.CMonroyProgramacionNCapasSpring.ML;

import java.util.List;
import java.util.Objects;

public class Result<T> {

    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public T object;
    public List<T> objects;
    
}
