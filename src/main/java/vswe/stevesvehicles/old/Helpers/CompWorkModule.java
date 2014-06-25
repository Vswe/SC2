package vswe.stevesvehicles.old.Helpers;
import java.util.Comparator;

import vswe.stevesvehicles.old.Modules.Workers.ModuleWorker;

public class CompWorkModule implements Comparator
{
    public int compare(Object obj1, Object obj2)
    {
		ModuleWorker work1 = (ModuleWorker)obj1;
		ModuleWorker work2 = (ModuleWorker)obj2;

       return work1.getWorkPriority() < work2.getWorkPriority() ? -1 : (work1.getWorkPriority() > work2.getWorkPriority() ? 1 : 0);
    }
}