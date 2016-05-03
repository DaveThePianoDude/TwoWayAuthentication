package TabularResults;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConvertSQL
{
    private static final Logger logger = 
        Logger.getLogger(ConvertSQL.class.getName());

    public static java.sql.ResultSet getResultSet(ResultSet trrs)
    {
        if (trrs == null)
            throw new NullPointerException(
                "TabularResults.ResultSet object must not be null.");

        return new SqlResultSet(trrs);
    }

    public static ResultSet getResultSet(java.sql.ResultSet rs,
                                         final int startingRow, 
                                         final int endingRow)
        throws SQLException
    {
        if (rs == null)
            throw new NullPointerException("ResultSet must not be null");
        if (startingRow <= 0)
            throw new IllegalArgumentException(
                "staringRow must be greater than zero");
        if (endingRow < startingRow)
            throw new IllegalArgumentException("StartingRow (" + startingRow +
                ") must be less than or equal to endingRow (" + endingRow + ")");

        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ColumnVector[] columnArray = new ColumnVector[columns];
        boolean isJDBC20;
        try
        {
            Class<? extends Object> resultSetClass = rs.getClass();
            Method m = resultSetClass.getMethod("getFetchSize", new Class[0]);
            if (Modifier.isAbstract(m.getModifiers()))
            {
                isJDBC20 = false;
            }
            else
            {
                isJDBC20 = true;
            }
        }
        catch (NoSuchMethodException ex)
        {
            isJDBC20 = false;
        }
        catch (SecurityException ex)
        {
            isJDBC20 = false;
        }

        if ((isJDBC20) && (rs.getType() != 1003))
        {
            rs.setFetchDirection(1000);
        }

        if ((isJDBC20) && (rs.getFetchSize() > 0) && (rs.getFetchSize() < 500))
        {
            rs.setFetchSize(500);
        }

        String cols = "";
        for (int c0 = 0; c0 < columns; ++c0)
        {
            columnArray[c0] = new ColumnVector(md, c0 + 1);
            cols += rs.getMetaData().getColumnName(c0 + 1) + " ";
        }

        if (logger.isLoggable(Level.FINE))
            logger.fine(
                "Retrieving columns: " + cols);

        int rows = 0;
        do
        {
            ++rows;

            if (rows >= startingRow) break;
        }
        while (rs.next());

        if (logger.isLoggable(Level.FINE))
            logger.fine(
                "Starting loop: rows = " + rows + ", endingRow = " + endingRow);

        while (rows - endingRow <= 0 && rs.next())
        {
            for (int c1 = 0; c1 < columns; ++c1)
            {
                columnArray[c1].addElement(rs);
            }

            ++rows;
        }

        if (logger.isLoggable(Level.FINE))
        {
            logger.fine(
                "Ending loop: rows = " + rows + ", endingRow = " + endingRow);
            logger.fine(
                "Exit conditions: rows>end=" + (rows > endingRow));
        }
        
        Column[] trColumnsArray = new Column[columns];

        for (int c1 = 0; c1 < columns; ++c1)
        {
            trColumnsArray[c1] = columnArray[c1].getColumn();
        }

        ResultSet trrs = new ResultSet(rows - startingRow, trColumnsArray);

        return trrs;
    }

    public static ResultSet getResultSet(java.sql.ResultSet rs)
        throws SQLException
    {
        return getResultSet(rs, 1, Integer.MAX_VALUE);
    }

    public static ResultSet getResultSet(java.sql.ResultSet rs, int maxRows)
        throws SQLException
    {
        return getResultSet(rs, 1, maxRows);
    }

    public static ResultSet concat(ResultSet first, ResultSet second)
    {
        return Util.concat(first, second);
    }

    public static ResultSet subset(ResultSet source, int startingRow,
                                   int endingRow)
    {
        return Util.subset(source, startingRow, endingRow);
    }

    public static List getCollection(ResultSet trrs)
    {
        return new TabularResultsListFacade(trrs);
    }

    public static void setOracleDatetimeCompatibility(boolean value)
    {
        ColumnVector.setOracleTimestampCompatibility(value);
    }

    public static boolean getOracleDatetimeCompability()
    {
        return ColumnVector.getOracleTimestampCompatibility();
    }
}
