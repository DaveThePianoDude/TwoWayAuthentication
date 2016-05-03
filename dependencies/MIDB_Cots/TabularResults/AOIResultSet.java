package TabularResults;

import java.util.List;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import mil.dia.midb.mdal.gmi.search.util.AOI;
import mil.dia.midb.mdal.gmi.search.util.Coord;

public class AOIResultSet extends TabularResults.SqlResultSet
{
    /** Number of extra columns needed for simple SQL queries (ILAT/ILON) */
    private static final int NUM_COORD_COLS = 2;

    /** Raw results sets.  This is needed to create a AOIMetaData object. */
    private TabularResults.ResultSet trs;

    /** List of Areas of Interest with their related conjunctions */
    private List aoiCons;

    /** Column number of ILAT column */
    private int ilatCol;
    /** Column number of ILON column */
    private int ilonCol;

    /**
	* Create the result set based on a tabular result set and
	* an Area of Interest used for the detailed point containment
	* checking.
	*
	* @param trs ResultSet to be post-filtered
	* @param aoi Area of Interest that must contain ILAT/ILON pair
	*     if row is to be returned.
	* @throws SQLException if ILAT/ILON columns cannot be found for
	*     the result set.
	*/
    public AOIResultSet(TabularResults.ResultSet trs, List aoiCons)
	   throws SQLException
    {
	   super(trs);

	   this.trs = trs;
	   this.aoiCons = aoiCons;

	   setIlatIlonCols();
    }

    /**
	* This method iterates through the ResultSet to find a row that
	* is contained in the current Area of Interest
	*
	* @return true is another row was found.
	*/
    public boolean next()
	   throws SQLException
    {
	   boolean found = false;

	   while(!found && super.next())
	   {
		  Coord pt = getCoordFromRow();
		  if (matches(pt))
			 found = true;
	   }

	   return found;
    }

    /**
	* This method determines whether the given point is trully
	* found within the complex AOI described by the aoiCons
	* structure.  The conjuctions in the aoiCons list will be
	* evaluated using left to right associativity.
	*
	* @param pt Point to check
	* @return true if the point is contained
	*/
    private boolean matches(Coord pt)
    {
	   List entry = (List) aoiCons.get(0);
	   AOI  aoi = (AOI) entry.get(0);
	   String conjunctive = (String) entry.get(1);
	   boolean lastResult = aoi.containsPoint(pt);

	   // Already did first AOI
	   for (int i = 1 ; i < aoiCons.size() ; i++ )
	   {
		  entry = (List) aoiCons.get(i);
		  conjunctive = (String) entry.get(1);

		  if (conjunctive.toUpperCase().equals("OR"))
		  {
			 if (lastResult == true)
				continue;
		  }
		  else if (conjunctive.toUpperCase().equals("AND"))
		  {
			 if (lastResult == false)
				continue;
		  }

		  // Otherwise, there's no short circuit eval, and we
		  // have to check if the next AOI contains the point.
		  aoi = (AOI) (entry.get(0));
		  lastResult = aoi.containsPoint(pt);
	   }

	   return lastResult;
    }


    /**
	* This method returns a MetaData object that contains information
	* about the columns returned
	* @return The relevant MetaData
	*/
    public ResultSetMetaData getMetaData() throws SQLException
    {
        return (ResultSetMetaData) new AOIMetaData(this.trs.getColumns());
    }

    /**
	* This method validates that the ILAT column is the penultimate
	* row of the current result set and the ILON column is the ultimate.
	*
	* @throws SQLException if there was a problem retrieving the MetaData
	*/
	private void setIlatIlonCols()
	   throws SQLException
    {
	   ResultSetMetaData rsmd = super.getMetaData();

	   String thisCol = rsmd.getColumnName(rsmd.getColumnCount() - 1);

	   if (thisCol.endsWith("ILAT"))
		  this.ilatCol = rsmd.getColumnCount() - 1;
	   else
		  throw new SQLException("Can't find ILAT column");

	   thisCol = rsmd.getColumnName(rsmd.getColumnCount());
	   if (thisCol.endsWith("ILON"))
		  this.ilonCol = rsmd.getColumnCount();
	   else
		  throw new SQLException("Can't find ILON column");
    }

    /**
	* Get the (ILAT, ILON) coordinate from the current row
	*
	* @return a Coord object with the currents rows position
	*/
    private Coord getCoordFromRow()
	   throws SQLException
    {
	   return new Coord(super.getInt(ilatCol), super.getInt(ilonCol));
    }

    /**
	* This class is used to return the MetaData of the query without
	* the ILAT/ILON columns that were implicitly added to allow for
	* AOI post-filtering.
	*/
    private class AOIMetaData extends SqlMetaData
    {
	   /** Create a new AOIMetaData object */
	   public AOIMetaData(TabularResults.Column[] columns)
	   {
		  super(columns);
	   }

	   /**
	    * Return the "real" number of columns requested, i.e.
	    * the number of columns returned less the 2 ILAT/ILON
	    * columns.
	    *
	    * @return Number of user columns
	    */
	   public int getColumnCount()
		  throws SQLException
	   {
		  return super.getColumnCount() - NUM_COORD_COLS;
	   }
    }


}
