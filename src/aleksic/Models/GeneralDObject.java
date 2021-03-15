
package aleksic.Models;

import java.io.Serializable;
import java.sql.*;

// Operacije navedenog interfejsa je potrebno da implementira svaka od domenskih klasa,
// koja zeli da joj bude omogucena komunikacija sa Database broker klasom.
public abstract class GeneralDObject implements Serializable
{ abstract public String getAtrValue();//{return "";}
  abstract public String setAtrValue();//{return "";}
  abstract public String getClassName();//{return "";}
  abstract public String getWhereCondition();//{return "";}
  abstract public String getNameByColumn(int column);//{return "";}
  abstract public GeneralDObject getNewRecord(ResultSet rs) throws SQLException; //{return null;}
  abstract public int getPrimaryKey();
}
