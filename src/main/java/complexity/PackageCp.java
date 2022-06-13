package complexity;

// https://en.wikipedia.org/wiki/Halstead_complexity_measures
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import dataSet.Class;
import dataSet.OMS;

/**
 * Halstead Complex를 패키지 관점에서 측정하는 클래스.<br>
 * 
 * @author thom1
 *
 */
public class PackageCp {
  Map<String, Integer> ceList = new HashMap<String, Integer>();
  Map<String, Integer> caList = new HashMap<String, Integer>();
  Map<String, Float> dList = new HashMap<String, Float>();
  Map<String, Float> abList = new HashMap<String, Float>();
  Map<String, Float> iList = new HashMap<String, Float>();
  Map<String, ArrayList<Class>> list;

  public PackageCp(OMS datalist) {
    list = datalist.separatePackage();
    for (String pkg : list.keySet()) {
      getScore(pkg, list.get(pkg));
    }

  }

  private void getScore(String pkg, ArrayList<Class> arrayList) {
    int ab = 0;
    int co = 0;
    int ce = 0;
    int ca = 0;
    ArrayList<Class> callList;
    ArrayList<Class> callByList;
    // TODO Auto-generated method stub
    for (Class cls : arrayList) {
      if (cls.isAbstract())
        ab++;
      else
        co++;
      callList = cls.getCallList().getClassList();
      callByList = cls.getCallByList().getClassList();

      for (Class caller : callList) {
        if (caller.getPackageName().equals(pkg))
          continue;
        else if (cls.getHasList().getClassList().contains(caller))
          ce++;

      }
      for (Class callBy : callByList) {
        if (callBy.getPackageName().equals(pkg))
          continue;
        else if (callBy.getHasList().getClassList().contains(cls))
          ca++;
      }
    }
    ceList.put(pkg, ce);
    caList.put(pkg, ca);

    if (0 == ce + ca)
      iList.put(pkg, (float) 0.0);
    else {
      float i = ce / (ce + ca);
      iList.put(pkg, i);
    }
    if (ab + co == 0)
      abList.put(pkg, (float) 0.0);
    else
      abList.put(pkg, (float) (ab / (ab + co)));

    float d = abList.get(pkg) + iList.get(pkg) - 1;
    if (d < 0)
      d *= -1;
    dList.put(pkg, d);
  }

  public void printScore() {
    for (String pkg : list.keySet()) {
      System.out.println(pkg + "(ce, ca, I, A, D) : (" + ceList.get(pkg) + ", " + caList.get(pkg)
          + ", " + iList.get(pkg) + ", " + abList.get(pkg) + ", " + dList.get(pkg) + ")");
    }
  }
}
